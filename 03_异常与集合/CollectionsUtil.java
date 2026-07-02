import java.util.*;

/**
 * CollectionsUtil.java - Collections 工具类和 Comparable/Comparator
 * <p>
 * 学习要点：
 * 1. Collections 工具类的常用方法
 * 2. 排序：Comparable 和 Comparator
 * 3. 查找、复制、反转、打乱
 * 4. 集合的同步包装
 * 5. 不可变视图 vs 不可变集合
 */
public class CollectionsUtil {

    public static void main(String[] args) {

        // ============ 1. 排序 ============
        System.out.println("========== 排序 ==========");
        List<Integer> nums = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9, 2, 6));

        Collections.sort(nums);                     // 自然顺序升序
        System.out.println("升序: " + nums);

        Collections.sort(nums, Comparator.reverseOrder());  // 降序
        System.out.println("降序: " + nums);

        // 直接使用 List.sort（推荐）
        nums.sort(Comparator.naturalOrder());
        System.out.println("naturalOrder: " + nums);

        // ============ 2. Comparable 自然排序 ============
        System.out.println("\n========== Comparable ==========");
        List<Student> students = new ArrayList<>(List.of(
            new Student("张三", 85),
            new Student("李四", 92),
            new Student("王五", 78)
        ));

        Collections.sort(students);                 // 使用 Student 的自然顺序（按成绩升序）
        System.out.println("按成绩排序:");
        students.forEach(System.out::println);

        // ============ 3. Comparator 自定义排序 ============
        System.out.println("\n========== Comparator ==========");
        // 按名字排序
        Collections.sort(students, Comparator.comparing(Student::getName));
        System.out.println("按名字排序:");
        students.forEach(System.out::println);

        // 按成绩降序
        Collections.sort(students, Comparator.comparingInt(Student::getScore).reversed());
        System.out.println("按成绩降序:");
        students.forEach(System.out::println);

        // 组合排序：先按成绩降序，再按名字升序
        Comparator<Student> comparator = Comparator
            .comparingInt(Student::getScore).reversed()
            .thenComparing(Student::getName);
        Collections.sort(students, comparator);
        System.out.println("组合排序:");
        students.forEach(System.out::println);

        // ============ 4. 查找 ============
        System.out.println("\n========== 查找 ==========");
        List<Integer> sortedNums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int idx = Collections.binarySearch(sortedNums, 5);
        System.out.println("查找 5: 索引 " + idx);

        Integer max = Collections.max(sortedNums);
        Integer min = Collections.min(sortedNums);
        System.out.println("最大: " + max + ", 最小: " + min);

        int count = Collections.frequency(List.of(1, 2, 3, 2, 1, 2), 2);
        System.out.println("2 出现次数: " + count);

        // ============ 5. 操作集合 ============
        System.out.println("\n========== 操作集合 ==========");
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));

        // 反转
        Collections.reverse(list);
        System.out.println("反转: " + list);

        // 打乱
        Collections.shuffle(list);
        System.out.println("打乱: " + list);

        // 交换
        Collections.swap(list, 0, list.size() - 1);
        System.out.println("交换首尾: " + list);

        // 填充
        List<Integer> fillList = new ArrayList<>(list);
        Collections.fill(fillList, 0);
        System.out.println("填充 0: " + fillList);

        // 复制
        List<Integer> dst = new ArrayList<>(Arrays.asList(new Integer[list.size()]));
        Collections.copy(dst, list);
        System.out.println("复制: " + dst);

        // ============ 6. 不可变集合 ============
        System.out.println("\n========== 不可变集合 ==========");
        // 不可变视图（源集合修改，视图会看到）
        List<Integer> original = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> unmodifiableView = Collections.unmodifiableList(original);

        try {
            unmodifiableView.add(4);            // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("不可修改视图");
        }

        // 修改源集合会影响视图
        original.add(4);
        System.out.println("修改原集合后视图: " + unmodifiableView);

        // JDK 9+ 真正的不可变集合（快照）
        List<Integer> trulyImmutable = List.copyOf(original);
        original.clear();
        System.out.println("List.copyOf 是快照: " + trulyImmutable);

        // ============ 7. 线程安全包装 ============
        System.out.println("\n========== 线程安全包装 ==========");
        List<Integer> syncList = Collections.synchronizedList(new ArrayList<>());
        syncList.add(1);
        syncList.add(2);
        System.out.println("同步 List: " + syncList);
        // 遍历时仍需手动加锁
        synchronized (syncList) {
            for (Integer i : syncList) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        // 实际使用建议：ConcurrentHashMap、CopyOnWriteArrayList（详见并发章节）

        // ============ 8. 单例集合 ============
        System.out.println("\n========== 单例集合 ==========");
        List<String> single = Collections.singletonList("只有一个");
        Set<String> singleSet = Collections.singleton("only");
        Map<String, Integer> singleMap = Collections.singletonMap("key", 100);
        System.out.println("单元素 List: " + single);
        System.out.println("单元素 Set: " + singleSet);
        System.out.println("单元素 Map: " + singleMap);

        // ============ 9. 空集合 ============
        System.out.println("\n========== 空集合 ==========");
        // 返回空集合，比 null 好
        List<Integer> empty1 = Collections.emptyList();
        List<Integer> empty2 = List.of();               // JDK 9+ 推荐
        System.out.println("空: " + empty1 + " / " + empty2);
    }
}

// ============ 实现 Comparable（自然排序） ============
class Student implements Comparable<Student> {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    // 定义自然顺序：按成绩升序
    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.score, other.score);
        // 或：return this.score - other.score;（可能溢出）
    }

    @Override
    public String toString() {
        return name + "(" + score + ")";
    }
}

/*
 * =============== Comparable vs Comparator ===============
 *
 *              Comparable                Comparator
 * 定义位置     类内部                    独立的类/Lambda
 * 方法         compareTo                 compare
 * 用途         定义自然顺序               定义外部/临时顺序
 * 数量         一个类只能一个            可以有多个不同的
 *
 * =============== 返回值约定 ===============
 *
 * compareTo/compare 的返回值：
 *   负数：this < other
 *   零：  this == other
 *   正数：this > other
 *
 * 推荐用 Integer.compare / Long.compare 避免溢出
 *
 * =============== Comparator 常用工厂方法 ===============
 *
 * naturalOrder()              自然顺序
 * reverseOrder()              逆自然顺序
 * comparing(keyExtractor)     按 key 比较
 * comparingInt / Long / Double 基本类型专用
 * thenComparing               次要比较
 * reversed()                  反转
 * nullsFirst / nullsLast      null 处理
 *
 * =============== Collections vs Arrays 工具类 ===============
 *
 * Collections：操作集合
 * Arrays：操作数组
 * 命名相似但独立
 */
