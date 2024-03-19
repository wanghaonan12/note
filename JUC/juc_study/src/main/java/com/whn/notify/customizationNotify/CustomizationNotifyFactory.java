package com.whn.notify.customizationNotify;

import com.whn.notify.Factory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:08
 * @Description: 虚假唤醒演示工厂
 * 工厂会生产指定数量的产品 ，当产品不足时，会自动调用工厂生产产品
 * 产品达到仓库上限时停止胜场
 */
public class CustomizationNotifyFactory extends Factory {
    /**
     * 仓库容量
     */
    private static final int MAX_PRODUCT_COUNT = 100;

    /**
     * 产品数量
     */
    private int productCount = 0;

    /**
     * 生产数量
     */
    private int productNum = 0;
    /**
     * 消费数量
     */
    private int consumNum = 0;

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
            while (tag != 0) {
                // 达到仓库上限等待消费处理，设置超时时间
                if (!condition1.await(1, TimeUnit.SECONDS)) {
                    // 检查是否超时
                    // 处理不满足预期的情况，比如抛出异常或者打印错误日志
                    System.err.println("线程" + Thread.currentThread().getName() + "线程嘎调了");
                    //终止执行
                    Thread.currentThread().interrupt();
                    // 退出当前方法
                    //return;
                }
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
            while (tag != 1) {
                //仓库产品不足等待生产
                // 达到仓库上限等待消费处理，设置超时时间
                if (!condition2.await(1, TimeUnit.SECONDS)) {
                    // 检查是否超时
                    // 在超时后检查tag的状态，如果不满足预期，则处理

                    // 处理不满足预期的情况，比如抛出异常或者打印错误日志
                    System.err.println("线程" + Thread.currentThread().getName() + "线程嘎调了");
                    //终止执行
                    Thread.currentThread().interrupt();

                    // 退出当前方法
                    //return;
                }
            }
            // 通知生产
            productCount--;
            tag++;
            System.out.println(Thread.currentThread().getName() + "线程消费产品-----------------------consume--------------------" + productCount);
            Thread.sleep(3000);
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume1() throws InterruptedException {
        lock.lock();
        try {
            while (tag != 2) {
                //仓库产品不足等待生产
                // 达到仓库上限等待消费处理，设置超时时间
                if (!condition3.await(1, TimeUnit.SECONDS)) {
                    // 检查是否超时
                    // 处理不满足预期的情况，比如抛出异常或者打印错误日志
                    System.err.println("线程" + Thread.currentThread().getName() + "线程嘎调了");
                    //终止执行
                    Thread.currentThread().interrupt();
                    // 退出当前方法
                    //return;
                }
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

    public void info() {
        System.out.println("生产了:" + productNum + "产品，消费了:" + consumNum + "产品。");
    }
}
