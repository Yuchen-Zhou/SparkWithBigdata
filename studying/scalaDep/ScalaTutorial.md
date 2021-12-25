# 			Scala Tutorial

### 简介

Scala是一门类Java的多范式语言，它整合了面向对象编程和函数式编程的最佳特性

- Scala运行于Java虚拟机（JVM）之上，并且兼容现有的Java程序
- Scala是一门纯粹的面向对象的语言
- Scala也是一门函数式语言

### Scala基础

#### 基本数据类型和变量

##### 1.基本数据类型

- Scala的数据类型包括：Byte、Char、Short、Int、Long、Float、Double和Boolean

- 和Java不同的是，在Scala中，这些类型都是“类”，并且都是包Scala的成员，比如，Int的全名是scala.Int。对于字符串，Scala用java.lang.String类来表示字符串

  | 值类型  | 范围                               |
  | ------- | ---------------------------------- |
  | Byte    | 8位有符号补码整数(-2^7^~2^7^-1)    |
  | Short   | 16位有符号补码整数(-2^15^~2^15^-1) |
  | Int     | 32位有符号补码整数(-2^31^~2^31^-1) |
  | Long    | 64位有符号补码整数(-2^63^~2^63^-1) |
  | Char    | 16位无符号Unicode字符(0~2^16^-1)   |
  | String  | 字符序列                           |
  | Float   | 32位IEEE754单精度浮点数            |
  | Double  | 64位IEEE754单精度浮点数            |
  | Boolean | true或false                        |

##### 2.字面量(literal)

```scala
val i = 123
val i = 3.14
val i = true
val i = 'A'
val i = "Hello"
```

##### 3.操作符

- 算术运算符：加(+)、减(-)、乘(*)、除(/)、余数(%)

- 关系运算符：大于(>)、小于(<)、等于(==)、不等于(!=)、大于等于(>=)、小于等于(<=)

- 逻辑运算符：逻辑与(&&)、逻辑或(||)、逻辑非(!)

- 位运算符：按位与(&)、按位或(|)、按位异或(^)、按位取反(~)

- 赋值运算符：=及其与其他运算符结合的扩展赋值运算符，例如+=、%=

  操作符优先级：算术运算符>关系运算符>逻辑运算符>赋值运算符

  在Scala中，操作符就是方法

  ```scala
  scala> val sum1 = 5 + 3
  sum1: Int = 8
  scala> val sum2 = (5). + (3)
  sum2: Int = 8
  ```

  富包装类

  - 对于基本数据类型，除了以上提到的各种操作符外，Scala还提供了许多常用运算的方法，只是这些方法不是在基本类里面定义，而是被封装到一个对应的富包装类中
  - 每个基本类型都有一个对应的富包装类，例如Int有一个RichInt类、String有一个RichString类，这些类位于包scala.runtime中
  - 当对一个基本数据类型的对象调用其富包装类提供的方法，Scala会自动通过隐式转换将该对象转换成对应的富包装类型，然后再调用相应的方法。

##### 4.变量

​	Scala有两种类型的变量：

 - val：是不可变的，在声明时就必须被初始化，而且初始化就不能再赋值

 - var：可变的，声明的时候需要进行初始化，初始化后还可以再次对其进行赋值

   类型推断机制(type inference)：根据初始值自动推断变量的类型，使得定义变量时可以省略具体的数据类型及其前面的冒号

   ```scala
   scala> val myStr = "Hello World"
   myStr: String = Hello World!
   
   scala> val myStr2: String = "Hello World"
   myStr2: String = Hello World!
   
   scala> println(myStr)
   Hello World!
   ```

#### 输入输出

##### 1.控制台输入输出语句

- 从控制台读入数据的方法：readInt、readDouble、readByte、readShort、readFloat、readLong、readChar、readBoolean及readLine，分别对应9种基本数据类型，其中前8种方法没有参数，readLine可以不提供参数，也可以带一个字符串参数的提示

- 所有这些函数都属于对象scala.io.StdIn的方法，使用前必须导入，或者直接用全称进行调用

  ```scala
  scala> import io.StdIn._
  import io.StdIn._
  
  scala> var i=readInt()
  54
  i: Int = 54
  scala> var f=readFloat
  1.618
  f:Float = 1.618
  scala> var b=readBoolean
  true
  b:Boolean = true
  scala> var str=readLine("please input your name:")
  please input your name:Li Lei
  str:String = Li Lei
  ```

  向控制台输出信息方法：

   - print()和println()，可以直接输出字符串或者其他数据类型，其中println在末尾自动换行

     ```scala
     scala> val i = 345
     i:Int = 345
     scala> print("i=");print(i)
     //两条语句位于同一行，不能省略中间的分号
     i = 345
     scala> println("hello");println("world")
     hello
     world
     ```

  - C语言风格格式化字符串的printf()函数

    ```scala
    scala> val i = 34
    i:Int = 34
    scala> val f=56.6
    f:Double = 56.6
    scala> printf("I am %d years old and weight %.1f Kg.", "Li Lie", i, f)
    I am 34 years old and weight 56.6 Kg.
    ```

    print()、println()和printf()都在对象Predef中定义，该对象默认情况下被所有Scala程序引用，因此可以直接使用Predef对象提供的方法，而无需使用scala.Predef.的形式

    ```scala
    scala> val i=10
    i:Int = 10
    scala> val f=3.5
    f:Double = 3.5452
    scala> val s="hello"
    s:String = hello
    scala> println(s"$s:i=$i,f=$f")
    hello:i=10,f=3.5452
    scala> println(f"$s:i=$i%-4d,f=$f%.1f")
    hello:i=10 ,f=3.5
    ```

