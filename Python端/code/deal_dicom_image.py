import os
import json

import numpy as np
import pydicom
from PIL import Image
from reportGeneration.reportGeneration import getReportGeneration

def deal_dicom_image(file_path, report_file):
    # 读取DICOM文件
    ds = pydicom.dcmread(file_path)

    # 提取图像数据
    image_data = ds.pixel_array

    # 调整像素值范围到0-255
    image_data = ((image_data - np.min(image_data)) / (np.max(image_data) - np.min(image_data)) * 255).astype(np.uint8)

    # 将NumPy数组转换为PIL图像
    image = Image.fromarray(image_data)

    image.show()

    # 获取医疗报告
    medical_report_json = getReportGeneration(image).encode('utf-8')
    medical_report = json.loads(medical_report_json)

    # 保存报告到文件
    report_data = {
        "file_name": file_path,
        "report": medical_report
    }

    with open(report_file, 'a', encoding='utf-8') as f:
        json.dump(report_data, f, ensure_ascii=False)
        f.write('\n')

    print(f"Processed {file_path}")

def process_dicom_directory(directory_path, report_file):
    # 确保报告文件是空的
    open(report_file, 'w').close()

    # 遍历目录中的所有文件
    for root, dirs, files in os.walk(directory_path):
        for file in files:
            if file.lower().endswith('.dcm'):
                file_path = os.path.join(root, file)
                deal_dicom_image(file_path, report_file)

# 示例：指定DICOM文件目录路径和报告文件路径
dicom_directory_path = 'dicom'
report_file_path = 'dicom/medical_reports.json'

# 调用函数处理目录中的所有DICOM文件并保存报告
process_dicom_directory(dicom_directory_path, report_file_path)
