/**
 * MyException.java - 异常处理
 * <p>
 * 学习要点：
 * 1. 异常体系（Throwable → Error / Exception）
 * 2. 编译时异常 vs 运行时异常
 * 3. try-catch-finally
 * 4. try-with-resources
 * 5. throw 和 throws
 * 6. 自定义异常
 * 7. 异常链（cause）
 * 8. 多异常捕获（multi-catch）
 */
public class MyException {

    public static void main(String[] args) {

        // ============ 1. 基本 try-catch ============
        System.out.println("========== 基本 try-catch ==========");
        try {
            int result = 10 / 0;                // 抛出 ArithmeticException
            System.out.println(result);
        } catch (ArithmeticException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }

        // ============ 2. 多异常捕获 ============
        System.out.println("\n========== 多异常捕获 ==========");
        try {
            String s = args.length > 0 ? args[0] : null;
            int i = Integer.parseInt(s);
            System.out.println("解析结果: " + i);
        } catch (NumberFormatException | NullPointerException e) {
            // 用 | 分隔多个异常类型（且这些异常没有继承关系）
            System.out.println("参数解析失败: " + e.getClass().getSimpleName());
        }

        // ============ 3. try-catch-finally ============
        System.out.println("\n========== try-catch-finally ==========");
        try {
            System.out.println("执行 try 块");
            throw new RuntimeException("模拟异常");
        } catch (RuntimeException e) {
            System.out.println("执行 catch 块: " + e.getMessage());
        } finally {
            System.out.println("finally 块（一定会执行，用于资源清理）");
        }
        // 注意：catch 中若写 return，finally 仍会在 return 之前执行
        // （见本文件末尾的 returnFinallyDemo 方法）

        // ============ 4. 抛出异常 ============
        System.out.println("\n========== 抛出异常 ==========");
        try {
            checkAge(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获: " + e.getMessage());
        }

        // ============ 5. 自定义异常 ============
        System.out.println("\n========== 自定义异常 ==========");
        try {
            withdraw(500, 1000);
        } catch (InsufficientBalanceException e) {
            System.out.println("业务异常: " + e.getMessage());
            System.out.println("当前余额: " + e.getCurrentBalance());
        }

        // ============ 6. 异常链 ============
        System.out.println("\n========== 异常链 ==========");
        try {
            businessMethod();
        } catch (BusinessException e) {
            System.out.println("业务异常: " + e.getMessage());
            System.out.println("原因: " + e.getCause().getMessage());
            // 完整堆栈
            // e.printStackTrace();
        }

        // ============ 7. try-with-resources（自动关闭资源） ============
        System.out.println("\n========== try-with-resources ==========");
        // 传统写法：
        // Resource r = null;
        // try { r = new Resource(); ... } finally { if (r != null) r.close(); }

        // JDK 7+ 推荐：
        try (MyResource resource = new MyResource("配置文件")) {
            resource.read();
        }
        // 无需手动关闭，try 结束会自动调用 close()

        // 可以有多个资源
        try (MyResource r1 = new MyResource("R1");
             MyResource r2 = new MyResource("R2")) {
            r1.read();
            r2.read();
        }

        // JDK 9+ 可以引用外部 final 或事实上 final 的资源
        MyResource external = new MyResource("外部资源");
        try (external) {                                        // 直接使用变量名
            external.read();
        }

        // ============ 8. 常见运行时异常一览 ============
        System.out.println("\n========== 常见异常 ==========");
        showCommonExceptions();

        // ============ 9. catch 中 return，finally 仍会执行 ============
        System.out.println("\n========== return 与 finally ==========");
        returnFinallyDemo();
        System.out.println("returnFinallyDemo 已返回，main 继续");
    }

    // 演示：catch 中 return，finally 仍会在 return 前执行
    // 单独成方法是为了避免在 main 中因 return 导致后续代码不可达
    static void returnFinallyDemo() {
        try {
            throw new RuntimeException("触发 catch");
        } catch (RuntimeException e) {
            System.out.println("catch 块: 准备 return");
            return;                                             // 即使 return，finally 也会执行
        } finally {
            System.out.println("finally 块（在 return 前执行）");
        }
    }

    // 使用 throw 抛出异常
    static void checkAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负: " + age);
        }
        System.out.println("年龄合法: " + age);
    }

    // 使用 throws 声明异常（编译时异常必须声明或捕获）
    static void withdraw(double amount, double balance) throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                "取款失败，金额: " + amount, balance);
        }
        System.out.println("成功取款: " + amount);
    }

    static void businessMethod() throws BusinessException {
        try {
            // 底层操作
            String s = null;
            s.length();                                         // 抛出 NPE
        } catch (NullPointerException e) {
            // 将底层异常包装为业务异常
            throw new BusinessException("业务处理失败", e);
        }
    }

    static void showCommonExceptions() {
        // NullPointerException
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {
            System.out.println("NPE: 空指针");
        }

        // ArrayIndexOutOfBoundsException
        try {
            int[] arr = new int[3];
            int x = arr[5];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界: " + e.getMessage());
        }

        // ClassCastException
        try {
            Object obj = "string";
            Integer i = (Integer) obj;
        } catch (ClassCastException e) {
            System.out.println("类型转换错误");
        }

        // NumberFormatException
        try {
            int i = Integer.parseInt("abc");
        } catch (NumberFormatException e) {
            System.out.println("数字格式错误: " + e.getMessage());
        }

        // ArithmeticException
        try {
            int x = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("算术异常: " + e.getMessage());
        }
    }
}

