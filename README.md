# Spark大数据平台笔记

## 0.前言
### 关于编程语言 
这个仓库主要以Python为主要开发语言，当然也会有一些Scala的开发，如果你对Scala开发更感兴趣，可以点[这里](./studying/scalaDep)

### 关于版本使用
- [JDK 1.8.0](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html)
- [Hadoop 2.6.0](https://hadoop.apache.org/release/2.6.0.html)
- [Spark 2.0.0](https://archive.apache.org/dist/spark/spark-2.0.0/)
- [HBase 1.2.4](https://archive.apache.org/dist/hbase/1.2.4/)
- [Maven 3.5.4](https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/)
- [Hive 1.2.2](https://archive.apache.org/dist/hive/hive-1.2.2/)
- [Zookeeper 3.4.5](https://archive.apache.org/dist/zookeeper/zookeeper-3.4.5/)
- [Scala 2.12.2](https://www.scala-lang.org/download/2.12.2.html)
- MySQL 5.7 参考下面MySQL的安装
- [Sqoop 1.4.7](https://archive.apache.org/dist/sqoop/1.4.7/)
- [Flume 1.8.0](https://downloads.apache.org/flume/1.8.0/)


## 1.安装
参考教程: [Hadoop伪分布式](http://dblab.xmu.edu.cn/blog/2441-2/)、[Hadoop集群](http://dblab.xmu.edu.cn/blog/2775-2/)、[Spark安装](http://dblab.xmu.edu.cn/blog/2501-2/)

- ### 1.1JDK安装

  ```shell
  $ tar -zxvf jdk* -C /opt/software/
  ```

- ### 1.2Hadoop安装

  ```shell
  $ tar -zxvf hadoop* -C /opt/software/
  ```

  - 配置文件的编写

    /opt/software/hadoop/etc/hadoop/core-site.xml

  ```xml
  <configuration>
      <property>
          <name>hadoop.tmp.dir</name>
          <value>file:/opt/software/hadoop/tmp</value>
      </property>
      <property>
          <name>fs.defaultFS</name>
          <value>hdfs://localhost:9000</value>
      </property>
  </configuration>
  ```

  /opt/software/hadoop/etc/hadoop/hdfs-site.xml

  ```xml
  <configuration>
      <property>
          <name>dfs.replication</name>
          <value>1</value>
      </property>
      <property>
          <name>dfs.namenode.name.dir</name>
          <value>file:/usr/local/hadoop/tmp/dfs/name</value>
      </property>
      <property>
          <name>dfs.datanode.data.dir</name>
          <value>file:/usr/local/hadoop/tmp/dfs/data</value>
      </property>
  </configuration>
  ```
  mapred-site.xml的配置修改
  ```shell
  $ cp mapred-site.xml.template mapred-site.xml
  $ vim mapred-site.xml
  ```

  ```xml
  <configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
  </configuration>
  ```
  yarn-site.xml的配置修改
  ```shell
  $ vim yarn-site.xml
  ```
  ```xml
  <configuration>
    <property>
      <name>yarn.nodemanager.aux-services</name>
      <value>mapreduce_shuffle</value>
    </property>
  </configuration>
  ```


  /opt/software/hadoop/etc/hadoop/hadoop-env.sh

  ```shell
  #修改JAVA_HOME
  export JAVA_HOME=/opt/software/jdk
  ```

  环境变量/etc/profile

  ```shell
  $ vim /etc/profile
  
  export JAVA_HOME=/opt/software/jdk
  export HADOOP_HOME=/opt/software/hadoop
  export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
  export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib
  
  $ source /etc/profile
  ```

  - 启动Hadoop

  ```shell
  $ cd /opt/software/hadoop
  $ ./sbin/start-dfs.sh
  ```

  编写快捷启动方式: 在家目录下创建start文件

  ```shell
  #! /bin/bash
  #!/bin/bash
  if [ $# -lt 1 ]
  then
      echo "No Args Input..."
      exit ;
  fi
  case $1 in
  "start")
          echo " =================== 启动 hadoop ==================="
  
          echo " --------------- 启动 hdfs ---------------"
          cd /opt/software/hadoop/sbin/
          ./start-dfs.sh
          echo " --------------- 启动 yarn ---------------"
          cd /opt/software/hadoop/sbin/
          ./start-yarn.sh
  ;;
  "stop")
          echo " =================== 关闭 hadoop ==================="
  
          echo " --------------- 关闭 yarn ---------------"
          cd /opt/software/hadoop/sbin/
          ./stop-yarn.sh
          echo " --------------- 关闭 hdfs ---------------"
          cd /opt/software/hadoop/sbin/
          ./stop-dfs.sh
  ;;
  *)
      echo "Input Args Error..."
  ;;
  esac
  ```

  ​	

  - 查看启动情况

  ```shell
  $ jps
  ```



- ### 1.3Spark安装

  ```shell
  $ tar -zxvf spark* -C /opt/software/
  $ cd /opt/software/spark
  $ cp ./conf/spark-env.sh.template ./conf/spark-env.sh
  $ vim ./conf/spark-env.sh
  $ export SPARK_DIST_CLASSPATH=$(/opt/software/hadoop/bin/hadoop classpath)
  ```

  编写环境变量

  ```shell
  $ vim /etc/profile
  	
  export HADOOP_HOME=/opt/software/hadoop
  export SPARK_HOME=/opt/software/spark
  export PATH=$JAVA_HOME/bin:$HADOOP_HOME/bin:$SPARK_HOME/bin:$PATH
  
  $ source /etc/profile
  ```

- ### 1.4MySQL的安装
  参考链接：https://www.linuxprobe.com/centos7-mysql.html

- ### 1.5Hive安装

  ```shell
  $ wget https://archive.apache.org/dist/hive/hive-1.2.1/apache-hive-1.2.1-bin.tar.gz
  $ tar -zxvf apache-hive-1.2.1-bin.tar.gz -C /opt/software/
  $ cd /opt/software/
  $ mv apache-hive-1.2.1-bin/ hive
  ```

  配置环境变量

  ```shell
  $ vim /etc/profile
  
  export HIVE_HOME=/usr/local/hive
  export PATH=$PATH:$HIVE_HOME/bin
  ```

  修改hive-site.xml

  ```shell
  $ cd hive/conf/
  $ cp hive-default.xml.template hive-default.xml
  
  $ vim hive-site.xml
  ```

  添加如下信息

  ```xml
  <?xml version="1.0" encoding="UTF-8" standalone="no"?>
  <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
  <configuration>
    <property>
      <name>javax.jdo.option.ConnectionURL</name>
      <value>jdbc:mysql://localhost:3306/hive?createDatabaseIfNotExist=true</value>
    </property>
    <property>
      <name>javax.jdo.option.ConnectionDriverName</name>
      <value>com.mysql.jdbc.Driver</value>
    </property>
    <property>
      <name>javax.jdo.option.ConnectionUserName</name>
      <value>hive</value>
    </property>
    <property>
      <name>javax.jdo.option.ConnectionPassword</name>
      <value>hive</value>
    </property>
  </configuration>
  ```

  

- ### 1.6Sqoop安装
  [参考链接](http://www.tutorialspoint.com/sqoop/sqoop_installation.htm)

    ```shell
    $ tar -zxvf sqoop-1.4.2.bin__hadoop-0.20.tar.gz -C /opt/software/
    $ mv sqoop-1.4.2.bin__hadoop-0.20.tar.gz sqoop
    ```

    修改配置文件swoop-env.sh

    ```shell
    $ cd sqoop/conf/
    $ cat sqoop-env-template.sh >> sqoop-env.sh
    $ vim sqoop-env.sh
    ```

    ```shell
    export HADOOP_COMMON_HOME=/opt/software/hadoop
    export HADOOP_MAPRED_HOME=/opt/software/hadoop
    export HIVE_HOME=/opt/software/hive
    ```

    修改环境变量

    ```shell
    $ vim /etc/profile
  
    export SQOOP_HOME=/opt/software/sqoop
    export PATH=$PATH:$SQOOP_HOMR/bin
    export CLASSPATH=$CLASSPATH:$SQOOP_HOME/lib
    ```

    将mysql驱动包拷贝到$SQOOP_HOME/lib

    ```shell
    $ cd /opt/package/mysql-connector-java-8.0.27/ && cp ./mysql-connector-java-8.0.27.jar /opt/software/sqoop/lib
    ```

    测试与Mysql的连接

    ```shell
    $ sqoop list-databases --connect jdbc:///127.0.0.1:3306 --username root --password
    ```

    将股票数据通过Sqoop从MySQL导入HDFS

    ```shell
    $ sqoop import \
    --connect jdbc:mysql://127.0.0.1:3306/stock_list \
    --username root \
    --password Zyc0804!  \
    --table stocks \
    -m 1 
    ```

- ### 1.7HBase安装

  ```shell
  $ tar -zxvf hbase-* -C /opt/software
  $ mv hbase* hbase
  $ vim /etc/profile
  
  export HBASE_HOME=/opt/software/hbase
  export PATH=$PATH:$HBASE_HOME/bin
  
  $ source /etc/profile
  ```

  HBase配置文件

  ```shell
  $ vim $HBASE/conf/hbase-env.sh
  
  export JAVA_HOME=/opt/software/jdk
  export HBASE_CLASSPATH=/opt/software/hadoop/conf
  export HBASE_MANAGES_ZK=true 
  ```

  ```shell
  $ vim $HBASE/conf/hbase-site.xml
  ```

  ```xml
  <configuration>
          <property>
                  <name>hbase.rootdir</name>
                  <value>hdfs://localhost:9000/hbase</value>
          </property>
          <property>
                  <name>hbase.cluster.distributed</name>
                  <value>true</value>
          </property>
  </configuration>
  ```

  使用细节参考：http://dblab.xmu.edu.cn/blog/install-hbase/
  
- ### 1.8Kafka安装
  ```shell
  $ tar -zxvf kafka_2.11-2.4.0.tgz -C /opt/software
  $ vim /etc/profile
  
  export KAFKA_HOME=/opt/software/kafka
  export PATH=$PATH:$KAFKA_HOME/bin
  ```
  ```shell
  #启动kafka
  $ kafka-server-start.sh config/server.properties
  
  #关闭kafka
  $ kafka-server-stop.sh config/server.properties
  ```
## 开发环境配置问题
[在Spark上使用Python3的解释器](./studying/py/README.md)
