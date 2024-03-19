package com.whn.multiThread;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 13:57
 * @Description: 多线程锁问题演示
 */
public class MultiThread {
    public static void main(String[] args) throws Exception {
        Phone phone = new Phone();
        Phone phone2 = new Phone();
        new Thread(() -> {
            try {
                phone.sendSMS();
                //phone.sendEmail();
                //phone2.getHello("phone2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();
        Thread.sleep(100);
        new Thread(() -> {
            try {
                 //phone2.sendEmail();
                 //phone.getHello("phone");
                phone2.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();
    }
}
