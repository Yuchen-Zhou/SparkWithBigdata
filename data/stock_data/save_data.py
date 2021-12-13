import pymysql
import os
import json
import time

path = '/opt/data/stock_data/'
db = pymysql.connect(host='127.0.0.1', user='root', password='Zyc0804!', database='stock_data', port=3306)
cursor = db.cursor()

#create_db = 'create database if not exists stock_data'
#cursor.execute(create_db)
id_list = []
for file in os.listdir(path):
    if file[6:] == '.json':
        id_list.append(file)

for id in id_list:
    file_path = path + str(id)
    f = open(file_path, 'r')
    info = json.load(f)['data']
    # print(info)
    klines = info['klines']
    # print(klines)
    stock_code = info['code']
    stock_name = '`' + info['name'] + '`'
    stock_data = info['klines']
    create_sql = 'create table if not exists ' + stock_name + '(date text,' \
                                                              'begin float, last float, max float, min float,' \
                                                              'trade int, bv float, amp float, ' \
                                                              'change_per float, changed float)'
    cursor.execute(create_sql)
    for kline in klines:
        kline = kline.split(',')
        kline[0] = ''.join(kline[0].split('-'))
        kline.pop()
        kline = ','.join(i for i in kline)
        print(kline)
        insert_sql = 'insert into {stock_name}(date, begin, last, max, min, trade, bv, amp, change_per, changed) ' \
                     'values ({values})'.format(stock_name=stock_name,values=kline)
        print(insert_sql)
        cursor.execute(insert_sql)
        db.commit()
    time.sleep(0.2)

db.close()
