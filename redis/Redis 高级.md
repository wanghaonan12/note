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
   > 
   > 
   >    ```java
   >       /**
   >     * @param host       redis 地址
   >     * @param port       redis 端口
   >     * @param password   redis 密码
   >     * @param bigHashKey keyName
   >     *                   使用 hscan 每次获取少量 field-value 在删除 hdel 删除 field
   >     */
   >    public void delBigHash(String host, int port, String password, String bigHashKey) {
   >        try (Jedis jedis = new Jedis(host, port)) {
   >            if (StringUtils.hasLength(password)) {
   >                jedis.auth(password);
   >            }
   >            ScanParams scanParams = new ScanParams().count(100);
   >            String cursor = "0";
   >            do {
   > 
   >                       ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(bigHashKey, cursor, scanParams);
   >                List<Map.Entry<String, String>> entryList = scanResult.getResult();
   >                if (entryList != null && !entryList.isEmpty()) {
   >                    for (Map.Entry<String, String> entry : entryList) {
   >                        jedis.hdel(bigHashKey, entry.getKey());
   >                    }
   >                    cursor = scanResult.getCursor();
   >                }
   >            } while (!"0".equals(cursor));
   >            //删除key
   >            jedis.del(bigHashKey);
   >        }
   >    }
   > 
   >           /**
   >     * @param host       redis 地址
   >     * @param port       redis 端口
   >     * @param password   redis 密码
   >     * @param bigListKey keyName
   >     *                   使用 ltrim 渐进式删除
   >     */
   >    public void delBigList(String host, int port, String password, String bigListKey) {
   >        try (Jedis jedis = new Jedis(host, port)) {
   >            if (StringUtils.hasLength(password)) {
   >                jedis.auth(password);
   >            }
   >            long len = jedis.llen(bigListKey);
   >            int counter = 0;
   >            int left = 100;
   >            while (counter > len) {
   >                //每次从左侧藏描100个
   >                jedis.ltrim(bigListKey, left, len);
   >                counter += left;
   >            }
   >            //删除key
   >            jedis.del(bigListKey);
   >        }
   > 
   >           }
   > 
   >           /**
   >     * @param host      redis 地址
   >     * @param port      redis 端口
   >     * @param password  redis 密码
   >     * @param bigSetKey keyName
   >     *                  使用 sscan 每次获取少量 field-value 在删除 srem 删除 field
   >     */
   >    public void delBigSet(String host, int port, String password, String bigSetKey) {
   >        try (Jedis jedis = new Jedis(host, port)) {
   >            if (StringUtils.hasLength(password)) {
   >                jedis.auth(password);
   >            }
   >            ScanParams scanParams = new ScanParams().count(100);
   >            String cursor = "0";
   >            do {
   >                ScanResult<String> scanResult = jedis.sscan(bigSetKey, cursor, scanParams);
   >                List<String> entryList = scanResult.getResult();
   >                if (entryList != null && !entryList.isEmpty()) {
   >                    for (String entry : entryList) {
   >                        jedis.srem(bigSetKey, entry);
   >                    }
   >                    cursor = scanResult.getCursor();
   >                }
   >            } while (!"0".equals(cursor));
   >            ///删除key
   >            jedis.del(bigSetKey);
   >        }
   >    }
   > 
   >           /**
   >     * @param host       redis 地址
   >     * @param port       redis 端口
   >     * @param password   redis 密码
   >     * @param bigZSetKey keyName
   >     * 使用 zscan 每次获取少量 field-value 在删除 zremrangeByRank 删除 field
   >     */
   >    public void delBigZSet(String host, int port, String password, String bigZSetKey) {
   >        try (Jedis jedis = new Jedis(host, port)) {
   >            if (StringUtils.hasLength(password)) {
   >                jedis.auth(password);
   >            }
   >            ScanParams scanParams = new ScanParams().count(100);
   >            String cursor = "0";
   >            do {
   >                ScanResult<Tuple> scanResult = jedis.zscan(bigZSetKey, cursor, scanParams);
   >                List<Tuple> entryList = scanResult.getResult();
   >                if (entryList != null && !entryList.isEmpty()) {
   >                    jedis.zremrangeByRank(bigZSetKey, 0, entryList.size());
   >                    cursor = scanResult.getCursor();
   >                }   
   >            } while (!"0".equals(cursor));
   >            //删除key
   >            jedis.del(bigZSetKey);
   >        }
   >    }
   > ```
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

