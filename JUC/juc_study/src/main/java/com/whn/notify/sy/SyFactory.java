package com.whn.notify.sy;

import com.whn.notify.Factory;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:08
 * @Description: 消费工厂
 * 工厂会生产指定数量的产品 ，当产品不足时，会自动调用工厂生产产品
 * 产品达到仓库上限时停止胜场
 */
public class SyFactory extends Factory {
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
        while (productCount >= MAX_PRODUCT_COUNT) {
            //达到仓库上限等待消费处理
            wait();
        }
        //通知消费
        notify();
        productCount++;
        productNum++;
        System.out.println(Thread.currentThread().getName() + "线程生产产品+++++++++++++++++++++++++++++++++++++++++++" + productCount);
    }

    /**
     *消费
     */
    @Override
    public synchronized void consume() throws InterruptedException {
        while (productCount <= 0) {
            //仓库产品不足等待生产
            wait();
        }
        // 通知生产
        notify();
        productCount--;
        consumNum++;
        System.out.println(Thread.currentThread().getName() + "线程消费产品-------------------------------------------" + productCount);
    }

    public void info() {
        System.out.println("生产了:" + productNum + "产品，消费了:" + consumNum + "产品。");
    }
}
