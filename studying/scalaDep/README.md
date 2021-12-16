# Spark编程 Scala版

## 环境配置
- Maven 3.5.4
- Scala 2.12.2
- IDEA 2021 （IDEA的下载可以去官网自行下载）


## Scala教程
参考菜鸟教程:https://www.runoob.com/scala/scala-tutorial.html

## Maven教程
参考菜鸟教程:https://www.runoob.com/maven/maven-tutorial.html

###  Maven打包Scala的pom.xml
可以[点这里查看](./pom.xml)
注：我做的实验只用到了spark-core和spark-sql,如果要进行Streaming或者MLlib,可以参考如下结构
```xml
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-组件_Scala版本号</artifactId>
        <version>spark版本号</version>
    </dependency>
```


## 使用IDEA Maven打包Scala
可以参考这篇教程：https://blog.csdn.net/u011470552/article/details/54564636  
重点在于要把多余的jar给删除否则就会出现大问题

## 一些Spark编程实例
和Python使用的数据是一致的，点击[这里](../../data)查看数据
