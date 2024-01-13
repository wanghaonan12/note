# 线程

> Java线程用于实现多任务处理，允许程序在同一时间执行多个独立的任务。线程能够同时执行多个代码片段，使得程序能够更高效地利用系统资源，并实现并发性和异步性。线程可以同时执行不同的操作，例如计算、IO操作、UI响应等。
>
> 首先了解一下什么是程序，进程，线程
>
> 然后在学习线程的创建，线程的生命周期，线程安全，线程的通信问题等

## 程序，进程，线程

➢程序(program)：是为完成特定任务、用某种语言编写的一组指令的集合,是一段静态的代码。 （程序是静态的）


➢进程(process)：是程序的一次执行过程。正在运行的一个程序，进程作为资源分配的单位，在内存中会为每个进程分配不同的内存区域。 （进程是动态的）是一个动的过程 ，进程的生命周期  :  有它自身的产生、存在和消亡的过程 

➢线程(thread)，进程可进一步细化为线程， 是一个程序内部的一条执行路径。
  若一个进程同一时间并行执行多个线程，就是支持多线程的。

![image-20230717145120080](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717145120080.png)

### 单核CPU与多核CPU的任务执行

![image-20230717145313982](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717145313982.png)

### 并行和并发：

并行：多个CPU同时执行多个任务
并发：一个CPU“同时”执行多个任务（采用时间片切换）

## 线程的创建

在我们平时的时候其实java程序就是以多线程的形式运行的：异常线程，主线程，垃圾收集线程

![image-20230717145950806](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717145950806.png)

我们创建自己的多线程会在主线程上进行扩充

![image-20230717150107884](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717150107884.png)

### 创建一：继承Thread类

> 必须重写Thread类中的run方法然后线程的任务/逻辑写在run方法中

```java
public class Code_01_ThreadCreate extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("线程："+this.getName()+"执行第"+(i+1)+"次！");
        }
    }
}
```

### 创建二：实现Runnable类

```java
public class Code_02_RunnableCreate implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            //Thread.currentThread()作用获取当前正在执行的线程
            System.out.println("线程："+Thread.currentThread().getName()+"执行第"+(i+1)+"次！");
        }
    }
}
```

### 创建三：实现Callable类

> 对比第一种和第二种创建线程的方式发现，无论第一种继承Thread类的方式还是第二种实现Runnable接口的方式，都需要有一个run方法，
> 但是这个run方法有不足：
>
> （1）没有返回值
> （2）不能抛出异常
>
> 基于上面的两个不足，在JDK1.5以后出现了第三种创建线程的方式：实现Callable接口：
>
> 实现Callable接口好处：（1）有返回值  （2）能抛出异常
> 缺点：线程创建比较麻烦

```java
public class Code_03_CallableCreate implements Callable<Integer> {
    /*
   1.实现Callable接口，可以不带泛型，如果不带泛型，那么call方式的返回值就是Object类型
   2.如果带泛型，那么call的返回值就是泛型对应的类型
   3.从call方法看到：方法有返回值，可以跑出异常
    */
    @Override
    public Integer call() throws Exception {
        return new Random().nextInt(10);//返回10以内的随机数
    }
}
```

**方法测试**

```java
public class TestThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        继承Thread类的实现方法
        Code_01_ThreadCreate threadCreate = new Code_01_ThreadCreate();
        threadCreate.start();

//        实现了runnable的实现方法
        Code_02_RunnableCreate runnableCreate = new Code_02_RunnableCreate();
        Thread thread = new Thread(runnableCreate, "thread_02222222222222222222");
        thread.start();

//       主线程的程序
        for (int i = 0; i < 10; i++) {
            System.out.println("主线程：执行第" + (i + 1) + "次！");
        }
//        实现Callable的实现方式
        Code_03_CallableCreate callableCreate = new Code_03_CallableCreate();
        FutureTask futureTask = new FutureTask(callableCreate);
        Thread callCreateThread = new Thread(futureTask, "thread_033333333333333333333333333333333");
        callCreateThread.start();
        System.out.println( "获取的随机数：" + futureTask.get());
    }
}
```

**测试结果**

