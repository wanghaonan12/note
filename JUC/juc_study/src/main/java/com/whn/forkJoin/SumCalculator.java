package com.whn.forkJoin;

/**
 * @Author: WangHn
 * @Date: 2024/3/22 17:18
 * @Description: 拆分合并
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class SumCalculator extends RecursiveTask<Long> {
    /**
     * 阈值，用于决定何时进行任务拆分
     */
    private static final int THRESHOLD = 1000;
    /**
     * 数据数组
     */
    private int[] array;
    /**
     * 开始位置
     */
    private int start;
    /**
     * 结束位置
     */
    private int end;

    public SumCalculator(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            SumCalculator leftTask = new SumCalculator(array, start, mid);
            SumCalculator rightTask = new SumCalculator(array, mid, end);

            leftTask.fork(); // 异步执行左半部分的任务
            long rightResult = rightTask.compute(); // 同步执行右半部分的任务
            long leftResult = leftTask.join(); // 获取左半部分任务的结果

            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        // 创建一个ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // 创建一个大数组
        int[] array = IntStream.rangeClosed(1, 100000).toArray();

        // 创建一个任务
        SumCalculator task = new SumCalculator(array, 0, array.length);

        // 提交任务并获取结果
        long result = pool.invoke(task);

        // 输出结果
        System.out.println("Sum: " + result);
    }
}

