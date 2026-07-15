import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.List;

/**
 * MyCompletableFuture.java - CompletableFuture 异步编程
 * <p>
 * 学习要点：
 * 1. CompletableFuture 与 Future 的区别
 * 2. 创建异步任务（supplyAsync / runAsync）
 * 3. 处理结果（thenApply / thenAccept / thenRun）
 * 4. 组合任务（thenCompose / thenCombine）
 * 5. 异常处理（exceptionally / handle）
 * 6. 多个任务组合（allOf / anyOf）
 */
public class MyCompletableFuture {

    public static void main(String[] args) throws Exception {

        // ============ 1. 创建异步任务 ============
        System.out.println("========== 创建异步任务 ==========");

        // supplyAsync: 有返回值
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Hello";
        });

        // runAsync: 无返回值
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            System.out.println("异步执行");
        });

        System.out.println("结果: " + f1.get());
        f2.get();

        // ============ 2. 链式处理 thenApply / thenAccept / thenRun ============
        System.out.println("\n========== 链式处理 ==========");

        // thenApply: 转换结果（有输入有输出）
        CompletableFuture<Integer> f3 = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World")           // String → String
            .thenApply(String::length);              // String → Integer
        System.out.println("length: " + f3.get());

        // thenAccept: 消费结果（有输入无输出）
        CompletableFuture.supplyAsync(() -> "Result")
            .thenAccept(s -> System.out.println("接收到: " + s))
            .get();

        // thenRun: 不关心结果（无输入无输出）
        CompletableFuture.supplyAsync(() -> "任意值")
            .thenRun(() -> System.out.println("上一步完成，我要干活"))
            .get();

        // ============ 3. thenCompose 平铺依赖 ============
        System.out.println("\n========== thenCompose ==========");

        // 需求：先获取 userId，再根据 userId 获取用户信息
        CompletableFuture<String> user = getUserId()
            .thenCompose(id -> getUserInfo(id));    // 平铺 Future<Future<String>>
        System.out.println("用户: " + user.get());

        // 对比：thenApply 会导致嵌套 Future
        // CompletableFuture<CompletableFuture<String>> nested = getUserId().thenApply(id -> getUserInfo(id));

        // ============ 4. thenCombine 合并两个独立任务 ============
        System.out.println("\n========== thenCombine ==========");

        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return 100;
        });

        CompletableFuture<Double> tax = CompletableFuture.supplyAsync(() -> {
            sleep(60);
            return 0.13;
        });

        CompletableFuture<Double> total = price.thenCombine(tax, (p, t) -> p * (1 + t));
        System.out.println("总价: " + total.get());

        // ============ 5. 异常处理 ============
        System.out.println("\n========== 异常处理 ==========");

        // exceptionally: 只处理异常，返回默认值
        String result = CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("出错了");
            return "success";
        }).exceptionally(ex -> {
            System.out.println("捕获异常: " + ex.getMessage());
            return "默认值";
        }).get();
        System.out.println("结果: " + result);

        // handle: 同时处理正常和异常情况
        String r2 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("失败");
        }).handle((val, ex) -> {
            if (ex != null) {
                return "异常处理: " + ex.getMessage();
            }
            return "正常: " + val;
        }).get();
        System.out.println(r2);

        // ============ 6. 多个任务组合 allOf / anyOf ============
        System.out.println("\n========== allOf / anyOf ==========");

        CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> { sleep(100); return "A"; });
        CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> { sleep(200); return "B"; });
        CompletableFuture<String> c = CompletableFuture.supplyAsync(() -> { sleep(150); return "C"; });

        // allOf: 等所有完成
        CompletableFuture<Void> all = CompletableFuture.allOf(a, b, c);
        all.get();
        System.out.println("所有完成:");
        System.out.println("  A: " + a.get());
        System.out.println("  B: " + b.get());
        System.out.println("  C: " + c.get());

        // 收集所有结果
        CompletableFuture<String>[] futures = new CompletableFuture[]{
            CompletableFuture.supplyAsync(() -> "R1"),
            CompletableFuture.supplyAsync(() -> "R2"),
            CompletableFuture.supplyAsync(() -> "R3")
        };
        CompletableFuture<List<String>> allResults = CompletableFuture
            .allOf(futures)
            .thenApply(v -> {
                List<String> results = new java.util.ArrayList<>();
                for (CompletableFuture<String> f : futures) {
                    try { results.add(f.get()); }
                    catch (Exception e) { results.add("ERR"); }
                }
                return results;
            });
        System.out.println("所有结果: " + allResults.get());

        // anyOf: 任意一个完成
        CompletableFuture<Object> any = CompletableFuture.anyOf(
            CompletableFuture.supplyAsync(() -> { sleep(200); return "慢"; }),
            CompletableFuture.supplyAsync(() -> { sleep(50); return "快"; })
        );
        System.out.println("第一个完成: " + any.get());

        // ============ 7. 完成前处理 ============
        System.out.println("\n========== 手动完成 ==========");
        CompletableFuture<String> manual = new CompletableFuture<>();

        // 用虚拟线程完成（JDK 21+，契合现代异步主题，避免直接 new Thread()）
        Thread.startVirtualThread(() -> {
            sleep(100);
            manual.complete("手动结果");
        });

        System.out.println("等待手动完成: " + manual.get());

        // ============ 8. 超时处理（JDK 9+） ============
        System.out.println("\n========== 超时 ==========");
        try {
            CompletableFuture.supplyAsync(() -> {
                sleep(500);
                return "慢结果";
            }).orTimeout(100, TimeUnit.MILLISECONDS).get();
        } catch (ExecutionException e) {
            System.out.println("超时: " + e.getCause().getClass().getSimpleName());
        }

        // completeOnTimeout: 超时给默认值
        String r = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "慢结果";
        }).completeOnTimeout("默认", 100, TimeUnit.MILLISECONDS).get();
        System.out.println("超时默认: " + r);

        // ============ 9. 综合案例：并行 API 调用 ============
        System.out.println("\n========== 综合案例 ==========");
        long start = System.currentTimeMillis();

        CompletableFuture<String> userFuture = fetchUser(1);
        CompletableFuture<String> orderFuture = fetchOrders(1);
        CompletableFuture<String> profileFuture = fetchProfile(1);

        String combined = userFuture
            .thenCombine(orderFuture, (u, o) -> u + ", " + o)
            .thenCombine(profileFuture, (uo, p) -> uo + ", " + p)
            .get();

        long time = System.currentTimeMillis() - start;
        System.out.println("综合结果: " + combined);
        System.out.println("耗时: " + time + "ms（并行3个各100ms的调用）");
    }

    static CompletableFuture<Integer> getUserId() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return 1001;
        });
    }

    static CompletableFuture<String> getUserInfo(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(50);
            return "用户#" + id;
        });
    }

    static CompletableFuture<String> fetchUser(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "User" + id;
        });
    }

    static CompletableFuture<String> fetchOrders(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Orders(3)";
        });
    }

    static CompletableFuture<String> fetchProfile(int id) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "VIP";
        });
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

