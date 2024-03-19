package com.whn.notify.falseAwakening;

import com.whn.notify.Factory;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:08
 * @Description: 虚假唤醒演示工厂
 * 工厂会生产指定数量的产品 ，当产品不足时，会自动调用工厂生产产品
 * 产品达到仓库上限时停止胜场
 */
public class FalseAwakeningFactory extends Factory {
    /**
     * 仓库容量
     */
    private static final int MAX_PRODUCT_COUNT = 100;

    /**
     * 产品数量
     */
    private int productCount = 0;

    /**
     * 生产数量
     */
    private int productNum = 0;
    /**
     * 消费数量
     */
    private int consumNum = 0;

    /**
     * 生产
     */
    @Override
    public synchronized void product() throws InterruptedException {
        //有产品的时候等待
        //if (productCount != 0) {
        //    wait();
        //}
        //修复虚假通知
        while (productCount != 0) {
            wait();
        }
        //通知消费
        productCount++;
        productNum++;
        notifyAll();
        System.out.println(Thread.currentThread().getName() + "+++++++product++++++>" + productCount);
    }

    /**
     *消费
     */
    @Override
    public synchronized void consume() throws InterruptedException {
        // 没有产品的时候等待
        //if (productCount != 1) {
        //    wait();
        //}
        //修复虚假通知
        while (productCount != 1) {
            wait();
        }
        // 通知生产
        productCount--;
        consumNum++;
        notifyAll();
        System.out.println(Thread.currentThread().getName() + "-------consume------>" + productCount);
    }

    public void info() {
        System.out.println("生产了:" + productNum + "产品，消费了:" + consumNum + "产品。");
    }
}
