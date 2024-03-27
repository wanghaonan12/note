# 1. RabbitMQ简介

o  官网： http://www.rabbitmq.com/

o  官方教程：http://www.rabbitmq.com/getstarted.html

## 什么是消息队列

消息队列是一种用于在不同应用程序或系统之间传递消息的通信方式。它通常用于解耦发送者和接收者之间的通信，使得它们可以异步地进行通信，而无需直接相互通信。消息队列通常由一个中间件组件来管理，它负责接收、存储和传递消息，并确保消息按照一定的顺序和可靠性进行传递。

消息队列的基本工作原理是，发送者将消息发送到队列中，然后接收者从队列中获取消息进行处理。这种方式可以实现发送者和接收者之间的解耦，即使发送者和接收者不在同一时间或不在同一地点，也可以保证消息的传递。

消息队列通常用于解决分布式系统中的通信问题，如异步通信、负载均衡、解耦服务等。常见的消息队列系统包括 RabbitMQ、Apache Kafka、ActiveMQ 等。

## AMQP和JMS

AMQP（Advanced Message Queuing Protocol）和JMS（Java Message Service）都是用于消息传递的协议或 API，但它们有一些不同之处：

1. **AMQP**：
   - AMQP 是一种开放的标准消息传递协议，它定义了消息传递的格式和交换方式。
   - AMQP 不限制实现的语言或平台，因此可以在多种编程语言和系统中使用。
   - AMQP 提供了更丰富的功能，如消息路由、交换、队列等，因此在复杂的消息传递场景中更为灵活。

2. **JMS**：
   - JMS 是 Java 平台的消息传递 API，提供了在 Java 应用程序中发送、接收和处理消息的标准方式。
   - JMS 是基于点对点（Point-to-Point）和发布-订阅（Publish-Subscribe）模式的。
   - JMS 只适用于 Java 程序，因此在其他语言或系统中无法直接使用。

虽然 AMQP 和 JMS 都是用于消息传递的技术，但它们的设计目标和实现方式略有不同。在选择使用哪种技术时，可以根据具体的需求、应用场景和已有的系统架构来进行选择。

## 常见的MQ

- **ActiveMQ**：基于JMS
- **RabbitMQ**：基于AMQP协议，erlang语言开发，稳定性好
- **RocketMQ**：基于JMS，阿里巴巴产品，目前交由Apache基金会
- **Kafka**：分布式消息系统，高吞吐量，处理日志，Scala和Java编写，Apache

## RabbitMQ的基本概念

> 上面介绍过 RabbitMQ 是 AMQP 协议的一个开源实现，所以其内部实际上也是 AMQP 中的基本概念：

![image-20240325165754016](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325165754016.png)

Ø **Message**
 消息，消息是不具名的，它由消息头和消息体组成。消息体是不透明的，而消息头则由一系列的可选属性组成，这些属性包括routing-key（路由键）、priority（相对于其他消息的优先权）、delivery-mode（指出该消息可能需要持久性存储）等。 

Ø **Publisher**
 消息的生产者，也是一个向交换器发布消息的客户端应用程序。 

Ø **Exchange**
 交换器，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。 

Ø **Binding**
 绑定，用于消息队列和交换器之间的关联。一个绑定就是基于路由键将交换器和消息队列连接起来的路由规则，所以可以将交换器理解成一个由绑定构成的路由表。

Ø **Queue**
 消息队列，用来保存消息直到发送给消费者。它是消息的容器，也是消息的终点。一个消息可投入一个或多个队列。消息一直在队列里面，等待消费者连接到这个队列将其取走。

Ø **Connection**
 网络连接，比如一个TCP连接。 

Ø **Channel**
 信道，多路复用连接中的一条独立的双向数据流通道。信道是建立在真实的TCP连接内地虚拟连接，AMQP 命令都是通过信道发出去的，不管是发布消息、订阅队列还是接收消息，这些动作都是通过信道完成。因为对于操作系统来说建立和销毁 TCP 都是非常昂贵的开销，所以引入了信道的概念，以复用一条 TCP 连接。

Ø **Consumer**
 消息的消费者，表示一个从消息队列中取得消息的客户端应用程序。 

Ø **Virtual Host**
 虚拟主机，表示一批交换器、消息队列和相关对象。虚拟主机是共享相同的身份认证和加密环境的独立服务器域。每个 vhost 本质上就是一个 mini 版的 RabbitMQ 服务器，拥有自己的队列、交换器、绑定和权限机制。vhost 是 AMQP 概念的基础，必须在连接时指定，RabbitMQ 默认的 vhost 是 / 。 

