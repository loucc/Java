/**
 * FlexibleConstructor.java - 灵活构造器体（JDK 25 JEP 513 正式特性）
 * <p>
 * 学习要点：
 * 1. 传统构造器的限制
 * 2. JDK 25 之前：super() / this() 必须是第一条语句
 * 3. JDK 25 允许在 super()/this() 之前有代码
 * 4. Prologue（前奏）代码的使用场景
 * 5. 提升构造器的清晰度和安全性
 */
public class FlexibleConstructor {

    public static void main(String[] args) {

        System.out.println("========== 灵活构造器体 ==========");

        // 使用新式构造器
        try {
            PositiveInt pi = new PositiveInt(42);
            System.out.println("有效: " + pi);

            PositiveInt bad = new PositiveInt(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获: " + e.getMessage());
        }

        // 参数预处理
        Rectangle r = new Rectangle(-5, 10);
        System.out.println("矩形: " + r);

        // 演示 this() 之前也可以有代码
        LoggingUser user = new LoggingUser("张三");
        System.out.println("用户: " + user);
    }
}

/*
 * =============== JDK 25 之前的限制 ===============
 *
 * super() 和 this() 必须是构造器的第一条可执行语句。
 * 目的：确保父类初始化完成后才使用继承的状态。
 *
 * 结果：无法在调用父类构造器之前做任何事，包括：
 * - 参数校验
 * - 参数转换
 * - 计算派生值
 *
 * =============== JEP 513：灵活构造器体 ===============
 *
 * JDK 25 允许在 super() 或 this() 之前编写"prologue"代码。
 *
 * 规则：
 * 1. prologue 中不能引用 this（对象还未初始化）
 * 2. 不能调用实例方法或访问实例字段
 * 3. 可以使用参数、局部变量、静态成员
 * 4. 可以进行参数校验、计算、变换
 */

// ============ 示例 1：参数校验 ============
class PositiveInt {
    private final int value;

    // 传统写法：只能在构造器主体里检查，或用工厂方法
    // JDK 25：可以在 super() 之前检查
    public PositiveInt(int value) {
        // 在 super() 之前做校验（JDK 25 允许）
        if (value <= 0) {
            throw new IllegalArgumentException("值必须为正: " + value);
        }
        super();                                        // 显式调用 Object 构造器（可省略）
        this.value = value;
    }

    @Override
    public String toString() { return "PositiveInt(" + value + ")"; }
}

// ============ 示例 2：参数变换 ============
class Rectangle {
    private final int width;
    private final int height;

    public Rectangle(int width, int height) {
        // 变换参数：宽高取绝对值，避免负值
        int w = Math.abs(width);
        int h = Math.abs(height);

        super();
        this.width = w;
        this.height = h;
    }

    @Override
    public String toString() {
        return "Rectangle(" + width + "×" + height + ")";
    }
}

// ============ 示例 3：this() 之前的代码 ============
class LoggingUser {
    private final String name;
    private final long createdAt;

    // 构造器 1：无参
    public LoggingUser() {
        // 需要基于时间生成一个默认名称
        long ts = System.currentTimeMillis();               // JDK 25 允许 this() 之前有代码
        this("Anonymous-" + ts);
    }

    // 构造器 2：有参
    public LoggingUser(String name) {
        this.name = name;
        this.createdAt = System.currentTimeMillis();
        System.out.println("[日志] 创建用户: " + name);
    }

    @Override
    public String toString() {
        return "LoggingUser(name=" + name + ", createdAt=" + createdAt + ")";
    }
}

// ============ 示例 4：子类构造器中的灵活性 ============
class Animal {
    private final String name;
    private final int legs;

    public Animal(String name, int legs) {
        this.name = name;
        this.legs = legs;
    }

    @Override
    public String toString() {
        return "Animal(" + name + ", " + legs + " legs)";
    }
}

class SafeDog extends Animal {
    private final String breed;

    // JDK 25：可以在 super() 之前校验参数
    public SafeDog(String name, String breed) {
        // 参数预处理和校验
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("名字不能为空");
        }
        if (breed == null) {
            breed = "unknown";
        }

        super(name, 4);         // 现在才调用父类
        this.breed = breed;
    }
}

/*
 * =============== 场景对比 ===============
 *
 * 【场景一：参数校验】
 *
 * 之前：
 *   class C extends P {
 *       C(int x) {
 *           super(validate(x));    // 只能通过静态方法迂回
 *       }
 *       static int validate(int x) {
 *           if (x < 0) throw new IllegalArgumentException();
 *           return x;
 *       }
 *   }
 *
 * 之后：
 *   class C extends P {
 *       C(int x) {
 *           if (x < 0) throw new IllegalArgumentException();
 *           super(x);
 *       }
 *   }
 *
 * 【场景二：变换参数】
 *
 * 之前：
 *   class SortedList<T> extends ArrayList<T> {
 *       SortedList(Collection<T> data) {
 *           super(sorted(data));   // 静态辅助方法
 *       }
 *   }
 *
 * 之后：
 *   class SortedList<T> extends ArrayList<T> {
 *       SortedList(Collection<T> data) {
 *           var sorted = new ArrayList<>(data);
 *           Collections.sort(sorted);
 *           super(sorted);
 *       }
 *   }
 *
 * 【场景三：多个构造器 this() 委托】
 *
 * 之前：无法在 this() 之前做任何计算，得改用静态方法
 * 之后：可以先计算默认值再调用 this()
 *
 * =============== prologue 中的限制 ===============
 *
 * ✅ 允许：
 * - 使用参数
 * - 使用局部变量
 * - 调用静态方法
 * - 使用静态字段
 * - throw 异常
 * - if/for/while 等控制流
 *
 * ❌ 禁止：
 * - 使用 this
 * - 调用实例方法（包括 this.foo() 和隐式 foo()）
 * - 访问实例字段
 * - 使用 super.foo（在 super() 之前）
 *
 * =============== JEP 演进 ===============
 *
 * JDK 22  JEP 447 首次预览：Statements before super(...)
 * JDK 23  JEP 482 第二次预览
 * JDK 24  JEP 492 第三次预览
 * JDK 25  JEP 513 正式：灵活构造器体
 *
 * =============== 使用建议 ===============
 *
 * 1. 用于参数校验，让异常在初始化前就抛出
 * 2. 用于参数变换（比如做防御性复制）
 * 3. 保持 prologue 简短，主要是校验和准备
 * 4. 复杂初始化逻辑仍应放在字段赋值之后
 * 5. 不要滥用：如果不需要，正常写代码即可
 */
