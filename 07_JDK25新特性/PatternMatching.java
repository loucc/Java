/**
 * PatternMatching.java - 模式匹配（instanceof、switch）
 * <p>
 * 学习要点：
 * 1. 模式匹配 instanceof（JDK 16+ 正式）
 * 2. switch 表达式的模式匹配（JDK 21+ 正式）
 * 3. Type Pattern（类型模式）
 * 4. Record Pattern（记录解构，JDK 21+）
 * 5. Guarded Pattern（守卫模式，when）
 * 6. 穷尽性检查（配合 sealed）
 */
public class PatternMatching {

    public static void main(String[] args) {

        // ============ 1. 传统 instanceof 的痛点 ============
        System.out.println("========== 传统 instanceof ==========");
        Object obj = "Hello Java";

        // 老写法：判断 + 强转 + 用
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println("传统: " + s.length());
        }

        // ============ 2. 模式匹配 instanceof（JDK 16+）============
        System.out.println("\n========== 模式匹配 instanceof ==========");
        // 判断的同时"绑定"到变量，无需强转
        if (obj instanceof String s) {
            System.out.println("模式匹配: " + s.length());
        }

        // 可以在条件中使用
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("长字符串: " + s);
        }

        // 否定分支中不能使用（作用域是"确定为该类型"的分支）
        if (!(obj instanceof String s)) {
            // 这里不能用 s
        } else {
            System.out.println("是字符串: " + s);
        }

        // 提前 return（reverse pattern）
        Object x = "test";
        printLength(x);

        // ============ 3. switch 表达式的模式匹配（JDK 21+）============
        System.out.println("\n========== switch 模式匹配 ==========");
        Object[] values = { 42, "hello", 3.14, true, null, java.util.List.of(1, 2, 3) };

        for (Object v : values) {
            String desc = describe(v);
            System.out.println(v + " → " + desc);
        }

        // ============ 4. Record Pattern（记录解构）============
        System.out.println("\n========== 记录解构 ==========");
        Object p = new PatternPoint(3, 4);
        Object l = new PatternLine(new PatternPoint(0, 0), new PatternPoint(5, 5));

        // 传统写法：instanceof 后逐层 getter
        if (p instanceof PatternPoint pp) {
            System.out.println("传统: (" + pp.x() + ", " + pp.y() + ")");
        }

        // 记录解构：直接提取字段
        if (p instanceof PatternPoint(int px, int py)) {
            System.out.println("解构: (" + px + ", " + py + ")");
        }

        // 嵌套解构
        if (l instanceof PatternLine(PatternPoint(int x1, int y1), PatternPoint(int x2, int y2))) {
            System.out.println("Line 解构: (" + x1 + "," + y1 + ") → (" + x2 + "," + y2 + ")");
        }

        // 在 switch 中使用
        System.out.println("面积: " + area(new PatternCircle(5)));
        System.out.println("面积: " + area(new PatternSquare(4)));

        // ============ 5. Guarded Pattern（守卫模式，when） ============
        System.out.println("\n========== 守卫模式 ==========");
        Object[] items = { 5, -3, "abc", "hello world", new PatternPoint(1, 2), new PatternPoint(0, 0) };
        for (Object item : items) {
            System.out.println(item + " → " + classify(item));
        }

        // ============ 6. sealed + 模式匹配（穷尽性）============
        System.out.println("\n========== sealed + 穷尽性 ==========");
        PatternShape[] shapes = { new PatternCircle(3), new PatternSquare(4), new PatternTriangle(3, 4) };
        for (PatternShape s : shapes) {
            System.out.println(describeShape(s));
        }
    }

    // 用 reverse pattern 提前 return
    static void printLength(Object obj) {
        if (!(obj instanceof String s)) {
            System.out.println("不是字符串");
            return;
        }
        // 此后 s 在作用域内
        System.out.println("长度: " + s.length());
    }

    // 全面的模式匹配 switch
    static String describe(Object o) {
        return switch (o) {
            case null -> "空值";
            case Integer i -> "整数: " + i;
            case Long l -> "长整型: " + l;
            case Double d -> "浮点数: " + d;
            case String s -> "字符串: " + s + " (长度 " + s.length() + ")";
            case Boolean b -> "布尔: " + b;
            case java.util.List<?> list -> "列表 (元素数 " + list.size() + ")";
            default -> "其他类型: " + o.getClass().getSimpleName();
        };
    }

    // 记录解构 + switch
    static double area(PatternShape s) {
        return switch (s) {
            case PatternCircle(double r) -> Math.PI * r * r;
            case PatternSquare(double side) -> side * side;
            case PatternTriangle(double b, double h) -> 0.5 * b * h;
        };
    }

    // 守卫模式（when 子句）
    static String classify(Object o) {
        return switch (o) {
            case Integer i when i > 0 -> "正整数";
            case Integer i when i < 0 -> "负整数";
            case Integer i -> "零";
            case String s when s.isBlank() -> "空白字符串";
            case String s when s.length() > 5 -> "长字符串";
            case String s -> "短字符串";
            case PatternPoint(int x, int y) when x == 0 && y == 0 -> "原点";
            case PatternPoint p -> "点";
            default -> "未知";
        };
    }

    // sealed + switch，编译器强制穷尽
    static String describeShape(PatternShape s) {
        return switch (s) {
            case PatternCircle c -> "圆：半径 " + c.r();
            case PatternSquare sq -> "正方形：边长 " + sq.side();
            case PatternTriangle t -> "三角形：底 " + t.base() + " 高 " + t.height();
            // 不需要 default！sealed 已确保覆盖所有子类
        };
    }
}