Ø **Broker**
 表示消息队列服务器实体。

# 2. RabbitMQ Docker 安装及配置

## Dokcer 安装

[Installing RabbitMQ | RabbitMQ](https://www.rabbitmq.com/docs/download)

1. 安装RabbitMQ并启动容器

`RABBITMQ_DEFAULT_PASS:`登录用户名

`RABBITMQ_DEFAULT_USER:`登录密码

```bash
# 安装启动rabbitmq容器
docker run -d --name myRabbitMQ -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=123456 -p 15672:15672 -p 5672:5672 rabbitmq:3.13-management
```

2. copy rabbitMQ的配置文件，日志，数据文件到宿主机

```bash
# 日志文件
docker cp 396ca8ac5f240(容器ID):/var/log/rabbitmq /home/rabbitmq/log
# 配置文件
docker cp 396ca8ac5f240(容器ID):/etc/rabbitmq /home/rabbitmq/conf
# 数据文件
docker cp 396ca8ac5f240(容器ID):/var/lib/rabbitmq /home/rabbitmq/data
```

3. 删除之前的容器 重新创建容器并绑定数据卷

```bash
docker run -d --name myRabbitMQ \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=123456 \
-p 15672:15672 \
-p 5672:5672 \
-v /home/rabbitmq/data:/var/lib/rabbitmq \
-v /home/rabbitmq/conf:/etc/rabbitmq \
-v /home/rabbitmq/log:/var/log/rabbitmq  \
rabbitmq:3.13-management
```

4. 解除防火墙`15672`和`5672`端口（与映射端口一致）
5. 进入rabbitMQ页面`localhost:15672`

![image-20240325155735335](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325155735335.png)

## 用户配置

>  通过刚刚配置的用户和密码登录在Admin标签页可以创建其他用户

![image-20240325160330407](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325160330407.png)

1.  **超级管理员(administrator)**

   可登录管理控制台，可查看所有的信息，并且可以对用户，策略(policy)进行操作。

2.  **监控者(monitoring)**

   可登录管理控制台，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)

3.  **策略制定者(policymaker)**

   可登录管理控制台, 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。

4.  **普通管理者(management)**

   仅可登录管理控制台，无法看到节点信息，也无法对策略进行管理。

5. **其他**

   无法登录管理控制台，通常就是普通的生产者和消费者。

# 3. 基础使用

## 前期准备

创建一个服务名为`RabbitMQ`的`Spring boot`项目

**xml配置**

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.14</version>
        <relativePath/>
    </parent>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <nacos.context>2.1.0-RC</nacos.context>
        <!--         <swagger.ui>2.9.2</swagger.ui> -->
        <swagger.ui>3.0.0</swagger.ui>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.18.26</lombok.version>
        <mysql.version>5.1.47</mysql.version>
        <druid.version>1.1.16</druid.version>
        <druid.spring.boot.starter.version>1.1.10</druid.spring.boot.starter.version>
        <mapper.version>4.1.5</mapper.version>
        <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
        <mysql.connector.version>5.1.47</mysql.connector.version>
        <hutool.version>5.2.3</hutool.version>
        <mybatis.plus.boot.starter.version>3.2.0</mybatis.plus.boot.starter.version>
        <guava.version>23.0</guava.version>
        <canal.client.version>1.1.0</canal.client.version>
        <redission.version>3.19.1</redission.version>
    </properties>
    <dependencies>
        <!--基于AMQP协议的消息中间件框架-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!-- SpringBoot通用依赖模块 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 通用基础配置 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <optional>true</optional>
        </dependency>

        <!-- swagger2 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>${swagger.ui}</version>
        </dependency>
        <!-- lombok   -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
        <!-- 通用基础配置junit/devtools/test/log4j/lombok/hutool -->
        <!-- hutool -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>
    </dependencies>
```

> 主要使用的是`spring-boot-starter-amqp`AMQP协议的消息中间件框架
>
> [Spring AMQP 中文文档 ](https://springdoc.cn/spring-amqp/)
>
> ```java
> <dependency>
>     <groupId>org.springframework.boot</groupId>
>     <artifactId>spring-boot-starter-amqp</artifactId>
> </dependency>
> ```

**yml配置**

```yaml
server:
  port: 9999
spring:
  application:
    name: rabbit-mq-learn
  swagger2:
    enabled: true
