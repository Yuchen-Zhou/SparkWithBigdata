import org.apache.spark._
import org.apache.spark.streaming._

object TestStreaming {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("WordCountStreaming").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    val lines = ssc.textFileStream("file:///opt/spark/mycode/logfile")
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x,1)).reduceByKey(_+_)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }
}