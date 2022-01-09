package org.apache.spark.examples.streaming

import java.util.HashMap
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

import java.util

object KafkaWordProducer {
  def main(args: Array[String]): Unit = {
    if (args.length < 4) {
      System.err.println("Usage: KafkaWordProducer <metadataBrokerList> <topic>" + "<messagesPerSec> <wordsPerMessage>")
      System.exit(1)
    }

    val Array(brokers, topic, messagesPerSec, wordsPerMessage) = args
    // Connect Zookeeper
    val prop = new util.HashMap[String, Object]()
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer")
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.Serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](prop)
    //Send message
    while (true){
      (1 to messagesPerSec.toInt).foreach { messageNum =>
        val str = (1 to wordsPerMessage.toInt).map(x => scala.util.Random.nextInt(10).toString).mkString(" ")
        println(str)
        println()

        val message = new ProducerRecord[String, String](topic, null, str)
        producer.send(message)
      }
      Thread.sleep(1000)
    }
  }
}