```

**config**

```java
@Configuration
public class SwaggerConfig {
    @Value("${spring.swagger2.enabled:false}")
    private Boolean enabled;
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled)
                //通过.select()方法，去配置扫描接口
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.whn.controller"))
                // 配置如何通过path过滤
                .paths(PathSelectors.any())
                .build();
    }
    Contact contact = new Contact("whn","https://blog.csdn.net/","1470918223@qq.com");

    //配置Swagger 信息 = ApiInfo
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
//                .contact(contact)
                .title("springboot利用swagger2构建api接口文档 "+"\t"+ DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()))
                .description("springboot+redis整合")
                .version("1.0")
                .build();
    }
}
```

**测试Controller**

```java
@ApiModel(value = "test Controller",description = "测试controller")
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/")
    public String getTest(){
        return "controller test";
    }
}
```

![image-20240325165144170](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325165144170.png)

## 生产消费处理

> RabbitMQ是一个消息代理：它接受和转发消息。 你可以把它想象成一个邮局：当你把邮件放在邮箱里时，你可以确定邮差先生最终会把邮件发送给你的收件人。
>
> 在这个比喻中，RabbitMQ是邮政信箱，邮局和邮递员。
>
> RabbitMQ与邮局的主要区别是它不处理纸张，而是接受，存储和转发数据消息的二进制数据块。

![image-20240325173019636](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325173019636.png)

- P（producer/ publisher）：生产者，一个发送消息的用户应用程序。
- C（consumer）：消费者，消费和接收有类似的意思，消费者是一个主要用来等待接收消息的用户应用程序
- 队列（红色区域）：rabbitmq内部类似于邮箱的一个概念。虽然消息流经rabbitmq和你的应用程序，但是它们只能存储在队列中。
- 队列只受主机的内存和磁盘限制，实质上是一个大的消息缓冲区。许多生产者可以发送消息到一个队列，许多消费者可以尝试从一个队列接收数据。总之：
  - 生产者将消息发送到队列，消费者从队列中获取消息，队列是存储消息的缓冲区。
  - 我们将用Java编写两个程序;发送单个消息的生产者，以及接收消息并将其打印出来的消费者。
  - 我们将详细介绍Java API中的一些细节，这是一个消息传递的“Hello World”。
  - 我们将调用我们的消息发布者（发送者）Send和我们的消息消费者（接收者）Recv。
  - 发布者将连接到RabbitMQ，发送一条消息，然后退出。



接下来我们使用**简单工作模型**演示一下mq的基础使用

1. 创建连接工具类

   ```java
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
           //设置账号信息，用户名、密码
           factory.setUsername(USER);
           factory.setPassword(PASSWORD);
           // 通过工程获取连接
           return factory.newConnection();
       }
   }
   ```

2. 创建生产者

   ```java
   / **
     * 声明队列
     * @param queue队列名称
     * @param durable  如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
     * @param Exclusive 如果我们声明一个排他队列，则为true（仅限此连接）
     * @param autoDelete 如果我们声明一个自动删除队列，则为true（服务器将在不再使用它时将其删除）
     * @param arguments 参数队列的其他属性（构造参数）
     * @return 一个声明确认方法来指示队列已成功声明
     * /
   Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments) throws IOException;
   ```

   ```java
   public class Producter {
       private final static String QUEUE_NAME = "simple_queue";
       public static void main(String[] argv) throws Exception {
           // 获取到连接
           Connection connection = ConnectionUtil.getConnection();
           // 从连接中创建通道，使用通道才能完成消息相关的操作
           Channel channel = connection.createChannel();
           // 声明（创建）队列
           channel.queueDeclare(QUEUE_NAME, false, false, false, null);
           // 消息内容
           String message = "Hello World!";
           // 向指定的队列中发送消息
           channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
           System.out.println(" [x] Sent '" + message + "'");
           //关闭通道和连接
           channel.close();
           connection.close();
       }
   }
   ```

   ![image-20240325173709365](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325173709365.png)

3. 创建消费者

   ```java
   public class Consumer {
       private final static String QUEUE_NAME = "simple_queue";
       public static void main(String[] argv) throws Exception {
           // 获取到连接
           Connection connection = ConnectionUtil.getConnection();
           // 创建通道
           Channel channel = connection.createChannel();
           // 声明队列
           channel.queueDeclare(QUEUE_NAME, false, false, false, null);
           // 定义队列的消费者
           DefaultConsumer consumer = new DefaultConsumer(channel) {
               // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
               @Override
               public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                   // body 即消息体
   
                   String msg = new String(body);
                   System.out.println(" [x] received : " + msg + "!");
               }
           };
           // 监听队列，第二个参数：是否自动进行消息确认。
           channel.basicConsume(QUEUE_NAME, true, consumer);
       }
   }
   ```

   ![image-20240325172810816](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240325172810816.png)

   > 我们发现，消费者已经获取了消息，但是程序没有停止，一直在监听队列中是否有新的消息。一旦有新的消息进入队列，就会立即打印.

# 4. 消息模型

1. **简单工作模型：**

   一个生产者，一个队列 ，一个消费者（一对一）

2. **工作模型：**

   一个生产者 ==> 一个队列 ==> 多个消费者。

   一个消息只能被消费一次。

3. **订阅模式-----Fanout（广播）:**

   一个生产者 ==> 一个交换机 ==> 多个列队 ==> 多个消费者。

   一个消息可以被多个消费者消费生产者发生消息只能发送到交换机。

4. **订阅模式 ---- Direct（路由）**

   一个生产者 ==> 一个交换机 ==> 多个队列 ==> 多个消费者routing Key ，

   一个消息发送给符合 routing Key 的队列

5. **订阅模式 ---- topic（通配符）**

   `Topic`类型的`Exchange`与`Direct`相比，都是可以根据`RoutingKey`把消息路由到不同的队列。只不过`Topic`类型`Exchange`可以让队列在绑定`Routing key` 的时候使用通配符！

6. **RPC，并不是MQ，暂时不学**

![image-20240326104521207](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326104521207.png)

**操作步骤**

![image-20240326105525377](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326105525377.png)

p：生产者		x：交换机		amqp：消息队列		c：消费者

**第一步：**p（生产者）绑定指定的x（交换机）

**第二部：**x（交换机）需要通过创建好的信道（图中的error、info、warning为信道名称）连接队列

**第三步：**amqp（消息队列）绑定对应的c（消费者）

# 5. 简单工作模型

> 接下来我们使用该SpringBoot的注解进行操作MQ，简单模型对应着上面的基础使用，创建参数也有介绍，如有需要使用工厂模式进行操作可以根据这两个的参数对比进行转换。

![image-20240326115428266](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326115428266.png)

**yml配置**

```yml
server:
  port: 9999
