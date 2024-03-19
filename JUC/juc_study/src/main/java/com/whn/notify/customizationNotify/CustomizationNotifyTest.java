package com.whn.notify.customizationNotify;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 14:18
 * @Description: 虚假唤醒测试
 */
public class CustomizationNotifyTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个线程池对象
        CustomizationNotifyFactory factory = new CustomizationNotifyFactory();
        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                try {
                    factory.consume1();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"CC").start();


    }
}
