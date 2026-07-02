import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generics.java - 泛型
 * <p>
 * 学习要点：
 * 1. 泛型类
 * 2. 泛型接口
 * 3. 泛型方法
 * 4. 类型参数命名约定（T, E, K, V, R）
 * 5. 有界类型参数（extends）
 * 6. 通配符（?, ? extends, ? super）
 * 7. 类型擦除
 * 8. 泛型的限制
 */
public class Generics {

    public static void main(String[] args) {

        // ============ 1. 使用泛型类 ============
        System.out.println("========== 泛型类 ==========");
        Box<String> stringBox = new Box<>("hello");
        Box<Integer> intBox = new Box<>(100);
        Box<Double> doubleBox = new Box<>(3.14);

        System.out.println("字符串盒子: " + stringBox.getContent());
        System.out.println("整数盒子: " + intBox.getContent());
        System.out.println("浮点盒子: " + doubleBox.getContent());

        // ============ 2. 使用泛型方法 ============
        System.out.println("\n========== 泛型方法 ==========");
        Integer[] intArr = {1, 2, 3, 4, 5};
        String[] strArr = {"a", "b", "c"};

        printArray(intArr);
        printArray(strArr);

        // 显式指定类型（一般不需要）
        Utils.<String>swap(strArr, 0, 2);
        printArray(strArr);

        // ============ 3. 多个类型参数 ============
        System.out.println("\n========== 多类型参数 ==========");
        Pair<String, Integer> pair = new Pair<>("年龄", 25);
        System.out.println(pair.getKey() + " = " + pair.getValue());

        // ============ 4. 有界类型参数 ============
        System.out.println("\n========== 有界类型 ==========");
        NumberBox<Integer> ibox = new NumberBox<>(10);
        NumberBox<Double> dbox = new NumberBox<>(3.14);
        // NumberBox<String> sbox = new NumberBox<>("hi");  // 编译错误！String 不是 Number

        System.out.println("Integer 平方: " + ibox.square());
        System.out.println("Double 平方: " + dbox.square());

        // ============ 5. 通配符 ============
        System.out.println("\n========== 通配符 ==========");
        List<Integer> intList = List.of(1, 2, 3);
        List<Double> doubleList = List.of(1.1, 2.2, 3.3);

        printNumbers(intList);
        printNumbers(doubleList);

        // 上界通配符：? extends Number 表示 Number 或其子类
        double sum1 = sumOfList(intList);
        double sum2 = sumOfList(doubleList);
        System.out.println("总和: " + sum1 + " / " + sum2);

        // 下界通配符：? super Integer 表示 Integer 或其父类
        List<Number> numList = new ArrayList<>();
        addIntegers(numList);
        System.out.println("加了整数: " + numList);

        // ============ 6. 无界通配符 ============
        System.out.println("\n========== 无界通配符 ==========");
        List<?> anyList = List.of(1, "a", 3.14);
        System.out.println("元素数: " + anyList.size());
        // ? 表示未知类型，不能添加元素（除了 null），只能读取为 Object

        // ============ 7. 泛型 + 集合 ============
        System.out.println("\n========== 泛型集合 ==========");
        Map<String, List<Integer>> scores = new HashMap<>();
        scores.put("张三", List.of(85, 90, 92));
        scores.put("李四", List.of(78, 82, 88));

        for (Map.Entry<String, List<Integer>> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // ============ 8. 泛型的类型擦除 ============
        System.out.println("\n========== 类型擦除 ==========");
        List<String> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();
        // 运行时两者都是 ArrayList，Class 相同
        System.out.println("类相同: " + (l1.getClass() == l2.getClass()));
    }

    // ============ 泛型方法 ============
    // <T> 是类型参数的声明，写在返回类型前
    public static <T> void printArray(T[] arr) {
        System.out.print("数组: [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // 使用上界通配符 ? extends Number
    // 读取：可以，任何 Number 或子类
    // 写入：不能，除了 null
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }

    public static void printNumbers(List<? extends Number> list) {
        for (Number n : list) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    // 使用下界通配符 ? super Integer
    // 读取：只能作为 Object
    // 写入：可以，Integer 或其子类
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
}

// ============ 泛型类 ============
// T 是类型参数（Type parameter）
class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}

// ============ 多个类型参数 ============
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

// ============ 有界类型参数（extends 表示上界）============
// T 必须是 Number 或其子类
class NumberBox<T extends Number> {
    private T value;

    public NumberBox(T value) {
        this.value = value;
    }

    // 因为 T extends Number，可以调用 Number 的方法
    public double square() {
        double d = value.doubleValue();
        return d * d;
    }
}

// ============ 多个上界（用 & 分隔）============
// T 必须同时是 Number 和 Comparable
class SortedBox<T extends Number & Comparable<T>> {
    private T value;

    public SortedBox(T value) { this.value = value; }

    public int compareTo(T other) {
        return value.compareTo(other);
    }
}

// ============ 泛型接口 ============
interface Container<T> {
    void add(T item);
    T get(int index);
    int size();
}

class MyList<T> implements Container<T> {
    private List<T> list = new ArrayList<>();

    @Override
    public void add(T item) { list.add(item); }

    @Override
    public T get(int index) { return list.get(index); }

    @Override
    public int size() { return list.size(); }
}

// ============ 静态工具类的泛型方法 ============
class Utils {

    // 泛型方法可以是 static 的
    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 返回值也可以是泛型
    public static <T> T firstOrDefault(List<T> list, T defaultValue) {
        return list.isEmpty() ? defaultValue : list.get(0);
    }

    // 有界的泛型方法
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
}

/*
 * =============== 类型参数命名约定 ===============
 *
 * T - Type（类型）
 * E - Element（元素，用于集合）
 * K - Key（键）
 * V - Value（值）
 * R - Return（返回值）
 * N - Number（数字）
 * S, U, V - 第二、第三、第四个类型
 *
 * =============== 通配符 ? 详解 ===============
 *
 * ? (无界)              - 未知类型
 * ? extends T (上界)    - T 或 T 的子类
 * ? super T (下界)      - T 或 T 的父类
 *
 * PECS 原则（Effective Java）：
 * - Producer Extends：生产者用 extends（读取数据）
 * - Consumer Super：消费者用 super（写入数据）
 *
 * 例：
 *   Collections.copy(List<? super T> dest, List<? extends T> src)
 *   dest 是消费者（放数据），用 super
 *   src 是生产者（拿数据），用 extends
 *
 * =============== 类型擦除 ===============
 *
 * Java 泛型在编译后会被"擦除"：
 * - List<String> 编译后就是 List
 * - <T> 编译后被替换为 Object（或上界）
 * - 运行时无法获取泛型参数
 *
 * 因此不能：
 * - new T()
 * - new T[10]
 * - T.class
 * - instanceof T
 *
 * =============== 泛型的限制 ===============
 *
 * 1. 不能用基本类型：List<int> ✗，用 List<Integer> ✓
 * 2. 不能创建泛型数组：new T[10] ✗
 * 3. 不能创建泛型对象：new T() ✗
 * 4. 静态成员不能用类的类型参数：class A<T> { static T x; } ✗
 * 5. 泛型类不能继承 Throwable
 *
 * =============== 好处 ===============
 *
 * 1. 编译期类型检查
 * 2. 消除强制类型转换
 * 3. 支持泛型算法
 * 4. 提高代码可读性和可维护性
 */
