/**
 * Strings.java - Java 字符串
 * <p>
 * 学习要点：
 * 1. String 的三种创建方式
 * 2. String 的不可变性
 * 3. 字符串常量池
 * 4. String 的常用方法
 * 5. StringBuilder（可变、非线程安全）
 * 6. StringBuffer（可变、线程安全）
 * 7. String.format() 格式化
 */
public class Strings {

    public static void main(String[] args) {

        // ============ 1. String 创建 ============
        System.out.println("========== String 创建 ==========");

        // 方式一：字符串字面量（推荐）
        String s1 = "Hello";
        String s2 = "Hello";
        // s1 和 s2 都指向常量池中同一个对象

        // 方式二：new String()
        String s3 = new String("Hello");        // 在堆中新建对象

        // 方式三：字符数组
        char[] chars = {'H', 'e', 'l', 'l', 'o'};
        String s4 = new String(chars);

        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s3: " + s3);
        System.out.println("s4: " + s4);

        // == 比较的是引用（地址）
        System.out.println("\ns1 == s2: " + (s1 == s2));            // true（同一常量池对象）
        System.out.println("s1 == s3: " + (s1 == s3));              // false（s3 是新对象）
        System.out.println("s1.equals(s3): " + s1.equals(s3));      // true（内容比较）

        // ============ 2. String 的不可变性 ============
        System.out.println("\n========== String 不可变性 ==========");
        String str = "Hello";
        String newStr = str.concat(" World");   // 不修改原字符串，返回新字符串
        System.out.println("原字符串: " + str);
        System.out.println("新字符串: " + newStr);

        // 拼接实际上是创建新对象，效率低
        // 大量拼接建议用 StringBuilder

        // ============ 3. String 常用方法 ============
        System.out.println("\n========== String 常用方法 ==========");
        String s = "Hello, Java World!";

        // 长度
        System.out.println("length(): " + s.length());

        // 字符访问
        System.out.println("charAt(0): " + s.charAt(0));

        // 大小写转换
        System.out.println("toUpperCase: " + s.toUpperCase());
        System.out.println("toLowerCase: " + s.toLowerCase());

        // 去除首尾空白
        System.out.println("trim: [" + "  hello  ".trim() + "]");
        System.out.println("strip: [" + "  hello  ".strip() + "]");  // JDK 11+, 支持 Unicode 空白

        // 查找
        System.out.println("indexOf(\"Java\"): " + s.indexOf("Java"));
        System.out.println("lastIndexOf(\"o\"): " + s.lastIndexOf("o"));
        System.out.println("contains(\"World\"): " + s.contains("World"));

        // 开始/结束判断
        System.out.println("startsWith(\"Hello\"): " + s.startsWith("Hello"));
        System.out.println("endsWith(\"!\"): " + s.endsWith("!"));

        // 截取
        System.out.println("substring(7): " + s.substring(7));          // 从索引 7 到结尾
        System.out.println("substring(7, 11): " + s.substring(7, 11));  // 索引 [7, 11)

        // 替换
        System.out.println("replace: " + s.replace("Java", "Python"));

        // 分割
        String csv = "apple,banana,orange,grape";
        String[] fruits = csv.split(",");
        System.out.println("split 结果:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        // 比较
        System.out.println("\n比较：");
        System.out.println("equals: " + "abc".equals("abc"));               // 严格相等
        System.out.println("equalsIgnoreCase: " + "ABC".equalsIgnoreCase("abc"));  // 忽略大小写
        System.out.println("compareTo: " + "abc".compareTo("abd"));         // -1 (a-b 差 -1)

        // 是否为空
        System.out.println("isEmpty: " + "".isEmpty());                     // true
        System.out.println("isBlank: " + "   ".isBlank());                  // true (JDK 11+)

        // 判断空指针的最佳实践
        String maybeNull = null;
        System.out.println("null safe: " + "hello".equals(maybeNull));      // 常量在前避免NPE

        // 字符串转字符数组
        char[] arr = "Hello".toCharArray();
        System.out.println("字符数组长度: " + arr.length);

        // 静态方法
        System.out.println("String.valueOf(123): " + String.valueOf(123));
        System.out.println("String.valueOf(true): " + String.valueOf(true));

        // 重复字符串（JDK 11+）
        System.out.println("重复: " + "abc".repeat(3));                     // abcabcabc

        // ============ 4. String.format 格式化 ============
        System.out.println("\n========== String.format ==========");
        String formatted = String.format("姓名: %s, 年龄: %d, 身高: %.2f",
            "张三", 25, 1.756);
        System.out.println(formatted);

        // 常用占位符
        // %s  - 字符串
        // %d  - 整数
        // %f  - 浮点数（默认 6 位小数）
        // %.2f - 保留 2 位小数
        // %5d - 至少 5 位宽度
        // %-5d - 左对齐
        // %05d - 前导零补齐
        // %x  - 十六进制
        // %o  - 八进制
        // %b  - 布尔
        // %c  - 字符
        // %n  - 换行（跨平台）
        // %%  - 百分号

        System.out.printf("圆周率: %.4f%n", 3.14159);
        System.out.printf("宽度: [%10d]%n", 42);
        System.out.printf("左对齐: [%-10d]%n", 42);
        System.out.printf("补零: [%05d]%n", 42);
        System.out.printf("十六进制: %x%n", 255);

        // ============ 5. StringBuilder（推荐用于字符串拼接） ============
        System.out.println("\n========== StringBuilder ==========");
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");                     // 追加
        sb.append(", ");
        sb.append("Java!");
        System.out.println("拼接: " + sb);

        // 链式调用
        String result = new StringBuilder()
            .append("A")
            .append("B")
            .append("C")
            .toString();
        System.out.println("链式: " + result);

        // 常用方法
        StringBuilder sb2 = new StringBuilder("Hello");
        sb2.insert(0, "[");                     // 插入
        sb2.append("]");
        System.out.println("insert/append: " + sb2);

        sb2.reverse();                          // 反转
        System.out.println("reverse: " + sb2);

        sb2.reverse();                          // 还原
        sb2.delete(0, 1);                       // 删除范围
        sb2.deleteCharAt(sb2.length() - 1);
        System.out.println("delete: " + sb2);

        sb2.replace(0, 1, "H");                 // 替换
        System.out.println("replace: " + sb2);

        System.out.println("length: " + sb2.length());
        System.out.println("capacity: " + sb2.capacity());

        // 循环中逐步构造字符串时复用一个 StringBuilder，避免反复创建中间字符串。
        // 性能结论需要使用 JMH 在目标 JDK 和真实数据规模下验证。

        // ============ 6. StringBuffer（线程安全） ============
        System.out.println("\n========== StringBuffer ==========");
        StringBuffer sbf = new StringBuffer("Hello");
        sbf.append(" World");
        System.out.println("StringBuffer: " + sbf);
        // API 和 StringBuilder 完全一样
        // 区别：公开操作带同步语义；现代代码通常优先避免跨线程共享构建器
    }
}

/*
 * =============== String / StringBuilder / StringBuffer 对比 ===============
 *
 *                  可变性       并发语义
 * String           不可变       可安全共享
 * StringBuilder    可变         不提供同步
 * StringBuffer     可变         公开操作同步
 *
 * =============== 使用建议 ===============
 *
 * 1. 少量拼接：直接用 String 的 + 运算符（编译器优化为 StringBuilder）
 * 2. 循环中逐步拼接：通常复用 StringBuilder
 * 3. 多线程环境：优先避免共享构建器；确需共享时再设计同步策略
 * 4. JDK 15+ 大段文本：使用文本块（详见 TextBlocks.java）
 * 5. 判空首选：str == null || str.isEmpty() 或 str.isBlank()
 */
