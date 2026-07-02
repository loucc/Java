import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * MyAnnotation.java - 注解（Annotation）
 * <p>
 * 学习要点：
 * 1. 什么是注解、作用
 * 2. 内置注解（@Override、@Deprecated、@SuppressWarnings、@FunctionalInterface）
 * 3. 元注解（@Target、@Retention、@Documented、@Inherited、@Repeatable）
 * 4. 自定义注解
 * 5. 注解属性
 * 6. 通过反射读取注解
 */
public class MyAnnotation {

    public static void main(String[] args) throws Exception {

        // ============ 1. 内置注解 ============
        System.out.println("========== 内置注解 ==========");

        // @Override: 检查方法确实重写了父类方法
        // @Deprecated: 标记为过时
        // @SuppressWarnings: 抑制警告
        // @FunctionalInterface: 检查是函数式接口
        // @SafeVarargs: 安全的可变参数
        Deprecated d = MyAnnotation.class.getMethod("oldMethod")
            .getAnnotation(Deprecated.class);
        if (d != null) {
            System.out.println("方法过时，since = " + d.since());
            System.out.println("将来会移除吗？ " + d.forRemoval());
        }

        // ============ 2. 自定义注解的使用 ============
        System.out.println("\n========== 自定义注解 ==========");

        // 通过反射读取注解
        Class<UserService> clazz = UserService.class;

        // 类上的注解
        Author author = clazz.getAnnotation(Author.class);
        System.out.println("类作者: " + author.name());
        System.out.println("类版本: " + author.version());
        System.out.println("类描述: " + author.description());

        // 方法上的注解
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                Test test = m.getAnnotation(Test.class);
                System.out.println("测试方法: " + m.getName()
                    + ", enabled=" + test.enabled()
                    + ", timeout=" + test.timeout());
            }
        }

        // ============ 3. 可重复注解 ============
        System.out.println("\n========== 可重复注解 ==========");
        Role[] roles = UserService.class.getAnnotationsByType(Role.class);
        for (Role r : roles) {
            System.out.println("角色: " + r.value());
        }

        // ============ 4. 简易测试框架示例 ============
        System.out.println("\n========== 简易测试运行器 ==========");
        runTests(UserService.class);
    }

    @Deprecated(since = "1.5", forRemoval = true)
    public void oldMethod() {
        // 过时的方法
    }

    // 简易测试运行器
    static void runTests(Class<?> testClass) throws Exception {
        Object instance = testClass.getDeclaredConstructor().newInstance();
        int total = 0, passed = 0;

        for (Method m : testClass.getDeclaredMethods()) {
            Test t = m.getAnnotation(Test.class);
            if (t == null || !t.enabled()) continue;

            total++;
            try {
                m.invoke(instance);
                passed++;
                System.out.println("✅ " + m.getName());
            } catch (Exception e) {
                System.out.println("❌ " + m.getName() + " → " + e.getCause().getMessage());
            }
        }
        System.out.println("\n测试结果: " + passed + "/" + total + " 通过");
    }
}

// ============ 定义自定义注解 ============

// 元注解：告诉编译器如何处理这个注解
@Target(ElementType.TYPE)                          // 只能用于类、接口
@Retention(RetentionPolicy.RUNTIME)                // 运行时可见（反射）
@Documented                                        // 出现在 javadoc 中
@interface Author {
    String name();                                 // 必填属性
    String version() default "1.0";                // 有默认值
    String description() default "";
}

// 用于方法的注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Test {
    boolean enabled() default true;
    long timeout() default 1000;
}

// 可重复注解（JDK 8+）
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Roles.class)                           // 指定容器注解
@interface Role {
    String value();
}

// 容器注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Roles {
    Role[] value();
}

// ============ 应用注解 ============
@Author(name = "张三", version = "2.0", description = "用户服务")
@Role("admin")
@Role("developer")
@Role("tester")
class UserService {

    @Test(enabled = true, timeout = 500)
    public void testCreateUser() {
        System.out.println("  运行: 创建用户");
        // 正常通过
    }

    @Test(enabled = true, timeout = 200)
    public void testDeleteUser() {
        System.out.println("  运行: 删除用户");
        throw new RuntimeException("模拟失败");
    }

    @Test(enabled = false)
    public void testSkipped() {
        // 被禁用，不会运行
    }

    public void notATest() {
        // 没有 @Test，忽略
    }
}

/*
 * =============== 什么是注解 ===============
 *
 * 注解（Annotation）是 JDK 5 引入的元数据形式：
 * - 不直接影响程序语义
 * - 但可以被编译器、工具、框架读取
 * - 用于标记、生成代码、配置行为
 *
 * =============== 语法 ===============
 *
 * @注解名                        // 无属性
 * @注解名("value")               // 单属性用 value
 * @注解名(name = "x", age = 10)  // 多属性
 *
 * =============== 内置注解 ===============
 *
 * @Override             检查方法重写
 * @Deprecated           标记为过时
 * @SuppressWarnings     抑制警告
 * @FunctionalInterface  函数式接口检查
 * @SafeVarargs          安全的可变参数
 *
 * =============== 元注解（用于注解的注解）===============
 *
 * @Target       指定注解可用位置：
 *   ElementType.TYPE            类/接口
 *   ElementType.FIELD           字段
 *   ElementType.METHOD          方法
 *   ElementType.PARAMETER       参数
 *   ElementType.CONSTRUCTOR     构造器
 *   ElementType.LOCAL_VARIABLE  局部变量
 *   ElementType.ANNOTATION_TYPE 注解
 *   ElementType.PACKAGE         包
 *   ElementType.TYPE_PARAMETER  类型参数
 *   ElementType.TYPE_USE        类型使用
 *
 * @Retention   保留策略：
 *   RetentionPolicy.SOURCE      源码级别（编译后丢弃）
 *   RetentionPolicy.CLASS       字节码保留但运行时不可见（默认）
 *   RetentionPolicy.RUNTIME     运行时可见（反射能读）
 *
 * @Documented  出现在 javadoc 中
 * @Inherited   子类继承父类的注解
 * @Repeatable  可以重复使用（JDK 8+）
 *
 * =============== 注解属性的定义 ===============
 *
 * @interface MyAnn {
 *     String value();                      // 必填
 *     int count() default 1;               // 有默认值
 *     String[] tags() default {};          // 数组
 *     Class<?> type() default Object.class; // Class 类型
 *     ElementType target() default ElementType.TYPE; // 枚举
 * }
 *
 * 属性类型只能是：
 * - 基本类型
 * - String
 * - Class
 * - 枚举
 * - 注解
 * - 以上类型的数组
 *
 * =============== 常见应用 ===============
 *
 * 1. 测试框架：JUnit 的 @Test、@BeforeEach
 * 2. Spring：@Component、@Autowired、@Transactional
 * 3. JPA：@Entity、@Column、@ManyToOne
 * 4. Web：@RestController、@RequestMapping
 * 5. 验证：@NotNull、@Size、@Email（Jakarta Validation）
 * 6. 序列化：@JsonProperty（Jackson）
 * 7. 自定义 DSL 和框架
 *
 * =============== 使用建议 ===============
 *
 * 1. 熟悉常用框架的注解
 * 2. 自定义注解 + 反射 = 简单的框架
 * 3. 注解处理器（annotation processor）可在编译期处理
 * 4. 注解只描述"是什么"，具体行为需要处理器/框架来实现
 */
