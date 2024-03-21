# 进程线程

> 进程（Process）是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。在早期面向进程设计的计算机结构中，进程是程序的基本执行实体；在当代面向线程设计的计算机结构中，进程是线程的容器。程序是指令、数据及其组织形式的描述，进程是程序的实体。
>
> 线程（英语：thread）是操作系统能够进行运算调度的最小单位。它被包含在进程之中，是进程中的实际运作单位。一条线程指的是进程中一个单一顺序的控制流，一个进程中可以并发多个线程，每条线程并行执行不同的任务。

**总结**

> 进程：指在系统中正在运行的一个应用程序；程序一旦运行就是进程；进程是资源分配的最小单位
>
> 线程：系统分配处理器时间资源的基本单元，或者说进程之内独立执行的一个单元执行流。线程时程序执行的最小单位

## 1. 线程状态

java.lang.Thread 包下的内部类

> 可以看出NEW（新建）、RUNNABLE（准备就绪）、BLOCKED（阻塞）、WAITING（等待-不见不散）、TIMED_WAITING（定时等待-过时不候）、TERMINATED（终结）

![image-20240222110018638](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240222110018638.png)

## 2. sleep和wait

> 1. sleep是Thread的静态方法；wait是Object的方法，任何对象实例都能调用。
> 2. sleep不会释放锁，它也不需要占用锁；wait会释放锁，但调用它的前提是当前线程占有锁（即代码要在synchronized中）
> 3. 它们都可以被interrupt方法中断

## 3. 管程

> **管程 (Moniters，也称为监视器)**
> 保证了同一时刻只有一个进程在管程内活动，即管程内定义的操作在同一时刻只被一个进程调用(由编译器实现)。 `java` 中叫 锁

## 4. 守护线程用户线程

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

# Lock

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

## synchronized使用

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

## lock使用

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

# Future

[Java中「Future」接口详解 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/622375761)

Future表示异步计算的结果，提供了用于检查计算是否完成、等待计算完成、以及检索计算结果的方法。

案例

```java
public class Future {
    public static void main(String[] args) throws Exception {
        FutureTask<String> futureTask = new FutureTask<>( new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
            }
        });
        new Thread(futureTask).start();
        System.out.println(futureTask.get()+"new Thread");
        //---------------------------------------------------------------------------------------------
        ExecutorService executorService = Executors.newScheduledThreadPool(10);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
                }
            });
        }

        List<java.util.concurrent.Future<String>> futures = executorService.invokeAll(tasks);
        futures.forEach(
                stringFuture -> {
                    try {
                        System.out.println(stringFuture.get()+"线程池");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        executorService.shutdown();
    }
}
```

**核心方法**

- `get()`：等待任务完成，获取执行结果，如果任务取消会抛出异常；
- `get(long timeout, TimeUnit unit)`：指定等待任务完成的时间，等待超时会抛出异常；
- `isDone()`：判断任务是否完成；
- `isCancelled()`：判断任务是否被取消；
- `cancel(boolean mayInterruptIfRunning)`：尝试取消此任务的执行，如果任务已经完成、已经取消或由于其他原因无法取消，则此尝试将失败；`mayInterruptIfRunning` : 如果在执行的时候是否停止该线程的执行。

```java
        new Thread(new FutureTask<>( new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
            }
        })).start();
        System.out.println("指定时间内获取值，获取不到报异常："+futureTask.get(10,TimeUnit.MILLISECONDS));
        System.out.println("获取值："+futureTask.get());
        System.out.println("任务是否完成:"+futureTask.isDone());
        System.out.println("尝试取消此任务的执行，如果任务已经完成、已经取消或由于其他原因无法取消，则此尝试将失败:"+futureTask.cancel(false));
        System.out.println("任务是否被取消："+futureTask.isCancelled());

		//--------------------------执行结果-------------------------
        //指定时间内获取值，获取不到报异常：4
        //获取值：4
        //任务是否完成:true
        //尝试取消此任务的执行，如果任务已经完成、已经取消或由于其他原因无法取消，则此尝试将失败:false
        //任务是否被取消：false
```



# 线程通信

## synchronized 和Lock 的线程通信

1. 关键字 `synchronized` 与` wait()`/`notify()`这两个方法一起使用可以实现等待/通 知模式

   用 `notify()`通知时，`JVM` 会随机唤醒某个等待的线程。**注意：如果有多个线程，并且设置了状态锁的时候使用`notify()`时会造成死锁问题，此时应使用`notifyAll()`**

   ```bash
   - wait()会使当前线程等待,同时会释放锁，直到被唤醒，便从当前位置继续执行。
   - notify()用于随机唤醒一个等待的线程，从被锁主的地方继续执行。
   - notifyAll()用于唤醒全部等待的线程。
   ```

   下面的代码在使用2个线程以上分别执行`product()`和` consume()`的时候便可能会进如死锁。

   ```java
       /**
        *生产
        */
       public synchronized void product() throws InterruptedException {
           //修复虚假通知
           while (productCount != 0) {
               wait();
           }
           //通知消费
           productCount++;
           notify();
       }
   
       /**
        *消费
        */
       public synchronized void consume() throws InterruptedException {
           //修复虚假通知
           while (productCount != 1) {
               wait();
           }
           // 通知生产
           productCount--;
           notify();
       }
   ```

   这里调用的时候可能会`AA`线程生产之后`notify()`的唤醒通知发到了`BB`线程，而`BB`线程满足等待条件`productCount != 0`又会进行等待，没有现成进行消费所以就进入了死锁。解决：**将notify()换成notifyAll()即可**

   ```java
   new Thread(()->product(),"AA").start(); // 生产方法
   new Thread(()->product(),"BB").start(); // 生产方法
   new Thread(()->consume()},"CC").start(); // 消费方法
   new Thread(()->consume()},"DD").start(); // 消费方法
   ```

