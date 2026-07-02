/**
 * CompactSourceFile.java - 紧凑源文件和实例主方法（JDK 25 JEP 512 正式特性）
 * <p>
 * 学习要点：
 * 1. 紧凑源文件的概念
 * 2. 实例 main 方法
 * 3. 无 static、无参数、无类名的最简写法
 * 4. 与传统 main 方法的对比
 * 5. 适用场景（学习、脚本、原型）
 * <p>
 * 本文件展示所有形式（本类只有 static main 是完整对照）。
 * 真正的紧凑源文件示例见文件末尾的说明和 example_compact.java 说明。
 */
public class CompactSourceFile {

    // 完整传统写法
    public static void main(String[] args) {
        System.out.println("========== 传统 main 方法 ==========");
        System.out.println("必须：public class + public static void main(String[] args)");

        // ============ 演变 ============
        System.out.println("\n========== JDK 25 主方法演变 ==========");
        System.out.println("""

                传统写法（JDK 24 之前必须这样）：

                    public class Hello {
                        public static void main(String[] args) {
                            System.out.println("Hello");
                        }
                    }

                JDK 25 支持的简化：

                    // 1. 省略 String[] 参数
                    public class Hello {
                        public static void main() {
                            System.out.println("Hello");
                        }
                    }

                    // 2. 省略 static，改为"实例 main 方法"
                    public class Hello {
                        void main() {
                            System.out.println("Hello");
                        }
                    }

                    // 3. 紧凑源文件：省略类声明
                    void main() {
                        System.out.println("Hello");
                    }

                最简形式（Hello.java 整个文件）：

                    void main() {
                        System.out.println("Hello, World!");
                    }

                运行方式：
                    java Hello.java
                """);

        // ============ 运行方式 ============
        System.out.println("\n========== 运行方式 ==========");
        System.out.println("紧凑源文件用途：学习、脚本、快速原型");
        System.out.println("可以像脚本一样直接运行：java 文件名.java");
    }
}

/*
 * =============== JEP 512：紧凑源文件和实例主方法 ===============
 *
 * JDK 25 正式特性。
 *
 * 目的：降低 Java 学习门槛，让入门者可以从最简单的形式开始，
 *      逐步引入类、public、static 等概念。
 *
 * =============== 主方法的启动查找顺序 ===============
 *
 * 当运行 java Xxx（或 java Xxx.java）时，JVM 查找 main 方法的顺序：
 *
 * 1. static void main(String[] args)       ← 传统写法（最高优先级）
 * 2. static void main()                     ← 无参数静态
 * 3. void main(String[] args)               ← 有参数实例
 * 4. void main()                            ← 最简形式
 *
 * 查找到即用；找不到会报错。
 *
 * =============== 紧凑源文件（Compact Source File） ===============
 *
 * 一个 .java 文件如果只包含"游离的"方法（没有显式类声明），
 * 编译器会自动创建一个隐式的类包装它们。
 *
 * 完整示例（compact_hello.java）：
 *
 *   void main() {
 *       System.out.println("Hello, World!");
 *       greet("Java");
 *   }
 *
 *   void greet(String name) {
 *       System.out.println("Hello, " + name);
 *   }
 *
 * 运行：java compact_hello.java
 *
 * =============== 特点 ===============
 *
 * 1. 顶层可以直接写方法（无需显式类）
 * 2. main 方法可以不是 static
 * 3. main 方法可以没有 String[] 参数
 * 4. 依然可以定义字段、其他方法、内部类
 * 5. 隐式类是 final 的
 * 6. 隐式类的成员默认包私有
 *
 * =============== 演进 ===============
 *
 * JDK 21  JEP 445 首次预览：无名类和实例 main 方法
 * JDK 22  JEP 463 第二次预览：更名为"隐式类"
 * JDK 23  JEP 477 第三次预览
 * JDK 24  JEP 495 第四次预览：紧凑源文件
 * JDK 25  JEP 512 正式：紧凑源文件和实例主方法
 *
 * =============== 使用建议 ===============
 *
 * ✅ 适合场景：
 * - 教学、学习入门
 * - 单文件小工具、脚本
 * - 快速原型验证
 * - JShell 之外的即时试验
 *
 * ❌ 不适合：
 * - 生产项目（应该用完整的类结构和模块化）
 * - 需要暴露 API 的库
 * - 大型系统
 *
 * =============== 与传统写法的兼容 ===============
 *
 * 传统的 public class + static main 依然完全支持，
 * 紧凑源文件只是提供了更简单的入门形式，
 * 学习者可以逐步升级到完整的 OOP 写法。
 *
 * =============== JEP 512 附带能力 ===============
 *
 * 隐式导入常用包：java.base 的 java.lang 已默认，
 * 紧凑源文件还会默认导入 java.io、java.util 等常用类，
 * 让 println、List.of 等无需 import 即用。
 *
 * 详见 ModuleImport.java（JEP 511）。
 */
