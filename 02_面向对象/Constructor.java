/**
 * Constructor.java - 构造器
 * <p>
 * 学习要点：
 * 1. 构造器的定义和特点
 * 2. 默认构造器
 * 3. 有参构造器
 * 4. 构造器重载
 * 5. this() 调用其他构造器
 * 6. 灵活构造器体（JDK 25 JEP 513）
 * 7. 代码块（静态代码块、实例代码块）
 */
public class Constructor {

    public static void main(String[] args) {

        // ============ 1. 使用不同的构造器创建对象 ============
        System.out.println("========== 使用构造器 ==========");
        Person p1 = new Person();                        // 无参
        Person p2 = new Person("张三");                   // 只有名字
        Person p3 = new Person("李四", 25);               // 名字+年龄
        Person p4 = new Person("王五", 30, "北京");        // 全参

        p1.showInfo();
        p2.showInfo();
        p3.showInfo();
        p4.showInfo();

        // ============ 2. 静态代码块和实例代码块 ============
        System.out.println("\n========== 代码块执行顺序 ==========");
        System.out.println("--- 创建第一个 Order ---");
        Order o1 = new Order("A001");
        System.out.println("--- 创建第二个 Order ---");
        Order o2 = new Order("A002");
        // 静态代码块只执行一次，实例代码块每次创建对象都执行
    }
}

// ============ 演示各种构造器 ============
class Person {
    private String name;
    private int age;
    private String address;

    // 1. 无参构造器
    public Person() {
        System.out.println("[无参构造]");
        // 使用 this() 调用其他构造器
        // 注意：JDK 25 前 this() 必须是第一条语句；JDK 25 起 JEP 513 放宽限制，
        //       允许在 this()/super() 之前放置校验或预处理代码（详见 07_JDK25新特性/FlexibleConstructor.java）
        // this("匿名", 0, "未知");
    }

    // 2. 单参构造器
    public Person(String name) {
        this();                                          // 调用无参构造
        this.name = name;
        System.out.println("[单参构造] name=" + name);
    }

    // 3. 双参构造器
    public Person(String name, int age) {
        this(name);                                      // 调用单参构造
        this.age = age;
        System.out.println("[双参构造] age=" + age);
    }

    // 4. 全参构造器
    public Person(String name, int age, String address) {
        this(name, age);                                 // 调用双参构造
        this.address = address;
        System.out.println("[全参构造] address=" + address);
    }

    public void showInfo() {
        System.out.println("name=" + name + ", age=" + age + ", address=" + address);
    }
}

// ============ 演示代码块 ============
class Order {

    private static int totalOrders;             // 静态变量
    private String orderId;
    private String createdAt;

    // 静态代码块：类加载时执行一次
    // 用途：初始化静态变量、加载配置
    static {
        System.out.println("《静态代码块》类加载时执行");
        totalOrders = 0;
    }

    // 实例代码块：每次创建对象时执行（在构造器之前）
    // 用途：所有构造器共享的初始化逻辑
    {
        System.out.println("《实例代码块》构造前执行");
        this.createdAt = "2026-01-01";
        totalOrders++;
    }

    public Order(String orderId) {
        System.out.println("《构造器》执行");
        this.orderId = orderId;
    }
}

/*
 * =============== 构造器特点 ===============
 *
 * 1. 名字必须与类名完全一致
 * 2. 没有返回值类型（连 void 都不写）
 * 3. 用于对象初始化
 * 4. 编译器会自动生成无参构造器（如果没写任何构造器）
 * 5. 一旦写了任何构造器，编译器就不再生成默认无参
 * 6. 可以重载（参数列表不同）
 *
 * =============== 代码执行顺序 ===============
 *
 * 类加载时：
 *   静态变量 → 静态代码块 (只执行一次)
 *
 * 创建对象时：
 *   实例变量 → 实例代码块 → 构造器
 *
 * 有父类时：
 *   父类静态 → 子类静态 → 父类实例 → 父类构造 → 子类实例 → 子类构造
 */

// ============ JDK 25 新特性：灵活构造器体（JEP 513） ============
// 详见 07_JDK25新特性/FlexibleConstructor.java
//
// 在 JDK 25 之前：super() 或 this() 必须是构造器的第一条语句
// JDK 25 允许：super() 或 this() 之前可以有语句（用于参数校验、准备工作）
