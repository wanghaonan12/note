Redis 高级

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

[canal官网](https://github.com/alibaba/canal/wiki/QuickStart)

### 安装mysql

1. 创建文件`my.cnf`在`/home/master/config`文件夹下

![image-20240219090851618](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219090851618.png)

2. 配置内容

   ```bash
   [mysqld]
   log-bin=mysql-bin #开启 binlog
   binlog-format=ROW #选择 ROW 模式
   server_id=1    #配置MySQL replaction需要定义，不要和canal的 slaveId重复
   ```

3. 安装mysql

   ```bash
   docker run -dp 3306:3306   
   -v /home/master/data:/var/lib/mysql   
   -v /home/master/config:/etc/mysql/conf.d   
   -v /home/master/log:/var/log/mysql   
   -e MYSQL_ROOT_PASSWORD=123456
   --name mysql 
   mysql:5.7.36 
   ```

4. 使用`SHOW VARIABLES LIKE 'log_bin';`检查 log_bin状态是否开启

   ![image-20240219091309285](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219091309285.png)

5. 创建canal使用的用户并赋予权限

   ```sql
   DROP USER IF EXISTS 'canal'@'%';
   CREATE USER 'canal'@'%' IDENTIFIED BY 'canal';  
   GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' IDENTIFIED BY 'canal';  
   FLUSH PRIVILEGES;
    
   SELECT * FROM mysql.user;
   ```

   ![image-20240219091819401](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219091819401.png)

### 安装canal

1. docker 安装canal

   ```bash
    docker run -p 11111:11111 --name canal -d canal/canal-server:latest
   ```

2. 复制`instance.properties`

   ```bash
   docker cp canal:/home/admin/canal-server/conf/example/instance.properties  /home/canal/
   ```

3. 修改`instance.properties`配置文件

   ```bash
   canal.instance.master.address=127.0.0.1:3306 #自己 mysql 的 ip:3306
   canal.instance.dbUsername=canal # 在mysql 中创建的用户
   canal.instance.dbPassword=canal # 在mysql 中创建的用户的密码
   ```

   

4. 删除 canal 容器 并重新创建拥有映射的容器

   ```bash
   docker rm -f canal
   
   docker run -dp 11111:11111 
   -v /home/canal/instance.properties:/home/admin/canal-server/conf/example/instance.properties 
   -v /home/canal/log:/home/admin/canal-server/logs 
   --name canal  
   canal/canal-server:latest
   ```

5.  查看启动状态

   > 查看canal中映射在宿主机的日志文件

   ![image-20240219095416303](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219095416303.png)

### 编写业务程序

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
    <guava.version>23.0</guava.version>
    <canal.client.version>1.1.0</canal.client.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.alibaba.otter</groupId>
        <artifactId>canal.client</artifactId>
        <version>${canal.client.version}</version>
    </dependency>
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
    url: jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf-8&useSSL=false
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
      nodes: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384
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

```

配置类

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

业务类

```java
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: WangHn
 * @Date: 2024/2/19 10:00
 * @Description: 基于canal 的redis数据同步
 */
@Component
public class RedisCanalClientExample {
    @Resource
    private RedisTemplate redisTemplate;
    private static final Integer _60SECONDS = 60;

    private static final String CANAL_IP = "43.138.25.182";
    private static final String MONITOR_DATABASE = "study.*";
    private static final int CANAL_PORT = 11111;


    private void redisInsert(List<Column> columns) {
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
            redisTemplate.opsForValue().set(columns.get(0).getValue(), jsonObject.toJSONString());
        }
    }


    private void redisDelete(List<Column> columns) {
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            jsonObject.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
            redisTemplate.delete(columns.get(0).getValue());
        }
    }

    private  void redisUpdate(List<Column> columns) {
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }
        if (columns.size() > 0) {
                redisTemplate.opsForValue().set(columns.get(0).getValue(), jsonObject.toJSONString());
                System.out.println("---------update after: " +  redisTemplate.opsForValue().get(columns.get(0).getValue()));
        }
    }

    private void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                //获取变更的row数据
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error,data:" + entry.toString(), e);
            }
            //获取变动类型
            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(), eventType));

            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.INSERT) {
                    redisInsert(rowData.getAfterColumnsList());
                } else if (eventType == EventType.DELETE) {
                    redisDelete(rowData.getBeforeColumnsList());
                } else {//EventType.UPDATE
                    redisUpdate(rowData.getAfterColumnsList());
                }
            }
        }
    }


    public void canalExecution() {
        System.out.println("---------O(∩_∩)O哈哈~ initCanal()-----------");
        // 创建链接canal服务端
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(CANAL_IP,
                CANAL_PORT), "example", "", "");
        int batchSize = 1000;
        //空闲空转计数器
        int emptyCount = 0;
        System.out.println("---------------------canal init OK，开始监听mysql变化------");
        try {
            connector.connect();
            //connector.subscribe(".*\\..*");
            connector.subscribe(MONITOR_DATABASE);
            connector.rollback();
            int totalEmptyCount = 10 * _60SECONDS;
            while (emptyCount < totalEmptyCount) {
                System.out.println("我是canal，每秒一次正在监听:" + UUID.randomUUID().toString());
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //计数器重新置零
                    emptyCount = 0;
                    printEntry(message.getEntries());
                }
                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
            System.out.println("已经监听了" + totalEmptyCount + "秒，无任何消息，请重启重试......");
        } finally {
            connector.disconnect();
        }
    }
}

