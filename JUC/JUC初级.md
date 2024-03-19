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

# 线程通信

## synchronized 和Lock 的线程通信

1. 关键字 `synchronized` 与` wait()`/`notify()`这两个方法一起使用可以实现等待/通 知模式

   用 notify()通知时，JVM 会随机唤醒某个等待的线程。**注意：如果有多个线程，并且设置了状态锁的时候使用`notify()`时会造成死锁问题，此时应使用`notifyAll()`**

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

# JUC 三大辅助类

## 1. 减少计数 CountDownLatch

CountDownLatch 类可以设置一个计数器，然后通过 countDown 方法来进行 减 1 的操作，使用 await 方法等待计数器不大于 0，然后继续执行 await 方法 之后的语句。

- CountDownLatch 主要有两个方法，当一个或多个线程调用 await 方法时，这 些线程会阻塞
- 其它线程调用 countDown 方法会将计数器减 1(调用 countDown 方法的线程 不会阻塞) 
- 当计数器的值变为 0 时，因 await 方法阻塞的线程会被唤醒，继续执行



## 2. 循环栅栏 CyclicBarrier

## 3. 信号灯 Semaphore
