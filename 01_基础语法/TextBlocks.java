/**
 * TextBlocks.java - 文本块（Text Blocks，JDK 15+ 正式特性）
 * <p>
 * 学习要点：
 * 1. 文本块的语法（三个双引号 """）
 * 2. 缩进处理规则
 * 3. 转义字符
 * 4. 与传统字符串拼接的对比
 * 5. 文本块的实际用途
 */
public class TextBlocks {

    public static void main(String[] args) {

        // ============ 1. 传统字符串写多行文本 ============
        System.out.println("========== 传统写法 ==========");
        String htmlOld = "<html>\n" +
                         "    <body>\n" +
                         "        <p>Hello, World!</p>\n" +
                         "    </body>\n" +
                         "</html>";
        System.out.println(htmlOld);

        // ============ 2. 文本块写法（推荐） ============
        System.out.println("\n========== 文本块写法 ==========");
        String htmlNew = """
                <html>
                    <body>
                        <p>Hello, World!</p>
                    </body>
                </html>""";
        System.out.println(htmlNew);

        // 两种写法完全等价
        System.out.println("\n两种写法内容相等: " + htmlOld.equals(htmlNew));

        // ============ 3. 文本块的语法规则 ============
        System.out.println("\n========== 语法规则 ==========");

        // 规则一：开始的 """ 后必须换行，否则编译错误
        String correct = """
                text""";
        // String wrong = """text""";           // 编译错误！

        // 规则二：结束的 """ 位置决定缩进
        // 以结束 """ 所在列为基准，前面的空格会被移除
        String indent1 = """
                行一
                行二
                行三
                """;
        System.out.println("[" + indent1 + "]");

        String indent2 = """
                        行一
                        行二
                    行三
                """;
        // 最左侧的行（"行三"）决定缩进——前面的4个空格被去掉
        System.out.println("[" + indent2 + "]");

        // ============ 4. 转义字符 ============
        System.out.println("\n========== 转义字符 ==========");

        // 文本块中可以自然写双引号
        String withQuote = """
                她说："你好"，然后离开了。""";
        System.out.println(withQuote);

        // \\ 反斜杠
        String path = """
                C:\\Users\\Admin""";
        System.out.println("路径: " + path);

        // 新增转义序列
        // \s - 显式空格（保留末尾空格）
        String withSpace = """
                Line1   \s
                Line2""";
        System.out.println("含尾部空格: [" + withSpace + "]");

        // \ 行末尾——不换行（连接到下一行）
        String longLine = """
                这是很长的一行，\
                但是不换行，\
                拼接成一行。""";
        System.out.println("长行连接: " + longLine);

        // ============ 5. 实际应用场景 ============
        System.out.println("\n========== 实际应用 ==========");

        // 场景一：SQL 语句
        String sql = """
                SELECT id, name, age
                FROM users
                WHERE age > 18
                  AND status = 'active'
                ORDER BY created_at DESC
                LIMIT 10
                """;
        System.out.println("SQL:");
        System.out.println(sql);

        // 场景二：JSON
        String json = """
                {
                    "name": "张三",
                    "age": 25,
                    "email": "zhang@example.com",
                    "hobbies": ["编程", "阅读", "跑步"]
                }
                """;
        System.out.println("JSON:");
        System.out.println(json);

        // 场景三：XML/HTML
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <config>
                    <server host="localhost" port="8080"/>
                    <database url="jdbc:mysql://localhost/mydb"/>
                </config>
                """;
        System.out.println("XML:");
        System.out.println(xml);

        // 场景四：命令行帮助信息
        String help = """
                Usage: myapp [options] <input>

                Options:
                  -h, --help       显示此帮助信息
                  -v, --version    显示版本
                  -o, --output     输出文件
                  -q, --quiet      静默模式
                """;
        System.out.println("Help:");
        System.out.println(help);

        // ============ 6. 文本块与 String.format 结合 ============
        System.out.println("\n========== 与 format 结合 ==========");

        String template = """
                姓名: %s
                年龄: %d
                邮箱: %s
                """;
        String info = String.format(template, "李四", 30, "li@example.com");
        System.out.println(info);

        // JDK 15+ formatted 方法（实例方法，更简洁）
        String info2 = """
                商品: %s
                价格: %.2f 元
                库存: %d
                """.formatted("苹果", 3.5, 100);
        System.out.println(info2);

        // ============ 7. 常用方法（继承自 String） ============
        System.out.println("\n========== 常用方法 ==========");
        String block = """
                Line 1
                Line 2
                Line 3
                """;
        System.out.println("行数: " + block.lines().count());   // 3

        // lines() 返回 Stream<String>，可以流式处理
        block.lines().forEach(line -> System.out.println("- " + line));

        // stripIndent() 手动去除公共缩进
        String withIndent = """
                    hello
                    world""";
        System.out.println("stripIndent 后:");
        System.out.println(withIndent);      // 编译时已处理

        // ============ 8. 编译时常量 ============
        // 文本块也是常量，会进入字符串常量池
        String a = """
                abc""";
        String b = "abc";
        System.out.println("\n文本块也是常量: " + (a == b));    // true
    }
}

/*
 * =============== 文本块的优点 ===============
 *
 * 1. 消除大量的 \n 和 \" 转义
 * 2. 可读性大大提升，所见即所得
 * 3. 缩进自动处理
 * 4. 编译期常量，性能无损失
 *
 * =============== 何时使用 ===============
 *
 * 使用：SQL、JSON、XML、HTML、命令帮助、多行错误信息、脚本
 * 不用：单行字符串（用普通 String 即可）
 *
 * =============== 转义速查 ===============
 *
 * \s     空格（防止被 trim）
 * \      行末的换行连接
 * \n     换行
 * \"     双引号（文本块中通常不需要）
 * \\     反斜杠
 */
