/**
 * Loops.java - Java 循环结构
 * <p>
 * 学习要点：
 * 1. for 循环
 * 2. while 循环
 * 3. do-while 循环
 * 4. 增强 for 循环（for-each）
 * 5. break 和 continue
 * 6. 标签（label）
 * 7. 死循环
 */
public class Loops {

    public static void main(String[] args) {

        // ============ 1. for 循环 ============
        System.out.println("========== for 循环 ==========");
        // for (初始化; 条件; 迭代)
        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // 逆序循环
        for (int i = 5; i > 0; i--) {
            System.out.print(i + " ");
        }
        System.out.println();

        // 多变量的 for 循环
        for (int i = 0, j = 10; i < j; i++, j--) {
            System.out.println("i=" + i + ", j=" + j);
        }

        // ============ 2. while 循环 ============
        System.out.println("\n========== while 循环 ==========");
        int count = 0;
        while (count < 5) {
            System.out.print(count + " ");
            count++;
        }
        System.out.println();

        // 用 while 处理"直到条件满足"的场景
        int num = 100;
        int digits = 0;
        while (num > 0) {
            num /= 10;
            digits++;
        }
        System.out.println("100 有 " + digits + " 位数字");

        // ============ 3. do-while 循环 ============
        System.out.println("\n========== do-while 循环 ==========");
        // 先执行一次，再判断条件——至少执行一次
        int n = 10;
        do {
            System.out.println("n = " + n);
            n--;
        } while (n > 7);

        // 特点：即使条件初始就不满足，也会执行一次
        int x = 100;
        do {
            System.out.println("do-while 至少执行一次，x=" + x);
        } while (x < 50);

        // ============ 4. 增强 for 循环（推荐用于遍历） ============
        System.out.println("\n========== 增强 for 循环 ==========");
        int[] arr = {10, 20, 30, 40, 50};
        // 语法：for (类型 变量 : 数组或集合)
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();

        String[] fruits = {"苹果", "香蕉", "橙子"};
        for (String fruit : fruits) {
            System.out.println("水果：" + fruit);
        }

        // 注意：增强 for 循环不能修改数组元素本身
        // 也无法获取索引

        // ============ 5. break 和 continue ============
        System.out.println("\n========== break ==========");
        // break: 立即退出当前循环
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                break;                          // 遇到 5 就退出
            }
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("\n========== continue ==========");
        // continue: 跳过当前迭代，进入下一次
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                continue;                       // 跳过偶数
            }
            System.out.print(i + " ");          // 只打印奇数
        }
        System.out.println();

        // ============ 6. 嵌套循环 ============
        System.out.println("\n========== 嵌套循环：九九乘法表 ==========");
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.printf("%d*%d=%-2d  ", j, i, i * j);
            }
            System.out.println();
        }

        // ============ 7. 标签（label）配合 break/continue ============
        System.out.println("\n========== 标签控制嵌套循环 ==========");
        // 在嵌套循环中，break/continue 默认只影响最内层
        // 使用标签可以指定影响哪一层
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i * j > 6) {
                    System.out.println("退出：i=" + i + ", j=" + j);
                    break outer;                // 退出外层循环
                }
                System.out.print("(" + i + "," + j + ") ");
            }
        }
        System.out.println();

        // continue 配合标签
        System.out.println("\n========== continue + 标签 ==========");
        loop:
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (j == 2) {
                    continue loop;              // 跳过外层这一次
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // ============ 8. 死循环 ============
        System.out.println("\n========== 死循环示例（有 break 退出） ==========");
        int counter = 0;
        while (true) {                          // 死循环
            if (counter >= 3) {
                break;                          // 必须有退出条件
            }
            System.out.println("counter = " + counter);
            counter++;
        }

        // 等价写法
        // for (;;) { ... }

        // ============ 9. 循环练习：计算 1-100 的和 ============
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("\n1 + 2 + ... + 100 = " + sum);

        // 打印素数
        System.out.println("100 以内的素数：");
        for (int i = 2; i < 100; i++) {
            boolean isPrime = true;
            for (int j = 2; j * j <= i; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}

/*
 * =============== 循环选择建议 ===============
 *
 * 1. 已知循环次数           → for 循环
 * 2. 遍历数组/集合          → 增强 for 循环（for-each）
 * 3. 条件不确定时循环       → while 循环
 * 4. 至少执行一次           → do-while 循环
 * 5. 通过索引访问数组元素   → 传统 for 循环（提供索引）
 * 6. 流式操作/函数式风格    → Stream + forEach（见 Streams.java）
 */