2. `Lock` 锁的 `newContition()`方法返回 `Condition` 对象，`Condition` 类 也可以实现等待/通知模式

   在**调用 `Condition` 的` await()`/`signal()`/`signalAll()`方法前，也需要线程持有相关的 Lock 锁**

   ```bash
   - await()会使当前线程等待,同时会释放锁，直到被唤醒，便从当前位置继续执行。
   - signal()用于唤醒一个等待的线程,不是随机。
   - signalAll()用于唤醒全部等待的线程。
   ```
   
   ```java
    	private Lock lock = new ReentrantLock();
       private Condition condition1 = lock.newCondition();
   
       /**
        * 生产
        */
       public void product() throws InterruptedException {
           lock.lock();
           try {
               if (productCount != 0) {
                   //达到仓库上限等待消费处理
                   condition1.await();
               }
               //通知消费
               productCount++;
               condition1.signalAll();
           } finally {
               lock.unlock();
           }
       }
   
       /**
        * 消费
        */
       public void consume() throws InterruptedException {
           lock.lock();
           try {
               if (productCount ！= 1) {
                   //仓库产品不足等待生产
                   condition1.await();
               }
               // 通知生产
               productCount--;
               condition1.signalAll();
           } finally {
               lock.unlock();
           }
       }
   ```
   
   这里的唤醒机制存在虚假唤醒不能保证线程一直让`productCount`处于1 / 0交替，因为在`AA`线程进行`signalAll()`时可能会被`BB`线程抢到并执行这时候便会进行生产操作，而再次`signalAll()`时才可能被消费方法抢到并执行。解决：将`if()`判断换成`while()`进行循环判断
   
   ```java
   new Thread(()->product(),"AA").start(); // 生产方法
   new Thread(()->product(),"BB").start(); // 生产方法
   new Thread(()->consume()},"CC").start(); // 消费方法
   new Thread(()->consume()},"DD").start(); // 消费方法
   ```
   
   

## 线程定制化通信

线程定制化通信需要添加一个标志位确定需要执行哪一个方法 下列代码中使用了tag作为标志位。

0的时候生产，1的时候consume消费，2的时候conusme1消费。

资源类

```java
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    /**
     * 标志
     */
    private Integer tag = 0;

    /**
     * 生产
     */
    @Override
    public void product() throws InterruptedException {
        lock.lock();
        try {
            while (tag !=0) {
                //达到仓库上限等待消费处理
                condition1.await();
            }
            //通知消费
            productCount += 2;
            tag++;
            System.out.println(Thread.currentThread().getName() + "线程生产产品+++++++++++++++++++++++++++++++++++++++++++" + productCount);
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费
     */
    @Override
    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (tag!=1) {
                //仓库产品不足等待生产
                condition2.await();
            }
            // 通知生产
            productCount--;
            tag++;
            System.out.println(Thread.currentThread().getName() + "线程消费产品-----------------------consume--------------------" + productCount);
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume1() throws InterruptedException {
        lock.lock();
        try {
            while (tag!=2) {
                //仓库产品不足等待生产
                condition3.await();
            }
            // 通知生产
            productCount--;
            tag = 0;
            System.out.println(Thread.currentThread().getName() + "线程消费产品-------------------consume1------------------------" + productCount);
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
```

执行代码

```java
        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.consume1();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"CC").start();
```

效果

```bash
AA线程生产产品+++++++++++++++++++++++++++++++++++++++++++2
BB线程消费产品-----------------------consume--------------------1
CC线程消费产品-------------------consume1------------------------0
AA线程生产产品+++++++++++++++++++++++++++++++++++++++++++2
BB线程消费产品-----------------------consume--------------------1
CC线程消费产品-------------------consume1------------------------0
```

### 隐患

当其中一个定制化通信的方法出现异常或是出现问题无法修改标志位的时候则整个线程就无法进行下去

假设AA线程在执行中无法将tag修改为1时接下来的线程将无法进行下去，将存在死锁现象，其他线程将持续等await()

**处理方法1：添加线程等待时间和触发条件进行强制执行(会出意外，不能一定能按照逻辑执行不推荐)**

```java
while (tag !=0) {
    // 达到仓库上限等待消费处理，设置超时时间1s 超过等待时间则返回false
    if (!condition1.await(1, TimeUnit.SECONDS)) {
        // 检查是否超时
        // 在超时后检查tag的状态，如果不满足预期，则处理
        if (tag != 0) {
            // 处理不满足预期的情况，比如抛出异常或者打印错误日志
            System.err.println("Product timed out waiting for tag to be 0.");
        }
        // 退出当前方法
        return;
    }
}
```

**处理方法2：添加线程等待时间终止线程**

```java
while (tag != 0) {
    // 达到仓库上限等待消费处理，设置超时时间
    if (!condition1.await(1, TimeUnit.SECONDS)) {
        // 处理不满足预期的情况，比如抛出异常或者打印错误日志
        System.err.println("线程" + Thread.currentThread().getName() + "线程嘎调了");
        //终止执行
        Thread.currentThread().interrupt();
    }
}
```

![image-20240315140618380](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240315140618380.png)

# 线程安全问题

## 问题引出

在学习集合线程安全之前，先来看一下为什么在多线程中会出现不安全。

以 ArrayList 为例，我们进入 ArrayList 源码，找到 add() 方法，源码如下

```java
public boolean add(E e) {
    modCount++;
    add(e, elementData, size);
    return true;
}   

private void add(E e, Object[] elementData, int s) {
    if (s == elementData.length)
        elementData = grow(); //扩容
    elementData[s] = e;
    size = s + 1;
}
```

显然，add() 方法没有使用同步互斥，所以在多线程并发是，会出现线程异常，测试代码如下

```java
ArrayList<String> list = new ArrayList<>();
for (int i = 0; i < 10; i++) {
    int finalI = i;
    new Thread(() -> {
        list.add(finalI +"");
        System.out.println(list);
    }).start();
}
```

![image-20240316111757980](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240316111757980.png)

