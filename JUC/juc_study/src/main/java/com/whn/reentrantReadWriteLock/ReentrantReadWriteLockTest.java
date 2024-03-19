package com.whn.reentrantReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 16:06
 * @Description: 读写锁
 */
public class ReentrantReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantResource myRescource = new ReentrantResource();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.write("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
                //myRescource.readWriteWrite("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
            },String.valueOf(i)).start();
        }

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.read("key"+String.valueOf(finalI));
                //myRescource.readWriteRead("key"+String.valueOf(finalI));
            },String.valueOf(i)).start();
        }


        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 显式读锁没有完成之前，写锁无法获取锁
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(()->{
                 myRescource.write("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
                //myRescource.readWriteWrite("key"+String.valueOf(finalI),"value"+String.valueOf(finalI));
            },"新读写锁"+String.valueOf(i)).start();
        }
    }
}


