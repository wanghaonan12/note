package com.whn.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 11:38
 * @Description: 线程合并
 */
public class AllOfTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result 1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Result 2";
        });
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        combinedFuture.thenRun(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);

                // 在所有 CompletableFuture 完成后执行某些函数
                String result1 = future1.get();
                String result2 = future2.get();
                System.out.println("Combined Results: " + result1 + " and " + result2);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        // 等待所有 CompletableFuture 完成
        combinedFuture.join();
    }
}
