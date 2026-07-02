/**
 * Variables.java - Java 变量和数据类型
 * <p>
 * 学习要点：
 * 1. 变量的声明和初始化
 * 2. Java 的 8 种基本数据类型
 * 3. 引用数据类型
 * 4. 类型转换（隐式转换和强制转换）
 * 5. 常量 final
 * 6. var 关键字（JDK 10+）
 */
public class Variables {

    // 静态常量（类变量）——大写字母加下划线
    public static final double PI = 3.14159;
    public static final String LANGUAGE = "Java";

    public static void main(String[] args) {

        // ============ 1. 8 种基本数据类型 ============

        // 整数类型
        byte b = 127;                       // 1 字节，范围 -128 ~ 127
        short s = 32767;                    // 2 字节，范围 -32768 ~ 32767
        int i = 2147483647;                 // 4 字节，最常用的整数类型
        long l = 9223372036854775807L;      // 8 字节，注意后缀 L

        // 浮点类型
        float f = 3.14F;                    // 4 字节，注意后缀 F
        double d = 3.141592653589793;       // 8 字节，默认浮点类型

        // 字符类型
        char c = 'A';                       // 2 字节，Unicode，注意单引号
        char cn = '中';                      // 支持中文
        char unicode = 'A';            // Unicode 表示（'A'）

        // 布尔类型
        boolean flag = true;                // 只有 true 和 false

        System.out.println("byte    = " + b);
        System.out.println("short   = " + s);
        System.out.println("int     = " + i);
        System.out.println("long    = " + l);
        System.out.println("float   = " + f);
        System.out.println("double  = " + d);
        System.out.println("char    = " + c + ", " + cn + ", " + unicode);
        System.out.println("boolean = " + flag);

        // ============ 2. 引用数据类型 ============

        // String 是最常用的引用类型
        String name = "张三";
        String greeting = "你好, " + name;
        System.out.println(greeting);

        // 数组也是引用类型
        int[] arr = {1, 2, 3, 4, 5};
        System.out.println("数组第一个元素: " + arr[0]);

        // ============ 3. 类型转换 ============

        // 自动类型转换（小 → 大）
        int intVal = 100;
        long longVal = intVal;              // int → long 自动转换
        double doubleVal = longVal;         // long → double 自动转换
        System.out.println("自动转换: " + intVal + " → " + longVal + " → " + doubleVal);

        // 强制类型转换（大 → 小，可能损失精度）
        double dv = 3.99;
        int iv = (int) dv;                  // 强转，直接截断，不四舍五入
        System.out.println("强制转换: " + dv + " → " + iv);    // 结果是 3

        // 表达式自动提升
        byte b1 = 10;
        byte b2 = 20;
        int result = b1 + b2;               // byte + byte 结果自动提升为 int
        System.out.println("表达式提升: " + result);

        // ============ 4. 常量 final ============

        final int MAX_SIZE = 100;           // 常量，不可再修改
        // MAX_SIZE = 200;                  // 编译错误！

        System.out.println("常量 PI = " + PI);
        System.out.println("常量 MAX_SIZE = " + MAX_SIZE);

        // ============ 5. var 关键字（JDK 10+ 局部变量类型推断） ============

        // 编译器会根据右侧的值推断类型
        var age = 25;                       // 推断为 int
        var pi = 3.14;                      // 推断为 double
        var text = "Hello";                 // 推断为 String
        var list = new java.util.ArrayList<String>();  // 推断为 ArrayList<String>

        System.out.println("var age = " + age + " (类型: " + ((Object) age).getClass().getSimpleName() + ")");
        System.out.println("var text = " + text);

        // 注意：var 只能用于局部变量，且必须初始化
        // var x;                           // 错误！
        // var y = null;                    // 错误！无法推断类型

        // ============ 6. 数字表示形式 ============

        int decimal = 100;                  // 十进制
        int binary = 0b1100100;             // 二进制，以 0b 开头
        int octal = 0144;                   // 八进制，以 0 开头
        int hex = 0x64;                     // 十六进制，以 0x 开头
        int underscore = 1_000_000;         // 数字下划线分隔，提高可读性

        System.out.println("十进制:   " + decimal);
        System.out.println("二进制:   " + binary);
        System.out.println("八进制:   " + octal);
        System.out.println("十六进制: " + hex);
        System.out.println("百万:     " + underscore);

        // ============ 7. 字符转义 ============

        System.out.println("换行:\n下一行");
        System.out.println("制表:\tTab");
        System.out.println("双引号: \"引号内\"");
        System.out.println("反斜杠: \\");
    }
}

/*
 * =============== 各类型取值范围（重要） ===============
 *
 * byte    : -128 ~ 127                          (2^7)
 * short   : -32,768 ~ 32,767                    (2^15)
 * int     : -2,147,483,648 ~ 2,147,483,647      (2^31)
 * long    : -2^63 ~ 2^63-1                      (超大数字，注意后缀 L)
 * float   : 约 ±3.4E38，7 位有效数字
 * double  : 约 ±1.8E308，15 位有效数字
 * char    : 0 ~ 65535                            (Unicode)
 * boolean : true / false
 *
 * =============== 命名规范 ===============
 *
 * 1. 变量名：小驼峰（camelCase）：userName, totalCount
 * 2. 常量名：全大写下划线：MAX_SIZE, DEFAULT_NAME
 * 3. 类名：大驼峰（PascalCase）：UserManager
 * 4. 包名：全小写：com.example.util
 */