##### 2.读写文件

Scala需要使用java.io.PrintWriter实现把数据写入到文件，PrintWriter类提供了print和println两个写方法

```scala
scala> import java.io.PrintWriter
scala> val outputFile = new PrintWriter("text.txt")
scala> outputFile.println("Hello World")
scala> outputFile.print("Spark is good")
scala> outputFile.close()
```

读取文件

```scala
scala> import scala.io.Source
import scala.io.Source
scala> val inputFile = Source.fromFile("output.txt")
inputFile: scala.io.BufferedSource = non-empty iterator

scala> val lines = inputFile.getLines
lines: Iterator[String] = non-empty iterator
scala> for (line <- lines) println(line)
```

#### 控制结构

##### 1.if条件表达式

```scala
val x = 6
if (x>0) { println("This is a positive number")}
else { println("This is not a positive number")}

val x = 3
if (x>0){println("This is a positive number")}
else if (x==0){println("This is a zero")}
else{println("This is a negative number")}

//与Java不同的是，Scala中的if表达式的值可以赋值给变量
val x = 6
val a = if (x>0) 1 else -1
```

##### 2.while循环

```scala
var i = 9
while (i > 0){
    i -= 1
    printf("i is %d\n", i)
}

var i = 0
do{
    i += 1
    println(i)
}while (i<5)
```

##### 3.for循环

基本语法

```
for (变量 <- 表达式){语句块}
```

其中，“变量<-表达式”被称为“生成器(generator)”

```scala
for (i <- 1 to 5) println(i)
1
2
3
4
5

for(i <- 1 to 5 by 2) println(i)
1
3
5
```

"守卫(guard)"的表达式：过滤出一些满足条件的结果

```scala
for (i <- 1 to 5 if i%2==0) println(i)
```

Scala也支持“多个生成器”的情形，可以用分号把它们隔开，比如：

```scala
for (i <- 1 to 5; j <- 1 to 3) println(i*j)
1
2
3
2
4
6
9
4
8
12
6
10
15
```

for推导式：for结构可以在每次执行的时候创造一个值，然后将包含了所有产生值的集合作为for循环表达式的结果返回，集合的类型由生成器中的集合类型确定

```scala
scala> val r = for(i <- Array(1, 2, 3, 4, 5) if i%2==0) yield{
    println(i); i
}
2
4
r: Array[Int] = Array(2, 4)
```

##### 4.异常处理

Scala不支持Java中的“受检查异常”(checked exception),将所有异常都当作“不受检异常”（或称为运行时异常）

```scala
import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException
try {
    val f = new FileReader("input.txt")
} catch {
    case ex : FileNotFoundException =>
    case ex : IOException =>
} finally {
    file.close()
}
```

5.对循环的控制

- 为了提前终止整个循环或者跳到下一个循环，Scala没有break和continue关键字
- Scala提供了一个Breaks类，Breaks类有两个方法用于对循环结构进行控制，即breakable和break

```scala
import util.control.Breaks._
val array=Array(1,3,10,5,4)
breakable{
for(i <- array){
	if(i>5) break
println(i)
    }
}
//上面的for语句将输出1，3

for(i <- array){
    breakable{
        if(i>5) break
        println(i)
    }
}
//输出1，3，5，4
```

#### 数据结构

##### 1.数组(Array)

数组：一种可变的、可索引的、元素具有相同类型的数据集合

Scala提供了参数化类型的通用数组类Array[T]，其中T可以是任意的Scala类型，可以通过显式指定类型或者通过隐式推断来实例化一个数组

```scala
val intValueArr = new Array[Int](3)

intValueArr(0) = 12
intValueArr(1) = 45
intValueArr(2) = 33
```

声明一个字符串数组

```scala
val myStrArr = new Array[String](3)
//声明一个长度为3的字符串数组，每个数组元素初始化为null

myStrArr(0) = "BigData"
myStrArr(1) = "Hadoop"
myStrArr(2) = "Spark"
for (i <- 0 to 2) println(myStrArr(i))
```

可以不给出数组类型，Scala会自动根据提供的初始化数据来推断出数组的类型

```scala
val intValueArr = Array(12, 45, 33)
val myStrArr = Array("BigData", "Hadoop", "Spark")
```

多维数组的创建：调用Array的ofDim方法

```scala
val myMatrix = Array.ofDim[Int](3,4)
//实际类型就是Array[Array[Int]]
val myCube = Array.ofDim[String](3, 2, 4)
//实际类型就是Array[Array[Array[Int]]]
```

