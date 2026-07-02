import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * NIO.java - NIO.2 现代文件 API（JDK 7+，java.nio.file）
 * <p>
 * 学习要点：
 * 1. Path 路径抽象
 * 2. Files 工具类（推荐）
 * 3. 文件读写的现代方式
 * 4. 目录操作
 * 5. 文件属性
 * 6. 文件监听（WatchService）
 * 7. Stream + Files 的组合
 */
public class NIO {

    public static void main(String[] args) throws IOException {

        // ============ 1. Path 路径抽象 ============
        System.out.println("========== Path ==========");

        // 创建 Path
        Path p1 = Paths.get("/tmp/test.txt");
        Path p2 = Path.of("/tmp/test.txt");                     // 更简洁（JDK 11+）
        Path p3 = Path.of("/tmp", "sub", "file.txt");           // 分段

        System.out.println("路径: " + p1);
        System.out.println("绝对: " + p1.toAbsolutePath());
        System.out.println("文件名: " + p1.getFileName());
        System.out.println("父目录: " + p1.getParent());
        System.out.println("根: " + p1.getRoot());
        System.out.println("元素数: " + p1.getNameCount());

        // Path 操作
        Path resolved = Path.of("/tmp").resolve("data/file.txt");
        System.out.println("resolve: " + resolved);

        Path relative = Path.of("/tmp/a/b").relativize(Path.of("/tmp/a/b/c/d.txt"));
        System.out.println("relativize: " + relative);

        Path normalized = Path.of("/tmp/./a/../b/./c").normalize();
        System.out.println("normalize: " + normalized);

        // ============ 2. 文件读写（一次性，小文件）============
        System.out.println("\n========== 简单读写 ==========");
        Path file = Path.of("/tmp/nio_test.txt");

        // 写入字符串
        Files.writeString(file, "Hello NIO.2\n第二行\n");

        // 追加
        Files.writeString(file, "追加内容\n", StandardOpenOption.APPEND);

        // 读取为字符串（JDK 11+）
        String content = Files.readString(file);
        System.out.println("内容: " + content);

        // 读取为行的列表
        List<String> lines = Files.readAllLines(file);
        System.out.println("行数: " + lines.size());
        lines.forEach(l -> System.out.println("  " + l));

        // 写入行列表
        Files.write(Path.of("/tmp/lines.txt"), List.of("行1", "行2", "行3"));

        // 字节读写
        byte[] bytes = Files.readAllBytes(file);
        Files.write(Path.of("/tmp/bytes.txt"), bytes);

        // ============ 3. Stream 方式读取（推荐大文件）============
        System.out.println("\n========== Stream 读取 ==========");
        try (Stream<String> stream = Files.lines(file)) {
            long count = stream
                .filter(l -> !l.isBlank())
                .peek(l -> System.out.println("处理: " + l))
                .count();
            System.out.println("有效行数: " + count);
        }

        // ============ 4. 文件操作 ============
        System.out.println("\n========== 文件操作 ==========");

        // 创建目录
        Path dir = Path.of("/tmp/nio_dir");
        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }

        // 创建多级目录
        Path deep = Path.of("/tmp/nio_dir/a/b/c");
        Files.createDirectories(deep);

        // 复制
        Path src = Path.of("/tmp/nio_test.txt");
        Path dst = Path.of("/tmp/nio_dir/copy.txt");
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("已复制到: " + dst);

