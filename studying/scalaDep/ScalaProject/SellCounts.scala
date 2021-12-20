//e-shopclothing2008数据集
// 每月的销量
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties

object SellCounts {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val file = "data/e-shopclothing2008.csv"
    val sparkdf = spark.read.option("header", "true").option("delimiter", ";").csv(file)
    val CountsRDD = sparkdf.select("month").rdd.
          map(line => (line(0).toString.toInt, 1)).reduceByKey(_+_)

    val new_df = CountsRDD.toDF("Month", "Counts")

    new_df.show()
  }
}
