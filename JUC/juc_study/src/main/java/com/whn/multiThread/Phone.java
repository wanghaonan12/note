package com.whn.multiThread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 13:58
 * @Description: 异步方法类
 */
class Phone {
    public static synchronized void sendSMS() throws Exception {
    //public synchronized void sendSMS() throws Exception {
        //停留 4 秒
        TimeUnit.SECONDS.sleep(4);
        System.out.println("------sendSMS");
    }

    public synchronized void sendEmail() throws Exception {
    //public synchronized void sendEmail() throws Exception {
        System.out.println("------sendEmail");
    }

    public void getHello(String string) {
        System.out.println("------getHello"+string);
    }
}
