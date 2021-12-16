#!/home/spark/miniconda3/envs/bigdata/bin/python
##分析Amazon数据集中各个评分的个数并显示出来

from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()
sparkdf = spark.read.options(header='true').csv('data/bestsellers_with_categories.csv')
rdd = sparkdf.select('User Rating').rdd.map(lambda line: (line['User Rating'], 1))\
            .reduceByKey(lambda x,y: x+y).sortByKey(False)
df = rdd.toDF().withColumnRenamed('_1', 'User Rating').withColumnRenamed('_2', 'Counts')

df.show()
