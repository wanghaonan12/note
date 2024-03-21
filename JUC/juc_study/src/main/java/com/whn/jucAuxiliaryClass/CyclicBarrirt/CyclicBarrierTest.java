package com.whn.jucAuxiliaryClass.CyclicBarrirt;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author: WangHn
 * @Date: 2024/3/20 13:16
 * @Description: 循环阑珊
 */
public class CyclicBarrierTest {
    // 创建固定值
    private static final int NUMBER  = 7;
    public static void main(String[] args) {
        // 每次执行 CyclicBarrier 一次障碍数会加一，如果达到了目标障碍数，才会执行 cyclicBarrier.await()之后的语句。
        CyclicBarrier cyclicBarrier = new CyclicBarrier(NUMBER, () -> {
            System.out.println("****集齐7颗龙珠就可以召唤神龙");
        });
        // 创建六个线程，模拟六个学生
        for (int i = 1; i <= 7; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 星龙被收集到了");
                try {
                    // 计数 +1
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            },String.valueOf(i)).start();
        }
    }
}

