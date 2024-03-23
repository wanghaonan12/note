package com.whn.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 10:43
 * @Description: 非阻塞调用
 */
public class ThenApplyTest {

    public static void main(String[] args) {
        long start = System.nanoTime();

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                // 使用更合适的方式模拟延迟，替代Thread.sleep
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.currentThread().interrupt();
                // 通过CompletableFuture传播异常
                throw new CompletionException(e);
            }
            return "Task Result";
        });

        // 在获取任务结果后执行额外的任务
        future.thenApply(result -> {
            try {
                // 使用更合适的延迟方式
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                // 恢复中断状态
                Thread.currentThread().interrupt();
                // 通过CompletableFuture传播异常
                throw new CompletionException(e);
            }
            return result + " with additional information";
        }).thenAccept(System.out::println).exceptionally(ex -> {
            // 处理异常，输出错误信息
            System.err.println("Error processing task: " + ex.getMessage());
            return null;
        });
        // 等待异步任务完成，以准确测量时间
        try {
            future.join();
        } catch (CompletionException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("Time taken: " + (end - start) / 1_000_000 + " milliseconds");
    }
}


