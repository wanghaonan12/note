package com.whn;

/**
 * @Author: WangHn
 * @Date: 2024/3/5 17:08
 * @Description: 创建线程
 */
public class MyThread2 implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"：线程正在执行>>>>>>>>>>");
        }
    }
    public static void main(String[] args) {
     new Thread(new MyThread2()).start();
     new Thread(new MyThread2()).start();
     new Thread(new MyThread2()).start();
     new Thread(new MyThread2()).start();
    }
}
