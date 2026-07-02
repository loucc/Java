import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

/**
 * Reflection.java - 反射机制
 * <p>
 * 学习要点：
 * 1. 反射的概念和用途
 * 2. Class 对象的获取（3 种方式）
 * 3. 获取和调用方法
 * 4. 获取和访问字段
 * 5. 创建对象（构造器）
 * 6. 获取注解
 * 7. 反射的性能和安全考虑
 */
public class Reflection {

    public static void main(String[] args) throws Exception {

        // ============ 1. 获取 Class 对象的 3 种方式 ============
        System.out.println("========== 获取 Class ==========");

        // 方式一：类名.class
        Class<String> c1 = String.class;

        // 方式二：对象.getClass()
        String s = "hello";
        Class<? extends String> c2 = s.getClass();

        // 方式三：Class.forName("全限定名")
        Class<?> c3 = Class.forName("java.lang.String");

        System.out.println("c1 == c2: " + (c1 == c2));      // true
        System.out.println("c2 == c3: " + (c2 == c3));      // true (同一个 Class 对象)

        // ============ 2. Class 的基本信息 ============
        System.out.println("\n========== Class 信息 ==========");
        Class<?> clz = Employee2.class;

        System.out.println("全名: " + clz.getName());
        System.out.println("简名: " + clz.getSimpleName());
        System.out.println("包名: " + clz.getPackageName());
        System.out.println("修饰符: " + Modifier.toString(clz.getModifiers()));
        System.out.println("父类: " + clz.getSuperclass().getSimpleName());
        System.out.println("接口: " + Arrays.toString(clz.getInterfaces()));

        // ============ 3. 反射获取字段 ============
        System.out.println("\n========== 反射字段 ==========");
        Employee2 emp = new Employee2("张三", 25, 15000);

        // 所有 public 字段（包括继承的）
        Field[] publicFields = clz.getFields();
        System.out.println("public 字段数: " + publicFields.length);

        // 声明的所有字段（不包括继承）
        Field[] declared = clz.getDeclaredFields();
        System.out.println("声明的字段:");
        for (Field f : declared) {
            f.setAccessible(true);                          // 突破 private 限制
            System.out.println("  " + f.getType().getSimpleName() + " " + f.getName()
                + " = " + f.get(emp));
        }

        // 修改私有字段
        Field nameField = clz.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(emp, "李四");
        System.out.println("修改后: " + emp);

        // ============ 4. 反射获取方法 ============
        System.out.println("\n========== 反射方法 ==========");

        // 所有方法（包括继承）
        Method[] allMethods = clz.getMethods();
        System.out.println("所有方法数（含继承）: " + allMethods.length);

        // 声明的方法
        Method[] declaredMethods = clz.getDeclaredMethods();
        System.out.println("声明的方法:");
        for (Method m : declaredMethods) {
            System.out.println("  " + Modifier.toString(m.getModifiers()) + " "
                + m.getReturnType().getSimpleName() + " " + m.getName() + "()");
        }

        // 调用方法
        Method greetMethod = clz.getMethod("greet", String.class);
        Object result = greetMethod.invoke(emp, "上午好");
        System.out.println("调用 greet: " + result);

        // 调用私有方法
        Method secret = clz.getDeclaredMethod("secretInfo");
        secret.setAccessible(true);
        System.out.println("私有方法: " + secret.invoke(emp));

        // ============ 5. 反射创建对象 ============
        System.out.println("\n========== 反射创建对象 ==========");

        // 无参构造
        Class<?> c = Employee2.class;
        Constructor<?> emptyCtor = c.getDeclaredConstructor();
        emptyCtor.setAccessible(true);
        Object e1 = emptyCtor.newInstance();
        System.out.println("无参: " + e1);

        // 有参构造
        Constructor<?> ctor = c.getConstructor(String.class, int.class, double.class);
        Object e2 = ctor.newInstance("王五", 30, 20000.0);
        System.out.println("有参: " + e2);

        // ============ 6. 反射获取泛型信息 ============
        System.out.println("\n========== 泛型信息 ==========");
        Field listField = Container.class.getDeclaredField("items");
        Type genericType = listField.getGenericType();
        System.out.println("字段泛型: " + genericType);

        if (genericType instanceof ParameterizedType pt) {
            Type[] actualTypeArgs = pt.getActualTypeArguments();
            for (Type t : actualTypeArgs) {
                System.out.println("  类型参数: " + t.getTypeName());
            }
        }

        // ============ 7. 数组反射 ============
        System.out.println("\n========== 数组反射 ==========");
        Object arr = Array.newInstance(int.class, 5);           // 创建 int[5]
        Array.set(arr, 0, 100);
        Array.set(arr, 1, 200);
        System.out.println("数组长度: " + Array.getLength(arr));
        System.out.println("元素 0: " + Array.get(arr, 0));

        int[] intArr = {1, 2, 3, 4, 5};
        System.out.println("数组类型: " + intArr.getClass().getSimpleName());
        System.out.println("组件类型: " + intArr.getClass().getComponentType());

        // ============ 8. 反射调用主方法（模拟） ============
        System.out.println("\n========== 反射调用 main ==========");
        Class<?> mainClass = SimpleMain.class;
        Method mainMethod = mainClass.getMethod("main", String[].class);
        // 注意：String[] 是可变参数，要包一层
        mainMethod.invoke(null, (Object) new String[]{"arg1", "arg2"});

        // ============ 9. 反射的性能 ============
        System.out.println("\n========== 反射性能 ==========");
        Employee2 sample = new Employee2("测试", 20, 10000);

        int times = 100_000;

        // 直接调用
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            sample.greet("hi");
        }
        long directTime = System.nanoTime() - start;

