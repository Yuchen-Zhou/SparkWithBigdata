import os
import pymysql
import json
import time

path = '/opt/data/stock_data'
db = pymysql.connect(host='127.0.0.1', user='root', password='Zyc0804!', database='stock_list', port=3306)
cursor = db.cursor()
sql_create = 'create table if not exists stocks(code int not null, name text)'
cursor.execute(sql_create)
stock_list = []
for file in os.listdir(path):
    if file[6:] == '.json':
        stock_list.append(file)

for stock in stock_list:
    file_path = os.path.join(path, stock)
    f = open(file_path, 'r')
    try:
        info = json.load(f)['data']
    except Exception:
        os.remove(file_path)
        print('已删除{stock}文件'.format(stock=stock))
        continue
    finally:
        code = "'" + info['code'] + "'"
        name = "'" + info['name'] + "'"
        sql_insert = 'insert into stocks(code, name) values ({code}, {name})'.format(code=code, name=name)
        cursor.execute(sql_insert)
        db.commit()
        print(code, name)
        time.sleep(0.2)
        continue
