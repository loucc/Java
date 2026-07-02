import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

/**
 * ThreadPool.java - 线程池和 Executor 框架
 * <p>
 * 学习要点：
 * 1. 为什么需要线程池
 * 2. Executor 框架层次
 * 3. ThreadPoolExecutor 的 7 个核心参数
 * 4. 4 种预定义线程池
 * 5. Future 获取异步结果
 * 6. ExecutorService 的关闭方式
 * 7. JDK 21+ 虚拟线程执行器
 */
public class ThreadPool {

    public static void main(String[] args) throws Exception {

        // ============ 1. Executors 创建预定义线程池 ============
        System.out.println("========== 4 种线程池 ==========");

        // 固定大小
        ExecutorService fixed = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            fixed.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + taskId);
                try { Thread.sleep(50); } catch (InterruptedException e) {}
            });
        }
        fixed.shutdown();
        fixed.awaitTermination(5, TimeUnit.SECONDS);

        // 缓存线程池：按需创建，60 秒空闲回收
        ExecutorService cached = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            cached.submit(() -> System.out.println(Thread.currentThread().getName()));
        }
        cached.shutdown();

        // 单线程池：所有任务顺序执行
        ExecutorService single = Executors.newSingleThreadExecutor();
        single.submit(() -> System.out.println("Single 1"));
        single.submit(() -> System.out.println("Single 2"));
        single.shutdown();

        // 定时/周期任务
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);
        scheduled.schedule(() -> System.out.println("100ms 后执行"), 100, TimeUnit.MILLISECONDS);
        scheduled.scheduleAtFixedRate(
            () -> System.out.println("[定时] " + System.currentTimeMillis()),
            0, 100, TimeUnit.MILLISECONDS);
        Thread.sleep(500);
        scheduled.shutdown();

        // ============ 2. 手动创建 ThreadPoolExecutor（推荐） ============
        System.out.println("\n========== 手动创建线程池 ==========");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,                                      // corePoolSize：核心线程数
            4,                                      // maximumPoolSize：最大线程数
            60L, TimeUnit.SECONDS,                  // keepAliveTime：非核心线程空闲时间
            new LinkedBlockingQueue<>(10),          // workQueue：任务队列
            new ThreadFactory() {                   // threadFactory：线程工厂
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "my-pool-" + (++count));
                }
            },
            new ThreadPoolExecutor.AbortPolicy()    // handler：拒绝策略
        );

        for (int i = 0; i < 5; i++) {
            final int id = i;
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + id);
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        // ============ 3. Future 获取任务结果 ============
        System.out.println("\n========== Future ==========");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // submit 一个 Callable 返回 Future
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(200);
            return 42;
        });

        System.out.println("异步计算中...");
        // future.get() 会阻塞直到完成
        Integer result = future.get();
        System.out.println("结果: " + result);

        // 带超时
        Future<Integer> future2 = executor.submit(() -> {
            Thread.sleep(500);
            return 100;
        });
        try {
            future2.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("超时了");
            future2.cancel(true);
        }

        // ============ 4. invokeAll / invokeAny ============
        System.out.println("\n========== invokeAll ==========");
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int n = i;
            tasks.add(() -> {
                Thread.sleep(50 * n);
                return n * n;
            });
        }

        // invokeAll: 等所有任务完成
        List<Future<Integer>> futures = executor.invokeAll(tasks);
        for (Future<Integer> f : futures) {
            System.out.print(f.get() + " ");
        }
        System.out.println();

        // invokeAny: 任意一个完成就返回（其他被取消）
        Integer any = executor.invokeAny(tasks);
        System.out.println("任意一个: " + any);

        executor.shutdown();

        // ============ 5. JDK 21+ 虚拟线程执行器 ============
        System.out.println("\n========== 虚拟线程执行器 ==========");
        // 每个任务都在新的虚拟线程中执行
        try (ExecutorService vExec = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                final int id = i;
                vExec.submit(() -> {
                    System.out.println(Thread.currentThread() + " 任务 " + id);
                });
            }
        }   // try-with-resources 自动 close

        // ============ 6. 拒绝策略 ============
        System.out.println("\n========== 拒绝策略 ==========");
        // 4 种内置策略：
        // AbortPolicy      抛出异常（默认）
        // CallerRunsPolicy 让提交任务的线程自己执行
        // DiscardPolicy    静默丢弃
        // DiscardOldestPolicy 丢弃最老的任务

        // ============ 7. 线程池状态 ============
        // RUNNING     接收新任务
        // SHUTDOWN    不接受新任务，处理完队列中的任务
        // STOP        不接受新任务，中断正在执行的
        // TIDYING     所有任务终结，队列为空
        // TERMINATED  彻底结束
    }
}

/*
 * =============== 为什么需要线程池 ===============
 *
 * 1. 降低创建/销毁线程的开销
 * 2. 提高响应速度（线程已存在）
 * 3. 便于管理和监控
 * 4. 限制并发数，避免资源耗尽
 *
 * =============== Executor 家族 ===============
 *
 * Executor                       execute(Runnable)
 *   └── ExecutorService          submit, shutdown, invokeAll/Any
 *         └── ScheduledExecutorService  schedule, scheduleAtFixedRate
 *
 * 实现：
 *   ThreadPoolExecutor           标准线程池
 *   ScheduledThreadPoolExecutor  定时线程池
 *   ForkJoinPool                 工作窃取池
 *
 * =============== ThreadPoolExecutor 的 7 个参数 ===============
 *
 * 1. corePoolSize        核心线程数（常驻）
 * 2. maximumPoolSize     最大线程数
 * 3. keepAliveTime       非核心线程空闲时间
 * 4. unit                时间单位
 * 5. workQueue           任务队列
 * 6. threadFactory       线程工厂
 * 7. handler             拒绝策略
 *
 * 执行流程：
 *   任务提交 → 核心线程处理
 *   核心已满 → 加入队列
 *   队列已满 → 创建非核心线程
 *   已达最大 → 执行拒绝策略
 *
 * =============== Executors 工厂方法（不推荐用于生产） ===============
 *
 * newFixedThreadPool(n)     无界队列，可能 OOM
 * newSingleThreadExecutor() 无界队列，可能 OOM
 * newCachedThreadPool()     无界线程数，可能 OOM
 * newScheduledThreadPool(n) 定时任务
 * newVirtualThreadPerTaskExecutor()  JDK 21+ 虚拟线程
 *
 * 阿里巴巴规范：禁止用 Executors 创建线程池，应手动 new ThreadPoolExecutor
 *
 * =============== Callable vs Runnable ===============
 *
 *              返回值    抛异常
 * Runnable    无         不能抛检查异常
 * Callable    有         可抛任何异常
 *
 * =============== Future 的方法 ===============
 *
 * get()                     阻塞获取结果
 * get(timeout, unit)        带超时
 * cancel(mayInterrupt)      取消任务
 * isDone()                  是否完成
 * isCancelled()             是否被取消
 *
 * =============== 关闭线程池 ===============
 *
 * shutdown()          优雅关闭：不接受新任务，等待队列执行完
 * shutdownNow()       立即关闭：尝试中断所有任务
 * awaitTermination()  等待终结
 *
 * 建议 try-with-resources（ExecutorService 实现 AutoCloseable，JDK 19+）
 */
