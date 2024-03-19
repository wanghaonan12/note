package com.whn;

/**
 * @Author: WangHn
 * @Date: 2024/3/5 17:08
 * @Description: 创建线程
 */
public class MyThread extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"：线程正在执行>>>>>>>>>>");
        }
        super.run();
    }
    public static void main(String[] args) {
        new MyThread().start();
        new MyThread().start();
        new MyThread().start();
    }
}
