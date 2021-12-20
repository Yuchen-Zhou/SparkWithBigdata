//e-shopclothing2008数据集
// 颜色销量最好的model
import org.apache.spark.sql.SparkSession
import java.util.Properties

object BestSellwithColour {
  def main(args: Array[String]): Unit ={
    val file = "data/e-shopclothing2008.csv"
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val sparkdf = spark.read.option("header", "true").option("delimiter", ";").csv(file)
    val rdd = sparkdf.select("page 2 (clothing model)", "colour").rdd.
            map(line =>((line(0).toString, line(1).toString.toInt), 1)).reduceByKey(_+_).
            map(value => (value._1._1, value._1._2, value._2))
    val new_df = rdd.toDF("ClothingModel", "Colour", "Counts")

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")
    new_df.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "BestSellwithColour", prop)

    println("All Successful!!")

  }
}