spring:
  application:
    name: rabbit-mq-learn
  swagger2:
    enabled: true
  rabbitmq:
    port: 5672
    host: 43.138.25.182
    username: admin
    password: 123456
    virtual-host: rabbitmq
```

**常量**

```java
public class RabbitMqConstant {
    /**
     * 直练队列名称
     */
    public final static String QUEUE_SIMPLE_QUEUE = "simple_queue";

    /**
     * 交换机名称
     */
    public final static String EXCHANGE_SIMPLE_CHANGE = "simple_change";

    /**
     * routing key名称
     */
    public final static String ROUTING_SIMPLE_ROUTING = "simple_routing";

}
```

**监听器**

```java
@Component
public class RabbitMQListener {

    @RabbitListener(queues = RabbitMqConstant.QUEUE_SIMPLE_QUEUE)
    public void messageReceive(String message){
        System.out.println("直连消息接收："+message);
    }
}
```

**消息发送**

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping("/simple/{message}")
    public void send(@PathVariable(value = "message") String message){
        System.out.println("发送消息："+message);
        //rabbitTemplate.convertAndSend(RabbitMqConstant.QUEUE_SIMPLE_QUEUE,message);
        amqpTemplate.convertAndSend(RabbitMqConstant.QUEUE_SIMPLE_QUEUE,message);
    }
}

```

> 上述案例的发送方式有两种，都可使用
>
> 1. `channel.basicPublish()` 方式（基础使用的教程中）
> 2. `rabbitTemplate.send()`方式
> 3. `amqpTemplate.send()`方式
>
>  2 和 3 都是对 1 的进一步封装

## 消息确认

> 在上面的那种接收中还会遇到点问题。
>
> 1. 在消息接收处理时，如何保证消息是被处理的，没有因为设备原因导致没有处理完成？
> 2. 消息发送时，如何确保消息被MQ接收到了？
>
> 
>
> 解决处理：
>
> 1. 在消息消费时针对重要的信息进行手动确认处理
> 2. 重新配置实现`RabbitTemplate.ConfirmCallback`和`RabbitTemplate.ReturnCallback`
>    - ConfirmCallback是一个回调接口，消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，**也就是只确认是否正确到达 Exchange 中。**
>    - ReturnCallback 接口，启动消息失败返回，此接口是在交换器路由不到队列时触发回调，该方法可以不使用，因为交换器和队列是在代码里绑定的，如果消息成功投递到Broker后几乎不存在绑定队列失败，除非你代码写错了。

