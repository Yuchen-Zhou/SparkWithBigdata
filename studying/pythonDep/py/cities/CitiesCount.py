#上榜国家统计
from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()
sparkdf1 = spark.read.options(header='true').csv('./data/cities_part1.csv')
sparkdf2 = spark.read.options(header='true').csv('./data/cities_part2.csv')
sparkdf = sparkdf1.unionAll(sparkdf2)

rdd = sparkdf.select('Country').rdd.map(lambda line: (line['Country'], 1)).reduceByKey(lambda x, y: x+y)
df = rdd.toDF().withColumnRenamed('_1', 'Country').withColumnRenamed('_2', 'Counts')

df.sort(df['Counts'].desc()).show()
