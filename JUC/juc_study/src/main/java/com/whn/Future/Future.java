package com.whn.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/20 13:50
 * @Description: 未来任务
 */
public class Future {
    public static void main(String[] args) throws Exception {
        //手动创建线程
        FutureTask<String> futureTask = new FutureTask<>( new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
            }
        });
        new Thread(futureTask).start();
        System.out.println(futureTask.get()+"new Thread");
        //---------------------------------------创建线程池------------------------------------------------------
        ExecutorService executorService = Executors.newScheduledThreadPool(10);
        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
                }
            });
        }

        List<java.util.concurrent.Future<String>> futures = executorService.invokeAll(tasks);
        futures.forEach(
                stringFuture -> {
                    try {
                        System.out.println(stringFuture.get()+"线程池");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        executorService.shutdown();

        //手动创建线程使用和新方法
        new Thread(new FutureTask<>( new Callable<String>() {
            @Override
            public String call() throws Exception {
                return Integer.valueOf((int) (Math.random() * 10 + 1)).toString();
            }
        })).start();
        System.out.println("指定时间内获取值，获取不到报异常："+futureTask.get(10,TimeUnit.MILLISECONDS));
        System.out.println("获取值："+futureTask.get());
        System.out.println("任务是否完成:"+futureTask.isDone());
        System.out.println("尝试取消此任务的执行，如果任务已经完成、已经取消或由于其他原因无法取消，则此尝试将失败:"+futureTask.cancel(false));
        System.out.println("任务是否被取消："+futureTask.isCancelled());


    }


}
