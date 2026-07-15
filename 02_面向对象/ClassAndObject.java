/**
 * ClassAndObject.java - 类和对象
 * <p>
 * 学习要点：
 * 1. 类的定义（成员变量、成员方法）
 * 2. 对象的创建（new 关键字）
 * 3. this 关键字
 * 4. 成员变量 vs 局部变量
 * 5. 静态成员 vs 实例成员
 * 6. main 方法所在类
 */
public class ClassAndObject {

    public static void main(String[] args) {

        // ============ 1. 创建对象 ============
        System.out.println("========== 创建对象 ==========");

        // new 类名(参数) 创建对象
        Student s1 = new Student();             // 无参构造
        s1.name = "张三";
        s1.age = 20;
        s1.score = 85.5;
        s1.study();
        s1.showInfo();

        Student s2 = new Student();
        s2.name = "李四";
        s2.age = 22;
        s2.score = 92.0;
        s2.showInfo();

        // ============ 2. 静态成员的访问 ============
        System.out.println("\n========== 静态成员 ==========");
        System.out.println("学生总数: " + Student.count);        // 类名访问
        System.out.println("学校: " + Student.SCHOOL_NAME);

        // 静态方法调用
        Student.showSchool();

        // ============ 3. 引用类型的特性 ============
        System.out.println("\n========== 引用类型 ==========");
        Student s3 = s1;                        // 两个变量指向同一对象
        s3.name = "改名了";                     // 通过 s3 修改
        System.out.println("s1.name = " + s1.name);    // s1 也变了

        Student s4 = null;
        // s4.study();                          // NullPointerException！

        // 比较对象
        Student s5 = new Student();
        s5.name = "张三";
        s5.age = 20;
        System.out.println("\ns1 == s5: " + (s1 == s5));       // false，不同对象
        // equals 默认是 == 比较，除非重写（详见 Inheritance.java）
    }
}

// ============ 学生类定义 ============
class Student {

    // 静态常量
    public static final String SCHOOL_NAME = "清华大学";

    // 静态变量（类变量）——所有对象共享
    public static int count = 0;

    // 实例变量（成员变量）——每个对象各一份
    String name;
    int age;
    double score;

    // 无参构造（未定义时编译器会自动生成）
    public Student() {
        count++;                                // 每创建一个对象计数 +1
        System.out.println("[创建了第 " + count + " 个学生]");
    }

    // 实例方法（成员方法）
    public void study() {
        System.out.println(name + " 正在学习");
    }

    public void showInfo() {
        // this 表示当前对象
        System.out.println("姓名: " + this.name + ", 年龄: " + this.age + ", 成绩: " + this.score);
    }

    // 静态方法——用类名直接调用，不需对象
    public static void showSchool() {
        System.out.println("学校: " + SCHOOL_NAME + ", 学生数: " + count);
        // 注意：静态方法不能访问实例变量和实例方法
        // System.out.println(name);            // 编译错误！
    }

    // 局部变量 vs 成员变量
    public void demo() {
        int localVar = 10;                      // 局部变量
        // localVar 只在此方法内有效
        this.age = 25;                          // 修改成员变量
    }
}

/*
 * =============== 类和对象核心概念 ===============
 *
 * 类（Class）：一种数据结构，是对象的模板
 * 对象（Object）：类的实例，是内存中真实存在的实体
 *
 * =============== 类的组成 ===============
 *
 * 1. 成员变量（字段）：描述对象的属性/状态
 * 2. 成员方法：描述对象的行为
 * 3. 构造器：用于创建对象
 * 4. 静态成员：类级别的成员（详见静态部分）
 * 5. 代码块：静态代码块、实例代码块
 * 6. 内部类：类中定义的类
 *
 * =============== 内存位置 ===============
 *
 * 类信息：方法区（Metaspace，JDK 8+）
 * 对象：堆（Heap）
 * 局部变量、方法调用：栈（Stack）
 * 静态变量：方法区（JDK 8 后逻辑上在堆中）
 *
 * =============== 变量的分类 ===============
 *
 *                 位置              生命周期        默认值
 * 静态变量       方法区            类加载~程序结束  有默认值
 * 实例变量       堆（对象内）      对象创建~回收    有默认值
 * 局部变量       栈                方法执行时       必须初始化
 *
 * =============== 修饰符总结 ===============
 *
 * public/protected/private/(默认)：访问控制（见 Encapsulation.java）
 * static：静态成员，属于类
 * final：不可修改
 * abstract：抽象（见 AbstractClass.java）
 */