record PatternPoint(int x, int y) {}
record PatternLine(PatternPoint start, PatternPoint end) {}

sealed interface PatternShape permits PatternCircle, PatternSquare, PatternTriangle {}
record PatternCircle(double r) implements PatternShape {}
record PatternSquare(double side) implements PatternShape {}
record PatternTriangle(double base, double height) implements PatternShape {}

/*
 * =============== 模式匹配的目的 ===============
 *
 * 消除大量样板代码：类型判断 → 类型强转 → 提取字段 → 使用
 *
 * 传统：if (obj instanceof X) { X x = (X) obj; ... }
 * 模式：if (obj instanceof X x) { ... }
 *
 * =============== 模式的种类 ===============
 *
 * 1. Type Pattern（类型模式）      obj instanceof String s
 * 2. Record Pattern（记录解构）    obj instanceof Point(int x, int y)
 * 3. Guarded Pattern（守卫）       case Integer i when i > 0 ->
 * 4. Any/Null Pattern              case null ->
 *
 * =============== 演进时间线 ===============
 *
 * JDK 14  预览：模式匹配 instanceof
 * JDK 16  正式：模式匹配 instanceof
 * JDK 17  预览：模式匹配 switch
 * JDK 21  正式：模式匹配 switch + Record Pattern
 * JDK 25  第三次预览：原生类型模式匹配（JEP 507，见 PrimitivePatterns.java）
 *
 * =============== switch 表达式的完整能力 ===============
 *
 * String result = switch (obj) {
 *     case null -> "null";                              // null 分支
 *     case Integer i when i < 0 -> "negative int";      // 守卫
 *     case Integer i -> "int: " + i;                    // 类型模式
 *     case String s when s.isBlank() -> "blank str";
 *     case String s -> "str: " + s;
 *     case Point(int x, int y) -> x + "," + y;         // 记录解构
 *     case int[] arr -> "array len " + arr.length;
 *     default -> "unknown";
 * };
 *
 * =============== 穷尽性检查 ===============
 *
 * 编译器要求 switch 表达式必须处理所有可能的值：
 * - Object：必须有 default（因为可能有任意子类）
 * - sealed 类：只要覆盖所有 permitted 子类，无需 default
 * - enum：覆盖所有枚举值，无需 default
 *
 * =============== 使用建议 ===============
 *
 * 1. 优先用模式 instanceof 替代 instanceof + 强转
 * 2. 多分支类型判断用 switch 表达式
 * 3. sealed + record + pattern 是新一代类型建模利器
 * 4. 守卫（when）用于细化条件
 * 5. 保持 case 顺序：更具体的在前
 *
 * =============== 陷阱 ===============
 *
 * 1. Type Pattern 中的变量只在"匹配"分支可见
 * 2. switch 表达式必须穷尽（否则编译错误）
 * 3. 有 case null 时，不需要检查 null（否则要）
 * 4. 顺序敏感：先匹配的先执行
 */
