# Maven教程

<img src='./maven.jpg'>

### 一、Maven使用的必要性

​	1.一个项目就是一个工程

​		如果说项目组织非常庞大，那么项目十分臃肿，不适合用package划分模块，我们希望一个模块对应一个工程，每个人负责自己模块，分工协作

​		**Maven可以将一个项目拆分成多个工程**

​	2.项目中需要的jar包需要手动复制粘贴至指定目录下

​		每一个项目需要相同的jar包，jar包会被重复包含到每一个项目。那么被大量项目重复使用的jar包会占用存储空间，浪费重复空间

​		**Maven引用jar包，使得重复被使用的jar包只有一份，节省了存储资源**

​	3.jar包需要别人手动准备好，或是到官网下载

​		**Maven可以通过“统一规范”正规下载jar包，然后存储在中央仓库**

​	4.一个jar包依赖的其他jar包，需要自己手动加入到项目中

​		**Maven其实本质上封装了jar包之间的依赖关系，只需要引用所需要的jar包就可以了**



### 二、Maven是什么

​	1.Maven是一款服务于Java平台的自动化构建工具

​		构建工具发展：Make$\rightarrow$Ant$\rightarrow$Maven$\rightarrow$Gradle

​	2.构建

  - 概念：以一系列的资源去生产一个可运行的项目。编译、部署、搭建
  - 编译：java源文件$\rightarrow$编译$\rightarrow$class字节码文件
  - 部署：一个BS项目最终运行的不是工程本身，而是编译后的结果



​	3.构建过程中的各个环节

	- 清理：将以前编译得到的class字节码文件删除，为下一次编译做准备
	- 编译：将Java文件编译得到class文件
	- 测试：自动测试，自动调用junit程序
	- 报告：报告测试结果
	- 打包：动态web工程打包war包，Java打成jar包
	- 安装：将打包过的项目复制到仓库中的指定位置
	- 部署：将动态web程序生成的war包复制到servlet容器下的指定目录，使其可以运行



### 三、Maven的核心概念

​	1.约定的目录结构

​		Maven需要知道源代码的位置进行自动编译，只能按照框架的约定

​		

| 项目根/             |                      |
| ------------------- | -------------------- |
| pom.xml             | Maven2的pom.xml      |
| src/                |                      |
| src/main/           | 项目主体目录根       |
| src/main/java       | 源代码目录           |
| src/main/resources  | 所需资源目录         |
| src/main/filters    | 资源过滤文件目录     |
| src/main/assembly   | Assembly descriptors |
| src/main/config     | 配置文件目录根       |
| src/test            | 项目测试目录根       |
| src/test/java       | 测试代码目录         |
| src/test/resources  | 测试所需资源目录     |
| src/test/filters    | 测试资源过滤文件目录 |
| src/site            | 与site相关的资源目录 |
| target/             | 输出目录根           |
| target/classes      | 项目主体输出目录     |
| target/test-calsses | 项目测试输出目录     |
| target/site         | 项目site输出目录     |

​	2.POM

 - 含义：Project Object Module项目对象模型
 - pom.xml是项目的核心配置文件



​	3.坐标（gav）

 - groupId：公司或者组织域名+项目名
 - artifactId：模块名称
 - version：版本
 - 坐标和仓库路径的对应关系



​	坐标示例

```xml
<groupId>org.springframework</groupId>
<artifactId>spring-core</artifactId>
<version>4.0.0.RELEASE</version>
```

​	对应的仓库路径

```
org/springframework/spring-core-4.0.0.RELEASE/spring-core-4.0.0.RELEASE.jar
```

​	

​	4.依赖

  - maven解析依赖时会到本地仓库去查找被依赖的jar包

  - 依赖范围：

      - compile范围依赖：
          - 对主程序是否有效：有效
          - 对测试程序是否有效：有效
          - 是否参与打包：参与
          - 是否参与部署：参与
          - 典型例子：spring-core
    - test范围依赖
      - 对主程序是否有效：无效
      - 对测试程序是否有效：有效
      - 是否参与打包：不参与
      - 是否参与部署：不参与
      - 典型例子：Junit
    - provided范围依赖
      - 对主程序是否有效：有效
      - 对测试程序是否有效：有效
      - 是否参与打包：不参与
      - 是否参与部署：不参与
      - 典型例子：Servlet-api.jar

- 依赖的传递性：可以的传递不用重复申明；只有compile范围的才能被传递，test和provided范围的在引用和被引用的模块之间都是相互独立的

- 依赖的排除

  - 情景：如果我们当前工程中引入A，A依赖于B，而B的版本不太稳定，我们不希望B对我们的项目造成影响，我们可以在引入A的时候将B排除

  ```xml
  <dependency>
  	<groupId>com.atguigu.maven</groupId>
      <artifactId>HelloFriend</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <type>jar</type>
      <scope>compile</scope>
      <exclusions>
      	<exclusion>
          	<groupId>commons-logging</groupId>
              <artifactId>commons-logging</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  ```

