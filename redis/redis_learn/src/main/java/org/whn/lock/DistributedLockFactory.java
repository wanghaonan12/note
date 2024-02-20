package org.whn.lock;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 13:16
 * @Description: redis工厂
 */
@Component
public class DistributedLockFactory {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private String lockName;
    private String uuid;

    public DistributedLockFactory() {
        this.uuid = IdUtil.simpleUUID();
    }

    public Lock getDistributedLock(LockType lockType) {
        Assert.notNull(lockType, "锁类型不能为空");
        if (LockType.REDIS.equals(lockType)) {
            this.lockName = "RedisLock";
            return new RedisDistributedLock(stringRedisTemplate, lockName, uuid);
        } else if (LockType.ZOOKEEPER.equals(lockType)) {
            this.lockName = "ZookeeperLockNode";
            //TODO zookeeper版本的分布式锁
            return null;
        } else if (LockType.MYSQL.equals(lockType)) {
            this.lockName = "MysqlLockNode";
            //TODO MYSQL版本的分布式锁
            return null;
        }
        return null;
    }
}
