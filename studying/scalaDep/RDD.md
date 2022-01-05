# Spark RDD 

## 1.RDD创建	

Spark采用textFile()方法来从文件系统中加载数据创建RDD，该方法把文件的URI作为参数，这个URI可以是本地文件系统的地址、分布式文件系统HDFS的地址。

```scala
#1.从文件系统中加载数据创建RDD
scala> val lines = sc.textFile("file:///opt/software/spark/README.md")
scala> val lines = sc.textFile("hdfs:///user/spark/stocks/part-m-00000")

#2.通过并行集合(数组)创建RDD
scala> val array = Array(1, 2, 3, 4, 5)
scala> val rdd = sc.parallelize(array)
```

## 2.RDD操作

转换操作

| 操作              | 含义                                                         |
| ----------------- | ------------------------------------------------------------ |
| filter(func)      | 筛选出满足函数func的元素，并返回一个新的数据集               |
| map(func)         | 将每个元素传递到函数func中，并将结果返回为一个新的数据集     |
| flatMap(func)     | 与map相似(),但每个输入元素都可以映射到0或多个输出结果        |
| groupByKey()      | 应用于(K, V)键值对的数据集时，返回一个新的(K, Iterable)形式的数据集 |
| reduceByKey(func) | 应用于(K, V)键值对的数据集时，返回一个新的(K, V)形式的数据集，其中每个值都是将每个key传递到函数func中进行聚合后的结果 |

行动操作

| 操作          | 含义                                                     |
| ------------- | -------------------------------------------------------- |
| count()       | 返回数据集中的元素个数                                   |
| collect()     | 以数组的形式返回数据集中的所有元素                       |
| first()       | 返回数据集中的第一个元素                                 |
| take(n)       | 以数组的形式返回数据集中的前n个元素                      |
| reduce(func)  | 通过函数func(输入两个参数并返回一个值)聚合数据集中的元素 |
| foreach(func) | 将数据集中的每个元素传递到函数func中运行                 |



## 3.惰性机制

惰性机制是指，整个转换过程只是记录了转换的轨迹，并不会发生真正的计算，只有遇到行动操作时，才会触发真正的计算

## 4.持久化

在Spark中，RDD采用惰性求值的机制，每次遇到行动操作，都会从头开始执行计算。

```scala
scala> val list = List("Hadoop", "Spark", "Hive")
scala> val rdd = sc.parallelize(list)
scala> println(rdd.count())
3
scala> println(rdd.collect().mkString(","))
Hadoop, Spark, Hive
```

实际上，可以通过持久化（缓存）机制来避免这种重复计算的开销。具体方法是使用persist()方法对一个RDD标记为持久化，持久化后的RDD会被保留到计算节点的内存中，被后面的行动操作重复使用。

- persist(MEMORY_ONLY):表示将RDD作为反序列化的对象存储在JVM中，如果内存不足，就要按照LRU原则替换缓存中的内容
- persist(MEMORY_AND_DISK):表示将RDD作为反序列化的对象存储在JVM中，如果内存不足，超出的分区将会被存放在磁盘中

```scala
#一般而言，使用cache()方法时，会调用persist(MEMORY_ONLY)
scala> val list = List("Hadoop", "Spark", "Hive")
scala> val rdd = sc.parallelize(list)
scala> rdd.cache() //会调用persist(MEMORY_ONLY)，但是，语句到这里并不会缓存RDD
scala> println(rdd.count()) //第一次行动操作，触发一次真正从头到尾的计算，把上面的rdd放入缓存
3
scala> println(rdd.collect().mkString(",")) //第二次行动操作，重复使用上面的rdd
Hadoop, Spark, Hive
```



## 5.分区

- 分区的作用

  RDD是弹性分布式数据集，通常RDD很大，会被分成多个分区，分别保存在不同的节点上。

  对RDD进行分区，第一个功能是增加并行度，第二个功能是减少通信开销

