package com.whn.jucAuxiliaryClass.CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: WangHn
 * @Date: 2024/3/20 10:53
 * @Description: 计数器
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建CountDown对象并设置初始值
        CountDownLatch countDownLatch = new CountDownLatch(6);
        // 创建六个线程，模拟六个学生
        for (int i = 1; i <= 6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"离开教室");
                // 计数 -1
                countDownLatch.countDown();
            },String.valueOf(i)).start();
        }
        // 等待，直到达到零
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+"锁门");
    }
}

