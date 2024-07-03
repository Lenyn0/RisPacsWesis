# ======= import module =======
import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision
from reportGeneration.models import CNN, MVCNN, TNN, Classifier, Generator, ClsGen, ClsGenInt
from reportGeneration.utils import save, load, train, test, data_to_device, data_concatenate
from PIL import Image, ImageFile
import torchvision.transforms as transforms
import numpy as np
import sentencepiece as spm
import os
import time
import json
import logging

def getReportGeneration(image):

    # 获取在main.py中定义的日志记录器
    logger = logging.getLogger()

    # ======= define variable =======
    # 读取demo_config.json配置文件
    demo_config = json.load(open('reportGeneration/demo_config.json', 'r'))

    model_path = demo_config['model_path']
    BACKBONE_NAME = demo_config['BACKBONE_NAME']
    MODEL_NAME = demo_config['MODEL_NAME']
    chinese_vocab = demo_config['chinese_vocab']
    # 读入chinese_vocab json文件, utf-8编码
    chinese_vocab = json.load(open(chinese_vocab, 'r', encoding='utf-8'))
    id2token = chinese_vocab['id2token']
    token2id = chinese_vocab['token2id']
    VOCAB_SIZE = chinese_vocab['vocab_size']
    POSIT_SIZE = chinese_vocab['vocab_size']
    max_len = demo_config['max_len']
    NUM_LABELS = demo_config['NUM_LABELS']
    NUM_CLASSES = demo_config['NUM_CLASSES']
    input_size = demo_config['input_size']
    # input_size是list, 需要转换成tuple
    input_size = tuple(input_size)
    max_views = demo_config['max_views']
    device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')
    # device = 'cpu'
    threshold = demo_config['threshold']

    # ======= data loader =======
    # data = {
    #     'id':'01220112303204',
    #     'report':'右肺野清晰，左肺见半月形透亮无肺纹理区，肺组织压缩约70%，肺组织内见斑片状高密度影。肺门影不大，纵隔不宽，心影大小形态未见异常，两膈光滑，肋膈角锐利。扫及左侧锁骨断裂、断端错位。',
    #     'history':'',
    #     'labels':'肺渗出性病变, 气胸/液气胸, 急性骨折',
    #     'img_path':'/root/autodl-tmp/Chinese_ChestXray_dataset/Chinese_ChestXray_PNG/04220111312022/PA.png',
    # }
    # data = {
    #     'img_path': 'img/PA.png',
    # }

    # ======= main function =======

    if BACKBONE_NAME == 'DenseNet121':
        backbone = torchvision.models.densenet121(pretrained=True)
        FC_FEATURES = 1024
    elif BACKBONE_NAME == 'DenseNet201':
        backbone = torchvision.models.densenet201(pretrained=True)
        FC_FEATURES = 1920
    else:
        raise ValueError('BACKBONE_NAME must be one of [DenseNet121, DenseNet201]')

    if MODEL_NAME == 'ClsGen':
        LR = 3e-4  # Fastest LR
        WD = 1e-2  # Avoid overfitting with L2 regularization
        DROPOUT = 0.1  # Avoid overfitting
        NUM_EMBEDS = 256
        FWD_DIM = 256
        NUM_HEADS = 8
        NUM_LAYERS = 1
        cnn = CNN(backbone, BACKBONE_NAME)
        cnn = MVCNN(cnn)
        tnn = TNN(embed_dim=NUM_EMBEDS, num_heads=NUM_HEADS, fwd_dim=FWD_DIM, dropout=DROPOUT, num_layers=NUM_LAYERS,
                  num_tokens=VOCAB_SIZE, num_posits=POSIT_SIZE)

        # Not enough memory to run 8 heads and 12 layers, instead 1 head is enough
        NUM_HEADS = 1
        NUM_LAYERS = 12
        cls_model = Classifier(num_topics=NUM_LABELS, num_states=NUM_CLASSES, cnn=cnn, tnn=tnn, fc_features=FC_FEATURES,
                               embed_dim=NUM_EMBEDS, num_heads=NUM_HEADS, dropout=DROPOUT)
        gen_model = Generator(num_tokens=VOCAB_SIZE, num_posits=POSIT_SIZE, embed_dim=NUM_EMBEDS, num_heads=NUM_HEADS,
                              fwd_dim=FWD_DIM, dropout=DROPOUT, num_layers=NUM_LAYERS)

        ignore_index = 3
        bos_id = 1
        eos_id = 2
        pad_id = 3
        model = ClsGen(cls_model, gen_model, NUM_LABELS, NUM_EMBEDS, bos_id=bos_id, eos_id=eos_id, pad_id=pad_id)

    model = nn.DataParallel(model).to(device=device)
    last_epoch, (best_metric, test_metric) = load(model_path, model)  # Reload
    # 如果device是cpu, 需要把模型的参数转换成cpu
    if device == 'cpu':
        # 去除DataParallel
        model = model.module
        model.cpu()
        # 把所有参数转换成cpu
        for param in model.parameters():
            param = param.cpu()

    #print('Reload From: {} | Last Epoch: {} | Validation Metric: {} | Test Metric: {}'.format(model_path, last_epoch,best_metric, test_metric))
    logger.info('Reload From: {} | Last Epoch: {} | Validation Metric: {} | Test Metric: {}'.format(model_path, last_epoch,best_metric, test_metric))

    transform = transforms.Compose([transforms.Resize(input_size), transforms.ToTensor()])

    model.eval()
    # 打印现在时间
    start_time = time.time()
    #print('Start Time: {}'.format(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time()))))
    logger.info('Start Time: {}'.format(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time()))))
    outputs = []
    targets = []
    with torch.no_grad():
        # ======= load data =======
        sources = []
        imgs, vpos = [], []  # vpos: view position, 记录视图的
        # img_file = data['img_path']
        # img = Image.open(img_file).convert('RGB')
        img=image.convert('RGB')
        imgs.append(transform(img).unsqueeze(0))  # (1,C,W,H)
        vpos.append(0)

        imgs = torch.cat(imgs, dim=0)  # (V,C,W,H)
        vpos = np.array(vpos, dtype=np.int64)  # (V)

        source_info = [token2id['[BOS]'], token2id['[EOS]']]
        # 添加[PAD]到self.max_len长度
        source_info = source_info + [token2id['[PAD]']] * (max_len - len(source_info))
        # np封装
        source_info = np.array(source_info, dtype=np.int64)  # (L)

        # 拼装数据
        # 对imgs添加一个batch维度
        imgs = imgs.unsqueeze(0)  # (1,V,C,W,H)
        # 对vpos添加一个batch维度
        vpos = torch.from_numpy(vpos).unsqueeze(0)  # (1,V)
        # 对source_info添加一个batch维度
        source_info = torch.from_numpy(source_info).unsqueeze(0)  # (1,L)

        sources.append((imgs, vpos))  # (V,C,W,H), (V) / (2,V,C,W,H), (2,V)
        sources.append(source_info)

        # ======= forward =======
        source = data_to_device(sources, device)
        output = model(image=source[0], history=source[1], threshold=threshold)

        # ======= 保存结果 =======
        gen_outputs = output[0][0]  # (L)

        candidate = ''
        for j in range(len(gen_outputs)):
            tok = chinese_vocab['id2token'][str(int(gen_outputs[j]))]
            if tok == '</s>' or tok == '[EOS]':
                break  # Manually stop generating token after </s> is reached
            elif tok == '<s>' or tok == '[BOS]':
                continue
            elif tok == '▁':  # space
                if len(candidate) and candidate[-1] != ' ':
                    candidate += ' '
            elif tok in [',', '.', '-', ':']:  # or not tok.isalpha():
                if len(candidate) and candidate[-1] != ' ':
                    candidate += ' ' + tok + ' '
                else:
                    candidate += tok + ' '
            else:  # letter
                candidate += tok

        # print('Generated Text: {}'.format(candidate))
        # # 打印现在时间
        # print('Time: {}'.format(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())))
        # # 打印耗时
        # # cpu: 14 s
        # # cuda: 3 s
        # print('Time Cost: {}'.format(time.time() - start_time))

        logger.info('Generated Text: {}'.format(candidate))
        logger.info('Time: {}'.format(time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())))
        logger.info('Time Cost: {}'.format(time.time() - start_time))

        # # 使用时间戳命名
        # TEXT_FILE = os.path.join('outputs', 'text_{}.txt'.format(int(time.time())))
        # out_file_hyp = open(TEXT_FILE, 'w')
        # out_file_hyp.write(candidate + '\n')
        # # 刷新缓冲区
        # out_file_hyp.flush()
        # # 关闭文件
        # out_file_hyp.close()

        # print('Generated Text: {}'.format(candidate))
        # print('Saved to: {}'.format(TEXT_FILE))

        medical_report = {
            '报告生成结果':candidate
        }
        return json.dumps(medical_report,ensure_ascii=False)
