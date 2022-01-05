#!/home/spark/miniconda3/envs/bigdata/bin/python

##这是分析每个作者的阅读量
from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()

sparkdf = spark.read.options(header='True', inferSchema='True', delimiter=',').csv('data/bestsellers_with_categories.csv')
rdd = sparkdf.select('Author', 'Reviews').rdd.map(lambda line: (line['Author'], line['Reviews'])).reduceByKey(lambda a,b: a+b).sortBy(lambda x: x[1], False)
df = rdd.toDF().withColumnRenamed('_1', 'Author').withColumnRenamed('_2', 'Reviews')


df.show()