```java
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
        System.out.println("ack：[{}]" + ack);
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
```

```yml
server:
  port: 9999
spring:
  application:
    name: rabbit-mq-learn
  swagger2:
    enabled: true
  rabbitmq:
    port: 5672
    host: 43.138.25.182
    username: admin
    password: 123456
    virtual-host: rabbitmq
    publisher-returns: true #启用
    publisher-confirm-type: simple #确认方式  SIMPLE CORRELATED NONE
```

1. **SIMPLE**： 这种确认类型使用 `RabbitTemplate#waitForConfirms()` 或者在受限范围内使用 `waitForConfirmsOrDie()` 来等待确认。简单来说，它等待直到消息已经被确认或者确认失败。
2. **CORRELATED**： 这种确认类型需要与 `CorrelationData` 一起使用，以便将确认与发送的消息相关联。通常在需要跟踪每条消息的状态时使用，通过为每条消息指定唯一的 `CorrelationData` 来关联确认信息。
3. **NONE**： 这是默认的确认类型，表示发布者确认被禁用。在这种情况下，

去除用户对应的`Virtual Hosts`权限测试发布消息

![image-20240326171723528](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326171723528.png)

![image-20240326173003495](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326173003495.png)

# 6. 工作模型（消费者的负载均衡配置）



![image-20240326194106346](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326194106346.png)

> 工作模式：主要思想就是避免执行资源密集型任务时，必须等待它执行完成。相反我们稍后完成任务，我们将任务封装为消息并将其发送到队列。 在后台运行的工作进程将获取任务并最终执行作业。当你运行许多消费者时，任务将在他们之间共享，但是**一个消息只能被一个消费者获取**。
>
> 简单来讲就是分配给多个消费者进行处理，处理快的就多处理，处理慢的就少处理，谁也别闲着，资源最大化利用，但是不能重复处理。

**生产者**
可以是多个

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/work/{message}")
    public void workSend(@PathVariable(value = "message") String message){
        System.out.println("work发送消息："+message);
        amqpTemplate.convertAndSend(RabbitMqConstant.QUEUE_WORK_QUEUE,message);
    }
}
```

**消费者1号和消费者2号**

创建消费者1号和消费者2号，1号的消费时常增加2s

```java
@Component
public class RabbitMQListener {

    @RabbitListener(queues = RabbitMqConstant.QUEUE_WORK_QUEUE,ackMode = "MANUAL")
    public void workMessageReceiveOne(Message message, Channel channel) {
        String messageBody = new String(message.getBody());
        try {
            //手动确认消息
            if (messageBody.length()<10){
                System.out.println("One -------- 消息接收："+messageBody);
                //模拟业务消耗
                TimeUnit.SECONDS.sleep(2);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                System.out.println(messageBody+"：消息接收失败！");
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

    @RabbitListener(queues = RabbitMqConstant.QUEUE_WORK_QUEUE,ackMode = "MANUAL")
    public void workMessageReceiveTwo(Message message, Channel channel) {
        String messageBody = new String(message.getBody());
        try {
            //手动确认消息
            if (messageBody.length()<10){
                System.out.println("Two ------------ 消息接收："+messageBody);
                //第一个参数是消息的标记，第二个参数是是否批量处理
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                System.out.println(messageBody+"：消息接收失败！");
                new RuntimeException("消息长度大于10");
                //第一个参数是消息的标记，第二个参数是是否批量处理，第三个参数，是否重新入队，true为重新入队，false为丢弃该消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**问题:**运行结果发现并没有能者多劳，一号的业务时间比较长，但是也消费处理了同样的消息

![image-20240326194036669](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326194036669.png)

**处理:**配置yml

```yml
server:
  port: 9999
spring:
  application:
    name: rabbit-mq-learn
  swagger2:
    enabled: true
  rabbitmq:
    port: 5672
    host: 43.138.25.182
    username: admin
    password: 123456
    virtual-host: rabbitmq
    listener:
      simple:
        prefetch: 1 #配置每个消费者领取的消息数量，处理完了才能领取下一条
```

![image-20240326203407334](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240326203407334.png)

# 7. 订阅模式-----Fanout（广播）

在在工作模式中：每个任务只被传递给一个工作人员。 

在这一部分，我们将做一些完全不同的事情 - 我们将会传递**一个信息给多个消费者**。 这种模式被称为**“发布/订阅”**。

![image-20240327102728990](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240327102728990.png)

**生产者**

> 与之前的工作模型相比区别就是绑定了一个交换机，其他的参数(Queue、RoutingKey)都为默认值("")

`RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE`：交换机名称(随意)

`RabbitMqConstant.ROUTING_DEFAULT`：默认名称为("")

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @PostMapping("/subscription/{message}")
    public void subscriptionSend(@PathVariable(value = "message") String message){
        System.out.println("subscription发送消息："+message);
        amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,RabbitMqConstant.ROUTING_DEFAULT,message);
    }
}
```

**生产者**

```java
@RabbitListener(queues = RabbitMqConstant.QUEUE_WORK_QUEUE,ackMode = "MANUAL")
// 等于 下面的样式 绑定默认的交换机 ，交换机的类型为默认的直连类型
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = RabbitMqConstant.QUEUE_WORK_QUEUE),
    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_DEFAULT,
                         type = ExchangeTypes.DIRECT)))
```

> 与之前的工作模式相比绑定了一个交换机

```java
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_ONE),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,
                            type = ExchangeTypes.FANOUT),
                    key = RabbitMqConstant.ROUTING_DEFAULT),
            ackMode = "MANUAL")
