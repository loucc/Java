import java.util.Optional;
import java.util.List;
import java.util.OptionalInt;

/**
 * MyOptional.java - Optional 类
 * <p>
 * 学习要点：
 * 1. Optional 的作用：避免 NullPointerException
 * 2. Optional 的创建（of, ofNullable, empty）
 * 3. Optional 的判断和获取
 * 4. Optional 的函数式操作（map、flatMap、filter、ifPresent）
 * 5. Optional 的正确使用姿势
 * 6. OptionalInt/OptionalLong/OptionalDouble
 */
public class MyOptional {

    public static void main(String[] args) {

        // ============ 1. Optional 创建 ============
        System.out.println("========== 创建 Optional ==========");

        // 有值：of（值不能为 null，否则 NPE）
        Optional<String> op1 = Optional.of("hello");

        // 可为空：ofNullable
        Optional<String> op2 = Optional.ofNullable("world");
        Optional<String> op3 = Optional.ofNullable(null);

        // 空：empty
        Optional<String> op4 = Optional.empty();

        System.out.println("op1: " + op1);
        System.out.println("op2: " + op2);
        System.out.println("op3: " + op3);
        System.out.println("op4: " + op4);

        // Optional.of(null) 会抛异常
        try {
            Optional<String> bad = Optional.of(null);
        } catch (NullPointerException e) {
            System.out.println("of(null) 会 NPE!");
        }

        // ============ 2. 判断 Optional ============
        System.out.println("\n========== 判断 ==========");
        Optional<String> opt = Optional.of("test");
        System.out.println("isPresent: " + opt.isPresent());
        System.out.println("isEmpty: " + opt.isEmpty());        // JDK 11+

        Optional<String> emp = Optional.empty();
        System.out.println("empty.isPresent: " + emp.isPresent());
        System.out.println("empty.isEmpty: " + emp.isEmpty());

        // ============ 3. 获取值 ============
        System.out.println("\n========== 获取值 ==========");
        // 不推荐：get()。空时抛 NoSuchElementException
        String v1 = opt.get();
        System.out.println("get: " + v1);

        // 推荐：orElse - 空时返回默认值
        String v2 = emp.orElse("默认值");
        System.out.println("orElse: " + v2);

        // 推荐：orElseGet - 空时执行 Supplier
        String v3 = emp.orElseGet(() -> {
            System.out.println("[计算默认值]");
            return "计算的默认值";
        });
        System.out.println("orElseGet: " + v3);

        // 推荐：orElseThrow - 空时抛异常
        try {
            String v4 = emp.orElseThrow(() -> new RuntimeException("值不能为空"));
        } catch (RuntimeException e) {
            System.out.println("orElseThrow: " + e.getMessage());
        }

        // JDK 10+ 无参 orElseThrow，等同 get
        // emp.orElseThrow();     // NoSuchElementException

        // ============ 4. ifPresent 消费值 ============
        System.out.println("\n========== ifPresent ==========");
        opt.ifPresent(v -> System.out.println("有值: " + v));

        emp.ifPresent(v -> System.out.println("不会执行"));

        // JDK 9+ ifPresentOrElse
        opt.ifPresentOrElse(
            v -> System.out.println("处理: " + v),
            () -> System.out.println("值为空")
        );

        // ============ 5. map / flatMap ============
        System.out.println("\n========== map / flatMap ==========");
        Optional<String> name = Optional.of("张三");
        Optional<Integer> len = name.map(String::length);       // Optional<String> → Optional<Integer>
        System.out.println("长度: " + len.orElse(0));

        // 链式操作
        String result = Optional.of("hello")
            .map(String::toUpperCase)
            .map(s -> "[" + s + "]")
            .orElse("空");
        System.out.println("链式: " + result);

        // flatMap 用于避免嵌套 Optional
        Optional<String> data = findUser(1).flatMap(OptionalUser::getEmail);
        System.out.println("邮箱: " + data.orElse("无"));

        Optional<String> nodata = findUser(999).flatMap(OptionalUser::getEmail);
        System.out.println("邮箱: " + nodata.orElse("用户不存在"));

        // ============ 6. filter 过滤 ============
        System.out.println("\n========== filter ==========");
        Optional<Integer> age = Optional.of(25);
        Optional<Integer> adult = age.filter(a -> a >= 18);
        Optional<Integer> senior = age.filter(a -> a >= 60);

        System.out.println("成年: " + adult.isPresent());
        System.out.println("老年: " + senior.isPresent());

        // ============ 7. JDK 9+ 新增方法 ============
        System.out.println("\n========== JDK 9+ 新方法 ==========");

        // stream() 转 Stream
        List<Integer> lens = List.of("a", "bb", "ccc").stream()
            .map(s -> s.length() > 1 ? Optional.of(s.length()) : Optional.<Integer>empty())
            .flatMap(Optional::stream)          // 只保留有值的
            .toList();
        System.out.println("过滤后长度: " + lens);

        // or - 空时提供另一个 Optional（避免裸 get()，用 orElse 解包）
        Optional<String> a = Optional.<String>empty()
            .or(() -> Optional.of("备用值"));
        System.out.println("or: " + a.orElse("(无)"));

        // ============ 8. 反例：常见误用 ============
        System.out.println("\n========== 反例 ==========");

        // 反例 1：直接 get()（可能 NPE）
        // Optional.empty().get();

        // 反例 2：用 Optional 做参数
        // void bad(Optional<String> s) { ... }  ❌
        // 应该用重载或默认值
        // void good(String s) { ... }

        // 反例 3：把 Optional 存到字段中
        // class User { Optional<String> email; }  ❌
        // 应该允许字段为 null，getter 返回 Optional
        // class User { String email; Optional<String> getEmail() { return Optional.ofNullable(email); } }

        // 反例 4：Optional 集合的元素
        // List<Optional<String>>  ❌ 应该过滤掉空的
        // 用 Stream 更好

        // ============ 9. OptionalInt/OptionalLong/OptionalDouble ============
        System.out.println("\n========== OptionalInt ==========");
        OptionalInt maxOpt = List.of(3, 1, 4, 1, 5).stream()
            .mapToInt(Integer::intValue)
            .max();
        System.out.println("最大: " + maxOpt.orElse(-1));

        // ============ 10. 综合示例：链式安全导航 ============
        System.out.println("\n========== 综合示例 ==========");
        // 传统写法（充满 null 检查）：
        // if (user != null) {
        //     Address addr = user.getAddress();
        //     if (addr != null) {
        //         String city = addr.getCity();
        //         if (city != null) {
        //             System.out.println(city.toUpperCase());
        //         }
        //     }
        // }

        // Optional 写法：
        OptionalUser u = new OptionalUser("李四", "li@example.com");
        String city = Optional.of(u)
            .flatMap(OptionalUser::getEmail)
            .map(String::toUpperCase)
            .orElse("无邮箱");
        System.out.println("邮箱大写: " + city);
    }

