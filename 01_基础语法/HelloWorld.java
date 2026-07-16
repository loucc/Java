/**
 * 01_HelloWorld.java - Java 第一个程序
 * <p>
 * 学习要点：
 * 1. Java 程序的基本结构
 * 2. main 方法
 * 3. System.out.println 输出
 * 4. JDK 25 新特性：紧凑源文件和实例 main 方法（JEP 512）
 * 5. 编译和运行 Java 程序
 * <p>
 * Java 程序的执行流程：
 * .java 源文件 → javac 编译 → .class 字节码文件 → JVM 执行
 */
public class HelloWorld {

    // main 方法是 Java 程序的入口
    // public: 公开的访问权限，JVM 可以访问
    // static: 静态方法，不需要创建对象即可调用
    // void: 无返回值
    // String[] args: 命令行参数，是字符串数组
    public static void main(String[] args) {

        // System.out.println() 打印内容并换行
        System.out.println("Hello, World!");

        // System.out.print() 打印内容不换行
        System.out.print("你好, ");
        System.out.println("Java 25");

        // System.out.printf() 格式化输出，类似 C 语言
        System.out.printf("Java 版本: %d, 我今年 %d 岁%n", 25, 20);

        // 输出命令行参数
        System.out.println("命令行参数数量: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数 " + i + ": " + args[i]);
        }

        // 紧凑源文件适合小程序、脚本和教学；具名类仍适合需要清晰类型边界的应用。
        // 详见 07_JDK25新特性/CompactSourceFile.java
    }
}

/*
 * =============== 编译和运行 ===============
 *
 * 命令行编译：javac HelloWorld.java
 * 命令行运行：java HelloWorld
 *
 * JDK 11+ 可以直接运行源码文件：
 *   java HelloWorld.java
 *
 * 带参数运行：
 *   java HelloWorld 参数1 参数2
 *
 * =============== 注意事项 ===============
 *
 * 1. 文件名必须与 public 类的名字完全一致（区分大小写）
 * 2. 每条语句以分号 ; 结尾
 * 3. 代码块使用大括号 { } 包裹
 * 4. Java 是严格区分大小写的
 * 5. 注释：
 *    - // 单行注释
 *    - / * ... * / 多行注释
 *    - / ** ... * / 文档注释（可通过 javadoc 生成 HTML 文档）
 */
