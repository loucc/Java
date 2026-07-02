import java.util.List;

/**
 * Record.java - 记录类（JDK 14+ 正式特性）
 * <p>
 * 学习要点：
 * 1. record 的定义（简洁的不可变数据类）
 * 2. 编译器自动生成的方法
 * 3. 紧凑构造器
 * 4. 自定义构造器和方法
 * 5. record 实现接口
 * 6. record 嵌套
 */
public class MyRecord {

    public static void main(String[] args) {

        // ============ 1. 基本使用 ============
        System.out.println("========== Record 基本使用 ==========");
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(5, 6);

        // 编译器自动生成 toString
        System.out.println("p1 = " + p1);

        // 自动生成访问器方法（注意：不是 getX()，是 x()）
        System.out.println("p1.x() = " + p1.x());
        System.out.println("p1.y() = " + p1.y());

        // 自动生成 equals 和 hashCode（基于字段）
        System.out.println("p1.equals(p2): " + p1.equals(p2));   // true
        System.out.println("p1.equals(p3): " + p1.equals(p3));   // false
        System.out.println("p1.hashCode() == p2.hashCode(): "
            + (p1.hashCode() == p2.hashCode()));                  // true

        // ============ 2. 紧凑构造器（数据校验） ============
        System.out.println("\n========== 紧凑构造器 ==========");
        try {
            Range r = new Range(1, 10);
            System.out.println("有效范围: " + r);

            Range invalid = new Range(10, 1);   // 抛出异常
        } catch (IllegalArgumentException e) {
            System.out.println("校验错误: " + e.getMessage());
        }

        // ============ 3. 自定义方法 ============
        System.out.println("\n========== 自定义方法 ==========");
        Rectangle rect = new Rectangle(4, 5);
        System.out.println("矩形: " + rect);
        System.out.println("面积: " + rect.area());
        System.out.println("周长: " + rect.perimeter());

        // 静态方法
        Rectangle square = Rectangle.square(5);
        System.out.println("正方形: " + square);

        // ============ 4. 实现接口 ============
        System.out.println("\n========== 实现接口 ==========");
        Circle circle = new Circle(3);
        System.out.println("圆的面积: " + circle.area());

        // ============ 5. record 嵌套（表达复杂数据） ============
        System.out.println("\n========== 嵌套 record ==========");
        Address addr = new Address("北京", "海淀区", "中关村大街");
        Person person = new Person("张三", 25, addr);
        System.out.println(person);

        // ============ 6. 局部 record（在方法内定义） ============
        System.out.println("\n========== 局部 record ==========");
        localRecordDemo();

        // ============ 7. 与集合的集成 ============
        System.out.println("\n========== 使用 record 作为 DTO ==========");
        List<Point> points = List.of(
            new Point(1, 2),
            new Point(3, 4),
            new Point(5, 6)
        );

        points.forEach(System.out::println);

        // record 完美适合流处理
        double avgX = points.stream()
            .mapToInt(Point::x)
            .average()
            .orElse(0);
        System.out.println("平均 x: " + avgX);
    }

    static void localRecordDemo() {
        // record 可以定义在方法内部
        record Pair(String key, int value) {}

        var pairs = List.of(
            new Pair("a", 1),
            new Pair("b", 2)
        );

        for (var p : pairs) {
            System.out.println(p.key() + " → " + p.value());
        }
    }
}

// ============ 1. 最简单的 record ============
// 编译器自动生成：
//   - 私有 final 字段：private final int x, y;
//   - 全参构造器
//   - 访问器方法：x(), y()
//   - equals()、hashCode()、toString()
record Point(int x, int y) {}

// ============ 2. 带校验的紧凑构造器 ============
record Range(int min, int max) {

    // 紧凑构造器：没有参数列表，也不能修改字段
    // 用于校验参数
    public Range {
        if (min > max) {
            throw new IllegalArgumentException("min > max: " + min + " > " + max);
        }
        // 参数自动赋值给字段，不用写 this.min = min
    }
}

// ============ 3. 添加自定义方法和静态方法 ============
record Rectangle(double width, double height) {

    // 紧凑构造器
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("宽高必须为正数");
        }
    }

    // 实例方法
    public double area() {
        return width * height;
    }

    public double perimeter() {
        return 2 * (width + height);
    }

    // 静态工厂方法
    public static Rectangle square(double side) {
        return new Rectangle(side, side);
    }

    // 静态常量
    public static final Rectangle UNIT = new Rectangle(1, 1);
}

// ============ 4. record 实现接口 ============
interface Shape2 {
    double area();
}

record Circle(double radius) implements Shape2 {
    // 覆盖：默认 record 也生成 public double radius()

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// ============ 5. 复合 record ============
record Address(String city, String district, String street) {}

record Person(String name, int age, Address address) {}

/*
 * =============== record 的特点 ===============
 *
 * 1. record 是隐式 final 的（不能被继承）
 * 2. record 的字段是 private final（不可变）
 * 3. 每个组件自动生成访问器方法（如 x() 而不是 getX()）
 * 4. 自动生成 equals()、hashCode()、toString()
 * 5. 自动生成全参构造器（规范构造器）
 * 6. record 隐式继承 java.lang.Record（不能显式继承其他类）
 * 7. record 可以实现接口
 * 8. 支持嵌套、局部 record
 *
 * =============== 三种构造器 ===============
 *
 * 1. 规范构造器（Canonical）：自动生成，接收所有字段
 * 2. 紧凑构造器（Compact）：省略参数列表，用于校验
 *    public Range {
 *        if (min > max) throw new IllegalArgumentException();
 *    }
 * 3. 自定义构造器：必须调用其他构造器（this()）
 *    public Range(int size) {
 *        this(0, size);
 *    }
 *
 * =============== record vs 类 ===============
 *
 * 用 record 的场景：
 * - 简单的数据载体（DTO、值对象、坐标、结果）
 * - 不可变数据
 * - 需要 equals/hashCode 基于字段
 * - 用作 Map 的 key 或 Set 元素
 *
 * 用普通类的场景：
 * - 需要可变状态
 * - 需要继承其他类
 * - 字段需要复杂的初始化逻辑
 * - 需要控制访问器命名（getX 而非 x）
 *
 * =============== record 与模式匹配 ===============
 *
 * record 支持解构（详见 PatternMatching.java）：
 *   if (obj instanceof Point(int x, int y)) {
 *       // 可以直接使用 x 和 y
 *   }
 */
