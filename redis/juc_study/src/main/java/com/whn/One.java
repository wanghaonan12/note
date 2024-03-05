package com.whn;

import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/2/22 10:56
 * @Description: 用户线程与守护线程
 */
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
        t1.setDaemon(false);
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
