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

        acc.deposit(1000);                      // 存钱
        acc.withdraw(200);                      // 取钱
        acc.withdraw(2000);                     // 余额不足
        acc.deposit(-100);                      // 非法金额

        System.out.println("当前余额: " + acc.getBalance());

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
    private String accountNo;
    private double balance;

    public Account(String accountNo) {
        this.accountNo = accountNo;
        this.balance = 0;
    }

    // getter：允许读取
    public String getAccountNo() {
        return accountNo;
    }

    public double getBalance() {
        return balance;
    }

    // 业务方法：控制修改逻辑
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("存款金额必须大于 0");
            return;
        }
        balance += amount;
        System.out.println("存入 " + amount + " 元, 余额 " + balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("取款金额必须大于 0");
            return;
        }
        if (amount > balance) {
            System.out.println("余额不足，当前余额 " + balance);
            return;
        }
        balance -= amount;
        System.out.println("取出 " + amount + " 元, 余额 " + balance);
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
