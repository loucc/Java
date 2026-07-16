/**
 * Operators.java - Java 运算符
 * <p>
 * 学习要点：
 * 1. 算术运算符 (+, -, *, /, %, ++, --)
 * 2. 赋值运算符 (=, +=, -=, *=, /=, %=)
 * 3. 比较运算符 (==, !=, >, <, >=, <=)
 * 4. 逻辑运算符 (&&, ||, !)
 * 5. 位运算符 (&, |, ^, ~, <<, >>, >>>)
 * 6. 三元运算符 (? :)
 * 7. instanceof 运算符
 * 8. 运算符优先级
 */
public class Operators {

    public static void main(String[] args) {

        // ============ 1. 算术运算符 ============
        System.out.println("========== 算术运算符 ==========");
        int a = 10, b = 3;
        System.out.println(a + " + " + b + " = " + (a + b));    // 13
        System.out.println(a + " - " + b + " = " + (a - b));    // 7
        System.out.println(a + " * " + b + " = " + (a * b));    // 30
        System.out.println(a + " / " + b + " = " + (a / b));    // 3（整数除法，直接截断小数）
        System.out.println(a + " % " + b + " = " + (a % b));    // 1（取余）

        // 浮点数除法
        System.out.println("10.0 / 3 = " + (10.0 / 3));         // 3.333...

        // 生产代码需要注意整数溢出和二进制浮点误差
        System.out.println("int 溢出: " + (Integer.MAX_VALUE + 1));
        System.out.println("0.1 + 0.2 = " + (0.1 + 0.2));
        System.out.println("金额等十进制精确计算应使用 BigDecimal，而不是 double");

        // 自增/自减
        int x = 5;
        System.out.println("x = " + x);
        System.out.println("x++ = " + (x++));    // 先用后加，输出 5
        System.out.println("此时 x = " + x);      // 6
        System.out.println("++x = " + (++x));    // 先加后用，输出 7
        System.out.println("此时 x = " + x);      // 7

        // ============ 2. 赋值运算符 ============
        System.out.println("\n========== 赋值运算符 ==========");
        int n = 10;
        n += 5;                                  // 相当于 n = n + 5
        System.out.println("n += 5 → " + n);     // 15
        n -= 3;                                  // n = n - 3
        System.out.println("n -= 3 → " + n);     // 12
        n *= 2;                                  // n = n * 2
        System.out.println("n *= 2 → " + n);     // 24
        n /= 4;                                  // n = n / 4
        System.out.println("n /= 4 → " + n);     // 6
        n %= 4;                                  // n = n % 4
        System.out.println("n %= 4 → " + n);     // 2

        // ============ 3. 比较运算符 ============
        System.out.println("\n========== 比较运算符 ==========");
        int p = 10, q = 20;
        System.out.println(p + " == " + q + " : " + (p == q));    // false
        System.out.println(p + " != " + q + " : " + (p != q));    // true
        System.out.println(p + " > " + q + " : " + (p > q));      // false
        System.out.println(p + " < " + q + " : " + (p < q));      // true
        System.out.println(p + " >= " + q + " : " + (p >= q));    // false
        System.out.println(p + " <= " + q + " : " + (p <= q));    // true

        // 注意：字符串比较不能用 ==，要用 equals
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");
        System.out.println("s1 == s2: " + (s1 == s2));            // true (字符串常量池)
        System.out.println("s1 == s3: " + (s1 == s3));            // false (new 出的对象在堆上)
        System.out.println("s1.equals(s3): " + s1.equals(s3));    // true (比较内容)

        // ============ 4. 逻辑运算符 ============
        System.out.println("\n========== 逻辑运算符 ==========");
        boolean t = true, f = false;
        System.out.println("true && false = " + (t && f));    // false，逻辑与
        System.out.println("true || false = " + (t || f));    // true，逻辑或
        System.out.println("!true = " + (!t));                // false，逻辑非

        // 短路特性：&& 左侧为 false 时右侧不执行；|| 左侧为 true 时右侧不执行
        int num = 0;
        // 如果没有短路，num == 0 且 10/num 会抛出异常
        boolean check = (num != 0) && (10 / num > 1);
        System.out.println("短路与结果: " + check);           // false，没有异常

        // ============ 5. 位运算符 ============
        System.out.println("\n========== 位运算符 ==========");
        int m = 12;         // 二进制 1100
        int nn = 10;        // 二进制 1010
        System.out.println("m & n = " + (m & nn));     // 1000 = 8   按位与
        System.out.println("m | n = " + (m | nn));     // 1110 = 14  按位或
        System.out.println("m ^ n = " + (m ^ nn));     // 0110 = 6   按位异或
        System.out.println("~m    = " + (~m));         // -13        按位取反
        System.out.println("m << 2 = " + (m << 2));    // 48         左移2位 (相当于 * 2^2)
        System.out.println("m >> 2 = " + (m >> 2));    // 3          右移2位 (相当于 / 2^2)
        System.out.println("m >>> 2 = " + (m >>> 2));  // 3          无符号右移

        // ============ 6. 三元运算符 ============
        System.out.println("\n========== 三元运算符 ==========");
        int age = 20;
        String status = age >= 18 ? "成年人" : "未成年";
        System.out.println("age=" + age + " → " + status);

        // 求最大值
        int max = a > b ? a : b;
        System.out.println("max(" + a + "," + b + ") = " + max);

        // 三元运算符可以嵌套，但多层嵌套会降低可读性，生产代码优先使用 if 或 switch
        int score = 85;
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7, 6 -> "C";
            default -> "D";
        };
        System.out.println("score=" + score + " → " + grade);

        // ============ 7. instanceof 运算符 ============
        System.out.println("\n========== instanceof ==========");
        Object obj = "Hello";
        System.out.println("obj instanceof String: " + (obj instanceof String));
        System.out.println("obj instanceof Integer: " + (obj instanceof Integer));

        // JDK 16+ 的模式匹配（详见 07_JDK25新特性/PatternMatching.java）
        if (obj instanceof String str) {
            System.out.println("字符串长度: " + str.length());
        }

        // ============ 8. 运算符优先级 ============
        System.out.println("\n========== 运算符优先级 ==========");
        // 优先级：() > 一元(++/--/!) > 乘除 > 加减 > 比较 > 逻辑 > 三元 > 赋值
        int r = 1 + 2 * 3;                          // 7，先乘后加
        int r2 = (1 + 2) * 3;                       // 9，括号优先
        boolean bool = 5 > 3 && 2 < 4;              // true，比较先于逻辑
        System.out.println("1 + 2 * 3 = " + r);
        System.out.println("(1 + 2) * 3 = " + r2);
        System.out.println("5 > 3 && 2 < 4 = " + bool);
    }
}

/*
 * =============== 运算符优先级从高到低 ===============
 *
 * 1. ()  []  .
 * 2. 一元运算符：++  --  !  ~
 * 3. 算术运算符：*  /  %
 * 4. 算术运算符：+  -
 * 5. 移位运算符：<<  >>  >>>
 * 6. 比较运算符：<  <=  >  >=  instanceof
 * 7. 相等运算符：==  !=
 * 8. 位运算符：&
 * 9. 位运算符：^
 * 10. 位运算符：|
 * 11. 逻辑与：&&
 * 12. 逻辑或：||
 * 13. 三元：?  :
 * 14. 赋值：=  +=  -=  *=  /= 等
 *
 * 建议：不确定优先级时，直接用括号 () 明确表达
 */
