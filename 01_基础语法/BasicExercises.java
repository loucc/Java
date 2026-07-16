import java.util.Arrays;

/**
 * BasicExercises.java - 基础语法综合练习
 *
 * 建议先折叠或删除参考实现，根据方法名和检查项重新实现，再运行 main 验证。
 * 可继续补充空数组、负数和边界年份等测试数据。
 */
public class BasicExercises {

    public static void main(String[] args) {
        check("闰年 2000", isLeapYear(2000));
        check("平年 1900", !isLeapYear(1900));
        check("闰年 2024", isLeapYear(2024));
        check("成绩等级", "B".equals(gradeOf(85)));
        check("数组最大值", maxOf(new int[]{-5, 8, 3, 8}) == 8);
        check("回文", isPalindrome("level"));
        check("非回文", !isPalindrome("java"));

        int[] values = {3, 1, 4, 1, 5};
        System.out.println("原数组保持不变: " + Arrays.toString(values));
    }

    static boolean isLeapYear(int year) {
        return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
    }

    static String gradeOf(int score) {
        if (score < 0 || score > 100) {
            return "INVALID";
        }
        return switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7, 6 -> "C";
            default -> "D";
        };
    }

    static int maxOf(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values 不能为空");
        }

        int max = values[0];
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        for (int left = 0, right = text.length() - 1; left < right; left++, right--) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
        }
        return true;
    }

    static void check(String name, boolean passed) {
        System.out.println((passed ? "PASS" : "FAIL") + " - " + name);
    }
}