```

测试类

```java
@SpringBootTest
class RedisCanalClientExampleTest {

    @Autowired
    private RedisCanalClientExample redisCanalClientExample;
    @Test
    void canalExecution() {
        redisCanalClientExample.canalExecution();
    }
}
```

![image-20240219104102447](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219104102447.png)

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

   实体类
   
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   @ApiModel(value = "聚划算活动producet信息")
   public class Product
   {
       //产品ID
       private Long id;
       //产品名称
       private String name;
       //产品价格
       private Integer price;
       //产品详情
       private String detail;
   }
   ```
   
   service
   
   ```java
   public interface JHSService {
   
       List<Product> getProductS(int page, int size);
   
       List<Product> getProductSAB(int page, int size);
   }
   
   ```
   
   实现类
   
   > 虽然 getProductSAB 和 getProductS 在测试的时候结果一样
   > 但是：在高并发情况下 getProductSAB 方法中更新数据时 可以保证 mysql 数据库不会受到大批量的命令减少缓存击穿的情况发生
   
   ```java
   @Service
   @Slf4j
   public class JHSServiceImpl implements JHSService {
   
   
       public  static final String JHS_KEY="jhs";
       public  static final String JHS_KEY_A="jhs:a";
       public  static final String JHS_KEY_B="jhs:b";
   
       @Autowired
       private RedisTemplate redisTemplate;
   
       /**
        * 偷个懒不加mybatis了，模拟从数据库读取100件特价商品，用于加载到聚划算的页面中
        * @return
        */
       private List<Product> getProductsFromMysql() {
           List<Product> list=new ArrayList<>();
           for (int i = 1; i <=20; i++) {
               Random rand = new Random();
               int id= rand.nextInt(10000);
               Product obj=new Product((long) id,"product"+i,i,"detail");
               list.add(obj);
           }
           return list;
       }
   
       @PostConstruct
       public void initJHS(){
           log.info("启动定时器淘宝聚划算功能模拟.........."+ DateUtil.now());
           new Thread(() -> {
               //模拟定时器一个后台任务，定时把数据库的特价商品，刷新到redis中
               while (true){
                   //模拟从数据库读取100件特价商品，用于加载到聚划算的页面中
                   List<Product> list=this.getProductsFromMysql();
                   //采用redis list数据结构的lpush来实现存储
                   this.redisTemplate.delete(JHS_KEY);
                   //lpush命令
                   this.redisTemplate.opsForList().leftPushAll(JHS_KEY,list);
                   //间隔一分钟 执行一遍，模拟聚划算每3天刷新一批次参加活动
                   try { TimeUnit.SECONDS.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
   
                   log.info("runJhs定时刷新..............");
               }
           },"t1").start();
       }
   
   
       @PostConstruct
       public void initJHSAB(){
           log.info("启动AB定时器计划任务淘宝聚划算功能模拟.........."+DateUtil.now());
           new Thread(() -> {
               //模拟定时器，定时把数据库的特价商品，刷新到redis中
               while (true){
                   //模拟从数据库读取100件特价商品，用于加载到聚划算的页面中
                   List<Product> list=this.getProductsFromMysql();
                   //先更新B缓存
                   this.redisTemplate.delete(JHS_KEY_B);
                   this.redisTemplate.opsForList().leftPushAll(JHS_KEY_B,list);
                   this.redisTemplate.expire(JHS_KEY_B,20L,TimeUnit.DAYS);
                   //再更新A缓存
                   this.redisTemplate.delete(JHS_KEY_A);
                   this.redisTemplate.opsForList().leftPushAll(JHS_KEY_A,list);
                   this.redisTemplate.expire(JHS_KEY_A,15L,TimeUnit.DAYS);
                   //间隔一分钟 执行一遍
                   try { TimeUnit.MINUTES.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
   
                   log.info("runJhs定时刷新双缓存AB两层..............");
               }
           },"t2").start();
       }
       
       @Override
       public List<Product> getProductS(int page, int size) {
           List<Product> list=null;
           long start = (page - 1) * size;
           long end = start + size - 1;
           try {
               //采用redis list数据结构的lrange命令实现分页查询
               list = this.redisTemplate.opsForList().range(JHS_KEY, start, end);
               if (CollectionUtils.isEmpty(list)) {
                   //TODO 走DB查询
               }
               log.info("查询结果：{}", list);
           } catch (Exception ex) {
               //这里的异常，一般是redis瘫痪 ，或 redis网络timeout
               log.error("exception:", ex);
               //TODO 走DB查询
           }
   
           return list;
       }
   
       @Override
       public List<Product> getProductSAB(int page, int size) {
           List<Product> list=null;
           long start = (page - 1) * size;
           long end = start + size - 1;
           try {
               //采用redis list数据结构的lrange命令实现分页查询
               list = this.redisTemplate.opsForList().range(JHS_KEY_A, start, end);
               if (CollectionUtils.isEmpty(list)) {
                   log.info("=========A缓存已经失效了，记得人工修补，B缓存自动延续5天");
                   //用户先查询缓存A(上面的代码)，如果缓存A查询不到（例如，更新缓存的时候删除了），再查询缓存B
                    list = this.redisTemplate.opsForList().range(JHS_KEY_B, start, end);
                    if(CollectionUtils.isEmpty(list)){
                       //TODO 走DB查询
                   }
               }
               log.info("查询结果：{}", list);
           } catch (Exception ex) {
               //这里的异常，一般是redis瘫痪 ，或 redis网络timeout
               log.error("exception:", ex);
               //TODO 走DB查询
           }
           return list;
       }
   }
   ```
   
   controller
   
   ```java
   @RestController
   @Slf4j
   @ApiModel(value = "JHS 互斥更新",description = "JHS 互斥更新")
   @RequestMapping("/jhs")
   public class JHSController
   {
   
       @Autowired
       private JHSServiceImpl jhsServiceImpl;
   
       @ApiOperation("getProductS 没有互斥")
       @PostMapping(value = "/getProductS/{page}/{size}")
       public List<Product> getProductS(@PathVariable  int page,@PathVariable int size)
       {
          return jhsServiceImpl.getProductS(page,size);
       }
   
   
       @ApiOperation("getProductS 互斥更新")
       @PostMapping(value = "/getProductSAB/{page}/{size}")
       public List<Product> getProductSAB(@PathVariable  int page, @PathVariable int size)
       {
          return jhsServiceImpl.getProductSAB(page,size);
       }
   
   }
   ```