- 依赖的原则：解决jar包冲突

  - 路径最短者优先
  - 路径相同时先申明者优先（dependency申明的顺序决定）



​	5.仓库

 - 仓库分类
    - 本地仓库：电脑本地目录下的仓库，为本地Maven工程服务
    - 远程仓库：
      	- 私服：搭建在局域网环境中，为局域网范围内的所有maven工程服务
      	- 中央仓库：架设在Internet上，为全世界的Maven工程服务
      	- 中央仓库镜像：为了分担中央仓库的流量，分担仓库的访问压力
- 仓库中保存的内容：Maven工程
  - Maven自身所需的插件
  - 第三方框架或工具的jar包
  - 我们自己开发的Maven工程
- 构建项目中依赖包下载的流程
  - 从本地资源库中查到并获得依赖包，如果没有，执行第2步
  - 从Maven默认中央仓库中查找并获得依赖包，如果没有执行第3步
  - 如果在pom.xml中定义了自定义的远程仓库，那么也会在这里的仓库中进行查找并获得依赖包，如果都没有找到，Maven会抛出异常



​	6.生命周期/插件/目标

 - 各个构建环节执行的顺序：不能打乱顺序，必须按照既定的正确顺序来执行
 - Maven的核心程序为了更好的实现自动化构建，按照这一特点执行生命周期中各个阶段：不论现在要执行生命周期中的哪一阶段，都是从这个生命周期最初的位置开始执行的
 - Maven有三套相互独立的生命周期，分别是
   	- Clean Lifecycle在进行真正的构建之前进行一些清理工作
      	- Default Lifecycle构建的核心部分，编译、测试、打包、安装、部署等
   	- Site Lifecycle生成项目报告，站点，发布站点
   - 他们相互独立，也可以直接运行mvn clean install site运行所有这三套生命周期
   - 每套生命周期都有一组阶段(Phase)组成，我们平时在命令行输入的命令总会对应一个特定的阶段。比如，运行mvn clean，这个clean是Clean生命周期的一个阶段。有Clean生命周期，也有clean阶段
- 插件和目标
  - Maven的核心仅仅定义了抽象的申明周期，具体的任务都是交由插件完成的
  - 每个插件都实现了多个功能，每个功能就是一个插件目标
  - Maven的生命周期与插件目标相互绑定，以完成某个具体的构建任务
  - 可以将目标看作“调用插件功能的命令”



​	7.继承

  - 现状：由于test范围的依赖不能传递，所以造成各个版本模块不一致

  - 需求：统一各个模块工程中对junit依赖的版本

  - 解决思路：将junit统一提取到“父”工程中，在子工程中声明junit依赖时不指定版本，以父工程统一设定的为准，同时也便于修改

  - 操作步骤：

      - 创建一个Maven工程作为父工程，以pom方式进行打包

        ```xml
        <groupId>com.zyc</groupId>
        <artifactId>Parent</artifactId>
        <version>1.0-SNAPSHOT</version>
        <packaging>pom</packaging>
        ```

    - 在子工程中声明对父工程的引用

      ```xml
      <parent>
      	<groupId>com.zyc</groupId>
          <artifactId>Parent</artifactId>
          <version>1.o-SNAPSHOT</version>
          <relativePath>../Parent/pom.xml</relativePath>
      </parent>
      ```

    - 将子工程与父工程重复的内容删除（将junit部分删除）

    - 在父工程中统一junit依赖

      ```xml
      <dependencies>
      	<dependency>
          	<groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.11</version>
              <scope>test</scope>
          </dependency>
      </dependencies>
      ```

    - 在子工程删除junit依赖的版本号部分



​	8.聚合

	- 作用：一键安装各个模块工程
	- 相当于多个mvn install各个模块转化为一个mvn install聚合工程





### 四、常用的Maven命令

​	1.注意：执行与构建相关的命令需要进入pom.xml所在的目录

​	2.常用命令

		- mvn clean：清理
		- mvn compile：编译主程序
		- mvn test：编译测试程序
		- mvn package：打包
		- mvn install：安装
		- mvn deploy：部署到服务器上相应路径







### 附A: sparkapp pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>zyc</groupId>
    <artifactId>Sparkproject</artifactId>
    <version>1.0-SNAPSHOT</version>


    <properties>
        <spark.version>2.4.2</spark.version>
        <scala.version>2.12.2</scala.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.12</artifactId>
            <version>2.4.2</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-sql_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-hive_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-mllib_${scala.version}</artifactId>
            <version>${spark.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.scala-tools</groupId>
                <artifactId>maven-scala-plugin</artifactId>
                <version>2.12.2</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.12.2</version>
                <configuration>
                    <skip>true</skip>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

