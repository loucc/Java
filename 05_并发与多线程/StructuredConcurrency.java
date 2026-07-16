import java.util.concurrent.*;
import java.time.Duration;
import java.time.Instant;

/**
 * StructuredConcurrency.java - 结构化并发概念与稳定 API 对照
 * <p>
 * 学习要点：
 * 1. 什么是结构化并发
 * 2. 与传统 Future/CompletableFuture 的对比
 * 3. 使用稳定 API 手动管理任务生命周期
 * 4. 理解稳定 API 与 StructuredTaskScope 的语义差异
 * <p>
 * 注意：JDK 25 的 StructuredTaskScope（JEP 505）仍是预览 API，本示例
 * 不使用预览特性。ExecutorService 示例只是对照，不具备 StructuredTaskScope
 * 的父子任务树、自动失败传播和 Joiner 合流语义。
 */
public class StructuredConcurrency {

    public static void main(String[] args) throws Exception {

        // ============ 传统方式的问题 ============
        System.out.println("========== 传统方式（问题）==========");
        traditionalWay();

        // ============ 稳定 API：显式管理生命周期 ============
        System.out.println("\n========== 手动管理并发任务 ==========");
        parallelFetchWithManualCancellation();
    }

    /**
     * 传统方式：手动管理多个 CompletableFuture
     * 问题：任务生命周期不明确，错误处理繁琐，取消困难
     */
    static void traditionalWay() throws Exception {
        Instant start = Instant.now();
        // IO 密集任务（fetchUser/fetchOrderCount 均为 sleep 模拟 IO），JDK 21+ 推荐虚拟线程执行器
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
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
     * 使用稳定的 ExecutorService 显式管理任务
     * <p>
     * JDK 25 的 StructuredTaskScope（JEP 505）仍是预览 API，这里用稳定的
     * 这段代码需要调用方自行规定失败、取消和结果收集策略：
     * <pre>
     * try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
     *     Future&lt;String&gt; user = exec.submit(() -> fetchUser(1));
     *     Future&lt;Integer&gt; order = exec.submit(() -> fetchOrderCount(1));
     *     // Future.get() 和失败后的取消都由调用方处理
     *     System.out.println(user.get() + ", " + order.get());
     * }
     * </pre>
     */
    static void parallelFetchWithManualCancellation() throws Exception {
        Instant start = Instant.now();

        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> userFuture = exec.submit(() -> fetchUser(1));
            Future<Integer> orderFuture = exec.submit(() -> fetchOrderCount(1));
            Future<String> profileFuture = exec.submit(() -> fetchProfile(1));

            try {
                String user = userFuture.get();
                int orders = orderFuture.get();
                String profile = profileFuture.get();

                System.out.println("用户: " + user);
                System.out.println("订单: " + orders);
                System.out.println("配置: " + profile);
            } catch (Exception e) {
                userFuture.cancel(true);
                orderFuture.cancel(true);
                profileFuture.cancel(true);
                throw e;
            }
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
 * ExecutorService 的 try-with-resources 会关闭执行器并等待任务终止，但这不等于
 * StructuredTaskScope 的词法父子关系、失败传播和可观察任务树。
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
 * =============== 当前稳定 API 对照（本文件采用）===============
 *
 * 用 ExecutorService + try-with-resources + 虚拟线程执行器手动管理任务：
 *
 *   try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
 *       Future<String> user  = exec.submit(() -> fetchUser(1));
 *       Future<Integer> order = exec.submit(() -> fetchOrderCount(1));
 *       // 调用方仍需处理失败、取消、超时和结果收集
 *       System.out.println(user.get() + ", " + order.get());
 *   }
 *
 * try-with-resources 管理执行器关闭，但不会自动实现结构化并发策略。
 *
 * =============== 与虚拟线程结合 ===============
 *
 * 虚拟线程执行器为每个任务分配一个虚拟线程，可以：
 * - 廉价地并发发起大量子任务
 * - 让执行器生命周期在代码中清晰可见
 *
 * =============== 使用建议 ===============
 *
 * 1. 只使用稳定 API 时，显式定义失败、取消和超时策略
 * 2. 长时间运行的独立任务：不需要收敛
 * 3. 优雅的错误处理：一个失败时手动 cancel 兄弟任务
 * 4. 扇出/扇入场景必须设置下游超时并限制外部资源并发
 *
 * =============== 现状（JDK 25）===============
 *
 * 完整的 StructuredTaskScope API（JEP 505）在 JDK 25 仍是第五次预览，
 * 需 --enable-preview 才能使用，API 仍可能在正式发布前微调。
 * 本示例不依赖预览特性，待其正式发布后再行切换。
 */
