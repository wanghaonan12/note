# RabbitMq

RabbitMQ是一种可靠的、可扩展的、跨平台的消息代理软件，用于在分布式应用程序之间传递消息。它实现了高级消息队列协议（AMQP）来进行通信。

## RabbitMQ基本概念

在开始学习RabbitMQ之前，需要了解一些基本概念：

- 生产者：生产者是向RabbitMQ发送消息的应用程序。
- 消费者：消费者是从RabbitMQ接收消息的应用程序。
- 队列：队列是存储消息的缓冲区。
- 消息：消息是从生产者发送到RabbitMQ的数据。
- 交换器：交换器是接收生产者发送的消息并将其路由到正确队列的组件。

## RabbitMQ工作流程

RabbitMQ的工作流程如下：

1. 生产者将消息发送到交换器。
2. 交换器将消息路由到一个或多个队列。
3. 消费者从队列中接收消息并处理它们。

## RabbitMQ Java项目实现

下面是一个使用Java编写的RabbitMQ示例项目，它演示了如何发送和接收消息：

### Maven依赖

首先，在Maven中添加RabbitMQ的依赖：

```xml
<dependencies>
    <dependency>
        <groupId>com.rabbitmq</groupId>
        <artifactId>amqp-client</artifactId>
        <version>5.4.3</version>
    </dependency>
</dependencies>
```

### 配置

```yaml
spring:
  rabbitmq:
    host: localhost              # RabbitMQ 服务器的主机名或 IP 地址
    port: 5672                   # RabbitMQ 服务器的端口号
    username: guest              # RabbitMQ 服务器的用户名
    password: guest              # RabbitMQ 服务器的密码
    virtual-host: /              # RabbitMQ 服务器的虚拟主机名称
    listener:                    # RabbitMQ 消费者监听器配置
      simple:
        acknowledge-mode: auto   # 消息确认模式，可选值有 none、manual 和 auto，默认为 auto。
        concurrency: 1           # 消费者并发处理的最大数量，默认为 1。
        max-concurrency: 1       # 消费者并发处理的最大数量，当使用 DirectMessageListenerContainer 时，这个属性表示消费者线程池的最大数量，默认为 1。
        prefetch: 1              # 消费者每次预取的消息数量，默认为 1。
        default-requeue-rejected: false  # 当消息被拒绝时，是否重新入队列，默认为 false。
    template:                    # RabbitMQ 消息模板配置
      retry:
        enabled: true            # 是否启用消息重试，默认为 true。
        initial-interval: 1000   # 消息重试的初始间隔时间（毫秒），默认为 1000 毫秒。
        max-attempts: 3          # 消息重试的最大次数，默认为 3 次。
        multiplier: 2            # 消息重试间隔时间的倍数，默认为 2。
        max-interval: 10000      # 消息重试的最大间隔时间（毫秒），默认为 10000 毫秒。
```

### 配置说明

好的，以下是RabbitMQ所有可选值的作用和意思的解释，做成表格：

| 配置项                             | 可选值及说明                                                 |
| ---------------------------------- | ------------------------------------------------------------ |
| `listener.simple.acknowledge-mode` | 消息确认模式，可选值有 `none`、`manual` 和 `auto`，默认为 `auto`。 |
| `none`                             | 不进行消息确认。                                             |
| `manual`                           | 手动确认消息，需要在消费者代码中调用 `channel.basicAck()` 或 `channel.basicNack()` 方法确认消息。 |
| `auto`                             | 自动确认消息，当消费者成功处理完消息时，消息会自动被确认。   |

| 配置项                        | 可选值及说明                         |
| ----------------------------- | ------------------------------------ |
| `listener.simple.concurrency` | 消费者并发处理的最大数量，默认为 1。 |
| 任何正整数                    | 消费者并发处理的最大数量。           |

| 配置项                            | 可选值及说明                                                 |
| --------------------------------- | ------------------------------------------------------------ |
| `listener.simple.max-concurrency` | 消费者并发处理的最大数量，当使用 DirectMessageListenerContainer 时，这个属性表示消费者线程池的最大数量，默认为 1。 |
| 任何正整数                        | 消费者并发处理的最大数量。                                   |

| 配置项                     | 可选值及说明                         |
| -------------------------- | ------------------------------------ |
| `listener.simple.prefetch` | 消费者每次预取的消息数量，默认为 1。 |
| 任何正整数                 | 消费者每次预取的消息数量。           |

