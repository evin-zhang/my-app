import json
import csv

# 读取JSON文件并解析出a属性值
with open('data.json', 'r') as f:
    data = json.load(f)
    a_set = set(obj['a'] for obj in data)

# 读取CSV文件并检查每行中b列的值是否存在于a_set中
with open('data.csv', 'r') as f:
    reader = csv.reader(f)
    rows = [row + ['Y' if row[1] in a_set else 'N'] for i, row in enumerate(reader) if i == 0 or row[1] != '']
    # 如果一行中的b列为空，则忽略该行，不做任何操作

# 将修改后的CSV文件写回磁盘
with open('data_update.csv', 'w', newline='') as f:
    writer = csv.writer(f)
    writer.writerows(rows)