解决该方法主要有三种，即使用这三个类：Vector、Collections、CopyOnWriteArrayList（常用）

## Vector

```java
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}
```

**使用方法**

```java
Vector<String> list = new Vector<>();
```

但是 Vector 用的不多，因为每次对添加的元素上锁，而且使用的是重量级锁synchronized是十分占用资源的，效率是十分低下的。其用法和 ArrayList 一样。

## Collections

进入 Collections 的底层，找到 synchronizedList(List list) 方法，源代码如下，synchronizedList(List list) 方法返回指定列表支持的同步（线程安全的）列表

```java
public static <T> List<T> synchronizedList(List<T> list) {
    return (list instanceof RandomAccess ?
            new SynchronizedRandomAccessList<>(list) :
            new SynchronizedList<>(list));
}

static <T> List<T> synchronizedList(List<T> list, Object mutex) {
    return (list instanceof RandomAccess ?
            new SynchronizedRandomAccessList<>(list, mutex) :
            new SynchronizedList<>(list, mutex));
}
```

**使用方法**

```java
List<Object> list = Collections.synchronizedList(new ArrayList<>());
```

## CopyOnWriteArrayList（常用）

这种方法用的最多。

`CopyOnWriteArrayList`涉及的底层原理为 写时复制技术

读的时候并发（多个线程操作）
写的时候独立，先复制相同的空间到某个区域，将其写到新区域，旧新合并，并且读新区域（每次加新内容都写到新区域，覆盖合并之前旧区域，读取新区域添加的内容）

首先我们对` CopyOnWriteArrayList `进行学习,其特点如下:  

它相当于线程安全的` ArrayList`。和 `ArrayList `一样，它是个可变数组；但是和` ArrayList `不同的时，它具有以下特性：

1. 它最**适合**于具有以下特征的应用程序：**`List `大小通常保持很小**，**只读操作远多 于可变操作**，需要在遍历期间防止线程间的冲突。
2. 它是**线程安全**的。
3.  因为通常需要复制整个基础数组，所以可变操作（add()、set() 和 remove()  等等）的开销很大。  
4. 迭代器支持 hasNext(), next()等不可变操作，但**不支持可变 remove()等操作**。 
5. 使用迭代器进行遍历的速度很快，并且不会与其他线程发生冲突。在构造迭代器时，迭代器依赖于不变的数组快照。 

进入 `CopyOnWriteArrayList` 底层，来看一下它是怎么实现的，其 `add() `源代码如下

```java
/**
* 向集合中添加一个元素 e。
* @param e 要添加的元素
* @return 总是返回 true，表示元素已被成功添加
*/
public boolean add(E e) {
    synchronized (lock) { // 使用锁来保证线程安全
        Object[] es = getArray(); // 获取当前集合的数组形式
        int len = es.length; // 获取数组的长度
        es = Arrays.copyOf(es, len + 1); // 复制数组，长度加1，为添加新元素做准备
        es[len] = e; // 将新元素 e 添加到数组的最后一个位置
        setArray(es); // 将修改后的数组设置回集合中
        return true;
    }
}
```

**使用方法**

```java
List<String> list = new CopyOnWriteArrayList<>();
```

## 总结

对比三者来看，`Vector`和`Collections`虽然也可以实现同步，但由于这两种方法在底层都使用了`synchronized`重量级锁，使其效率很低，所以对` ArrayList `的同步主要采用 `CopyOnWriteArrayList`

**集合线程安全对应表**

| 线程不安全 | 线程安全                      |
| ---------- | ----------------------------- |
| List<>();  | new CopyOnWriteArrayList<>(); |
| Set<>();   | new CopyOnWriteArraySet<>();  |
| Map<>();   | new ConcurrentHashMap<>();    |

`ConcurrentHashMap`与前两者实现方式不一样

```java
/**
* 插入键值对到ConcurrentHashMap中。如果key或value为null，则抛出NullPointerException。
* 如果onlyIfAbsent为true且键已存在，则不覆盖原有值，并返回原有的值。
* @param key  要插入的键，不可为null。
* @param value  要插入的值，不可为null。
* @param onlyIfAbsent  如果为true，且键已存在，则不插入新值。
* @return  如果插入了新值，返回null；如果onlyIfAbsent为true且键已存在，则返回已存在的值。
*/
final V putVal(K key, V value, boolean onlyIfAbsent) {
    // 检查key和value是否为null
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());  // 计算key的哈希值
    int binCount = 0;  // 用于统计所在桶中的节点数量
    // 循环开始，尝试插入键值对
    for (ConcurrentHashMap.Node<K,V>[] tab = table;;) {
        ConcurrentHashMap.Node<K,V> f; int n, i, fh; K fk; V fv;
        // 如果表未初始化，则初始化
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            // 尝试在空桶中添加节点
            if (casTabAt(tab, i, null, new ConcurrentHashMap.Node<K,V>(hash, key, value)))
                break;
        }
        else if ((fh = f.hash) == MOVED)
            // 如果桶已被移动，则帮助进行转移
            tab = helpTransfer(tab, f);
        else {
            // 如果不是空桶，并且不满足onlyIfAbsent条件，或者需要覆盖值，则进行同步操作
            V oldVal = null;
            synchronized (f) {
                // 再次检查节点是否仍存在
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        // 链表形式的节点插入或更新操作
                        binCount = 1;
                        for (ConcurrentHashMap.Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            ConcurrentHashMap.Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new ConcurrentHashMap.Node<K,V>(hash, key, value);
                                break;
                            }
                        }
                    }
                    else if (f instanceof ConcurrentHashMap.TreeBin) {
                        // 红黑树形式的节点插入或更新操作
                        ConcurrentHashMap.Node<K,V> p;
                        binCount = 2;
                        if ((p = ((ConcurrentHashMap.TreeBin<K,V>)f).putTreeVal(hash, key,
                                                                                value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                    else if (f instanceof ConcurrentHashMap.ReservationNode)
                        // 发现递归更新操作，抛出异常
                        throw new IllegalStateException("Recursive update");
                }
            }
            // 更新操作后的处理
            if (binCount != 0) {
                // 如果桶中节点数量达到阈值，转换为红黑树
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;  // 如果存在旧值，则返回旧值
                break;
            }
        }
    }
    // 更新计数器
    addCount(1L, binCount);
    return null;  // 如果没有返回旧值，则返回null
}
```