下图中的数据流向是：

1. `service`从`redis`中获取数据，如果有则进行返回
2. `service`从`redis`中获取数据，**没有**责前往`mysql`数据库获取返回并重写进`redis`方便下次访问

![image-20240130203236924](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240130203236924.png)

1. 如何解决一致性问题？

   > 1. redis 中有数据则需要和 数据库中的保持一致
   > 2. redis 没有数据 ，需要获取最新的数据添加到redis中

   **代码实现**

   pom

   ```xml
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.5.14</version>
           <relativePath/>
       </parent>
   
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <maven.compiler.source>1.8</maven.compiler.source>
           <maven.compiler.target>1.8</maven.compiler.target>
           <junit.version>4.12</junit.version>
           <nacos.context>2.1.0-RC</nacos.context>
   <!--        <swagger.ui>2.9.2</swagger.ui>-->
           <swagger.ui>3.0.0</swagger.ui>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <maven.compiler.source>1.8</maven.compiler.source>
           <maven.compiler.target>1.8</maven.compiler.target>
           <log4j.version>1.2.17</log4j.version>
           <lombok.version>1.18.26</lombok.version>
           <mysql.version>5.1.47</mysql.version>
           <druid.version>1.1.16</druid.version>
           <druid.spring.boot.starter.version>1.1.10</druid.spring.boot.starter.version>
           <mapper.version>4.1.5</mapper.version>
           <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
           <mysql.connector.version>5.1.47</mysql.connector.version>
           <hutool.version>5.2.3</hutool.version>
           <mybatis.plus.boot.starter.version>3.2.0</mybatis.plus.boot.starter.version>
       </properties>
   
       <dependencies>
           <!--SpringBoot通用依赖模块-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <!--jedis-->
           <dependency>
               <groupId>redis.clients</groupId>
               <artifactId>jedis</artifactId>
               <version>4.3.1</version>
           </dependency>
           <!--通用基础配置-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>${junit.version}</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
           <dependency>
               <groupId>log4j</groupId>
               <artifactId>log4j</artifactId>
               <version>${log4j.version}</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>${lombok.version}</version>
               <optional>true</optional>
           </dependency>
           <dependency>
               <groupId>com.alibaba.nacos</groupId>
               <artifactId>nacos-spring-context</artifactId>
               <version>${nacos.context}</version>
           </dependency>
           <dependency>
               <groupId>com.alibaba.boot</groupId>
               <artifactId>nacos-discovery-spring-boot-starter</artifactId>
               <version>0.2.12</version>
           </dependency>
           <!--SpringBoot与Redis整合依赖-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-data-redis</artifactId>
           </dependency>
           <dependency>
               <groupId>org.apache.commons</groupId>
               <artifactId>commons-pool2</artifactId>
           </dependency>
           <!--swagger2-->
           <dependency>
               <groupId>io.springfox</groupId>
               <artifactId>springfox-boot-starter</artifactId>
               <version>${swagger.ui}</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>${lombok.version}</version>
           </dependency>
   
           <!--Mysql数据库驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>${mysql.connector.version}</version>
           </dependency>
           <!--SpringBoot集成druid连接池-->
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid-spring-boot-starter</artifactId>
               <version>${druid.spring.boot.starter.version}</version>
           </dependency>
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid</artifactId>
               <version>${druid.version}</version>
           </dependency>
           <!--mybatis plus和springboot整合-->
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>${mybatis.plus.boot.starter.version}</version>
           </dependency>
           <!--通用基础配置junit/devtools/test/log4j/lombok/hutool-->
           <!--hutool-->
           <dependency>
               <groupId>cn.hutool</groupId>
               <artifactId>hutool-all</artifactId>
               <version>${hutool.version}</version>
           </dependency>
       </dependencies>
   ```

   yml

   ```yml
   server:
     port: 1346
   spring:
     datasource:
       driver-class-name: com.mysql.jdbc.Driver
       druid:
         test-while-idle: false
       password: 123456
       type: com.alibaba.druid.pool.DruidDataSource
       url: jdbc:mysql://43.138.25.182:3306/study?useUnicode=true&characterEncoding=utf-8&useSSL=false
       username: root
     swagger2:
       enabled: true
     application:
       name: redis_learn
     redis:
   #    单机连接
   #    host:  43.138.25.182
   #    port: 6379
   #    database: 0
   #    集群连接
       cluster:
         nodes: 43.138.25.182:6379,43.138.25.182:6380,43.138.25.182:6381,43.138.25.182:6382,43.138.25.182:6383,43.138.25.182:6384
         max-redirects: 3
       password: 111111
       lettuce:
         cluster:
           refresh:
             #支持集群拓扑动态感应刷新,自适应拓扑刷新是否使用所有可用的更新，默认false关闭
             adaptive: true
             #定时刷新
             period: 2000
   
     #在springboot2.6.X结合swagger2.9.X会提示documentationPluginsBootstrapper空指针异常，
     #原因是在springboot2.6.X中将SpringMVC默认路径匹配策略从AntPathMatcher更改为PathPatternParser，
     # 导致出错，解决办法是matching-strategy切换回之前ant_path_matcher
     mvc:
       pathmatch:
         matching-strategy: ant_path_matcher
   nacos:
     discovery:
       server-addr: 43.138.25.182:8848
   ```

   service

   > findUserById2  比 findUserById 多一个`synchronized` 锁 作用是防止在高并发情况下的缓存击穿 用来减少mysql的压力 ，在第一个检索完后会触发并添加到redis中 所以进行 双检加锁策略进一步减少 mysql压力

   ```java
   @Service
   public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser>
           implements TUserService {
       public static final String CACHE_KEY_USER = "user:";
       @Resource
       private RedisTemplate redisTemplate;
   
       /**
        * 业务逻辑没有写错，对于小厂中厂(QPS《=1000)可以使用，但是大厂不行
        *
        * @param id
        * @return
        */
       @Override
       public TUser findUserById(Long id) {
           TUser user = null;
           String key = CACHE_KEY_USER + id;
           //1 先从redis里面查询，如果有直接返回结果，如果没有再去查询mysql
           user = (TUser) redisTemplate.opsForValue().get(key);
           if (user == null) {
               //2 redis里面无，继续查询mysql
               user = getById(id);
               if (user == null) {
                   //3.1 redis+mysql 都无数据
                   //你具体细化，防止多次穿透，我们业务规定，记录下导致穿透的这个key回写redis
                   return user;
               } else {
                   //3.2 mysql有，需要将数据写回redis，保证下一次的缓存命中率
                   redisTemplate.opsForValue().set(key, user);
               }
           }
           return user;
       }
   
   
       /**
        * 加强补充，避免突然key失效了，打爆mysql，做一下预防，尽量不出现击穿的情况。
        *
        * @param id
        * @return
        */
       @Override
       public TUser findUserById2(Long id) {
           TUser user = null;
           String key = CACHE_KEY_USER + id;
           //1 先从redis里面查询，如果有直接返回结果，如果没有再去查询mysql，
           // 第1次查询redis，加锁前
           user = (TUser) redisTemplate.opsForValue().get(key);
           if (user == null) {
               //2 大厂用，对于高QPS的优化，进来就先加锁，保证一个请求操作，让外面的redis等待一下，避免击穿mysql
               synchronized (TUserServiceImpl.class) {
                   //第2次查询redis，加锁后
                   user = (TUser) redisTemplate.opsForValue().get(key);
                   //3 二次查redis还是null，可以去查mysql了(mysql默认有数据)
                   if (user == null) {
                       //4 查询mysql拿数据(mysql默认有数据)
                       user = getById(id);
                       if (user == null) {
                           return null;
                       } else {
                           //5 mysql里面有数据的，需要回写redis，完成数据一致性的同步工作
                           redisTemplate.opsForValue().setIfAbsent(key, user, 7L, TimeUnit.DAYS);
                       }
                   }
               }
           }
           return user;
       }
   
       @Override
       public boolean addUser(String name) {
           return save(TUser.builder().username(name).build());
       }
   
       /**
        * 删除时先删除 缓存数据
        * @param id
        * @return
        */
       @Override
       public boolean deleteUser(Long id) {
           String key = CACHE_KEY_USER + id;
           redisTemplate.delete(key);
           return removeById(id);
       }
   
       /**
        * 延时双删 防止缓存数据不能及时更新导致的脏数据
        * @param user
        * @return
        */
       @Override
       public boolean updateUser(TUser user) {
           String key = CACHE_KEY_USER + user.getId();
           redisTemplate.delete(key);
           boolean b = updateById(user);
           redisTemplate.delete(key);
           return b;
       }
   }
   ```

   config

   ```java
   @Configuration
   public class RedisConfig
   {
       /**
        * redis序列化的工具配置类，下面这个请一定开启配置
        * 127.0.0.1:6379> keys *
        * 1) "ord:102"  序列化过
        * 2) "\xac\xed\x00\x05t\x00\aord:102"   野生，没有序列化过
        * this.redisTemplate.opsForValue(); //提供了操作string类型的所有方法
        * this.redisTemplate.opsForList(); // 提供了操作list类型的所有方法
        * this.redisTemplate.opsForSet(); //提供了操作set的所有方法
        * this.redisTemplate.opsForHash(); //提供了操作hash表的所有方法
        * this.redisTemplate.opsForZSet(); //提供了操作zset的所有方法
        * @param lettuceConnectionFactory
        * @return
        */
       @Bean
       public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory)
       {
           RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
   
           redisTemplate.setConnectionFactory(lettuceConnectionFactory);
           //设置key序列化方式string
           redisTemplate.setKeySerializer(new StringRedisSerializer());
           //设置value的序列化方式json，使用GenericJackson2JsonRedisSerializer替换默认序列化
           redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
   
           redisTemplate.setHashKeySerializer(new StringRedisSerializer());
           redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
   
           redisTemplate.afterPropertiesSet();
   
           return redisTemplate;
       }
   }
   ```

   controller

   ```java
   @RestController
   @Slf4j
   @ApiModel(value = "redis 读写一致性测试",description = "redis 读写一致性测试")
   @RequestMapping("/user")
   public class TUserController
   {
   
       @Autowired
       private TUserServiceImpl tUserServiceImpl;
   
       @ApiOperation("新增用户")
       @PostMapping(value = "/add/{name}")
       public boolean addOrder(@PathVariable String name)
       {
         return  tUserServiceImpl.addUser(name);
       }
   
   
       @ApiOperation("删除用户")
       @DeleteMapping(value = "/{id}")
       public boolean deleteUser(@PathVariable Long id)
       {
           return tUserServiceImpl.deleteUser(id);
       }
   
       @ApiOperation("获取用户2")
       @GetMapping(value = "/findUserById/{id}")
       public TUser findUserById(@PathVariable Long id)
       {
           return tUserServiceImpl.findUserById(id);
       }
       @ApiOperation("获取用户2")
       @GetMapping(value = "/findUserById2/{id}")
       public TUser findUserById2(@PathVariable Long id)
       {
           return tUserServiceImpl.findUserById2(id);
       }
   
   }
   ```

   ![image-20240218144410103](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218144410103.png)

   ![image-20240218144522406](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218144522406.png)

   

