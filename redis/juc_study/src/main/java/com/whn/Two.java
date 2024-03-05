package com.whn;

/**
 * @Author: WangHn
 * @Date: 2024/2/22 11:34
 * @Description: 线程同步锁 Lock 和 synchronized
 */

/**
 * 1. 创建资源类
 */
class Ticket{
    private int rest = 100;
    public synchronized void sale() {
//        if (rest > 0) {
            System.out.println(Thread.currentThread().getName() + "卖出一张票，还剩：" + --rest + "张；");
//        }
    }
}

public class Two {
    public static void main(String[] args) {
        // 创建线程使用资源类
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