```java
线程：Thread-0执行第1次！
线程：Thread-0执行第2次！
主线程：执行第1次！
线程：Thread-0执行第3次！
线程：Thread-0执行第4次！
线程：Thread-0执行第5次！
线程：Thread-0执行第6次！
线程：Thread-0执行第7次！
线程：Thread-0执行第8次！
线程：Thread-0执行第9次！
线程：Thread-0执行第10次！
线程：thread_02222222222222222222执行第1次！
主线程：执行第2次！
主线程：执行第3次！
线程：thread_02222222222222222222执行第2次！
主线程：执行第4次！
主线程：执行第5次！
线程：thread_02222222222222222222执行第3次！
主线程：执行第6次！
主线程：执行第7次！
线程：thread_02222222222222222222执行第4次！
线程：thread_02222222222222222222执行第5次！
主线程：执行第8次！
线程：thread_02222222222222222222执行第6次！
线程：thread_02222222222222222222执行第7次！
线程：thread_02222222222222222222执行第8次！
线程：thread_02222222222222222222执行第9次！
主线程：执行第9次！
线程：thread_02222222222222222222执行第10次！
主线程：执行第10次！
获取的随机数：4
```

## 生命周期

Java线程的生命周期包括以下几个状态：

1. 新建（New）：当创建一个Thread对象时，线程处于新建状态。此时线程还没有开始执行，即尚未调用start()方法。
2. 可运行（Runnable）：一旦调用了线程的start()方法，线程就进入可运行状态。在可运行状态下，线程并不一定正在执行，只是处于就绪状态，等待系统的调度。
3. 运行（Running）：当线程获得CPU执行时间片时，就进入运行状态。在运行状态下，线程执行自己的任务代码。
4. 阻塞（Blocked）：线程可能因为某些原因而暂时停止执行，进入阻塞状态。常见的阻塞原因包括等待I/O操作、等待获取锁等。在阻塞状态下，线程不会消耗CPU资源。
5. 等待（Waiting）：线程等待某个特定条件的触发，进入等待状态。可以通过调用wait()方法或者某些等待方法（如Object.wait()、Thread.join()）将线程置于等待状态。
6. 超时等待（Timed Waiting）：线程等待一段时间后自动恢复到可运行状态，进入超时等待状态。可以通过调用Thread.sleep()方法或者带有超时参数的等待方法（如Thread.join(long timeout)、Object.wait(long timeout)）使线程进入超时等待状态。
7. 终止（Terminated）：线程执行完任务或者因为异常等原因退出时，进入终止状态。

![image-20230717161643879](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717161643879.png)

下面是一个简单的示例代码，演示了Java线程的生命周期：

```java
public class ThreadLifecycleDemo {
    public static void main(String[] args) {
        // 创建新线程
        Thread thread = new Thread(() -> {
            // 线程任务代码
            System.out.println("线程任务开始执行");
            try {
                Thread.sleep(2000); // 模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程任务执行完毕");
        });

        System.out.println("1线程状态：" + thread.getState()); // 打印线程状态
        System.out.println("线程启动start");
        // 启动线程
        thread.start();

        System.out.println("2线程状态：" + thread.getState()); // 打印线程状态

        try {
            System.out.println("线程进入睡眠1秒");
            Thread.sleep(1000); // 等待1秒，确保线程进入运行状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("3线程状态：" + thread.getState()); // 打印线程状态

        try {
            thread.join(); // 等待线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("4线程状态：" + thread.getState()); // 打印线程状态
    }
}
```

输出结果如下：

```
1线程状态：NEW
线程启动start
2线程状态：RUNNABLE
线程进入睡眠1秒
线程任务开始执行
3线程状态：TIMED_WAITING
线程任务执行完毕
4线程状态：TERMINATED
```

## 线程常用方法

以下是Java线程类（Thread）的一些常用方法的完整列表：

