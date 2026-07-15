import java.util.function.*;
import java.util.List;
import java.util.Arrays;

/**
 * Lambda.java - Lambda 表达式和方法引用
 * <p>
 * 学习要点：
 * 1. Lambda 表达式的语法
 * 2. Lambda 与函数式接口
 * 3. 方法引用（4 种）
 * 4. 变量捕获（final 或事实上 final）
 * 5. Lambda 的应用场景
 */
public class Lambda {

    public static void main(String[] args) {

        // ============ 1. Lambda 基本语法 ============
        System.out.println("========== Lambda 语法 ==========");

        // 匿名内部类写法
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类实现 Runnable");
            }
        };
        r1.run();

        // Lambda 表达式
        Runnable r2 = () -> System.out.println("Lambda 实现 Runnable");
        r2.run();

        // Lambda 语法：
        //   () -> 表达式                      // 无参、单行
        //   () -> { 语句块; }                  // 无参、多行
        //   (x) -> 表达式                     // 一个参数（括号可省）
        //   x -> x * 2                        // 单参数省略括号
        //   (x, y) -> x + y                   // 多参数
        //   (int x, int y) -> x + y           // 可以指定类型

        // ============ 2. 各种 Lambda 示例 ============
        System.out.println("\n========== 各种 Lambda ==========");

        // 无参数
        Supplier<String> greet = () -> "Hello";
        System.out.println(greet.get());

        // 一个参数
        Function<Integer, Integer> square = x -> x * x;
        System.out.println("5 的平方: " + square.apply(5));

        // 两个参数
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println("3 + 4 = " + add.apply(3, 4));

        // 有语句块
        Function<Integer, String> describe = n -> {
            if (n > 0) return "正数";
            else if (n < 0) return "负数";
            else return "零";
        };
        System.out.println("-5: " + describe.apply(-5));

        // ============ 3. Lambda 与函数式接口 ============
        System.out.println("\n========== 函数式接口 ==========");
        // 只要接口只有一个抽象方法，就可以用 Lambda 实现

        // 自定义函数式接口
        MyFunc<Integer, Integer> triple = x -> x * 3;
        System.out.println("triple(5) = " + triple.apply(5));

        // 内置函数式接口
        // Predicate<T>: T -> boolean
        Predicate<Integer> isPositive = n -> n > 0;
        System.out.println("5 是正数: " + isPositive.test(5));
        System.out.println("-3 是正数: " + isPositive.test(-3));

        // Function<T, R>: T -> R
        Function<String, Integer> length = s -> s.length();
        System.out.println("'hello' 长度: " + length.apply("hello"));

        // Consumer<T>: T -> void
        Consumer<String> printer = s -> System.out.println("打印: " + s);
        printer.accept("hi");

        // Supplier<T>: () -> T（无参单方法调用可用方法引用）
        Supplier<Double> random = Math::random;
        System.out.println("随机数: " + random.get());

        // ============ 4. 方法引用（Lambda 的进一步简化） ============
        System.out.println("\n========== 方法引用 ==========");

        // 方式一：类::静态方法
        // Lambda: x -> Integer.parseInt(x)
        Function<String, Integer> parse1 = Integer::parseInt;
        System.out.println(parse1.apply("42"));

        // 方式二：对象::实例方法
        String s = "Hello";
        // Lambda: () -> s.toUpperCase()
        Supplier<String> upper = s::toUpperCase;
        System.out.println(upper.get());

        // 方式三：类::实例方法（第一个参数作为对象）
        // Lambda: x -> x.length()
        Function<String, Integer> len = String::length;
        System.out.println("长度: " + len.apply("Java"));

        // 方式四：类::new (构造器引用)
        // Lambda: () -> new StringBuilder()
        Supplier<StringBuilder> sbCreator = StringBuilder::new;
        StringBuilder sb = sbCreator.get();
        sb.append("built");
        System.out.println(sb);

        // ============ 5. Lambda 在集合中的应用 ============
        System.out.println("\n========== 集合中的 Lambda ==========");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        // forEach
        nums.forEach(n -> System.out.print(n + " "));
        System.out.println();

        // 方法引用
        nums.forEach(System.out::println);

        // 排序
        List<String> words = new java.util.ArrayList<>(List.of("banana", "apple", "cherry"));
        words.sort((a, b) -> a.compareTo(b));           // Lambda
        words.sort(String::compareTo);                   // 方法引用
        System.out.println("排序后: " + words);

        // 移除元素
        List<Integer> ns = new java.util.ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        ns.removeIf(n -> n % 2 == 0);                    // 移除偶数
        System.out.println("奇数: " + ns);

        // ============ 6. 变量捕获 ============
        System.out.println("\n========== 变量捕获 ==========");
        // Lambda 可以访问外部的：
        //   - 静态字段和实例字段（读写皆可）
        //   - 局部变量（必须 final 或事实上 final，只读）

        int factor = 10;                        // 事实上 final
        Function<Integer, Integer> multiplier = x -> x * factor;
        System.out.println("5 * factor = " + multiplier.apply(5));

        // factor = 20;                         // 修改会导致上面的 Lambda 编译错误

        // Lambda 中的 this 指外部类
        new Lambda().captureThis();

        // ============ 7. Lambda 组合 ============
        System.out.println("\n========== Lambda 组合 ==========");

        // Function 的 andThen / compose
        Function<Integer, Integer> plus3 = x -> x + 3;
        Function<Integer, Integer> times2 = x -> x * 2;

        Function<Integer, Integer> plus3ThenTimes2 = plus3.andThen(times2);
        System.out.println("(5+3)*2 = " + plus3ThenTimes2.apply(5));

        Function<Integer, Integer> times2ThenPlus3 = plus3.compose(times2);
        System.out.println("5*2+3 = " + times2ThenPlus3.apply(5));

        // Predicate 的 and / or / negate
        Predicate<Integer> positive = n -> n > 0;
        Predicate<Integer> even = n -> n % 2 == 0;
        Predicate<Integer> positiveAndEven = positive.and(even);
        Predicate<Integer> positiveOrEven = positive.or(even);
        Predicate<Integer> notPositive = positive.negate();

        System.out.println("6 是正偶数: " + positiveAndEven.test(6));
        System.out.println("-2 是正数或偶数: " + positiveOrEven.test(-2));
        System.out.println("-5 不是正数: " + notPositive.test(-5));
    }

    public void captureThis() {
        Runnable r = () -> System.out.println("Lambda 中的 this: " + this.getClass().getSimpleName());
        r.run();
    }
}

