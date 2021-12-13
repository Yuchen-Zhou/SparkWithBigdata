import requests
from bs4 import BeautifulSoup
import time, csv

#爬取列表页
def get_phone_list(base_url, page, headers):
    index_url = '/cell_phone_index/subcate57_list_' + str(page) + '.html'
    r = requests.get(url=base_url + index_url, headers=headers)
    soup = BeautifulSoup(r.text, 'lxml')
    div = soup.find(name='ul', class_='clearfix', id='J_PicMode')
    phone_list = div.find_all(name='li')
    href_list, title_list, price_list, scores_list = [], [], [], []

    for phone in phone_list:
        try:
            href_list.append(phone.h3.a['href'])    #详情页链接
            title_list.append(phone.h3.a['title'])  #手机名称
            price_list.append(phone.find(name='b', class_='price-type').string) #参考价格
            scores_list.append(phone.find(name='span', class_='score').string)  #综合评分
        except:
            scores_list.append(' ')
            continue

    return href_list, title_list, price_list, scores_list


def get_phone_info(base_url, phone_url, headers):
    path = base_url + phone_url
    print(path)
    req = requests.get(url=path, headers=headers)
    soup = BeautifulSoup(req.text, 'lxml')
    try:
        j_price = soup.find(name='span', class_='m-price _j_price_num').string.split('¥')[1]
        div = soup.find(name='div', class_='features-score features-score-5')
        scores = div.find_all(name='div', class_='circle-value')
        return [score.string for score in scores], j_price
    except:
        print('{}没有评分'.format(path))
        return [' ', ' ', ' ', ' ', ' '], ' '


def writeFile(file_path, title, price, j_price, avg_score, scores):
    with open(file_path, 'a', encoding='utf8') as f:
        writer = csv.writer(f)
        scores = [s for s in scores]
        lines = [title, price, j_price, avg_score]
        for s in scores:
            lines.append(s)
        print(lines)
        writer.writerow(lines)


if __name__ == '__main__':
    base_url = 'https://detail.zol.com.cn'
    headers = {'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit\
                            /537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36'}
    file_path = './phones_selling.csv'
    with open(file_path, 'w', encoding='utf8') as file:
        file.write('title, price, j_price, score, cost_per, performance, continue, face, photo\n')
    for pa in range(1, 15):
        href_list, title_list, price_list, scores_list = get_phone_list(base_url, pa, headers)
        print(len(href_list), len(title_list), len(price_list), len(scores_list))
        for li in href_list:
            index = href_list.index(li)
            scores, j_price = get_phone_info(base_url, li, headers)
            writeFile(file_path=file_path, title=title_list[index], price=price_list[index], j_price=j_price,
                      avg_score=scores_list[index], scores=scores)
            time.sleep(0)
        time.sleep(5)