可以使用多级圆括号来访问多维数组的元素，例如myMatrix(0)(1)返回第一行第二列的元素

##### 2.元组(Tuple)

元组是对多个不同类型对象的一种简单封装。定义元组最简单的方法就是把多个元素用逗号分开并用圆括号包围起来。使用下划线"_"加上从1开始的索引值，来访问元组的元素

```scala
scala> val tuple = ("BigData", 2021,  45.0)
tuple: (String, Int, Double) = (BigData, 2021, 45.0)

scala> println(tuple._1)
BigData
scala> println(tuple._2)
2021
scala> println(tuple._3)
45.0
```

如果需要在方法里返回多个不同类型的对象，Scala可以通过返回一个元组来实现

##### 3.容器(Collection)

- Scala提供了一套丰富的容器(collection)库，包括序列(Sequence)、集合(Set)、映射(Map)等
- Scala用了三个包来组织容器类，分别是scala.collection、scala.collection.mutable和scala.collection.immutable
- scala.collection封装了可变容器和不可变容器的超类或特质，定义了可变容器和不可变容器的一些通用操作

##### 4.序列(Sequence)

序列(Sequence)：元素可以按照特定的顺序访问的容器。序列中每个元素均带有一个从0开始计数的固定索引位置。  

序列容器的根是collection.Seq特质。其具有两个子特质LinearSeq和IndexedSeq。LinearSeq序列具有高效的head和tail操作，而IndexedSeq序列具有高效的随机存储操作。 

实现了特质LinearSeq的常用序列有列表(List)和队列(Queue)。实现了特质IndexedSeq的常用序列有可变数组(ArrayBuffer)和向量(Vector).  

###### 序列-列表(List)

- 列表：一种共享相同类型的不可变的对象序列。定义在scala.collection.immutable包中
- 不同于Java的java.util.List，scala的List一旦被定义，其值就不能被改变，因此声明List时，必须初始化
- 列表有头部和尾部的概念，可以分别使用head和tail方法来获取
- head返回的是列表第一个元素的值
- tail返回的是除第一个元素外的其他值构成的新列表，这体现出列表具有递归的链表结构
- strList.head将返回字符串“BigData”，strList.tail返回List("Hadoop", "Spark")

###### 序列-向量(Vector)

```scala
scala> val vec1=Vector(1,2)
vec1:scala.collection.immutable.Vector[Int] = Vector(1, 2, 3)
scala> val vec2= 3 +: 4 +: vec1
vec2:scala.collection.immutable.Vector[Int] = Vector(3,4,1,2)
scala> val vec3= vec2 :+ 5
vec3:scala.collection.immutable.Vector[Int] = Vector(3,4,1,2,5)
scala> vec3(3)
res1: Int = 2
```

###### 序列-Range

- Range类：一种特殊的、带索引的不可变数字等差序列。其包含的值为从给定起点按一定步长增长（减小）到指定重点的所有数值
- Range可以支持创建不同数据类型的数值序列，包括Int、Long、Float、Double、Char、BigInt和BigDecimal等

```scala
//(1)创建一个从1到5的数值序列，包含区间终点5，步长为1
scala> val r = new Range(1, 5, 1)
scala> 1 to 5
res: scala.collection.immutable.Range.Inclusive = Range(1,2,3,4,5)
scala> 1.to(5)
res: scala.collection.immutable.Range.Inclusive = Range(1,2,3,4,5)

//(2)创建一个从1到5的数值序列，不包含区间终点5，步长为1
scala> 1 until 5
res: scala.collection.immutable.Range = Range(1,2,3,4)

//(3)创建一个从1到10点数值序列，包含区间终点10，步长为2
scala> 1 to 10 by 2
res: scala.collection.immutable.Range = Range(1,3,5,7,9)

//(4)创建一个Float类型的数值序列，从0.5f到5.9f，步长为0.3f
scala> 0.5f to 5.9f by 0.8f
res0: scala.collection.immutable.NumericRange[Float] = NumericRange 0.5 to 5.9 by 0.3
```

##### 5.集合(Set)

- 集合(Set)：不重复元素的容器(collection)

- 列表(List)中的元素是按照插入的先后顺序来组织的，但是，“集合”中的元素并不会记录元素的插入顺序，而是以“哈希”方法对元素的值进行组织，所以，它允许你快速地找到某个元素

- 集合包括可变集和不可变集，分别位于scala.collection.mutable包和scala.collection.immutable包，缺省情况下创建的是不可变集

  ```scala
  var mySet = Set("Hadoop", "Spark")
  mySet += "Scala"
  ```

  如果要声明一个可变集，则需要提前引入scala.collection.mutable.Set

  ```scala
  import scala.collection.mutable.Set
  val myMutableSet = Set("Database", "BigData")
  myMutableSet += "Cloud Computing"
  ```

##### 6.映射(Map)

- 映射(Map)：一系列键值对的容器。键是唯一的，但值不一定是唯一的。可以根据键来对值进行快速的检索

