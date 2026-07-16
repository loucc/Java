import java.util.*;
import java.util.stream.*;

/**
 * Streams.java - Stream API（流式操作）
 * <p>
 * 学习要点：
 * 1. Stream 的三大步骤：创建 → 中间操作 → 终结操作
 * 2. 中间操作：filter、map、sorted、distinct、limit、skip
 * 3. 终结操作：collect、reduce、count、forEach、anyMatch、findFirst
 * 4. 收集器 Collectors
 * 5. 数值流（IntStream、LongStream、DoubleStream）
 * 6. 并行流（parallelStream）
 */
public class Streams {

    public static void main(String[] args) {

        // ============ 1. Stream 创建 ============
        System.out.println("========== 创建 Stream ==========");

        // 方式一：从集合创建
        Stream<Integer> s1 = List.of(1, 2, 3, 4, 5).stream();

        // 方式二：Stream.of
        Stream<String> s2 = Stream.of("a", "b", "c");

        // 方式三：从数组
        Stream<Integer> s3 = Arrays.stream(new Integer[]{1, 2, 3});

        // 方式四：IntStream 范围
        IntStream range = IntStream.range(1, 6);            // 1..5
        IntStream closed = IntStream.rangeClosed(1, 5);     // 1..5
        System.out.println("range: " + Arrays.toString(range.toArray()));

        // 方式五：Stream.generate（无限流，需 limit）
        Stream<Double> randoms = Stream.generate(Math::random).limit(3);
        randoms.forEach(d -> System.out.printf("%.2f ", d));
        System.out.println();

        // 方式六：Stream.iterate
        Stream.iterate(1, i -> i * 2).limit(5).forEach(i -> System.out.print(i + " "));
        System.out.println();

        // ============ 2. filter：过滤 ============
        System.out.println("\n========== filter ==========");
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evens = nums.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        System.out.println("偶数: " + evens);

        // ============ 3. map：映射转换 ============
        System.out.println("\n========== map ==========");
        List<String> words = List.of("apple", "banana", "cherry");
        List<String> upper = words.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("大写: " + upper);

        // map 改变元素类型
        List<Integer> lengths = words.stream()
            .map(String::length)
            .toList();
        System.out.println("长度: " + lengths);

        // ============ 4. flatMap：扁平化 ============
        System.out.println("\n========== flatMap ==========");
        List<List<Integer>> nested = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6, 7, 8, 9)
        );
        List<Integer> flat = nested.stream()
            .flatMap(List::stream)
            .toList();
        System.out.println("扁平化: " + flat);

        // 分词
        List<String> sentences = List.of("Hello World", "Java Streams");
        List<String> allWords = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .toList();
        System.out.println("单词: " + allWords);

        // ============ 5. sorted：排序 ============
        System.out.println("\n========== sorted ==========");
        List<Integer> unsorted = List.of(3, 1, 4, 1, 5, 9, 2, 6);
        List<Integer> sorted = unsorted.stream()
            .sorted()
            .toList();
        System.out.println("升序: " + sorted);

        // 降序
        List<Integer> desc = unsorted.stream()
            .sorted(Comparator.reverseOrder())
            .toList();
        System.out.println("降序: " + desc);

        // 自定义排序
        List<String> byLength = words.stream()
            .sorted(Comparator.comparingInt(String::length))
            .toList();
        System.out.println("按长度排序: " + byLength);

        // ============ 6. distinct / limit / skip ============
        System.out.println("\n========== distinct/limit/skip ==========");
        List<Integer> withDup = List.of(1, 2, 2, 3, 3, 3, 4);
        List<Integer> unique = withDup.stream()
            .distinct()
            .toList();
        System.out.println("去重: " + unique);

        List<Integer> first3 = nums.stream()
            .limit(3)
            .toList();
        System.out.println("前3个: " + first3);

        List<Integer> skip3 = nums.stream()
            .skip(3)
            .toList();
        System.out.println("跳过3个: " + skip3);

        // 分页：跳过前2页，取3个
        List<Integer> page = nums.stream().skip(4).limit(3).toList();
        System.out.println("分页: " + page);

        // ============ 7. peek：查看流中元素（调试用） ============
        System.out.println("\n========== peek ==========");
        List<Integer> result = nums.stream()
            .filter(n -> n > 3)
            .peek(n -> System.out.println("过滤后: " + n))
            .map(n -> n * 2)
            .peek(n -> System.out.println("映射后: " + n))
            .limit(3)
            .toList();
        System.out.println("最终: " + result);

        // ============ 8. 终结操作：收集 collect ============
        System.out.println("\n========== collect ==========");
        List<Integer> toList = nums.stream().filter(n -> n > 5).toList();
        Set<Integer> toSet = nums.stream().collect(Collectors.toSet());
        Map<Integer, Integer> toMap = nums.stream()
            .collect(Collectors.toMap(n -> n, n -> n * n));
        String joined = words.stream()
            .collect(Collectors.joining(", ", "[", "]"));

        System.out.println("List: " + toList);
        System.out.println("Set: " + toSet);
        System.out.println("Map: " + toMap);
        System.out.println("Join: " + joined);

        // 分组
        Map<Boolean, List<Integer>> partition = nums.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println("分组: " + partition);

        Map<Integer, List<String>> byLen = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("按长度分组: " + byLen);

        // 统计
        Long count = nums.stream().collect(Collectors.counting());
        IntSummaryStatistics stats = nums.stream().collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println("统计: " + stats);

        // toList (JDK 16+)
        List<Integer> newList = nums.stream().filter(n -> n > 5).toList();
        System.out.println("JDK 16+ toList: " + newList);

        // ============ 9. 终结操作：reduce ============
        System.out.println("\n========== reduce ==========");
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("总和: " + sum);

        int product = nums.stream().reduce(1, (a, b) -> a * b);
        System.out.println("乘积: " + product);

        Optional<Integer> max = nums.stream().reduce(Integer::max);
        System.out.println("最大: " + max.orElse(-1));

        // 字符串连接
        String all = words.stream().reduce("", (a, b) -> a + " " + b);
        System.out.println("连接: " + all);

        // ============ 10. 终结操作：查找和匹配 ============
        System.out.println("\n========== 查找和匹配 ==========");
        boolean allPos = nums.stream().allMatch(n -> n > 0);
        boolean anyEven = nums.stream().anyMatch(n -> n % 2 == 0);
        boolean noneNeg = nums.stream().noneMatch(n -> n < 0);
        System.out.println("全部为正: " + allPos);
        System.out.println("存在偶数: " + anyEven);
        System.out.println("无负数: " + noneNeg);

        Optional<Integer> first = nums.stream().filter(n -> n > 5).findFirst();
        Optional<Integer> any = nums.stream().filter(n -> n > 5).findAny();
        System.out.println("第一个 > 5: " + first.orElse(-1));

        long cnt = nums.stream().filter(n -> n > 5).count();
        System.out.println(">5 的个数: " + cnt);

        // ============ 11. 数值流 ============
        System.out.println("\n========== 数值流 ==========");
        int intSum = IntStream.rangeClosed(1, 100).sum();
        double avg = IntStream.rangeClosed(1, 100).average().orElse(0);
        int intMax = IntStream.of(3, 1, 4, 1, 5, 9, 2, 6).max().orElse(-1);

        System.out.println("1..100 之和: " + intSum);
        System.out.println("1..100 平均: " + avg);
        System.out.println("最大: " + intMax);

        // 装箱与拆箱
        List<Integer> boxed = IntStream.rangeClosed(1, 5).boxed().toList();   // IntStream → Stream<Integer>
        System.out.println("装箱结果: " + boxed);
        int unboxedSum = List.of(1, 2, 3).stream().mapToInt(Integer::intValue).sum();  // Stream<Integer> → IntStream
        System.out.println("拆箱求和: " + unboxedSum);

        // ============ 12. 并行流（parallelStream） ============
        System.out.println("\n========== 并行流 ==========");
        long parallelSum = IntStream.rangeClosed(1, 1_000_000)
            .parallel()
            .mapToLong(value -> value)
            .sum();
        System.out.println("并行求和: " + parallelSum);
        System.out.println("并行流使用 commonPool，不保证比顺序流快；性能应通过 JMH 和真实负载验证");

        // ============ 13. 综合案例 ============
        System.out.println("\n========== 综合案例 ==========");
        // 员工数据
        record Employee(String name, String dept, int salary) {}

        List<Employee> employees = List.of(
            new Employee("张三", "研发", 15000),
            new Employee("李四", "研发", 20000),
            new Employee("王五", "销售", 12000),
            new Employee("赵六", "销售", 18000),
            new Employee("钱七", "运营", 10000)
        );

        // 按部门分组，计算每部门平均工资
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.averagingInt(Employee::salary)
            ));
        System.out.println("部门平均工资: " + avgSalaryByDept);

        // 每部门薪资最高员工
        Map<String, Optional<Employee>> topByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.maxBy(Comparator.comparingInt(Employee::salary))
            ));
        System.out.println("部门薪资最高:");
        topByDept.forEach((dept, emp) -> System.out.println("  " + dept + ": " + emp.orElseThrow()));

        // 总薪资
        int totalSalary = employees.stream().mapToInt(Employee::salary).sum();
        System.out.println("总薪资: " + totalSalary);

        // 薪资 > 15000 的员工姓名
        List<String> highSalary = employees.stream()
            .filter(e -> e.salary() > 15000)
            .map(Employee::name)
            .sorted()
            .toList();
        System.out.println("高薪员工: " + highSalary);
    }
}

