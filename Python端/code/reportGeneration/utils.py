import torch
import torch.nn as nn
import torch.nn.functional as F
from sklearn.manifold import TSNE
from tqdm.auto import tqdm
import matplotlib.pyplot as plt
import os
import csv
import time
# ------ Helper Functions ------
def data_to_device(data, device='cpu'):
    if isinstance(data, torch.Tensor):
        # data = data.cuda()
        data = data.to(device)
    elif isinstance(data, tuple):
        data = tuple(data_to_device(item,device) for item in data)
    elif isinstance(data, list):
        data = list(data_to_device(item,device) for item in data)
    elif isinstance(data, dict):
        data = dict((k,data_to_device(v,device)) for k,v in data.items())
    else:
        raise TypeError('Unsupported Datatype! Must be a Tensor/List/Tuple/Dict.')
    return data

def data_concatenate(iterable_data, dim=0):
    data = iterable_data[0] # can be a list / tuple / dict / tensor
    if isinstance(data, torch.Tensor):
        return torch.cat([*iterable_data], dim=dim)
    elif isinstance(data, tuple):
        num_cols = len(data)
        num_rows = len(iterable_data)
        return_data = []
        for col in range(num_cols):
            data_col = []
            for row in range(num_rows):
                data_col.append(iterable_data[row][col])
            return_data.append(torch.cat([*data_col], dim=dim))
        return tuple(return_data)
    elif isinstance(data, list):
        num_cols = len(data)
        num_rows = len(iterable_data)
        return_data = []
        for col in range(num_cols):
            data_col = []
            for row in range(num_rows):
                data_col.append(iterable_data[row][col])
            return_data.append(torch.cat([*data_col], dim=dim))
        return list(return_data)
    elif isinstance(data, dict):
        num_cols = len(data)
        num_rows = len(iterable_data)
        return_data = []
        for col in data.keys():
            data_col = []
            for row in range(num_rows):
                data_col.append(iterable_data[row][col])
            return_data.append(torch.cat([*data_col], dim=dim))
        return dict((k,return_data[i]) for i,k in enumerate(data.keys()))
    else:
        raise TypeError('Unsupported Datatype! Must be a Tensor/List/Tuple/Dict.')

def data_distributor(model, source):
    if isinstance(source, torch.Tensor):
        output = model(source)
    elif isinstance(source, tuple) or isinstance(source, list):
        output = model(*source)
    elif isinstance(source, dict): #在clsGen模型使用,因为source是一个字典
        output = model(**source) #output(B,T,C);(B,T,2); 文本分类器{input: findings, output: 114_labels}
    else:
        raise TypeError('Unsupported DataType! Try List/Tuple!')
    return output
    
def args_to_kwargs(args, kwargs_list=None): # This function helps distribute input to corresponding arguments in Torch models
    if kwargs_list != None:
        if isinstance(args, dict): # Nothing to do here
            return args 
        else: # args is a list or tuple or single element
            if isinstance(args, torch.Tensor): # single element
                args = [args]
            assert len(args) == len(kwargs_list)
            return dict(zip(kwargs_list, args))
    else: # Nothing to do here
        return args