// 自定义函数式接口
@FunctionalInterface
interface MyFunc<T, R> {
    R apply(T t);

    // 可以有默认方法和静态方法
    default MyFunc<T, R> andThen(MyFunc<R, R> after) {
        return t -> after.apply(apply(t));
    }
}

/*
 * =============== Lambda 表达式语法总结 ===============
 *
 * 完整形式：(参数列表) -> { 语句块 }
 *
 * 简化规则：
 * 1. 只有一个参数，可省略括号：x -> ...
 * 2. 无参必须有括号：() -> ...
 * 3. 单行表达式，省略 { } 和 return：() -> "hello"
 * 4. 参数类型通常可推断，无需写：(x, y) -> x + y
 *
 * =============== 方法引用的 4 种形式 ===============
 *
 * 1. 类::静态方法          Integer::parseInt
 * 2. 对象::实例方法        System.out::println
 * 3. 类::实例方法          String::length（第一个参数作为对象）
 * 4. 类::new               ArrayList::new
 *
 * =============== 内置函数式接口 ===============
 *
 * Function<T, R>       T -> R              apply
 * Predicate<T>         T -> boolean        test
 * Consumer<T>          T -> void           accept
 * Supplier<T>          () -> T             get
 * BiFunction<T,U,R>    (T,U) -> R          apply
 * BiConsumer<T,U>      (T,U) -> void       accept
 * BinaryOperator<T>    (T,T) -> T          apply
 * UnaryOperator<T>     T -> T              apply
 *
 * 基本类型专用（避免装箱）：
 * IntFunction, IntPredicate, IntConsumer, IntSupplier, IntUnaryOperator...
 *
 * =============== Lambda vs 匿名内部类 ===============
 *
 *                     Lambda              匿名内部类
 * 只能用于             函数式接口          任意接口/抽象类
 * this                外部对象            匿名类自身
 * 编译                 InvokeDynamic       独立 .class 文件
 * 性能                 略好                一般
 * 语法                 简洁                冗长
 *
 * =============== 使用建议 ===============
 *
 * 1. 优先用 Lambda 替代单方法匿名类
 * 2. 能用方法引用就用方法引用（更简洁）
 * 3. 复杂逻辑写成命名方法，然后用方法引用
 * 4. 保持 Lambda 简短（超过 5 行考虑抽取方法）
 * 5. 使用内置函数式接口，不轻易自定义
 */
