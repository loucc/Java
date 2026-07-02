/**
 * MyThread.java - 线程基础
 * <p>
 * 学习要点：
 * 1. 进程和线程的概念
 * 2. 创建线程的 3 种方式
 * 3. 线程的生命周期
 * 4. 线程的常用方法（start, sleep, join, interrupt, yield）
 * 5. 线程优先级
 * 6. 守护线程
 */
public class MyThread {

    public static void main(String[] args) throws InterruptedException {

        // ============ 1. 创建线程方式一：继承 Thread ============
        System.out.println("========== 继承 Thread ==========");
        MyThreadA t1 = new MyThreadA("线程A");
        t1.start();                                     // 启动线程，会调用 run()
        // t1.run();                                    // 直接调用 run 是普通方法调用！

        // ============ 2. 方式二：实现 Runnable（推荐） ============
        System.out.println("\n========== 实现 Runnable ==========");
        Thread t2 = new Thread(new MyRunnable(), "线程B");
        t2.start();

        // Lambda 简化（Runnable 是函数式接口）
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Lambda 线程: " + i);
            }
        }, "线程C");
        t3.start();

        // ============ 3. 方式三：实现 Callable（有返回值） ============
        System.out.println("\n========== 实现 Callable ==========");
        java.util.concurrent.Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 1; i <= 100; i++) sum += i;
            return sum;
        };

        java.util.concurrent.FutureTask<Integer> future = new java.util.concurrent.FutureTask<>(callable);
        Thread t4 = new Thread(future, "线程D");
        t4.start();

        try {
            Integer result = future.get();          // 阻塞直到得到结果
            System.out.println("1..100 之和: " + result);
        } catch (java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }

        // ============ 4. 常用方法 ============
        System.out.println("\n========== Thread 常用方法 ==========");

        // 当前线程信息
        Thread current = Thread.currentThread();
        System.out.println("当前线程: " + current.getName());
        System.out.println("线程 ID: " + current.threadId());   // JDK 19+
        System.out.println("线程状态: " + current.getState());

        // sleep：让当前线程休眠
        System.out.println("休眠 100ms...");
        Thread.sleep(100);
        System.out.println("醒来了");

        // join：等待另一线程结束
        Thread joinT = new Thread(() -> {
            try { Thread.sleep(200); } catch (InterruptedException e) {}
            System.out.println("[joinT] 执行完毕");
        });
        joinT.start();
        joinT.join();                               // 主线程等 joinT 结束
        System.out.println("[主线程] joinT 已结束");

        // yield：让出 CPU（提示，不保证）
        Thread.yield();

        // ============ 5. 线程优先级 ============
        System.out.println("\n========== 优先级 ==========");
        Thread low = new Thread(() -> System.out.println("低优先级"));
        Thread high = new Thread(() -> System.out.println("高优先级"));

        low.setPriority(Thread.MIN_PRIORITY);       // 1
        high.setPriority(Thread.MAX_PRIORITY);      // 10
        // 默认：Thread.NORM_PRIORITY (5)

        low.start();
        high.start();

        // ============ 6. 守护线程 ============
        System.out.println("\n========== 守护线程 ==========");
        Thread daemon = new Thread(() -> {
            while (true) {
                System.out.println("[守护] tick");
                try { Thread.sleep(50); } catch (InterruptedException e) { break; }
            }
        });
        daemon.setDaemon(true);                     // 必须在 start 前设置
        daemon.start();

        Thread.sleep(200);
        System.out.println("[主] 主线程结束，守护线程也会终止");

        // ============ 7. 中断（interrupt） ============
        System.out.println("\n========== 中断线程 ==========");
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("[worker] 干活中...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // sleep 抛异常时会清除中断标志，需要手动恢复
                    System.out.println("[worker] 被中断");
                    Thread.currentThread().interrupt();     // 重设中断标志
                    break;
                }
            }
            System.out.println("[worker] 优雅结束");
        });
        worker.start();

        Thread.sleep(300);
        worker.interrupt();
        worker.join();

        // ============ 8. 线程状态 ============
        System.out.println("\n========== 线程状态 ==========");
        Thread state = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        });
        System.out.println("初始: " + state.getState());       // NEW
        state.start();
        System.out.println("启动后: " + state.getState());     // RUNNABLE
        Thread.sleep(50);
        System.out.println("休眠中: " + state.getState());     // TIMED_WAITING
        state.join();
        System.out.println("结束后: " + state.getState());     // TERMINATED
    }
}

// 方式一：继承 Thread
class MyThreadA extends Thread {
    public MyThreadA(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(getName() + ": " + i);
        }
    }
}

// 方式二：实现 Runnable
class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}

/*
 * =============== 进程 vs 线程 ===============
 *
 * 进程：操作系统资源分配的基本单位（一个 JVM 进程）
 * 线程：CPU 调度的基本单位（一个进程内有多个线程）
 *
 * 特点：
 * - 同一进程内的线程共享内存（堆、方法区）
 * - 各线程有独立的栈
 * - 线程切换比进程切换轻量
 *
 * =============== 创建线程 3 种方式对比 ===============
 *
 * 方式             优点                缺点
 * extends Thread   简单                Java 单继承，无法再继承其他类
 * implements Runnable  常用           无返回值
 * implements Callable  有返回值、可抛异常  略复杂，通过 Future 获取结果
 *
 * 推荐：Runnable 或 Callable + 线程池
 *
 * =============== 线程的生命周期（状态） ===============
 *
 * NEW              新建，未 start
 * RUNNABLE         就绪或运行中
 * BLOCKED          阻塞（等待锁）
 * WAITING          无限等待（wait/join/park）
 * TIMED_WAITING    有限等待（sleep/wait(t)/join(t)）
 * TERMINATED       结束
 *
 * =============== 常用方法 ===============
 *
 * start()          启动线程
 * run()            线程主体（不要直接调用）
 * sleep(ms)        休眠（static，作用于当前线程）
 * join()           等待线程结束
 * yield()          让出 CPU（提示，static）
 * interrupt()      中断线程
 * isInterrupted()  是否被中断
 * interrupted()    是否被中断（会清除标志，static）
 * currentThread()  当前线程（static）
 * getName/setName  名字
 * getPriority/setPriority  优先级（1-10）
 * setDaemon(true)  设置为守护线程
 * getState()       获取状态
 * getId()/threadId() 线程 ID（JDK 19+ 用 threadId）
 *
 * =============== JDK 21+ 虚拟线程 ===============
 *
 * 传统线程（平台线程）：1:1 映射到 OS 线程，昂贵
 * 虚拟线程：JVM 管理，轻量级，可以创建百万个
 *
 * 详见 VirtualThread.java
 */