- 分区的原则

  RDD分区的一个原则是使分区的个数尽量等于集群中的CPU核心(Core)数目。对于不同的Spark部署模式而言(Local模式、Standalone模式、YARN模式、Mesos模式)，都可以通过设置spark.default.parallelism这个参数的值，来配置默认的分区数目。

  - Local模式：默认为本地机器的CPU数目，若设置了local[N]，则默认为N
  - Standalone或YARN模式：在“集群中所有CPU核心数目总和”和“2”这二者中取较大值作为默认值
  - Mesos模式：默认的分区数为8

- 设置分区的个数

  - 创建RDD时手动指定分区个数

    ```scala
    //在调用textFile()和parallelize()方法的时候手动指定分区个数即可
    scala> val array = Array(1, 2, 3, 4, 5)
    scala> val rdd = sc.parallelize(array, 2)
    ```

  - 使用repartition方法重新设置分区个数

    ```scala
    //通过转换操作得到新的RDD时，直接调用repartition方法即可
    scala> val data = sc.textFile("file:///opt/software/spark/README.md", 2)
    scala> data.paritions.size
    2
    scala> rdd.repartitions.sieze
    1
    ```

- 自定义分区方法

  Spark提供了自带的哈希分区(HashPartitioner)与区域分区(RangePartitioner)，能够满足大多数应用场景的需求。与此同时，Spark也支持自定义方式，即通过提供一个自定义的Partitioner对象来控制RDD的分区方式，从而利用领域知识进一步减小通信开销。

  实现自定义分区的方法：

  - numPartitions: Int 	返回创建出来的分区数
  - getPartition(Key: Any): Int  返回给定键的分区编号（0 到 numPartitioners-1）
  - equals():  Java判断相等性的标准方法

  下面是一个实例，要求根据key值的最后一位数字写到不同的文件中。例如，10写入到part-00000, 11写入part-00001， 12写入到part-00002.

  ```scala
  import org.apache.spark.{Partitioner, SparkContext, SparkConf}
  //自定义分区类，需要继承org.apache.spark.Partitioner类
  class MuPartitioner(numParts: Int) extends Partitioner {
  	//覆盖分区数
      override def numPartitions : Int = numParts
      //覆盖分区号获取函数
      override def getPartition(key: Any) : Int = {
          key.toString.toInt%10
      }
  }
  object TestPartitioner {
      def main(args: Array[String]) {
          val conf = new SparkConf()
          val sc = new SparkContext(conf)
          //模拟5个分区的数据
          val data = sc.parallelize(1 to 10, 5)
          //根据尾号转变为10个分区，分别写到10个文件
         	data.map((_, 1)).partitionBy(new MyPartitioner(10)).map(_._1) \
         .saveAsTextFile("file:///opt/software/....")
      }
  }
  ```

## 6.键值对RDD

键值对RDD(Pair RDD)是指每个RDD元素都是(key, value)键值对类型，是一种常见的RDD类型，可以应用于很多应用场景

- 键值对RDD的创建

  - 从文件中加载生成RDD

    ```scala
    scala> val lines = sc.textFile("file:///opt/software/spark/READM.md")
    scala> val pairRDD = lines.flatMap(line => line.split(" ")).map(word => (word, 1))
    scala> pairRDD.foreach(println)
    ```

    

  - 通过并行集合（数组）创建RDD

    ```scala
    scala> val list = List("Hadoop", "Spark", "Hive", "Spark")
    scala> val rdd = sc.parallelize(list)
    scala> val pairRDD = rdd.map(word => (word, 1))
    scala> val pairRDD.foreach(println)
    (Hadoop, 1)
    (Spark, 1)
    (Hive, 1)
    (Spark, 1)
    ```

    

## 7.常用的键值对操作

常用的键值对转换操作包括 reduceByKey(func)、groupByKey()、keys、values、sortByKey()、mapValues(func)、join和combineByKey等

- reduceByKey(func)

  reduceByKey(func)的功能是，使用func函数合并具有相同键的值。

  ```scala
  scala> pairRDD.reduceByKey((a, b) => a+b).foreach(println)
  (Spark, 2)
  (Hive, 1)
  (Hadoop, 1)
  ```

  