2. 双写一致性，你先动缓存redisi还是数据库mysql哪一个？why?

   > 一般情况下 mysql 数据库作为底单数据 作最终解释

3. 延时双删你做过吗？会有哪些问题？

   > 1. 延迟是时间不好控制
   > 2. 虽然不多但还是会影响一点性能

4. 有这么一种情况，微服务查询redis无mysql有，为保证数据双写一致性回写redis你需要注意什么？双检加锁策略你了解过吗？如何尽量避免缓存击穿？

   > 在查询 `redis` 无法获取数据后添加一把锁再次检索`redis`,如果有数据则返回 。没有则检索数据库 检索到则添加到缓存并返回。

5. redis和mysq双写100%会出漏，做不到强一致性，你如何保证最终一致性？

   > 如果业务层要求必须读取一致性的数据：
   >
   > 1. 在更新数据库时，先在Redis缓存客户端暂停并发读请求，等数据库更新完、缓存值删除后，再读取数据，从而保证数据一致性，这是理论可以达到的效果，但实际，不推荐，因为真实生产环境中，分布式下很难做到实时一致性，一般都是最终一致性，请大家参考。
   >
   > 2. 使用阿里的cannl 但是 canal 也无法保证100%的一致性

## Redis 与 Mysql 数据双写一致性案例



