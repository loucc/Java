/*
 * ModuleImport.java - 模块导入声明（JDK 25 JEP 511 正式特性）
 * <p>
 * 学习要点：
 * 1. 模块导入声明的语法
 * 2. 一次性导入整个模块的所有导出包
 * 3. 简化学习和快速原型
 * 4. 与传统 import 的对比
 * <p>
 * 注意：模块导入声明是 JDK 25 正式特性，无需预览标志。
 */

// JDK 25 新写法（JEP 511）：一行导入整个模块的所有导出类型
// 等价于导入了 java.util.*、java.io.*、java.lang.*、java.time.* 等所有 java.base 导出包
import module java.base;

// 也可与普通 import 混用（需要某些非 java.base 的类型时）
// import java.util.stream.Collectors;

public class ModuleImport {

    public static void main(String[] args) {

        // ============ 1. 传统 import 的痛点 ============
        System.out.println("========== 传统 import ==========");
        System.out.println("每引入一个新类型，就要多一行 import");
        System.out.println("学习者面对大量 import 感到困惑");

        // 使用需要 4 个 import 的类
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);

        System.out.println("list: " + list);
        System.out.println("map: " + map);

        // ============ 2. 模块导入的写法 ============
        System.out.println("\n========== 模块导入示例 ==========");
        System.out.println("""

                import module java.base;
                // 上面一行相当于导入了 java.base 模块中所有导出的类型：
                //   java.util.*, java.io.*, java.lang.*,
                //   java.time.*, java.net.*, java.nio.*,
                //   等等

                public class Demo {
                    public static void main(String[] args) {
                        List<Integer> list = new ArrayList<>();
                        Map<String, Integer> map = new HashMap<>();
                        var now = LocalDateTime.now();
                        var path = Path.of("/tmp");
                        // 无需任何显式 import！
                    }
                }
                """);

        // ============ 3. 语法规则 ============
        System.out.println("\n========== 语法规则 ==========");
        System.out.println("""
                import module <模块名>;

                示例：
                    import module java.base;      // 核心模块（最常用）
                    import module java.desktop;   // GUI（Swing/AWT）
                    import module java.sql;       // 数据库
                    import module java.net.http;  // HTTP 客户端
                    import module java.xml;       // XML

                可以与普通 import 混用：
                    import module java.base;
                    import java.util.stream.Collectors;  // 需要哪些额外类型
                """);

        // ============ 4. 常用模块速查 ============
        System.out.println("\n========== 常用模块 ==========");
        System.out.println("""
                模块名                          主要内容
                java.base                       所有核心类（util、io、lang、time...）
                java.desktop                    Swing、AWT
                java.sql                        JDBC
                java.net.http                   HttpClient
                java.xml                        XML 处理
                java.logging                    Logger
                java.management                 JMX
                jdk.httpserver                  简易 HTTP 服务器
                """);

        // ============ 5. 命名冲突 ============
        System.out.println("\n========== 命名冲突 ==========");
        System.out.println("""
                当 module 导入多个模块，有同名类时：
                - 用类的全限定名解决冲突
                - 例如 java.util.Date 和 java.sql.Date：

                    import module java.base;
                    import module java.sql;

                    java.util.Date d1 = new java.util.Date();
                    java.sql.Date d2 = new java.sql.Date(0);
                """);
    }
}

/*
 * =============== JEP 511：模块导入声明 ===============
 *
 * JDK 25 正式特性。
 *
 * 语法：import module <module-name>;
 *
 * 效果：等价于导入了该模块所有导出的包中所有 public 类型
 *
 * =============== 与紧凑源文件的协同 ===============
 *
 * 紧凑源文件（JEP 512）自动隐式导入 java.base 模块，
 * 所以下面的紧凑源文件不需要任何 import：
 *
 *     void main() {
 *         var list = List.of(1, 2, 3);   // 无需 import
 *         var map  = Map.of("a", 1);
 *         var now  = java.time.LocalDateTime.now();
 *     }
 *
 * 这大大简化了入门 Java 的过程。
 *
 * =============== 使用建议 ===============
 *
 * ✅ 适合场景：
 * - 教学示例
 * - 快速原型
 * - 单文件工具/脚本
 * - REPL / JShell 风格代码
 *
 * ⚠️ 谨慎使用：
 * - 大型代码库：显式 import 更清晰，便于工具分析
 * - 团队协作：容易混淆类型来源
 * - 生产代码：更推荐传统 import
 *
 * 简单说：
 *   - 学习/试验 → module 导入
 *   - 生产/协作 → 显式 import
 *
 * =============== 演进 ===============
 *
 * JDK 23  JEP 476 首次预览
 * JDK 24  JEP 494 第二次预览
 * JDK 25  JEP 511 正式
 *
 * =============== 相关知识：Java 模块系统 ===============
 *
 * 模块（Module）是 JDK 9 引入的概念，
 * 每个模块由 module-info.java 描述其依赖和导出。
 *
 * 完整的模块系统详见 08_高级特性/ModuleSystem.java
 */