## Redis 分布式锁（手写）

`DistributedLockFactory`工厂类创建锁

```java
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

```

`LockType` Lock类型枚举

```java
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

```

`RedisDistributedLock` 自定义分布式锁

> 独占性:
>
> 任何时刻只能有且仅有一个线程持有若redis集群环境下，不能因为某一个节点挂了而出现获取锁和释放锁失败的情况,**使用线程id加锁id拼接作为锁的key，保证所的唯一性**
> 高可用:
>
> 高并发请求下，依旧性能OK好使
> 防死锁:
>
> 杜绝死锁，必须有超时控制机制或者澈销操作，有个兜底终止跳出方案，**设置锁的过期时间，**
> 不乱抢:
>
> 防止张冠李戴，不能私下unlock别人的锁，只能自己加锁自己释放，自己约的锁含着泪也要自己解 ，**根据线程id为名称如果不是自己线程下的锁则无法进行解锁**
> 重入性:
>
> 同一个节点的同一个线程如果获得锁之后，它也可以再次获取这个锁。**使用hash结构存储 ，每次重入都会进行加1操作，解锁则反过来,保证重入性**
>
> **使用 lua 语法保证操作的原子性，设置自动续期防止超时**

```java
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
```



```java
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private DistributedLockFactory distributedLockFactory;

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
}

```

