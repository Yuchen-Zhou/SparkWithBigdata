# 基于大数据的机器学习

机器学习是一门人工智能科学，该领域的主要研究对象是人工智能。机器学习利用数据或以往的经验，以此优化计算机程序的性能标准  
机器学习强调三个关键词：算法、经验、性能


- 传统的机器学习算法，由于技术和单机存储的限制，只能在少量数据上使用，依赖数据抽样
- 大数据技术的出现，可以支持在全量数据上进行机器学习
- 机器学习算法涉及大量迭代计算
- 基于磁盘的MapReduce不适合进行大量迭代计算
- 基于内存的Spark比较适合进行大量迭代计算

# 机器学习库MLlib概述

- Spark提供了一个基于海量数据的机器学习库，它提供了常用机器学习算法的分布式实现
- 开发者只需要有Spark基础并且了解机器学习算法的原理，以及方法相关参数的含义，就可以轻松的调用相应的API来实现基于海量数据的机器学习过程
- 需要注意的是，MLlib中只包含能够在集群上运行良好的并行算法，这一点很重要
- 一些较新的研究得出的算法因为使用于集群，也被包含在MLlib中，例如分布式随机森林算法、最小交替二乘法。
- 如果是小规模数据集上训练各机器学习模型，可以是用单节点的机器学习算法库（Weka）
- MLlib由一些通用的机器学习算法和工具组成，包括分类、回归、聚类、协同过滤、降维等,同时还包括底层的优化原语和高层的流水线（Pipeline）API，具体如下：
    - 算法工具：常用的学习算法，如分类、回归、聚类和协同过滤
    - 特征化工具：特征提取、转化、降维和选择工具
    - 流水线(Pipeline)：用于构建、评估和调整机器学习工作流的工具
    - 持久性：保存和加载算法、模型和管道
    - 实用工具：线性代数、统计、数据处理等工具
    
    
    
MLlib目前支持4中常见的机器学习问题：分类、回归、聚类和协同过滤
    
    
  ||离散数据|连续数据|  
  |--|--|--| 
  |监督学习|Classification、LogisticRegression(with Elastic-Net)、SVM、DecisionTree、RandomForest、GBT、NaiveBayes、MultilayerPerceptron、OneVsRest| Regression、LogisticRegression(with Elastic-Net)、DecsionTree、RandomFores、GBT、AFTSurvivalRegression、IsotonicRegression|
  |无监督学习|Clustering、KMeans、GuassianMixture、LDA、PowerIterationClustering、BisectingKMeans| Dimensionality Reduction、 matrix factorization、PCA、SVD、ALS、WLS|

# 基本数据类型
spark.ml包提供了一系列基本数据类型以及支持底层的机器学习算法，主要的数据类型包括本地向量、标注点、本地矩阵等。本地向量与本地矩阵作为公共接口提供简单数据模型，底层的线性代数操作由Breeze库和jblas库提供；标注点类型表示监督学习的训练样本

### 本地向量
本地向量分为稠密向量(DenseVector)和稀疏向量(SparseVector)两种。稠密向量使用双精度浮点型数组来表示每一纬的元素，稀疏向量则是基于一个整型索引数组和一个双精度浮点型的值数组。例如，向量(1.0, 0.0, 3.0)的稠密向量表示形式是[1.0, 0.0, 3.0]，而稀疏向量形式则是(3, [0,2], [1.0, 3.0])，其中，3是向量的长度，[0, 2]是向量中非0维度的索引值，表示位置为0、2的两个元素为非零值，而[1.0, 3.0]则是按索引排列的数组元素值