1. start()：启动线程并使其进入可运行状态。
2. run()：定义线程要执行的任务代码，需要重写Thread类的run()方法。
3. sleep(long millis)：使线程暂停指定的毫秒数。
4. sleep(long millis, int nanos)：使线程暂停指定的毫秒数和纳秒数。
5. join()：等待线程执行完毕，使当前线程进入阻塞状态。
6. join(long millis)：等待线程执行完毕，最多等待指定的毫秒数。
7. join(long millis, int nanos)：等待线程执行完毕，最多等待指定的毫秒数和纳秒数。
8. interrupt()：中断线程，给线程发送中断信号。
9. isInterrupted()：判断线程是否被中断。
10. interrupted()：判断线程是否被中断，并清除中断状态。
11. isAlive()：判断线程是否处于活动状态（可运行、运行、阻塞）。
12. setName(String name)：设置线程的名称。
13. getName()：获取线程的名称。
14. setPriority(int priority)：设置线程的优先级。
15. getPriority()：获取线程的优先级。
16. setDaemon(boolean on)：将线程设置为守护线程。
17. isDaemon()：判断线程是否为守护线程。
18. yield()：使当前线程放弃当前的CPU资源，让其他线程有机会执行。

以下是一些常用方法的示例：

```java
public class ThreadCommonUseMethod {
    public static void main(String[] args) {
        // 创建新线程
        Thread thread = new Thread(() -> {
            System.out.println("线程任务开始执行");
        });

// 启动线程
        thread.start();

// 暂停当前线程一段时间
        try {
            Thread.sleep(2000); // 暂停2秒钟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// 等待线程执行完毕
        try {
            thread.join(); // 等待线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// 中断线程
        thread.interrupt();

// 判断线程是否处于活动状态
        boolean isAlive = thread.isAlive();

// 设置线程名称
        thread.setName("MyThread");

// 获取线程名称
        String threadName = thread.getName();

// 设置线程优先级
        thread.setPriority(Thread.MAX_PRIORITY);

// 获取线程优先级
        int priority = thread.getPriority();

// 设置线程为守护线程
        thread.setDaemon(true);

// 判断线程是否为守护线程
        boolean isDaemon = thread.isDaemon();

// 让出CPU资源
        Thread.yield();

        Code_01_ThreadCreate threadCreate = new Code_01_ThreadCreate();
//        设置伴随
        threadCreate.setDaemon(true); //在主线程die之后也跟随着挂掉
        threadCreate.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("mian线程输出"+i);
        }
    }
}
```

**运行结果**

```
线程任务开始执行
mian线程输出0
mian线程输出1
mian线程输出2
mian线程输出3
mian线程输出4
mian线程输出5
mian线程输出6
mian线程输出7
线程：Thread-1执行第1次！
线程：Thread-1执行第2次！
线程：Thread-1执行第3次！
线程：Thread-1执行第4次！
mian线程输出8
线程：Thread-1执行第5次！
线程：Thread-1执行第6次！
线程：Thread-1执行第7次！
线程：Thread-1执行第8次！
mian线程输出9
线程：Thread-1执行第9次！
线程：Thread-1执行第10次！
线程：Thread-1执行第11次！
线程：Thread-1执行第12次！
线程：Thread-1执行第13次！
线程：Thread-1执行第14次！
线程：Thread-1执行第15次！
线程：Thread-1执行第16次！
线程：Thread-1执行第17次！
线程：Thread-1执行第18次！
线程：Thread-1执行第19次！
线程：Thread-1执行第20次！
线程：Thread-1执行第21次！
线程：Thread-1执行第22次！
线程：Thread-1执行第23次！
线程：Thread-1执行第24次！
线程：Thread-1执行第25次！
线程：Thread-1执行第26次！
```

## 线程安全

> 新建一个线程模拟在火车站卖票的，
> 现在只有10张票了
>
> 有多个窗可以一同购买

```java
public class ThreadSecure_01_Impl extends Thread {
    public ThreadSecure_01_Impl(String name) {
        super(name);
    }
    static Integer ticket = 10;
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (ticket > 0) {
                System.out.println("用户在：" + this.getName() + "窗口购买第" + ticket-- + "张去北京的车票！");
            } else {
                this.stop();
            }
        }
    }
}

```

```java
public class ThreadSecure_01 {
    public static void main(String[] args) {
        ThreadSecure_01_Impl t1 = new ThreadSecure_01_Impl("窗口1");
        ThreadSecure_01_Impl t2 = new ThreadSecure_01_Impl("窗口2");
        ThreadSecure_01_Impl t3 = new ThreadSecure_01_Impl("窗口3");

        t1.start();
        t2.start();
        t3.start();
    }
}
```

通过多次购买返现会出现有一张票多次售出的情况

![image-20230717170638335](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717170638335.png)