```

- `bindings`: 定义了与消息队列绑定相关的设置。

  - `value`: 表示队列的相关设置。
    - `value = @Queue(value = RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_ONE)`: 声明一个队列，其中 `RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_ONE` 是队列的名称。
  - `exchange`: 表示交换机的相关设置。
    - `value = @Exchange(value = RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,type = ExchangeTypes.FANOUT)`: 声明一个交换机，
      - `value` :表示`RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE` 是交换机的名称。
      - `type `: 交换机类型。交换机常用类型有`direct`(直连，默认类型)、topic(主题)、fanout(订阅广播模式)
  - `key`: 表示绑定键，用于指定将消息路由到指定队列的规则。
    - `RabbitMqConstant.ROUTING_DEFAULT`: 绑定键，用于将消息路由到队列的规则。

  > 两个消费者的交换机`Exchange`是一样的，唯一不同的是队列`Queue`。
  >
  > 消费者One的队列名称：`RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_ONE`
  >
  > 消费者Two的队列名称：`RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_TWO`

```java
@Component
public class RabbitMQListener {
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
                    value = @Queue(value = RabbitMqConstant.QUEUE_SUBSCRIPTION_ROUTING_TWO),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_SUBSCRIPTION_CHANGE,
                            type = ExchangeTypes.FANOUT),
                    key = RabbitMqConstant.ROUTING_DEFAULT ),
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
}
```

> 结果：发送的一个消息被所有订阅的队列消费。
>
> ![image-20240327113553387](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240327113553387.png)
>
> **添加新的消费者进入队列：**
>
> **方法1：**手动添加队列信息，并绑定对应的交换机。**(推荐，之前队列的消息不会丢失)**
>
> **方法2：**删除之前的交换机和队列，可能会有消息丢失。

# 8. 订阅模式 ---- Direct（路由）

**路由模式：**就是有**选择性的接收消息**

如下图：

消费者1和消费者2都能接收到 绑定了 error 的 queue 发过来的消息

消费者2能收到 info 、 error 、 warning 的队列而的消息

![image-20240327131240346](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240327131240346.png)

**生产者**

> 生产者比之前的广播模式多了routingKey的配置，之前使用的时默认路由
>
> 场景模拟：
>
> 1. 输入的不是数字向 error 的 routingKey 发送消息（消费者1，2各消费一次，共两次）
> 2. 是数字向 info 、error 、warning  的 routingKey 发送消息（消费者1消费一次，消费者2消费三次，共四次）

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static String REGEX = "\\d+";
    
    @PostMapping("/direct/{message}")
    @ApiOperation(value = "direct发送消息")
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
}
```

**消费者**

> 消费者比广播模式多了routhingKey的绑定，之前绑定的时默认("")
>
> 一号消费者绑定：ROUTING_DIRECT_ROUTING_ERROR
>
> 二号消费者绑定：ROUTING_DIRECT_ROUTING_INFO、ROUTING_DIRECT_ROUTING_WARNING、ROUTING_DIRECT_ROUTING_ERROR
>
> 所以发送error的两个都能接收消费、而warning和info只有2号消费者接收

```java
@Component
public class RabbitMQListener {
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
}
```

