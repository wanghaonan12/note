package com.whn.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 11:08
 * @Description: 链式调用
 */
public class ThenComposeTest {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "123456789";
        });
        // 链式调用
        CompletableFuture<Integer> chainedFuture = future.thenCompose(result ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return result.length();
                }));

        chainedFuture.join();
        chainedFuture.thenAccept(System.out::println);//9
    }
}