# 多线程锁

```java
class Phone {
    //public static synchronized void sendSMS() throws Exception {
    public synchronized void sendSMS() throws Exception {
        //停留 4 秒
        TimeUnit.SECONDS.sleep(4);
        System.out.println("------sendSMS");
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println("------sendEmail");
    }

    public void getHello(String string) {
        System.out.println("------getHello"+string);
    }
}
```

**测试方法**每一次的测试方法或是Phone其中的方法会有微微调整

```java
public class MultiThread {
    public static void main(String[] args) throws Exception {
        Phone phone = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> {
            try {
                //phone.sendEmail();
                phone2.getHello("phone2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();
        Thread.sleep(100);
        new Thread(() -> {
            try {
                 //phone.sendEmail();
                 phone.getHello("phone");
                //phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
    }
}
```

**现象1**

测试条件：同一对象先调用非静态同步`sendSMS`->后调用非静态同步`sendEmail`

现象：运行顺序取决于调用顺序

```bash
------sendEmail
------sendSMS
```

**现象2**

测试条件：同一对象先调用非静态同步`sendSMS`并且延迟4秒 ->后调用非静态同步`sendEmail`

现象：`sendEmail`方法会等待`sendSMS`方法运行完之后再运行

原因：`synchronized`锁住的是这个对象，在`sendSMS`方法占用锁的时候其他同步方法不可以获得锁

```bash
------sendSMS
------sendEmail
```

**现象3**

测试条件：同一对象先调用非静态同步`sendSMS`并且延迟4秒 ->后调用非静态非同步`getHello`

现象：`getHello`方法直接运行，`sendSMS`方法四秒后出现

原因：`getHello`因为是非同步方法所以不会锁限制，所以可以直接运行

```bash
------getHellophone
------sendSMS
```

**现象4**

测试条件：调用`对象1`的非静态同步 `sendSMS` 并延迟4秒->后调用`对象2`的非静态同步`sendEmail`

现象：`sendEmail`和`sendSMS`以各自的速度运行完成

原因：`synchronized`锁住的是相同对象的非静态方法，对与不同对象的非静态方法不起作用

```bash
------sendEmail
------sendSMS
```

**现象5**

测试条件：调用`对象2`的非静态非同步 `getHello` 并延迟4秒->后调用`对象`的非静态非同步`getHello`

现象：运行顺序取决于调用顺序

```bash
------getHellophone2
------getHellophone
```

**现象6**

测试条件：调用同一对象的静态同步`sendSMS` 并延迟四秒->后调用静态同步`sendEmail`

现象：`sendEmail`非静态先执行

原因：因为 `sendSMS()` 是静态同步方法，它锁住的是` Phone` 类，而非` phone `实例。所以，第二个线程仍然可以立即执行 `phone.sendEmail()`，因为它锁定的是 `phone` 实例，并不与静态同步方法冲突。

```bash
------sendEmail
------sendSMS
```

**现象7**

测试条件：调用`对象1`的静态同步 `sendSMS` 并延迟4秒->后调用`对象2`的非静态同步`sendEmail`

现象：`sendEmail`非静态先执行

原因：和现象6一样

```bash
------sendEmail
------sendSMS
```

**现象8**

测试条件：调用`对象1`的静态同步 `sendSMS` 并延迟4秒->后调用`对象2`的非静态同步`sendSMS`

现象：`sendSMS`会按照调用顺序执行

原因：`sendSMS`方法被`static`修饰并且是同步方法所以会锁主`Phone`的`class`在其中一个对象获取到了锁喉其他对象无法获取锁

```bash
------sendSMS
------sendSMS
```

**总结**

```bash
1. 同一对象访问不同的同步锁，是按照顺序执行
2. 同一对象访问同步锁与不同步锁，是先不同步锁执行
3. 同一对象访问不同静态同步锁，按照顺序执行
4. 同一对象访问一个静态同步锁，一个同步锁，先执行同步锁
5. 不同对象访问不同同步锁，按照顺序执行
6. 不同对象访问不同静态同步锁，按照顺序执行
7. 不同对象访问一个静态同步锁，一个同步锁，先执行同步锁，即先出同步锁在出静态同步锁
```

类的`非静态同步`方法锁住的是调用该方法的对象`this`,影响的是这个对象

类的`静态同步`方法锁住的是这个`class` ,影象整个使用整个`class`的所有对象及方法

## 可重入锁 ReentrantLock

`ReentrantLock`，意思是“可重入锁”，关于可重入锁的概念将在后面讲述。  `ReentrantLock `是唯一实现了` Lock` 接口的类，并且 `ReentrantLock` 提供了更 多的方法。下面通过一些实例看具体看一下如何使用。 

**注意：`ReentrantLock`获取锁和释放锁的次数一定要一样，否则会出现死锁的现象，下面的代码中就无法进入到第二个线程**

```java
public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 acquired the lock");
                lock.lock();
                try {
                    System.out.println("Thread 1 acquired the lock again");
                }finally {
                    //lock.unlock();
                }
            }finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 2 acquired the lock");
            }finally {
                lock.unlock();
            }
        }).start();

        System.out.println("Main thread acquired the lock");
    }

}
```

## 公平锁和非公平锁

看一下`ReentrantLock`的构造器,默认情况下创建了一个非公平锁，当使用第二个有参构造器的时候并且传入`true`可以创建公平锁。

