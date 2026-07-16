import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.Map;

/**
 * MyHttpClient.java - 现代 HTTP 客户端（JDK 11+）
 * <p>
 * 学习要点：
 * 1. HttpClient 的构建
 * 2. GET / POST / PUT / DELETE 请求
 * 3. 同步和异步请求
 * 4. 请求头设置
 * 5. 请求体（JSON、表单）
 * 6. 处理响应（状态码、头、Body）
 * 7. HTTP/2 支持
 */
public class MyHttpClient {

    public static void main(String[] args) throws Exception {

        // ============ 1. 创建 HttpClient ============
        System.out.println("========== 创建 HttpClient ==========");
        HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)            // HTTP/2
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)   // 自动重定向
            // .proxy(ProxySelector.of(new InetSocketAddress("proxy.host", 8080)))
            .build();

        // ============ 2. GET 请求 ============
        System.out.println("\n========== GET 请求 ==========");
        HttpRequest getRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/get"))
            .header("User-Agent", "Java-25-Learning")
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
            .GET()                                          // 默认就是 GET
            .build();

        // 同步发送
        HttpResponse<String> resp = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("状态码: " + resp.statusCode());
        System.out.println("HTTP 版本: " + resp.version());
        System.out.println("响应头 (部分):");
        resp.headers().map().entrySet().stream()
            .limit(3)
            .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue()));
        System.out.println("Body (前200字符): " + resp.body().substring(0, Math.min(200, resp.body().length())));

        // ============ 3. POST 请求（JSON） ============
        System.out.println("\n========== POST JSON ==========");
        String json = """
                {
                    "name": "张三",
                    "age": 25
                }
                """;

        HttpRequest postJson = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/post"))
            .header("Content-Type", "application/json; charset=utf-8")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> postResp = client.send(postJson, BodyHandlers.ofString());
        System.out.println("状态: " + postResp.statusCode());
        System.out.println("Body 长度: " + postResp.body().length());

        // ============ 4. POST 表单 ============
        System.out.println("\n========== POST 表单 ==========");
        Map<String, String> form = Map.of("username", "test", "password", "123456");
        String formData = form.entrySet().stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .reduce((a, b) -> a + "&" + b)
            .orElse("");

        HttpRequest postForm = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/post"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formData))
            .build();

        HttpResponse<String> formResp = client.send(postForm, BodyHandlers.ofString());
        System.out.println("状态: " + formResp.statusCode());

        // ============ 5. PUT / DELETE ============
        System.out.println("\n========== PUT / DELETE ==========");
        HttpRequest put = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/put"))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString("{\"update\": true}"))
            .build();

        HttpRequest delete = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/delete"))
            .DELETE()
            .build();

        System.out.println("PUT: " + client.send(put, BodyHandlers.ofString()).statusCode());
        System.out.println("DELETE: " + client.send(delete, BodyHandlers.ofString()).statusCode());

        // ============ 6. 异步请求 ============
        System.out.println("\n========== 异步请求 ==========");
        CompletableFuture<HttpResponse<String>> future = client.sendAsync(
            getRequest, BodyHandlers.ofString());

        future.thenApply(HttpResponse::body)
            .thenAccept(body -> System.out.println("异步响应长度: " + body.length()))
            .join();                                        // 等待完成

        // ============ 7. 并发多个请求 ============
        System.out.println("\n========== 并发请求 ==========");
        var urls = new String[]{
            "https://httpbin.org/delay/1",
            "https://httpbin.org/delay/1",
            "https://httpbin.org/delay/1"
        };

        var futures = java.util.Arrays.stream(urls)
            .map(url -> HttpRequest.newBuilder().uri(URI.create(url)).build())
            .map(req -> client.sendAsync(req, BodyHandlers.ofString()))
            .toList();
        CompletableFuture<?>[] futureArray = futures.toArray(CompletableFuture<?>[]::new);
        CompletableFuture.allOf(futureArray).join();
        System.out.println("并发请求全部完成，状态码: "
            + futures.stream().map(f -> f.join().statusCode()).toList());

        // ============ 8. 不同的 BodyHandlers ============
        System.out.println("\n========== BodyHandlers ==========");
        // ofString()                字符串
        // ofByteArray()             字节数组
        // ofInputStream()           输入流
        // ofFile(Path)              保存到文件
        // ofLines()                 Stream<String>
        // ofPublisher()             反应式流
        // discarding()              丢弃 body

        HttpRequest r = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/bytes/100"))
            .build();

        HttpResponse<byte[]> byteResp = client.send(r, BodyHandlers.ofByteArray());
        System.out.println("字节响应长度: " + byteResp.body().length);

        // 保存到文件
        // HttpResponse<Path> fileResp = client.send(r, BodyHandlers.ofFile(Path.of("/tmp/download.bin")));

        // ============ 9. 处理错误 ============
        System.out.println("\n========== 错误处理 ==========");
        HttpRequest badReq = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/status/500"))
            .build();

        HttpResponse<String> badResp = client.send(badReq, BodyHandlers.ofString());
        if (badResp.statusCode() >= 400) {
            System.out.println("请求失败: " + badResp.statusCode());
        }

        // 异常处理
        try {
            HttpRequest timeoutReq = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delay/10"))
                .timeout(Duration.ofSeconds(1))
                .build();
            client.send(timeoutReq, BodyHandlers.ofString());
        } catch (java.net.http.HttpTimeoutException e) {
            System.out.println("超时: " + e.getMessage());
        }
    }
}