所有本地向量都以`org.apache.spark.ml.linalg.Vector`为基类，`DenseVector`和`SparseVector`分别是它的两个继承类，故推荐使用Vecotrs工具类下定义的工厂方法来创建本地向量。需要注意的是，Scala会默认引入`scala.collection.immutable.Vector`，如果要使用`spark.ml`包提供的向量类型，要导入`org.apache.spark.ml.linalg.Vector`这个。例如：
```scala
scala> import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.linalg.{Vector, Vectors}

//创建一个稠密本地向量
scala> val dv: Vector = Vectors.dense(2.0, 0.0, 8.0)
dv: org.apache.spark.ml.linalg.Vector = [2.0,0.0,8.0]

//创建一个稀疏本地向量
//方法第二个参数数组指定了非零元素的索引，而第三个参数数组则给定了非零元素值
scala> val sv1: Vector = Vectors.sparse(3, Array(0, 2), Array(2.0, 8.0))
sv1: org.apache.spark.ml.linalg.Vector = (3,[0,2],[2.0,8.0])

//另一种创建稀疏本地向量
//方法的第二个参数是一个序列，其中每个元素都是一个非零值的元组:(index, elem)
scala> val sv2: Vector = Vectors.sparse(3, Seq((0, 2.0), (2, 8.0)))
sv2: org.apache.spark.ml.linalg.Vector = (3,[0,2],[2.0,8.0])
```

### 标注点
标注点(Labeled Point)是一种带有标签(Label/Response)的本地向量，通常用在监督学习算法中，它可以是稠密或者稀疏的。由于标签是用双精度浮点型来存储的，因此，标注点类型在回归(Regression)和分类(Classification)问题上均可使用。例如，对于二分类问题，则正样本的标签为1，负样本的标签为0；对于多分类别的分类问题来说，标签则应是一个以0开始的索引序列:0,1,2,...

标注点的实现类是`org.apache.spark.feature.LabeledPoint`，位于`org.apache.spark.ml.feature`包下，标注点的创建方法如下：
```scala
scala> import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.linalg.Vectors

scala> import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.feature.LabeledPoint

//下面创建一个标签为1.0（分类中可视为正样本）的稠密向量标注点
scala> val pos = LabeledPoint(1.0, Vectors.dense(2.0, 0.0, 8.0))
pos: org.apache.spark.ml.feature.LabeledPoint = (1.0,[2.0,0.0,8.0])

//创建一个标签为0.0（分类中可视为负样本）的稀疏向量标注点
scala> val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(2.0, 8.0)))
neg: org.apache.spark.ml.feature.LabeledPoint = (0.0,(3,[0,2],[2.0,8.0]))
```

在实际的机器学习问题中，稀疏向量数据是非常常见的，MLlib提取了读取LIBSVM格式数据的支持，该格式被广泛用于LIBSVM、LIBLINEAR等机器学习库。该格式下，每一个带有标签的样本点由以下格式表示：  
`label index1:value1    index2:value2   index3:value3 ···`  
其中，label是该样本点的标签值，一系列`index:value`则代表了该样本向量中所有非零元素的索引和元素值。需要特别注意的是，index是以1开始并递增的。

下面读取一个LIBSVM格式文件生成向量:
```scala
scala> val examples = spark.read.format("libsvm").load("file:///opt/spark/data/mllib/sample_libsvm_data.txt")
examples: org.apache.spark.sql.DataFrame = [label: double, features: vector]
```

