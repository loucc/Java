import java.util.concurrent.*;
import java.time.Duration;
import java.time.Instant;

/**
 * StructuredConcurrency.java - 结构化并发（JDK 25 JEP 505 第五次预览）
 * <p>
 * 学习要点：
 * 1. 什么是结构化并发
 * 2. 与传统 Future/CompletableFuture 的对比
 * 3. StructuredTaskScope 的使用
 * 4. 常用的 Joiner
 * 5. 错误传播和取消
 * <p>
 * 编译运行需加参数：
 * javac --enable-preview --source 25 StructuredConcurrency.java
 * java  --enable-preview StructuredConcurrency
 * <p>
 * 注意：本文件中的代码是结构化并发的伪代码/示意，具体 API 会随预览版本演进。
 */
public class StructuredConcurrency {

    public static void main(String[] args) throws Exception {

        // ============ 传统方式的问题 ============
        System.out.println("========== 传统方式（问题）==========");
        traditionalWay();

        // ============ 结构化并发（概念展示）============
        System.out.println("\n========== 结构化并发 ==========");
        structuredWay();

        // ============ 使用示例 ============
        System.out.println("\n========== 综合案例 ==========");
        parallelFetchExample();
    }

    /**
     * 传统方式：手动管理多个 CompletableFuture
     * 问题：任务生命周期不明确，错误处理繁琐，取消困难
     */
    static void traditionalWay() throws Exception {
        Instant start = Instant.now();
        try (ExecutorService exec = Executors.newFixedThreadPool(2)) {
            Future<String> f1 = exec.submit(() -> fetchUser(1));
            Future<Integer> f2 = exec.submit(() -> fetchOrderCount(1));

            try {
                String user = f1.get();                     // 阻塞等待
                Integer count = f2.get();                   // 阻塞等待
                System.out.println("用户: " + user + ", 订单数: " + count);
            } catch (Exception e) {
                // 一个失败，另一个也可能已经在跑，需要手动取消
                f1.cancel(true);
                f2.cancel(true);
                throw e;
            }
        }
        System.out.println("耗时: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

    /**
     * 结构化并发：任务作为一个"作用域"内的整体
     * <p>
     * 注意：以下代码为 JDK 25 结构化并发的概念示例，
     * 具体 API 名称/包路径在预览期可能有变化。
     * <p>
     * 实际用法（JDK 25 语法）：
     * <pre>
     * try (var scope = StructuredTaskScope.open()) {
     *     var f1 = scope.fork(() -> fetchUser(1));
     *     var f2 = scope.fork(() -> fetchOrderCount(1));
     *     scope.join();               // 等待所有 fork 完成
     *     String user = f1.get();     // 获取结果
     *     Integer count = f2.get();
     * }
     * </pre>
     */
    static void structuredWay() {
        System.out.println("[伪代码示意]");
        System.out.println("try (var scope = StructuredTaskScope.open()) {");
        System.out.println("    var user  = scope.fork(() -> fetchUser(1));");
        System.out.println("    var order = scope.fork(() -> fetchOrderCount(1));");
        System.out.println("    scope.join();       // 等所有分支");
        System.out.println("    combine(user.get(), order.get());");
        System.out.println("}");
        System.out.println();
        System.out.println("特点：");
        System.out.println("- 所有 fork 出的子任务在 scope 内");
        System.out.println("- scope.close() 时确保所有子任务已完成或取消");
        System.out.println("- 一个失败可以自动取消其他兄弟任务");
    }

    /**
     * 使用 ExecutorService 模拟结构化并发的行为
     * （在没有预览特性时演示等价的思想）
     */
    static void parallelFetchExample() throws Exception {
        Instant start = Instant.now();

        // 使用虚拟线程执行器 + try-with-resources 是当前接近的写法
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> userFuture = exec.submit(() -> fetchUser(1));
            Future<Integer> orderFuture = exec.submit(() -> fetchOrderCount(1));
            Future<String> profileFuture = exec.submit(() -> fetchProfile(1));

            // 等所有完成
            String user = userFuture.get();
            int orders = orderFuture.get();
            String profile = profileFuture.get();

            System.out.println("用户: " + user);
            System.out.println("订单: " + orders);
            System.out.println("配置: " + profile);
        }

        System.out.println("耗时: " + Duration.between(start, Instant.now()).toMillis() + "ms");
    }

    // ==== 模拟远程调用 ====
    static String fetchUser(int id) throws InterruptedException {
        Thread.sleep(100);
        return "User#" + id;
    }

    static Integer fetchOrderCount(int id) throws InterruptedException {
        Thread.sleep(150);
        return 42;
    }

    static String fetchProfile(int id) throws InterruptedException {
        Thread.sleep(120);
        return "VIP";
    }
}

/*
 * =============== 什么是结构化并发（Structured Concurrency）===============
 *
 * 结构化编程要求代码的控制流是嵌套的、有清晰边界的。
 * 结构化并发把这个理念应用到并发：
 *
 *   任务的生命周期应该受词法作用域约束。
 *   父任务的作用域结束前，所有子任务必须结束（或被取消）。
 *
 * 就像 try-with-resources 保证资源关闭一样，
 * try (var scope = StructuredTaskScope.open()) { ... }
 * 保证所有 fork 的子任务在 scope 关闭时已处理。
 *
 * =============== 解决的问题 ===============
 *
 * 传统 Future/CompletableFuture 的问题：
 *
 * 1. 泄漏的任务
 *    Future f = exec.submit(...);   // 忘记取消，一直跑
 *
 * 2. 错误传播复杂
 *    一个失败不会自动取消兄弟任务
 *
 * 3. 取消传播丢失
 *    父任务取消，子任务不知道
 *
 * 4. 上下文丢失
 *    ThreadLocal 无法自然传递给子任务
 *
 * 5. 观察和调试困难
 *    任务关系不清晰
 *
 * =============== StructuredTaskScope（JDK 25 API 概览）===============
 *
 * 基本用法：
 *   try (var scope = StructuredTaskScope.open()) {
 *       Subtask<A> a = scope.fork(() -> taskA());
 *       Subtask<B> b = scope.fork(() -> taskB());
 *       scope.join();          // 等所有子任务
 *       // 或 scope.joinUntil(deadline)
 *       // 使用 a.get(), b.get()
 *   }
 *
 * =============== 常用 Joiner（合流策略）===============
 *
 * JDK 25 引入 StructuredTaskScope.Joiner：
 *
 *   Joiner.allSuccessfulOrThrow()   全部成功，一个失败全部取消
 *   Joiner.anySuccessfulResultOrThrow()  任一成功即返回，其他取消
 *   Joiner.awaitAll()               等待所有（不管成败）
 *   Joiner.awaitAllSuccessfulOrThrow()  等所有成功，一个失败抛异常
 *
 * 用法：
 *   try (var scope = StructuredTaskScope.open(Joiner.allSuccessfulOrThrow())) {
 *       ...
 *   }
 *
 * =============== 与虚拟线程结合 ===============
 *
 * 结构化并发默认使用虚拟线程执行子任务，
 * 结合虚拟线程可以：
 * - 廉价地并发发起大量子任务
 * - 精确控制生命周期
 * - 自然的错误和取消传播
 *
 * =============== 使用建议 ===============
 *
 * 1. 需要并发多个任务并等结果：用结构化并发
 * 2. 长时间运行的独立任务：不需要结构化
 * 3. 优雅的错误处理：优先结构化
 * 4. 微服务调用扇出/扇入：完美契合
 *
 * =============== 现状（JDK 25）===============
 *
 * JEP 505 是结构化并发的第五次预览。
 * 使用时必须启用预览特性：
 *   javac --enable-preview --source 25 XXX.java
 *   java  --enable-preview XXX
 *
 * API 仍可能在正式发布前微调。
 */
