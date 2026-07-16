/**
 * SealedClass.java - 密封类（JDK 17+ 正式特性）
 * <p>
 * 学习要点：
 * 1. sealed 关键字（受限的继承）
 * 2. permits 关键字（明确指定允许的子类）
 * 3. 子类的三种选择：final、sealed、non-sealed
 * 4. sealed 与 switch 模式匹配（穷尽性检查）
 * 5. sealed 接口
 */
public class SealedClass {

    public static void main(String[] args) {

        // ============ 1. 使用密封类 ============
        System.out.println("========== 使用密封类 ==========");
        MyShape[] shapes = {
            new MyCircle2(5),
            new MySquare(4),
            new MyTriangle(3, 4)
        };

        for (MyShape s : shapes) {
            System.out.println("面积: " + area(s));
        }

        // ============ 2. sealed + 模式匹配（穷尽性） ============
        System.out.println("\n========== 模式匹配 ==========");
        for (MyShape s : shapes) {
            String info = describeShape(s);
            System.out.println(info);
        }

        // ============ 3. sealed 接口 ============
        System.out.println("\n========== sealed 接口 ==========");
        Result<Integer> r1 = new Success<>(100);
        Result<Integer> r2 = new Failure<>("失败原因");

        printResult(r1);
        printResult(r2);
    }

    // 编译器可以确认已经覆盖了所有子类
    // 不需要 default 分支！
    static double area(MyShape s) {
        return switch (s) {
            case MyCircle2 c -> Math.PI * c.radius() * c.radius();
            case MySquare sq -> sq.side() * sq.side();
            case MyTriangle t -> 0.5 * t.base() * t.height();
        };
    }

    static String describeShape(MyShape s) {
        return switch (s) {
            case MyCircle2 c when c.radius() > 10 -> "大圆";
            case MyCircle2 c -> "小圆，半径 " + c.radius();
            case MySquare sq -> "正方形，边长 " + sq.side();
            case MyTriangle t -> "三角形";
        };
    }

    static <T> void printResult(Result<T> r) {
        // 模式匹配 sealed 接口
        String msg = switch (r) {
            case Success<T> s -> "成功: " + s.value();
            case Failure<T> f -> "失败: " + f.error();
        };
        System.out.println(msg);
    }
}

// ============ 1. sealed 类 ============
// sealed 关键字：明确指定允许哪些类继承
// permits 后面列出允许的子类
sealed abstract class MyShape permits MyCircle2, MySquare, MyTriangle {
    // 通用方法
    public abstract double area();
}

// 子类必须选择以下之一：
// 1. final：不能再被继承
// 2. sealed：也是密封的，需要 permits
// 3. non-sealed：解除密封限制，可以任意继承

// final：终止继承链
final class MyCircle2 extends MyShape {
    private final double radius;
    public MyCircle2(double radius) { this.radius = radius; }
    public double radius() { return radius; }

    @Override
    public double area() { return Math.PI * radius * radius; }
}

// non-sealed：允许其他类再继承
non-sealed class MySquare extends MyShape {
    private final double side;
    public MySquare(double side) { this.side = side; }
    public double side() { return side; }

    @Override
    public double area() { return side * side; }
}

// 使用 non-sealed 的子类可以任意扩展
class ColoredSquare extends MySquare {
    private final String color;
    public ColoredSquare(double side, String color) {
        super(side);
        this.color = color;
    }
    public String color() { return color; }
}

// sealed：继续密封
sealed class MyTriangle extends MyShape permits RightTriangle {
    private final double base;
    private final double height;

    public MyTriangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public double base() { return base; }
    public double height() { return height; }

    @Override
    public double area() { return 0.5 * base * height; }
}

final class RightTriangle extends MyTriangle {
    public RightTriangle(double base, double height) {
        super(base, height);
    }
}

// ============ 2. sealed 接口 ============
sealed interface Result<T> permits Success, Failure {
}

record Success<T>(T value) implements Result<T> {}

record Failure<T>(String error) implements Result<T> {}

// ============ 3. 简化写法：如果所有子类都在同一文件，permits 可省略 ============
sealed interface Payment {}
record CashPayment(double amount) implements Payment {}
record CardPayment(double amount, String cardNo) implements Payment {}
record CryptoPayment(double amount, String wallet) implements Payment {}

/*
 * =============== 密封类的目的 ===============
 *
 * 1. 明确表达"这个类型只有这些子类"的意图
 * 2. 允许编译器做穷尽性检查（switch 表达式）
 * 3. 提高设计的表达力和类型安全
 * 4. 是 record + sealed + pattern matching 组合的重要基础
 *
 * =============== sealed 的语法 ===============
 *
 * // 显式列出允许的子类：
 * sealed class A permits B, C, D {}
 *
 * // 同一文件（编译单元）内可以省略 permits：
 * sealed interface X {}
 * record Y() implements X {}
 * record Z() implements X {}
 *
 * =============== 子类的三种选择 ===============
 *
 * 每个 sealed 类的子类必须明确选择：
 * - final：最常见，终止继承
 * - sealed：继续密封链，需再写 permits
 * - non-sealed：解除密封，任何类都能继承
 *
 * =============== 与模式匹配的结合 ===============
 *
 * sealed + switch 表达式：编译器检查所有分支覆盖，无需 default
 * sealed + record + pattern matching = 代数数据类型（ADT）风格
 *
 * =============== 典型应用 ===============
 *
 * 1. 表示"有限的选择"：JSON 值、AST 节点、状态机
 * 2. Result 类型：Success/Failure（避免异常）
 * 3. 领域建模：账户类型、订单状态
 * 4. 编译器/DSL：Token、Expression
 *
 * =============== 限制 ===============
 *
 * 1. 命名模块中 permitted 子类必须在同一模块；未命名模块中必须在同一包
 * 2. 子类必须直接继承密封类
 * 3. 子类必须声明为 final、sealed 或 non-sealed
 */