上面的代码出现问题：出现了 重票，错票，---》 线程安全引起的问题 
原因：多个线程，在争抢资源的过程中，导致共享的资源出现问题。一个线程还没执行完，另一个线程就参与进来了，开始争抢。

>  解决：
>
> 在我的程序中，加入“锁” --》加同步  --》同步监视器

上锁

```java
public class ThreadSecure_02_Impl extends Thread {
    public ThreadSecure_02_Impl(String name) {
        super(name);
    }

    static Integer ticket = 10;

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
//        上锁
            synchronized (this) {
                if (ticket > 0) {
                    System.out.println("用户在：" + this.getName() + "窗口购买第" + ticket-- + "张去北京的车票！");
                }
            }
        }
    }
}
```

```java
public class ThreadSecure_02 {
    public static void main(String[] args) {
        ThreadSecure_02_Impl t1 = new ThreadSecure_02_Impl("窗口1");
        ThreadSecure_02_Impl t2 = new ThreadSecure_02_Impl("窗口2");
        ThreadSecure_02_Impl t3 = new ThreadSecure_02_Impl("窗口3");
        t1.start();
        t2.start();
        t3.start();
    }
}
```

运行结果

![image-20230717172902274](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717172902274.png)



总结1：同步监视器（锁子）   -----  synchronized(同步监视器){ }

1. 必须是引用数据类型，不能是基本数据类型
2. 也可以创建一个专门的同步监视器，没有任何业务含义 
3. 一般使用共享资源做同步监视器即可
4. 在同步代码块中不能改变同步监视器对象的引用 
5. 尽量不要String和包装类Integer做同步监视器 
6. 建议使用final修饰同步监视器

总结2：同步代码块的执行过程

1. 第一个线程来到同步代码块，发现同步监视器open状态，需要close,然后执行其中的代码
2. 第一个线程执行过程中，发生了线程切换（阻塞 就绪），第一个线程失去了cpu，但是没有开锁open
3. 第二个线程获取了cpu，来到了同步代码块，发现同步监视器close状态，无法执行其中的代码，第二个线程也进入阻塞状态
4. 第一个线程再次获取CPU,接着执行后续的代码；同步代码块执行完毕，释放锁open
5. 第二个线程也再次获取cpu，来到了同步代码块，发现同步监视器open状态，拿到锁并且上锁，由阻塞状态进入就绪状态，再进入运行状态，重复第一个线程的处理过程（加锁）
   强调：同步代码块中能发生CPU的切换吗？能！！！ 但是后续的被执行的线程也无法执行同步代码块（因为锁仍旧close） 

总结3：其他

1. 多个代码块使用了同一个同步监视器（锁），锁住一个代码块的同时，也锁住所有使用该锁的所有代码块，其他线程无法访问其中的任何一个代码块 
2. 多个代码块使用了同一个同步监视器（锁），锁住一个代码块的同时，也锁住所有使用该锁的所有代码块， 但是没有锁住使用其他同步监视器的代码块，其他线程有机会访问其他同步监视器的代码块

## 案例

> 应用场景：生产者和消费者问题
> 假设仓库中只能存放一件产品，生产者将生产出来的产品放入仓库，消费者将仓库中产品取走消费
> 如果仓库中没有产品，则生产者将产品放入仓库，否则停止生产并等待，直到仓库中的产品被消费者取走为止
> 如果仓库中放有产品，则消费者可以将产品取走消费，否则停止消费并等待，直到仓库中再次放入产品为止
>
> ![image-20230719114243274](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230719114243274.png)

1. 准备产品对象

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo {
    /**
     * 品牌
     */
    String brand;

    /**
     * 名字
     */
    String name;
}
```



2. 准备仓库对象

> 我们在仓库里面设置拥有线程安全的同步方法（制造商品和消费商品的方法），这样就可以让他们拥有共同的线程锁在工厂类中就可以直接调用了
>
> 其中还设置了一些仓库参数，当然也可以设置一个仓库类闯进去，这里为了省事直接在仓库里面写了静态参数

```java
@Data
public class WarehouseDto {
    /**
     * 仓库啤酒数量
     */
    public  Integer beersCount=0;
    /**
     * 仓库巧克力数量
     */
    public   Integer chocolateCount=0;
    /**
     * 巧克力总产量
     */
    public Integer manufactureChocolateCount = 0;
    /**
     * 巧克力总消费量
     */
    public Integer consumptionChocolateCount = 0;
    /**
     * 啤酒总产量
     */
    public Integer manufactureBeerCount = 0;
    /**
     * 啤酒总消费量
     */
    public Integer consumptionBeerCount = 0;
    /**
     * 仓库啤酒存货
     */
    public  List<ProductVo> beers=new ArrayList<>();
    /**
     * 仓库巧克力存货
     */
    public  List<ProductVo> chocolates=new ArrayList<>();
    /**
     * 每种产品可以放的数量
     */
    public  Integer capacity = 10;