- Scala的映射包含了可变的和不可变的两种版本，分别定义在包scala.collection.mutable和scala.collection.immutable里。默认情况下，Scala中使用不可变的映射。如果想使用可变映射，必须明确地导入scala.collection.mutable.Map

  ```scala
  val university = Map("XMU" -> "Xiamen University", "THU" -> "Tsinghua University", "PKU" -> "Peking University")
  
  //如果想要获取映射中的值，可以通过键来获取
  println(university("XMU"))
  
  //对于这种方式的访问，如果给定的键不存在，则会跑出异常
  val xmu = if(university.contains("XMU"))
  university("XMU") else 0 println(xmu)
  ```

  可变的映射

  ```scala
  import scala.collection.mutable.Map
  val university2 = Map("XMU" -> "Xiamen University", "THU" -> "Tsinghua University", "PKU" -> "Peking University")
  university2("XMU") = "Xiamen University"
  university2("FZU") = "Fuzhou University"
  
  //也可以使用+=操作来添加新元素
  university2 += ("TJU" -> "Tianjin University")
  university2 += ("SDU" -> "Shandong University", "ZJU" -> "Zhejiang University")
  ```

##### 7.迭代器(Iterator)

- 迭代器(Iterator)不是一个容器，而是提供了按顺序访问容器元素的数据结构

- 迭代器包含两个基本擦欧总：next和hasNext。next可以返回迭代器的下一个元素，hasNext用于检测是否还有下一个元素

  ```scala
  val iter = Iterator("Hadoop", "Spark", "Scala")
  while (iter.hasNext){
      println(iter.next())
  }
  ```

  

### 面向对象编程基础

#### 类

##### 1.类的定义

```scala
class Counter{
	//这里定义类的字段和方法
}
```

字段定义：用val或var关键字进行定义

方法定义：

```scala
def 方法名(参数列表)：返回结果类型={方法体}

class Counter{
    var value = 0
    def increment(step:Int):Unit = {value += step}
    def current():Int = {value}
}
```

使用new关键字创建一个类的实例

```scala
val myCounter = new Counter
myCounter.value = 5
myCounter.increment(3)
println(myCounter.current)
```

##### 2.类成员的可见性

- Scala类中所有成员的默认可见性为公有，任何作用域内都能直接访问公有成员
- 除了默认的公有可见性，Scala也提供private和protected，其中，private成员只对本类型和嵌套类型可见；protected成员对本类型和其继承类型都可见

为了避免直接暴露public字段，建议将字段设置为private，对于private字段，Scala采用类似Java中的getter和setter方法，定义了两对的方法value和value_=进行读取和修改

```scala
class Counter{
	private var privateValue = 0
    def value = privateValue
    def value_= (newValue: Int) {
        if (newValue > 0) privateValue = newValue
    }
    def increment(step: Int) : Unit = {value += step}
    def current(): Int = {value}
}
```

##### 3.方法的定义方式

基本语法：

`def 方法名(参数列表): 返回结果类型={方法体}`

- 方法参数前不能加上val或var，所有的方法参数都是不可变类型
- 无参数的方法定义时可以省略括号，这时调用时也不能带有括号；如果定义时带有括号，则调用时可以带括号，也可以不带括号
- 方法名后面的圆括号()可以用大括号{}来代替
- 如果方法只有一个参数，可以省略号(.)而采用中缀操作符调用方法
- 如果方法体只有一条语句，可以省略方法体两边的大括号

```scala
class Counter{
    var value = 0
    def increment(step:Int):Unit = {value += step}
    def current:Int = value
    def getValue():Int = value
}
```

- 当方法的返回结果类型可以从最后的表达式推断出时，可以省略结果类型
- 如果方法返回类型为Uint，可以同时省略返回结果类型和等号，但不能省略大括号

```scala
class Counter{
	var value = 0
    def increment(step:Int){value += step}
    def current()=value
}
```

##### 4.构造器

- Scala类的定义主体就是类的构造器，称为主构造器。在类名之后用圆括号列出主构造器的参数列表
- 主构造器的参数前可以使用val或var关键字，Scala内部将自动为这些参数创建私有字段，并提供对应的访问方法

```scala
scala> class Counter(var name:String)
defined class Counter

scala> var mycounter = new Counter("Runner")
mycounter: Counter = Counter@d190639

scala> println(mycounter.name)
Runner

scala> mycounter.name_=("Timer")
scala> mycounter.name = "Timer"
mycounter.name:String = Timer
```

- Scala类可以包含零个或多个辅助构造器(auxiliary constructor).辅助构造器使用this进行定义，this的返回类型为Unit
- 每个辅助构造器的第一个表达式必须是调用一个此前已经定义的辅助构造器或主构造器，调用的形式为"this"

```scala
class Counter{
    private var value = 0
    private var name = ""
    private var step = 1
    println("the main constructor")
    def this(name:String){
        this()
        this.name = name
        printf("the first auxiliary constructor, name:%s\n", name)
    }
    
    def this(name:String, step:Int){
        this(name)
        this.step = step
        printf("the second auxiliary constructor, name:%s, step:%d\n", name, step)
    }
    def increment(step:Int):Unit = {value+=step}
    def current():Int = {value}
}
```