## BitMap、Hyperloglog、GEO 实战



## BloomFilter（过滤器）



## 缓存预热+缓存雪崩+缓存击穿+缓存穿透

### 名词解释

![image-20240218163223070](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218163223070.png)

1. 缓存预热

   > 当系统上线时，缓存内还没有数据，如果直接提供给用户使用，每个请求都会穿过缓存去访问底层数据库，如果并发大的话，很有可能在上线当天就会宕机，因此我们需要在上线前先将数据库内的热点数据缓存至Redis内再提供出去使用
   > **处理方法：**
   >
   > 1. 人工将所有用户会进行大量访问的页面进行测试操作，由测试或是开发人员提前将数据加入缓存
   > 2. 编写程序脚本 在程序启动时加入数据库中数据到`redis`中

2. 缓存雪崩

   > `redis` 查无此数据 大量的查询发送大 `mysql` 数据库
   >
   > **处理方法：**
   >
   > 硬件原因导致
   >
   > 1. 使用主从
   > 2. 使用集群（推荐）
   > 3. 使用哨兵
   >
   > 软件原因导致
   >
   > 1. 服务降级
   > 2. 差异时间存储

3. 缓存穿透

   ![image-20240218163552884](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218163552884.png)

   > 简单说就是本来无一物，两库都没有。既不在Redis缓存库，也不在mysql,数据库存在被多次暴击风险
   >
   > **处理方法：**
   >
   > 1. 空对象缓存或者缺省值缓存（如果使用不同的key进行请求则会导致缺省缓存失效）
   > 2. 使用Google布隆过滤器Guava解决缓存穿透（不符合过滤器条件的请求会直接返回 不会经过redis）

   **案例：**

   依赖

   ```xml
     <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.5.14</version>
           <relativePath/>
       </parent>
   
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <maven.compiler.source>1.8</maven.compiler.source>
           <maven.compiler.target>1.8</maven.compiler.target>
           <junit.version>4.12</junit.version>
           <nacos.context>2.1.0-RC</nacos.context>
   <!--        <swagger.ui>2.9.2</swagger.ui>-->
           <swagger.ui>3.0.0</swagger.ui>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <maven.compiler.source>1.8</maven.compiler.source>
           <maven.compiler.target>1.8</maven.compiler.target>
           <log4j.version>1.2.17</log4j.version>
           <lombok.version>1.18.26</lombok.version>
           <mysql.version>5.1.47</mysql.version>
           <druid.version>1.1.16</druid.version>
           <druid.spring.boot.starter.version>1.1.10</druid.spring.boot.starter.version>
           <mapper.version>4.1.5</mapper.version>
           <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
           <mysql.connector.version>5.1.47</mysql.connector.version>
           <hutool.version>5.2.3</hutool.version>
           <mybatis.plus.boot.starter.version>3.2.0</mybatis.plus.boot.starter.version>
           <guava.version>23.0</guava.version>
       </properties>
   
       <dependencies>
           <!--guava Google 开源的 Guava 中自带的布隆过滤器-->
           <dependency>
               <groupId>com.google.guava</groupId>
               <artifactId>guava</artifactId>
               <version>${guava.version}</version>
           </dependency>
           <!--SpringBoot通用依赖模块-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
           <!--jedis-->
           <dependency>
               <groupId>redis.clients</groupId>
               <artifactId>jedis</artifactId>
               <version>4.3.1</version>
           </dependency>
           <!--通用基础配置-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>${junit.version}</version>
           </dependency>
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
           <dependency>
               <groupId>log4j</groupId>
               <artifactId>log4j</artifactId>
               <version>${log4j.version}</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>${lombok.version}</version>
               <optional>true</optional>
           </dependency>
           <dependency>
               <groupId>com.alibaba.nacos</groupId>
               <artifactId>nacos-spring-context</artifactId>
               <version>${nacos.context}</version>
           </dependency>
           <dependency>
               <groupId>com.alibaba.boot</groupId>
               <artifactId>nacos-discovery-spring-boot-starter</artifactId>
               <version>0.2.12</version>
           </dependency>
           <!--SpringBoot与Redis整合依赖-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-data-redis</artifactId>
           </dependency>
           <dependency>
               <groupId>org.apache.commons</groupId>
               <artifactId>commons-pool2</artifactId>
           </dependency>
           <!--swagger2-->
           <dependency>
               <groupId>io.springfox</groupId>
               <artifactId>springfox-boot-starter</artifactId>
               <version>${swagger.ui}</version>
           </dependency>
           <dependency>
               <groupId>org.projectlombok</groupId>
               <artifactId>lombok</artifactId>
               <version>${lombok.version}</version>
           </dependency>
   
           <!--Mysql数据库驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>${mysql.connector.version}</version>
           </dependency>
           <!--SpringBoot集成druid连接池-->
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid-spring-boot-starter</artifactId>
               <version>${druid.spring.boot.starter.version}</version>
           </dependency>
           <dependency>
               <groupId>com.alibaba</groupId>
               <artifactId>druid</artifactId>
               <version>${druid.version}</version>
           </dependency>
           <!--mybatis和springboot整合-->
   <!--        <dependency>-->
   <!--            <groupId>org.mybatis.spring.boot</groupId>-->
   <!--            <artifactId>mybatis-spring-boot-starter</artifactId>-->
   <!--            <version>${mybatis.spring.boot.version}</version>-->
   <!--        </dependency>-->
           <dependency>
               <groupId>com.baomidou</groupId>
               <artifactId>mybatis-plus-boot-starter</artifactId>
               <version>${mybatis.plus.boot.starter.version}</version>
           </dependency>
           <!--通用基础配置junit/devtools/test/log4j/lombok/hutool-->
           <!--hutool-->
           <dependency>
               <groupId>cn.hutool</groupId>
               <artifactId>hutool-all</artifactId>
               <version>${hutool.version}</version>
           </dependency>
       </dependencies>
   ```

   测试过滤器

   ```java
   public class MyTest {
       @Test
       public void testGuavaWithBloomFilter() {
   // 创建布隆过滤器对象
           BloomFilter<Integer> filter = BloomFilter.create(Funnels.integerFunnel(), 100);
   // 判断指定元素是否存在
           System.out.println(filter.mightContain(1));
           System.out.println(filter.mightContain(2));
   // 将元素添加进布隆过滤器
           filter.put(1);
           filter.put(2);
           System.out.println(filter.mightContain(1));
           System.out.println(filter.mightContain(2));
       }
   }
   #######################################
   false
   false
   true
   true
   ```

   service

   ```java
   public abstract class GuavaBloomFilterService {
       /**
        * GuavaBloomFilter 初始化
        */
       protected abstract void guavaInit();
   
       /**
        * GuavaBloomFilter 清除
        */
       public abstract void guavaClear();
   
       /**
        * GuavaBloomFilter 重启
        */
       public void reload() {
           guavaClear();
           guavaInit();
       }
   
       /**
        * 测试GuavaBloomFilter 过滤数据
        * @param dataCount 数据数量（单位：w）
        */
       public abstract void testFilterData(Integer dataCount);
   
       /**
        * 测试指定数据是否存在
        * @param integer 测试数据
        * @return boolean
        */
   
       public abstract boolean guavaFilter(Integer integer);
   }
   
   ```

   impl

   ```java
   @Slf4j
   @Service
   public class GuavaBloomFilterServiceImpl extends GuavaBloomFilterService {
       public static final int _1W = 10000;
       //布隆过滤器里预计要插入多少数据
       public static int size = 100 * _1W;
       //误判率,它越小误判的个数也就越少(思考，是不是可以设置的无限小，没有误判岂不更好)
       //fpp the desired false positive probability
       public static double fpp = 0.03;
       // 构建布隆过滤器
       private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size,fpp);
   
       @Override
       protected void guavaInit() {
           //1 先往布隆过滤器里面插入100万的样本数据
           for (int i = 1; i <=size; i++) {
               bloomFilter.put(i);
           }
       }
   
       @Override
       public void guavaClear() {
           bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);
       }
   
       public boolean guavaFilter(Integer integer){
           return bloomFilter.mightContain(integer);
       }
   
       public void testFilterData(Integer dataCount){
           int count = 0;
           for (int i = 0; i < dataCount*_1W; i++) {
               boolean b = bloomFilter.mightContain(i);
               if(!b) {
                   count++;
                   log.warn("Guava data :{},is don`t exit ",i);
               }
           }
           System.out.println(dataCount*_1W+"数据中共："+count+"不存在！");
       }
   }
   ```

   > 添加过滤器拦截在redis之前减少缓存穿透!
   > ![image-20240218173343761](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218173343761.png)

   ![image-20240218173148986](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240218173148986.png)

4. 缓存击穿

   > 大量的请求同时查询一个key时，此时这个ky正好失效了，就会导致大量的请求都打到数据库上面去
   >
   > **处理方法：**
   >
   > 1. 差异失效时间，对于访问频繁的热点ky,干脆就不设置过期时间
   > 2. 互斥跟新，采用双检加锁策略

   

## Redis 分布式锁（手写）



## Redlock算法

