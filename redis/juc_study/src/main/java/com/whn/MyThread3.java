package com.whn;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: WangHn
 * @Date: 2024/3/5 17:08
 * @Description: 创建线程
 */
public class MyThread3 implements Callable<Integer> {
    /*
   1.实现Callable接口，可以不带泛型，如果不带泛型，那么call方式的返回值就是Object类型
   2.如果带泛型，那么call的返回值就是泛型对应的类型
   3.从call方法看到：方法有返回值，可以跑出异常
    */
    @Override
    public Integer call() throws Exception {
        return new Random().nextInt(10);//返回10以内的随机数
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("主线程111");
        FutureTask futureTask = new FutureTask(new MyThread3());
        new Thread(futureTask).start();
        System.out.println(futureTask.get().toString());
        System.out.println("主线程222");
    }
}
