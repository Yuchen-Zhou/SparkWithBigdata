// passwd数据集
// 分析出各个国家的常用密码前三
import org.apache.spark.sql.SparkSession
import java.util.Properties

object CountryPasswd {
  def main(args: Array[String]): Unit = {
    val file = "data/passwd.csv"
    val spark = SparkSession.builder.master("local").getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").csv(file)
    val rdd = df.select("country", "Password", "User_count", "Rank").rdd.
      map(line => (line(0).toString, line(1).toString, line(2).toString.toInt, line(3).toString.toInt)).
      filter(value => value._4 <=3)
    val new_df = rdd.toDF("country", "Password", "User_count", "CountryRank")
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")

    new_df.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "CountryPasswd", prop)

    new_df.show()
  }
}
