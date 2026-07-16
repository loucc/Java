import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.stream.IntStream;

/**
 * VirtualThread.java - 虚拟线程（JDK 21+ 重大特性）
 * <p>
 * 学习要点：
 * 1. 什么是虚拟线程
 * 2. 虚拟线程 vs 平台线程
 * 3. 创建虚拟线程的多种方式
 * 4. 虚拟线程的应用场景
 * 5. Thread Builder API
 * 6. 虚拟线程的性能优势
 */
public class VirtualThread {

    public static void main(String[] args) throws InterruptedException {

        // ============ 1. 创建虚拟线程的最简方式 ============
        System.out.println("========== 创建虚拟线程 ==========");

        // 方式一：Thread.startVirtualThread(Runnable)
        Thread vt1 = Thread.startVirtualThread(() -> {
            System.out.println("虚拟线程1: " + Thread.currentThread());
        });
        vt1.join();

        // 方式二：Thread.ofVirtual()
        Thread vt2 = Thread.ofVirtual()
            .name("my-virtual-1")
            .start(() -> {
                System.out.println("虚拟线程2: " + Thread.currentThread());
            });
        vt2.join();

        // 方式三：Thread.ofVirtual().unstarted()
        Thread vt3 = Thread.ofVirtual()
            .name("my-virtual-2")
            .unstarted(() -> System.out.println("虚拟线程3: " + Thread.currentThread()));
        vt3.start();
        vt3.join();

        // ============ 2. 虚拟线程 vs 平台线程对比 ============
        System.out.println("\n========== 虚拟线程 vs 平台线程 ==========");

        Thread virtual = Thread.ofVirtual().unstarted(() -> {
            System.out.println("是虚拟线程? " + Thread.currentThread().isVirtual());
            System.out.println("守护线程? " + Thread.currentThread().isDaemon());
        });
        virtual.start();
        virtual.join();

        Thread platform = Thread.ofPlatform().unstarted(() -> {
            System.out.println("是虚拟线程? " + Thread.currentThread().isVirtual());
        });
        platform.start();
        platform.join();

        // ============ 3. 虚拟线程执行器 ============
        System.out.println("\n========== 虚拟线程执行器 ==========");

        // 推荐做法：一个任务一个虚拟线程
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(50);
                    System.out.println("任务" + i + " -> " + Thread.currentThread());
                    return null;
                });
            });
        }
        // try-with-resources 会自动 shutdown

        // 虚拟线程的优势来自阻塞型任务的高并发，不应使用 main 中的计时充当基准。
        // 性能和容量需要使用 JMH、JFR 与真实外部资源限制共同评估。

        // ============ 5. Thread Builder API ============
        System.out.println("\n========== Thread Builder ==========");

        // Thread.Builder 是 JDK 21+ 引入的
        Thread.Builder builder = Thread.ofVirtual()
            .name("worker-", 0)                             // 名字前缀 + 递增序号
            .inheritInheritableThreadLocals(false);         // 不继承 InheritableThreadLocal

        Thread t1 = builder.unstarted(() -> System.out.println(Thread.currentThread().getName()));
        Thread t2 = builder.unstarted(() -> System.out.println(Thread.currentThread().getName()));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // ============ 6. 虚拟线程的 IO 阻塞（自动 unmount） ============
        System.out.println("\n========== IO 阻塞演示 ==========");
        try (ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor()) {
            // 10 个虚拟线程各自阻塞在网络请求上
            for (int i = 0; i < 10; i++) {
                final int id = i;
                exec.submit(() -> {
                    long t = System.currentTimeMillis();
                    // 模拟 IO：sleep 会让虚拟线程 unmount，不占用平台线程
                    Thread.sleep(100);
                    System.out.println("任务" + id + " 完成 (" + (System.currentTimeMillis() - t) + "ms)");
                    return null;
                });
            }
        }

        // ============ 7. 何时不适合虚拟线程 ============
        System.out.println("\n========== 使用建议 ==========");
        System.out.println("适合虚拟线程：IO 密集 (HTTP、数据库、文件)");
        System.out.println("不适合虚拟线程：CPU 密集 (计算)");
        System.out.println("注意：native/外部函数调用仍可能 pin 载体线程");
    }
}

/*
 * =============== 什么是虚拟线程 ===============
 *
 * 虚拟线程（Virtual Thread）是 JDK 21 引入的正式特性（JEP 444）：
 * - 由 JVM 管理，不是 1:1 映射到操作系统线程
 * - 比平台线程轻量得多，可按任务创建；实际容量取决于任务状态和可用内存
 * - 面向 IO 阻塞场景优化
 *
 * =============== 虚拟线程 vs 平台线程 ===============
 *
 *                  平台线程                虚拟线程
 * 实现             OS 线程（重）           JVM 管理（轻）
 * 内存             OS 栈开销较大            按需增长的堆栈块
 * 数量             受 OS 线程资源限制        通常可支持更高并发量
 * 创建成本         高                      极低
 * 上下文切换       内核态                  用户态
 * 使用场景         CPU 密集                IO 密集
 *
 * =============== 工作原理 ===============
 *
 * 虚拟线程使用 M:N 调度：
 * - 多个虚拟线程复用少量平台线程（carrier thread）
 * - 阻塞时 unmount，让出平台线程
 * - 唤醒后重新 mount
 *
 * 相当于把"每请求一线程"的模式变便宜了。
 *
 * =============== 创建方式对比 ===============
 *
 * // 1. 一次性启动
 * Thread.startVirtualThread(runnable);
 *
 * // 2. Builder 模式
 * Thread.ofVirtual().name("worker").start(runnable);
 *
 * // 3. 未启动
 * Thread t = Thread.ofVirtual().unstarted(runnable);
 * t.start();
 *
 * // 4. 执行器（推荐）
 * try (var exec = Executors.newVirtualThreadPerTaskExecutor()) {
 *     exec.submit(task);
 * }
 *
 * =============== 何时使用 ===============
 *
 * ✅ 适合虚拟线程：
 * - HTTP 服务器每请求一线程
 * - 数据库连接每查询一线程
 * - 微服务的下游调用（大量 IO）
 * - 消息队列消费者
 *
 * ❌ 不适合：
 * - 把虚拟线程当作提升 CPU 密集计算速度的工具
 * - 使用 ThreadLocal 存大量数据（考虑 ScopedValue）
 *
 * =============== Pinning 问题 ===============
 *
 * 从 JDK 24（JEP 491）起，在 synchronized 块中阻塞不再因为监视器而 pin。
 * 某些 native 调用或外部函数调用期间，虚拟线程仍可能 pin 到载体线程。
 * 不要仅为了虚拟线程把 synchronized 机械替换成 ReentrantLock；应先通过
 * JFR 的 jdk.VirtualThreadPinned 事件定位真实问题。
 *
 * =============== 与 JDK 25 的关系 ===============
 *
 * JDK 25 中虚拟线程更加成熟，配合：
 * - 结构化并发（JEP 505）：管理多个虚拟线程的生命周期
 * - 作用域值（JEP 506）：传递有界生命周期的只读上下文
 * - 见 StructuredConcurrency.java、ScopedValues.java
 */
