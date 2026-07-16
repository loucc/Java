import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * FileIO.java - 传统 Java IO（java.io）
 * <p>
 * 学习要点：
 * 1. File 类
 * 2. 字节流：InputStream / OutputStream
 * 3. 字符流：Reader / Writer
 * 4. 缓冲流：BufferedReader / BufferedWriter
 * 5. 转换流：InputStreamReader / OutputStreamWriter
 * 6. 打印流：PrintStream / PrintWriter
 * 7. 对象流：ObjectInputStream / ObjectOutputStream
 * <p>
 * 注意：现代 Java 更推荐用 java.nio.file（详见 NIO.java）
 */
public class FileIO {

    public static void main(String[] args) throws IOException {

        // ============ 1. File 类 ============
        System.out.println("========== File 类 ==========");
        File file = new File("/tmp/java_learn_test.txt");
        System.out.println("路径: " + file.getPath());
        System.out.println("绝对路径: " + file.getAbsolutePath());
        System.out.println("存在: " + file.exists());
        System.out.println("是文件: " + file.isFile());
        System.out.println("父目录: " + file.getParent());

        // 创建目录
        File dir = new File("/tmp/java_learn_dir");
        if (!dir.exists()) {
            dir.mkdirs();                       // mkdirs 递归创建
            System.out.println("目录已创建: " + dir);
        }

        // 列出文件
        File tmp = new File("/tmp");
        String[] files = tmp.list();
        if (files != null) {
            System.out.println("/tmp 下文件数: " + files.length);
        }

        // ============ 2. 字节输出流 FileOutputStream ============
        System.out.println("\n========== FileOutputStream ==========");
        try (FileOutputStream fos = new FileOutputStream("/tmp/bytes.txt")) {
            fos.write(72);                     // 写单个字节 H
            fos.write("ello\n".getBytes(StandardCharsets.UTF_8));
            fos.write("你好\n".getBytes(StandardCharsets.UTF_8));
            System.out.println("字节写入完成");
        }

        // ============ 3. 字节输入流 FileInputStream ============
        System.out.println("\n========== FileInputStream ==========");
        try (FileInputStream fis = new FileInputStream("/tmp/bytes.txt")) {
            byte[] buffer = new byte[1024];
            int n = fis.read(buffer);
            String content = new String(buffer, 0, n, StandardCharsets.UTF_8);
            System.out.println("读取内容: " + content);
        }

        // ============ 4. 字符流 FileWriter / FileReader ============
        System.out.println("\n========== FileWriter/FileReader ==========");
        try (FileWriter fw = new FileWriter("/tmp/text.txt", StandardCharsets.UTF_8)) {
            fw.write("你好，Java IO\n");
            fw.write("这是第二行\n");
            fw.append("追加的内容");
        }

        try (FileReader fr = new FileReader("/tmp/text.txt", StandardCharsets.UTF_8)) {
            char[] buf = new char[1024];
            int n = fr.read(buf);
            System.out.println("读取: " + new String(buf, 0, n));
        }

        // ============ 5. 缓冲流（推荐，效率高） ============
        System.out.println("\n========== BufferedWriter/Reader ==========");
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("/tmp/buffered.txt", StandardCharsets.UTF_8))) {
            bw.write("第一行");
            bw.newLine();                       // 跨平台换行
            bw.write("第二行");
            bw.newLine();
            bw.write("第三行");
        }

        // 按行读取（最常用）
        try (BufferedReader br = new BufferedReader(
                new FileReader("/tmp/buffered.txt", StandardCharsets.UTF_8))) {
            String line;
            int lineNo = 1;
            while ((line = br.readLine()) != null) {
                System.out.println((lineNo++) + ": " + line);
            }
        }

        // BufferedReader 的 lines() 方法（返回 Stream）
        try (BufferedReader br = new BufferedReader(
                new FileReader("/tmp/buffered.txt", StandardCharsets.UTF_8))) {
            br.lines().forEach(l -> System.out.println("流: " + l));
        }

        // ============ 6. 指定字符编码：转换流 ============
        System.out.println("\n========== 指定编码 ==========");
        // 写入时指定 UTF-8
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream("/tmp/utf8.txt"), StandardCharsets.UTF_8))) {
            bw.write("UTF-8 编码测试：中文字符");
        }

        // 读取时指定 UTF-8
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("/tmp/utf8.txt"), StandardCharsets.UTF_8))) {
            System.out.println("读取: " + br.readLine());
        }

        // ============ 7. PrintWriter（方便的字符输出） ============
        System.out.println("\n========== PrintWriter ==========");
        try (PrintWriter pw = new PrintWriter(
                new FileWriter("/tmp/print.txt", StandardCharsets.UTF_8))) {
            pw.println("这是 println");
            pw.printf("格式化: %s = %d%n", "age", 25);
            pw.print("不换行");
        }

        // System.out 就是 PrintStream
        System.out.println("System.out 是 PrintStream: " + (System.out instanceof PrintStream));

        // ============ 8. 对象序列化 ============
        System.out.println("\n========== 对象序列化 ==========");
        Employee emp = new Employee("张三", 25, 15000.0);

        // 写入对象
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("/tmp/employee.dat"))) {
            oos.writeObject(emp);
            System.out.println("对象已序列化");
        }

        // 读取对象
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("/tmp/employee.dat"))) {
            Employee read = (Employee) ois.readObject();
            System.out.println("读取的对象: " + read);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // ============ 9. Scanner 读取输入 ============
        System.out.println("\n========== Scanner ==========");
        try (Scanner scanner = new Scanner(new File("/tmp/buffered.txt"))) {
            while (scanner.hasNextLine()) {
                System.out.println("Scanner: " + scanner.nextLine());
            }
        }

        // Scanner 也可以读键盘输入
        // Scanner input = new Scanner(System.in);
        // String line = input.nextLine();

        // ============ 10. 复制文件（字节流） ============
        System.out.println("\n========== 复制文件 ==========");
        copyFile("/tmp/text.txt", "/tmp/text_copy.txt");
        System.out.println("复制完成");
    }

    // 使用缓冲流复制文件
    static void copyFile(String src, String dst) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst))) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, n);
            }
        }
    }
}

