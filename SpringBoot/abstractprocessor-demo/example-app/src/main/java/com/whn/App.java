package com.whn;

/**
 * @author whn
 * @time 2025/7/10
 * @description 启动入口程序
 */
public class App {
    public static void main(String[] args) {
        User user = new User();
        user.name = "Alice";
        user.age = 30;
        // toString 方法是处理器生成的类中实现的
        System.out.println(user);
    }
}