这里，spark是spark-shell自动建立的`SparkSession`，它的read属性是`org.apache.spark.sql`包下名为DataFrameReader类的对象，该对象提供了读取LIBSVM格式的方法，使用非常方便。下面继续查看加载进来的标注点的值:
```scala
scala> examples.collect().head
res0: org.apache.spark.sql.Row = [0.0,(692,[127,128,129,130,131,154,155,156,157,158,159,181,182,183,184,185,186,187,188,189,207,208,209,210,211,212,213,214,215,216,217,235,236,237,238,239,240,241,242,243,244,245,262,263,264,265,266,267,268,269,270,271,272,273,289,290,291,292,293,294,295,296,297,300,301,302,316,317,318,319,320,321,328,329,330,343,344,345,346,347,348,349,356,357,358,371,372,373,374,384,385,386,399,400,401,412,413,414,426,427,428,429,440,441,442,454,455,456,457,466,467,468,469,470,482,483,484,493,494,495,496,497,510,511,512,520,521,522,523,538,539,540,547,548,549,550,566,567,568,569,570,571,572,573,574,575,576,577,578,594,595,596,597,598,599,600,601,602,603,604,622,623,624,625,626,627,628,629,630,651,652,653,654,655,656,657],[51.0,159.0,253.0,159.0,50.0,48.0,238.0,252.0,25...
```
这里，`examples.collect()`把RDD转换为了向量，并取第一个元素的值。每个标注点共有692个维，其中，第127列对应的值是51.0，第128列对应的值是159.0，以此类推

### 本地矩阵
本地矩阵具有整型的行、列索引值和双精度浮点型的元素值，他存储在单机上。MLlib支持稠密矩阵`DenseMatrix`和稀疏矩阵`SparseMatrix`两种本地矩阵。稠密矩阵将所有元素的值存储在一个列优先(Column-major)的双精度型数组中，而稀疏矩阵则将非零元素以列优先的CSC(Compressed Sparse Column)模式进行存储。

本地矩阵的基类是`org.apache.spark.ml.linalg.Matrix`,`DenseMatrix`和`SparseMatrix`均是他的继承类。和本地向量类似，`spark.ml`包也为本地矩阵提供了相应的工具类Matrices，调用工厂方法即可创建实例。下面创建一个稠密矩阵：  
```scala
scala> import org.apache.spark.ml.linalg.{Matrix, Matrices}
import org.apache.spark.ml.linalg.{Matrix, Matrices}

//下面创建一个3行2列的稠密矩阵[ [1.0, 2.0], [3.0, 4.0], [5.0, 6.0]]
//这里的数组参数是列优先，即按照的方式从数组中提取元素
scala> val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
dm: org.apache.spark.ml.linalg.Matrix =
1.0  2.0
3.0  4.0
5.0  6.0
```

下面继续创建一个稀疏矩阵
```scala
//创建一个3行2列的稀疏矩阵[ [9.0, 0.0], [0.0, 8.0], [0.0, 6.0]]
//第一组数组参数表示列指针，即每一列元素的开始索引值
//第二个数组参数表示行索引，即对应的元素是属于哪一行
//第三个数组即是按列优先排列的所有非零元素，通过列指针和行索引即可判断每个元素所在的位置
scala> val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))
sm: org.apache.spark.ml.linalg.Matrix =
3 x 2 CSCMatrix
(0,0) 9.0
(2,1) 6.0
(1,1) 8.0
```

这里创建了一个3行2列的稀疏矩阵[ [9.0, 0.0], [0.0, 8.0], [0.0, 6.0]]。`Matrices.sparse`的参数中，3表示行数，2表示列数。第1个数组参数表示列指针，其长度=列数+1，表示每一列元素的开始索引值。第2个数组参数表示行索引，即对应的元素是属于哪一列，其长度=非零元素的个数。第3个数第2列有2个(=3-1)元素；第二个数组(0, 2, 1)表示共有3个元素，分别在第0、2、1行

# 机器学习流水线
## 机器学习流水线概念
- DataFrame：使用Spark SQL中的DataFrame作为数据集，它可以容纳各种数据类型。较之RDD，DataFrame包含了schema信息，更类似传统数据库的二维表格。
- 它被ML Pipeline用来存储源数据。例如，DataFrame中的列可以是存储的文本、特征向量、真实标签和预测的标签等

