package com.whn.volatile1;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 13:31
 * @Description: volatile 测试Demo
 */
public class VolatileTest {
    //public volatile int inc = 0;
    //public int inc = 0;
    public volatile AtomicInteger inc=new AtomicInteger(0);

    public void increase() {
        //inc++;
        inc.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        final VolatileTest test = new VolatileTest();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
                10,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    test.increase();
                }
            });
        }
        threadPoolExecutor.shutdown();
        //System.out.println(test.inc.get());
        System.out.println(test.inc); //每次结果都不一样
    }
}