#### 对象

##### 1.单例对象

- Scala采用单例对象(Singleton object)来实现静态与Java静态成员同样的功能
- 使用object关键字定义单例对象

```scala
object Person{
    private var lastId = 0
    def newPersonId() = {
        lastId += 1
        lastId
    }
}
```

***伴生对象和孤立对象***

- 当一个单例对象和它的同名类一起出现时，这时的单例对象被称为这个同名类的“伴生对象”(companion object)。相应的类被称为这个单例对象的“伴生类”
- 类和它的伴生对象必须存在于同一个文件中，可以相互访问私有成员
- 没有同名类的单例对象，被称为孤立对象(standalone object)。一般情况下，Scala程序的入口点main方法就是定义在一个孤立对象里

```scala
class Person(val name:String){
    private val id = Person.newPersonId()
    def info() {
        printf("The id of %s is %d.\n", name, id)
    }
}

object Person{
    private var lastId = 0
    def newPersonId() = {
        lastId += 1
        lastId
    }
    
    def main(args:Array[String]){
        val person1 = new Person("Lilei")
        val person2 = new Person("Hanmei")
        person1.info()
        person2.info()
    }
}
```

##### 2.apply方法

- 思考下行代码的执行过程:

  `val myStrArr = Array("BigData", "Hadoop", "Spark")`

- Scala自动调整Array类的伴生对象Array中的一个称为apply的方法，来创建一个Array对象myStrArr
- apply方法调用约定：用括号传递给类实例或单例对象名一个或多个参数时，Scala会在相应的类或对象中查找方法名为apply且参数列表与传入的参数一致的方法，并用传入的参数来调用该apply方法

伴生对象中的apply方法：将所有类的构造方法以apply方法的形式定义在伴生对象中，这样伴生对象就像生成类实例的工厂，而这些apply方法也被称为工厂方法

```scala
class Car(name:String){
    def info(){
        println("Car name is "+ name)
    }
}
object Car{
    def apply(name:String) = new Car(name)
}
object MyTestApply{
    def main(args:Array[String]){
        val mycar = Car("BMW")
        mycar.info()
    }
}
```

- 保持对象和函数之间使用的一致性
- 面向对象："对象.方法" VS 数学："函数（参数）"
- Scala中一切都是对象，包括函数也是对象。Scala中的函数既保留括号调用样式，也可以使用点号调用形式，其对应的方法名即为apply

```scala
scala> def add=(x:Int,y:Int)=>x+y
add:(Int, Int) => Int
scala> add(4, 5)
res: Int = 9
scala> add.apply(4, 5)
res: Int = 9
```

##### 3.update

与apply方法类似的update方法也遵循相应的调用约定：当对带有括号并包括一到若干参数的对象进行赋值时，编译器将调用对象的update方法，并将括号里的参数和等号右边的值一起作为update方法的输入参数来执行调用

```scala
scala> val myStrArr = new Array[String](3)
//声明一个长度为3的字符串数组，每个数组元素初始化为null
scala> myStrArr(0) = "BigData"
//实际上，调用了伴生类Array中的update方法，执行myStrArr.update(0,"BigData")
scala> myStrArr(1) = "Hadoop"
scala> myStrArr(2) = "Spark"
```

- unapply方法用于对对象进行解构操作，与apply方法的反向操作，apply方法接受构造参数变成对象，而unapply方法接受一个对象，从中提取值

```scala
class Car(val brand:String, val price:Int){
    def info(){
        println("Car brand is "+ brand + " and price is "+ price)
    }
}
object Car{
    def apply(brand:String, price:Int) = {
        println("Debug:calling apply...")
        new Car(brand, price)
    }
    def unapply(c:Car):Option[(String, Int)] = {
        println("Debug:calling unapply...")
        Some((c.brand, c.price))
    }
}
object TestUnapply{
    def main (args:Array[String]){
        var Car(carbrand, carprice) = Car("BMW", 9000000)
        println("brand:"+ carbrand + " and carprice: "+ carprice)
    }
}
```

#### 继承

##### 1.抽象类

如果一个类包含没有实现的成员，则必须使用abstract关键词进行修饰，定义为抽象类

```scala
abstract class Car(val name:String){
    val carBrand:String
    def info()
    def greeting(){
        println("Welcome to my car!")
    }
}
```

##### 2.扩展类

Scala只支持单一继承，而不支持多重继承。在类定义中使用extends关键字表示继承关系。定义子类时，需要注意：

- 重载父类的抽象成员（包括字段和方法）时，override关键字是可选的；而重载父类的非抽象成员时，override关键字是必选的
- 只能重载val类型的字段，而不能重载var类型的字段。因为var类型本身就是可变的，所以，可以直接修改它的值，无需重载

