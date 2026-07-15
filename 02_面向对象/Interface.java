/**
 * Interface.java - 接口
 * <p>
 * 学习要点：
 * 1. 接口的定义和实现（interface / implements）
 * 2. 接口的成员（常量、抽象方法）
 * 3. default 默认方法（JDK 8+）
 * 4. static 静态方法（JDK 8+）
 * 5. private 私有方法（JDK 9+）
 * 6. 多实现和接口继承
 * 7. 函数式接口
 * 8. 空接口（标记接口）
 */
public class Interface {

    public static void main(String[] args) {

        // ============ 1. 使用接口 ============
        System.out.println("========== 使用接口 ==========");
        Drawable circle = new MyCircle(5);
        circle.draw();
        System.out.println("面积: " + circle.getArea());
        circle.print();                         // default 方法
        circle.printWithHelper();               // default 方法调用 private 方法

        // 接口静态方法
        System.out.println("Drawable 描述: " + Drawable.description());

        // ============ 2. 多接口实现 ============
        System.out.println("\n========== 多接口实现 ==========");
        Duck duck = new Duck();
        duck.swim();
        duck.fly();
        duck.eat();

        // ============ 3. 多态：接口作为类型 ============
        System.out.println("\n========== 接口多态 ==========");
        Swimmable[] swimmers = { new Duck(), new Fish() };
        for (Swimmable s : swimmers) {
            s.swim();
        }

        // ============ 4. 函数式接口 + Lambda ============
        System.out.println("\n========== 函数式接口 ==========");
        // 传统实现（匿名内部类）
        Calculator add = new Calculator() {
            @Override
            public int calc(int a, int b) {
                return a + b;
            }
        };
        System.out.println("加法: " + add.calc(3, 5));

        // Lambda 表达式（详见 Lambda.java）
        Calculator subtract = (a, b) -> a - b;
        Calculator multiply = (a, b) -> a * b;
        System.out.println("减法: " + subtract.calc(10, 3));
        System.out.println("乘法: " + multiply.calc(4, 6));
    }
}

// ============ 1. 基本接口 ============
interface Drawable {

    // 接口中的字段隐含 public static final
    String TYPE = "shape";                      // 相当于 public static final String TYPE = "shape";

    // 抽象方法：隐含 public abstract
    void draw();
    double getArea();

    // JDK 8+ default 默认方法：有默认实现，实现类可以选择重写
    default void print() {
        System.out.println("图形类型: " + TYPE);
        System.out.println("面积: " + getArea());
    }

    // JDK 8+ static 静态方法：只能通过接口名调用
    static String description() {
        return "Drawable 接口：可绘制的图形";
    }

    // JDK 9+ private 私有方法：仅供接口内其他方法使用
    private void helper() {
        System.out.println("[内部辅助方法]");
    }

    default void printWithHelper() {
        helper();
        print();
    }
}

// ============ 实现接口 ============
class MyCircle implements Drawable {
    private double radius;

    public MyCircle(double radius) {
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("画圆，半径 " + radius);
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    // print() 使用接口默认实现
}

// ============ 2. 多接口实现 ============
interface Swimmable {
    void swim();

    default void introduce() {
        System.out.println("我会游泳");
    }
}

interface Flyable {
    void fly();

    default void introduce() {
        System.out.println("我会飞");
    }
}

interface Eatable {
    void eat();
}

// 同时实现多个接口，用逗号分隔
class Duck implements Swimmable, Flyable, Eatable {
    @Override
    public void swim() {
        System.out.println("鸭子在水里游");
    }

    @Override
    public void fly() {
        System.out.println("鸭子在天上飞");
    }

    @Override
    public void eat() {
        System.out.println("鸭子在吃鱼");
    }

    // 多个接口有同名的默认方法：必须重写！
    @Override
    public void introduce() {
        System.out.println("我是鸭子，我会游泳也会飞");
        Swimmable.super.introduce();            // 指定调用 Swimmable 的默认实现
    }
}

class Fish implements Swimmable {
    @Override
    public void swim() {
        System.out.println("鱼在水里游");
    }
}

// ============ 3. 函数式接口 ============
// @FunctionalInterface 注解要求接口有且仅有一个抽象方法
@FunctionalInterface
interface Calculator {
    int calc(int a, int b);
    // 但可以有默认方法和静态方法

    default Calculator andThen(Calculator next) {
        return (a, b) -> next.calc(calc(a, b), 0);
    }

    static Calculator identity() {
        return (a, b) -> a;
    }
}

// ============ 4. 接口继承（可以多继承） ============
interface Animal2 {
    void breathe();
}

interface Pet {
    void beNamed();
}

// 一个接口可以继承多个接口
interface DomesticAnimal extends Animal2, Pet {
    void live();
}

class MyDog implements DomesticAnimal {
    @Override
    public void breathe() { System.out.println("呼吸"); }

    @Override
    public void beNamed() { System.out.println("被起名"); }

    @Override
    public void live() { System.out.println("居住"); }
}

// ============ 5. 标记接口（无任何方法） ============
// 例如 Serializable、Cloneable，只是标记类"具有某种能力"
interface Auditable {
    // 无任何方法
}

class AuditableService implements Auditable {
    // 只是打个标记
}

/*
 * =============== 接口的特点 ===============
 *
 * 1. 用 interface 关键字定义
 * 2. 所有方法默认 public
 * 3. 字段默认 public static final
 * 4. 不能实例化
 * 5. 支持多继承（接口继承接口）和多实现（类实现多接口）
 * 6. JDK 8+ 支持 default 和 static 方法
 * 7. JDK 9+ 支持 private 方法
 *
 * =============== 接口 vs 抽象类 ===============
 *
 *                      接口                    抽象类
 * 定义                 interface               abstract class
 * 关系                 implements（多）        extends（单）
 * 字段                 只能 public static final 可以有各种字段
 * 构造器               不能有                  可以有
 * 方法                 abstract/default/static/private   任意
 * 描述                 能力/契约               is-a 关系
 *
 * =============== 何时选择 ===============
 *
 * 选接口：
 * - 定义能力（Comparable、Runnable、Serializable）
 * - 需要多继承
 * - 完全无实现的规范
 * - 函数式编程
 *
 * 选抽象类：
 * - 需要字段/构造器
 * - 有大量共享的实现代码
 * - 明显的 is-a 关系
 * - 模板方法模式
 *
 * =============== 常用内置接口 ===============
 *
 * Comparable<T>       : 定义自然顺序
 * Comparator<T>       : 定义外部比较器
 * Iterable<T>         : 可以用 for-each
 * Runnable            : 无参无返回值的任务
 * Callable<V>         : 有返回值的任务
 * Cloneable           : 可克隆（标记接口）
 * Serializable        : 可序列化（标记接口）
 * AutoCloseable       : 可自动关闭（try-with-resources）
 */
