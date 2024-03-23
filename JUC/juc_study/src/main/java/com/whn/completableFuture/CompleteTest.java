package com.whn.completableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 10:37
 * @Description: 手动完成案例
 */
public class CompleteTest {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                // 模拟耗时操作
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 返回任务结果
            return "Thread Task Result";
        });

        // 通过其他路径获取任务结果后，手动完成 CompletableFuture
        String result = "Task Result";
        future.complete(result);

        // 获取任务结果
        future.thenAccept(System.out::println);// Task Result
    }
}