/*
 * =============== CompletableFuture vs Future ===============
 *
 *                 Future              CompletableFuture
 * 获取结果         阻塞 get            回调（非阻塞）
 * 链式操作         不支持              支持
 * 手动完成         不支持              complete/completeExceptionally
 * 异常处理         get 时抛出         exceptionally/handle
 * 多任务组合       不支持              allOf/anyOf
 *
 * =============== 常用方法分类 ===============
 *
 * 【创建】
 *   supplyAsync(Supplier)        有返回值
 *   runAsync(Runnable)           无返回值
 *   completedFuture(value)       已完成的
 *
 * 【结果处理（依赖上一步的返回值）】
 *   thenApply(Function)          转换：T → U
 *   thenAccept(Consumer)         消费：T → void
 *   thenRun(Runnable)            仅触发：不关心结果
 *
 * 【组合两个任务】
 *   thenCompose(Function)        平铺：Future<Future<T>> → Future<T>
 *   thenCombine(other, BiFunc)   合并两个 Future 的结果
 *
 * 【异常处理】
 *   exceptionally(Function)       只处理异常
 *   handle(BiFunction)           处理结果和异常
 *   whenComplete(BiConsumer)     完成回调，不改变结果
 *
 * 【多任务】
 *   allOf(futures...)            全部完成
 *   anyOf(futures...)            任一完成
 *
 * 【超时（JDK 9+）】
 *   orTimeout(t, unit)           超时抛异常
 *   completeOnTimeout(val, t, unit)  超时给默认值
 *
 * =============== 方法命名规则 ===============
 *
 * xxx        同步线程
 * xxxAsync   默认线程池（ForkJoinPool.commonPool）
 * xxxAsync(fn, executor)  自定义 Executor
 *
 * =============== 使用建议 ===============
 *
 * 1. 网络/IO 密集任务：用 CompletableFuture 并行
 * 2. 需要转换用 thenApply，需要触发副作用用 thenAccept
 * 3. 依赖前一个的异步结果用 thenCompose
 * 4. 独立任务合并用 thenCombine
 * 5. 记得处理异常（exceptionally/handle）
 * 6. 生产环境用自定义 Executor，避免共享 commonPool
 * 7. JDK 21+ 更推荐结构化并发（详见 StructuredConcurrency.java）
 */
