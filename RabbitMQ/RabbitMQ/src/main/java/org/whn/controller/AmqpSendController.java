package org.whn.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.constant.RabbitMqConstant;

/**
 * @Author: WangHn
 * @Date: 2024/3/26 10:21
 * @Description: 整合amqp进行队列发送
 */
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static String REGEX = "\\d+";
    @PostMapping("/simple/{message}")
    @ApiOperation(value = "simplesSend 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void simplesSend(@PathVariable(value = "message") String message){
        System.out.println("simple发送消息："+message);
        //rabbitTemplate.convertAndSend(RabbitMqConstant.QUEUE_SIMPLE_QUEUE,message);
        amqpTemplate.convertAndSend(RabbitMqConstant.QUEUE_SIMPLE_QUEUE,message);
    }

    @PostMapping("/work/{message}")
    @ApiOperation(value = "workSend 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void workSend(@PathVariable(value = "message") String message){
        System.out.println("work发送消息："+message);
        amqpTemplate.convertAndSend(RabbitMqConstant.QUEUE_WORK_QUEUE,message);
    }

    @PostMapping("/subscription/{message}")
    @ApiOperation(value = "subscriptionSend 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void subscriptionSend(@PathVariable(value = "message") String message){
        System.out.println("subscription发送消息："+message);
        amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,RabbitMqConstant.ROUTING_DEFAULT,message);
    }
    @PostMapping("/direct/{message}")
    @ApiOperation(value = "direct 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void direct(@PathVariable(value = "message") String message){
        if (!message.matches(REGEX)) {
            System.out.println("direct ROUTING_DIRECT_ROUTING_ERROR 发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,RabbitMqConstant.ROUTING_DIRECT_ROUTING_ERROR,message);
        }else {
            System.out.println("direct ROUTING_DIRECT_ROUTING_INFO 、ROUTING_DIRECT_ROUTING_WARNING 、ROUTING_DIRECT_ROUTING_ERROR 发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,RabbitMqConstant.ROUTING_DIRECT_ROUTING_ERROR,message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,RabbitMqConstant.ROUTING_DIRECT_ROUTING_WARNING,message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,RabbitMqConstant.ROUTING_DIRECT_ROUTING_INFO,message);
        }
    }

    @PostMapping("/topic/{message}")
    @ApiOperation(value = "topic 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void topic(@PathVariable(value = "message") String message){
        if (!message.matches(REGEX)) {
            String routingKey =   RabbitMqConstant.ROUTING_TOPIC_ROUTING_INFO+ "." +RabbitMqConstant.ROUTING_TOPIC_ROUTING_WARNING;
            System.out.println("direct "+routingKey+" 发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_TOPIC_CHANGE,routingKey,message);
        }else {
            String routingKey =  RabbitMqConstant.ROUTING_TOPIC_ROUTING_WARNING + "." +RabbitMqConstant.ROUTING_TOPIC_ROUTING_ERROR + "." + RabbitMqConstant.ROUTING_TOPIC_ROUTING_INFO;
            System.out.println("direct "+routingKey+" 发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_TOPIC_CHANGE,routingKey,message);
        }
    }

    @PostMapping("/lasting/{message}")
    @ApiOperation(value = "lasting 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void lasting(@PathVariable(value = "message") String message){
            System.out.println("lasting  发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_LASTING_CHANGE,RabbitMqConstant.ROUTING_DEFAULT,message);
    }

    @PostMapping("/deadLatterUser/{message}")
    @ApiOperation(value = "lasting 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void deadLatterUser(@PathVariable(value = "message") String message){
            System.out.println("lasting  发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DEAD_LETTER_USER,RabbitMqConstant.ROUTING_DEAD_LETTER_USER,message);
    }
}
