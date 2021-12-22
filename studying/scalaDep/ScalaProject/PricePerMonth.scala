//TSLA数据集
//月线分析按收盘价来计算

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties

object PricePerMonth {
  def main(args: Array[String]): Unit = {
    val file = "data/TSLA.csv"
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").csv(file)
    val rdd = df.select("Date", "Close").rdd.
      map(
        line => (
          line(0).toString.split("-")(0)+line(0).toString.split("-")(1),
          (line(1).toString.toFloat, 1))
      ).reduceByKey((x,y) => (x._1+y._1, x._2+y._2)).
      map(value => (value._1, value._2._1/value._2._2)).
      map(value => (value._1.toLong, value._2))

    val new_df = rdd.toDF("Date", "Price").sort(asc("Date"))

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")
    new_df.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "PricePerMonth", prop)
    new_df.sort(asc("Date")).show()


  }
}