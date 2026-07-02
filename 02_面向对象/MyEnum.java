/**
 * MyEnum.java - 枚举
 * <p>
 * 学习要点：
 * 1. enum 关键字
 * 2. 枚举的常用方法（values, valueOf, name, ordinal）
 * 3. 枚举的构造器、字段、方法
 * 4. 枚举实现接口
 * 5. 抽象方法枚举
 * 6. 枚举 + switch
 * 7. EnumSet / EnumMap
 */
public class MyEnum {

    public static void main(String[] args) {

        // ============ 1. 基本使用 ============
        System.out.println("========== 基本枚举 ==========");
        Season season = Season.SPRING;
        System.out.println("当前季节: " + season);
        System.out.println("名字: " + season.name());       // SPRING
        System.out.println("序号: " + season.ordinal());    // 0

        // 遍历所有枚举
        for (Season s : Season.values()) {
            System.out.println(s.ordinal() + " → " + s);
        }

        // 字符串转枚举
        Season s = Season.valueOf("SUMMER");
        System.out.println("valueOf: " + s);

        // ============ 2. switch + enum ============
        System.out.println("\n========== switch + enum ==========");
        printMood(Season.WINTER);
        printMood(Season.SUMMER);

        // JDK 21+ 的模式匹配 switch
        System.out.println(seasonEmoji(Season.AUTUMN));

        // ============ 3. 带字段和方法的枚举 ============
        System.out.println("\n========== 带字段的枚举 ==========");
        for (Planet p : Planet.values()) {
            System.out.printf("%-8s 质量: %.2e kg, 半径: %.0f m%n",
                p.name(), p.getMass(), p.getRadius());
        }

        // 计算地球表面重力
        double earthGravity = Planet.EARTH.surfaceGravity();
        System.out.printf("地球表面重力: %.3f m/s²%n", earthGravity);

        // ============ 4. 抽象方法枚举 ============
        System.out.println("\n========== 抽象方法枚举 ==========");
        int a = 10, b = 3;
        for (Operation op : Operation.values()) {
            System.out.printf("%d %s %d = %d%n", a, op.getSymbol(), b, op.apply(a, b));
        }

        // ============ 5. 枚举实现接口 ============
        System.out.println("\n========== 实现接口 ==========");
        for (TrafficLight light : TrafficLight.values()) {
            light.action();
        }

        // ============ 6. EnumSet 和 EnumMap（高效） ============
        System.out.println("\n========== EnumSet ==========");
        var weekends = java.util.EnumSet.of(Day.SATURDAY, Day.SUNDAY);
        var weekdays = java.util.EnumSet.complementOf(weekends);
        System.out.println("周末: " + weekends);
        System.out.println("工作日: " + weekdays);

        System.out.println("\n========== EnumMap ==========");
        var schedule = new java.util.EnumMap<Day, String>(Day.class);
        schedule.put(Day.MONDAY, "开会");
        schedule.put(Day.FRIDAY, "总结");
        System.out.println("日程: " + schedule);
    }

    // switch 处理枚举时不需要写枚举类型前缀
    static void printMood(Season s) {
        String mood = switch (s) {
            case SPRING -> "生机勃勃";
            case SUMMER -> "热情似火";
            case AUTUMN -> "沉静收获";
            case WINTER -> "宁静沉思";
        };
        System.out.println(s + ": " + mood);
    }

    static String seasonEmoji(Season s) {
        return switch (s) {
            case SPRING -> s + " 🌸";
            case SUMMER -> s + " ☀️";
            case AUTUMN -> s + " 🍂";
            case WINTER -> s + " ❄️";
        };
    }
}

// ============ 1. 简单枚举 ============
enum Season {
    SPRING, SUMMER, AUTUMN, WINTER
}

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

// ============ 2. 带字段、构造器、方法的枚举 ============
enum Planet {
    // 每个枚举常量后面用括号传参给构造器
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6);

    // 万有引力常数
    private static final double G = 6.67300E-11;

    // 字段
    private final double mass;      // 质量 kg
    private final double radius;    // 半径 m

    // 构造器隐含 private（枚举构造器不能是 public/protected）
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    // 常规方法
    public double getMass() { return mass; }
    public double getRadius() { return radius; }

    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }
}

// ============ 3. 抽象方法枚举（每个枚举常量实现自己的行为） ============
enum Operation {
    PLUS("+") {
        @Override
        public int apply(int a, int b) { return a + b; }
    },
    MINUS("-") {
        @Override
        public int apply(int a, int b) { return a - b; }
    },
    TIMES("*") {
        @Override
        public int apply(int a, int b) { return a * b; }
    },
    DIVIDE("/") {
        @Override
        public int apply(int a, int b) { return a / b; }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() { return symbol; }

    public abstract int apply(int a, int b);
}

// ============ 4. 枚举实现接口 ============
interface Actionable {
    void action();
}

enum TrafficLight implements Actionable {
    RED {
        @Override
        public void action() {
            System.out.println("🔴 红灯停");
        }
    },
    YELLOW {
        @Override
        public void action() {
            System.out.println("🟡 黄灯准备");
        }
    },
    GREEN {
        @Override
        public void action() {
            System.out.println("🟢 绿灯行");
        }
    }
}

/*
 * =============== 枚举的特点 ===============
 *
 * 1. enum 关键字定义
 * 2. 枚举本质是 java.lang.Enum 的子类
 * 3. 每个枚举常量都是一个单例
 * 4. 隐含 final（不能被继承）
 * 5. 构造器隐含 private
 * 6. 编译器自动生成 values() 和 valueOf(String)
 * 7. 可以有字段、构造器、方法
 * 8. 可以实现接口
 * 9. 不能继承其他类（已经继承 Enum）
 *
 * =============== Enum 类的常用方法 ===============
 *
 * name()           - 返回常量名（如 "SPRING"）
 * ordinal()        - 返回常量在声明中的序号（从 0 开始）
 * toString()       - 默认返回 name()，可重写
 * compareTo(E)     - 按 ordinal 比较
 * getDeclaringClass() - 返回枚举类
 *
 * =============== 编译器生成的方法 ===============
 *
 * values()         - 返回所有枚举常量的数组
 * valueOf(String)  - 根据名字返回对应枚举
 *
 * =============== 枚举的用途 ===============
 *
 * 1. 表示固定的一组常量（性别、颜色、状态等）
 * 2. 单例模式（Effective Java 推荐）
 * 3. 状态机
 * 4. 策略模式（配合抽象方法）
 * 5. switch 表达式（编译器可检查穷尽性）
 *
 * =============== EnumSet / EnumMap ===============
 *
 * EnumSet：专为枚举优化的 Set，用位运算实现，极快
 * EnumMap：以枚举为 key 的 Map，用数组实现，快
 */