> 在这就可以看到 error 发送的 被两个消费者都接收到了，而发送的warning info 只有在2号消费者接收

![image-20240327140426049](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240327140426049.png)

# 9. 订阅模式 ---- topic（通配符）

> Topic类型的Exchange与Direct相比，都是可以根据RoutingKey把消息路由到不同的队列。只不过Topic类型Exchange可以让队列在绑定Routing key 的时候使用通配符！
>
> -  Routingkey 一般都是有一个或多个单词组成，多个单词之间以”.”分割，例如： item.insert  通配符规则：
>   -  `#`：匹配**0或多个词**（含零个）
>   - `*`：匹配不多不少恰好1个词（不含零个）
> - 举例：
>   - `audit.#`：能够匹配`audit.irs.corporate` 或者 `audit.irs``
>   - ``audit.*`：只能匹配`audit.irs`

**生产者**

> 主题模式的生产者与之前相比routingKey的设置方式是用`.`进行分割

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static String REGEX = "\\d+";
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
}
```

**消费者**

> 消费者方面与之前的相比在routingkey绑定的时候也用了通配符。
>
> 注意：这里需要进行全路径通配如：
>
> `warning.*` 无法匹配 `warning.info.error`，需要改成 ` warning.* .*`才可以匹配。

```java
@Component
public class RabbitMQListener {
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.QUEUE_TOPIC_QUEUE_ONE),
                    exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_TOPIC_CHANGE,
                            type = ExchangeTypes.TOPIC),
                    key = RabbitMqConstant.ROUTING_TOPIC_ROUTING_INFO+".*"),
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
                    key = RabbitMqConstant.ROUTING_TOPIC_ROUTING_WARNING+".*"+".*"),
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
}
```

# 10. 持久化及@Queue、**@RabbitListener** 、@Exchange属性介绍

```java
@RabbitListener(
    bindings = @QueueBinding(
        value = @Queue(value = RabbitMqConstant.QUEUE_LASTING_QUEUE,durable = "true"),
        exchange = @Exchange(value = RabbitMqConstant.EXCHANGE_LASTING_CHANGE,
                             type = ExchangeTypes.TOPIC,durable = "true"),
        key = RabbitMqConstant.ROUTING_DEFAULT),
    ackMode = "MANUAL")
