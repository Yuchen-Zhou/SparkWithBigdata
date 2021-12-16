#!/home/spark/miniconda3/env/bigdata/bin/python
## 这是分析阅读量前十的书籍

from pyspark.sql import SparkSession

spark = SparkSession.builder.getOrCreate()
sparkdf = spark.read.options(header='true').csv('data/bestsellers_with_categories.csv')
sparkdf.sort(sparkdf['Reviews'].desc()).select('Name', 'Reviews').show((15))