| 配置项                                     | 可选值及说明                                                 |
| ------------------------------------------ | ------------------------------------------------------------ |
| `listener.simple.default-requeue-rejected` | 当消息被拒绝时，是否重新入队列，默认为 false。               |
| `true`                                     | 消息被拒绝时重新入队列，下次可能被再次消费。                 |
| `false`                                    | 消息被拒绝时不重新入队列，会被放到死信队列（如果配置了）或者被丢弃。 |

| 配置项                   | 可选值及说明                                                 |
| ------------------------ | ------------------------------------------------------------ |
| `template.retry.enabled` | 是否启用消息重试，默认为 true。                              |
| `true`                   | 启用消息重试。                                               |
| `false`                  | 禁用消息重试，如果消费者无法处理消息，消息会被直接标记为已处理。 |

| 配置项                            | 可选值及说明                                       |
| --------------------------------- | -------------------------------------------------- |
| `template.retry.initial-interval` | 消息重试的初始间隔时间（毫秒），默认为 1000 毫秒。 |
| 任何正整数                        | 消息重试的初始间隔时间。                           |

| 配置项                            | 可选值及说明      |
| --------------------------------- | ---------------------------- |
| template.retry.max-attempts | 消息最大重试次数，默认为 3 次。 |
| 任何正整数 | 消息最大重试次数。 |

### 生产者代码

以下是生产者代码，它演示了如何将消息发送到RabbitMQ：

```java
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建连接工厂:创建一个连接工厂实例，用于连接到RabbitMQ代理。
        ConnectionFactory factory = new ConnectionFactory();
		//设置主机：设置RabbitMQ代理的主机名为“localhost”。
        factory.setHost("localhost");

        // 创建连接:创建一个到RabbitMQ代理的TCP连接。
        Connection connection = factory.newConnection();

        // 创建通道:创建一个到RabbitMQ代理的通道。
        Channel channel = connection.createChannel();

        // 声明队列:声明一个名为“hello”的队列。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息:声明一个名为“hello”的队列。
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("Sent message: " + message);

        // 关闭连接:关闭通道和连接。
        channel.close();
        connection.close();
    }
}
```

## 生命队列参数解释

```java
channel.queueDeclare(QUEUE_NAME, durable, exclusive, autoDelete, arguments);
```

1. `QUEUE_NAME`：队列名称，用于标识队列。在发送消息或消费消息时需要使用这个队列名称。
2. `durable`：是否持久化。如果设置为 `true`，则消息队列将在RabbitMQ服务器重启后仍然存在，否则会被自动删除。如果想要将消息队列持久化，需要设置为 `true`。
3. `exclusive`：是否是独占队列。如果设置为 `true`，则只能有一个消费者使用这个队列，这个队列将自动删除。如果想要共享队列，需要设置为 `false`。
4. `autoDelete`：是否自动删除。如果设置为 `true`，则队列将在最后一个消费者停止使用后自动删除。如果想要在消费者停止使用后仍然存在，需要设置为 `false`。
5. `arguments`：其他参数。可以设置一些额外的参数，如队列最大长度、消息过期时间等。在这个例子中，我们将 `arguments` 设置为 `null`。

需要注意的是，在使用 `channel.queueDeclare()` 方法时，如果已经存在一个名为 `QUEUE_NAME` 的队列，并且该队列的属性与指定的属性不匹配，将会抛出一个异常。因此，在实际应用中，需要确保队列的属性与指定的属性匹配。

### 消费者代码

以下是消费者代码，它演示了如何从RabbitMQ接收消息：

```java
import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // 创建连接工厂:声明一个名为“hello”的队列。
        ConnectionFactory factory = new ConnectionFactory();
		//设置主机：设置RabbitMQ代理的主机名为“localhost”。
        factory.setHost("localhost");

        // 创建连接:声明一个名为“hello”的队列。
        Connection connection = factory.newConnection();

        // 创建通道:声明一个名为“hello”的队列。
        Channel channel = connection.createChannel();

        // 声明队列:声明一个名为“hello”的队列。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 创建消费者:创建一个消费者实例，并在收到消息时打印消息。
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received message: " + message);
            }
        };

        // 启动消费者:启动一个消费者，并在队列中接收消息。
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
```

## 总结

RabbitMQ是一个强大的消息代理软件，用于在分布式应用程序之间传递消息。在这篇文章中，我们介绍了RabbitMQ的基本概念、工作流程和Java项目实现，涉及到了生产者和消费者的代码实现。希望这篇文章能够帮助你了解RabbitMQ的基本原理和使用方法。