/*
 * =============== Stream 的三大特点 ===============
 *
 * 1. 不存储数据（只是"数据的视图"）
 * 2. 不修改源数据
 * 3. 惰性求值（只有终结操作才触发实际计算）
 *
 * =============== Stream 的操作分类 ===============
 *
 * 中间操作（返回 Stream，可链式调用）：
 *   filter          过滤
 *   map / mapToXxx  映射
 *   flatMap         扁平化映射
 *   sorted          排序
 *   distinct        去重
 *   limit           限制数量
 *   skip            跳过
 *   peek            查看（调试）
 *
 * 终结操作（返回非 Stream）：
 *   forEach         遍历
 *   count           计数
 *   collect         收集
 *   reduce          归约
 *   toArray         转数组
 *   toList          转 List (JDK 16+)
 *   min/max         最值
 *   sum/average     求和/平均（数值流）
 *   findFirst/findAny  查找
 *   anyMatch/allMatch/noneMatch  匹配
 *
 * =============== Collectors 常用方法 ===============
 *
 * toList()                          转 List
 * toSet()                           转 Set
 * toMap(keyF, valF)                 转 Map
 * toUnmodifiableList/Set/Map()      转不可变
 * joining([sep, prefix, suffix])    字符串连接
 * counting()                        计数
 * summingInt/Long/Double            求和
 * averagingInt/Long/Double          平均
 * summarizingInt/Long/Double        汇总统计
 * maxBy / minBy                     最值
 * groupingBy                        分组
 * partitioningBy                    二分
 * mapping / filtering / reducing    组合
 *
 * =============== 使用建议 ===============
 *
 * 1. 只有在任务可拆分、计算量足够且 commonPool 适合时才考虑并行流
 * 2. 有状态操作（sorted、distinct）在并行流中较慢
 * 3. Stream 只能被消费一次
 * 4. 方法引用和 Lambda 按可读性选择
 * 5. 使用 IntStream 处理数字，避免装箱
 * 6. JDK 16+ 用 toList() 替代 collect(toList())
 */
