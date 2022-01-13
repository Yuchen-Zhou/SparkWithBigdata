import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


//给定wm_2000.json数据集，针对推单数和接单数两列
//过滤并统计推单数小于接单数的行数，将异常的数据进行统计，将清洗后的数据写入到hdfs中
object Mission1 {
  def main(args: Array[String]): Unit = {
    val file = "data/wm_2000.json"
    val conf = new SparkConf().setAppName("Mission1").setMaster("local")
    val spark = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._
    val df = spark.read.option("header", "true").json(file)
    val num = df.filter($"推单数"<$"接单数").count()
    println("===“推单数”小于“接单数”的异常数据条数为"+ num+"条===")

    val new_df = df.filter($"推单数">=$"接单数")
    new_df.write.json("output/output1")
  }
}
