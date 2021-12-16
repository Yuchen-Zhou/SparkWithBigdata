#分析大于3小于50书籍的个数
from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()
sparkdf = spark.read.options(header='true').csv('data/bestsellers_with_categories.csv')
rdd = sparkdf.select('Price').rdd.filter(lambda line:(int(line['Price']) > 3 and int(line['Price'])< 50))\
        .map(lambda line: (int(line['Price']), 1)).reduceByKey(lambda x,y: x+y).sortByKey(False)

df = rdd.toDF().withColumnRenamed('_1', 'Price').withColumnRenamed('_2', 'Counts')
df.show(47)