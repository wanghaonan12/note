package com.whn.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: WangHn
 * @Date: 2024/3/22 14:53
 * @Description: 线程池练习
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        ExecutorService threadPool3 = Executors.newCachedThreadPool();

        // 是个顾客请求
        try {
            for (int i = 1; i <= 10; i++) {
                // 到此时执行execute()方法才创建线程
                threadPool2.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " 办理业务");
                });
            }
        } finally {
            // 关闭线程
            threadPool1.shutdown();
            threadPool2.shutdown();
        }
    }
}