```java
@RestController
@Slf4j
@ApiModel(value = "redis lock",description = "redis lock")
@RequestMapping("/redisLock")
public class RedisLockController
{

    @Autowired
    private InventoryServiceImpl inventoryServiceImpl;

    @ApiOperation("自研")
    @PostMapping(value = "/selfResearch")
    public String selfResearch()
    {return inventoryServiceImpl.selfLock();}

}
```



Jmeter压测 自定义 redisLock 1000个商品

![image-20240219152241785](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219152241785.png)

## Redission 锁

### 单机案例

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
        <canal.client.version>1.1.0</canal.client.version>
        <redission.version>3.19.1</redission.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.redisson</groupId>
            <artifactId>redisson</artifactId>
            <version>${redission.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.otter</groupId>
            <artifactId>canal.client</artifactId>
            <version>${canal.client.version}</version>
        </dependency>
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

`CacheConfiguration`配置类将`Redisson`放到容器中管理

```java
@Configuration
public class CacheConfiguration {
    /**
     * 单机
     * @return
     */
    @Bean
    public Redisson redisson()
    {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6385").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }

}
```

`InventoryServiceImpl` 实现类

> 使用`Redisson` 比之前自定义的redisLock省去不少事情，只需要配置一下就可以使用了

```java
@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String port;

    @Autowired
    private Redisson redisson;

    @Override
    public String redLock() {
        String retMessage = "";
        RLock redissonLock = redisson.getLock("zzyyRedisLock");
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
}

```

```java
@RestController
@Slf4j
@ApiModel(value = "redis lock",description = "redis lock")
@RequestMapping("/redisLock")
public class RedisLockController
{

    @Autowired
    private InventoryServiceImpl inventoryServiceImpl;

    @ApiOperation("自研")
    @PostMapping(value = "/selfResearch")
    public String selfResearch()
    {return inventoryServiceImpl.selfLock();}

    @ApiOperation("redLock")
    @PostMapping(value = "/redLock")
    public String redLock()
    {return inventoryServiceImpl.redLock();}

}
```

压测redlock没有出现超卖

![image-20240219151844383](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219151844383.png)

### 多机案例

> 经过之前的手写`RedisLock`和使用`Redission`发现使用以上两种方法时当redis宕机时就无法获取锁，使用集群也可能在没有同步之前 master 主机宕掉了 容错率较低，接下来就出现了 redlock ，他有点类似集群不过这些`redis` 机器都作为主机且没有从属关系，且只有所有机器的信息写入的时候才会返回数据加入成功，这样就不用担心redis宕机导致的无法获取锁的情况。

![image-20240219161359313](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240219161359313.png)

> 线程 1 首先获取锁成功，将键值对写入 redis 的 master 节点，在 redis 将该键值对同步到 slave 节点之前，master 发生了故障；redis 触发故障转移，其中一个 slave 升级为新的 master，此时新上位的master并不包含线程1写入的键值对，因此线程 2 尝试获取锁也可以成功拿到锁，此时相当于有两个线程获取到了锁，可能会导致各种预期之外的情况发生，例如最常见的脏数据。 我们加的是排它独占锁，同一时间只能有一个建redis锁成功并持有锁，严禁出现2个以上的请求线程拿到锁。危险的

[官网分布式锁介绍](https://redis.io/docs/manual/patterns/distributed-locks/)

该方案也是基于（set 加锁、Lua 脚本解锁）进行改良的，所以redis之父antirez 只描述了差异的地方，大致方案如下。假设我们有N个Redis主节点，例如 N = 5这些节点是完全独立的，我们不使用复制或任何其他隐式协调系统，

为了取到锁客户端执行以下操作：

| 1    | 获取当前时间，以毫秒为单位；                                 |
| ---- | ------------------------------------------------------------ |
| 2    | 依次尝试从5个实例，使用相同的 key 和随机值（例如 UUID）获取锁。当向Redis 请求获取锁时，客户端应该设置一个超时时间，这个超时时间应该小于锁的失效时间。例如你的锁自动失效时间为 10 秒，则超时时间应该在 5-50 毫秒之间。这样可以防止客户端在试图与一个宕机的 Redis 节点对话时长时间处于阻塞状态。如果一个实例不可用，客户端应该尽快尝试去另外一个 Redis 实例请求获取锁； |
| 3    | 客户端通过当前时间减去步骤 1 记录的时间来计算获取锁使用的时间。当且仅当从大多数（N/2+1，这里是 3 个节点）的 Redis 节点都取到锁，并且获取锁使用的时间小于锁失效时间时，锁才算获取成功； |
| 4    | 如果取到了锁，其真正有效时间等于初始有效时间减去获取锁所使用的时间（步骤 3 计算的结果）。 |
| 5    | 如果由于某些原因未能获得锁（无法在至少 N/2 + 1 个 Redis 实例获取锁、或获取锁的时间超过了有效时间），客户端应该在所有的 Redis 实例上进行解锁（即便某些Redis实例根本就没有加锁成功，防止某些节点获取到锁但是客户端没有得到响应而导致接下来的一段时间不能被重新获取锁）。 |

该方案为了解决数据不一致的问题，直接舍弃了异步复制只使用 master 节点，同时由于舍弃了 slave，为了保证可用性，引入了 N 个节点，官方建议是 5。阳哥本次教学演示用3台实例来做说明。

客户端只有在满足下面的这两个条件时，才能认为是加锁成功。

条件1：客户端从超过半数（大于等于N/2+1）的Redis实例上成功获取到了锁；

条件2：客户端获取锁的总耗时没有超过锁的有效时间。

**redis 部署的台数计算**

1. 先知道什么是容错

    失败了多少个机器实例后我还是可以容忍的，所谓的容忍就是数据一致性还是可以Ok的，CP数据一致性还是可以满足

    加入在集群环境中，redis失败1台，可接受。2X+1 = 2 * 1+1 =3，部署3台，死了1个剩下2个可以正常工作，那就部署3台。

    加入在集群环境中，redis失败2台，可接受。2X+1 = 2 * 2+1 =5，部署5台，死了2个剩下3个可以正常工作，那就部署5台。

2. 为什么是奇数？

    加入在集群环境中，redis失败1台，可接受。2N+2= 2 * 1+2 =4，部署4台 经过计算4台和3台的效果一样

    从而达到**最少的机器，最多的产出效果**

**实战**

配置 `RedissonClient`

```java
@Data
public class RedisSingleProperties {
    private  String address1;
    private  String address2;
    private  String address3;
}
```

```java
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
```

```java
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
```

```java
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class CacheConfiguration {

    private final String PASSWORD = "134679";

    @Resource
    private RedisProperties redisProperties;

//    @Bean
    RedissonClient redissonClient1() {
        Config config = new Config();
        String node = redisProperties.getSingle().getAddress1();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(redisProperties.getPool().getConnTimeout())
                .setConnectionPoolSize(redisProperties.getPool().getSize())
                .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
        serverConfig.setPassword("111111");
        return Redisson.create(config);
    }

//    @Bean
    RedissonClient redissonClient2() {
        Config config = new Config();
        String node = redisProperties.getSingle().getAddress2();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(redisProperties.getPool().getConnTimeout())
                .setConnectionPoolSize(redisProperties.getPool().getSize())
                .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
        serverConfig.setPassword(PASSWORD);
        return Redisson.create(config);
    }

//    @Bean
    RedissonClient redissonClient3() {
        Config config = new Config();
        String node = redisProperties.getSingle().getAddress3();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(redisProperties.getPool().getConnTimeout())
                .setConnectionPoolSize(redisProperties.getPool().getSize())
                .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
            serverConfig.setPassword(PASSWORD);
        return Redisson.create(config);
    }


    /**
     * 单机
     * @return
     */
    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://43.138.25.182:6385").setDatabase(0);
        config.useSingleServer().setPassword(PASSWORD);
        return (Redisson) Redisson.create(config);
    }

}
```

yml

```yml
server:
  port: 1347
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
    pool:
      connTimeout: 3000
      size: 10
      soTimeout: 3000
    single:
      address1: 43.138.25.182:6385
      address2: 43.138.25.182:6386
      address3: 43.138.25.182:6387
    mode: single
    timeout: 3000

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

serviceImpl

```java
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
```

controller

```java
    @ApiOperation("multiLock")
    @PostMapping(value = "/multiLock")
    public String multiLock()
    {return inventoryServiceImpl.multiLock();}

```

使用jmeter测试

![image-20240220163202633](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240220163202633.png)
