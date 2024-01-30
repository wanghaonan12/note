# Redis 高级

## Redis 单线程 VS 多线程

1. redis到底是单线程还是多线程？

   > Redis的版本很多3.x、4.x、6.x，版本不同架构也是不同的，不限定版本问是否单线程也不太严谨。
   >
   > 1 版本3.x ，最早版本，也就是大家口口相传的redis是单线程，阳哥2016年讲解的redis就是3.X的版本。
   >
   > 2 版本4.x，严格意义来说也不是单线程，而是负责处理客户端请求的线程是单线程，但是开始加了点多线程的东西(异步删除)。---貌似
   >
   > 3 2020年5月版本的6.0.x后及2022年出的7.0版本后，告别了大家印象中的单线程，用一种全新的多线程来解决问题。---实锤

   ![image-20240130105330203](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130105330203.png)

   > Redis是单线程
   >
   > 主要是指Redis的网络IO和键值对读写是由一个线程来完成的，Redis在处理客户端的请求时包括获取 (socket 读)、解析、执行、内容返回 (socket 写) 等都由一个顺序串行的主线程处理，这就是所谓的“单线程”。这也是Redis对外提供键值存储服务的主要流程。

   ![image-20240130105640215](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130105640215.png)

2. redis为什么快？

   > 1. 基于内存操作：Redis 的所有数据都存在内存中，因此所有的运算都是内存级别的，所以他的性能比较高；
   > 2. 数据结构简单：Redis 的数据结构是专门设计的，而这些简单的数据结构的查找和操作的时间大部分复杂度都是 O(1)，因此性能比较高；
   > 3. 多路复用和非阻塞 I/O：Redis使用 I/O多路复用功能来监听多个 socket连接客户端，这样就可以使用一个线程连接来处理多个请求，减少线程切换带来的开销，同时也避免了 I/O 阻塞操作
   > 4. 避免上下文切换：因为是单线程模型，因此就避免了不必要的上下文切换和多线程竞争，这就省去了多线程切换带来的时间和性能上的消耗，而且单线程不会导致死锁问题的发生

3. Redis为什么选择单线程？

   **因为是单线程模型，因此就避免了不必要的上下文切换和多线程竞争，这就省去了多线程切换带来的时间和性能上的消耗，而且单线程不会导致死锁问题的发生**

4. 既然单线程这么好，为什么逐渐又加入了多线程特性？

   **正常情况下使用 del 指令可以很快的删除数据，而当被删除的 key 是一个非常大的对象时，例如时包含了成千上万个元素的 hash 集合时，那么 del 指令就会造成 Redis 主线程卡顿。这就是redis3.x单线程时代最经典的故障，大key删除的头疼问题，由于redis是单线程的，del bigKey .....等待很久这个线程才会释放，类似加了一个synchronized锁，你可以想象高并发下，程序堵成什么样子？所以在一些功能上做了分支线程**

## BigKey

