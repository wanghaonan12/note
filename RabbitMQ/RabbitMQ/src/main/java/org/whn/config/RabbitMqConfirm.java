package org.whn.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author: WangHn
 * @Date: 2024/3/26 14:00
 * @Description: RabbitMQ 发送确认
 */
@Configuration
@Log4j2
public class RabbitMqConfirm implements RabbitTemplate.ReturnsCallback,RabbitTemplate.ConfirmCallback{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("ack：" + ack);
        if (ack) {
            log.info("消息发送成功{}",correlationData);
            System.out.println("消息到达rabbitmq服务器");
        } else {
            // 通知处理或是放入死信队列重发
            log.warn(cause);
            System.out.println("消息可能未到达rabbitmq服务器");
        }
    }
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息主体 message : " + returned.getMessage());
        log.info("消息主体 replyCode : " + returned.getReplyCode());
        log.info("描述 replyText：" + returned.getReplyText());
        log.info("消息使用的交换器 exchange : " + returned.getExchange());
        log.info("消息使用的路由键 routing : " + returned.getRoutingKey());
    }

}
