import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * DateTime.java - Java 日期时间 API (java.time，JDK 8+)
 * <p>
 * 学习要点：
 * 1. LocalDate、LocalTime、LocalDateTime
 * 2. ZonedDateTime 时区
 * 3. Instant 时间戳
 * 4. Duration 和 Period 时间间隔
 * 5. DateTimeFormatter 格式化
 * 6. 时间计算和比较
 * 7. 老 API：Date、Calendar（避免使用）
 */
public class DateTime {

    public static void main(String[] args) {

        // ============ 1. 获取当前日期时间 ============
        System.out.println("========== 当前时间 ==========");

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        ZonedDateTime zoned = ZonedDateTime.now();
        Instant instant = Instant.now();

        System.out.println("当前日期: " + today);
        System.out.println("当前时间: " + now);
        System.out.println("当前日期时间: " + dateTime);
        System.out.println("带时区: " + zoned);
        System.out.println("时间戳: " + instant);
        System.out.println("毫秒: " + instant.toEpochMilli());

        // ============ 2. 创建指定的日期时间 ============
        System.out.println("\n========== 创建指定时间 ==========");

        LocalDate date1 = LocalDate.of(2026, 7, 1);
        LocalDate date2 = LocalDate.of(2026, Month.JULY, 1);        // 用 Month 枚举
        LocalTime time1 = LocalTime.of(14, 30, 45);
        LocalDateTime dt1 = LocalDateTime.of(2026, 7, 1, 14, 30);
        LocalDateTime dt2 = LocalDateTime.of(date1, time1);

        System.out.println("日期: " + date1);
        System.out.println("时间: " + time1);
        System.out.println("日期时间: " + dt1);
        System.out.println("组合: " + dt2);

        // 从字符串解析
        LocalDate parsed1 = LocalDate.parse("2026-01-01");
        LocalDateTime parsed2 = LocalDateTime.parse("2026-01-01T14:30:00");
        System.out.println("解析: " + parsed1 + " / " + parsed2);

        // ============ 3. 获取日期字段 ============
        System.out.println("\n========== 获取字段 ==========");
        LocalDate d = LocalDate.of(2026, 7, 15);

        System.out.println("年: " + d.getYear());
        System.out.println("月: " + d.getMonth() + " (" + d.getMonthValue() + ")");
        System.out.println("日: " + d.getDayOfMonth());
        System.out.println("星期: " + d.getDayOfWeek());              // WEDNESDAY
        System.out.println("一年中第几天: " + d.getDayOfYear());
        System.out.println("是闰年: " + d.isLeapYear());

        // ============ 4. 日期时间的加减（不可变，返回新对象） ============
        System.out.println("\n========== 加减 ==========");
        LocalDate base = LocalDate.of(2026, 1, 1);

        System.out.println("加 10 天: " + base.plusDays(10));
        System.out.println("加 3 月: " + base.plusMonths(3));
        System.out.println("加 1 年: " + base.plusYears(1));

        System.out.println("减 1 周: " + base.minusWeeks(1));

        // 通用方式
        System.out.println("加 5 天: " + base.plus(5, ChronoUnit.DAYS));

        // 修改字段（with 方法）
        System.out.println("改月为 12: " + base.withMonth(12));
        System.out.println("改日为 31: " + base.withDayOfMonth(31));

        // ============ 5. TemporalAdjuster 便捷调整 ============
        System.out.println("\n========== 便捷调整 ==========");
        LocalDate ref = LocalDate.of(2026, 7, 15);

        System.out.println("这个月第一天: " + ref.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println("这个月最后一天: " + ref.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("下个月第一天: " + ref.with(TemporalAdjusters.firstDayOfNextMonth()));
        System.out.println("下个周一: " + ref.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
        System.out.println("这个月最后一个周日: " + ref.with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY)));

        // ============ 6. 比较 ============
        System.out.println("\n========== 比较 ==========");
        LocalDate a = LocalDate.of(2026, 1, 1);
        LocalDate b = LocalDate.of(2026, 6, 1);

        System.out.println("a < b: " + a.isBefore(b));
        System.out.println("a > b: " + a.isAfter(b));
        System.out.println("a == b: " + a.isEqual(b));
        System.out.println("compareTo: " + a.compareTo(b));

        // ============ 7. 时间差：Duration 和 Period ============
        System.out.println("\n========== Duration / Period ==========");

        // Duration: 用于时间（时、分、秒）
        LocalDateTime t1 = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime t2 = LocalDateTime.of(2026, 1, 1, 12, 30);
        Duration duration = Duration.between(t1, t2);
        System.out.println("Duration: " + duration);
        System.out.println("总分钟: " + duration.toMinutes());
        System.out.println("总秒: " + duration.getSeconds());
        System.out.println("小时: " + duration.toHours());

        // Period: 用于日期（年、月、日）
        LocalDate birthday = LocalDate.of(2000, 5, 20);
        LocalDate nowDate = LocalDate.now();
        Period age = Period.between(birthday, nowDate);
        System.out.println("年龄: " + age.getYears() + " 年 " + age.getMonths() + " 月 " + age.getDays() + " 天");

        // 直接计算天数差
        long daysBetween = ChronoUnit.DAYS.between(birthday, nowDate);
        System.out.println("总天数: " + daysBetween);

        // ============ 8. 格式化 DateTimeFormatter ============
        System.out.println("\n========== 格式化 ==========");
        LocalDateTime dt = LocalDateTime.of(2026, 7, 1, 14, 30, 45);

        // 内置格式
        System.out.println("ISO: " + dt.format(DateTimeFormatter.ISO_DATE_TIME));

        // 自定义格式
        DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("格式1: " + dt.format(fmt1));

        DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分");
        System.out.println("格式2: " + dt.format(fmt2));

        DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("EEEE, MMMM dd yyyy");
        System.out.println("格式3: " + dt.format(fmt3));

        // 解析
        LocalDateTime parsed = LocalDateTime.parse(
            "2026-07-01 14:30:45",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        System.out.println("解析: " + parsed);

        // ============ 9. 时区 ZoneId 和 ZonedDateTime ============
        System.out.println("\n========== 时区 ==========");

        // 系统默认时区
        System.out.println("系统时区: " + ZoneId.systemDefault());

        // 常用时区
        ZoneId beijing = ZoneId.of("Asia/Shanghai");
        ZoneId newYork = ZoneId.of("America/New_York");
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        ZoneId utc = ZoneId.of("UTC");

        LocalDateTime localTime = LocalDateTime.of(2026, 7, 1, 14, 0);
        ZonedDateTime beijingTime = localTime.atZone(beijing);

        System.out.println("北京: " + beijingTime);
        System.out.println("纽约: " + beijingTime.withZoneSameInstant(newYork));
        System.out.println("东京: " + beijingTime.withZoneSameInstant(tokyo));
        System.out.println("UTC:  " + beijingTime.withZoneSameInstant(utc));

        // ============ 10. Instant 时间戳 ============
        System.out.println("\n========== Instant ==========");

        Instant now2 = Instant.now();
        System.out.println("当前: " + now2);
        System.out.println("毫秒: " + now2.toEpochMilli());
        System.out.println("秒: " + now2.getEpochSecond());

        // 从毫秒创建
        Instant ins = Instant.ofEpochMilli(1_700_000_000_000L);
        System.out.println("从毫秒: " + ins);

        // Instant → LocalDateTime
        LocalDateTime ldt = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
        System.out.println("转 LocalDateTime: " + ldt);

        // ============ 11. 老 API 转换 ============
        System.out.println("\n========== 与老 API 互转 ==========");
        java.util.Date oldDate = new java.util.Date();
        Instant fromDate = oldDate.toInstant();
        LocalDateTime fromOld = LocalDateTime.ofInstant(fromDate, ZoneId.systemDefault());
        System.out.println("Date → LocalDateTime: " + fromOld);

        LocalDateTime newDT = LocalDateTime.now();
        java.util.Date toDate = java.util.Date.from(newDT.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("LocalDateTime → Date: " + toDate);
    }
}

/*
 * =============== java.time 核心类 ===============
 *
 * LocalDate         年月日（无时间、无时区）
 * LocalTime         时分秒（无日期、无时区）
 * LocalDateTime     年月日 + 时分秒（无时区）
 * ZonedDateTime     带时区的日期时间
 * OffsetDateTime    带偏移量的日期时间（无时区规则）
 * Instant           时间戳（UTC，纳秒精度）
 * ZoneId            时区
 * Duration          两个时刻之间的差（时分秒）
 * Period            两个日期之间的差（年月日）
 * DateTimeFormatter 格式化/解析
 * ChronoUnit        时间单位（DAYS, HOURS 等）
 * DayOfWeek         星期几枚举
 * Month             月份枚举
 * MonthDay          月+日（生日）
 * YearMonth         年+月（信用卡有效期）
 *
 * =============== 特点 ===============
 *
 * 1. 不可变（Immutable）：所有操作返回新对象
 * 2. 线程安全
 * 3. API 清晰、命名规范
 * 4. 类型明确（LocalDate vs Instant，避免混淆）
 *
 * =============== 常用格式化模式 ===============
 *
 * yyyy    4位年份 (2026)
 * yy      2位年份 (26)
 * MM      月份 (01-12)
 * MMM     月份缩写 (Jul)
 * MMMM    月份全称 (July)
 * dd      日 (01-31)
 * HH      24小时制 (00-23)
 * hh      12小时制 (01-12)
 * mm      分钟
 * ss      秒
 * SSS     毫秒
 * a       AM/PM
 * E/EEE   星期几缩写
 * EEEE    星期几全称
 * z/zzz   时区缩写
 * Z       时区偏移
 *
 * =============== 与老 API 对比 ===============
 *
 *              Date/Calendar             java.time
 * 可变性       可变                      不可变
 * 线程安全     不安全                    安全
 * API          难用                      清晰
 * 时区支持     困难                      优秀
 * 精度         毫秒                      纳秒
 *
 * 建议：所有新代码使用 java.time，避免 Date、Calendar、SimpleDateFormat。
 */