/*
 * =============== HttpClient 优势 ===============
 *
 * JDK 11 引入的 java.net.http.HttpClient 取代了老旧的 HttpURLConnection：
 * 1. 支持 HTTP/2
 * 2. 支持 WebSocket
 * 3. 同步和异步 API
 * 4. 流式响应
 * 5. 内置连接池
 *
 * =============== 基本使用步骤 ===============
 *
 * 1. 创建 HttpClient (通常只需一个)
 * 2. 构建 HttpRequest
 * 3. 发送并获取 HttpResponse
 * 4. 处理响应
 *
 * =============== 常用方法 ===============
 *
 * // HttpClient.Builder
 *   version(HTTP_1_1 / HTTP_2)     协议版本
 *   connectTimeout(duration)       连接超时
 *   followRedirects(policy)        重定向策略
 *   authenticator(auth)            认证
 *   cookieHandler(handler)         Cookie
 *   proxy(selector)                代理
 *
 * // HttpRequest.Builder
 *   uri(uri)                       目标
 *   GET() / POST(body) / PUT(body) / DELETE()  方法
 *   header(name, value)            设置头
 *   headers(kv...)                 批量设置
 *   timeout(duration)              请求超时
 *   version(v)                     协议
 *
 * // BodyPublishers（请求体）
 *   ofString(str)                  字符串
 *   ofByteArray(bytes)             字节
 *   ofFile(path)                   文件
 *   ofInputStream(supplier)        流
 *   noBody()                       无
 *
 * // BodyHandlers（响应体）
 *   ofString()                     字符串
 *   ofByteArray()                  字节
 *   ofFile(path)                   保存到文件
 *   ofLines()                      按行流
 *   ofInputStream()                原始流
 *   discarding()                   丢弃
 *
 * =============== 同步 vs 异步 ===============
 *
 * 同步：client.send(request, handler)         → HttpResponse
 * 异步：client.sendAsync(request, handler)    → CompletableFuture<HttpResponse>
 *
 * 并发调用可以使用 sendAsync，或在线程阻塞风格更清晰时结合虚拟线程使用 send。
 * 两种方式都需要配置连接/请求超时、限制下游并发，并检查 HTTP 状态码。
 * JDK 25 不包含通用 JSON API；JSON 处理通常使用 JSON-P、Jackson、Gson 等库。
 */
