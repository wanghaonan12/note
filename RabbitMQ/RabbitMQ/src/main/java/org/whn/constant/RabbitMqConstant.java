package org.whn.constant;

/**
 * @Author: WangHn
 * @Date: 2024/3/26 11:08
 * @Description: rabbitMQ 常量参数
 */
public class RabbitMqConstant {

    /**
     * 默认交换机名称
     */
    public final static String EXCHANGE_DEFAULT = "exchange_default";
    /**
     * 默认交换机名称
     */
    public final static String EXCHANGE_DEAD_LETTER = "exchange_dead_letter";
    /**
     * 默认交换机名称
     */
    public final static String EXCHANGE_DEAD_LETTER_USER = "exchange_dead_letter_user";

    /**
     * 默认订阅 routing key 名称
     */
    public final static String ROUTING_DEFAULT = "routing_default";
    /**
     * 默认订阅 routing key 名称
     */
    public final static String ROUTING_DEAD_LETTER = "routing_dead_letter";
    /**
     * 默认订阅 routing key 名称
     */
    public final static String ROUTING_DEAD_LETTER_USER = "routing_dead_letter_user";

    /**
     * 发布订阅交换机名称
     */
    public final static String EXCHANGE_SUBSCRIPTION_CHANGE = "subscription_change";
    /**
     * 发布订阅交换机名称
     */
    public final static String EXCHANGE_DIRECT_CHANGE = "direct_change";
    /**
     * 主题 交换机名称
     */
    public final static String EXCHANGE_TOPIC_CHANGE = "topic_change";
    /**
     * 持久化 交换机名称
     */
    public final static String EXCHANGE_LASTING_CHANGE = "lasting_change";

    /**
     * 发布订阅 队列 名称
     */
    public final static String QUEUE_SUBSCRIPTION_ROUTING_ONE = "subscription_queue_one";
    /**
     * 发布订阅 队列 名称
     */
    public final static String QUEUE_SUBSCRIPTION_ROUTING_TWO = "subscription_queue_two";

    /**
     * 发布订阅 队列 名称
     */
    public final static String ROUTING_DIRECT_ROUTING_INFO = "direct_routing_info";
    /**
     * 发布订阅 队列 名称
     */
    public final static String ROUTING_DIRECT_ROUTING_ERROR = "direct_routing_error";
    /**
     * 发布订阅 队列 名称
     */
    public final static String ROUTING_DIRECT_ROUTING_WARNING = "direct_routing_warning";

    /**
     * 主题订阅 队列 名称
     */
    public final static String ROUTING_TOPIC_ROUTING_INFO = "topic_routing_info";
    /**
     * 主题订阅 队列 名称
     */
    public final static String ROUTING_TOPIC_ROUTING_ERROR = "topic_routing_error";
    /**
     * 主题订阅 队列 名称
     */
    public final static String ROUTING_TOPIC_ROUTING_WARNING = "topic_routing_warning";

    /**
     * 订阅模式 队列 名称
     */
    public final static String QUEUE_DIRECT_QUEUE_ONE = "direct_queue_one";

    /**
     * 订阅模式 队列 名称
     */
    public final static String QUEUE_DIRECT_QUEUE_TWO = "direct_queue_two";
    /**
     * 订阅模式 队列 名称
     */
    public final static String QUEUE_TOPIC_QUEUE_ONE = "topic_queue_one";

    /**
     * 订阅模式 队列 名称
     */
    public final static String QUEUE_TOPIC_QUEUE_TWO = "topic_queue_two";
    /**
     * 直练队列名称
     */
    public final static String QUEUE_SIMPLE_QUEUE = "simple_queue";
    /**
     * 工作模式队列名称
     */
    public final static String QUEUE_WORK_QUEUE = "work_queue";
    /**
     * 持久化队列名称
     */
    public final static String QUEUE_LASTING_QUEUE = "lasting_queue";

    /**
     * 死心化队列名称
     */
    public final static String QUEUE_DESA_LETTER_QUEUE = "dead_letter_queue";
    /**
     * 死心化队列名称
     */
    public final static String QUEUE_DESA_LETTER_USER_QUEUE = "dead_letter_user_queue";


}