1. 如何生产上限制keys */flushdb/flushall等危险命令以防止误删误用？

   > 可以更改共享数据库中危险命令的名称'''' 为空时表示禁用
   >
   > ```
   > rename-command keys ""
   > rename-command flushdb ""
   > rename-command flushall ""
   > ```

2. MEMORY USAGE 命令

   > ```bash
   > # 语法MEMORY USAGE key 返回 redis中指定 key 占用的的大小
   > MEMORY USAGE order:797
   > 128
   > ```

3. BigKey问题，多大算big？你如何发现？如何删除？如何处理？

   **阿里规范**

   ![image-20240130185048272](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130185048272.png)

   **危害：1. 内存不均，集群迁移困难 2. 超时删除，大key删除过慢 3. 网络阻塞**

   ```bash
   # 使用 bigkeys进行查看 会列出string hash list set 等类型参数中的key的大小
   redis-cli -a 111111 -p 6384 --bigkeys
   ```

   ![image-20240130185823805](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130185823805.png)

   **如何删除**

   > 每种类型的bigkey删除使用不同的方法。
   >
   > 1. String ： 使用nulink进行删除
   >
   > 2. Hash ，Set，Zset ：需要进行渐进式删除降低容量，最后在进行 `del key`
   >
   >    ```java
   >    public class RedisUtils {
   >    
   >    
   >        /**
   >         * @param host       redis 地址
   >         * @param port       redis 端口
   >         * @param password   redis 密码
   >         * @param bigHashKey keyName
   >         *                   使用 hscan 每次获取少量 field-value 在删除 hdel 删除 field
   >         */
   >        public void delBigHash(String host, int port, String password, String bigHashKey) {
   >            try (Jedis jedis = new Jedis(host, port)) {
   >                if (StringUtils.hasLength(password)) {
   >                    jedis.auth(password);
   >                }
   >                ScanParams scanParams = new ScanParams().count(100);
   >                String cursor = "0";
   >                do {
   >    
   >                    ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
   >                    List<Map.Entry<String, String>> entryList = scanResult.getResult();
   >                    if (entryList != null && !entryList.isEmpty()) {
   >                        for (Map.Entry<String, String> entry : entryList) {
   >                            jedis.hdel(bigHashKey, entry.getKey());
   >                        }
   >                        cursor = scanResult.getCursor();
   >                    }
   >                } while (!"0".equals(cursor));
   >                //删除key
   >                jedis.del(bigHashKey);
   >            }
   >        }
   >    
   >        /**
   >         * @param host       redis 地址
   >         * @param port       redis 端口
   >         * @param password   redis 密码
   >         * @param bigListKey keyName
   >         *                   使用 ltrim 渐进式删除
   >         */
   >        public void delBigList(String host, int port, String password, String bigListKey) {
   >            try (Jedis jedis = new Jedis(host, port)) {
   >                if (StringUtils.hasLength(password)) {
   >                    jedis.auth(password);
   >                }
   >                long len = jedis.llen(bigListKey);
   >                int counter = 0;
   >                int left = 100;
   >                while (counter > len) {
   >                    //每次从左侧藏描100个
   >                    jedis.ltrim(bigListKey, left, len);
   >                    counter += left;
   >                }
   >                //删除key
   >                jedis.del(bigListKey);
   >            }
   >    
   >        }
   >    
   >        /**
   >         * @param host      redis 地址
   >         * @param port      redis 端口
   >         * @param password  redis 密码
   >         * @param bigSetKey keyName
   >         *                  使用 sscan 每次获取少量 field-value 在删除 srem 删除 field
   >         */
   >        public void delBigSet(String host, int port, String password, String bigSetKey) {
   >            try (Jedis jedis = new Jedis(host, port)) {
   >                if (StringUtils.hasLength(password)) {
   >                    jedis.auth(password);
   >                }
   >                ScanParams scanParams = new ScanParams().count(100);
   >                String cursor = "0";
   >                do {
   >                    ScanResult<String> scanResult = jedis.sscan(bigSetKey, cursor, scanParams);
   >                    List<String> entryList = scanResult.getResult();
   >                    if (entryList != null && !entryList.isEmpty()) {
   >                        for (String entry : entryList) {
   >                            jedis.srem(bigSetKey, entry);
   >                        }
   >                        cursor = scanResult.getCursor();
   >                    }
   >                } while (!"0".equals(cursor));
   >                ///删除key
   >                jedis.del(bigSetKey);
   >            }
   >        }
   >    
   >        /**
   >         * @param host       redis 地址
   >         * @param port       redis 端口
   >         * @param password   redis 密码
   >         * @param bigZSetKey keyName
   >         * 使用 zscan 每次获取少量 field-value 在删除 zremrangeByRank 删除 field
   >         */
   >        public void delBigZSet(String host, int port, String password, String bigZSetKey) {
   >            try (Jedis jedis = new Jedis(host, port)) {
   >                if (StringUtils.hasLength(password)) {
   >                    jedis.auth(password);
   >                }
   >                ScanParams scanParams = new ScanParams().count(100);
   >                String cursor = "0";
   >                do {
   >                    ScanResult<Tuple> scanResult = jedis.zscan(bigZSetKey, cursor, scanParams);
   >                    List<Tuple> entryList = scanResult.getResult();
   >                    if (entryList != null && !entryList.isEmpty()) {
   >                        jedis.zremrangeByRank(bigZSetKey, 0, entryList.size());
   >                        cursor = scanResult.getCursor();
   >                    }   
   >                } while (!"0".equals(cursor));
   >                //删除key
   >                jedis.del(bigZSetKey);
   >            }
   >        }
   >    }
   >    
   >    ```
   >
   >    

4. BigKey你做过调优吗？惰性释放lazyfree了解过吗?

   > lazyfree的原理不难想象，就是在删除对象时只是进行逻辑删除，然后把对象丢给后台，让后台线程去执行真正的destruct，避免由于对象体积过大而造成阻塞。redis的lazyfree实现即是如此，下面就是配置在什么时候进行一个lazy的del，在删除的时候会有自己的判断进行决定时使用正常的删除还是使用unlink

   ```bash
   lazyfree-lazy-eviction yes
   lazyfree-lazy-expire yes 
   lazyfree-lazy-server-del yes
   replica-lazy-flush no
   lazyfree-lazy-user-del yes
   lazyfree-lazy-user-flush no
   ```

   [官网内存优化 ](https://redis.io/docs/management/optimization/memory-optimization/)

   > 从 Redis 2.2 开始，许多数据类型都经过优化，以使用更少的空间，直到达到一定大小。 当小于给定数量的元素并达到最大元素大小时，哈希、列表、仅由整数组成的集合和排序集以非常节省内存的方式进行编码，使用的内存最多减少 10 倍（平均节省的*内存减少 5 倍*）。
   >
   > 从用户和 API 的角度来看，这是完全透明的。 由于这是 CPU/内存的权衡，因此可以调整最大值 特殊编码类型的元素数和最大元素大小 使用以下 redis.conf 指令（显示默认值）：

   ### Redis <= 6.2

   ```
   hash-max-ziplist-entries 512
   hash-max-ziplist-value 64
   zset-max-ziplist-entries 128 
   zset-max-ziplist-value 64
   set-max-intset-entries 512
   ```

   ### Redis >= 7.0

   ```
   hash-max-listpack-entries 512
   hash-max-listpack-value 64
   zset-max-listpack-entries 128
   zset-max-listpack-value 64
   set-max-intset-entries 512
   ```

   ### Redis >= 7.2

   还提供以下指令：

   ```
   set-max-listpack-entries 128
   set-max-listpack-value 64
   ```

   如果特殊编码的值超出了配置的最大大小， Redis 会自动将其转换为普通编码。 对于小值，此操作非常快， 但是，如果您更改设置以使用特殊编码的值 对于更大的聚合类型，建议运行一些 基准测试和测试以检查转换时间。

5. Morekey问题，生产上redis数据库有1000W记录，你如何遍历？key *可以吗?

   [Scan |Redis ](https://redis.io/commands/scan/)

   > 使用scan命令对key 进行遍历 ，效果有点类似 mysql 的分页查询
   >
   > ```bash
   > SCAN cursor [MATCH pattern] [COUNT count] [TYPE type]
   > # cursor  游标地址从 0 开始 到 0 结束 每次请求都会返回一个游标 要继续从这个地方开始才不会遗漏的遍历完所有
   > #[MATCH pattern]  key的匹配规则
   > #[COUNT count] 类似 mysql 中的 limit
   > #[TYPE type] key的类型
   > ```
   >
   > 该命令和密切相关的命令 [`SSCAN、`](https://redis.io/commands/sscan)[`HSCAN`](https://redis.io/commands/hscan) 和 [`ZSCAN`](https://redis.io/commands/zscan) 用于以增量方式循环访问元素集合。`SCAN`
   >
   > - `SCAN`循环访问当前选定的 Redis 数据库中的密钥集。
   > - [`SSCAN`](https://redis.io/commands/sscan) 循环访问 Sets 类型的元素。
   > - [`HSCAN`](https://redis.io/commands/hscan) 迭代哈希类型及其关联值的字段。
   > - [`ZSCAN`](https://redis.io/commands/zscan) 迭代排序集类型的元素及其关联的分数。
   >
   > ![image-20240130202700074](C:/Users/wangRich/AppData/Roaming/Typora/typora-user-images/image-20240130202700074.png)

## 缓存双写一致性更新策略

> 数据库和Redis的读写一致性问题

![image-20240130203236924](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130203236924.png)





## Redis 与 Mysql 数据双写一致性案例



## BitMap、Hyperloglog、GEO 实战



## BloomFilter（过滤器）



## 缓存预热+缓存雪崩+缓存击穿+缓存穿透



## Redis 分布式锁（手写）



## Redlock算法

