//e-shopclothing2008数据集
// 销量最好的model
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties

object BestSell {
  def main(args: Array[String]): Unit = {
    val file = "data/e-shopclothing2008.csv"
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").option("delimiter", ";").csv(file)
    val rdd = df.select("page 2 (clothing model)").rdd.
        map(line => (line(0).toString, 1)).reduceByKey(_+_)
    val new_df = rdd.toDF("Kinds", "Counts")
    val new_df2 = new_df.sort(desc("Counts"))
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")
    new_df2.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "BestSell", prop)

    println("All Successful!!")
  }
}
