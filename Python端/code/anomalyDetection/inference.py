import contextlib
import logging
import os
import sys
from anomalyDetection.utils import create_logger
import numpy as np
import torch

import anomalyDetection.backbones
import anomalyDetection.common
import anomalyDetection.metrics
import anomalyDetection.feat_extractor
import anomalyDetection.sampler
import anomalyDetection.utils
from anomalyDetection.wscxr import WSCXR
from datetime import datetime
from anomalyDetection.datasets.dataset import MedDataset, MedAbnormalDataset
import tqdm
import warnings
import PIL
from pytorch_grad_cam.utils.image import show_cam_on_image
import torch.nn.functional as F
import argparse
from easydict import EasyDict
import yaml
from torchvision import transforms
from PIL import Image
import json
import io
import random
import base64

IMAGENET_MEAN = [0.485, 0.456, 0.406]
IMAGENET_STD = [0.229, 0.224, 0.225]


def create_wscxr_instance(
        backbone_name,
        layers_to_extract_from,

        pretrain_embed_dimension,
        target_embed_dimension,

        patchsize,
        meta_epochs,
        gan_epochs,

        dsc_layers,
        dsc_hidden,
        dsc_margin,
        dsc_lr,

):
    def get_wscxr(input_shape, device):
        backbone_seed = None
        backbone = anomalyDetection.backbones.load(backbone_name)
        backbone.name, backbone.seed = backbone_name, backbone_seed

        wscxr_inst = WSCXR(device)

        wscxr_inst.load(
            backbone=backbone,
            layers_to_extract_from=layers_to_extract_from,
            device=device,
            input_shape=input_shape,
            pretrain_embed_dimension=pretrain_embed_dimension,
            target_embed_dimension=target_embed_dimension,

            patchsize=patchsize,
            meta_epochs=meta_epochs,
            gan_epochs=gan_epochs,

            dsc_layers=dsc_layers,
            dsc_hidden=dsc_hidden,
            dsc_margin=dsc_margin,
            dsc_lr=dsc_lr,
        )

        return wscxr_inst

    return get_wscxr


def normalization(segmentations, avgpool_size=64):
    segmentations_ = torch.tensor(segmentations[:, None, ...]).cuda()  # N x 1 x H x W
    segmentations_ = F.avg_pool2d(segmentations_, (avgpool_size, avgpool_size), stride=1).cpu().numpy()

    min_scores = (
        segmentations_.reshape(-1).min(axis=-1).reshape(1)
    )

    max_scores = (
        segmentations_.reshape(-1).max(axis=-1).reshape(1)
    )

    segmentations = (segmentations - min_scores) / (max_scores - min_scores)
    segmentations = np.clip(segmentations, a_min=0, a_max=1)

    return segmentations


# def dataset(
#         data_path,
#         batch_size,
#         imagesize,
#         num_workers,
#         max_normal,
# ):
#     def get_dataloaders(seed):
#         test_dataset = MedDataset(
#             data_path,
#             imagesize=imagesize,
#             split='test',
#             normal_only=False,
#             seed=seed,
#         )
#
#         print("test:{}".format(len(test_dataset)))
#
#         test_dataloader = torch.utils.data.DataLoader(
#             test_dataset,
#             batch_size=batch_size,
#             shuffle=False,
#             num_workers=num_workers,
#             pin_memory=True,
#         )
#
#         test_dataloader.name = '{}_test'.format(args.dataset_name)
#
#         dataloader_dict = {
#             "test": test_dataloader
#         }
#
#         return dataloader_dict
#
#     return get_dataloaders


def main(args, input_image, transformed_image):
    device = anomalyDetection.utils.set_torch_device(args.gpu)
    device_context = (
        torch.cuda.device("cuda:{}".format(device.index))
        if "cuda" in device.type.lower()
        else contextlib.suppress()
    )
    anomalyDetection.utils.fix_seeds(args.seed, device)

    image_size = (3, args.config.imagesize, args.config.imagesize)
    with device_context:
        WSCXR = create_wscxr_instance(
            args.config.backbone_name,
            args.config.layers_to_extract_from,
            args.config.pretrain_embed_dimension,
            args.config.target_embed_dimension,
            args.config.patchsize,
            args.config.meta_epochs,
            args.config.gan_epochs,
            args.config.dsc_layers,
            args.config.dsc_hidden,
            args.config.dsc_margin,
            args.config.dsc_lr,

        )(image_size, device)

        #discriminator_path = os.path.join(args.config.results_path, args.dataset_name, args.config.dsc_save_path)
        discriminator_path = args.config.dsc_save_path
        print("load discriminator path: {}".format(discriminator_path))
        WSCXR.discriminator.load_state_dict(torch.load(discriminator_path))

        if WSCXR.backbone.seed is not None:
            anomalyDetection.utils.fix_seeds(WSCXR.backbone.seed, device)

        torch.cuda.empty_cache()

        # print(type(input_image))
        transformed_image = transformed_image.unsqueeze(0)
        # print(input_image.shape)

        scores, segmentations = WSCXR.predict(transformed_image)

        segmentations = normalization(np.array(segmentations))

        save_images_root = os.path.join(args.config.results_path, args.dataset_name, "seg_images")
        os.makedirs(save_images_root, exist_ok=True)

        image = np.array(input_image).astype(np.uint8)
        heat = show_cam_on_image(image / 255, segmentations[0], use_rgb=True)

        img_buffer = io.BytesIO()
        PIL.Image.fromarray(heat).save(img_buffer, format="PNG")
        encoded_image = base64.b64encode(img_buffer.getvalue()).decode('utf-8')

        return scores,encoded_image
        # _, image_name = os.path.split(image_path)
        # PIL.Image.fromarray(heat).save(os.path.join(save_images_root, image_name))


def transform_image(origin_image, imagesize):
    transform_img = [
        transforms.Resize((imagesize, imagesize), Image.BILINEAR),
        transforms.ToTensor(),
        transforms.Normalize(mean=IMAGENET_MEAN, std=IMAGENET_STD),
    ]
    transform_img = transforms.Compose(transform_img)
    transformed_image = transform_img(origin_image)
    return transformed_image


#if __name__ == "__main__":
def GetAnomalyDetection(image):
    parser = argparse.ArgumentParser(description="WSCXR")

    parser.add_argument("--gpu", type=int, default=[0])
    parser.add_argument("--seed", type=int, default=0)

    parser.add_argument("--dataset_name", default='zhanglab', type=str,
                        choices=['zhanglab', 'chexpert12'])

    parser.add_argument("--faiss_on_gpu", type=bool, default=False)
    parser.add_argument("--faiss_num_workers", type=int, default=8)

    args = parser.parse_args()

    with open(os.path.join("anomalyDetection/config", "{}.yaml".format(args.dataset_name))) as f:
        args.config = EasyDict(yaml.load(f, Loader=yaml.FullLoader))


    input_image = image.convert("RGB")
    resized_image = input_image.resize((args.config.imagesize, args.config.imagesize), PIL.Image.BILINEAR)

    transformed_image = transform_image(resized_image, args.config.imagesize)

    scores, encoded_image = main(args, input_image=resized_image, transformed_image=transformed_image)

    # 构建包含图像数据和检查结果的JSON对象
    detection_result = {
        '异常分数': scores,
        "image_data": encoded_image,
    }
    detection_result['异常分数'] = str(detection_result['异常分数'][0])
    print(detection_result)
    return json.dumps(detection_result)