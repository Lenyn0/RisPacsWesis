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


def generate_medical_report():
    medical_report = {
        '临床症状': '胸痛和呼吸困难',
        '影像学表现': '胸部X射线显示左侧胸腔积液，心脏轮廓正常，肺纹理清晰，无明显异常阴影。未见肺实变或肺门肿大。',
        '结论': '放射学检查未显示肺部明显异常，但左侧胸腔积液需要进一步临床评估。'
    }
    return json.dumps(medical_report, ensure_ascii=False)


def generate_search_result():
    # 模拟生成搜索结果
    search_result = {
        "result": "Sample search data"
    }
    return json.dumps(search_result, ensure_ascii=False)


def send_data_to_RIS(data):
    config = configparser.ConfigParser()
    config.read('resources/config.ini')
    base_url = config.get('DEFAULT', 'baseurl')
    endpoint = config.get('DEFAULT', 'endpoint')
    url = f'{base_url}{endpoint}'
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, data=data, headers=headers)
    print('Status Code:', response.status_code)
    print(data)


def receive_images_continuously():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('localhost', 5555))
    server_socket.listen(10)
    print('Server is waiting for client...')

    while True:
        client_socket, client_address = server_socket.accept()
        print(f'Client connected: {client_address}')

        json_data = b''
        while True:
            chunk = client_socket.recv(4096)
            if not chunk:
                break
            json_data += chunk

        if not json_data:
            continue

        data = json.loads(json_data.decode('utf-8'))
        flag = data.get('flag', '')
        image_data = data.get('image', '')
        random_filename = f'received_image_{random.randint(1, 100000)}.png'
        image_bytes = io.BytesIO(base64.b64decode(image_data))
        image = Image.open(image_bytes)
        print(f'Image received and saved as {random_filename}. Flag: {flag}')

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

        print('发送成功')
        client_socket.shutdown(socket.SHUT_WR)


if __name__ == '__main__':
    receive_images_continuously()
