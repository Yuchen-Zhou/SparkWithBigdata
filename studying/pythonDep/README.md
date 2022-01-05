# Python开发环境配置
如果你没有学过Python（但是你对用Python开发Spark程序感兴趣）或者你的Python基础比较薄弱可以参考我的另一个仓库
点击[这里](https://github.com/Yuchen-Zhou/PythonLearning)进行跳转

1.使用Python3环境   
在/etc/profile下修改环境变量
```shell
export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.7-src.zip:$PYTHONPATH
export PYSPARK_PYTHON=这里写你的Python路径/bin/python
export PYSPARK_DRIVER_PYTHON=和上面一致
```

2.使用Conda环境
在/etc/profile下修改环境变量
```shell
export PYTHONPATH=$SPARK_HOME/python:$SPARK_HOME/python/lib/py4j-0.10.7-src.zip:$PYTHONPATH
export PYSPARK_PYTHON=/home/spark/miniconda3/envs/bigdata/bin/python
export PYSPARK_DRIVER_PYTHON=/home/spark/miniconda3/envs/bigdata/bin/python
```