```scala
abstract class Car{
    val carBrand: String
    def info()
    def greeting(){println("Welcome to my car!")}
}

class BMWCar extends Car{
    override val carBrand = "BMW"
    def info(){print("This is a %s car. It is expensive.\n", carBrand)}
    override def greeting(){println("Welcome to my BMW Car!")}
}

class BYDCar extends Car{
    override val carBrand = "BYD"
    def info() {println("This is a %s Car. It is cheap.\n", carBrand)}
    override def greeting() {println("Welcome to my BYD car!")}
}

object MyCar{
    def main(args:Array[String]){
        val myCar1 = new BMWCar()
        val myCar2 = new BYDCar()
        myCar1.greeting()
        myCar1.info()
        myCar2.greeting()
        myCar2.info()
    }
}
```

<img src="/Users/zhouyuchen/Documents/截屏2021-12-23 下午8.54.07.png" alt="截屏2021-12-23 下午8.54.07" style="zoom:40%;" />

- Null是所有引用类型的子类，其唯一的实例为null，表示一个“空”对象，可以赋值给任何引用类型的变量，但不能赋值给值类型的变量
- Nothing是所有其他类型的子类，包括Null。Nothing没有实例，主要用于异常处理的返回类型

##### 4.Option类

- Scala提供null是为了实现在JVM与其他Java库的兼容性，但是，除非明确需要与Java库进行交互，否则，Scala建议尽量避免使用这种可能带来的bug的null，而改用Option类
- Option是一个抽象类，有一个具体的子类Some和一个对象None，其中，前者表示有值的情形，后者表示没有值
- 当方法不确定是否有对象返回时，可以让返回值类型为Option[T],其中，T为类型参数。对于这类方法，如果确实有T类型的对象需要返回，会将该对象包装成一个Some对象并返回；如果没有值需要返回，将返回None

```scala
scala> case class Book(val name:String, val price:Double)
defined class Book
scala> val books = Map("Hadoop" -> Book("Hadoop", 35.5),
      | "spark" -> Book("Spark", 55.5),
      | "hbase" -> Book("Hbase", 26.0))
books:scala.collection.immutable.Map[String, Book] = ...
scala> books.get("hadoop")
res: Option[Book] = Some(Book(Hadoop, 35.5))
scala> books.get("hive")
res: Option[Book] = None
scala> books.get("hadoop").get
res: Book = Book(Hadoop, 35.5)
scala> boos.get("hive").get //None对象的get会抛出异常
java.util.NoSuchElementException:None get
...
scala> books.get("hive").getOrElse(Book("Unknown name", 0))
res: Book = Book(Unknown name, 0.0)
```

#### 特质(trait)

##### 1.特质概述

- Java中提供了接口，允许一个类实现任意数量的接口
- Scala中没有接口的概念，而是提供了“特质(trait)”,它不仅实现了接口的功能，还具备了很多其他的特性
- Scala的特质是代码重用的基本单元，可以同时拥有抽象方法和具体方法
- Scala中，一个类只能继承自一个超类，却可以实现多个特质，从而重用特质中的方法和字段，实现了多重继承

##### 2.特质的定义

使用关键字trait定义特质

```scala
trait Flyable {
    var maxFilyHeight:Int //抽象字段
    def fly() //抽象方法
    def breathe(){
        println("I can't breathe")
    }
}
```

- 特质既可以包含抽象成员，也可以包含非抽象成员。包含抽象成员时，不需要abstract关键字
- 特质可以使用extends继承其他的特质

##### 3.把特质混入类中

可以使用extends或with关键字把特质混入类中  

如果特质中包含抽象成员，则该类必须为这些抽象成员提供具体实现，除非该类被定义为抽象类

```scala
class Bird(flyHeight:Int) extends Flyable{
    var maxFlyHeight:Int = flyHeight
    def fly(){
        printf("I can fly can at the height of %d.", maxFlyHeight)
    }//重载特质的抽象方法
}
```

把上面定义的特质Flyable和类Bird封装到一个代码文件Bird.scala

```scala
trait Flyable{
    var maxFlyHeight:Int
    def fly()
    def breathe(){
        println("I can breathe")
    }
}
class Bird(FlyHeight:Int) extends Flyable{
    var maxFlyHeight:Int = flyHeight
    def fly(){
        print("I can fly at the height of %d", maxFlyHeight)
    }
}
```

如果要混入多个特质，可以连续使用多个with

```scala
trait Flyable{
    var maxFlyHeight:Int
    def fly()
    def breathe(){
        println("I can breathe")
    }
}
trait HasLegs{
    val legs:Int
    def move(){println("I can walk with %d legs", legs)}
}
class Animal(val category:String){
    def info(){println("This is a "+ category)}
}
class Bird(flyHeight:Int) extends Animal("Bird") with Flyable with HasLegs{
    var maxFlyHeight:Int = flyHeight
    val legs = 2
    def fly(){
        println("I can fly at the height of %d", maxFlyHeight)
    }
}
```

#### 模式匹配

##### 1.match语句

最常见的模式匹配是match语句，match语句用于在当需要从多个分支中进行选择的场景

```scala
import scala.io.StdIn._
println("Please input the score:")
val grade = readChar()
grade match{
    case 'A' => println("85-100")
    case 'B' => println("70-84")
    case 'C' => println("60-69")
    case 'D' => println("<60")
    case_ => println("error input")
}
```

