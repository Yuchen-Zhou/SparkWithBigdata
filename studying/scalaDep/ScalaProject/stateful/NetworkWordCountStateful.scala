package org.apache.spark.examples.streaming

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.storage.StorageLevel


object NetworkWordCountStateful {
  def main(args: Array[String]): Unit = {
    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.foldLeft(0)(_+_)
      val previousCount = state.getOrElse(0)
      Some (currentCount + previousCount)
    }
    StreamingExamples.setStreamingLogLevels()
    val conf = new SparkConf().setAppName("NetworkWordCountStateful").setMaster("local[2]")
    val sc = new StreamingContext(conf, Seconds(5))
    sc.checkpoint("file:///home/spark/stateful/")
    val lines = sc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))
    val wordDstream = words.map(x => (x, 1))
    val stateDStream = wordDstream.updateStateByKey[Int](updateFunc)
    stateDStream.print()
    sc.start()
    sc.awaitTermination()
  }
}
