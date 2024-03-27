package org.whn.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: WangHn
 * @Date: 2024/3/25 17:10
 * @Description: rabbitMq 连接工具类
 */
public class ConnectionUtil {
    /**
     * rabbitMq 链接地址
     */
    private static final String HOST = "43.138.25.182";
    /**
     * rabbitMq 连接端口
     */
    private static final int PORT = 5672;
    /**
     * 连接用户名
     */
    private static final String USER = "admin";
    /**
     * 虚拟主机
     */
    private static final String VIRTUAL_HOST = "rabbitmq";
    /**
     * 连接用户密码
     */

    private static final String PASSWORD = "123456";
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost(HOST);
        //端口
        factory.setPort(PORT);
        factory.setVirtualHost(VIRTUAL_HOST);
        //设置账号信息，用户名、密码
        factory.setUsername(USER);
        factory.setPassword(PASSWORD);
        // 通过工程获取连接
        return factory.newConnection();
    }
}
