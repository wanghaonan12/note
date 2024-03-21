package com.whn.blockingQueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 订单类
class Order {
    private String orderId;
    private String userId;

    public Order(String orderId, String userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }
}

// 订单生成者
class OrderProducer implements Runnable {
    private ArrayBlockingQueue<Order> orderQueue;

    public OrderProducer(ArrayBlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order("Order" + i, "User" + i);
            try {
                // 将订单放入订单队列中
                orderQueue.put(order);
                int sleepTime = (new Random().nextInt(5) + 1);
                System.out.println("创建订单消耗：" + sleepTime + "秒++++++++++++++");
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 订单处理任务
class OrderTask implements Runnable {
    private Order order;

    public OrderTask(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        System.out.println("Processing order " + order.getOrderId() + " for user " + order.getUserId());
        // 在这里添加订单处理逻辑
        try {
            // 模拟订单处理时间
            int sleepTime = (new Random().nextInt(5) + 1);
            System.out.println("处理订单消耗：" + sleepTime + "秒-----------------------");
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Order " + order.getOrderId() + " processed successfully.");
    }
}

public class ShoppingCart {
    public static void main(String[] args) {
        // 创建一个有界阻塞队列，用于存放订单任务
        ArrayBlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(10);

        // 创建一个固定大小的线程池来处理订单
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 创建订单生产者线程
        OrderProducer orderProducer = new OrderProducer(orderQueue);
        Thread producerThread = new Thread(orderProducer);
        producerThread.start();
        // 处理订单任务
        //while (orderQueue.size()>0) {
        // 恒为真 因为订单创建也做了延迟模拟 如果没有检索到集合中存在值的时候便会跳过关闭线程池
        while (true) {
            Order order = null;
            try {
                // 从订单队列中取出订单任务并提交给线程池处理
                order = orderQueue.take();
                executor.submit(new OrderTask(order));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 关闭线程池
        //executor.shutdown();
    }
}
