//Amazon图书销量数据集
//统计每年出版的书的各个种类的数量
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import java.util.Properties

object YearGenC {
  def main(args: Array[String]) : Unit = {
    val conf = new SparkConf().setAppName("GenreCounts").setMaster("local")
    val sc = new SparkContext(conf)
    val file = "data/bestsellers_with_categories.csv"
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val df = spark.read.format("csv").option("header", "true").load(file)
    val YearGenRDD = df.select("Year", "Genre").rdd.
      map(line => ((line(0).toString(), line(1).toString()), 1)).
      reduceByKey(_ + _).map(x => (x._1._1, x._1._2, x._2))
    val YearGenDF = YearGenRDD.toDF("Year", "Genre", "Counts").
      sort("Year")
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")


    YearGenDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "GenCounts2", prop)

    println("All successful!!!")
  }
}

