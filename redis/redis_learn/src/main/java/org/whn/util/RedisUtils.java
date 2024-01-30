package org.whn.util;

import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.Tuple;

import java.util.List;
import java.util.Map;

/**
 * @Author: WangHn
 * @Date: 2024/1/30 19:09
 * @Description:
 */
public class RedisUtils {


    /**
     * @param host       redis 地址
     * @param port       redis 端口
     * @param password   redis 密码
     * @param bigHashKey keyName
     *                   使用 hscan 每次获取少量 field-value 在删除 hdel 删除 field
     */
    public void delBigHash(String host, int port, String password, String bigHashKey) {
        try (Jedis jedis = new Jedis(host, port)) {
            if (StringUtils.hasLength(password)) {
                jedis.auth(password);
            }
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {

                ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
                List<Map.Entry<String, String>> entryList = scanResult.getResult();
                if (entryList != null && !entryList.isEmpty()) {
                    for (Map.Entry<String, String> entry : entryList) {
                        jedis.hdel(bigHashKey, entry.getKey());
                    }
                    cursor = scanResult.getCursor();
                }
            } while (!"0".equals(cursor));
            //删除key
            jedis.del(bigHashKey);
        }
    }

    /**
     * @param host       redis 地址
     * @param port       redis 端口
     * @param password   redis 密码
     * @param bigListKey keyName
     *                   使用 ltrim 渐进式删除
     */
    public void delBigList(String host, int port, String password, String bigListKey) {
        try (Jedis jedis = new Jedis(host, port)) {
            if (StringUtils.hasLength(password)) {
                jedis.auth(password);
            }
            long len = jedis.llen(bigListKey);
            int counter = 0;
            int left = 100;
            while (counter > len) {
                //每次从左侧藏描100个
                jedis.ltrim(bigListKey, left, len);
                counter += left;
            }
            //删除key
            jedis.del(bigListKey);
        }

    }

    /**
     * @param host      redis 地址
     * @param port      redis 端口
     * @param password  redis 密码
     * @param bigSetKey keyName
     *                  使用 sscan 每次获取少量 field-value 在删除 srem 删除 field
     */
    public void delBigSet(String host, int port, String password, String bigSetKey) {
        try (Jedis jedis = new Jedis(host, port)) {
            if (StringUtils.hasLength(password)) {
                jedis.auth(password);
            }
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.sscan(bigSetKey, cursor, scanParams);
                List<String> entryList = scanResult.getResult();
                if (entryList != null && !entryList.isEmpty()) {
                    for (String entry : entryList) {
                        jedis.srem(bigSetKey, entry);
                    }
                    cursor = scanResult.getCursor();
                }
            } while (!"0".equals(cursor));
            ///删除key
            jedis.del(bigSetKey);
        }
    }

    /**
     * @param host       redis 地址
     * @param port       redis 端口
     * @param password   redis 密码
     * @param bigZSetKey keyName
     * 使用 zscan 每次获取少量 field-value 在删除 zremrangeByRank 删除 field
     */
    public void delBigZSet(String host, int port, String password, String bigZSetKey) {
        try (Jedis jedis = new Jedis(host, port)) {
            if (StringUtils.hasLength(password)) {
                jedis.auth(password);
            }
            ScanParams scanParams = new ScanParams().count(100);
            String cursor = "0";
            do {
                ScanResult<Tuple> scanResult = jedis.zscan(bigZSetKey, cursor, scanParams);
                List<Tuple> entryList = scanResult.getResult();
                if (entryList != null && !entryList.isEmpty()) {
                    jedis.zremrangeByRank(bigZSetKey, 0, entryList.size());
                    cursor = scanResult.getCursor();
                }
            } while (!"0".equals(cursor));
            //删除key
            jedis.del(bigZSetKey);
        }
    }
}
