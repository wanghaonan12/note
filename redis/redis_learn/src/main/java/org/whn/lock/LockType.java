package org.whn.lock;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 13:18
 * @Description: 锁类型枚举
 */
public enum LockType {
    /**
     * redis
     */
    REDIS,
    /**
     * zookeeper
     */
    ZOOKEEPER,
    /**
     * mysql
     */
    MYSQL
}
