import csv 
file_path = '/home/spark/code/SparkProgramming/data/Assignment.csv'
f = open(file_path, 'r')
text = f.read()
rows = [t for t in text.split('\n')]


print(rows[:10])