- groupByKey()

  groupByKey()的功能是，对具有相同键的值进行分组。

  groupByKey和reduceByKey的区别是：reduceByKey用于对每个key对应的多个value进行聚合操作，并且聚合操作可以通过函数func进行自定义；groupByKey也是对每个key进行操作，但是，对每个key只会生成一个value-list，groupByKey本身不能自定义函数，需要先用groupByKey生成RDD，然后才能对此RDD通过map进行自定义函数操作

  ```scala
  scala> pairRDD.groupByKey()
  scala> val words = Array("one", "two", "two", "three", "three", "three")
  scala> val wordPairsRDD = sc.parallelize(words).map(word => (word, 1))
  scala> val wordCountsWithReduce = wordPairsRDD.reduceByKey(_+_)
  scala> val wordCountsWithGroup = wordPairsRDD.
  	| groupByKey().map(t => (t._1+t._2.sum))
  ```

- keys

  键值对RDD每个元素都是(key, value)的形式，keys操作只会把键值对RDD中的key返回，形成一个新的RDD

  ```scala
  scala> pairRDD.keys
  scala> pairRDD.keys.foreach(println)
  Hadoop
  Spark
  Hive
  Spark
  ```

- values

  values操作只会把键值对RDD中的value返回，形成一个新的RDD。

  ```scala
  scala> pairRDD.values
  scala> pairRDD.values.foreach(println)
  1
  1
  1
  ```

- sortByKey()

  sortByKey()的功能是返回一个根据key排序的RDD。

  ```scala
  scala> pairRDD.sortByKey()
  scala> pairRDD.sortByKey().foreach(println)
  (Hadoop, 1)
  (Hive, 1)
  (Spark, 1)
  (Spark, 1)
  ```

- sortBy()

  sortByKey的功能是返回一个根据key排序的RDD，而sortBy()则可以根据其他字段进行排序

  ```scala
  scala> val d1 = sc.parallelize(Array(("c", 8), ("b", 25), ("c", 17), ("a", 42), ("b", 4), ("d", 9)))
  scala> d1.reduceByKey(_+_).sortByKey(false).collect
  res: Array[(String, Int)] = Array((d,9), (c,25), (b,29), (a,42))
  ```

- mapValues(func)

  mapValues(func)对键值对RDD中的每个value都应用一个函数，但是，key不会发生变化。

  ```scala
  scala> pairRDD.mapValues(x => x+1)
  scala> pairRDD.mapValues(x => x+1).foreach(println)
  (Hadoop, 2)
  (Spark, 2)
  (Hive, 2)
  (Spark, 2)
  ```

- join()

  join()内连接，对于给定的两个输入数据集(K, V1)和(K, V2)，只有在两个数据集中都在存在的key才会被输出，最终得到一个(K, (V1, V2))的数据集。

  ```scala
  scala> val pairRDD1 = sc.
  	| parallelize(Array(("spark", 1), ("spark", 2), ("hadoop", 3), ("hadoop", 5)))
  scala> val pairRDD2 = sc.parallelize(Array(("spark", "fast")))
  scala> pairRDD1.join(pairRDD2)
  scala> pairRDD1.join(pairRDD2).foreach(println)
  (spark, (1, fast))
  (spark, (2, fast))
  ```

- combineByKey

  combineByKey(createCombiner, mergeValue, partitioner, mapSideCombine)中的各个参数的含义如下：

   - createCombiner：在第一次遇到key时创建组合器函数，将RDD数据集中的V类型值转换成C类型值(V=>C)

   - mergeValue：合并值函数，再次遇到相同的Key时，将createCombiner的C类型值与这次传入的V类型值合并成一个C类型值(C, V) => C

   - mergeCombiners：合并组合器函数，将C类型值两两合并成一个C类型值

   - partitioner：使用已有的或自定义的分区函数，默认是HashPartitioner

   - mapSideCombine：是否存在map端进行Combine操作，默认为true

 ```scala
//实例
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf 

object Comine {
    def main (args: Array[String]) {
        val conf = new SparkConf().setAppName("combine").setMaster("local")
        val sc = new SparkContext(conf)
        val data = sc.parallelize(Array(("company-1", 88), ("company-1", 96), ("company-1", 85), ("company-2", 94), ("company-2", 86), ("company-2", 74), ("company-3", 86), ("company-3", 88), ("company-3", 92)), 3)
        val res = data.combineByKey(
            (income) => (income, 1),
            (acc:(Int, Int), income) => (acc._1+income, acc._2+1),
            (acc1:(Int, Int), acc2:(Int, Int)) => (acc1._1+acc2._1, acc1._2+acc2._2 ) 
            ).map( {case (key, value) => (key, value._1, value._1/value._2.toFloat)})
        res.repartition(1).saveAsTextFile("file:///.....")
    }
}
```

