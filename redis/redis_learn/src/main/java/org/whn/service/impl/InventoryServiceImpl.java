package org.whn.service.impl;

import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.whn.lock.DistributedLockFactory;
import org.whn.lock.LockType;
import org.whn.service.InventoryService;

import java.util.Locale;
import java.util.concurrent.locks.Lock;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 14:01
 * @Description: redisLock 实现类
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    public static final String CACHE_KEY_REDLOCK = InventoryServiceImpl.class.toString().toUpperCase(Locale.ROOT)+"_REDLOCK";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private DistributedLockFactory distributedLockFactory;


    //V9.1,引入Redisson对应的官网推荐RedLock算法实现类
    @Autowired
    private Redisson redisson;

    @Override
    public String selfLock() {
        String retMessage = "";

        Lock redisLock = distributedLockFactory.getDistributedLock(LockType.REDIS);
        redisLock.lock();
        try {
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存，每次减少一个
            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品,库存剩余:" + inventoryNumber;
                System.out.println(retMessage + "\t" + "服务端口号" + port);
                //暂停120秒钟线程,故意的，演示自动续期的功能。。。。。。
//                try {
//                    TimeUnit.SECONDS.sleep(120);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            } else {
                retMessage = "商品卖完了,o(╥﹏╥)o";
            }
        } finally {
            redisLock.unlock();
        }
        return retMessage + "\t" + "服务端口号" + port;
    }

    @Override
    public String redLock() {
        String retMessage = "";
        RLock redissonLock = redisson.getLock(CACHE_KEY_REDLOCK);

        redissonLock.lock();
        try {
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存，每次减少一个
            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品,库存剩余:" + inventoryNumber;
                System.out.println(retMessage + "\t" + "服务端口号" + port);
            } else {
                retMessage = "商品卖完了,o(╥﹏╥)o";
            }
        } finally {
            //改进点，只能删除属于自己的key，不能删除别人的
            if (redissonLock.isLocked() && redissonLock.isHeldByCurrentThread()) {
                redissonLock.unlock();
            }
        }
        return retMessage + "\t" + "服务端口号" + port;
    }

    @Autowired
    RedissonClient redissonClient1;

    @Autowired
    RedissonClient redissonClient2;

    @Autowired
    RedissonClient redissonClient3;
    @Override
    public String multiLock() {
        String retMessage = "";
        RLock lock1 = redissonClient1.getLock(CACHE_KEY_REDLOCK);
        RLock lock2 = redissonClient2.getLock(CACHE_KEY_REDLOCK);
        RLock lock3 = redissonClient3.getLock(CACHE_KEY_REDLOCK);
        RedissonMultiLock redLock = new RedissonMultiLock(lock1, lock2, lock3);
        redLock.lock();
        try {
            //1 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存，每次减少一个
            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "成功卖出一个商品,库存剩余:" + inventoryNumber;
                System.out.println(retMessage + "\t" + "服务端口号" + port);
            } else {
                retMessage = "商品卖完了,o(╥﹏╥)o";
            }
        } finally {
            //改进点，只能删除属于自己的key，不能删除别人的
//            if (redLock.isLocked() && redLock.isHeldByCurrentThread()) {
                redLock.unlock();
//            }
        }
        return retMessage + "\t" + "服务端口号" + port;
    }
}
