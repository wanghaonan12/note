package com.whn.notify;

/**
 * @Author: WangHn
 * @Date: 2024/3/12 15:19
 * @Description: 工程类
 */
public class Factory {
    public synchronized void product() throws InterruptedException {
    }

    /**
     *消费
     */
    public synchronized void consume() throws InterruptedException {
    }

    public static Runnable productRunable(Integer count, Factory factory){
        return ()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.product();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
    public static Runnable consumeRunable(Integer count, Factory factory){
        return ()->{
            for (int i = 0; i < 500; i++) {
                try {
                    factory.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