公平锁和非公平锁是在获取锁的机制上有所区别。公平锁保证了线程按照请求锁的顺序被唤醒，而非公平锁则没有这个限制，可以由操作系统自由调度。在大多数情况下，非公平锁的性能要优于公平锁，因为它没有额外的开销来维护请求顺序。但在某些高并发的环境下，公平锁可以更好地保证线程的公平性，避免线程饥饿问题。

- **公平锁**：效率相对低 ，但是cpu 的利用高了
- **非公平锁**：效率高，但是线程容易饿死（所有的工作可能由一个线程完成）

```java
    /**
     * Creates an instance of {@code ReentrantLock}.
     * This is equivalent to using {@code ReentrantLock(false)}.
     */
    public ReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * Creates an instance of {@code ReentrantLock} with the
     * given fairness policy.
     *
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
```

## 读写锁ReadWriteLock 

现实中有这样一种场景：对共享资源有读和写的操作，且写操作没有读操作那么频繁。在没有写操作的时候，多个线程同时读一个资源没有任何问题，所以应该允许多个线程同时读取共享资源；但是如果一个线程想去写这些共享资源，就不应该允许其他线程对该资源进行读和写的操作了。

针对这种场景，JAVA的并发包提供了读写锁 `ReentrantReadWriteLock`，它表示两个锁，一个是读操作相关的锁，称为共享锁；一个是写相关的锁，称为排他锁  

`ReentrantLock` 实现了 `Lock `接口，`ReentrantReadWriteLock `实现了 `ReadWriteLock `接口。对于`ReentrantLock` 它的 **读读也是一个线程**访问，浪费资源。`ReentrantReadWriteLock` 可以**实现读读共享**！

```java
public class ReentrantResource {
    Map<String, String> map = new HashMap<>();
    // ========= ReentrantLock 等价于 ======== Synchronized
    Lock lock = new ReentrantLock();
    // ========= ReentrantReadWritLock =======读读共享
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void write(String key, String value) {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在写入。。。。");
            map.put(key,value);
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"\t完成写入。。。。");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            lock.unlock();
        }
    }
    public void readWriteWrite(String key, String value) {
        readWriteLock.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在写入。。。。");
            map.put(key,value);
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"\t完成写入。。。。");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }
    public void read(String key) {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在读入。。。。");
            String s = map.get(key);
            try {
                // 1. 暂停500毫秒
                // 2. 暂停2000毫秒，显式读锁没有完成之前，写锁无法获取锁
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println(Thread.currentThread().getName()+"\t完成读入。。。。\t"+s);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            lock.unlock();
        }
    }
    public void readWriteRead(String key) {
        readWriteLock.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在读入。。。。");
            String s = map.get(key);
            try {
                // 1. 暂停500毫秒
                // 2. 暂停2000毫秒，显式读锁没有完成之前，写锁无法获取锁
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println(Thread.currentThread().getName()+"\t完成读入。。。。\t"+s);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
}
```

测试类

```java
public class ReentrantReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantResource myRescource = new ReentrantResource();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.write("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
                //myRescource.readWriteWrite("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
            },String.valueOf(i)).start();
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.read("key"+String.valueOf(finalI));
                //myRescource.readWriteRead("key"+String.valueOf(finalI));
            },String.valueOf(i)).start();
        }


        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 显式读锁没有完成之前，写锁无法获取锁
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.write("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
                //myRescource.readWriteWrite("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
            },"新读写锁"+String.valueOf(i)).start();
        }
    }
}
```

**总结**

> 1. 在线程持有读锁的情况下，该线程不能取得写锁(因为获取写锁的时候，如果发现当前的读锁被占用，就马上获取失败，不管读锁是不是被当前线程持有)。 
> 2. 在线程持有写锁的情况下，该线程可以继续获取读锁（获取读锁时如果发现写锁被占用，只有写锁没有被当前线程占用的情况才会获取失败）。
>    - 原因: 当线程获取读锁的时候，可能有其他线程同时也在持有读锁，因此不能把获取读锁的线程“升级”为写锁；而对于获得写锁的线程，它一定独占了读写锁，因此可以继续让它获取读锁，当它同时获取了写锁和读锁后，还可以先释放写锁继续持有读锁，这样一个写锁就“降级”为了读锁。  

# JUC 三大辅助类

## 1. 减少计数 CountDownLatch

CountDownLatch 类可以设置一个计数器，然后通过 countDown 方法来进行 减 1 的操作，使用 await 方法等待计数器不大于 0，然后继续执行 await 方法 之后的语句。

- CountDownLatch 主要有两个方法，当一个或多个线程调用 await 方法时，这 些线程会阻塞
- 其它线程调用 `countDown` 方法会将计数器减 1(调用 `countDown` 方法的线程 不会阻塞) 
- 当计数器的值变为 0 时，因 await 方法阻塞的线程会被唤醒，继续执行

---

>  案例学生全出门关闭教室门

```java
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建CountDown对象并设置初始值
        //CountDownLatch countDownLatch = new CountDownLatch(6);
        // 创建六个线程，模拟六个学生
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"离开教室");
                // 计数 -1
                //countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        // 等待，直到达到零
        //countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"锁门");
    }
}
```

![image-20240320105600010](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240320105600010.png)

> 当没有启用计数器的时候主线程的关门没有等待学生全部出去，便把门关上了，这不符合业务需求
> 启用计数器之后便会通过`countDown` 方法计算，当(学生)计数器归零便放开主线程的`awaiat`方法。
> 解决了线程混乱的问题。

## 2. 循环栅栏 CyclicBarrier

`CyclicBarrier` 看英文单词可以看出大概就是循环阻塞的意思，在使用中 `CyclicBarrier` 的构造方法第一个参数是目标障碍数，每次执行 `CyclicBarrier` 一 次障碍数会加一，如果达到了目标障碍数，才会执行 `cyclicBarrier.await()`之后 的语句。可以将 `CyclicBarrier` 理解为加 1 操作 

