package com.whn.ThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author whn
 * @time 2025/5/9
 * @description LockSupport 线程工具类
 */
public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 阻塞子线程
            LockSupport.park();
            System.out.println("子线程执行完毕！");
        });
        thread.start();

        TimeUnit.SECONDS.sleep(2);
        System.out.println("主线程执行完毕！");
        // 唤醒子线程
        LockSupport.unpark(thread);
    }
}
