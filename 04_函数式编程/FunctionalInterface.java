import java.util.function.*;
import java.util.List;

/**
 * FunctionalInterface.java - 函数式接口
 * <p>
 * 学习要点：
 * 1. @FunctionalInterface 注解
 * 2. 四大核心函数式接口
 * 3. 函数式接口的默认方法
 * 4. 各种基本类型专用函数式接口
 * 5. BiFunction 系列
 */
public class FunctionalInterface {

    public static void main(String[] args) {

        // ============ 1. Function<T, R>: T → R ============
        System.out.println("========== Function ==========");
        Function<String, Integer> len = String::length;
        System.out.println("length of 'Hello' = " + len.apply("Hello"));

        // andThen: 先执行 this，再执行 after
        Function<Integer, Integer> times2 = x -> x * 2;
        Function<String, Integer> lenTimes2 = len.andThen(times2);
        System.out.println("length * 2 = " + lenTimes2.apply("Hello"));

        // compose: 先执行 before，再执行 this
        Function<String, String> toUpper = String::toUpperCase;
        Function<String, Integer> upperLen = len.compose(toUpper);
        System.out.println("upper length = " + upperLen.apply("hello"));

        // identity: T -> T
        Function<Integer, Integer> id = Function.identity();
        System.out.println("identity(5) = " + id.apply(5));

        // ============ 2. Predicate<T>: T → boolean ============
        System.out.println("\n========== Predicate ==========");
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;

        System.out.println("5 > 0 : " + isPositive.test(5));
        System.out.println("6 是偶数: " + isEven.test(6));

        // and / or / negate
        Predicate<Integer> positiveEven = isPositive.and(isEven);
        Predicate<Integer> positiveOrEven = isPositive.or(isEven);
        Predicate<Integer> notPositive = isPositive.negate();

        System.out.println("6 是正偶: " + positiveEven.test(6));
        System.out.println("-2 是正或偶: " + positiveOrEven.test(-2));
        System.out.println("-5 非正: " + notPositive.test(-5));

        // isEqual: 与某值相等
        Predicate<String> isHello = Predicate.isEqual("hello");
        System.out.println("isEqual('hello'): " + isHello.test("hello"));

        // ============ 3. Consumer<T>: T → void ============
        System.out.println("\n========== Consumer ==========");
        Consumer<String> print = System.out::println;
        print.accept("使用 Consumer 打印");

        // andThen: 组合多个 Consumer
        Consumer<String> printTwice = print.andThen(print);
        printTwice.accept("打印两次");

        // ============ 4. Supplier<T>: () → T ============
        System.out.println("\n========== Supplier ==========");
        Supplier<String> greeter = () -> "Hello, Java";
        System.out.println(greeter.get());

        Supplier<java.util.List<Integer>> listSupplier = java.util.ArrayList::new;
        System.out.println("新集合: " + listSupplier.get());

        // ============ 5. BiFunction<T, U, R>: (T, U) → R ============
        System.out.println("\n========== BiFunction ==========");
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("3 + 4 = " + add.apply(3, 4));

        BiFunction<String, String, String> concat = (a, b) -> a + b;
        System.out.println(concat.apply("Hello, ", "World"));

        // ============ 6. BiPredicate、BiConsumer ============
        System.out.println("\n========== BiPredicate / BiConsumer ==========");
        BiPredicate<String, Integer> lengthEquals = (s, n) -> s.length() == n;
        System.out.println("hello 长度为 5: " + lengthEquals.test("hello", 5));

        BiConsumer<String, Integer> printKV = (k, v) -> System.out.println(k + " = " + v);
        printKV.accept("age", 25);

        // ============ 7. UnaryOperator / BinaryOperator ============
        System.out.println("\n========== UnaryOperator / BinaryOperator ==========");
        // UnaryOperator<T> = Function<T, T>
        UnaryOperator<Integer> negate = x -> -x;
        System.out.println("-5 → " + negate.apply(5));

        // BinaryOperator<T> = BiFunction<T, T, T>
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        System.out.println("3 * 4 = " + multiply.apply(3, 4));

        // BinaryOperator.maxBy / minBy
        BinaryOperator<Integer> maxOp = BinaryOperator.maxBy(Integer::compare);
        System.out.println("max(3, 7) = " + maxOp.apply(3, 7));

        // ============ 8. 基本类型专用（避免装箱拆箱） ============
        System.out.println("\n========== 基本类型专用 ==========");
        // IntFunction<R>: int → R
        IntFunction<String> intToStr = i -> "Number: " + i;
        System.out.println(intToStr.apply(42));

        // ToIntFunction<T>: T → int
        ToIntFunction<String> toLen = String::length;
        System.out.println("length: " + toLen.applyAsInt("hello"));

        // IntPredicate: int → boolean
        IntPredicate isPos = i -> i > 0;
        System.out.println("5 > 0: " + isPos.test(5));

        // IntConsumer: int → void
        IntConsumer intPrinter = i -> System.out.println("int: " + i);
        intPrinter.accept(100);

        // IntSupplier: () → int
        IntSupplier randInt = () -> (int) (Math.random() * 100);
        System.out.println("随机整数: " + randInt.getAsInt());

        // IntUnaryOperator: int → int
        IntUnaryOperator doubleIt = i -> i * 2;
        System.out.println("6*2 = " + doubleIt.applyAsInt(6));

        // IntBinaryOperator: (int, int) → int
        IntBinaryOperator sum = Integer::sum;
        System.out.println("3+4 = " + sum.applyAsInt(3, 4));

        // ============ 9. 实际应用示例 ============
        System.out.println("\n========== 实际应用 ==========");
        // 策略模式
        System.out.println("加: " + operate(10, 5, (a, b) -> a + b));
        System.out.println("减: " + operate(10, 5, (a, b) -> a - b));
        System.out.println("乘: " + operate(10, 5, (a, b) -> a * b));

        // 过滤 + 映射
        List<String> names = List.of("Alice", "Bob", "Charlie", "Dave");
        printFiltered(names, s -> s.length() > 3, String::toUpperCase);
    }

