# Spark SQL

## 1.DataFrame的创建

从Spark2.0开始，Spark使用全新的SparkSession接口。Spark Session支持从不同的数据源加载数据，以及把数据转换成DataFrame，并且支持把DataFrame转换成SQLContext自身的表，然后使用SQL语句来操作数据

```scala
//可以通过下面的语句创建一个SparkSession
scala> import org.apache.spark.sql.SparkSession
scala> val spark = SparkSession.builder().getOrCreate()
```

实际上，在启动spark-shell以后，spark-shell就默认提供了一个SparkContext对象和一个SparkSession对象。

在创建DataFrame之前，为了支持RDD转换为DataFrame及后续的SQL操作，需要执行`import spark.implicits._`导入相应的包，启用隐式转换。

在创建DataFrame时，可以使用spark.read操作，从不同类型的文件中加载创建DataFrame

- spark.read.json("people.json"):读取people.json文件创建DataFrame;在读取本地文件或HDFS文件时，要注意给出正确的文件路径
- spark.read.parquet("people.parquet"):读取people.parquet文件创建DataFrame
- spark.read.csv("people.csv"):读取people.csv文件创建DataFrame

或者

- spark.read.format("json").load("people.json")
- spark.read.format("parquet").load("people.parquet")
- spark.read.format("csv").load("people.csv")

## 2.DataFrame的保存

可以使用spark.write操作，把一个DataFrame保存成不同格式的文件。例如，把一个名称为df的DataFrame保存到不同格式文件中，方法如下：

- df.write.json("people.json")
- df.write.parquet("peolple.parquet")
- df.write.csv("people.csv")
- df.write.format("json").save("people.json")
- df.write.format("csv").save("people.csv")
- df.write.format("parquet").save("people.parquet")

```scala
scala> val peopleDF = spark.read.format("json").
	| load("file:///opt/software/examples/src/main/resources/people.json")
scala> peopleDF.select("name", "age").write.format("csv").
	| save("file:///opt/code/newpeople.csv")
```

上面代码中，`peopleDF.select("name", "age").write`语句的功能是从peopleDF中选择name和age两列的数据进行保存，如果要保存所有列的数据，只需要使用peopleDF.write即可，保存之后会看到一个名为newpeople.csv的目录

## 3.DataFrame的常用操作

- `printSchema()`

  可以使用`df.printSchema()`操作，打印出DataFrame的模式信息
  ```scala
  scala> df.printSchema()
  ```

- `select()`

  `df.select()`的功能，是从DataFrame中选取部分列的数据
  ```scala
  scala> df.select("col_name1", "col_name2", ...).show()
  ```

- `filter()`

  `df.filter()`可以实现条件查询，找到满足条件要求的记录
  ```scala
  //过滤掉A列中小于B列的数据
  scala> df.filter($"A">=$"B")
  //过滤出A列中等于某值或某字符串的数据
  scala> df.filter($"A"==="xx")
  scala> df.filter($"A"===123)
  scala> df.filter($"A")
  scala> df.filter(df("A") === "xx")
  scala> df.filter(df.col("A") === "xx")
  //统计某列中空值的个数
  scala> df.filter(df.col("col_name").isNull).count
  ```

- `groupBy()`

  `df.groupBy()`用于对记录进行分组

- `sort()`

  `df.sort()`用于对记录进行排序，`df.sort(df("age").desc, df("name").asc)`，desc为降序，asc为升序
  ```scala
  scala> df.sort(desc("col_name1"), asc("col_name2"))
  ```
- 去除空值、Nan

  `df.na.drop()`

- 空值填补

  `df.na.fill()`
  
- 列名更改

  `df.withColumnRenamed(old_name: String, new_name: String)`

- 添加列

  `df.withColumn(colName : scala.Predef.String, col : org.apache.spark.sql.Column)`



## 4.从RDD转换得到DataFrame

利用反射机制推断RDD模式

```scala
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.SparkSession
object Test {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().getOrCreate()
    import spark.implicits._
    case class Person(name: String, age: Long)
    val peopleDF = spark.sparkContext.textFile("file:///.....").
      map(_.split(",")).
      map(attributes => Person(attributes(0), attributes(1).trim.toInt)).toDF()
    peopleDF.createOrReplaceTempView("people")
    val personRDD = spark.sql("select name, age from people where age > 20")
    personRDD.map(t => "Name: " + t(0) + "Age: " + t(1)).show()
  }
}
```

使用编程方式定义RDD模式

```scala
import org.apache.spark.sql.types._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
object Test {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().getOrCreate()
    val fields = Array(StructField("name", StringType, true), StructField("age", IntegerType, true))
    val schema = StructType(fields)
    val peopleRDD = spark.sparkContext.textFile("file:///....")
    val rowRDD = peopleRDD.map(_.split(",")).map(attributes => Row(attributes(0), attributes(1).trim.toInt))
    val peopleDF = spark.createDataFrame(rowRDD, schema)
    peopleDF.createOrReplaceTempView("people")
    val results = spark.sql("select name, age from people")
    results.map(attributes => "name: " + attributes(0) + "," + "age: " + attributes(1)).show()
  }
}
```

## 5.使用SparkSQL读写MySQL数据库

`(1)准备工作`

这里采用MySQL数据库来存储和管理数据。

