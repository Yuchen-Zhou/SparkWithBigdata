//Amazon作者阅读量排行
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import java.util.Properties


object ReviewsRank {
  def main(args: Array[String]) : Unit = {
    val file = "data/bestsellers_with_categories.csv"
    val saveFile = "file:///home/spark/code/SparkProgramming/studying/scalaDep/ScalaProject/ReviewsRank.csv"
    val spark = SparkSession.builder.getOrCreate()
    import spark.implicits._
    val sparkdf = spark.read.format("csv").option("header", "true").
                  load(file)
    val ReviewRDD = sparkdf.select("Author", "Reviews").rdd.
              map(line =>(line(0).toString(), line(1).toString.toInt)).reduceByKey(_+_)
    val ReviewDF = ReviewRDD.toDF("Author", "Reviews").sort(desc( "Reviews"))


    //可以自己改成写入MySQL，csv保存下来会比较奇怪
//    val prop = new Properties()
//    prop.put("user", "root")
//    prop.put("password", "Passwd1!")
//    prop.put("driver", "com.mysql.jdbc.Driver")
//    ReviewDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "ReviewsRank", prop)

    ReviewDF.write.format("csv").save(saveFile)


    println("Save succeed!!!")
  }
}