    // 使用函数式接口作为参数（策略模式）
    static int operate(int a, int b, BinaryOperator<Integer> op) {
        return op.apply(a, b);
    }

    // 更复杂：接受过滤器和转换器
    static void printFiltered(List<String> list, Predicate<String> filter, Function<String, String> mapper) {
        for (String s : list) {
            if (filter.test(s)) {
                System.out.println(mapper.apply(s));
            }
        }
    }
}

/*
 * =============== 四大核心函数式接口 ===============
 *
 * Function<T, R>       T -> R              apply(t)
 * Predicate<T>         T -> boolean        test(t)
 * Consumer<T>          T -> void           accept(t)
 * Supplier<T>          () -> T             get()
 *
 * =============== 双参数版本 ===============
 *
 * BiFunction<T, U, R>  (T, U) -> R         apply(t, u)
 * BiPredicate<T, U>    (T, U) -> boolean   test(t, u)
 * BiConsumer<T, U>     (T, U) -> void      accept(t, u)
 *
 * =============== 特化接口 ===============
 *
 * UnaryOperator<T>     T -> T              apply(t)   （Function 特化）
 * BinaryOperator<T>    (T, T) -> T         apply(t, t) （BiFunction 特化）
 *
 * =============== 基本类型专用（避免装箱） ===============
 *
 * IntFunction<R>       int -> R
 * ToIntFunction<T>     T -> int
 * IntUnaryOperator     int -> int
 * IntBinaryOperator    (int, int) -> int
 * IntPredicate         int -> boolean
 * IntConsumer          int -> void
 * IntSupplier          () -> int
 *
 * 同样有 Long / Double 版本
 *
 * =============== 默认方法一览 ===============
 *
 * Function:
 *   andThen(after)     先 this 后 after
 *   compose(before)    先 before 后 this
 *   identity()         身份函数（静态）
 *
 * Predicate:
 *   and(other)         与
 *   or(other)          或
 *   negate()           非
 *   isEqual(target)    等于（静态）
 *   not(target)        取反（JDK 11+，静态）
 *
 * Consumer:
 *   andThen(after)     先 this 后 after
 *
 * =============== @FunctionalInterface 注解 ===============
 *
 * 1. 标记一个接口是函数式接口
 * 2. 编译器会检查：有且仅有一个抽象方法
 * 3. 可以有 default、static、Object 的方法
 * 4. 不加也可以用 Lambda，但加上更清晰、更安全
 */
