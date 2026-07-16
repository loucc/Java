import java.util.*;

/**
 * MyCollections.java - 集合框架
 * <p>
 * 学习要点：
 * 1. Collection 家族（List、Set、Queue）
 * 2. Map 家族
 * 3. 各集合的实现类特点和适用场景
 * 4. 迭代器 Iterator
 * 5. 遍历集合的多种方式
 * 6. 集合的常用操作
 */
public class MyCollections {

    public static void main(String[] args) {

        // ============ 1. List 列表（有序，可重复） ============
        System.out.println("========== List ==========");

        // ArrayList：数组实现，随机访问快，插入慢（尾部除外）
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("Go");
        arrayList.add("Java");                  // 允许重复
        arrayList.add(1, "Rust");               // 指定位置插入
        System.out.println("ArrayList: " + arrayList);
        System.out.println("size: " + arrayList.size());
        System.out.println("get(0): " + arrayList.get(0));
        System.out.println("indexOf(Java): " + arrayList.indexOf("Java"));
        System.out.println("contains(Go): " + arrayList.contains("Go"));

        arrayList.remove("Go");                 // 按值删除
        arrayList.remove(0);                    // 按索引删除
        System.out.println("删除后: " + arrayList);

        // LinkedList：链表实现，已定位节点处的插入删除是 O(1)，随机访问是 O(n)
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.addFirst(0);                 // 头部添加
        linkedList.addLast(3);                  // 尾部添加
        System.out.println("LinkedList: " + linkedList);
        System.out.println("first: " + linkedList.getFirst());
        System.out.println("last: " + linkedList.getLast());

        // Vector：遗留的同步动态数组；新代码通常按需求选择 ArrayList 或并发集合

        // ============ 2. Set 集合（无序，不重复） ============
        System.out.println("\n========== Set ==========");

        // HashSet：哈希表实现，不保证迭代顺序；常见操作平均 O(1)
        Set<String> hashSet = new HashSet<>();
        hashSet.add("apple");
        hashSet.add("banana");
        hashSet.add("orange");
        hashSet.add("apple");                   // 重复元素被忽略
        System.out.println("HashSet: " + hashSet);
        System.out.println("size: " + hashSet.size());
        System.out.println("contains(apple): " + hashSet.contains("apple"));

        // LinkedHashSet：保持插入顺序
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("c");
        linkedHashSet.add("a");
        linkedHashSet.add("b");
        System.out.println("LinkedHashSet: " + linkedHashSet);   // 保持插入顺序

        // TreeSet：红黑树实现，自动排序
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(3);
        treeSet.add(1);
        treeSet.add(4);
        treeSet.add(1);                         // 重复忽略
        treeSet.add(5);
        System.out.println("TreeSet: " + treeSet);                // 自动排序

        // ============ 3. Queue 队列（先进先出） ============
        System.out.println("\n========== Queue ==========");

        Queue<String> queue = new LinkedList<>();
        queue.offer("A");                       // 入队
        queue.offer("B");
        queue.offer("C");
        System.out.println("Queue: " + queue);
        System.out.println("peek: " + queue.peek());               // 查看队首但不删除
        System.out.println("poll: " + queue.poll());               // 出队
        System.out.println("剩余: " + queue);

        // Deque 双端队列
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offerFirst(1);
        deque.offerLast(2);
        deque.offerFirst(0);
        System.out.println("Deque: " + deque);                     // [0, 1, 2]

        // 优先级队列
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(3);
        pq.offer(1);
        pq.offer(4);
        pq.offer(1);
        pq.offer(5);
        System.out.print("PriorityQueue 出队: ");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");                     // 按优先级出队（小的先出）
        }
        System.out.println();

        // ============ 4. Map 键值对 ============
        System.out.println("\n========== Map ==========");

        // HashMap：最常用，无序
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("张三", 25);
        hashMap.put("李四", 30);
        hashMap.put("王五", 28);
        System.out.println("HashMap: " + hashMap);
        System.out.println("张三: " + hashMap.get("张三"));
        System.out.println("contains 张三: " + hashMap.containsKey("张三"));
        System.out.println("size: " + hashMap.size());

        // getOrDefault
        System.out.println("赵六: " + hashMap.getOrDefault("赵六", -1));

        // putIfAbsent - 不存在才添加
        hashMap.putIfAbsent("张三", 999);       // 不会覆盖
        hashMap.putIfAbsent("新人", 20);        // 会添加
        System.out.println("putIfAbsent 后: " + hashMap);

        // compute / merge
        hashMap.merge("张三", 1, Integer::sum);  // 存在则合并
        System.out.println("merge 后 张三: " + hashMap.get("张三"));

        // LinkedHashMap：保持插入顺序
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("c", 3);
        linkedHashMap.put("a", 1);
        linkedHashMap.put("b", 2);
        System.out.println("LinkedHashMap: " + linkedHashMap);