##### 8.数据读写

- 文件数据读写

  - 本地文件系统的数据读写

    - 从本地文件中读取数据创建RDD

      从本地文件系统读取数据，可以采用textFile()方法，可以为textFile()方法提供一个本地文件或目录地址，如果是一个文件地址，它会加载该文件，如果是一个目录地址，他会加载该目录下的所有文件的数据

      ```scala
      scala> val textFile = sc.textFile("file:///....")
      ```

    - 把RDD写入到文本文件中

      可以使用saveAsTextFile()方法把RDD中的数据保存到文本文件中。需要注意的是，这里的saveAsTextFile()中提供的参数，不是文件名称，而是目录名称。因为，Spark通常在分布式环境下执行，RDD会存在多个分区，由多个任务对这些分区进行并行计算，每个分区都会保存到一个单独的文件中。

      ```scala
      scala> val textFile = sc.textFile("file:///....")
      scala> textFile.saveAsTextFile("file:///anotherpath....")
      ```

  - 分布式文件系统HDFS的数据读写

    从分布式文件系统HDFS中读取数据，也是采用textFile()方法，可以为textFile()方法提供一个HDFS文件或目录地址，如果是一个文件地址，它会加载该文件，如果是一个目录地址，它会加载该目录下的所有文件的数据。

    ```scala
    scala> val textFile = sc.textFile("hdfs:///localhost:9000/user/spark/...")
    scala> textFile.first()
    ```

    同样，可以使用saveAsTextFile()方法把RDD中的数据保存到HDFS文件中

    ```scala
    scala> val textFile = sc.textFile("xx.txt")scala> textFile.saveAsTextFile("writeback")
    ```

  - JSON文件的读取

    JSON(JavaScript Object Notation)是一种轻量级的数据交换格式，它基于ECMAScript规范的一个子集，采用完全独立于编程语言的文本格式来存储和表示数据。Spark提供了一个JSON样例数据文件，存放在$SPARK_HOME/examples/src/main/resources/people.json中。people.json文件内容如下：

    ```json
    {"name": "Michael"}{"name": "Andy", "age": 30}{"name": "Justin", "age": 19}
    ```

    ```scala
    scala> val jsonStr = sc.textFile("file:///opt/software/spark/examples/src/main/resources/people.json")scala> jsonStr.foreach(println){"name": "Michael"}{"name": "Andy", "age": 30}{"name": "Justin", "age": 19}
    ```

    Scala中有一个自带JSON库——scala.util.parsing.json.JSON，可以实现对JSON数据的解析，JSON.parseFull(jsonString: String)函数以一个JSON字符串作为输入并进行解析，如果解析成功，则返回一个Some(map: Map[String, Any])，如果解析失败，则返回None

    ```scala
    scala> import scala.util.parsing.json.JSON
    scala> val inputFile = "file:///opt/software/spark/examples/src/main/resources/people.json"        
    scala> val conf = new SparkConf().setAppName("JSONRead")
    scala> val sc = new SparkContext(conf)
    scala> val jsonStrs = sc.textFile(inputFile)
    scala> val result = jsonStrs.map(s => JSON.parseFull(s))
            result.foreach( {r => r match {
                		case Some(map: Map[String, Any]) => println(map)
                		case None => println("Parsing failed")
                		case other => println("Unkown data structure: " + other)  }})
    ```
