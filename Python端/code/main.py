import socket
import json
import io
import random
import base64
from PIL import Image
import requests
import configparser
from reportGeneration.reportGeneration import getReportGeneration
from anomalyDetection.inference import GetAnomalyDetection
import os
import logging
from logging.handlers import RotatingFileHandler


# 生成医疗报告的函数
def generate_medical_report():
    medical_report = {
        '临床症状': '胸痛和呼吸困难',
        '影像学表现': '胸部X射线显示左侧胸腔积液，心脏轮廓正常，肺纹理清晰，无明显异常阴影。未见肺实变或肺门肿大。',
        '结论': '放射学检查未显示肺部明显异常，但左侧胸腔积液需要进一步临床评估。'
    }
    return json.dumps(medical_report)

# 生成检索结果的函数
def generate_search_result():
    # 模拟生成搜索结果
    search_result = {}

    for i in range(1, 20):  # 从1到19
        filename = f"{i}.png"  # 图片文件名
        if os.path.exists(filename):  # 检查文件是否存在
            try:
                with open(filename, "rb") as image_file:
                    encoded_string = base64.b64encode(image_file.read()).decode()  # 将图片编码为base64
                    search_result[f"image_data{i}"] = encoded_string  # 将编码后的图片添加到结果中
                    search_result[f"report_data{i}"] = f'这是第{i}份报告'  # 添加报告
            except Exception as e:
                logger.error(f"Error reading file {filename}: {e}")
    return json.dumps(search_result)

# 向RIS发送数据的函数
def send_data_to_RIS(data):
    base_url = config.get('DEFAULT', 'baseurl')
    endpoint = config.get('DEFAULT', 'endpoint')
    url = f'{base_url}{endpoint}'
    headers = {'Content-Type': 'application/json'}
    try:
        response = requests.post(url, data=data, headers=headers)
        #print('Status Code:', response.status_code)
    except Exception as e:
        logger.error(f"Error sending data to RIS: {e}")
    logger.info(data)

# 持续接收图像的函数
def receive_images_continuously():
    port = int(config.get('DEFAULT', 'port'))

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', port))
    server_socket.listen(10)
    logger.info('Server is waiting for client...')

    while True:
        client_socket, client_address = server_socket.accept()
        logger.info(f'Client connected: {client_address}')

        json_data = b''
        while True:
            try:
                chunk = client_socket.recv(4096)
                if not chunk:
                    break
                json_data += chunk
            except Exception as e:
                logger.error(f"Error receiving data: {e}")
                break

        if not json_data:
            continue


        try:
            data = json.loads(json_data.decode('utf-8'))
        except json.JSONDecodeError as e:
            logger.error(f"Error parsing JSON: {e}")
            continue

        flag = data.get('flag', '')
        image_data = data.get('image', '')

        image_bytes = io.BytesIO(base64.b64decode(image_data))
        image = Image.open(image_bytes)

        if flag == 'reportGeneration':
            medical_report_json = getReportGeneration(image)
            client_socket.sendall(medical_report_json.encode('utf-8'))
            send_data_to_RIS(medical_report_json)
        elif flag == 'anomalyDetection':
            detection_result_json = GetAnomalyDetection(image)
            client_socket.sendall(detection_result_json.encode('utf-8'))
        elif flag == 'search':
            search_result_json = generate_search_result()
            client_socket.sendall(search_result_json.encode('utf-8'))

        logger.info('发送成功')
        client_socket.shutdown(socket.SHUT_WR)

# 主函数
if __name__ == '__main__':
    # 读取配置文件
    config = configparser.ConfigParser()
    config.read('resources/config.ini')

    # 创建一个日志记录器
    logger = logging.getLogger()

    # 设置日志记录器的级别
    logger.setLevel(logging.INFO)

    # 创建一个RotatingFileHandler实例
    handler = RotatingFileHandler('app.log', maxBytes=10 * 1024 * 1024, backupCount=3)

    # 设置日志记录格式
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    handler.setFormatter(formatter)

    # 将RotatingFileHandler添加到日志记录器中
    logger.addHandler(handler)
    receive_images_continuously()
