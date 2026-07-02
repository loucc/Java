/**
 * AbstractClass.java - 抽象类
 * <p>
 * 学习要点：
 * 1. abstract 关键字
 * 2. 抽象方法（只有声明，没有方法体）
 * 3. 抽象类不能实例化
 * 4. 子类必须重写全部抽象方法，或者自身也是抽象类
 * 5. 抽象类的应用：模板方法模式
 */
public class AbstractClass {

    public static void main(String[] args) {

        // 抽象类不能直接实例化
        // Vehicle v = new Vehicle();           // 编译错误！

        // 只能实例化其非抽象子类
        Car car = new Car("Tesla Model 3");
        car.start();
        car.run();
        car.stop();

        System.out.println();
        Bike bike = new Bike("Giant");
        bike.start();
        bike.run();
        bike.stop();

        // 多态：抽象类引用指向子类对象
        System.out.println("\n========== 多态 ==========");
        Vehicle[] vehicles = { car, bike, new Motorcycle("Harley") };
        for (Vehicle v : vehicles) {
            v.travel();                         // 调用模板方法
            System.out.println("---");
        }
    }
}

// ============ 抽象父类 ============
// abstract 修饰的类是抽象类
abstract class Vehicle {

    protected String name;

    public Vehicle(String name) {
        this.name = name;
    }

    // 抽象方法：只有方法签名，没有方法体
    // 强制子类实现
    public abstract void start();
    public abstract void stop();

    // 普通方法：可以有实现
    public void run() {
        System.out.println(name + " 正在行驶");
    }

    // 模板方法模式：定义算法骨架，具体步骤由子类实现
    public final void travel() {
        start();
        run();
        stop();
    }

    public String getName() { return name; }
}

// ============ 子类必须实现全部抽象方法 ============
class Car extends Vehicle {
    public Car(String name) {
        super(name);
    }

    @Override
    public void start() {
        System.out.println(name + " 点火启动");
    }

    @Override
    public void stop() {
        System.out.println(name + " 踩刹车停车");
    }

    // 可以重写非抽象方法
    @Override
    public void run() {
        System.out.println(name + " 在公路上疾驰");
    }
}

class Bike extends Vehicle {
    public Bike(String name) {
        super(name);
    }

    @Override
    public void start() {
        System.out.println(name + " 蹬踏板");
    }

    @Override
    public void stop() {
        System.out.println(name + " 捏刹车");
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String name) {
        super(name);
    }

    @Override
    public void start() {
        System.out.println(name + " 拉油门");
    }

    @Override
    public void stop() {
        System.out.println(name + " 后刹车");
    }
}

// ============ 抽象类也可以继承抽象类（不必实现所有抽象方法）============
abstract class ElectricVehicle extends Vehicle {
    protected int batteryLevel;

    public ElectricVehicle(String name, int batteryLevel) {
        super(name);
        this.batteryLevel = batteryLevel;
    }

    // 新增抽象方法
    public abstract void charge();

    // 可以选择实现或不实现父类的抽象方法
    @Override
    public void start() {
        System.out.println(name + " 静默启动");
    }
    // stop() 仍然是抽象的，由 ElectricCar 实现
}

class ElectricCar extends ElectricVehicle {
    public ElectricCar(String name, int batteryLevel) {
        super(name, batteryLevel);
    }

    @Override
    public void stop() {
        System.out.println(name + " 能量回收停车");
    }

    @Override
    public void charge() {
        System.out.println(name + " 正在充电，电量: " + batteryLevel + "%");
    }
}

/*
 * =============== 抽象类的特点 ===============
 *
 * 1. 用 abstract 关键字修饰
 * 2. 可以包含抽象方法和普通方法
 * 3. 可以包含字段、构造器、静态方法
 * 4. 不能直接实例化（不能 new）
 * 5. 子类必须实现所有抽象方法，否则子类也必须是抽象类
 * 6. 抽象类可以继承抽象类
 * 7. 抽象方法必须在抽象类中（或接口中）
 *
 * =============== 抽象方法的特点 ===============
 *
 * 1. 用 abstract 修饰
 * 2. 没有方法体（分号结尾，不是 {}）
 * 3. 不能是 static、final、private
 * 4. 强制子类实现
 *
 * =============== 抽象类的应用 ===============
 *
 * 1. 定义规范：明确子类必须实现哪些方法
 * 2. 代码复用：通用逻辑放在抽象类中
 * 3. 模板方法模式：父类定义算法骨架，子类实现具体步骤
 * 4. 多态基础：为多种子类提供统一父类型
 *
 * =============== 抽象类 vs 接口 ===============
 *
 *                      抽象类                  接口
 * 关键字               abstract class          interface
 * 继承/实现            extends（单继承）       implements（多实现）
 * 字段                 可以有实例字段          只能是 public static final
 * 方法                 抽象+普通+静态          抽象+default+静态+私有
 * 构造器               可以有                  不能有
 * 使用场景             is-a 关系               has-a 或 capability 关系
 *
 * 详见 Interface.java
 */
