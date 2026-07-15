# JDK 25 学习指南

> 本目录是基于 JDK 25 (LTS版本，2025年9月发布) 的完整学习资料，涵盖从基础到最新特性的全部内容。

## 环境准备

```bash
# 检查Java版本，应为25及以上
java --version

# 编译单个文件
javac 文件名.java

# 运行程序
java 类名

# 直接运行源码（JDK 11+，无需先编译）
java 文件名.java

# 启用预览特性（JDK 25 部分新特性需要）
java --enable-preview --source 25 文件名.java
```

## 完整学习顺序

### 第一阶段：基础语法 (01_基础语法)
> 掌握Java最基础的语法结构，为后续学习打好基础。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 01 | `HelloWorld.java` | 第一个Java程序、JDK 25简化main方法 |
| 02 | `Variables.java` | 变量、8种基本数据类型、类型转换、var |
| 03 | `Operators.java` | 算术、比较、逻辑、位运算、三元运算符 |
| 04 | `ControlFlow.java` | if/else、switch语句、switch表达式 |
| 05 | `Loops.java` | for、while、do-while、增强for、break/continue |
| 06 | `MyArrays.java` | 一维/二维数组、Arrays工具类 |
| 07 | `Strings.java` | String、StringBuilder、StringBuffer、常用API |
| 08 | `TextBlocks.java` | 文本块（JDK 15+） |

### 第二阶段：面向对象 (02_面向对象)
> Java 的核心——面向对象编程。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 09 | `ClassAndObject.java` | 类、对象、字段、方法、this |
| 10 | `Constructor.java` | 构造器、灵活构造器体(JDK 25) |
| 11 | `Encapsulation.java` | 封装、访问修饰符 |
| 12 | `Inheritance.java` | 继承、super、方法重写 |
| 13 | `Polymorphism.java` | 多态、向上/向下转型 |
| 14 | `AbstractClass.java` | 抽象类、抽象方法 |
| 15 | `Interface.java` | 接口、default方法、静态方法、私有方法 |
| 16 | `InnerClass.java` | 成员内部类、静态内部类、匿名内部类 |
| 17 | `MyEnum.java` | 枚举类、枚举方法 |
| 18 | `MyRecord.java` | 记录类(JDK 14+) |
| 19 | `SealedClass.java` | 密封类(JDK 17+) |

### 第三阶段：异常与集合 (03_异常与集合)
> 处理错误和管理数据的核心工具。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 20 | `MyException.java` | try-catch-finally、自定义异常、try-with-resources |
| 21 | `Generics.java` | 泛型类、泛型方法、通配符、上下界 |
| 22 | `MyCollections.java` | List、Set、Map、Queue完整介绍 |
| 23 | `CollectionsUtil.java` | Collections工具类、不可变集合 |

### 第四阶段：函数式编程 (04_函数式编程)
> Java 8+ 引入的函数式编程范式。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 24 | `Lambda.java` | Lambda表达式、方法引用 |
| 25 | `FunctionalInterfaceDemo.java` | 函数式接口、内置函数式接口 |
| 26 | `Streams.java` | Stream API、中间操作、终结操作 |
| 27 | `MyOptional.java` | Optional的正确使用 |

### 第五阶段：并发与多线程 (05_并发与多线程)
> 现代Java并发编程，包括JDK 21+的重大革新。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 28 | `MyThread.java` | Thread、Runnable、线程生命周期 |
| 29 | `MySynchronized.java` | synchronized、volatile、原子类 |
| 30 | `ThreadPool.java` | Executor、线程池、Future |
| 31 | `MyCompletableFuture.java` | 异步编程 |
| 32 | `VirtualThread.java` | 虚拟线程(JDK 21+ 重大特性) |
| 33 | `StructuredConcurrency.java` | 结构化并发(JDK 25) |
| 34 | `ScopedValues.java` | 作用域值(JDK 25) |

### 第六阶段：IO与网络 (06_IO与网络)
> 输入输出、文件操作和网络通信。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 35 | `DateTime.java` | 日期时间API (java.time) |
| 36 | `FileIO.java` | 传统IO、字节流、字符流 |
| 37 | `NIO.java` | NIO.2、Path、Files |
| 38 | `MyHttpClient.java` | 现代HTTP客户端(JDK 11+) |

### 第七阶段：JDK 25 新特性 (07_JDK25新特性)
> 重点掌握JDK 25引入的最新特性。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 39 | `PatternMatching.java` | 模式匹配 for instanceof/switch |
| 40 | `PrimitivePatterns.java` | 原生类型模式匹配(JEP 507预览) |
| 41 | `CompactSourceFile.java` | 紧凑源文件(JEP 512) |
| 42 | `ModuleImport.java` | 模块导入声明(JEP 511) |
| 43 | `FlexibleConstructor.java` | 灵活构造器体(JEP 513) |

### 第八阶段：高级特性 (08_高级特性)
> 反射、注解、模块系统等高级主题。

| 序号 | 文件 | 主要内容 |
|-----|------|---------|
| 44 | `MyAnnotation.java` | 注解、元注解、自定义注解 |
| 45 | `Reflection.java` | 反射机制、Class、Method、Field |
| 46 | `ModuleSystem.java` | 模块系统(JDK 9+) |

## JDK 25 主要新特性速览

JDK 25 是 LTS (长期支持) 版本，2025年9月发布，包含以下重要 JEP：

| JEP | 特性 | 状态 |
|-----|------|-----|
| JEP 505 | 结构化并发 | 第五次预览 |
| JEP 506 | 作用域值 | 正式 |
| JEP 507 | 原生类型模式匹配 | 第三次预览 |
| JEP 508 | Vector API | 第十次孵化 |
| JEP 509 | JFR CPU时间分析 | 实验性 |
| JEP 510 | 密钥派生函数API | 正式 |
| JEP 511 | 模块导入声明 | 正式 |
| JEP 512 | 紧凑源文件和实例main方法 | 正式 |
| JEP 513 | 灵活构造器体 | 正式 |
| JEP 514 | AOT命令行工效 | 正式 |
| JEP 515 | AOT方法分析 | 正式 |
| JEP 519 | 紧凑对象头 | 正式 |

## 学习建议

1. **按阶段学习**：基础阶段建议按顺序，熟悉语法后可按主题选择
2. **先预测再运行**：运行前写下输出、异常或编译结果，再与实际结果比较
3. **主动修改**：每个示例至少改变一个输入、删除一行或制造一个失败场景
4. **解释原因**：不要只记 API；尝试说明编译器、JVM 或类库为什么这样表现
5. **间隔复习**：隔天不看源码重写关键示例，再用原文件核对遗漏
6. **查阅一手资料**：遇到边界问题时核对 Javadoc、JLS 和对应 JEP

建议每章为示例补三个问题：

- 这段代码在编译期和运行期分别发生什么？
- 改变哪个条件会让结果不同或失败？
- 这个 API 的适用边界和常见误用是什么？

可用下面的命令检查所有示例是否仍能在 JDK 25 编译（预览参数对普通源码无害）：

```bash
find . -name '*.java' -print0 | xargs -0 -n1 javac --enable-preview --release 25 -Xlint:all
find . -name '*.class' -delete
```

## 参考资源

- [OpenJDK 25 官方](https://openjdk.org/projects/jdk/25/)
- [JEP 索引](https://openjdk.org/jeps/0)
- [Java API 文档](https://docs.oracle.com/en/java/javase/25/docs/api/)