// ============ 自定义异常（继承 Exception：编译时异常） ============
class InsufficientBalanceException extends Exception {

    private final double currentBalance;

    public InsufficientBalanceException(String message, double currentBalance) {
        super(message);
        this.currentBalance = currentBalance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}

// ============ 业务异常（异常链） ============
class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

// ============ 自定义运行时异常（无需 throws） ============
class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}

// ============ 可自动关闭的资源（try-with-resources） ============
class MyResource implements AutoCloseable {

    private final String name;

    public MyResource(String name) {
        this.name = name;
        System.out.println("[打开] " + name);
    }

    public void read() {
        System.out.println("[读取] " + name);
    }

    // 实现 close 方法（AutoCloseable 接口）
    @Override
    public void close() {
        System.out.println("[关闭] " + name);
    }
}

/*
 * =============== Java 异常体系 ===============
 *
 * Throwable
 *   ├── Error          - 严重错误，程序无法处理（如 OutOfMemoryError）
 *   └── Exception
 *         ├── RuntimeException  - 运行时异常（非受检异常）
 *         │     ├── NullPointerException
 *         │     ├── ArrayIndexOutOfBoundsException
 *         │     ├── ClassCastException
 *         │     ├── NumberFormatException
 *         │     ├── ArithmeticException
 *         │     └── IllegalArgumentException
 *         └── 其他 Exception    - 编译时异常（受检异常）
 *               ├── IOException
 *               ├── SQLException
 *               ├── ClassNotFoundException
 *               └── FileNotFoundException
 *
 * =============== 编译时异常 vs 运行时异常 ===============
 *
 * 编译时异常（Checked）：
 * - 必须显式处理（try-catch 或 throws）
 * - 编译期检查
 * - 例：IOException、SQLException
 * - 用于"可预期的、可恢复的"错误
 *
 * 运行时异常（Unchecked）：
 * - 可以不处理
 * - 编译器不检查
 * - 例：NPE、越界、ClassCast
 * - 用于"编程错误、意外情况"
 *
 * =============== 处理异常的原则 ===============
 *
 * 1. 只在能处理时才 catch
 * 2. 不要吞异常（catch 后什么都不做）
 * 3. 记录足够的日志信息
 * 4. 保留异常链（cause）
 * 5. 资源使用 try-with-resources
 * 6. finally 中不要 return（会覆盖 try 中的 return）
 * 7. 精确捕获，不要 catch (Exception e) 或 Throwable
 * 8. 自定义异常包含足够的上下文
 *
 * =============== 常用方法 ===============
 *
 * getMessage()           - 返回异常信息
 * getCause()             - 返回原始异常
 * printStackTrace()      - 打印堆栈跟踪
 * getStackTrace()        - 获取堆栈数组
 * getLocalizedMessage()  - 本地化消息
 */
