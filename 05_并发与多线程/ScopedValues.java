import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * ScopedValues.java - 作用域值（JDK 25 JEP 506 正式特性）
 * <p>
 * 学习要点：
 * 1. ScopedValue 的作用
 * 2. ScopedValue vs ThreadLocal
 * 3. ScopedValue.where().run() 语法
 * 4. 在结构化并发中传递值
 * 5. 不可变、有作用域的隐式参数
 */
public class ScopedValues {

    // 声明一个 ScopedValue
    // 通常声明为 static final
    static final ScopedValue<String> USER = ScopedValue.newInstance();
    static final ScopedValue<Integer> REQUEST_ID = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {

        // ============ 1. 基本使用 ============
        System.out.println("========== 基本使用 ==========");

        // 绑定值到当前作用域，运行代码块
        ScopedValue.where(USER, "张三").run(() -> {
            System.out.println("当前用户: " + USER.get());
            processRequest();
        });

        // 作用域结束后，值不再可访问
        System.out.println("USER 已绑定? " + USER.isBound());

        // 直接 get() 会抛 NoSuchElementException
        try {
            USER.get();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("作用域外访问异常: " + e.getMessage());
        }

        // ============ 2. 多个 ScopedValue 组合 ============
        System.out.println("\n========== 多个绑定 ==========");
        ScopedValue.where(USER, "李四")
            .where(REQUEST_ID, 1001)
            .run(() -> {
                System.out.println("用户: " + USER.get() + ", 请求ID: " + REQUEST_ID.get());
                logRequest();
            });

        // ============ 3. 嵌套作用域 ============
        System.out.println("\n========== 嵌套作用域 ==========");
        ScopedValue.where(USER, "外层用户").run(() -> {
            System.out.println("外层: " + USER.get());

            // 内层重新绑定
            ScopedValue.where(USER, "内层用户").run(() -> {
                System.out.println("内层: " + USER.get());
            });

            System.out.println("回到外层: " + USER.get());
        });

        // ============ 4. 带返回值 ============
        System.out.println("\n========== 带返回值 call() ==========");
        String result = ScopedValue.where(USER, "王五").call(() -> {
            return "处理结果: 用户=" + USER.get();
        });
        System.out.println(result);

        // ============ 5. orElse 提供默认值 ============
        System.out.println("\n========== orElse ==========");
        String user = USER.orElse("默认用户");
        System.out.println("未绑定时: " + user);

        // ============ 6. 在虚拟线程中使用 ============
        System.out.println("\n========== 虚拟线程 + ScopedValue ==========");
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            ScopedValue.where(USER, "调用者").run(() -> {
                // 子任务会继承 ScopedValue（结构化并发场景下）
                // 注意：普通 submit 不会自动传递，需要在提交时显式包装
                exec.submit(() -> {
                    // 在异步任务中，不能直接访问外层 ScopedValue
                    // 除非通过结构化并发或显式重新绑定
                    System.out.println("异步任务，USER 绑定? " + USER.isBound());
                });
            });
        }

        // ============ 7. 综合示例：请求追踪 ============
        System.out.println("\n========== 请求追踪场景 ==========");
        handleRequest(1001, "userA");
        handleRequest(1002, "userB");
    }

    // 在作用域内可以直接访问 USER
    static void processRequest() {
        System.out.println("[processRequest] 用户: " + USER.get());
        auditLog();
    }

    static void auditLog() {
        System.out.println("[audit] 记录 " + USER.get() + " 的操作");
    }

    static void logRequest() {
        System.out.println("[log] user=" + USER.get() + ", req=" + REQUEST_ID.get());
    }

    // 模拟请求处理
    static void handleRequest(int reqId, String userName) {
        ScopedValue.where(USER, userName)
            .where(REQUEST_ID, reqId)
            .run(() -> {
                System.out.println("---- 处理请求 " + reqId + " ----");
                validateUser();
                fetchData();
                sendResponse();
            });
    }

    static void validateUser() {
        System.out.println("[req=" + REQUEST_ID.get() + "] 校验用户: " + USER.get());
    }

    static void fetchData() {
        System.out.println("[req=" + REQUEST_ID.get() + "] 获取数据");
    }

    static void sendResponse() {
        System.out.println("[req=" + REQUEST_ID.get() + "] 返回响应");
    }
}

/*
 * =============== 什么是 ScopedValue ===============
 *
 * ScopedValue（作用域值）是 JDK 25 正式引入的特性（JEP 506），
 * 提供了一种在方法之间隐式传递不可变数据的机制。
 *
 * 核心思想：
 * - 值在一个明确的作用域内绑定
 * - 作用域内所有代码（包括嵌套调用）都可以访问
 * - 作用域结束后，值自动清除
 * - 绑定不可重新赋值；绑定对象本身仍可能是可变对象
 *
 * =============== ScopedValue vs ThreadLocal ===============
 *
 *                    ThreadLocal              ScopedValue
 * 可变性             可变（set/remove）       不可变
 * 生命周期           线程生命周期             显式的作用域
 * 生命周期管理       需要正确 remove          由词法作用域约束
 * 虚拟线程           每个线程独立状态          适合传递只读上下文
 * 继承给子线程       InheritableThreadLocal  结构化并发自动继承
 * 性能               依场景测量                依场景测量
 * 绑定语义           可 set/remove             作用域内不可重新绑定同一绑定
 *
 * =============== 基本 API ===============
 *
 * // 声明
 * static final ScopedValue<Type> KEY = ScopedValue.newInstance();
 *
 * // 绑定并运行代码块
 * ScopedValue.where(KEY, value).run(() -> {
 *     // 这里可以 KEY.get()
 * });
 *
 * // 有返回值
 * T result = ScopedValue.where(KEY, value).call(() -> {
 *     return ...;
 * });
 *
 * // 多个绑定
 * ScopedValue.where(A, "x").where(B, 1).run(...);
 *
 * // 判断是否绑定
 * KEY.isBound();
 *
 * // 提供默认值
 * KEY.orElse(defaultValue);
 *
 * =============== 使用场景 ===============
 *
 * 1. 请求上下文
 *    - 用户身份、请求 ID、租户信息、traceId
 *    - Web 框架传递 request 对象
 *
 * 2. 事务上下文
 *    - 数据库连接、事务状态
 *
 * 3. 安全上下文
 *    - 权限信息、认证令牌
 *
 * 4. 结构化并发中的父子共享
 *    - 结构化并发中，父作用域的 ScopedValue 自动被子任务继承
 *
 * =============== 使用建议 ===============
 *
 * 1. 静态 final 声明：ScopedValue.newInstance()
 * 2. 保持数据不可变（用 record 或不可变对象）
 * 3. 尽量缩小作用域
 * 4. 只读、单向上下文传递可评估替代 ThreadLocal；可变线程状态不是其目标
 * 5. 配合结构化并发使用效果最佳
 *
 * =============== 迁移建议：ThreadLocal → ScopedValue ===============
 *
 * 之前：
 *   static ThreadLocal<User> USER_TL = new ThreadLocal<>();
 *
 *   USER_TL.set(user);
 *   try { doWork(); }
 *   finally { USER_TL.remove(); }
 *
 * 之后：
 *   static ScopedValue<User> USER = ScopedValue.newInstance();
 *
 *   ScopedValue.where(USER, user).run(() -> doWork());
 */
