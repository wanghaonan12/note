package org.whn.cache;

import lombok.Data;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 14:11
 * @Description:
 */
@Data
public class RedisPoolProperties {

    private int maxIdle;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int connTimeout;

    private int soTimeout;

    /**
     * 池大小
     */
    private  int size;

}
