package com.whn.threadPool;

/**
 * @Author: WangHn
 * @Date: 2024/3/22 16:20
 * @Description: 自定义创建线程
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 入门案例
 */
public class ThreadPoolDemo1 {

    /**
     * 火车站 3 个售票口, 10 个用户买票
     * @param args
     */
    public static void main(String[] args) {
        //定时线程次:线程数量为 3---窗口数为 3
        ExecutorService threadService = new ThreadPoolExecutor(3,
                5,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),

                new ThreadPoolExecutor.DiscardOldestPolicy());
        try {
            //10 个人买票
            for (int i = 1; i <= 10; i++) {
                threadService.execute(()->{
                    try {
                        System.out.println(Thread.currentThread().getName() + "窗口,开始卖票");
                                Thread.sleep(5000);
                        System.out.println(Thread.currentThread().getName() + "窗口买票结束");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //完成后结束
            threadService.shutdown();
        }
    }
}
