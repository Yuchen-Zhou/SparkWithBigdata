//TSLA数据集
//分析出特斯拉上市以来每年的股票交易量

import org.apache.spark.sql.SparkSession
import java.util.Properties
import org.apache.spark.sql.functions._

object VolumePerYear {
  def main(args: Array[String]): Unit = {
    val file = "data/TSLA.csv"
    val spark = SparkSession.builder.master("local").getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").csv(file)
    val rdd = df.select("Date", "Volume").rdd.
      map(line => (line(0).toString.split("-")(0), line(1).toString.toLong)).
      reduceByKey((x,y) => x+y)
    val new_df = rdd.toDF("Year", "Volume__sum")

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")

    new_df.sort(asc("Year")).write.
      mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "VolumePerYear", prop)
    new_df.sort(asc("Year")).show()
  }
}
