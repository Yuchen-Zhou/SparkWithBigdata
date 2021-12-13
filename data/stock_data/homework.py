import requests
import re
import time

# 获取每一页股票列表
def get_stoctlist(list_url, headers):

    req_list = requests.get(url=list_url, headers=headers)
    req_list.encoding = req_list.apparent_encoding

    stock_names = re.findall('.*?"f12":"(.*?)".*?"f14":"(.*?)"', req_list.text, re.S)
    return stock_names

# 访问并获取每一支股票的数据
def get_data(stock_id, headers):
    # 由于上证深证创业板对应的数据库不同，所以需要对每个股票对id进行分析
    if stock_id[0] == '0':
        num = 33
        stock_title = '0.' + stock_id
    elif stock_id[0] == '6':
        num = 59
        stock_title = '1.' + stock_id
    elif stock_id[0] == '3':
        num = 65
        stock_title = '0.' + stock_id

    url = 'http://'+ str(num) + '.push2his.eastmoney.com/api/qt/stock/kline/get?cb=jQuery11240566578469407552_16' \
          '31334861186&secid=' \
          + stock_title + '&ut=fa5fd1943c7b386f172d6893dbfba10b&fields1=f1%2Cf2%2Cf3' \
                       '%2Cf4%2Cf5%2Cf6&fields2=f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58%2Cf5' \
                       '9%2Cf60%2Cf61&klt=101&fqt=0&beg=0&end=20500101&smplmt=460&lmt=1000000&_=1631' \
                       '334861244'

    single_req = requests.get(url=url, headers=headers)
    single_req.encoding = single_req.apparent_encoding

    data = re.search('({"rc".*?"]}})', single_req.text, re.S)


    path = './' + stock_id + '.json'
    with open(path, 'a', encoding='utf-8') as file:
        file.write(data.group(1))

# main函数用于控制访问每一页的股票
def main(page):
    list_url = 'https://13.push2.eastmoney.com/api/qt/clist/get?cb=jQuery11240649612240365' \
               '9659_1631590625030&pn='+ str(page) +'&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&' \
               'fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5' \
               ',f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f1' \
               '36,f115,f152&_=1631590625031'
    headers = {
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit'
                      '/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36'
    }

    stock_list = get_stoctlist(list_url, headers)

    for stock in stock_list:
        try:
            stock_id = stock[0]
            get_data(stock_id, headers)
            print(stock[1])
            time.sleep(1)
        except:
            continue

# 循环233次
if __name__ == '__main__':
    for page in range(1, 234):
        main(page)

