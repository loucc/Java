import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MySynchronized.java - synchronized、volatile 和原子类
 * <p>
 * 学习要点：
 * 1. 线程安全问题的原因
 * 2. synchronized 关键字（三种用法）
 * 3. volatile 关键字（可见性）
 * 4. ReentrantLock 显式锁
 * 5. 原子类 AtomicInteger 等
 * 6. wait / notify 线程通信
 */
public class MySynchronized {

    public static void main(String[] args) throws InterruptedException {

        // ============ 1. 演示线程不安全 ============
        System.out.println("========== 线程不安全示例 ==========");
        UnsafeCounter unsafe = new UnsafeCounter();
        runCounter(() -> unsafe.increment(), "unsafe");
        System.out.println("期望: 10000, 实际: " + unsafe.count);

        // ============ 2. synchronized 解决 ============
        System.out.println("\n========== synchronized ==========");
        SafeCounter safe = new SafeCounter();
        runCounter(() -> safe.increment(), "synchronized");
        System.out.println("期望: 10000, 实际: " + safe.count);

        // ============ 3. ReentrantLock ============
        System.out.println("\n========== ReentrantLock ==========");
        LockCounter lockCounter = new LockCounter();
        runCounter(() -> lockCounter.increment(), "lock");
        System.out.println("期望: 10000, 实际: " + lockCounter.count);

        // ============ 4. 原子类（适合单变量原子更新）============
        System.out.println("\n========== 原子类 ==========");
        AtomicCounter atomic = new AtomicCounter();
        runCounter(() -> atomic.increment(), "atomic");
        System.out.println("期望: 10000, 实际: " + atomic.count.get());

        // ============ 5. volatile 演示 ============
        System.out.println("\n========== volatile ==========");
        VolatileDemo vd = new VolatileDemo();
        Thread reader = new Thread(vd::reader);
        reader.start();
        Thread.sleep(100);
        vd.flag = true;
        reader.join();

        // ============ 6. wait / notify ============
        System.out.println("\n========== wait/notify ==========");
        WaitNotifyDemo wn = new WaitNotifyDemo();

        Thread consumer = new Thread(() -> {
            try { wn.consume(); } catch (InterruptedException e) {}
        });
        consumer.start();

        Thread.sleep(200);
        Thread producer = new Thread(() -> wn.produce());
        producer.start();

        consumer.join();
        producer.join();
    }

    // 启动 10 个线程，每个执行 1000 次操作
    static void runCounter(Runnable task, String name) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    task.run();
                }
            }, name + "-" + i);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();
    }
}

// ============ 线程不安全的计数器 ============
class UnsafeCounter {
    int count = 0;

    // count++ 不是原子操作！包含读、加、写三步
    public void increment() {
        count++;
    }
}

// ============ 用 synchronized 修饰方法 ============
class SafeCounter {
    int count = 0;

    // 方式一：synchronized 修饰实例方法（锁 this）
    public synchronized void increment() {
        count++;
    }

    // 方式二：synchronized 修饰静态方法（锁 Class 对象）
    public static synchronized void staticMethod() {
        // ...
    }

    // 方式三：synchronized 代码块（更细粒度）
    private final Object lock = new Object();
    public void increment2() {
        synchronized (lock) {
            count++;
        }
    }
}

// ============ 使用 ReentrantLock ============
class LockCounter {
    int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();                                // 加锁
        try {
            count++;
        } finally {
            lock.unlock();                          // 必须在 finally 中解锁
        }
    }
}

// ============ 原子类（适合单变量原子更新）============
class AtomicCounter {
    AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();                    // 原子自增
        // count.getAndIncrement();                 // 后自增
        // count.addAndGet(5);                      // 加 5
        // count.compareAndSet(0, 1);               // CAS
    }
}

// ============ volatile 保证可见性 ============
class VolatileDemo {

    // 没有 volatile：其他线程修改可能对当前线程不可见（JIT 优化后可能死循环）
    // 加了 volatile：保证可见性，即：一个线程写了，其他线程能立即看到
    volatile boolean flag = false;

    public void reader() {
        System.out.println("reader 等待 flag...");
        while (!flag) {
            // 不加 volatile 可能永远看不到 flag 被改成 true
        }
        System.out.println("reader 看到 flag=true, 退出");
    }
}

// ============ wait / notify 线程通信 ============
class WaitNotifyDemo {
    private String product;

    public synchronized void produce() {
        System.out.println("[生产者] 生产商品");
        product = "商品A";
        notify();                                   // 唤醒等待的线程
        // notifyAll();                             // 唤醒所有等待的线程
    }

    public synchronized void consume() throws InterruptedException {
        while (product == null) {
            System.out.println("[消费者] 等待商品...");
            wait();                                 // 释放锁并等待
        }
        System.out.println("[消费者] 消费商品: " + product);
        product = null;
    }
}

/*
 * =============== 线程安全问题的根源 ===============
 *
 * 1. 原子性：一个操作要么全部执行，要么全部不执行
 *    - count++ 实际是 3 步：读 → 加 → 写
 * 2. 可见性：一个线程的修改对其他线程立即可见
 *    - CPU 缓存导致
 * 3. 有序性：程序按代码顺序执行
 *    - 编译器/CPU 重排序
 *
 * =============== synchronized 详解 ===============
 *
 * 用法：
 *   synchronized 方法      锁 this
 *   synchronized 静态方法  锁 Class 对象
 *   synchronized(obj) {}   锁 obj
 *
 * 特性：
 *   - 保证原子性、可见性、有序性
 *   - 可重入（同一线程可以重复获取同一把锁）
 *   - JDK 6+ 有锁优化（偏向、轻量级、重量级）
 *
 * 注意：
 *   - 锁对象要选好（不要用 String、Integer 等常量池对象）
 *   - 范围越小越好
 *
 * =============== volatile 详解 ===============
 *
 * 保证：
 *   - 可见性
 *   - 有序性（禁止指令重排）
 *
 * 不保证：
 *   - 原子性（不能替代锁）
 *
 * 适用场景：
 *   - 一写多读的标志位
 *   - 单例的双重检查锁定
 *   - 状态标记
 *
 * =============== synchronized vs ReentrantLock ===============
 *
 *                    synchronized       ReentrantLock
 * 使用              关键字，隐式        显式加锁/解锁
 * 可中断            不可                lockInterruptibly
 * 尝试锁            无                  tryLock(timeout)
 * 公平锁            非公平              可配置
 * 条件变量          单个 wait/notify    多个 Condition
 * 性能              依竞争和临界区测量    依竞争和临界区测量
 *
 * =============== 原子类家族 ===============
 *
 * AtomicInteger, AtomicLong, AtomicBoolean
 * AtomicReference<V>
 * AtomicIntegerArray, AtomicLongArray
 * LongAdder, DoubleAdder（高竞争统计场景可减少热点，读取不是原子快照）
 *
 * 原子类通常基于 CAS，适合表达单变量原子状态；多字段不变量仍需要其他同步设计
 *
 * =============== wait / notify 使用规则 ===============
 *
 * 1. 必须在 synchronized 块内调用
 * 2. wait/notify 的对象必须是锁对象
 * 3. wait 会释放锁并阻塞，被 notify 后重新竞争锁
 * 4. wait 应该在 while 循环中（防止虚假唤醒）
 * 5. 优先用 java.util.concurrent（BlockingQueue、Semaphore 等）
 */
