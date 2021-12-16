##分析每年散文和小说的对比

from pyspark.sql import SparkSession
spark = SparkSession.builder.getOrCreate()
sparkdf = spark.read.options(header='true').csv('data/bestsellers_with_categories.csv')
rdd = sparkdf.select('Year', 'Genre').rdd.map(lambda line: ((line['Year'], line['Genre']), 1))\
        .reduceByKey(lambda x,y: x+y).map(lambda x: (x[0][0], x[0][1], x[1]))

df = rdd.toDF().withColumnRenamed('_1', 'Year').withColumnRenamed('_2', 'Genre').withColumnRenamed('_3', 'Counts')
df = df.sort(df['Year'].asc())

df.show()