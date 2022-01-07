package org.apache.spark.examples.streaming
import org.scalatest.enablers.Length

import java.io.{PrintStream, PrintWriter}
import java.net.ServerSocket
import scala.io.Source

object DataSourceSocket {
  def index(length: Int) = {
    val rdm = new java.util.Random()
    rdm.nextInt(length)
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      System.err.println("Usage: <filename> <port> <millisecond>")
      System.exit(1)
    }

    val fileName = args(0)
    val lines = Source.fromFile(fileName).getLines.toList
    val rowCount = lines.length
    val listener = new ServerSocket(args(1).toInt)
    while (true) {
      val socket = listener.accept()
      new Thread() {
        override def run = {
          println("Got client connected from: " + socket.getInetAddress)
          val out = new PrintWriter(socket.getOutputStream(), true)
          while (true) {
            Thread.sleep(args(2).toLong)
            val content = lines(index(rowCount))
            println(content)
            out.write(content + '\n')
            out.flush()
          }
          socket.close()
        }
      }.start()
    }
  }
}
