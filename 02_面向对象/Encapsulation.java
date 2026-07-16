/**
 * Encapsulation.java - 封装
 * <p>
 * 学习要点：
 * 1. 封装的概念
 * 2. 访问修饰符（public、protected、default、private）
 * 3. private 字段 + public getter/setter
 * 4. JavaBean 规范
 * 5. 数据校验
 */
public class Encapsulation {

    public static void main(String[] args) {

        // ============ 1. 封装的好处 ============
        System.out.println("========== 封装示例 ==========");

        Account acc = new Account("6217000000000001");

        // 直接访问 balance 会编译错误，只能通过方法
        // acc.balance = -1000;                 // 编译错误！private

        acc.deposit(100_000);                   // 存入 1000.00 元（单位：分）
        acc.withdraw(20_000);                   // 取出 200.00 元
        acc.withdraw(200_000);                  // 余额不足
        acc.deposit(-10_000);                   // 非法金额

        System.out.printf("当前余额: %.2f 元%n", acc.getBalanceInCents() / 100.0);

        // ============ 2. 数据校验 ============
        System.out.println("\n========== 数据校验 ==========");
        User user = new User();
        user.setName("");                       // 名字不能为空
        user.setAge(200);                       // 年龄不合理
        user.setEmail("invalid");               // 邮箱格式错误

        user.setName("张三");
        user.setAge(25);
        user.setEmail("zhang@example.com");
        user.print();
    }
}

// ============ 银行账户：演示封装 ============
class Account {

    // private 字段：外部无法直接访问
    private final String accountNo;
    private long balanceInCents;

    public Account(String accountNo) {
        this.accountNo = accountNo;
        this.balanceInCents = 0;
    }

    // getter：允许读取
    public String getAccountNo() {
        return accountNo;
    }

    public long getBalanceInCents() {
        return balanceInCents;
    }

    // 业务方法：控制修改逻辑
    public void deposit(long amountInCents) {
        if (amountInCents <= 0) {
            System.out.println("存款金额必须大于 0");
            return;
        }
        balanceInCents = Math.addExact(balanceInCents, amountInCents);
        System.out.printf("存入 %.2f 元, 余额 %.2f 元%n",
            amountInCents / 100.0, balanceInCents / 100.0);
    }

    public void withdraw(long amountInCents) {
        if (amountInCents <= 0) {
            System.out.println("取款金额必须大于 0");
            return;
        }
        if (amountInCents > balanceInCents) {
            System.out.printf("余额不足，当前余额 %.2f 元%n", balanceInCents / 100.0);
            return;
        }
        balanceInCents -= amountInCents;
        System.out.printf("取出 %.2f 元, 余额 %.2f 元%n",
            amountInCents / 100.0, balanceInCents / 100.0);
    }
}

// ============ JavaBean 规范：私有字段 + 公开 getter/setter ============
class User {

    private String name;
    private int age;
    private String email;

    // 无参构造器（JavaBean 要求）
    public User() {}

    // 全参构造器（可选）
    public User(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // getter
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    // setter，可以加校验
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("[Error] 名字不能为空");
            return;
        }
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            System.out.println("[Error] 年龄必须在 0-150 之间");
            return;
        }
        this.age = age;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            System.out.println("[Error] 邮箱格式错误");
            return;
        }
        this.email = email;
    }

    public void print() {
        System.out.println("User { name='" + name + "', age=" + age + ", email='" + email + "' }");
    }
}

/*
 * =============== 封装的核心思想 ===============
 *
 * 封装 = 把数据和操作数据的方法绑定在一起，并对外隐藏内部实现细节
 *
 * 好处：
 * 1. 保护数据（防止非法修改）
 * 2. 便于修改（内部实现改变不影响外部）
 * 3. 便于维护（集中控制）
 * 4. 提供数据校验
 *
 * =============== 访问修饰符（从严到宽） ===============
 *
 *                本类     同包     不同包子类   任意
 * private        √        ×        ×            ×
 * (default)      √        √        ×            ×    (不写任何修饰符)
 * protected      √        √        √            ×
 * public         √        √        √            √
 *
 * 使用建议：
 * - 字段：默认 private
 * - 工具方法：private
 * - 对外API：public
 * - 子类扩展用：protected
 * - 同包工具：不写修饰符 (default)
 *
 * =============== JavaBean 规范 ===============
 *
 * 1. 类必须是 public
 * 2. 提供无参构造器
 * 3. 属性用 private 修饰
 * 4. 提供 public 的 getter/setter
 * 5. 建议实现 Serializable
 *
 * 现代替代方案：
 * - Java Record（详见 Record.java）
 * - Lombok（第三方库，@Getter/@Setter）
 */
