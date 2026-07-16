/**
 * ModuleSystem.java - 模块系统（JDK 9+ Project Jigsaw）
 * <p>
 * 学习要点：
 * 1. 什么是模块（Module）
 * 2. module-info.java 描述文件
 * 3. 模块的声明和依赖
 * 4. exports / requires / opens / uses / provides
 * 5. 命名模块 vs 未命名模块
 * 6. 与 JDK 25 模块导入声明（JEP 511）的关系
 * <p>
 * 本文件说明模块系统概念，实际使用需要按模块化项目结构。
 */
public class ModuleSystem {

    public static void main(String[] args) {
        System.out.println("========== 模块系统概览 ==========");

        // 获取当前类所在的模块
        Module module = ModuleSystem.class.getModule();
        System.out.println("当前模块: " + module.getName());
        System.out.println("是命名模块: " + module.isNamed());

        // 获取 java.base 模块的信息
        Module base = String.class.getModule();
        System.out.println("\njava.base 模块:");
        System.out.println("  名字: " + base.getName());
        System.out.println("  Layer: " + base.getLayer());
        System.out.println("  descriptor: " + base.getDescriptor().version().orElse(null));

        // 列出 java.base 导出的部分包
        var descriptor = base.getDescriptor();
        System.out.println("\njava.base 导出的部分包:");
        descriptor.exports().stream().limit(10).forEach(e ->
            System.out.println("  " + e.source())
        );

        // 说明
        printModuleGuide();
    }

    static void printModuleGuide() {
        System.out.println("""

                ========== 模块系统详解 ==========

                【1. 什么是模块】
                模块是一组相关的包、资源、和一个描述文件（module-info.java）。
                模块系统解决了传统 classpath 的问题：
                - 依赖不明确（谁依赖谁？）
                - 缺乏封装（所有 public 都能访问）
                - 无法验证依赖（运行时才知道少 jar）

                【2. module-info.java 示例】

                模块声明文件，放在源码根目录：

                    module com.example.myapp {
                        // 依赖其他模块
                        requires java.base;         // 隐式的（可省略）
                        requires java.sql;
                        requires transitive java.logging;   // 传递依赖

                        // 导出包给其他模块
                        exports com.example.myapp.api;
                        exports com.example.myapp.util to com.friend.module;   // 限定导出

                        // 允许反射访问
                        opens com.example.myapp.model;
                        opens com.example.myapp.entity to org.hibernate.orm;

                        // 声明使用某服务
                        uses com.example.spi.MyService;

                        // 提供服务实现
                        provides com.example.spi.MyService
                            with com.example.myapp.impl.MyServiceImpl;
                    }

                【3. 关键字详解】

                module      模块声明
                requires    依赖的模块
                    static      编译时需要，运行时可选
                    transitive  传递给依赖我的模块
                exports     导出包（可被其他模块 import）
                    to          限定给指定模块
                opens       允许运行时深反射，与 exports 的普通访问语义不同
                    to          限定
                uses        使用某个服务接口（ServiceLoader）
                provides    提供服务实现
                    with        指定实现类

                【4. 常见 JDK 模块】

                java.base       核心（默认依赖，不用写）
                java.sql        JDBC
                java.xml        XML
                java.desktop    Swing/AWT
                java.logging    日志
                java.net.http   HTTP 客户端
                java.management JMX

                【5. 编译和运行】

                # 编译模块
                javac --module-source-path src -d out $(find src -name "*.java")

                # 运行
                java --module-path out --module com.example.myapp/com.example.Main

                # 简写
                java -p out -m com.example.myapp/com.example.Main

                【6. 与 classpath 的对比】

                classpath（传统）：
                - 所有 jar 平铺
                - public 类全局可见
                - 依赖靠约定

                modulepath（模块）：
                - 依赖明确声明
                - 只有 exports 的包对外可见
                - 编译期就验证依赖完整

                【7. 与 JDK 25 JEP 511 的关系】

                JEP 511 允许在 import 时直接引入整个模块：
                    import module java.base;

                这是使用模块的便捷语法，不是模块系统本身。
                核心的模块化概念仍然是 module-info.java。

                【8. 未命名模块】

                传统项目（无 module-info.java）自动属于"未命名模块"：
                - 可以读取所有其他模块（临时兼容）
                - 无法被命名模块 requires
                - 便于渐进式迁移

                【9. 使用建议】

                ✅ 什么时候用模块：
                - 大型项目多个组件
                - 提供 SDK / API 库
                - 需要强封装
                - 需要精细的依赖管理

                ⚠️ 什么时候不必用：
                - 单文件学习/试验（用 JEP 512 紧凑源文件更简单）
                - 简单的应用（保持传统 classpath 也无妨）
                - 现有大量依赖不支持模块的老库

                【10. 迁移路径】

                传统项目 → 模块化，分步骤：
                1. 保持 classpath 不变，正常工作
                2. 尝试将部分组件独立成"自动模块"（从 jar 名字生成）
                3. 编写 module-info.java，成为"命名模块"
                4. 优化 exports 和 requires
                """);
    }
}

/*
 * =============== 模块系统示例项目结构 ===============
 *
 * my-app/
 * ├── src/
 * │   ├── com.example.core/                    <- 模块名做目录
 * │   │   ├── module-info.java
 * │   │   └── com/example/core/
 * │   │       ├── Main.java
 * │   │       └── util/Utils.java
 * │   └── com.example.api/
 * │       ├── module-info.java
 * │       └── com/example/api/
 * │           └── Service.java
 * └── out/                                     <- 编译输出
 *
 * 模块间依赖：
 *   com.example.core 依赖 com.example.api
 *   com.example.api  只有一个接口
 *
 * =============== ServiceLoader 服务机制 ===============
 *
 * 模块系统内置服务发现机制：
 *
 * 1. 定义服务接口（在提供者模块）：
 *    package com.example.spi;
 *    public interface MyService { void run(); }
 *
 * 2. 服务提供者模块：
 *    module com.example.provider {
 *        requires com.example.spi;
 *        provides com.example.spi.MyService
 *            with com.example.provider.MyServiceImpl;
 *    }
 *
 * 3. 服务使用者：
 *    module com.example.consumer {
 *        requires com.example.spi;
 *        uses com.example.spi.MyService;
 *    }
 *
 * 4. 加载：
 *    ServiceLoader<MyService> loader = ServiceLoader.load(MyService.class);
 *    for (MyService s : loader) { s.run(); }
 *
 * =============== 常用工具 ===============
 *
 * jdeps           分析依赖，帮助生成 module-info.java
 * jmod            打包为 .jmod 文件
 * jlink           创建自定义运行时（只含需要的模块）
 * jpackage        打包为原生应用（含运行时）
 *
 * jdeps --generate-module-info out/ my-app.jar
 *
 * =============== 学习建议 ===============
 *
 * 1. 入门阶段：了解概念，不必强制使用
 * 2. 进阶阶段：给自己的库/项目添加 module-info
 * 3. 高级阶段：设计模块化架构、jlink 定制运行时
 *
 * 参考：
 * - The State of the Module System
 * - Understanding Java 9 Modules (Oracle 文档)
 */