该类是 **允许一组线程** 互相 等待，直到到达某个公共屏障点，在设计一组固定大小的线程的程序中，这些线程必须互相等待，因为barrier在释放等待线程后可以重用，所以称为循环barrier

---

> 集齐七龙珠召唤神龙

```java
public class CyclicBarrierTest {
    // 创建固定值
    private static final int NUMBER  = 7;
    public static void main(String[] args) {
        // 每次执行 CyclicBarrier 一次障碍数会加一，如果达到了目标障碍数，才会执行 cyclicBarrier.await()之后的语句。
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, () -> {
            System.out.println("****集齐7颗龙珠就可以召唤神龙");
        });
        // 创建六个线程，模拟六个学生
        for (int i = 1; i <= 7; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 星龙被收集到了");
                try {
                    // 计数 +1
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            },String.valueOf(i)).start();
        }
    }
}
```

| 特性/工具类    | CountDownLatch                                          | CyclicBarrier                                                |
| -------------- | ------------------------------------------------------- | ------------------------------------------------------------ |
| 定义           | 用于一次性等待多个线程完成任务的同步工具                | 用于多线程互相等待至某一状态再共同执行的可循环使用的同步工具 |
| 计数器用途     | 计数值递减至0时释放所有等待线程，递减后不可复原         | 标记参与线程数量，所有线程调用await后计数复位                |
| 使用场景       | 适用于一次性启动多个线程并等待它们全部完成后执行下一步  | 适用于多阶段任务，每个阶段都需要所有线程完成后再进入下一阶段 |
| 计数器可重置性 | 不可重置，一次性的计数行为                              | 可重置，达成一致后可再次进行新一轮的等待                     |
| 执行回调任务   | 不支持在所有线程完成时自动执行回调函数                  | 支持在所有线程到达屏障点时执行自定义的Runnable任务           |
| 使用方法       | 调用`countDown()`方法减少计数，`await()`方法等待计数为0 | 调用`await()`方法使当前线程等待所有线程到达屏障点            |

## 3. 信号灯 Semaphore

​	一个计数信号量，从概念上将，信号量维护了一个许可集，如有必要，在许可可用前会阻塞每一个`acquire()`，然后在获取该许可。每个`release()`释放一个许可，从而可能释放一个正在阻塞的获取者。但是，不使用实际的许可对象，`Semaphore`只对可用许可的号码进行计数，并采取相应的行动
​	`Semaphore`的构造方法中传入的第一个参数是最大信号量（可以看成最大线程池），每个信号量初始化为一个最多只能分发一个许可证。使用 `acquire`方法获得许可证，`release`方法释放许可 

---

> 场景: 抢车位, 6部汽车 3个停车位 

```java
public class SemaphoreTest {
    public static void main(String[] args) {
        //创建Semaphore，设置许可数量
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                try {
                    // 抢占
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"抢到了车位");
                    // 设置停车时间
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    // 离开车位
                    System.out.println(Thread.currentThread().getName()+"------离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
```

# 阻塞队列

## 简介

`Concurrent`包中，`BlockingQueue`很好的解决了多线程中，如何高效安全**传输**数据的问题。通过这些高效并且线程安全的队列类，为我们快速搭建高质量的多线程程序带来极大的便利。

阻塞队列，顾名思义，首先它**是一个队列**, 通过一个**共享的队列**，可以使得数据由队列的**一端输入**，从另外**一端输出**；

 <img src="https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240320150648870.png" width="" height="">

> 试图从空的队列中获取元素的线程将会被阻塞，直到其他线程往空的队列插入新的元素 
>
> 试图向已满的队列中添加新元素的线程将会被阻塞，直到其他线程从队列中移除一个或多个元素或者完全清空，使队列变得空闲起来并后续新增 
>
> 常用的队列主要有以下两种：
>
> - 先进先出（FIFO）：先插入的队列的元素也最先出队列，类似于排队的功能。从某种程度上来说这种队列也体现了一种公平性 
> - 后进先出（LIFO）：后插入队列的元素最先出队列，这种队列优先处理最近发生的事件(栈) 

## 为什么需要 BlockingQueue 

> 好处是我们不需要关心什么时候需要阻塞线程，什么时候需要唤醒线程，因为这一切`BlockingQueue`都给你一手包办了。在`concurrent`包发布以前，在多线程环境下，我们每个程序员都必须去自己控制这些细节，尤其还要兼顾效率和线程安全，而这会给我们的程序带来不小的复杂度。 

**场景**

> 多线程环境中，通过队列可以很容易实现数据共享，比如经典的“生产者”和“消费者”模型中，通过队列可以很便利地实现两者之间的数据共享。假设我们有若干生产者线程，另外又有若干个消费者线程。如果生产者线程需要把准备好的数据共享给消费者线程，利用队列的方式来传递数据，就可以很方便地解决他们之间的数据共享问题。但如果生产者和消费者在某个时间段内，万一发生数据处理速度不匹配的情况呢？理想情况下，如果生产者产出数据的速度大于消费者消费的速度，并且当生产出来的数据累积到一定程度的时候，那么生产者必须暂停等待一下（阻塞生产者线程），以便等待消费者线程把累积的数据处理完毕，反之亦然

## BlockingQueue 核心方法

![image-20240320152204206](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240320152204206.png)

![image-20240320152212921](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240320152212921.png)

| 操作                                    | 描述                                                 | 备注                                                         |
| --------------------------------------- | ---------------------------------------------------- | ------------------------------------------------------------ |
| 添加数据                                |                                                      |                                                              |
| add(E e)                                | 将元素添加到队列尾部，如果队列已满则抛出异常         | 异常：IllegalStateException（队列已满）、InterruptedException（队列被中断） |
| offer(anObject)                         | 将对象添加到队列，若队列已满则返回false              |                                                              |
| offer(E o, long timeout, TimeUnit unit) | 尝试在指定时间内添加元素到队列                       | 如果超时仍未成功加入队列，则返回失败                         |
| put(anObject)                           | 将对象添加到队列，若队列已满则阻塞直到队列有空间可用 | 当前线程被阻塞直到队列有空间可用                             |
| 获取数据                                |                                                      |                                                              |
| poll(time)                              | 取出队首对象，若无立即取出则等待指定时间             | 超时返回null                                                 |
| poll(long timeout, TimeUnit unit)       | 从队列取出队首对象                                   | 等待指定时间，超时返回失败                                   |
| take()                                  | 取出队首对象，若队列为空则阻塞直到队列有数据可取     | 当前线程被阻塞直到队列有数据可取                             |
| drainTo()                               | 一次性获取所有可用数据对象，提升获取数据效率         | 可指定获取数据个数，减少加锁释放锁的开销                     |