    // 模拟数据源
    static Optional<OptionalUser> findUser(int id) {
        if (id == 1) return Optional.of(new OptionalUser("张三", "zhang@example.com"));
        if (id == 2) return Optional.of(new OptionalUser("李四", null));
        return Optional.empty();
    }
}

class OptionalUser {
    private String name;
    private String email;

    public OptionalUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }

    // getter 返回 Optional 是常见模式
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}

/*
 * =============== Optional 的目的 ===============
 *
 * 表达一个值"可能不存在"，替代返回 null
 * 强制调用者思考"如何处理空值"
 *
 * =============== 常用方法一览 ===============
 *
 * 创建：
 *   of(T)                值不能为 null
 *   ofNullable(T)        值可以为 null
 *   empty()              空
 *
 * 判断：
 *   isPresent()          有值？
 *   isEmpty()            无值？(JDK 11+)
 *
 * 获取：
 *   get()                取值（空时抛异常，不推荐）
 *   orElse(T)            空则返回默认值
 *   orElseGet(Supplier)  空则调用 Supplier
 *   orElseThrow()        空则抛异常 (JDK 10+)
 *   orElseThrow(Supplier) 空则抛自定义异常
 *
 * 消费：
 *   ifPresent(Consumer)  有值则消费
 *   ifPresentOrElse(Consumer, Runnable)  分支 (JDK 9+)
 *
 * 转换：
 *   map(Function)        转换为其他 Optional
 *   flatMap(Function)    展平 Optional<Optional<T>>
 *   filter(Predicate)    过滤
 *
 * 组合：
 *   or(Supplier)         空则提供另一个 Optional (JDK 9+)
 *   stream()             转 Stream (JDK 9+)
 *
 * =============== 使用原则 ===============
 *
 * DO：
 * - 用作方法返回值，表达"可能没有结果"
 * - 用 map/filter/flatMap 链式处理
 * - 用 orElse/orElseGet 提供默认值
 *
 * DON'T：
 * - 用作方法参数
 * - 用作类的字段
 * - 用作集合元素
 * - 直接 get() 不判断
 * - 判断 isPresent() 后 get()（应该用 orElse 或 ifPresent）
 *
 * =============== orElse vs orElseGet ===============
 *
 * orElse(x)：       无论有无值都会创建 x
 * orElseGet(() -> x)：只在空时才调用 Supplier
 *
 * 有副作用或创建开销时用 orElseGet
 */
