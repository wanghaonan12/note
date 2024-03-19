package com.whn.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 15:39
 * @Description: 可重入锁
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1 acquired the lock");
                lock.lock();
                try {
                    System.out.println("Thread 1 acquired the lock again");
                }finally {
                    //lock.unlock();
                }
            }finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 2 acquired the lock");
            }finally {
                lock.unlock();
            }
        }).start();

        System.out.println("Main thread acquired the lock");
    }

}
