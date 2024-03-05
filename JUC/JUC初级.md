# JUC初级

## 进程线程

> 进程（Process）是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。在早期面向进程设计的计算机结构中，进程是程序的基本执行实体；在当代面向线程设计的计算机结构中，进程是线程的容器。程序是指令、数据及其组织形式的描述，进程是程序的实体。
>
> 线程（英语：thread）是操作系统能够进行运算调度的最小单位。它被包含在进程之中，是进程中的实际运作单位。一条线程指的是进程中一个单一顺序的控制流，一个进程中可以并发多个线程，每条线程并行执行不同的任务。

**总结**

> 进程：指在系统中正在运行的一个应用程序；程序一旦运行就是进程；进程是资源分配的最小单位
>
> 线程：系统分配处理器时间资源的基本单元，或者说进程之内独立执行的一个单元执行流。线程时程序执行的最小单位

### 1. 线程状态

java.lang.Thread 包下的内部类

> 可以看出NEW（新建）、RUNNABLE（准备就绪）、BLOCKED（阻塞）、WAITING（等待-不见不散）、TIMED_WAITING（定时等待-过时不候）、TERMINATED（终结）

![image-20240222110018638](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240222110018638.png)

### 2. sleep和wait

> 1. sleep是Thread的静态方法；wait是Object的方法，任何对象实例都能调用。
> 2. sleep不会释放锁，它也不需要占用锁；wait会释放锁，但调用它的前提是当前线程占有锁（即代码要在synchronized中）
> 3. 它们都可以被interrupt方法中断

### 3. 管程

> **管程 (Moniters，也称为监视器)**
> 保证了同一时刻只有一个进程在管程内活动，即管程内定义的操作在同一时刻只被一个进程调用(由编译器实现)。 `java` 中叫 锁

### 4. 守护线程用户线程

用户线程：自定义线程  主线程结束了，用户线程还在运行，jvm还存活

守护线程：比如说[垃圾回收](https://so.csdn.net/so/search?q=垃圾回收&spm=1001.2101.3001.7020)线程  没有用户线程了，只有守护线程，jvm结束

![01-进程和线程概念](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/01-%E8%BF%9B%E7%A8%8B%E5%92%8C%E7%BA%BF%E7%A8%8B%E6%A6%82%E5%BF%B5.png)

```java
public class One {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            //打印线程名和
            System.out.println(Thread.currentThread().getName()+":"+(Thread.currentThread().isDaemon() ? "守护" : "用户")+"线程正在执行");
            //禁止创建的线程停止
            while (true){
            }
        }, "t1");
        // 设置线程是守护线程还是用户线程 默认false
        t1.setDaemon(true);
        t1.start();
        //让主线程晚于t1线程关闭
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("线程执行完毕！");
    }
}
```

> 修改`Daemon`状态可以看到用户线程没有结束`jvm`不会停止 ,但是守护线程没有结束的时候 `jvm`会停止工作。 

![image-20240222112858379](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240222112858379.png)

 **多线程其他实现方式**

## Lock

> `synchronized` 关键字可以实现同步锁功能 ，能够修饰 一个 对象，变量，方法，来控制这个所修饰的，被顺序的访问。
>
> 1. 当修饰一个**代码块**，被修饰的代码块称为同步语句块，其作用的范围是大括号{} **括起来的代码**，**作用的对象是调用这个代码块的对象**；  
>
> 2. 当修饰一个**方法**，被修饰的方法称为同步方法，其作用的**范围是整个方法**，**作用的对象是调用这个方法的对象**； 
>
> 3. 虽然可以使用 synchronized 来定义方法，但 `synchronized` 并**不属于方法定义的一部分**，因此，**synchronized 关键字不能被继承**。
>
>    如果在父类中的某个方法使用了 `synchronized` 关键字，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上` synchronized` 关键字才可以。当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，因此， 子类的方法也就相当于同步了。
>
> 4. 修饰一个**静态的方法**，其作用的**范围是整个静态方法**，**作用的对象是这个类的所有对象**；  
> 5. 修饰一个**类**，**其作用的范围是 synchronized 后面括号括起来的部分**，**作用主的是对象，是这个类的所有对象**。 

通过`synchronized`关键字进行买票的案例

1. 创建资源类，拥有属性和被异步实现的方法
2. 创建多线程，调用资源类中的方法

```java
/**
 * 1. 创建资源类
 */
class Ticket{
    private int rest = 100;
    public synchronized void sale() {
        if (rest > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出一张票，还剩：" + --rest + "张；");
        }
    }
}

public class Two {
    public static void main(String[] args) {
        // 2. 创建线程使用资源类
        Ticket ticket = new Ticket();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 25; i++) {
                    ticket.sale();
                }
            }
        };
        new Thread(runnable,"A").start();
        new Thread(runnable,"B").start();
        new Thread(runnable,"C").start();
        new Thread(runnable,"D").start();
    }
}
```

没有出现重复买同一张票的情况

![image-20240222114536172](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240222114536172.png)

如果一个代码块被 `synchronized` 修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况： 

1. 获取锁的线程执行完了该代码块，然后线程释放对锁的占有；  
2. 线程执行发生异常，此时` JVM `会让线程自动释放锁。  

那么**如果这个获取锁的线程由于要等待` IO `或者其他原因**（比如调用` sleep` 方法）**被阻塞了**，但是又没有释放锁，**其他线程只能等待**，试想一 下，这多么**影响程序执行效率**。  因此就需要有一种机制可以不让等待的线程一直无期限地等待下去。比如只等待一定的时间或者能够响应中断，**通过 `Lock `就可以解决**。 

### lock使用

> `Lock` 不是 `Java `语言内置的，`synchronized` 是 `Java` 语言的关键字，因此是内置特性。
>
> `Lock` 是一个`类`，通过这个类可以实现同步访问；
>
> - `Lock` 和 `synchronized` 有一点非常大的不同，采用 `synchronized` 不需要用户去手动释放锁，当 `synchronized` 方法或者 `synchronized` 代码块执行完之后， 系统**会自动让线程释放对锁的占用**；
> - 而 `Lock` 则**必须要用户去手动释放锁**，如果没有主动释放锁，就有可能导致出现死锁现象。 

必须注意确保在锁定时执行的所有代码由 `try-finally` 或 `try-catch` 保护，以保证锁一定被被释放，防止死锁的发生。。

```java
// 创建可重入锁
private final ReentrantLock lock = new ReentrantLock();
try {
    //上锁
    lock.lock();
	//功能操作
    ...
}finally {
    //解锁
    lock.unlock();
}
```

