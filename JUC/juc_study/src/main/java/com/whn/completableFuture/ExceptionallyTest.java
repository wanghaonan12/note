package com.whn.completableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: WangHn
 * @Date: 2024/3/23 11:44
 * @Description: 异常处理
 */
public class ExceptionallyTest {
    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 模拟任务抛出异常
            System.out.println("Task is running");
            return "Result"+(1 / 0);
        });

        // 异常处理
        future.exceptionally(ex -> {
            System.out.println("Exception occurred: " + ex.getMessage());
            return "default result"; // 返回默认值或者执行其他处理
        });

        future.join(); // 等待任务完成
        future.thenAccept(System.out::println); // 输出结果是默认值，而不是异常结果
    }
}
