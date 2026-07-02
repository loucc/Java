/**
 * InnerClass.java - 内部类
 * <p>
 * 学习要点：
 * 1. 成员内部类（Inner Class）
 * 2. 静态内部类（Static Nested Class）
 * 3. 局部内部类（Local Class）
 * 4. 匿名内部类（Anonymous Class）
 * 5. Lambda 表达式对匿名内部类的简化
 */
public class InnerClass {

    public static void main(String[] args) {

        // ============ 1. 成员内部类 ============
        System.out.println("========== 成员内部类 ==========");
        Outer outer = new Outer();
        // 创建成员内部类对象：需要外部类实例
        Outer.Inner inner = outer.new Inner();
        inner.show();

        // ============ 2. 静态内部类 ============
        System.out.println("\n========== 静态内部类 ==========");
        // 创建静态内部类：不需要外部类实例
        Outer.StaticInner staticInner = new Outer.StaticInner();
        staticInner.show();

        // ============ 3. 局部内部类 ============
        System.out.println("\n========== 局部内部类 ==========");
        outer.testLocalClass();

        // ============ 4. 匿名内部类 ============
        System.out.println("\n========== 匿名内部类 ==========");

        // 匿名内部类：定义类的同时创建对象，只用一次
        Greeting g1 = new Greeting() {
            @Override
            public void say() {
                System.out.println("你好，匿名内部类！");
            }
        };
        g1.say();

        // 匿名内部类实现 Runnable
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类实现 Runnable");
            }
        };
        r.run();

        // 匿名内部类继承抽象类
        AbstractGreeting ag = new AbstractGreeting() {
            @Override
            public void greet() {
                System.out.println("匿名类继承抽象类");
            }
        };
        ag.greet();

        // ============ 5. Lambda 表达式简化匿名内部类（仅函数式接口）============
        System.out.println("\n========== Lambda 简化 ==========");
        Runnable r2 = () -> System.out.println("Lambda 表达式");
        r2.run();

        Greeting g2 = () -> System.out.println("Lambda 实现 Greeting");
        g2.say();

        // ============ 6. 内部类访问外部变量 ============
        System.out.println("\n========== 内部类访问外部 ==========");
        outer.demoAccess();
    }
}

class Outer {
    private String outerField = "外部字段";
    private static String staticField = "静态字段";

    // ============ 成员内部类 ============
    // 每个成员内部类对象都持有一个外部类对象的引用
    class Inner {
        private String innerField = "内部字段";

        public void show() {
            System.out.println("访问外部字段: " + outerField);
            System.out.println("访问外部静态: " + staticField);
            System.out.println("内部字段: " + innerField);

            // 使用 外部类.this 显式访问外部对象
            System.out.println("显式: " + Outer.this.outerField);
        }
    }

    // ============ 静态内部类 ============
    // 不持有外部类对象引用，可以独立使用
    static class StaticInner {
        public void show() {
            // 只能访问外部类的静态成员
            System.out.println("静态内部类访问静态: " + staticField);
            // System.out.println(outerField);  // 编译错误
        }
    }

    // ============ 局部内部类 ============
    public void testLocalClass() {
        int localVar = 10;                      // 局部变量必须是 final 或"事实上 final"

        // 局部内部类：定义在方法中
        class LocalClass {
            public void print() {
                System.out.println("局部变量: " + localVar);
                System.out.println("外部字段: " + outerField);
            }
        }

        LocalClass local = new LocalClass();
        local.print();

        // localVar = 20;                       // 修改会导致上面的类无法编译
    }

    public void demoAccess() {
        Inner in = new Inner();
        in.show();
    }
}

// 独立的接口和抽象类，供匿名内部类使用
interface Greeting {
    void say();
}

abstract class AbstractGreeting {
    public abstract void greet();

    public void hello() {
        System.out.println("hello from abstract");
    }
}

/*
 * =============== 内部类的分类 ===============
 *
 * 1. 成员内部类（非静态嵌套类）
 *    - 持有外部对象的引用
 *    - 通过 外部对象.new 内部类() 创建
 *    - 可以访问外部所有成员（包括 private）
 *
 * 2. 静态内部类（静态嵌套类）
 *    - 不持有外部对象引用
 *    - 通过 外部类.内部类() 创建
 *    - 只能访问外部的静态成员
 *
 * 3. 局部内部类
 *    - 定义在方法或代码块内
 *    - 只能在定义处使用
 *    - 可以访问的局部变量必须是 final 或"事实上 final"
 *
 * 4. 匿名内部类
 *    - 没有类名，定义和使用一次性完成
 *    - 常用于实现接口或继承抽象类的临时对象
 *    - JDK 8+ 常被 Lambda 表达式替代（仅限函数式接口）
 *
 * =============== 内部类的用途 ===============
 *
 * 1. 逻辑上分组：将只在外部类内部使用的辅助类放在内部
 * 2. 提高封装性：内部类可以私有
 * 3. 便于事件处理：GUI 编程中常用
 * 4. 提供多重继承的另一种方式
 *
 * =============== 编译结果 ===============
 *
 * Outer.java 会编译出多个 .class：
 *   Outer.class
 *   Outer$Inner.class            (成员内部类)
 *   Outer$StaticInner.class      (静态内部类)
 *   Outer$1LocalClass.class      (局部内部类)
 *   Outer$1.class                (匿名内部类)
 *
 * =============== 使用建议 ===============
 *
 * 1. 优先使用静态内部类（避免持有外部引用带来的内存泄漏）
 * 2. 一次性使用的接口实现用 Lambda 而非匿名内部类
 * 3. 简单的数据结构用 Record（详见 Record.java）
 * 4. 私有工具类可以作为静态内部类
 */
