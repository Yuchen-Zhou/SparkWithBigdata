# Spark编程 Scala版

## 环境配置
- Maven 3.5.4
- Scala 2.12.2
- IDEA 2021 （IDEA的下载可以去官网自行下载）

## Scala版Spark编程详解
- [Scala Basic](./ScalaBasic.md)
- [Maven Tutorial](./Maven_Tutorial/Maven.md)
- [Spark RDD](./RDD.md)
- [Spark DataFrame](./DataFrame.md)
- [Spark Streaming](./Streaming.md)


## 使用IDEA Maven打包Scala
可以参考这篇教程：https://blog.csdn.net/u011470552/article/details/54564636  
重点在于要把多余的jar给删除否则就会出现大问题

pom.xml文件怎么写参考[这里](./pom.xml)

注：我做的实验只用到了spark-core和spark-sql,如果要进行Streaming或者MLlib,可以参考如下结构
```xml
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-组件_Scala版本号</artifactId>
        <version>spark版本号</version>
    </dependency>
```


## 一些Spark编程实例
1.数据：和Python使用的数据是一致的，点击[这里](../../data)查看数据  
2.代码：和Python上的实现效果相同的代码在[这里](./ScalaProject)
