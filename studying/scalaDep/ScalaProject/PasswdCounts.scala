// passwd数据集
// 统计密码出现的次数并列出密码出现次数的前五名

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties

object PasswdCounts {
  def main(args: Array[String]): Unit = {
    val file = "data/passwd.csv"
    val spark = SparkSession.builder.appName("PasswdC").master("local").getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").csv(file)
    val rdd = df.select("Password", "User_count").rdd.
          map(line => (line(0).toString, line(1).toString.toInt)).
          reduceByKey(_+_).map(value => (value._1, value._2))
    val new_df = rdd.toDF("Password", "User_count")

    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "Passwd1!")
    prop.put("driver", "com.mysql.jdbc.Driver")
    new_df.sort(desc("User_count")).write.
      mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "PasswordConts", prop)
  }
}
