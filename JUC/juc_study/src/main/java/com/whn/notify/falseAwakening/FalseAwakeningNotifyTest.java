package com.whn.notify.falseAwakening;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:18
 * @Description: 虚假唤醒测试
 */
public class FalseAwakeningNotifyTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个线程池对象
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        FalseAwakeningFactory factory = new FalseAwakeningFactory();
        new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"AA").start();
        new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"BB").start();
        new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"CC").start();
        new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"DD").start();
        //threadPoolExecutor.shutdown();
        //threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        //factory.info();
        /**
         * 打印出来的结果应该是 一个1 和 0 交替的
         */

    }
}
