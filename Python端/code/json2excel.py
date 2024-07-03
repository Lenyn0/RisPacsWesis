import json
import os

import pandas as pd

# 读取 medical_reports.json 文件
json_file = 'test/medical_reports.json'
with open(json_file, 'r', encoding='utf-8') as f:
    data = f.readlines()

# 解析 JSON 数据
reports = [json.loads(line.strip()) for line in data]

# 假设您的 Excel 文件是 test.xlsx，其中有一个名为 Sheet1 的工作表
excel_file = 'test/测试资料（内含正常异常）.xlsx'
sheet_name_abnormal = '测试异常'
sheet_name_normal = '测试正常'

# 加载 Excel 文件
df_normal = pd.read_excel(excel_file, sheet_name=sheet_name_normal, dtype={'acc': str})
df_abnormal = pd.read_excel(excel_file, sheet_name=sheet_name_abnormal, dtype={'acc': str})
count=1
# 将报告生成结果写入 Excel 文件的对应行
for report in reports:
    print(f"Processing{count}...")
    count+=1
    file_name = report['file_name']
    report_result = report['report']['报告生成结果']

    # 从文件名中提取 acc（假设文件名中的 acc 是用 '-' 分隔的第二部分）
    acc = os.path.splitext(os.path.basename(file_name))[0]
    flag = file_name.split('-')[1]

    if flag == 'abnormal':
        df = df_abnormal
    elif flag == 'normal':
        df = df_normal

    # 在 Excel 中查找对应的行
    idx = df.index[df['acc'] == acc].tolist()

    # 如果找到匹配的行，则将报告生成结果写入对应的列
    if idx:
        idx = idx[0]  # 假设只有一个匹配项，取第一个匹配的索引
        df.loc[idx, '报告生成结果'] = report_result

# 将更新后的 DataFrame 写回到 Excel 文件的对应工作表
with pd.ExcelWriter(excel_file, engine='openpyxl') as writer:
    df_abnormal.to_excel(writer, sheet_name=sheet_name_abnormal, index=False)
    df_normal.to_excel(writer, sheet_name=sheet_name_normal, index=False)