def tsne_plot(last_hidden_states, reports_ids):
    # last_hidden_states(batch, seq_len-1, 512)
    # reports_ids(batch, seq_len)
    
    # 对隐状态向量进行t-SNE降维
    tsne = TSNE(n_components=2, random_state=0,metric='cosine')
    
    for i in range(last_hidden_states.shape[0]):
        hidden_states = last_hidden_states[i].view(-1, 256) # (seq_len-1, 512)
        report_ids = reports_ids[i][1:] # (seq_len-1)
        # 统计report_ids非0的个数
        count = 0
        for j in range(report_ids.shape[0]):
            if report_ids[j] != 3:
                count += 1
        count += 1 # 加上[END]的token; 41
        # 将隐状态向量展开成2D numpy数组
        hidden_states_np = hidden_states.detach().cpu().numpy()[:count, :] # (41, 512)
        hidden_states_tsne = tsne.fit_transform(hidden_states_np)
        
        # 按照序号对点进行分组
        group_ids = [i // 10 for i in range(count)] # (41,)

        # 绘制降维后的二维散点图，并标出每个点对应的序号
        fig, ax = plt.subplots()
        ax.scatter(hidden_states_tsne[:, 0], hidden_states_tsne[:, 1], c=group_ids) #
        for j, txt in enumerate(range(count)):
            ax.annotate(txt, (hidden_states_tsne[j, 0], hidden_states_tsne[j, 1]))
        # plt.savefig('results/tsne/'+images_id[i]+'.png')
        if not os.path.exists('results/tnse'):
            os.makedirs('results/tnse')
        # 保存图片,添加时间戳
        plt.savefig('results/tnse/'+str(time.time())+'.png')
        # 把reports_ids的内容值也保存到csv文件中
        my_list = reports_ids[i][1:].tolist()
        # 写入 CSV 文件
        # csv_file = 'results/tnse/'+images_id[i]+'.csv'
        # with open(csv_file, mode='w', newline='') as file:
        #     writer = csv.writer(file)
        #     writer.writerow(['Index', 'Value'])
        #     for index, value in enumerate(my_list):
        #         writer.writerow([index, value])
        if i==2:
            exit()
# ------ Core Functions ------
def train(epoch,data_loader, model, optimizer, criterion, scheduler=None, device='cpu', kw_src=None, kw_tgt=None, kw_out=None, scaler=None, contrastive=False):
    model.train()
    re_loss_dict = {'total_loss':0, 'ce_loss_shift':0, 'ce_loss_1':0, 'ce_loss_2':0, 'sup_loss':0}
    prog_bar = tqdm(data_loader, dynamic_ncols=True)
    for i, (source, target) in enumerate(prog_bar): # 在分类模型时,source只包含caption,而target只包含label;classifier+generator时,source包含img,caption,label,history,而target包含caption和label
        source = data_to_device(source, device) #sources包含了image,target_info,np_labels,source_info, img(8,2,2,3,256,256)
        target = data_to_device(target, device) #target(B,2);targets包含了 target_info,np_labels

        source = args_to_kwargs(source, kw_src) #把list转换成dict
        target = args_to_kwargs(target, kw_tgt)

        if scaler != None:
            with torch.cuda.amp.autocast():
                output = data_distributor(model, source) #calssifier[(B,T,C);(B,T,2)]; calssifier+generator[(B,L,L);(B,T,C)];classifier+generator+interaptor模型[(B,L,L);(B,T,C);(B,T,C)]
                output = args_to_kwargs(output, kw_out) #无操作
                # 文本分类器阶段: target(batch,calss_num), output(batch,calss_num,2),loss=0.7121, output[,,0]是预测为0的概率, output[,,1]是预测为1的概率, 因为是从attention中得到的, 直接就是概率
                   # ClsGen: train: output[0](batch,length,vocab),output[1](batch,class_num,2), output[2](batch,length,vocab), target[0](batch,length), target[1](batch,class_num), loss=7.7559
                loss_dict = criterion(output, target,epoch) # sub_mimic_14类文本分类器:0.6778; sub_mimic_125类文本分类器: 0.6866; sub_mimic_114类分类器:0.7133
                # tsne_plot(output[3], target[0])
                # ClsGenInt: train: output[0](batch,length,vocab),output[1](batch,class_num,2),output[2](batch,class_num,2),output[3](batch,length,256) target[0](batch,length), target[1](batch,class_num), loss=1.3006
            # 遍历loss_dict
            for key in loss_dict:
                re_loss_dict[key] += loss_dict[key].item()
                
            
            # 获取total_loss
            running_loss = re_loss_dict['total_loss']
            # prog_bar.set_description('Loss: {}'.format(loss/(i+1)))
            prog_bar.set_description('Loss: {}'.format(running_loss/(i+1)))

            # Back-propagate and update weights
            loss = loss_dict['total_loss']
            optimizer.zero_grad()
            scaler.scale(loss).backward()
            scaler.step(optimizer)
            scaler.update()
            if scheduler != None:
                scheduler.step()
        else:
            output = data_distributor(model, source)
            output = args_to_kwargs(output, kw_out)
            loss = criterion(output, target)

            running_loss += loss.item()
            prog_bar.set_description('Loss: {}'.format(running_loss/(i+1)))

            # Back-propagate and update weights
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()
            if scheduler != None:
                scheduler.step()
    # 遍历re_loss_dict, 把所有的key都添加'train_'前缀
    keys = list(re_loss_dict.keys())
    for key in keys:
        re_loss_dict['train_'+key] = re_loss_dict.pop(key)
    for key in re_loss_dict:
        re_loss_dict[key] /= len(data_loader)
    
    return re_loss_dict
    # if contrastive==True:
    #     return running_loss / len(data_loader), ce_loss_shift / len(data_loader), ce_loss_1 / len(data_loader), ce_loss_2 / len(data_loader), sup_loss / len(data_loader)
    # else:
    #     return running_loss / len(data_loader)

def test(epoch,data_loader, model, criterion=None, device='cpu', return_results=True, kw_src=None, kw_tgt=None, kw_out=None, select_outputs=[], contrastive=False):
    model.eval()
    running_loss = 0
    outputs = []
    targets = []
    re_loss_dict = {'total_loss':0,'ce_loss_shift':0,'ce_loss_1':0,'ce_loss_2':0,'sup_loss':0}

    with torch.no_grad():
        prog_bar = tqdm(data_loader, dynamic_ncols=True)
        for i, (source, target) in enumerate(prog_bar):
            source = data_to_device(source, device)
            target = data_to_device(target, device)

            source = args_to_kwargs(source, kw_src)
            target = args_to_kwargs(target, kw_tgt)

            output = data_distributor(model, source)
            output = args_to_kwargs(output, kw_out)

            if criterion != None:
                loss_dict = criterion(output, target,epoch)
                # 遍历loss_dict
                for key in loss_dict:
                    re_loss_dict[key] += loss_dict[key].item()

            running_loss = re_loss_dict['total_loss']
            prog_bar.set_description('Loss: {}'.format(running_loss/(i+1)))

            if return_results:
                if len(select_outputs) == 0:
                    outputs.append(data_to_device(output,'cpu'))
                    targets.append(data_to_device(target,'cpu'))
                else:
                    list_output = [output[row] for row in select_outputs]
                    list_target = [target[row] for row in select_outputs]
                    outputs.append(data_to_device(list_output if len(list_output) > 1 else list_output[0],'cpu'))
                    targets.append(data_to_device(list_target if len(list_target) > 1 else list_target[0],'cpu'))
    # 遍历re_loss_dict, 把所有的key都添加'test_'前缀
    keys = list(re_loss_dict.keys())
    for key in keys:
        re_loss_dict['test_'+key] = re_loss_dict.pop(key)
    for key in re_loss_dict:
        re_loss_dict[key] /= len(data_loader)
    if return_results:
        outputs = data_concatenate(outputs)
        targets = data_concatenate(targets)
        return re_loss_dict, outputs, targets
    else:
        return re_loss_dict

def save(path, model, optimizer=None, scheduler=None, epoch=-1, stats=None):
    path = path[:-3] + '_epoch{}.pt'.format(epoch) if epoch != -1 else path
    print('Saving model to {}'.format(path))
    torch.save({
        # --- Model Statistics ---
        'epoch': epoch,
        'stats': stats,
        # --- Model Parameters ---
        'model_state_dict': model.state_dict(),
        'optimizer_state_dict': optimizer.state_dict() if optimizer != None else None,
        'scheduler_state_dict': scheduler.state_dict() if scheduler != None else None,
    }, path)

def load(path, model, optimizer=None, scheduler=None):
    checkpoint = torch.load(path)
    # --- Model Statistics ---
    epoch = checkpoint['epoch']
    stats = checkpoint['stats']
    # --- Model Parameters ---
    model.load_state_dict(checkpoint['model_state_dict'])
    if optimizer != None:
        try:
            optimizer.load_state_dict(checkpoint['optimizer_state_dict'])
        except: # Input optimizer doesn't fit the checkpoint one --> should be ignored
            print('Cannot load the optimizer')
    if scheduler != None:
        try:
            scheduler.load_state_dict(checkpoint['scheduler_state_dict'])
        except: # Input scheduler doesn't fit the checkpoint one --> should be ignored
            print('Cannot load the scheduler')
    return epoch, stats
