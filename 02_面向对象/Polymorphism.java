/**
 * Polymorphism.java - 多态
 * <p>
 * 学习要点：
 * 1. 多态的概念
 * 2. 编译时类型 vs 运行时类型
 * 3. 向上转型（自动）
 * 4. 向下转型（强制，需 instanceof 判断）
 * 5. 多态的应用场景
 * 6. 多态的限制
 */
public class Polymorphism {

    public static void main(String[] args) {

        // ============ 1. 多态的基本形式 ============
        System.out.println("========== 多态基本形式 ==========");

        // 父类引用指向子类对象（向上转型）
        Shape s1 = new Circle(5);
        Shape s2 = new Rectangle(4, 6);
        Shape s3 = new Triangle(3, 4);

        // 调用同一个方法，表现不同——多态
        s1.draw();
        s2.draw();
        s3.draw();

        System.out.println("s1 面积: " + s1.area());
        System.out.println("s2 面积: " + s2.area());
        System.out.println("s3 面积: " + s3.area());

        // ============ 2. 多态数组 ============
        System.out.println("\n========== 多态数组 ==========");
        Shape[] shapes = {
            new Circle(3),
            new Rectangle(2, 5),
            new Triangle(4, 6),
            new Circle(7)
        };

        double totalArea = 0;
        for (Shape shape : shapes) {
            shape.draw();
            totalArea += shape.area();
        }
        System.out.println("总面积: " + totalArea);

        // ============ 3. 多态作为方法参数（重要应用） ============
        System.out.println("\n========== 多态参数 ==========");
        printShapeInfo(new Circle(10));
        printShapeInfo(new Rectangle(3, 3));

        // ============ 4. 向下转型 ============
        System.out.println("\n========== 向下转型 ==========");
        Shape s = new Circle(5);
        // s.getRadius();                       // 编译错误！Shape 中没有此方法

        // 强制转换（前提：s 实际是 Circle）
        if (s instanceof Circle) {
            Circle c = (Circle) s;              // 传统写法
            System.out.println("半径: " + c.getRadius());
        }

        // JDK 16+ 模式匹配写法
        if (s instanceof Circle c) {
            System.out.println("半径 (模式匹配): " + c.getRadius());
        }

        // 错误的向下转型
        try {
            Shape s4 = new Circle(5);
            Rectangle r = (Rectangle) s4;       // ClassCastException
        } catch (ClassCastException e) {
            System.out.println("转型错误: " + e.getMessage());
        }

        // ============ 5. 编译时类型 vs 运行时类型 ============
        System.out.println("\n========== 编译时 vs 运行时 ==========");
        Shape shape = new Circle(5);
        // shape 的编译时类型是 Shape，运行时类型是 Circle
        // 调用方法看运行时类型：多态
        // 访问字段看编译时类型（不支持多态）：字段无多态

        shape.draw();                           // 调用 Circle.draw()
        System.out.println("shape.name = " + shape.name);   // Shape 的 name（如果都有 name 字段）

        // ============ 6. static 方法没有多态 ============
        System.out.println("\n========== static 方法 ==========");
        Base b = new Sub();
        b.instanceMethod();                     // Sub 的方法（多态）
        b.staticMethod();                       // Base 的方法（无多态）
    }

    // 多态方法：可以接收 Shape 的任何子类
    public static void printShapeInfo(Shape shape) {
        System.out.println("--- 形状信息 ---");
        shape.draw();
        System.out.println("面积: " + shape.area());
    }
}

// ============ 形状家族 ============
abstract class Shape {
    String name = "通用形状";

    public abstract void draw();
    public abstract double area();
}

class Circle extends Shape {
    String name = "圆";                        // 与父类同名字段（不推荐）
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    @Override
    public void draw() {
        System.out.println("画一个圆，半径 " + radius);
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("画一个矩形，" + width + " x " + height);
    }

    @Override
    public double area() {
        return width * height;
    }
}

class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw() {
        System.out.println("画一个三角形，底 " + base + ", 高 " + height);
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// ============ 展示 static 方法没有多态 ============
class Base {
    public static void staticMethod() {
        System.out.println("Base.staticMethod");
    }

    public void instanceMethod() {
        System.out.println("Base.instanceMethod");
    }
}

class Sub extends Base {
    // 这不是重写，是"隐藏"
    public static void staticMethod() {
        System.out.println("Sub.staticMethod");
    }

    // 真正的重写
    @Override
    public void instanceMethod() {
        System.out.println("Sub.instanceMethod");
    }
}

/*
 * =============== 多态的三大要素 ===============
 *
 * 1. 继承（extends）或实现（implements）
 * 2. 方法重写（Override）
 * 3. 父类引用指向子类对象
 *
 * =============== 多态的好处 ===============
 *
 * 1. 提高代码的可扩展性
 *    - 新增子类无需修改现有代码
 * 2. 提高代码的可维护性
 *    - 面向父类/接口编程，实现"开闭原则"
 * 3. 统一处理多种类型
 *    - 一个方法适用于多种类型
 *
 * =============== 多态的规则 ===============
 *
 * 1. 方法调用：运行时类型（动态绑定）
 * 2. 字段访问：编译时类型（静态绑定）
 * 3. 静态方法：编译时类型（无多态）
 * 4. private 方法：编译时类型（无多态）
 * 5. final 方法：编译时类型（无多态）
 *
 * =============== 类型转换 ===============
 *
 * 向上转型：Shape s = new Circle(5);         // 自动，安全
 * 向下转型：Circle c = (Circle) s;           // 强制，需 instanceof 验证
 *
 * 最佳实践：使用模式匹配
 *   if (s instanceof Circle c) { ... }        // JDK 16+
 */