- Transformer：转换器，是一种可以将一个DataFrame转换为另一个DataFrame的算法。比如一个模型就是一个Transformer。它可以把一个不包含预测标签等测试数据集DataFrame打上标签，转换成另一个包含预测标签的DataFrame
- Estimator：估计器或评估器，它是学习算法或在训练数据上的训练方法的概念抽象。在Pipeline里通常是被用来操作DataFrame数据并生成一个Transformer。从技术上讲，Estimator实现了一个方法fit()，它接受一个DataFrame并产生一个转换器。比如一个随机森林算法就是一个Estimator，它可以调用fit()，通过训练特征数据而得到一个随机深林算法
- Parameter：Parameter被用来设置Transformer或者Estimator的参数。现在，所有转换器和评估器可共享用于指定参数的公共API。ParamMap是一组（参数，值）对
- PipeLine：流水线或者管道。流水线将多个工作流阶段（转换器和评估器）连接在一起，形成机器学习的工作流，并获得结果输出。


## 流水线工作流程
要构建一个Pipeline流水线，首先需要定义Pipeline中的各个流水线阶段PipelineStage(包括转换器和评估器），比如指标提取和转换模型训练等。有了这些处理特定问题的转换器和评估器，就可以按照具体的处理逻辑有序地组织PipelineStages并创建一个Pipeline

```scala
val pipeline = new Pipeline().setStages(Array(stage1, stage2, stage3, ...))
```

在一个流水线中，上一个PipelineStage的输出，恰好是下一个PipelineStage的输入。流水线建好以后，就可以把训练数据集作为输入参数，调用流水线实例的fit()方法，以流的方式来处理源训练数据。该调用会返回一个PipelineModel类的实例，进而被用来预测测试数据的标签。更具体地说，流水线的各个阶段按顺序运行，输入的DataFrame在它通过每个阶段时会被转换，对于转换器阶段，在DataFrame上会调用`transform()`方法，对于评估器阶段，先调用fit()方法来生成一个转换器，然后在DataFrame上调用该转换器的transform()方法。

例如，如下图所示，一个流水线具有3个阶段，前两个阶段(Tokenizer和HashingTF)是转换器，第三个阶段(LogisticRegression)是评估器。图中
下面一行表示流经这个流水线的数据，其中，圆柱表示`DataFrame`。在原始DataFrame上调用`Pipeline.fit()`方法执行流水线，每个阶段运行流程如下：

(1)在Tokenizer阶段，调用`transform()`方法将原始文本文档拆分为单词，并向DataFrame添加一个带有单词的新列；  
(2)在HashingTF阶段，调用其`transform()`方法将DataFrame中的单词列转换为特征向量，并将这些向量作为一个新列添加到DataFrame中  
(3)在LogisticsRegression阶段，由于它是一个评估器，因此会调用`LogisticRegresion.fit()`产生一个转换器`LogisticRegresionModel`；如果工作流有更多的阶段，则在将DataFrame传递到下一阶段之前，会调用LogisticsRegressionModel的transform()方法。

<img src='./pics/27.png' width='80%'>

流水线本身就是一个评估器，因此，在流水线的fit()方法运行之后，会产生一个流水线模型(PipelineModel)，这是一个转换器，可在测试数据的时候使用。如下图所示，PipelineModel具有与原流水线相同的阶段数，但是，原流水线中的所有评估器。调用PipelineModel的transform()方法时，测试数据按顺序通过流水线的各个阶段，每个阶段的transform()方法更新数据集（DataFrame），并将其传递到下一个阶段。通过这种方式，流水线和PipelineModel确保了训练和测试数据通过相同的特征处理步骤。这里给出的示例都是用于线性流水线的，即流水线中每个阶段使用由前一阶段产生的数据，但是，也可以构建一个有向无环图（DAG）形式的流水线，以拓扑顺序指定每个阶段的输入和输出列名称。流水线的阶段必须时唯一的实例，相同的实例不应该两次插入流水线。但是，具有相同类型的两个阶段实例，可以放在同一个流水线中，流水线将使用不同的ID创建不同的实例。此外，DataFrame会对各个阶段的数据类型进行描述，流水线和流水线模型(PipelineModel)会在实际运行流水线之前，做类型的运行时检查，但不能使用编译时的类型检查。

<img src='./pics/28.png' width='80%'>



MLlib评估器和转换器，使用统一的API指定参数。其中，Param是一个自描述包含文档的命名参数，而ParamMap是一组(参数,值)对。将参数传递给算法主要有两种方法：
- 设置实例的参数。例如，lr是一个LogisticRegression实例，用`lr.setMaxIter(10)`进行参数设置后，可以使`lr.fit()`最多迭代10次
- 传递ParamMap给fit()或transform()函数。ParamMap中的任何参数，将覆盖先前通过set方法指定的参数

需要注意的是参数同时属于评估器和转换器的特定实例。如果同一个流水线中的两个算法实例（比如LogisticRegression实例lr1和lr2），都需要设置`maxItera`参数，则可以建立一个ParamMap，即`ParamMap(lr1.maxIter -> 10, lr2.maxIter -> 20)`，然后传递给这个流水线



# 特征提取、转换和选择
机器学习过程中，输入的数据格式多种多样，为了满足相应机器学习算法的格式，一般都需要对数据进行预处理。特征处理相关的算法大体分为以下3类。

(1)特征提取：从原始数据中抽取特征；  
(2)特征转换：缩放、转换或修改特征；   
(3)特征选择：从较大特征集中选取特征子集  

## 特征提取
特征提取(Feature Extraction)是指利用已有的特征计算出一个抽象程度更高的特征集，也指计算得到某个特征的算法。

**1.特征提取操作**  
spark.ml包提供的提取操作包括以下几种：  
(1)TF-IDF。词频-逆向文件频率(Term Frequency_Inverse Document Frequency, TF-IDF)是文本挖掘领域常用的特征提取方法。给定一个语料库，TF-IDF通过词汇在语料库中出现次数和在文档中出现次数，来衡量每个词汇对文档的重要程度，进而构建基于语料库的文档的向量化表达。  
(2)Word2Vec。Word2Vec是由Google提出的一种词前乳(Word Embedding)向量化模型。有CBOW和Skip-gram两种模型，spark.ml使用的是后者。
(3)CountVectorizer。CountVectorizer可以看成是TF-IDF的退化版本，它仅通过度量每个词汇在文档中出现的次数（词频），来为每一个文档构建出向量化表达，可以通过设置超参数来限制向量维度，过滤掉出现较少的词汇

**2.特征提取的例子**  
“词频-逆向文件频率”(TF-IDF)是一种在文本挖掘中广泛使用的特征向量化方法，它可以体现一个文档中词语在语料库中的重要程度。

词语由t表示，文档由d表示，语料库由D表示。词频TF(t, d)是词语t在文档d中出现的次数。文件频率DF(t, D)是包含词语的文档的个数。TF-IDF就是在数值化文档信息，衡量词语能够提供多少信息以区分文档。其定义如下:
$$
        IDF(t,D) = log \frac {|D| + 1}{DF(t,D)+1} \\
$$

其中，`|D|`是语料库中总的文档数。公式中使用log函数，当词出现在所有文档中时，它的IDF值变为0。`DF(t, D)+1`是为了避免分母为0的情况。TF-IDF度量值表示如下：

$$

        TFIDF(t,d,D) = TF(t,d)·IDF(t,D)
$$

在Spark ML库中，TF-IDF被分成两部分：
- TF(+hashing)
    HashingTF是一个Transformer，在文本处理中，接收词条的集合然后把这些集合转化为固定长度的特征向量。这个算法在哈希的同时会统计各个词条的词频。
        
- IDF
    IDF是一个Estimator，在一个数据集上应用它的fit()方法，产生一个IDFModel。该IDFModel接收特征向量（由HashingTF产生），然后计算每一个词在文档中出现的频次。IDF会减少那些在语料库中出现频率较高的词的权重。