// 需要实现 Serializable 才能序列化
class Employee implements Serializable {

    // 建议显式声明 serialVersionUID
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private double salary;

    // transient 字段不会被序列化
    private transient String password;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{name=" + name + ", age=" + age + ", salary=" + salary + "}";
    }
}

/*
 * =============== Java IO 分类 ===============
 *
 * 按方向：
 *   输入流：Input / Reader
 *   输出流：Output / Writer
 *
 * 按单位：
 *   字节流：InputStream / OutputStream (处理二进制)
 *   字符流：Reader / Writer (处理文本)
 *
 * 按功能：
 *   节点流：直接连接数据源（FileInputStream 等）
 *   处理流：包装其他流（BufferedInputStream 等）
 *
 * =============== 常用流的关系（装饰器模式） ===============
 *
 * 字节流：
 *   InputStream/OutputStream (抽象)
 *     ├── FileInputStream/FileOutputStream (文件)
 *     ├── ByteArrayInputStream/ByteArrayOutputStream (内存)
 *     ├── BufferedInputStream/BufferedOutputStream (缓冲)
 *     ├── DataInputStream/DataOutputStream (基本类型)
 *     ├── ObjectInputStream/ObjectOutputStream (对象)
 *     └── PrintStream (System.out)
 *
 * 字符流：
 *   Reader/Writer (抽象)
 *     ├── FileReader/FileWriter (文件)
 *     ├── CharArrayReader/CharArrayWriter (内存)
 *     ├── StringReader/StringWriter (字符串)
 *     ├── BufferedReader/BufferedWriter (缓冲)
 *     ├── InputStreamReader/OutputStreamWriter (字节转字符)
 *     └── PrintWriter (打印)
 *
 * =============== 使用建议 ===============
 *
 * 1. 文本文件用字符流，二进制文件用字节流
 * 2. 频繁小块读写时考虑缓冲流；Files 等高层 API 可能已处理缓冲
 * 3. 自己打开的可关闭资源优先用 try-with-resources
 * 4. 显式指定字符编码（如 UTF-8）
 * 5. 现代代码优先用 java.nio.file.Files（详见 NIO.java）
 * 6. 序列化考虑安全性（避免不可信数据）
 *
 * =============== Serializable 序列化要点 ===============
 *
 * 1. 类必须实现 Serializable 接口（标记接口）
 * 2. 建议声明 serialVersionUID
 * 3. transient 字段不参与序列化
 * 4. static 字段不参与序列化（属于类）
 * 5. 父类字段：父类实现 Serializable 才会被序列化
 * 6. 反序列化不调用构造器
 */
