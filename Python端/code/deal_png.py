import os
import json

import numpy as np
from PIL import Image
from reportGeneration.reportGeneration import getReportGeneration
import concurrent.futures

def deal_png_image(file_path):

    try:
        # 打开并处理图像
        image = Image.open(file_path)
        # 获取医疗报告
        medical_report_json = getReportGeneration(image).encode('utf-8')
        medical_report = json.loads(medical_report_json)

        # 准备报告数据
        report_data = {
            "file_name": file_path,
            "report": medical_report
        }
        print(f"Processed {file_path}")
        return report_data

    except Exception as e:
        print(f"Failed to process {file_path}: {e}")
        return None
    finally:
        # 确保图像文件被关闭以释放内存
        if 'image' in locals():
            image.close()

def process_dicom_directory(directory_path, report_file,batch_size=10):
    # 确保报告文件是空的
    open(report_file, 'w').close()

    report_data_list = []

    # 遍历目录中的所有文件
    file_paths = []
    for root, dirs, files in os.walk(directory_path):
        for file in files:
            if file.endswith('.png'):
                file_path = os.path.join(root, file)
                file_paths.append(file_path)
                #report_data_list.append(deal_png_image(file_path))

    # 分批处理文件路径列表
    for i in range(0, len(file_paths), batch_size):
        print(f"Processing files {i} to {i + batch_size}...")
        batch_paths = file_paths[i:i + batch_size]

        # 使用并行处理提高速度
        with concurrent.futures.ThreadPoolExecutor() as executor:
            results = list(executor.map(deal_png_image, batch_paths))

        # 收集非空结果
        for result in results:
            if result is not None:
                report_data_list.append(result)

        # 保存当前批次的报告到文件
        with open(report_file, 'a', encoding='utf-8') as f:
            for report_data in report_data_list:
                json.dump(report_data, f, ensure_ascii=False)
                f.write('\n')

        # 清空报告数据列表以释放内存
        report_data_list.clear()

    print(f"Processed {len(file_paths)} files.")


dicom_directory_path = 'test'
report_file_path = 'test/medical_reports.json'


process_dicom_directory(dicom_directory_path, report_file_path)
