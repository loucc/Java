/**
 * PrimitivePatterns.java - 原生类型模式匹配（JDK 25 JEP 507 第三次预览）
 * <p>
 * 学习要点：
 * 1. 什么是原生类型模式匹配
 * 2. 之前的限制
 * 3. 新特性：instanceof / switch 支持基本类型
 * 4. 精度转换的安全性检查
 * 5. 与包装类的对比
 * <p>
 * 编译运行需加参数：
 * javac --enable-preview --source 25 PrimitivePatterns.java
 * java  --enable-preview PrimitivePatterns
 */
public class PrimitivePatterns {

    public static void main(String[] args) {

        // ============ 之前的限制 ============
        System.out.println("========== 之前的限制 ==========");
        // JDK 21 之前，模式匹配只支持引用类型，不支持基本类型：
        //   x instanceof int   ✗ 编译错误
        //   case int i ->      ✗ 编译错误
        //
        // 只能：
        //   x instanceof Integer i
        //   case Integer i -> ...

        // ============ JDK 25 支持基本类型模式（预览） ============
        System.out.println("\n========== 原生类型模式（预览） ==========");
        System.out.println("以下代码需 --enable-preview 才能编译运行");

        demonstrate();

        // ============ 类型转换检查 ============
        System.out.println("\n========== 类型转换 ==========");
        System.out.println("原生类型模式支持精度检查");
        System.out.println("比如 long → int 只在值在 int 范围内时匹配");

        checkNarrowing(100L);
        checkNarrowing(Long.MAX_VALUE);
    }

    // 使用原生类型模式匹配（示意用法）
    static void demonstrate() {
        Object o = 42;

        // 传统写法（JDK 21）
        if (o instanceof Integer i) {
            int val = i;
            System.out.println("传统: " + val);
        }

        // JDK 25 预览：直接用 int
        // if (o instanceof int i) {              // 需要 --enable-preview
        //     System.out.println("原生模式: " + i);
        // }

        // switch 的原生类型模式（预览）
        // String desc = switch (o) {
        //     case int i    -> "int: " + i;
        //     case long l   -> "long: " + l;
        //     case double d -> "double: " + d;
        //     case String s -> "str: " + s;
        //     default       -> "other";
        // };

        // 目前非预览环境下的等价写法：
        String desc = switch (o) {
            case Integer i -> "int: " + i;
            case Long l -> "long: " + l;
            case Double d -> "double: " + d;
            case String s -> "str: " + s;
            default -> "other";
        };
        System.out.println(desc);
    }

    // 精度检查示例
    static void checkNarrowing(long value) {
        System.out.println("检查 long 值: " + value);

        // 传统：需手动检查范围
        if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) {
            int i = (int) value;
            System.out.println("  可安全转 int: " + i);
        } else {
            System.out.println("  超出 int 范围");
        }

        // JDK 25 预览：只有值在 int 范围内才匹配
        // if (value instanceof int i) {
        //     System.out.println("  安全转 int: " + i);
        // } else {
        //     System.out.println("  溢出，不匹配");
        // }
    }

    // 使用记录 + 原生类型解构（示意）
    // record Position(int x, int y) {}
    //
    // static void describePos(Object obj) {
    //     if (obj instanceof Position(int x, int y)) {   // 直接解构成 int
    //         System.out.println("位置: " + x + ", " + y);
    //     }
    // }
}

/*
 * =============== JEP 507：原生类型模式匹配 ===============
 *
 * JDK 25 第三次预览。
 *
 * 目标：让 instanceof 和 switch 也能匹配原生类型（int, long, double 等）。
 *
 * =============== 语法 ===============
 *
 * // 原生类型 instanceof
 * if (obj instanceof int i) { ... }
 * if (obj instanceof long l) { ... }
 * if (obj instanceof double d) { ... }
 *
 * // 原生类型 switch
 * String desc = switch (obj) {
 *     case int i    -> "int: " + i;
 *     case long l   -> "long: " + l;
 *     case double d -> "double: " + d;
 *     case boolean b -> "bool: " + b;
 *     ...
 * };
 *
 * // 记录解构中使用原生类型
 * if (obj instanceof Point(int x, int y)) { ... }
 *
 * =============== 类型转换的安全性 ===============
 *
 * 原生类型模式匹配同时是"安全的类型转换"：
 * - 从大类型到小类型：值必须能容纳（比如 long → int 需要值 ≤ Integer.MAX_VALUE）
 * - 值不能容纳时，模式不匹配
 *
 * 例：
 *   Object o = 1_000_000_000_000L;  // 长整数
 *   if (o instanceof int i) { ... } // 不匹配（超出 int 范围）
 *   if (o instanceof long l) { ... } // 匹配
 *
 * 这比传统的强转 (int) l 更安全，避免溢出。
 *
 * =============== 好处 ===============
 *
 * 1. 减少装箱/拆箱开销
 * 2. 消除显式强转
 * 3. 与已有模式匹配统一
 * 4. 安全的类型转换（值域检查）
 *
 * =============== 使用建议 ===============
 *
 * 由于是预览特性，正式代码中还需谨慎：
 * - 需要 --enable-preview 编译和运行
 * - API 可能微调
 * - 建议先在学习/实验中使用
 *
 * 学习目标：
 * 1. 了解这个能力，为将来正式版本做准备
 * 2. 理解值域安全转换的思想
 *
 * =============== 相关 JEP 演进 ===============
 *
 * JDK 20  首次预览
 * JDK 21  第二次预览
 * JDK 25  第三次预览（JEP 507）
 *
 * 稳定后会与模式匹配整体形成完整的类型建模能力：
 *   sealed + record + pattern + primitive 类型
 */
