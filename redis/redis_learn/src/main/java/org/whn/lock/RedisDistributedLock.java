package org.whn.lock;

import cn.hutool.core.lang.Assert;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 13:13
 * @Description: redisLock
 * 引入DistributedLockFactory工厂模式，从工厂获得即可
 */
public class RedisDistributedLock implements Lock {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 锁名字
     */
    private final String lockName;
    /**
     * uuid
     */
    private final String uuidValue;
    /**
     * 有效时常
     */
    private final long expireTime;

    public RedisDistributedLock(StringRedisTemplate stringRedisTemplate, String lockName, String uuid) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
        this.uuidValue = uuid + ":" + Thread.currentThread().getId();
        this.expireTime = 30L;
    }

    @Override
    public void lock() {
        tryLock();
    }

    @Override
    public boolean tryLock() {
        try {
            tryLock(expireTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加锁
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        Assert.notNull(time, "time must not be null");
        Assert.isTrue(time > 0L, "time must not big than 0L");
        // lua 脚本 当 keys1不存在 或 keys1 的元素 argv1 不存在时，执行 hincrby 命令并设置有效时常  （hincrby 可以替代 hset 命令）
        // 使用 lua 脚本可以保证原子性
        // hincrby 命令 返回 1 表示加锁成功 同时可以实现锁的重入性 每次重入便会+1
        String script =
                "if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],ARGV[1]) == 1 then    " +
                        "redis.call('hincrby',KEYS[1],ARGV[1],1)    " +
                        "redis.call('expire',KEYS[1],ARGV[2])    " +
                        "return 1  " +
                        "else   " +
                        "return 0 " +
                        "end";
        System.out.println("lockName:" + lockName + "\t" + "uuidValue:" + uuidValue);
        // 加锁失败 自旋重试加锁
        while (Boolean.FALSE.equals(stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockName), uuidValue, String.valueOf(time)))) {
            try {
                TimeUnit.MILLISECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //新建一个后台扫描程序，来坚持key目前的ttl，是否到我们规定的1/2 1/3来实现续期
        renewExpire();
        return true;

    }


    /**
     * 解锁
     */
    @Override
    public void unlock() {
        System.out.println("unlock(): lockName:" + lockName + "\t" + "uuidValue:" + uuidValue);
//     lua 脚本   如果 KEYS1 锁不存在返回 null 如果 KEYS[1] 的 ARGV[1] 参数-1 =0 则删除
        // nil = false 1 = true 0 = false
        String script =
                "if redis.call('HEXISTS',KEYS[1],ARGV[1]) == 0 then    " +
                        "return nil  " +
                        "elseif redis.call('HINCRBY',KEYS[1],ARGV[1],-1) == 0 then    " +
                        "return redis.call('del',KEYS[1])  " +
                        "else    " +
                        "return 0 " +
                        "end";
        Long flag = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(lockName), uuidValue, String.valueOf(expireTime));

        if (null == flag) {
            throw new RuntimeException("this lock doesn't exists，o(╥﹏╥)o");
        }
    }

    /**
     * 续期方法
     */
    private void renewExpire() {
        //lua 脚本 KEYS[1] 的 ARGV[1] 存在 则设置 KEYS[1] 的有效时间为 ARGV[2]
        String script =
                "if redis.call('HEXISTS',KEYS[1],ARGV[1]) == 1 then     " +
                        "return redis.call('expire',KEYS[1],ARGV[2]) " +
                        "else     " +
                        "return 0 " +
                        "end";

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockName), uuidValue, String.valueOf(expireTime))) {
                    renewExpire();
                }
            }
            //expireTime 的 1/3 秒执行一次
        }, (this.expireTime * 1000) / 3);
    }


    //====下面两个暂时用不到，不再重写
    //====下面两个暂时用不到，不再重写
    //====下面两个暂时用不到，不再重写
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