    /**
     * 生产啤酒
     */
    public synchronized void  manufactureBeer(){
        if (this.beersCount>= this.capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.beers.add(ProductVo.builder().brand("费罗娜").name("芒果味果啤").build());
        this.beersCount++;
        this.manufactureBeerCount++;
        System.out.println("啤酒总产量："+this.manufactureBeerCount+",仓库剩余："+this.beers.size()+",仓库容量："+this.beersCount+",生产了：费罗娜++++++++++++++++++++芒果味果啤+++++++++++++++++费罗娜");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notify();
    }

    /**
     * 生产巧克力
     */
    public synchronized void  manufactureChocolate(){
        if (this.chocolateCount>=this.capacity) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.chocolates.add(ProductVo.builder().brand("费列罗").name("草莓味巧克力").build());
        this.chocolateCount++;
        this.manufactureChocolateCount++;
        System.out.println("巧克力总产量："+this.manufactureChocolateCount+",仓库剩余："+this.chocolates.size()+",仓库容量："+this.chocolateCount+",生产了：费列罗++++++++++++++++++++草莓味巧克力+++++++++++++++++费列罗");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        notify();
    }

    /**
     * 消费啤酒
     */
    public synchronized void  consumptionBeer(){
        if (this.beersCount<=0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ProductVo remove = this.beers.remove(0);
        this.beersCount--;
        this.consumptionBeerCount++;
        System.out.println("啤酒总消费量："+this.consumptionBeerCount+",仓库剩余："+this.beers.size()+",仓库容量："+this.beersCount+",消费了："+remove.getBrand()+"---------------------"+remove.getName()+"-----------------"+remove.getBrand());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notify();
    }

    /**
     * 消费巧克力
     */
    public synchronized void  consumptionChocolate(){
        if (this.chocolateCount<=0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ProductVo remove = this.chocolates.remove(0);
        this.chocolateCount--;
        this.consumptionChocolateCount++;
        System.out.println("巧克力总消费量："+this.consumptionChocolateCount+",仓库剩余："+this.chocolates.size()+",仓库容量："+this.chocolateCount+",消费了："+remove.getBrand()+"---------------------"+remove.getName()+"-----------------"+remove.getBrand());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        notify();
    }
}
```



3. 准备消费工厂

> 消费工厂消费一件商品

```java
public class ConsumerFactory implements Runnable{
    public WarehouseDto warehouse;

    public ConsumerFactory() {
    }

    public ConsumerFactory(WarehouseDto warehouse) {
        this.warehouse = warehouse;
    }



    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            warehouse.consumptionBeer();
            warehouse.consumptionChocolate();

        }
//        consumptionChocolate();
    }
}
```



4. 准备制作工厂

> 在制作工厂设置制作100个商品

```java
public class ManufactureFactory implements Runnable{


    public WarehouseDto warehouse;

    public ManufactureFactory() {
    }

    public ManufactureFactory(WarehouseDto warehouse) {
        this.warehouse = warehouse;
    }



    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            warehouse.manufactureBeer();
            warehouse.manufactureChocolate();
        }
//        manufactureChocolate();
    }
}
```

5. 启动工厂流水线程

```java
public class ThreadExample {
    public static void main(String[] args) {
        WarehouseDto warehouseDto = new WarehouseDto();
        ConsumerFactory consumerFactory = new ConsumerFactory(warehouseDto);
        ManufactureFactory manufactureFactory = new ManufactureFactory(warehouseDto);
        Thread thread = new Thread(consumerFactory);
        Thread thread1 = new Thread(manufactureFactory);
        thread1.start();
        thread.start();
    }
}
```

在结果展示

![image-20230719115321622](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230719115321622.png)

![image-20230719115348749](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230719115348749.png)
