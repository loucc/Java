// compact_hello.java - JDK 25 紧凑源文件示例
// 运行：java compact_hello.java
//
// 注意：文件名要与内部 main 所在的隐式类无关，取任何 xxx.java 都可以
// 只是启动时用 java xxx.java

void main() {
    System.out.println("Hello from Compact Source File!");
    System.out.println("这个文件没有 public class 声明！");

    greet("Java 25");
    greet("学习者");

    // 紧凑源文件（JEP 512）自动导入 java.base 模块，List/Map 等可直接用，无需 import
    var list = List.of(1, 2, 3, 4, 5);
    int sum = 0;
    for (int i : list) sum += i;
    System.out.println("Sum: " + sum);
}

void greet(String name) {
    System.out.println("Hello, " + name);
}

// 可以定义"字段"（其实是隐式类的字段）
final int VERSION = 25;