        // 移动/重命名
        Path moved = Path.of("/tmp/nio_dir/moved.txt");
        Files.move(dst, moved, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("已移动到: " + moved);

        // 删除
        Files.deleteIfExists(moved);
        Files.deleteIfExists(deep);

        // ============ 5. 文件属性 ============
        System.out.println("\n========== 文件属性 ==========");
        Path attr = file;

        System.out.println("存在: " + Files.exists(attr));
        System.out.println("大小: " + Files.size(attr) + " 字节");
        System.out.println("可读: " + Files.isReadable(attr));
        System.out.println("可写: " + Files.isWritable(attr));
        System.out.println("可执行: " + Files.isExecutable(attr));
        System.out.println("是普通文件: " + Files.isRegularFile(attr));
        System.out.println("是目录: " + Files.isDirectory(attr));
        System.out.println("最后修改: " + Files.getLastModifiedTime(attr));

        // 批量获取属性
        BasicFileAttributes ba = Files.readAttributes(attr, BasicFileAttributes.class);
        System.out.println("创建时间: " + ba.creationTime());
        System.out.println("大小: " + ba.size());

        // ============ 6. 遍历目录 ============
        System.out.println("\n========== 遍历目录 ==========");
        Path tmp = Path.of("/tmp");

        // 只列出直接子文件（不递归）
        try (Stream<Path> paths = Files.list(tmp)) {
            long fileCount = paths
                .filter(Files::isRegularFile)
                .count();
            System.out.println("/tmp 下的文件数: " + fileCount);
        }

        // 递归遍历（walk）
        try (Stream<Path> paths = Files.walk(tmp, 2)) {         // 深度 2
            long javaFiles = paths
                .filter(p -> p.toString().endsWith(".java"))
                .count();
            System.out.println("/tmp 及子目录中的 .java 文件: " + javaFiles);
        }

        // 查找特定文件
        try (Stream<Path> found = Files.find(
                tmp, 3,
                (p, a) -> a.isRegularFile() && p.toString().endsWith(".txt"))) {
            System.out.println("找到的 .txt 文件（前 5 个）:");
            found.limit(5).forEach(p -> System.out.println("  " + p));
        }

        // ============ 7. 文件监听 WatchService ============
        System.out.println("\n========== WatchService（演示，非阻塞）==========");
        Path watchDir = Path.of("/tmp");
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            watchDir.register(watcher,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("监听 /tmp 目录变化（生产中会用循环阻塞获取 key）");
            // 生产使用：
            // while (true) {
            //     WatchKey key = watcher.take();          // 阻塞
            //     for (WatchEvent<?> event : key.pollEvents()) {
            //         System.out.println(event.kind() + ": " + event.context());
            //     }
            //     key.reset();
            // }
        }

        // ============ 8. 与老 API 互转 ============
        System.out.println("\n========== 老新 API 转换 ==========");
        java.io.File oldFile = new java.io.File("/tmp/test.txt");
        Path fromFile = oldFile.toPath();                       // File → Path
        java.io.File toFile = fromFile.toFile();                // Path → File
        System.out.println("File ↔ Path 互转正常");

        // ============ 9. 综合示例：统计 .java 文件行数 ============
        System.out.println("\n========== 统计代码行数 ==========");
        Path start = Path.of("/Users/loucongcong/devData/java-learn/jdk25");
        if (Files.exists(start)) {
            try (Stream<Path> paths = Files.walk(start)) {
                long total = paths
                    .filter(p -> p.toString().endsWith(".java"))
                    .flatMap(p -> {
                        try {
                            return Files.lines(p);
                        } catch (IOException e) {
                            return Stream.empty();
                        }
                    })
                    .filter(l -> !l.isBlank())
                    .count();
                System.out.println("学习项目非空代码行数: " + total);
            }
        }
    }
}

/*
 * =============== java.io vs java.nio.file ===============
 *
 *                      java.io                java.nio.file
 * 表示路径              File                  Path
 * 主要 API             流（Stream）           工具方法（Files）
 * 异常                 通用 IOException       更具体
 * 属性访问             有限                   丰富（POSIX 等）
 * 目录监听             无                     WatchService
 * 大文件               需手工缓冲             Files.lines + Stream
 * 推荐度               老 API                 首选
 *
 * =============== Path 常用方法 ===============
 *
 * getFileName()       文件名
 * getParent()         父目录
 * getRoot()           根
 * getNameCount()      元素数
 * getName(i)          第 i 段
 * subpath(a, b)       子路径
 * resolve(other)      拼接
 * relativize(other)   相对路径
 * normalize()         规范化（去掉 .、..）
 * toAbsolutePath()    绝对路径
 * toRealPath()        真实路径（解析符号链接）
 *
 * =============== Files 常用方法 ===============
 *
 * 【存在性/属性】
 *   exists, notExists, isRegularFile, isDirectory
 *   size, getLastModifiedTime, isReadable/Writable/Executable
 *
 * 【读】
 *   readString(path)                 读全部字符串
 *   readAllLines(path)               读全部行
 *   readAllBytes(path)               读字节
 *   lines(path)                      Stream 逐行
 *   newBufferedReader(path)          BufferedReader
 *
 * 【写】
 *   writeString(path, str, opts...)  写字符串
 *   write(path, bytes)               写字节
 *   write(path, iterable, opts...)   写多行
 *   newBufferedWriter(path)          BufferedWriter
 *
 * 【文件操作】
 *   createFile, createDirectory, createDirectories
 *   copy, move
 *   delete, deleteIfExists
 *
 * 【遍历】
 *   list(dir)          直接子项 Stream<Path>
 *   walk(dir, depth)   递归 Stream<Path>
 *   find(dir, depth, matcher)  查找
 *
 * =============== OpenOption 常用值 ===============
 *
 * StandardOpenOption.CREATE          不存在则创建
 * StandardOpenOption.CREATE_NEW      不存在则创建，存在则抛错
 * StandardOpenOption.APPEND          追加
 * StandardOpenOption.TRUNCATE_EXISTING  截断
 * StandardOpenOption.READ            读
 * StandardOpenOption.WRITE           写
 * StandardOpenOption.SYNC            同步到磁盘
 *
 * =============== 使用建议 ===============
 *
 * 1. 优先使用 Files 工具类
 * 2. 小文件：Files.readString/writeString
 * 3. 大文件：Files.lines + Stream
 * 4. 遍历：Files.walk + Stream
 * 5. try-with-resources 关闭 Stream
 * 6. 显式指定编码（Charset）
 */
