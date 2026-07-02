/**
 * ControlFlow.java - Java 控制流程
 * <p>
 * 学习要点：
 * 1. if / else if / else 语句
 * 2. switch 语句（传统写法）
 * 3. switch 表达式（JDK 14+ 新写法）
 * 4. switch 中的 yield 关键字
 * 5. 模式匹配 switch（JDK 21+）
 */
public class ControlFlow {

    public static void main(String[] args) {

        // ============ 1. if 语句 ============
        System.out.println("========== if 语句 ==========");
        int score = 85;

        if (score >= 60) {
            System.out.println("及格");
        }

        // if - else
        if (score >= 60) {
            System.out.println("通过");
        } else {
            System.out.println("挂科");
        }

        // if - else if - else 多分支
        String grade;
        if (score >= 90) {
            grade = "优秀";
        } else if (score >= 80) {
            grade = "良好";
        } else if (score >= 60) {
            grade = "及格";
        } else {
            grade = "不及格";
        }
        System.out.println("成绩：" + score + " → " + grade);

        // ============ 2. 传统 switch 语句 ============
        System.out.println("\n========== 传统 switch ==========");
        int day = 3;
        String dayName;
        switch (day) {
            case 1:
                dayName = "星期一";
                break;                          // 必须写 break，否则会"穿透"
            case 2:
                dayName = "星期二";
                break;
            case 3:
                dayName = "星期三";
                break;
            case 4:
                dayName = "星期四";
                break;
            case 5:
                dayName = "星期五";
                break;
            case 6:
            case 7:
                dayName = "周末";                // 多个 case 共用一个逻辑
                break;
            default:
                dayName = "无效";
        }
        System.out.println("第 " + day + " 天：" + dayName);

        // switch 支持的类型：byte、short、int、char、String、enum
        String command = "start";
        switch (command) {
            case "start":
                System.out.println("启动");
                break;
            case "stop":
                System.out.println("停止");
                break;
            default:
                System.out.println("未知命令");
        }

        // ============ 3. switch 表达式（JDK 14+ 推荐写法）============
        System.out.println("\n========== switch 表达式 ==========");
        // 使用 -> 箭头，不需要 break，不会穿透
        int day2 = 6;
        String dayName2 = switch (day2) {
            case 1 -> "星期一";
            case 2 -> "星期二";
            case 3 -> "星期三";
            case 4 -> "星期四";
            case 5 -> "星期五";
            case 6, 7 -> "周末";                 // 多个值用逗号分隔
            default -> "无效";
        };
        System.out.println("第 " + day2 + " 天：" + dayName2);

        // ============ 4. switch 表达式 + yield（用于代码块）============
        System.out.println("\n========== switch + yield ==========");
        int month = 5;
        String season = switch (month) {
            case 3, 4, 5 -> "春";
            case 6, 7, 8 -> "夏";
            case 9, 10, 11 -> "秋";
            case 12, 1, 2 -> "冬";
            default -> {
                System.out.println("月份 " + month + " 无效");
                yield "未知";                    // 代码块中用 yield 返回值
            }
        };
        System.out.println("月份 " + month + " → " + season);

        // ============ 5. 模式匹配 switch（JDK 21+）============
        System.out.println("\n========== 模式匹配 switch ==========");
        Object obj = "Hello";
        String desc = switch (obj) {
            case Integer i -> "整数：" + i;
            case String s -> "字符串：" + s + "，长度：" + s.length();
            case Double d -> "双精度：" + d;
            case null -> "空值";
            default -> "未知类型";
        };
        System.out.println(desc);

        // 支持 when 守卫条件
        Object value = 100;
        String result = switch (value) {
            case Integer i when i > 0 -> "正整数：" + i;
            case Integer i when i < 0 -> "负整数：" + i;
            case Integer i -> "零";
            default -> "非整数";
        };
        System.out.println(result);

        // ============ 6. 嵌套控制流 ============
        System.out.println("\n========== 嵌套判断 ==========");
        int userAge = 25;
        boolean hasLicense = true;
        boolean hasCar = true;

        if (userAge >= 18) {
            if (hasLicense) {
                if (hasCar) {
                    System.out.println("可以开自己的车");
                } else {
                    System.out.println("有驾照但没车");
                }
            } else {
                System.out.println("成年但无驾照");
            }
        } else {
            System.out.println("未成年，不能开车");
        }

        // 建议：使用逻辑运算符简化嵌套
        if (userAge >= 18 && hasLicense && hasCar) {
            System.out.println("[优化后] 可以开自己的车");
        }
    }

    /*
     * =============== switch 语句 vs 表达式 对比 ===============
     *
     * 传统 switch（语句）：
     * - 用 case ... : 和 break
     * - 会穿透（fall-through）
     * - 不返回值
     *
     * switch 表达式（推荐）：
     * - 用 case ... -> 箭头
     * - 不穿透，无需 break
     * - 返回值（可赋给变量）
     * - 支持 yield（代码块中返回值）
     * - 支持模式匹配（JDK 21+）
     * - 必须穷尽所有情况（default 或全部枚举）
     */
}
