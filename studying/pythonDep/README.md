# Python开发环境配置
如果你没有学过Python（但是你对用Python开发Spark程序感兴趣）或者你的Python基础比较薄弱可以参考我的另一个仓库
点击[这里](https://github.com/Yuchen-Zhou/PythonLearning)进行跳转

在这里我们学习Spark编程使用的是Jupyter，使用Jupyter打开[index.ipynb](./index.ipynb)就可以查看所有代码

当然啦这里的代码并不是直接可以运行的，首先你需要在集群或者伪分布式的机器上安装Python和Jupyter运行代码，其次需要将文件路径进行修改。


**Tips!!!**  
在Spark大数据平台里使用Python需要修改Python的环境变量，将其原来的Python2替换掉

- 1.使用Python3环境   
    在/etc/profile下修改环境变量
    ```shell
    export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.7-src.zip:$PYTHONPATH
    export PYSPARK_PYTHON=这里写你的Python路径/bin/python
    export PYSPARK_DRIVER_PYTHON=和上面一致
    ```
- 2.使用Conda环境
    在/etc/profile下修改环境变量
    
    ```shell
    export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.7-src.zip:$PYTHONPATH
    export PYSPARK_PYTHON=/home/spark/miniconda3/envs/bigdata/bin/python
    export PYSPARK_DRIVER_PYTHON=/home/spark/miniconda3/envs/bigdata/bin/python
    ```

