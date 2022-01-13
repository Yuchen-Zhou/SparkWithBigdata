import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


//读取上一任务写入到HDFS中到output数据集，针对客单价进行数据清洗
//1.统计客单价列缺失值的个数和比例
//2.缺失比例低于5%，将所有缺失值样本删除；缺失值比例大于5%，对缺失值字段进行中位数填充
//3.将处理完毕的数据输出到HDFS的output/output2中
object Mission2 {
  def main(args: Array[String]): Unit = {
    val file = "output/output1"
    val conf = new SparkConf().setMaster("local").setAppName("Mission2")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._
    var df = spark.read.option("header", "true").json(file)
    val na_cnt = df.filter(df.col("客单价").isNull).count()
    val na_per = na_cnt.toDouble / df.count().toDouble
    println("===“客单价”属性缺失记录为"+ na_cnt +"条，缺失比例" + na_per + "%===")

    if (na_per < 0.05) {
      df = df.na.drop(Array("客单价"))
      println("===“客单价”缺失记录已删除===")
    } else {
      val df2 = df.select("客单价").na.drop().sort(asc("客单价"))
      val media = df2.take((df2.count() / 2 + 1).toInt).last.toString().toInt
      df = df.select("客单价").na.fill(media)
      println("===“客单价”属性中位数为" + media+ "天===")
    }

    df.write.json("output/output2")

  }
}