## 阻塞队列分类

| 并发队列                | 介绍                                                         | 使用场景                                                     |
| ----------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `ArrayBlockingQueue`    | 基于数组实现的有界阻塞队列。按照先进先出（FIFO）顺序排序。   | 适用于需要有限容量且按照FIFO顺序操作的场景，如线程池任务队列。 |
| `LinkedBlockingQueue`   | 基于链表实现的可选有界或无界阻塞队列。按照FIFO顺序排序。     | 适用于需要无限容量或大容量的场景，如生产者-消费者模型。      |
| `DelayQueue`            | 无界阻塞队列，存放实现了Delayed接口的对象，按照延迟时间排序。 | 适用于需要按照延迟时间执行任务的场景，如定时任务调度。       |
| `PriorityBlockingQueue` | 基于优先级堆的无界阻塞队列。元素按照自然顺序或比较器排序。   | 适用于需要按照优先级顺序操作的场景，如任务调度中的优先级队列。 |
| `SynchronousQueue`      | 没有存储元素的阻塞队列。每个插入操作必须等待对应的移除操作。 | 适用于直接传递数据的场景，如线程间数据传输。                 |
| `LinkedTransferQueue`   | 无界队列，支持非阻塞和阻塞式传输。基于链表实现。             | 适用于需要灵活的数据传输场景，如异步任务执行。               |
| `LinkedBlockingDeque`   | 双向阻塞队列，由链表实现。支持队列和栈操作。                 | 适用于需要在队列两端插入和移除元素的场景，如工作流管理。     |

**ArrayBlockingQueue(常用)** 