```

> 队列持久化配置在@Queue中配置durable = "true"就可以了，交换机持久化配置在@Exchange中配置durable = "true"就可以了（交换机默认就是True）

**@Queue**注解为我们提供了队列相关的一些属性，具体如下：

- name: 队列的名称；
- durable: 是否持久化；
- exclusive: 是否独享、排外的；
- autoDelete: 是否自动删除；
- arguments：队列的其他属性参数，有如下可选项，可参看图2的arguments：
  - x-message-ttl：消息的过期时间，单位：毫秒；
  - x-expires：队列过期时间，队列在多长时间未被访问将被删除，单位：毫秒；
  - x-max-length：队列最大长度，超过该最大值，则将从队列头部开始删除消息；
  - x-max-length-bytes：队列消息内容占用最大空间，受限于内存大小，超过该阈值则从队列头部开始删除消息；
  - x-overflow：设置队列溢出行为。这决定了当达到队列的最大长度时消息会发生什么。有效值是drop-head、reject-publish或reject-publish-dlx。仲裁队列类型仅支持drop-head；
  - x-dead-letter-exchange：死信交换器名称，过期或被删除（因队列长度超长或因空间超出阈值）的消息可指定发送到该交换器中；
  - x-dead-letter-routing-key：死信消息路由键，在消息发送到死信交换器时会使用该路由键，如果不设置，则使用消息的原来的路由键值
  - x-single-active-consumer：表示队列是否是单一活动消费者，true时，注册的消费组内只有一个消费者消费消息，其他被忽略，false时消息循环分发给所有消费者(默认false)
  - x-max-priority：队列要支持的最大优先级数;如果未设置，队列将不支持消息优先级；
  - x-queue-mode（Lazy mode）：将队列设置为延迟模式，在磁盘上保留尽可能多的消息，以减少RAM的使用;如果未设置，队列将保留内存缓存以尽可能快地传递消息；
  - x-queue-master-locator：在集群模式下设置镜像队列的主节点信息。

**@RabbitListener** 提供消费者配置如下：

- ackMode：覆盖容器工厂 AcknowledgeMode属性。
- admin：参考AmqpAdmin.
- autoStartup：设置为 true 或 false，以覆盖容器工厂中的默认设置。
- QueueBinding[]    bindings：QueueBinding提供监听器队列名称以及交换和可选绑定信息的数组。
- concurrency：消费并发数。
- containerFactory：RabbitListenerContainerFactory的bean名称 ，没有则使用默认工厂。
- converterWinsContentType：设置为“false”以使用“replyContentType”属性的值覆盖由消息转换器设置的任何内容类型标头。
- errorHandler：消息异常时调用的方法名。
- exclusive：当为true时，容器中的单个消费者将独占使用 queues()，从而阻止其他消费者从队列接收消息。
- executor：线程池bean的名称
- group：如果提供，则此侦听器的侦听器容器将添加到以该值作为其名称的类型为 的 bean 中Collection<MessageListenerContainer>。
- id：为此端点管理的容器的唯一标识符。
- messageConverter：消息转换器。
- priority：此端点的优先级。
- String[]    queues：监听的队列名称
- Queue[]    queuesToDeclare：监听的队列Queue注解对象,与bindings()、queues()互斥。
- replyContentType：用于设置回复消息的内容类型。
- replyPostProcessor：在ReplyPostProcessor发送之前处理响应的 bean 名称 。
- returnExceptions：设置为“true”以导致使用正常replyTo/@SendTo语义将侦听器抛出的异常发送给发送者。

**@Exchange** 提供交换机配置配置如下：

- `value()`: 这是一个用来指定交换机（Exchange）名称的元素。可以通过 `@Exchange("exchangeName")` 这样的方式来指定名称。
- `name()`: 这也是用来指定交换机名称的元素，与 `value()` 元素同义。这个元素在版本 2.0 之后引入，可以通过 `@Exchange(name = "exchangeName")` 这样的方式来指定名称。
- `type()`: 用来指定交换机的类型，包括自定义类型。默认为 `ExchangeTypes.DIRECT`，即直接交换机。如果使用自定义的交换机类型，需要在消息代理（broker）上安装相应的插件。
- `durable()`: 如果设置为 `false`，表示交换机是非持久化的。
- `autoDelete()`: 如果设置为 `true`，表示当没有连接到这个交换机的队列或者交换机时，会自动删除该交换机。
- `internal()`: 如果设置为 `true`，表示这个交换机是一个内部交换机。
- `ignoreDeclarationExceptions()`: 如果设置为 `true`，表示在声明交换机时忽略声明时可能出现的异常。
- `delayed()`: 如果设置为 `true`，表示这个交换机是一个延迟消息交换机，需要在消息代理上安装延迟消息交换机插件。
- `arguments()`: 用来指定在声明交换机时应用的参数。
- `declare()`: 如果设置为 `true`，表示如果存在管理者（admin），则管理者会声明这个组件。
- `admins()`: 返回应该声明这个组件的管理者 bean 的列表。

# 11. 死信队列

> 在RabbitMQ中，死信队列（Dead Letter Queue）是一种消息处理机制，用于处理那些无法被正常消费的消息。当消息在队列中过期、被拒绝或达到最大队列长度时，这些消息将被自动转移到死信队列中。通过合理配置和使用死信队列，您可以有效地处理和监控消息传递过程中的异常情况，提高系统的可靠性和稳定性。

**生产者**

死信制造生产者

```java
@ApiModel(value = "AmqpSendController",description = "整合amqp进行消息推送")
@RestController
@RequestMapping("/amqpSend")
public class AmqpSendController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/deadLatterUser/{message}")
    @ApiOperation(value = "lasting 发送消息")
    @ApiImplicitParam(name = "message",value = "消息内容",required = true,dataType = "String")
    public void deadLatterUser(@PathVariable(value = "message") String message){
            System.out.println("lasting  发送消息："+message);
            amqpTemplate.convertAndSend(RabbitMqConstant.EXCHANGE_DEAD_LETTER_USER,RabbitMqConstant.ROUTING_DEAD_LETTER_USER,message);
    }
}

```

**消费者**

> 消费者有两个:
>
> `deadLaterMessageUser:`死信生产消费者，设置队列上限为3，添加延时时间模拟业务处理，让其将多余的消息丢到死信队列
>
> ``deadLaterMessageReceive:`处理其他队列发过来的死信消息

```java
@Component
public class RabbitMQListener {
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

```

> 发送的消息7条但是消费者的队列上限是3条还有一条正在被处理，所以有三条消息会被丢弃到设置的死心队列，如下

![image-20240327163112254](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240327163112254.png)