- 通配符_相当于Java中的default分支
- match结构中不需要break语句来跳出判断，Scala从前往后匹配到一个分支后，会自动跳出判断
- case后面的表达式可以是任何类型的常量，而不是要求是整数类型

##### 2.case类

- case类是一种特殊的类，它们经过优化以被用于模式匹配
- 当定义一个类时，如果在class关键字前加上case关键字，则该类称为case类
- Scala为case类自动重载了许多实用的方法，包括toString、equals和hashcode方法
- Scala为每一个case类自动生成一个伴生对象，其包括模版代码
  - 1个apply方法，因此，实例化case类的时候无需使用new关键字
  - 1个unapply方法，该方法包含一个类型为伴生类的参数，返回的结果是Option类型，对应的类型参数是N元组，N是伴生类中主构造器参数的个数。Unapply方法用于对对象进行解构操作，在case类模式匹配中，该方法被自动调用，并将待匹配的对象作为参数传递给他

例如，假设有如下定义的一个case类:

`case class Car(brand:String, price:Int)`

则编译器自动生成的伴生对象是

```scala
object Car{
    def apply(brand:String, price:Int) = new Car(brand, price)
    def unapply(c:Car):Option[(String,Int)] = Some((c.brand, c.price))
}
```

```scala
case class Car(brand:String, price:Int)
val myBYDCar = Car("BYD", 89000)
val myBMWCar = Car("BMW", 1200000)
val myBenzCar = Car("Benz", 1500000)
for (car <- List(myBYDCar, myBMWCar, myBenzCar)){
    car match {
        case Car("BYD", 89000) => println("Hello BYD")
        case Car("BWM", 1200000) => println("Hello BMW")
        case Car(brand, price) => println("Brand: "+ brand +", Price: " + price + ", do you want it?")        
    }
}
```

#### 包

##### 1.包的定义

- 为了解决程序命名冲突问题，Scala也和Java一样采用包(package)来层次化、模块化地组织程序

  `package autodepartment`

  `class MyClass`

- 为了在任意位置访问MyClass类，需要使用autodepartment.MyClass

- 通过在关键字package后面加大括号，可以将程序的不同部分放在不同的包里。

```scala
package xmu{
    package autodepartment{
        class ControlCourse{
            ....
        }
    }
    package csdepartment{
        class OSCourse{
            val cc = new autodepartment.ControlCourse
        }
    }
}
```

##### 2.引用包成员

- 可以用import子句来引用包成员，这样可以简化包成员的访问方式

  ```scala
  import xmu.autoddepartment.ControlCourse
  class MyClass{
      var myos = new ControlCourse
  }
  ```

- 使用通配符下划线(_)引入类或对象的所有成员

  ```scala
  import scala.io.StdIn._
  var i = readInt()
  var f = readFloat()
  var str = readLine()
  ```

- Scala隐式地添加了一些引用到每个程序前面，相当于每个Scala程序都隐式地以如下代码开始

  ```scala
  import java.lang._
  import scala._
  import Predef._
  ```

### 函数式编程基础

#### 函数定义与使用

定义函数最通用的方法是作为某个类或者对象的成员，这种函数被称为方法，其定义的基本语法为

**def 方法名(参数列表)：结果类型 = {方法体}**

字面量包括整数字面量、浮点数字面量、布尔型字面量、字符字面量、字符串字面量、符号字面量、函数字面量和元组字面量

```scala
val i = 123
val i = 3.14
val i = true
val i = 'A'
val i = "Hello"
```

`def counter(value: Int):Int = {value += 1}`

`(Int) => Int`

#### 高阶函数

高阶函数：当一个函数包含其他函数作为其参数或者返回结果为一个函数时，该函数被称为高阶函数

```scala
//例如：假设需要分别计算从一个整数到另一个整数的“连加和”、“平方和”以及“2的幂次和”
//方案1:不采用高阶函数
def powerOfTwo(x: Int):Int = {if(x==0) 1 else 2 * powerOfTwo(x-1)}
def sumInts(a:Int, b:Int):Int = {
    if (a>b) 0 else a + sumInts(a+1, b)
}
def sumSquares(a:Int, b:Int):Int = {
    if(a>b) 0 else a*a + sumSquares(a+1, b)
}
def sumPowersOfTwo(a:Int, b:Int):Int = {
    if(a>b) 0 else powerOfTwo(a) + sumPowersOfTwo(a+1, b)
}

//方案2:采用高阶函数
def sum(f:Int => Int, a:Int, b:Int): Int = {
    if(a>b) 0 else f(a) + sum(f, a+1, b)
}

scala> sum(x=>x,1,5)
res: Int = 15
scala> sum(x=>x*x,1,5)
res: Int = 55
scala> sum(powerOfTwo,1,5)
res: Int =62
```

#### 针对容器的操作

##### 1.遍历操作