> ​	基于数组的阻塞队列实现，在 `ArrayBlockingQueue` 内部，维护了一个定长数组，以便缓存队列中的数据对象，这是一个常用的阻塞队列，除了一个定长数 组外，`ArrayBlockingQueue` 内部还保存着两个整形变量，分别标识着队列的头部和尾部在数组中的位置。  ``ArrayBlockingQueue` 在生产者放入数据和消费者获取数据，都是共用同一个锁对象，由此也意味着两者无法真正并行运行，这点尤其不同于 `LinkedBlockingQueue`；
>
> ​	按照实现原理来分析，`ArrayBlockingQueue` 完全可以采用分离锁，从而实现生产者和消费者操作的完全并行运行。`Doug Lea `之 所以没这样去做，也许是因为 `ArrayBlockingQueue` 的数据写入和获取操作已经足够轻巧，以至于引入独立的锁机制，除了给代码带来额外的复杂性外，其 在性能上完全占不到任何便宜。 `ArrayBlockingQueue` 和 `LinkedBlockingQueue` 间还有一个明显的不同之处在于，前者在插入或删除 元素时不会产生或销毁任何额外的对象实例，而后者则会生成一个额外的` Node` 对象。这在长时间内需要高效并发地处理大批量数据的系统中，其对于 `GC `的影响还是存在一定的区别。而在创建 `ArrayBlockingQueue` 时，我们还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁。
>
> 一句话总结: 由数组结构组成的有界阻塞队列。

 **LinkedBlockingQueue(常用)** 

> 基于链表的阻塞队列，同 `ArrayListBlockingQueue` 类似，其内部也维持着一 个数据缓冲队列（该队列由一个链表构成），当生产者往队列中放入一个数据 时，队列会从生产者手中获取数据，并缓存在队列内部，而生产者立即返回； 只有当队列缓冲区达到最大值缓存容量时（`LinkedBlockingQueue` 可以通过 构造函数指定该值），才会阻塞生产者队列，直到消费者从队列中消费掉一份数据，生产者线程会被唤醒，反之对于消费者这端的处理也基于同样的原理。 而` LinkedBlockingQueue `之所以能够高效的处理并发数据，还因为其对于生 产者端和消费者端分别采用了独立的锁来控制数据同步，这也意味着在高并发 的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列 的并发性能。`  ArrayBlockingQueue` 和 `LinkedBlockingQueue` 是两个最普通也是最常用 的阻塞队列，一般情况下，在处理多线程间的生产者消费者问题，使用这两个 类足以。  
>
> 一句话总结: 由链表结构组成的有界（但大小默认值为 integer.MAX_VALUE）阻塞队列。

**DelayQueue** 

> `DelayQueue` 中的元素只有当其指定的延迟时间到了，才能够从队列中获取到 该元素。`DelayQueue` 是一个没有大小限制的队列，因此往队列中插入数据的 操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻 塞。
>
>   一句话总结: 使用优先级队列实现的延迟无界阻塞队列。

**PriorityBlockingQueue**

> `PriorityBlockingQueue`  基于优先级的阻塞队列（优先级的判断通过构造函数传入的` Compator `对象来 决定），但需要注意的是 `PriorityBlockingQueue` 并不会阻塞数据生产者，而 只会在没有可消费的数据时，阻塞数据的消费者。因此使用的时候要特别注意，生产者生产数据的速度绝对不能快于消费者消费 数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。  在实现 `PriorityBlockingQueue`时，内部控制线程同步的锁采用的是公平锁。
>
> 一句话总结: 支持优先级排序的无界阻塞队列。

**SynchronousQueue**

> `SynchronousQueue`    一种无缓冲的等待队列，类似于无中介的直接交易，有点像原始社会中的生产 者和消费者，生产者拿着产品去集市销售给产品的最终消费者，而消费者必须 亲自去集市找到所要商品的直接生产者，如果一方没有找到合适的目标，那么 对不起，大家都在集市等待。相对于有缓冲的 `BlockingQueue` 来说，少了一 个中间经销商的环节（缓冲区），如果有经销商，生产者直接把产品批发给经 销商，而无需在意经销商最终会将这些产品卖给那些消费者，由于经销商可以 库存一部分商品，因此相对于直接交易模式，总体来说采用中间经销商的模式 会吞吐量高一些（可以批量买卖）；但另一方面，又因为经销商的引入，使得 产品从生产者到消费者中间增加了额外的交易环节，单个产品的及时响应性能 可能会降低。  声明一个 `SynchronousQueue` 有两种不同的方式，它们之间有着不太一样的 行为。  公平模式和非公平模式的区别:  • 公平模式：`SynchronousQueue` 会采用公平锁，并配合一个 `FIFO` 队列来阻塞 多余的生产者和消费者，从而体系整体的公平策略；  • 非公平模式（`SynchronousQueue` 默认）：`SynchronousQueue` 采用非公平 锁，同时配合一个 `LIFO` 队列来管理多余的生产者和消费者，而后一种模式， 如果生产者和消费者的处理速度有差距，则很容易出现饥渴的情况，即可能有 某些生产者或者是消费者的数据永远都得不到处理。
>
>   一句话总结: 不存储元素的阻塞队列，也即单个元素的队列。

**LinkedTransferQueue**  

> `LinkedTransferQueue` 是一个由链表结构组成的无界阻塞 `TransferQueue` 队 列。相对于其他阻塞队列，`LinkedTransferQueue` 多了 `tryTransfer` 和 `transfer` 方法。  `LinkedTransferQueue` 采用一种预占模式。意思就是消费者线程取元素时，如 果队列不为空，则直接取走数据，若队列为空，那就生成一个节点（节点元素 为 `null`）入队，然后消费者线程被等待在这个节点上，后面生产者线程入队时 发现有一个元素为 `null` 的节点，生产者线程就不入队了，直接就将元素填充到   该节点，并唤醒该节点等待的线程，被唤醒的消费者线程取走元素，从调用的 方法返回。
>
>   一句话总结: 由链表组成的无界阻塞队列。

**LinkedBlockingDeque  **

> ` LinkedBlockingDeque` 是一个由链表结构组成的双向阻塞队列，即可以从队 列的两端插入和移除元素。  对于一些指定的操作，在插入或者获取队列元素时如果队列状态不允许该操作 可能会阻塞住该线程直到队列状态变更为允许操作，这里的阻塞一般有两种情 况  • 插入元素时: 如果当前队列已满将会进入阻塞状态，一直等到队列有空的位置时 再讲该元素插入，该操作可以通过设置超时参数，超时后返回 `false` 表示操作 失败，也可以不设置超时参数一直阻塞，中断后抛出 `InterruptedException` 异 常  • 读取元素时: 如果当前队列为空会阻塞住直到队列不为空然后返回元素，同样可 以通过设置超时参数
>
>  一句话总结: 由链表组成的双向阻塞队列

**小结** 

> 1. 在多线程领域：所谓阻塞，在某些情况下会挂起线程（即阻塞），一旦条件 满足，被挂起的线程又会自动被唤起  
>
> 2. 为什么需要 `BlockingQueue`?
>
>     在 `concurrent` 包发布以前，在多线程环境下， 我们每个程序员都必须去自己控制这些细节，尤其还要兼顾效率和线程安全， 而这会给我们的程序带来不小的复杂度。使用后我们不需要关心什么时候需要 阻塞线程，什么时候需要唤醒线程，因为这一切 `BlockingQueue` 都给你一手 包办了

## 案例

> 一个线程用来创建订单信息，另一个线程用消费。每个线程都有随机的延时，模拟业务操作耗时，队列会根据订单情况进行消费。

```java
// 订单类
class Order {
    private String orderId;
    private String userId;

    public Order(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }
}

// 订单生成者
class OrderProducer implements Runnable {
    private ArrayBlockingQueue<Order> orderQueue;

    public OrderProducer(ArrayBlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order("Order" + i, "User" + i);
            try {
                // 将订单放入订单队列中
                orderQueue.put(order);
                int sleepTime = (new Random().nextInt(5) + 1);
                System.out.println("创建订单消耗：" + sleepTime + "秒++++++++++++++");
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 订单处理任务
class OrderTask implements Runnable {
    private Order order;

    public OrderTask(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        System.out.println("Processing order " + order.getOrderId() + " for user " + order.getUserId());
        // 在这里添加订单处理逻辑
        try {
            // 模拟订单处理时间
            int sleepTime = (new Random().nextInt(5) + 1);
            System.out.println("处理订单消耗：" + sleepTime + "秒-----------------------");
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order " + order.getOrderId() + " processed successfully.");
    }
}

public class ShoppingCart {
    public static void main(String[] args) {
        // 创建一个有界阻塞队列，用于存放订单任务
        ArrayBlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(10);

        // 创建一个固定大小的线程池来处理订单
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 创建订单生产者线程
        OrderProducer orderProducer = new OrderProducer(orderQueue);
        Thread producerThread = new Thread(orderProducer);
        producerThread.start();
        // 处理订单任务
        //while (orderQueue.size()>0) {
        // 恒为真 因为订单创建也做了延迟模拟 如果没有检索到集合中存在值的时候便会跳过关闭线程池
        while (true) {
            Order order = null;
            try {
                // 从订单队列中取出订单任务并提交给线程池处理
                order = orderQueue.take();
                executor.submit(new OrderTask(order));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        //executor.shutdown();
    }
}
```

![image-20240321092618388](C:/Users/wangRich/AppData/Roaming/Typora/typora-user-images/image-20240321092618388.png)
