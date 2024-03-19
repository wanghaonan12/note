package com.whn.reentrantReadWriteLock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 16:20
 * @Description: 读写锁资源类
 */
public class ReentrantResource {
    Map<String, String> map = new HashMap<>();
    // ========= ReentrantLock 等价于 ======== Synchronized
    Lock lock = new ReentrantLock();
    // ========= ReentrantReadWritLock =======读读共享
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void write(String key, String value) {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在写入。。。。");
            map.put(key,value);
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"\t完成写入。。。。");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            lock.unlock();
        }
    }
    public void readWriteWrite(String key, String value) {
        readWriteLock.writeLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在写入。。。。");
            map.put(key,value);
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println(Thread.currentThread().getName()+"\t完成写入。。。。");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            readWriteLock.writeLock().unlock();
        }
    }
    public void read(String key) {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在读入。。。。");
            String s = map.get(key);
            try {
                // 1. 暂停500毫秒
                // 2. 暂停2000毫秒，显式读锁没有完成之前，写锁无法获取锁
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println(Thread.currentThread().getName()+"\t完成读入。。。。\t"+s);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            lock.unlock();
        }
    }
    public void readWriteRead(String key) {
        readWriteLock.readLock().lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t正在读入。。。。");
            String s = map.get(key);
            try {
                // 1. 暂停500毫秒
                // 2. 暂停2000毫秒，显式读锁没有完成之前，写锁无法获取锁
                TimeUnit.MILLISECONDS.sleep(2000);
                System.out.println(Thread.currentThread().getName()+"\t完成读入。。。。\t"+s);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
}
