package org.whn.tabbitMqListener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.whn.constant.RabbitMqConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/3/26 10:35
 * @Description: rabbitMQ 消费者监听器
 */
@Component
public class RabbitMQListener {
    @RabbitListener(queues = RabbitMqConstant.QUEUE_SIMPLE_QUEUE, ackMode = "MANUAL")
    public void simpleMessageReceive(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("直连消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMqConstant.QUEUE_WORK_QUEUE, ackMode = "MANUAL")
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstant.QUEUE_WORK_QUEUE),
            exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_DEFAULT,
                    type = ExchangeTypes.DIRECT)))
    public void workMessageReceiveOne(Message message, Channel channel) {
        try {
            //手动确认消息
            String messageBody = new String(message.getBody());
            if (messageBody.length() < 10) {
                System.out.println("One -------- 消息接收：" + messageBody);
                //模拟业务消耗
                TimeUnit.SECONDS.sleep(5);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = RabbitMqConstant.QUEUE_WORK_QUEUE, ackMode = "MANUAL")
    public void workMessageReceiveTwo(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("Two ------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_ONE),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,
                            type = ExchangeTypes.FANOUT),
                    key = RabbitMqConstant.ROUTING_DEFAULT),
            ackMode = "MANUAL")
    public void subscriptionMessageReceiveOne(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("One SUBSCRIPTION ------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_TWO, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,
                            type = ExchangeTypes.FANOUT),
                    key = RabbitMqConstant.ROUTING_DEFAULT),
            ackMode = "MANUAL")
    public void subscriptionMessageReceiveTwo(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("Two SUBSCRIPTION ------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_DIRECT_QUEUE_ONE),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,
                            type = ExchangeTypes.DIRECT),
                    key = RabbitMqConstant.ROUTING_DIRECT_ROUTING_ERROR),
            ackMode = "MANUAL")
    public void directMessageReceiveOne(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("One direct " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_DIRECT_QUEUE_TWO),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_DIRECT_CHANGE,
                            type = ExchangeTypes.DIRECT),
                    key = {RabbitMqConstant.ROUTING_DIRECT_ROUTING_INFO,
                            RabbitMqConstant.ROUTING_DIRECT_ROUTING_WARNING,
                            RabbitMqConstant.ROUTING_DIRECT_ROUTING_ERROR}),
            ackMode = "MANUAL")
    public void directMessageReceiveTwo(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("Two direct " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_TOPIC_QUEUE_ONE),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_TOPIC_CHANGE,
                            type = ExchangeTypes.TOPIC),
                    key = RabbitMqConstant.ROUTING_TOPIC_ROUTING_INFO + ".*"),
            ackMode = "MANUAL")
    public void topicMessageReceiveOne(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("One direct " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_TOPIC_QUEUE_TWO),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_TOPIC_CHANGE,
                            type = ExchangeTypes.TOPIC),
                    key = RabbitMqConstant.ROUTING_TOPIC_ROUTING_WARNING + ".*" + ".*"),
            ackMode = "MANUAL")
    public void topicMessageReceiveTwo(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("Two direct " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_LASTING_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_LASTING_CHANGE,
                            type = ExchangeTypes.TOPIC, durable = "true"),
                    key = RabbitMqConstant.ROUTING_DEFAULT),
            ackMode = "MANUAL")
    public void lastingMessageReceive(Message message, Channel channel) {
        try {
            String messageBody = new String(message.getBody());
            //手动确认消息
            if (messageBody.length() < 10) {
                System.out.println("lasting " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println(messageBody + "：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_DESA_LETTER_USER_QUEUE, durable = "true",
                            arguments = {@Argument(name = "x-max-length",value = "3",type = "java.lang.Integer"), //配置对列消息上限
                                    @Argument(name = "x-dead-letter-exchange",value = RabbitMqConstant.EXCHANGE_DEAD_LETTER), //配置死信交换机
                                    @Argument(name = "x-dead-letter-routing-key",value = RabbitMqConstant.ROUTING_DEAD_LETTER)}),//配置死信RoutingKey
                    exchange = @Exchange(value =RabbitMqConstant.EXCHANGE_DEAD_LETTER_USER),
                    key = RabbitMqConstant.ROUTING_DEAD_LETTER_USER))
    public void deadLaterMessageUser(Message message) {
        String messageBody = new String(message.getBody());
        try {
            //添加延迟进入阻塞超过设置长度让消息进入死信队列
            TimeUnit.SECONDS.sleep(5);
            System.out.println("deadLaterMessageUser " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_DESA_LETTER_QUEUE, durable = "true"),
                    exchange = @Exchange(name = RabbitMqConstant.EXCHANGE_DEAD_LETTER),
                    key = RabbitMqConstant.ROUTING_DEAD_LETTER))
    public void deadLaterMessageReceive(Message message) {
        String messageBody = new String(message.getBody());
        System.out.println("死信队列处理消息 " + message.getMessageProperties().getReceivedRoutingKey() + "------------ 消息接收：" + messageBody);
    }
}
