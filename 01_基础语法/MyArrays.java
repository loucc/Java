import java.util.Arrays;

/**
 * Arrays.java - Java 数组
 * <p>
 * 学习要点：
 * 1. 一维数组的定义和使用
 * 2. 数组的初始化方式
 * 3. 数组遍历
 * 4. 二维数组
 * 5. Arrays 工具类
 * 6. 数组常见操作
 * 7. 命令行参数数组
 */
public class MyArrays {

    public static void main(String[] args) {

        // ============ 1. 一维数组定义 ============
        System.out.println("========== 数组定义 ==========");

        // 方式一：静态初始化（推荐，简洁）
        int[] scores = {85, 92, 78, 95, 88};
        System.out.println("scores.length = " + scores.length);
        System.out.println("scores[0] = " + scores[0]);

        // 方式二：显式静态初始化
        int[] nums = new int[]{1, 2, 3, 4, 5};

        // 方式三：动态初始化（先定义大小，再赋值）
        int[] ages = new int[5];                // 默认全部为 0
        ages[0] = 18;
        ages[1] = 20;
        ages[2] = 22;
        System.out.println("ages[3] = " + ages[3]);    // 默认值 0

        // 引用类型数组默认值是 null
        String[] names = new String[3];
        System.out.println("names[0] = " + names[0]);  // null

        // 声明数组还可以将 [] 放在变量名后（不推荐，但合法）
        int nums2[] = {1, 2, 3};

        // ============ 2. 各类型数组的默认值 ============
        System.out.println("\n========== 数组默认值 ==========");
        int[] intArr = new int[3];
        double[] dblArr = new double[3];
        boolean[] boolArr = new boolean[3];
        char[] charArr = new char[3];
        String[] strArr = new String[3];

        System.out.println("int[] 默认: " + intArr[0]);            // 0
        System.out.println("double[] 默认: " + dblArr[0]);         // 0.0
        System.out.println("boolean[] 默认: " + boolArr[0]);       // false
        System.out.println("char[] 默认: " + (int) charArr[0]);    // 0
        System.out.println("String[] 默认: " + strArr[0]);         // null

        // ============ 3. 数组遍历 ============
        System.out.println("\n========== 数组遍历 ==========");

        // 方式一：传统 for 循环（可获取索引）
        System.out.print("传统 for: ");
        for (int i = 0; i < scores.length; i++) {
            System.out.print(scores[i] + " ");
        }
        System.out.println();

        // 方式二：增强 for 循环（推荐，简洁）
        System.out.print("增强 for: ");
        for (int score : scores) {
            System.out.print(score + " ");
        }
        System.out.println();

        // 方式三：Arrays.toString() 直接打印
        System.out.println("toString: " + Arrays.toString(scores));

        // ============ 4. 数组常见操作 ============
        System.out.println("\n========== 数组操作 ==========");

        // 求和
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        System.out.println("总分: " + sum);
        System.out.println("平均分: " + (sum / (double) scores.length));

        // 求最大值和最小值
        int max = scores[0], min = scores[0];
        for (int score : scores) {
            if (score > max) max = score;
            if (score < min) min = score;
        }
        System.out.println("最高分: " + max + ", 最低分: " + min);

        // 查找元素
        int target = 95;
        int index = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == target) {
                index = i;
                break;
            }
        }
        System.out.println("找 " + target + " 的位置: " + index);

        // 反转数组
        int[] copy = scores.clone();
        for (int i = 0, j = copy.length - 1; i < j; i++, j--) {
            int temp = copy[i];
            copy[i] = copy[j];
            copy[j] = temp;
        }
        System.out.println("反转后: " + Arrays.toString(copy));

        // ============ 5. Arrays 工具类 ============
        System.out.println("\n========== Arrays 工具类 ==========");
        int[] arr = {5, 3, 8, 1, 9, 4};

        // 打印数组
        System.out.println("原数组: " + Arrays.toString(arr));

        // 排序（升序）
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        System.out.println("排序后: " + Arrays.toString(sorted));

        // 二分查找（必须先排序）
        int idx = Arrays.binarySearch(sorted, 8);
        System.out.println("查找 8 的索引: " + idx);

        // 填充
        int[] filled = new int[5];
        Arrays.fill(filled, 7);
        System.out.println("全部填 7: " + Arrays.toString(filled));

        // 拷贝
        int[] copied = Arrays.copyOf(arr, 3);           // 拷贝前 3 个
        System.out.println("拷贝前3个: " + Arrays.toString(copied));

        int[] range = Arrays.copyOfRange(arr, 1, 4);    // 拷贝索引 1~3
        System.out.println("拷贝索引1-3: " + Arrays.toString(range));

        // 比较
        int[] a1 = {1, 2, 3};
        int[] a2 = {1, 2, 3};
        System.out.println("a1 == a2: " + (a1 == a2));                // false（不同对象）
        System.out.println("Arrays.equals: " + Arrays.equals(a1, a2)); // true（内容相同）

        // ============ 6. 二维数组 ============
        System.out.println("\n========== 二维数组 ==========");

        // 静态初始化
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        // 遍历二维数组
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }

        // 增强 for 遍历二维数组
        System.out.println("\n增强 for 遍历:");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }

        // 二维数组的动态初始化
        int[][] grid = new int[3][4];       // 3行4列
        System.out.println("grid[0][0] = " + grid[0][0]);

        // 不规则二维数组（每行长度不同）
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1};
        jagged[1] = new int[]{1, 2};
        jagged[2] = new int[]{1, 2, 3};

        System.out.println("\n不规则数组:");
        for (int[] row : jagged) {
            System.out.println(Arrays.toString(row));
        }

        // Arrays.deepToString 打印多维数组
        System.out.println("\ndeepToString: " + Arrays.deepToString(matrix));

        // ============ 7. 数组常见错误 ============
        System.out.println("\n========== 常见错误 ==========");
        int[] test = {1, 2, 3};
        try {
            int val = test[10];                 // 数组越界
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界: " + e.getMessage());
        }

        try {
            int[] nullArr = null;
            int len = nullArr.length;           // 空指针
        } catch (NullPointerException e) {
            System.out.println("空指针: 不能访问 null 数组的属性");
        }

        // ============ 8. 命令行参数数组 ============
        System.out.println("\n========== 命令行参数 ==========");
        System.out.println("args.length = " + args.length);
        for (String arg : args) {
            System.out.println("  arg = " + arg);
        }
    }
}

/*
 * =============== 数组特点总结 ===============
 *
 * 1. 数组一旦创建，长度不可变
 * 2. 数组元素类型必须一致
 * 3. 数组是引用类型，存在堆中
 * 4. 数组下标从 0 开始，到 length-1 结束
 * 5. 越界会抛 ArrayIndexOutOfBoundsException
 * 6. 数组基本类型有默认值，引用类型默认为 null
 *
 * =============== 数组 vs 集合（List） ===============
 *
 *              数组                  ArrayList
 * 长度         固定                  动态扩容
 * 类型         基本+引用             只能引用类型（自动装箱）
 * 语法         [] 语法糖             方法调用
 * 性能         略高                  略低（有额外开销）
 * 使用场景     大小固定、追求性能    大小变化、丰富的操作
 *
 * 注意：本文件类名叫 MyArrays 是因为要避免与 java.util.Arrays 冲突。
 */
