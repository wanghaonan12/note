package com.whn.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author: WangHn
 * @Date: 2024/2/19 14:09
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
    private int database;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    private String password;

    private String mode;

    /**
     * 池配置
     */

    private RedisPoolProperties pool;

    /**
     * 单机信息配置
     */
    private RedisSingleProperties single;


}
