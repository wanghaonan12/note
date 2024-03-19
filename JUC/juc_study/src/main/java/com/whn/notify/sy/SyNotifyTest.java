package com.whn.notify.sy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:18
 * @Description: synchronized 同步锁通知测试
 */
public class SyNotifyTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个线程池对象
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        SyFactory factory = new SyFactory();
        threadPoolExecutor.execute(new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        threadPoolExecutor.execute(new Thread(()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        threadPoolExecutor.execute(new Thread(()->{
            for (int i = 0; i < 1000; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        factory.info();
    }
}