        // TreeMap：按 key 排序
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("banana", 2);
        treeMap.put("apple", 1);
        treeMap.put("cherry", 3);
        System.out.println("TreeMap: " + treeMap);

        // ============ 5. 遍历集合的方式 ============
        System.out.println("\n========== 遍历方式 ==========");
        List<String> list = List.of("a", "b", "c");

        // 方式一：普通 for
        System.out.print("普通 for: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        // 方式二：增强 for
        System.out.print("增强 for: ");
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();

        // 方式三：迭代器
        System.out.print("Iterator: ");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // 方式四：forEach + Lambda
        System.out.print("forEach: ");
        list.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 方式五：Stream（详见 Streams.java）
        System.out.print("Stream: ");
        list.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // ============ 6. Map 的遍历 ============
        System.out.println("\n========== Map 遍历 ==========");
        Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);

        // 遍历 keySet
        System.out.println("keySet:");
        for (String key : map.keySet()) {
            System.out.println("  " + key + " → " + map.get(key));
        }

        // 遍历 entrySet（推荐，避免多次 get）
        System.out.println("entrySet:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " → " + entry.getValue());
        }

        // forEach
        System.out.println("forEach:");
        map.forEach((k, v) -> System.out.println("  " + k + " → " + v));

        // ============ 7. 集合的转换 ============
        System.out.println("\n========== 集合转换 ==========");
        // Array → List
        String[] arr = {"a", "b", "c"};
        List<String> l1 = Arrays.asList(arr);           // 定长列表，不能 add/remove
        List<String> l2 = new ArrayList<>(l1);          // 可变列表
        System.out.println("数组转 List: " + l2);

        // List → Array（JDK 11+ 推荐用 IntFunction 重载，更直观）
        String[] arr2 = l2.toArray(String[]::new);
        System.out.println("List 转数组: " + Arrays.toString(arr2));

        // List → Set (去重)
        List<Integer> nums = List.of(1, 2, 3, 2, 1);
        Set<Integer> uniqueNums = new HashSet<>(nums);
        System.out.println("去重: " + uniqueNums);

        // ============ 8. 不可变集合（JDK 9+） ============
        System.out.println("\n========== 不可变集合 ==========");
        List<Integer> immutableList = List.of(1, 2, 3);
        Set<String> immutableSet = Set.of("a", "b", "c");
        Map<String, Integer> immutableMap = Map.of("a", 1, "b", 2);
        // 不可变集合不能修改
        // immutableList.add(4);                // UnsupportedOperationException

        System.out.println("不可变 List: " + immutableList);
        System.out.println("不可变 Set: " + immutableSet);
        System.out.println("不可变 Map: " + immutableMap);
    }
}

/*
 * =============== 集合框架层次 ===============
 *
 * Collection (接口)
 *   ├── List (接口)               有序、可重复
 *   │     ├── ArrayList          动态数组
 *   │     ├── LinkedList         双向链表
 *   │     └── Vector             遗留的同步动态数组
 *   ├── Set (接口)                无序、不重复
 *   │     ├── HashSet            哈希表
 *   │     ├── LinkedHashSet      哈希表 + 双向链表（保持插入顺序）
 *   │     └── TreeSet            红黑树（自动排序）
 *   └── Queue (接口)              队列
 *         ├── LinkedList         双向链表
 *         ├── PriorityQueue      优先级队列
 *         └── ArrayDeque         双端队列
 *
 * Map (接口)                     键值对
 *   ├── HashMap                  哈希表
 *   ├── LinkedHashMap            哈希表 + 双向链表
 *   ├── TreeMap                  红黑树（按 key 排序）
 *   └── Hashtable                遗留的同步 Map
 *
 * =============== 选型建议 ===============
 *
 * 需求                          推荐
 * 通用列表，随机访问多          ArrayList
 * 频繁头尾插入/删除             LinkedList / ArrayDeque
 * 去重，不保证迭代顺序           HashSet
 * 去重，保持插入顺序            LinkedHashSet
 * 去重，自动排序                TreeSet
 * 通用键值对                    HashMap
 * 保持插入顺序的键值对          LinkedHashMap
 * 自动排序的键值对              TreeMap
 * 优先级队列                    PriorityQueue
 * 并发访问                      按读写模式选择 ConcurrentHashMap 等并发集合
 *
 * =============== 时间复杂度速查 ===============
 *
 *                get/get(i)   add     remove   contains
 * ArrayList     O(1)         O(1)*   O(n)     O(n)
 * LinkedList    O(n)         O(1)    O(1)     O(n)
 * HashSet       -            O(1)*   O(1)*    O(1)*
 * TreeSet       -            O(logn) O(logn)  O(logn)
 * HashMap       O(1)*        O(1)*   O(1)*    O(1)*
 * TreeMap       O(logn)      O(logn) O(logn)  O(logn)
 *
 * * 平均，最坏 O(n) 或 O(logn)
 */
