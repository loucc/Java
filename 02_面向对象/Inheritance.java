/**
 * Inheritance.java - 继承
 * <p>
 * 学习要点：
 * 1. 继承的概念（extends）
 * 2. super 关键字
 * 3. 方法重写（Override）
 * 4. 构造器与继承
 * 5. Object 类
 * 6. final 类和 final 方法
 */
public class Inheritance {

    public static void main(String[] args) {

        // ============ 1. 使用子类对象 ============
        System.out.println("========== 继承示例 ==========");
        Dog dog = new Dog("旺财", 3, "金毛");
        dog.eat();                              // 继承自 Animal
        dog.sleep();                            // 继承自 Animal
        dog.bark();                             // 自己的方法
        dog.showInfo();                         // 重写的方法

        System.out.println();
        Cat cat = new Cat("咪咪", 2, true);
        cat.eat();
        cat.meow();
        cat.showInfo();

        // ============ 2. Object 类 ============
        System.out.println("\n========== Object 类 ==========");
        System.out.println("dog.toString() = " + dog);          // 自动调用 toString
        System.out.println("dog.hashCode() = " + dog.hashCode());
        System.out.println("dog.getClass() = " + dog.getClass().getSimpleName());

        Dog dog2 = new Dog("旺财", 3, "金毛");
        System.out.println("dog == dog2: " + (dog == dog2));    // false
        System.out.println("dog.equals(dog2): " + dog.equals(dog2));  // true（重写了）

        // ============ 3. instanceof 判断类型 ============
        System.out.println("\n========== instanceof ==========");
        Animal a = dog;                                          // 向上转型
        System.out.println("a instanceof Animal: " + (a instanceof Animal));    // true
        System.out.println("a instanceof Dog: " + (a instanceof Dog));          // true
        System.out.println("a instanceof Cat: " + (a instanceof Cat));          // false

        // 模式匹配 instanceof（JDK 16+）
        if (a instanceof Dog d) {
            d.bark();                           // d 自动是 Dog 类型
        }
    }
}

// ============ 父类 ============
class Animal {
    // 保护字段：子类可访问
    protected String name;
    protected int age;

    public Animal() {
        System.out.println("[Animal 无参构造]");
    }

    public Animal(String name, int age) {
        System.out.println("[Animal 有参构造]");
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " 正在吃东西");
    }

    public void sleep() {
        System.out.println(name + " 正在睡觉");
    }

    public void showInfo() {
        System.out.println("Animal: " + name + ", " + age + " 岁");
    }

    // 重写 Object 的 toString
    @Override
    public String toString() {
        return "Animal{name='" + name + "', age=" + age + "}";
    }

    // 重写 equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Animal animal = (Animal) obj;
        return age == animal.age && java.util.Objects.equals(name, animal.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age);
    }
}

// ============ 子类 Dog ============
// 使用 extends 关键字继承
class Dog extends Animal {
    private String breed;                       // 子类新增字段

    public Dog(String name, int age, String breed) {
        // super() 必须是第一条语句（JDK 25 之前）
        super(name, age);                       // 调用父类构造
        this.breed = breed;
        System.out.println("[Dog 构造]");
    }

    // 子类新增方法
    public void bark() {
        System.out.println(name + " 汪汪叫");
    }

    // 方法重写（覆盖父类方法）
    // @Override 是可选的注解，但强烈推荐使用（有编译期检查）
    @Override
    public void showInfo() {
        // 可以通过 super 调用父类方法
        super.showInfo();
        System.out.println("品种: " + breed);
    }
}

// ============ 子类 Cat ============
class Cat extends Animal {
    private boolean isCurious;

    public Cat(String name, int age, boolean isCurious) {
        super(name, age);
        this.isCurious = isCurious;
    }

    public void meow() {
        System.out.println(name + " 喵喵叫");
    }

    @Override
    public void showInfo() {
        System.out.println("Cat: " + name + ", " + age + " 岁, 好奇: " + isCurious);
    }
}

/*
 * =============== 继承的特点 ===============
 *
 * 1. Java 是单继承（一个类只能有一个直接父类）
 * 2. 一个类可以实现多个接口（多实现）
 * 3. 所有类都直接或间接继承 Object
 * 4. 子类继承父类的所有非 private 成员
 * 5. 构造器不能被继承
 * 6. private 成员子类不能直接访问
 *
 * =============== super 关键字 ===============
 *
 * 1. super.成员变量：访问父类字段
 * 2. super.方法()：调用父类方法
 * 3. super(参数)：调用父类构造器
 *
 * =============== 方法重写规则 ===============
 *
 * 1. 方法名、参数列表完全相同
 * 2. 返回类型相同或是原类型的子类（协变返回）
 * 3. 访问权限不能比父类更严格
 * 4. 抛出的异常不能比父类更多
 * 5. private 方法不能被重写（因为对子类不可见）
 * 6. static 方法不能被重写（但可以隐藏）
 * 7. final 方法不能被重写
 *
 * =============== Object 类的常用方法 ===============
 *
 * toString()      : 返回对象的字符串描述
 * equals(Object)  : 判断两个对象是否"相等"
 * hashCode()      : 返回哈希码（用于 HashMap 等）
 * getClass()      : 返回运行时类
 * clone()         : 克隆对象（需实现 Cloneable）
 * wait/notify     : 线程通信（详见并发部分）
 *
 * =============== final 关键字 ===============
 *
 * final 类：不能被继承（如 String、Integer）
 * final 方法：不能被重写
 * final 变量：不能被重新赋值（常量）
 */