        // 反射调用
        Method mGreet = clz.getMethod("greet", String.class);
        start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            mGreet.invoke(sample, "hi");
        }
        long reflectTime = System.nanoTime() - start;

        System.out.println("直接调用 " + times + " 次: " + directTime / 1_000_000 + "ms");
        System.out.println("反射调用 " + times + " 次: " + reflectTime / 1_000_000 + "ms");
        System.out.println("反射是直接的 " + (reflectTime / directTime) + " 倍");
    }
}

class Employee2 {
    public String name;
    private int age;
    protected double salary;
    static int count;

    public Employee2() {
        this("未知", 0, 0);
    }

    public Employee2(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        count++;
    }

    public String greet(String greeting) {
        return greeting + ", 我是 " + name;
    }

    private String secretInfo() {
        return "工资: " + salary;
    }

    @Override
    public String toString() {
        return "Employee(" + name + ", " + age + ", " + salary + ")";
    }
}

class Container {
    private List<String> items;
}

class SimpleMain {
    public static void main(String[] args) {
        System.out.println("SimpleMain.main 被反射调用了！args = " + Arrays.toString(args));
    }
}

/*
 * =============== 什么是反射 ===============
 *
 * 反射（Reflection）：在运行时"检查"和"操作"类、对象、字段、方法的能力。
 *
 * 例如：
 * - 加载一个用户配置的类名
 * - 调用一个字符串名字的方法
 * - 遍历所有字段并打印
 * - 无视访问修饰符（setAccessible）
 *
 * =============== Class 对象 ===============
 *
 * 每个类在 JVM 中都有一个 Class 对象，是"元信息"的入口。
 * 三种获取方式（都返回同一个实例）：
 *   1. String.class
 *   2. obj.getClass()
 *   3. Class.forName("java.lang.String")
 *
 * =============== 常用反射 API ===============
 *
 * 【类信息】
 *   getName()               全限定名
 *   getSimpleName()         简单名
 *   getPackageName()        包名
 *   getModifiers()          修饰符（int，用 Modifier 解析）
 *   getSuperclass()         父类 Class
 *   getInterfaces()         接口数组
 *
 * 【字段 Field】
 *   getFields()             所有 public 字段（含继承）
 *   getDeclaredFields()     声明的字段（不含继承）
 *   getField(name)          单个 public 字段
 *   getDeclaredField(name)  单个声明的字段
 *   field.get(obj)          读值
 *   field.set(obj, val)     写值
 *   field.setAccessible(true) 突破 private
 *
 * 【方法 Method】
 *   getMethods()            所有 public 方法（含继承）
 *   getDeclaredMethods()    声明的方法
 *   getMethod(name, argTypes...)
 *   method.invoke(obj, args...)  调用
 *
 * 【构造器 Constructor】
 *   getConstructors()
 *   getDeclaredConstructors()
 *   constructor.newInstance(args...)
 *
 * 【注解】
 *   getAnnotation(class)
 *   getAnnotations()
 *   isAnnotationPresent(class)
 *
 * =============== 反射的用途 ===============
 *
 * 1. 框架：Spring、Hibernate、JUnit
 * 2. 序列化：JSON、XML 库
 * 3. IDE：代码补全、代码检查
 * 4. 依赖注入：根据类型自动装配
 * 5. 动态代理：AOP、RPC
 * 6. 测试框架
 *
 * =============== 反射的缺点 ===============
 *
 * 1. 性能损耗（比直接调用慢 10-100 倍）
 * 2. 破坏封装（setAccessible）
 * 3. 编译时无法检查
 * 4. 增加代码复杂度
 * 5. JDK 9+ 模块系统限制反射
 *
 * =============== 使用建议 ===============
 *
 * 1. 只在必要时使用
 * 2. 缓存 Method/Field 对象（避免重复查找）
 * 3. 优先用非反射方案（如 Lambda、MethodHandle）
 * 4. 现代替代：VarHandle、MethodHandles（JDK 9+）
 *
 * =============== 相关新特性 ===============
 *
 * JDK 25 支持的运行时能力：
 * - MethodHandles（方法句柄，更快）
 * - VarHandle（更高效的字段访问）
 * - LambdaMetafactory（Lambda 的运行时机制）
 */