- Scala容器的标准遍历方法foreach

  `def foreach[U](f:Elem => U):Unit`

  ```scala
  scala> val list = List(1, 2, 3)
  list:List[Int] = List(1, 2, 3)
  scala> val f=(i:Int) => println(i)
  f:Int => Unit = <function1>
  scala> list.foreach(f)
  1
  2
  3
  ```

  ```scala
  scala> val university = Map("XMU" -> "Xiamen University", "THU" -> "Tsinghua University", "ZJU" -> "Zhejiang University")
  university: scala.collection.mutable.Map[String, String] = ...
  scala> university foreach{kv => println(kv._1+":"+kv._2)}
  XMU:Xiamen University
  THU:Tsinghua University
  ZJU:Zhejiang University
  ```

  `university foreach{x=>x match {case (k,v) => println(k+":"+v)}}`

  `university foreach{case (k,v) => println(k+":"+v)}`

##### 2.映射操作

- 映射是指通过对容器中的元素进行某些运算来生成一个新的容器。两个典型的映射操作是map方法和flatMap方法
- map方法（一对一映射）：将某个函数应用到集合中每个元素，映射得到一个新的元素，map方法会返回一个与原容器类型大小都相同的心容器，只不过元素的类型可能不同

```scala
scala> val books = List("Hadoop", "Hive", "HDFS")
books: List[String] = List(Hadoop, Hive, HDFS)
scala> books.map(s => s.toUpperCase)
res: List[String] = List(HADOOP, HIVE, HDFS)
scala> books.map(s => s.length)
res: List[Int] = List(6, 4, 4)
```

- flatMap方法（一对多映射）：将某个函数应用到容器中的元素时，对每个元素都会返回一个容器（而不是一个元素），然后，flatMap把生成的多个容器“拍扁”成为一个容器并返回。返回的容器与原容器类型相同，但大小可能不同，其中元素的类型也可能不同

```scala
scala> books flatMap(s => s.toList)
res: List[Char] = List(H, a, d, o, o, p, H, i, v, e, H, D, F, S)
```

##### 3.过滤操作

- 过滤：遍历一个容器，从中获取满足指定条件的元素，返回一个新的容器
- filter方法：接受一个返回布尔值的函数f作为参数，并将f作用到每个元素上，将f返回真值的元素组成一个新容器返回

##### 4.规约操作

- 规约操作是对容器元素进行两两运算，将其“规约”为一个值
- reduce方法：接受一个二元函数f作为参数，首先将f作用在某两个元素上并返回一个值，然后再将f作用在上一个返回值和容器的下一个元素上，再返回一个值，以此类推，最后容器中的所有值会被规约为一个
- fold方法：一个双参数列表的函数，从提供的初始值开始规约。第一个参数列表接受一个规约的初始值，第二个参数列表接受与reduce中一样的二元函数参数
- foldLeft和foldRight：前者从左到右进行遍历，后者从右到左进行遍历





### Spark RDD 

##### 1.RDD创建	

Spark采用textFile()方法来从文件系统中加载数据创建RDD，该方法把文件的URI作为参数，这个URI可以是本地文件系统的地址、分布式文件系统HDFS的地址。

```scala
#1.从文件系统中加载数据创建RDD
scala> val lines = sc.textFile("file:///opt/software/spark/README.md")
scala> val lines = sc.textFile("hdfs:///user/spark/stocks/part-m-00000")

#2.通过并行集合(数组)创建RDD
scala> val array = Array(1, 2, 3, 4, 5)
scala> val rdd = sc.parallelize(array)
```

##### 2.RDD操作

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



##### 3.惰性机制

惰性机制是指，整个转换过程只是记录了转换的轨迹，并不会发生真正的计算，只有遇到行动操作时，才会触发真正的计算

##### 4.持久化

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



##### 5.分区

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

##### 6.键值对RDD

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

    

##### 7.常用的键值对操作

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




### Spark SQL

##### 1.DataFrame的创建

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

##### 2.DataFrame的保存

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

##### 3.DataFrame的常用操作

- `printSchema()`

  可以使用`df.printSchema()`操作，打印出DataFrame的模式信息

- `select()`

  `df.select()`的功能，是从DataFrame中选取部分列的数据

- `filter()`

  `df.filter()`可以实现条件查询，找到满足条件要求的记录

- `groupBy()`

  `df.groupBy()`用于对记录进行分组

- `sort()`

  `df.sort()`用于对记录进行排序，`df.sort(df("age").desc, df("name").asc)`，desc为降序，asc为升序

- 去除空值、Nan

  `df.na.drop()`

- 空值填补

  `df.na.fill()`

##### 4.从RDD转换得到DataFrame

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

##### 5.使用SparkSQL读写MySQL数据库

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



##### 6.连接Hive读写数据

Hive是构建在Hadoop之上的数据仓库工具。可以支持大规模数据存储、分析，具有良好的可扩展性。在某种程度上，Hive可以看作是用户编程接口，因为它本身不会存储和处理数据，而是依赖分布式文件系统HDFS来实现数据的存储，依赖分布式并行计算模型MapReduce来实现数据的处理

`(1)准备工作`

为了让Spark能够访问Hive，必须为Spark添加Hive支持。

在spark-shell中执行`import org.apache.spark.sql.hive.HiveContext 

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



