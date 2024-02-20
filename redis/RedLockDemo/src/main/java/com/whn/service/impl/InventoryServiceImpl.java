package com.whn.service.impl;

import com.whn.service.InventoryService;
import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 14:01
 * @Description: redisLock 实现类
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    public static final String CACHE_KEY_REDLOCK = InventoryServiceImpl.class.toString().toUpperCase(Locale.ROOT)+"_REDLOCK";

    @Value("${server.port}")
    private String port;
    //V9.1,引入Redisson对应的官网推荐RedLock算法实现类
//    @Autowired
//    private Redisson redisson;


//    @Autowired
//    RedissonClient redissonClient1;
//
//    @Autowired
//    RedissonClient redissonClient2;
//
//    @Autowired
//    RedissonClient redissonClient3;
//    @Override
//    public String multiLock() {
//        String retMessage = "";
//        RLock lock1 = redissonClient1.getLock(CACHE_KEY_REDLOCK);
//        RLock lock2 = redissonClient2.getLock(CACHE_KEY_REDLOCK);
//        RLock lock3 = redissonClient3.getLock(CACHE_KEY_REDLOCK);
//        RedissonMultiLock redLock = new RedissonMultiLock(lock1, lock2, lock3);
//        redLock.lock();
//        try {
//            //1 查询库存信息
//            String result = "100";
//            //2 判断库存是否足够
//            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
//            //3 扣减库存，每次减少一个
//            if (inventoryNumber > 0) {
//                --inventoryNumber;
//                retMessage = "成功卖出一个商品,库存剩余:" + inventoryNumber;
//                System.out.println(retMessage + "\t" + "服务端口号" + port);
//            } else {
//                retMessage = "商品卖完了,o(╥﹏╥)o";
//            }
//            try {
//                this.wait(15000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            //改进点，只能删除属于自己的key，不能删除别人的
////            if (redLock.isLocked() && redLock.isHeldByCurrentThread()) {
//                redLock.unlock();
////            }
//        }
//        return retMessage + "\t" + "服务端口号" + port;
//    }

    @Override
    public String multiLock() {
        String retMessage = "";
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://43.138.25.182:6385")
                .setPassword("134679").setDatabase(0);
        RedissonClient redissonClient1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSingleServer().setAddress("redis://43.138.25.182:6386")
                .setPassword("134679").setDatabase(0);
        RedissonClient redissonClient2 = Redisson.create(config2);

        Config config3 = new Config();
        config3.useSingleServer().setAddress("redis://43.138.25.182:6387")
                .setPassword("134679").setDatabase(0);
        RedissonClient redissonClient3 = Redisson.create(config3);

        RLock lock1 = redissonClient1.getLock(CACHE_KEY_REDLOCK);
        RLock lock2 = redissonClient2.getLock(CACHE_KEY_REDLOCK);
        RLock lock3 = redissonClient3.getLock(CACHE_KEY_REDLOCK);
        RedissonMultiLock redLock = new RedissonMultiLock(lock1, lock2, lock3);
        redLock.lock();
        try {
            //1 查询库存信息
            String result = "100";
            //2 判断库存是否足够
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            //3 扣减库存，每次减少一个
            if (inventoryNumber > 0) {
                --inventoryNumber;
                retMessage = "成功卖出一个商品,库存剩余:" + inventoryNumber;
                System.out.println(retMessage + "\t" + "服务端口号" + port);
            } else {
                retMessage = "商品卖完了,o(╥﹏╥)o";
            }
            try {
                this.wait(15000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