```shell
$ service mysql start
$ mysql -u root -p
```

在MySQL Shell环境中，输入下面SQL语句完成数据库和表的创建

```mysql
mysql> create database spark;
mysql> use spark;
mysql> create table student(id int(4), name char(20), gender char(4), age int(4));
mysql> insert into student values(1, 'Xueqian', 'F', 23);
mysql> insert into student values(2, 'Weiliang', 'M', 24);
mysql> select * from student;
```

在spark-shell中想要顺利链接MySQL必须要有JDBC驱动程序

```shell
$ spark-shell -- jars $SPARK_HOME/jars/mysql-connector-* --driver-class-path $SPARK_HOME/jars/mysql-*
```

`(2)读取MySQL数据库中的数据`

`spark.read.format("jdbc")`操作可以实现对MySQL数据库的读写。

```scala
scala> val jdbcDF = spark.read.format("jdbc").
	| option("url", "jdbc:mysql://localhost:3306/spark").
	| option("driver", "com.mysql.jdbc.Driver").
	| option("dbtable", "student").
	| option("user", "root").
	| option("password", "...").
	| load()
scala> jdbcDF.show()
```

`(3)向MySQL数据库写入数据`

在MySQL数据库中，已经创建了一个名为spark的数据库，并创建了一个名为student的表。下面要向表中写入数据。

```scala
import java.util.Properties
import org.apache.spark.sql.types._
import org.apache.spark.sql.Row

val studentRDD = spark.sparkContext.parallelize(Array("3 Rongcheng M 26", "4 Guanhua F 27")).map(_.split(" "))

val schema = StructType(List(StructField("id", IntegerType, true), StructField("name", StringType, true), StructField("gender", StringType, true), StructField("age", IntegerType, true)))

val rowRDD = studentRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))

val studentDF = spark.createDataFrame(rowRDD, schema)

val prop = new Properties()
prop.put("user", "root")
prop.put("password", "Passwd1!")
prop.put("driver", "com.mysql.jdbc.Driver")

studentDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "student", prop)
```



## 6.连接Hive读写数据

Hive是构建在Hadoop之上的数据仓库工具。可以支持大规模数据存储、分析，具有良好的可扩展性。在某种程度上，Hive可以看作是用户编程接口，因为它本身不会存储和处理数据，而是依赖分布式文件系统HDFS来实现数据的存储，依赖分布式并行计算模型MapReduce来实现数据的处理

`(1)准备工作`

为了让Spark能够访问Hive，必须为Spark添加Hive支持。

在spark-shell中执行`import org.apache.spark.sql.hive.HiveContext `

`(2)在Hive中创建数据库和表`

由于之前安装的Hive是使用MySQL数据库来存放Hive的元数据。因此，在使用Hive之前必须启动MySQL数据库，

`service mysql start`

然后，启动Hive，新建一个数据库Sparktest，并在这个数据库下面创建一个表，然后录入两条数据

```sql
$ hive
hive> create database if not exists sparktest;
hive> show databases;
#在数据库sparktest中创建一个表student
hive> create table if not exists spark.student(
> id int, name string, gender string, age int);
hive> use sparktest;
hive> show tables;
hive> insert into student values(1, 'Xueqian', 'F', 24);
hive> insert into student values(2, 'Weiliang', 'M', 23);
hive> select * from student;
```

`(3)连接Hive读写数据`

为了能够让Spark顺利访问Hive，需要修改`spark-env.sh`

```shell
export SPARK_DIST_CLASSPATH=$(/opt/software/hadoop/bin/hadoop classpath)
export JAVA_HOME=/opt/software/jdk
export CLASSPATH=$CLASSPATH:/opt/software/hive/lib
export SCALA_HOME=/opt/software/scala
export HADOOP_CONF_DIR=/opt/software/hadoop/etc/hadoop
export HIVE_CONF_DIR=/opt/software/hive/conf
export SPARK_CLASSPATH=$SPARK_CLASSPATH:/opt/software/hive/lib/mysql-connec*
```

从Hive中读取数据

```scala
scala> import org.apache.spark.sql.Row
scala> import org.apache.spark.sql.SparkSession
scala> case class Record(key:Int, value:String)
scala> val warehouseLocation = "Spark-warehouse"
scala> val spark = SparkSession.builder().
	| appName("Spark Hive Example").
	| config("spark.sql.warehouse.dir", warehouseLocation).
	| enableHiveSupport().getOrCreate()
scala> import spark.implicits._
scala> import spark.sql
scala> sql("select * from sparktest.student").show()
```

向Hive中写入数据

```scala
scala> import java.util.Properties
scala> import org.apache.spark.sql.types._
scala> import org.apache.spark.sql.Row
scala> val studetnRDD = spark.sparkContext.
	| parallelize(Array("3 Rongcheng M 26", "4 Guanhua F 24")).
	| map(_.split(" "))
scala> val schema = Structtype(List(StructField("id", IntegerType, true), StructField("name", StringType, true), StructField("gender", StringType, true), StructField("age", IntegerType, true)))
scala> val rowRDD = studentRDD.
	| map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))
scala> val studentDF = spark.createDataFrame(rowRDD, schema)

scala> studentDF.show()
scala> studentDF.registerTempTable("temptable")
scala> sql("insert into sparktest.student select * from tempTable")
```



