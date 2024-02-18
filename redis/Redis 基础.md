Redis 基础

## 简介

> Redis **（Remote Dictionary Server）**(远程字典服务)是完全开源的，使用ANSIC语言编写遵守BSD协议，是一个高性能的Key-Value数据库提供了丰富的数据结构，例如**String**、**Hash**、**List**、**Set**、**SortedSet**等等。数据是存在**内存**中的，同时Redis**支持事务**、**持久化**、**LUA脚本**、**发布/订阅**、**缓存淘汰**、流技术等多种功能特性提供了主从模式、Redis Sentinel和Redis Cluster集群架构方案

![image-20240115092113783](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115092113783.png)

**常用地址**

1. 官网：[Redis](https://redis.io/)
2. 中文官网：[CRUG网站 (redis.cn)](http://www.redis.cn/)
3. 中文学习网：[redis中文文档](https://redis.com.cn/documentation.html)
4. Redis 在线测试：[Try Redis](https://try.redis.io/)
5. 指令参考： [Redis 命令参考 — Redis 命令参考 (redisfans.com)](http://doc.redisfans.com/)

**Redis之父安特雷兹**:

1. 个人博客：http://antirez.com/latest/0
2. GitHub：https://github.com/antirez

### 优点

- 性能极高-Redisi能**读的速度是110000次/秒**，**写的速度是81000次/秒**
- Redis数据类型丰富，不仅仅支持简单的key-value类型的数据，同时还提供ist,set,Zset,hash等数据结构的存储
- Redis支持数据的持久化，可以将内存中的数据保持在滋盘中，重启的时候可以再次加载进行使用
- Redisi支持数据的备份，即master-slave模式的数据备份

### 主流使用功能

![image-20240115093437240](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115093437240.png)

1. **分布式缓存，挡在mysql数据库前的带刀护卫**

   > 传统数据库关系(mysql)
   >
   > Redis是key-value数据库(NoSQL一种)，mysql是关系数据库
   >
   > Redis数据操作主要在内存，而mysql主要存储在磁盘
   >
   > Redis在某一些场景使用中要明显优于mysql，比如计数器、排行榜等方面
   >
   > Redis通常用于一些特定场景，需要与Mysql一起配合使用
   >
   > 两者并不是相互替换和竞争关系，而是共用和配合使用

   ![image-20240115092741053](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115092741053.png)

2. **内存存储和持久化(RDB+AOF) redis支持异步将内存中的数据写到硬盘上，同时不影响继续服务**

3. **高可用架构搭配**

   - 单机模式
   - 主从模式
   - 哨兵模式
   - 集群模式

4. **缓存穿透、击穿、雪崩**

5. **分布式锁**

6. **队列**

7. 常用小功能：

   - 排行榜
   - 点赞
   - 购物车
   - ....

### 版本命名规则

Redis从发布至今，已经有十余年的时光了，一直遵循着自己的命名规则：

版本号第二位如果是**奇数**，则为**非稳定版本** 如2.7、2.9、3.1

版本号第二位如果是**偶数**，则为**稳定版本** 如2.6、2.8、3.0、3.2

当前奇数版本就是下一个稳定版本的开发版本，如2.9版本是3.0版本的开发版本

我们可以通过redis.io官网来下载自己感兴趣的版本进行源码阅读：

历史发布版本的源码：https://download.redis.io/releases/

![image-20240115094816008](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115094816008.png)

## Redis 安装/卸载

### 安装

docker 普通安装

```bash
docker run --restart=always \
-p 6379:6379 \
--name myredis \
-v /home/redis/redis.conf:/etc/redis/redis.conf \
-v /home/redis/data:/data \
-d redis:7.0.12 redis-server /etc/redis/redis.conf
```

redis在容器中是没有配置文件的，需要自己在映射的位置创建填写

[redis 官网配置文件](https://redis.io/docs/management/config/)

docker 集群安装有手就行，请看[docker专栏](https://blog.csdn.net/weixin_52359996/article/details/134144493)

> (基础默认配置安装)

1. 下载获得redis-7.0.0.tar.gz后将它放入我们的Linux目录/opt

   ```bash
   wget https://download.redis.io/releases/redis-7.0.0.tar.gz
   ```

2. /opt目录下解压redis

   ```bash
   tar -zxvf redis-7.0.0.tar.gz
   ```

3. 进入目录

   ```bash
   cd redis-7.0.0
   ```

4. 在redis-7.0.0目录下执行make命令

   ```bash
   make && make install
   ```

5. 查看默认安装目录：usr/local/bin

   **redis-benchmark:**性能测试工具，服务启动后运行该命令，看看自己本子性能如何

   **redis-check-aof:**修复有问题的AOF文件，rdb和aof后面讲

   **redis-check-dump:**修复有问题的dump.rdb文件

   **redis-cli:**客户端，操作入口

   **redis-sentinel:**redis集群使用

   **redis-server:**Redis服务器启动命令

   ![image-20240115113925324](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115113925324.png)

6. helloword测试

   1. 启动

      ```bash
      redis-server
      ```

      ![image-20240115114448646](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115114448646.png)

   2. 切换页面测试

   ```BASH
   redis-cli
   ```

   ![image-20240115114531096](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115114531096.png)

### 卸载

1. 停止redis-server 服务
2. 删除/usr/local/lib目录下与redis相关的文件

## Redis 操作

> 使用 help 可以很好的帮助我们使用 redis

```bash
help @string
help @list
...
```

```bash
SET key value [NX | XX] [GET] [EX seconds | PX milliseconds |
  EXAT unix-time-seconds | PXAT unix-time-milliseconds | KEEPTTL]
```

- `EX` – *seconds* – Set the specified expire time, in seconds. (*seconds* – 设置键key的过期时间，单位时秒)
- `PX` – *milliseconds* – Set the specified expire time, in milliseconds. (*milliseconds* – 设置键key的过期时间，单位时毫秒)
- `NX` – Only set the key if it does not already exist. (只有键key不存在的时候才会设置key的值)
- `XX` – Only set the key if it already exist. (只有键key存在的时候才会设置key的值)
- `EXAT`– *timestamp-seconds* -- Set the specified Unix time at which the key will expire, in seconds (a positive integer)时间戳-秒——设置密钥到期的指定Unix时间，单位为秒(一个正整数)。
- `PXAT` –*timestamp-milliseconds* -- Set the specified Unix time at which the key will expire, in milliseconds (a positive integer).(时间戳-毫秒——设置密钥到期的指定Unix时间，单位为毫秒(正整数)。
- `KEEPTTL`– Retain the time to live associated with the key.（保留与该键关联的生存时间。）

### key

| 命令                                                      | 描述                                                  |
| :-------------------------------------------------------- | :---------------------------------------------------- |
| [DEL](https://redis.com.cn/commands/del.html)             | 用于删除 key                                          |
| [DUMP](https://redis.com.cn/commands/dump.html)           | 序列化给定 key ，并返回被序列化的值                   |
| [EXISTS](https://redis.com.cn/commands/exists.html)       | 检查给定 key 是否存在                                 |
| [EXPIRE](https://redis.com.cn/commands/expire.html)       | 为给定 key 设置过期时间                               |
| [EXPIREAT](https://redis.com.cn/commands/expireat.html)   | 用于为 key 设置过期时间，接受的时间参数是 UNIX 时间戳 |
| [PEXPIRE](https://redis.com.cn/commands/pexpire.html)     | 设置 key 的过期时间，以毫秒计                         |
| [PEXPIREAT](https://redis.com.cn/commands/pexpireat.html) | 设置 key 过期时间的时间戳(unix timestamp)，以毫秒计   |
| [KEYS](https://redis.com.cn/commands/keys.html)           | 查找所有符合给定模式的 key                            |
| [MOVE](https://redis.com.cn/commands/move.html)           | 将当前数据库的 key 移动到给定的数据库中               |
| [PERSIST](https://redis.com.cn/commands/persist.html)     | 移除 key 的过期时间，key 将持久保持                   |
| [PTTL](https://redis.com.cn/commands/pttl.html)           | 以毫秒为单位返回 key 的剩余的过期时间                 |
| [TTL](https://redis.com.cn/commands/ttl.html)             | 以秒为单位，返回给定 key 的剩余生存时间(              |
| [RANDOMKEY](https://redis.com.cn/commands/randomkey.html) | 从当前数据库中随机返回一个 key                        |
| [RENAME](https://redis.com.cn/commands/rename.html)       | 修改 key 的名称                                       |
| [RENAMENX](https://redis.com.cn/commands/renamenx.html)   | 仅当 newkey 不存在时，将 key 改名为 newkey            |
| [TYPE](https://redis.com.cn/commands/type.html)           | 返回 key 所储存的值的类型                             |
| * **FLUSHDB**                                             | **删除当前数据库中的所有键**                          |
| * **FLUSHALL**                                            | **删除所有数据库中的所有键**                          |

## 数据类型

### (字符串)String

> String 是 Redis 最基本的类型，一个 key 对应一个 value。
>
> String 类型是二进制安全的，意思是 Redis 的 String 可以包含任何数据，比如 Jpg 图片或者序列化的对象 。 
>
> String 类型是 Redis 最基本的数据类型，一个 Redis 中字符串value 最多可以是512M

String 命令：[Redis命令中心（Redis commands） -- Redis中国用户组（CRUG）](http://www.redis.cn/commands.html#string)

**常用命令（菜鸟地址教程）**

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [SET key value](https://www.runoob.com/redis/strings-set.html) 设置指定 key 的值。 |
| 2    | [GET key](https://www.runoob.com/redis/strings-get.html) 获取指定 key 的值。 |
| 3    | [GETRANGE key start end](https://www.runoob.com/redis/strings-getrange.html) 返回 key 中字符串值的子字符 |
| 4    | [GETSET key value](https://www.runoob.com/redis/strings-getset.html) 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。 |
| 5    | [GETBIT key offset](https://www.runoob.com/redis/strings-getbit.html) 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。 |
| 6    | [MGET key1 key2..](https://www.runoob.com/redis/strings-mget.html) 获取所有(一个或多个)给定 key 的值。 |
| 7    | [SETBIT key offset value](https://www.runoob.com/redis/strings-setbit.html) 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。 |
| 8    | [SETEX key seconds value](https://www.runoob.com/redis/strings-setex.html) 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。 |
| 9    | [SETNX key value](https://www.runoob.com/redis/strings-setnx.html) 只有在 key 不存在时设置 key 的值。 |
| 10   | [SETRANGE key offset value](https://www.runoob.com/redis/strings-setrange.html) 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。 |
| 11   | [STRLEN key](https://www.runoob.com/redis/strings-strlen.html) 返回 key 所储存的字符串值的长度。 |
| 12   | [MSET key value key value ...](https://www.runoob.com/redis/strings-mset.html) 同时设置一个或多个 key-value 对。 |
| 13   | [MSETNX key value key value ...](https://www.runoob.com/redis/strings-msetnx.html) 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 |
| 14   | [PSETEX key milliseconds value](https://www.runoob.com/redis/strings-psetex.html) 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。 |
| 15   | [INCR key](https://www.runoob.com/redis/strings-incr.html) 将 key 中储存的数字值增一。 |
| 16   | [INCRBY key increment](https://www.runoob.com/redis/strings-incrby.html) 将 key 所储存的值加上给定的增量值（increment） 。 |
| 17   | [INCRBYFLOAT key increment](https://www.runoob.com/redis/strings-incrbyfloat.html) 将 key 所储存的值加上给定的浮点增量值（increment） 。 |
| 18   | [DECR key](https://www.runoob.com/redis/strings-decr.html) 将 key 中储存的数字值减一。 |
| 19   | [DECRBY key decrement](https://www.runoob.com/redis/strings-decrby.html) key 所储存的值减去给定的减量值（decrement） 。 |
| 20   | [APPEND key value](https://www.runoob.com/redis/strings-append.html) 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。 |

用法：分布式锁，过期 token ，数据库

### (列表)List

> Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）
>
> 它的底层实际是个双端链表，最多可以包含 2^32 - 1 个元素 (4294967295, 每个列表超过40亿个元素)

**常用指令（菜鸟教程地址）**

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [BLPOP key1 key2  timeout](https://www.runoob.com/redis/lists-blpop.html) 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 |
| 2    | [BRPOP key1 key2  timeout](https://www.runoob.com/redis/lists-brpop.html) 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 |
| 3    | [BRPOPLPUSH source destination timeout](https://www.runoob.com/redis/lists-brpoplpush.html) 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。 |
| 4    | [LINDEX key index](https://www.runoob.com/redis/lists-lindex.html) 通过索引获取列表中的元素 |
| 5    | [LINSERT key BEFORE\|AFTER pivot value](https://www.runoob.com/redis/lists-linsert.html) 在列表的元素前或者后插入元素 |
| 6    | [LLEN key](https://www.runoob.com/redis/lists-llen.html) 获取列表长度 |
| 7    | [LPOP key](https://www.runoob.com/redis/lists-lpop.html) 移出并获取列表的第一个元素 |
| 8    | [LPUSH key value1 value2](https://www.runoob.com/redis/lists-lpush.html) 将一个或多个值插入到列表头部 |
| 9    | [LPUSHX key value](https://www.runoob.com/redis/lists-lpushx.html) 将一个值插入到已存在的列表头部 |
| 10   | [LRANGE key start stop](https://www.runoob.com/redis/lists-lrange.html) 获取列表指定范围内的元素 |
| 11   | [LREM key count value](https://www.runoob.com/redis/lists-lrem.html) 移除列表元素 |
| 12   | [LSET key index value](https://www.runoob.com/redis/lists-lset.html) 通过索引设置列表元素的值 |
| 13   | [LTRIM key start stop](https://www.runoob.com/redis/lists-ltrim.html) 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。 |
| 14   | [RPOP key](https://www.runoob.com/redis/lists-rpop.html) 移除列表的最后一个元素，返回值为移除的元素。 |
| 15   | [RPOPLPUSH source destination](https://www.runoob.com/redis/lists-rpoplpush.html) 移除列表的最后一个元素，并将该元素添加到另一个列表并返回 |
| 16   | [RPUSH key value1 value2](https://www.runoob.com/redis/lists-rpush.html) 在列表中添加一个或多个值到列表尾部 |
| 17   | [RPUSHX key value](https://www.runoob.com/redis/lists-rpushx.html) 为已存在的列表添加值 |

### (哈希表)Hash

> Redis hash 是一个 String 类型的 field（字段） 和 value（值） 的映射表，hash 特别适合用于存储对象。
>
> Redis 中每个 hash 可以存储 2^32 - 1 键值对（40多亿）

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [HSET key field value](https://www.runoob.com/redis/hashes-hset.html) 将哈希表 key 中的字段 field 的值设为 value 。 |
| 2    | [HEXISTS key field](https://www.runoob.com/redis/hashes-hexists.html) 查看哈希表 key 中，指定的字段是否存在。 |
| 3    | [HGET key field](https://www.runoob.com/redis/hashes-hget.html) 获取存储在哈希表中指定字段的值。 |
| 4    | [HGETALL key](https://www.runoob.com/redis/hashes-hgetall.html) 获取在哈希表中指定 key 的所有字段和值 |
| 5    | [HINCRBY key field increment](https://www.runoob.com/redis/hashes-hincrby.html) 为哈希表 key 中的指定字段的整数值加上增量 increment 。 |
| 6    | [HINCRBYFLOAT key field increment](https://www.runoob.com/redis/hashes-hincrbyfloat.html) 为哈希表 key 中的指定字段的浮点数值加上增量 increment 。 |
| 7    | [HKEYS key](https://www.runoob.com/redis/hashes-hkeys.html) 获取哈希表中的所有字段 |
| 8    | [HLEN key](https://www.runoob.com/redis/hashes-hlen.html) 获取哈希表中字段的数量 |
| 9    | [HMGET key field1 field2](https://www.runoob.com/redis/hashes-hmget.html) 获取所有给定字段的值 |
| 10   | [HMSET key field1 value1 field2 value2 ](https://www.runoob.com/redis/hashes-hmset.html) 同时将多个 field-value (域-值)对设置到哈希表 key 中。 |
| 11   | [HSETNX key field value](https://www.runoob.com/redis/hashes-hsetnx.html) 只有在字段 field 不存在时，设置哈希表字段的值。 |
| 12   | [HDEL key field1 field2](https://www.runoob.com/redis/hashes-hdel.html) 删除一个或多个哈希表字段 |
| 13   | [HVALS key](https://www.runoob.com/redis/hashes-hvals.html) 获取哈希表中所有值。 |
| 14   | [HSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/hashes-hscan.html) 迭代哈希表中的键值对。 |

用法：购物车

### (集合)Set

> Redis 的 Set 是 String 类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据，集合对象的编码可以是 intset 或者 hashtable。
>
> Redis 中 Set 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。
>
> 集合中最大的成员数为 2^32 - 1 (4294967295, 每个集合可存储40多亿个成员)

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [SADD key member1 member2](https://www.runoob.com/redis/sets-sadd.html) 向集合添加一个或多个成员 |
| 2    | [SCARD key](https://www.runoob.com/redis/sets-scard.html) 获取集合的成员数 |
| 3    | [SDIFF key1 key2](https://www.runoob.com/redis/sets-sdiff.html) 返回第一个集合与其他集合之间的差异。 |
| 4    | [SDIFFSTORE destination key1 key2](https://www.runoob.com/redis/sets-sdiffstore.html) 返回给定所有集合的差集并存储在 destination 中 |
| 5    | [SINTER key1 key2](https://www.runoob.com/redis/sets-sinter.html) 返回给定所有集合的交集 |
| 6    | [SINTERSTORE destination key1 key2](https://www.runoob.com/redis/sets-sinterstore.html) 返回给定所有集合的交集并存储在 destination 中 |
| 7    | [SISMEMBER key member](https://www.runoob.com/redis/sets-sismember.html) 判断 member 元素是否是集合 key 的成员 |
| 8    | [SMEMBERS key](https://www.runoob.com/redis/sets-smembers.html) 返回集合中的所有成员 |
| 9    | [SMOVE source destination member](https://www.runoob.com/redis/sets-smove.html) 将 member 元素从 source 集合移动到 destination 集合 |
| 10   | [SPOP key](https://www.runoob.com/redis/sets-spop.html) 移除并返回集合中的一个随机元素 |
| 11   | [SRANDMEMBER key count](https://www.runoob.com/redis/sets-srandmember.html) 返回集合中一个或多个随机数 |
| 12   | [SREM key member1 member2](https://www.runoob.com/redis/sets-srem.html) 移除集合中一个或多个成员 |
| 13   | [SUNION key1 key2](https://www.runoob.com/redis/sets-sunion.html) 返回所有给定集合的并集 |
| 14   | [SUNIONSTORE destination key1 key2](https://www.runoob.com/redis/sets-sunionstore.html) 所有给定集合的并集存储在 destination 集合中 |
| 15   | [SSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/sets-sscan.html) 迭代集合中的元素 |

### (有序集合)Zset

> Redis zset 和 set 一样也是 String 类型元素的集合,且不允许重复的成员。
>
> 不同的是每个元素都会关联一个 double 类型的分数，redis正是通过分数来为集合中的成员进行从小到大的排序。
>
> Zset 的成员是唯一的,但分数(score)却可以重复。
>
> Zset 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是 O(1)。 集合中最大的成员数为 2^32 - 1

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [ZADD key score1 member1 score2 member2](https://www.runoob.com/redis/sorted-sets-zadd.html) 向有序集合添加一个或多个成员，或者更新已存在成员的分数 |
| 2    | [ZCARD key](https://www.runoob.com/redis/sorted-sets-zcard.html) 获取有序集合的成员数 |
| 3    | [ZCOUNT key min max](https://www.runoob.com/redis/sorted-sets-zcount.html) 计算在有序集合中指定区间分数的成员数 |
| 4    | [ZINCRBY key increment member](https://www.runoob.com/redis/sorted-sets-zincrby.html) 有序集合中对指定成员的分数加上增量 increment |
| 5    | [ZINTERSTORE destination numkeys key key ...](https://www.runoob.com/redis/sorted-sets-zinterstore.html) 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 destination 中 |
| 6    | [ZLEXCOUNT key min max](https://www.runoob.com/redis/sorted-sets-zlexcount.html) 在有序集合中计算指定字典区间内成员数量 |
| 7    | [ZRANGE key start stop WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrange.html) 通过索引区间返回有序集合指定区间内的成员 |
| 8    | [ZRANGEBYLEX key min max LIMIT offset count](https://www.runoob.com/redis/sorted-sets-zrangebylex.html) 通过字典区间返回有序集合的成员 |
| 9    | [ZRANGEBYSCORE key min max [WITHSCORES\] [LIMIT]](https://www.runoob.com/redis/sorted-sets-zrangebyscore.html) 通过分数返回有序集合指定区间内的成员 |
| 10   | [ZRANK key member](https://www.runoob.com/redis/sorted-sets-zrank.html) 返回有序集合中指定成员的索引 |
| 11   | [ZREM key member member ...](https://www.runoob.com/redis/sorted-sets-zrem.html) 移除有序集合中的一个或多个成员 |
| 12   | [ZREMRANGEBYLEX key min max](https://www.runoob.com/redis/sorted-sets-zremrangebylex.html) 移除有序集合中给定的字典区间的所有成员 |
| 13   | [ZREMRANGEBYRANK key start stop](https://www.runoob.com/redis/sorted-sets-zremrangebyrank.html) 移除有序集合中给定的排名区间的所有成员 |
| 14   | [ZREMRANGEBYSCORE key min max](https://www.runoob.com/redis/sorted-sets-zremrangebyscore.html) 移除有序集合中给定的分数区间的所有成员 |
| 15   | [ZREVRANGE key start stop WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrevrange.html) 返回有序集中指定区间内的成员，通过索引，分数从高到低 |
| 16   | [ZREVRANGEBYSCORE key max min WITHSCORES](https://www.runoob.com/redis/sorted-sets-zrevrangebyscore.html) 返回有序集中指定分数区间内的成员，分数从高到低排序 |
| 17   | [ZREVRANK key member](https://www.runoob.com/redis/sorted-sets-zrevrank.html) 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序 |
| 18   | [ZSCORE key member](https://www.runoob.com/redis/sorted-sets-zscore.html) 返回有序集中，成员的分数值 |
| 19   | [ZUNIONSTORE destination numkeys key key ...](https://www.runoob.com/redis/sorted-sets-zunionstore.html) 计算给定的一个或多个有序集的并集，并存储在新的 key 中 |
| 20   | [ZSCAN key cursor [MATCH pattern\] [COUNT count]](https://www.runoob.com/redis/sorted-sets-zscan.html) 迭代有序集合中的元素（包括元素成员和元素分值） |

作用：点赞列表

### (地理空间)GEO

> Redis GEO 主要用于存储地理位置信息，并对存储的信息进行操作，包括、**添加地理位置的坐标**、**获取地理位置的坐标**、**计算两个位置之间的距离**、根据用户给定的经纬度坐标来获取指定范围内的地理位置集合

| 序号 | 命令及描述                                                   |
| ---- | ------------------------------------------------------------ |
| 1    | [GEOADD](https://redis.io/commands/geoadd/):将指定的地理空间项（经度、纬度、名称）添加到指定的键中。数据以排序集的形式存储到键中，这样就可以使用 [`GEOSEARCH`](https://redis.io/commands/geosearch) 命令查询项目。  有效经度为 -180 到 180 度。 有效纬度为 -85.05112878 至 85.05112878 度。 |
| 2    | [GEODIST](https://redis.io/commands/geodist/): 给定一个表示地理空间索引的排序集，该命令使用 [`GEOADD`](https://redis.io/commands/geoadd) 命令填充，该命令将返回指定单位中两个指定成员之间的距离。 |
| 3    | [GEOHASH](https://redis.io/commands/geohash/):返回有效的 [Geohash](https://en.wikipedia.org/wiki/Geohash) 字符串，该字符串表示一个或多个元素在表示地理空间索引的排序集值中的位置（其中元素是使用 [`GEOADD`](https://redis.io/commands/geoadd) 添加的）。 |
| 4    | [GEOPOS](https://redis.io/commands/geopos/):返回由排序集设置键表示的地理空间索引的所有指定成员的位置（经度、纬度）。 |
| 5    | [GEORADIUS(已弃用)](https://redis.io/commands/georadius/):使用 [`GEOADD`](https://redis.io/commands/geoadd) 返回填充了地理空间信息的排序集的成员，这些成员位于使用中心位置和距中心的最大距离（半径）指定的区域的边界内。 |
| 6    | [GEORADIUS_RO](https://redis.io/commands/georadius_ro/):此命令与 [`GEORADIUS`](https://redis.io/commands/georadius) 命令相同，只是它不支持可选的 和 参数。`STORE``STOREDIST` |
| 7    | [GEORADIUSBYMEMBER](https://redis.io/commands/georadiusbymember/):此命令与 [`GEORADIUS`](https://redis.io/commands/georadius) 完全相同，唯一的区别是 将经度和纬度值作为要查询的区域的中心，它采用已存在于已排序集表示的地理空间索引中的成员的名称。 |
| 8    | [GEORADIUSBYMEMBER_RO](https://redis.io/commands/georadiusbymember_ro/):此命令与 [`GEORADIUSBYMEMBER`](https://redis.io/commands/georadiusbymember) 命令相同，只是它不支持可选的 和 参数。`STORE``STOREDIST` |
| 9    | [GEOSEARCH](https://redis.io/commands/geosearch/):使用 [`GEOADD`](https://redis.io/commands/geoadd) 返回填充了地理空间信息的排序集的成员，这些成员位于给定形状指定的区域的边界内。此命令扩展了 [`GEORADIUS`](https://redis.io/commands/georadius) 命令，因此除了在圆形区域内搜索外，它还支持在矩形区域内进行搜索。应使用此命令来代替已弃用的 GEORADIUS 和 [`GEORADIUSBYMEMBER`](https://redis.io/commands/georadiusbymember) 命令。 |
| 10   | [GEOSEARCHSTORE](https://redis.io/commands/geosearchstore/):此命令类似于 [`GEOSEARCH`](https://redis.io/commands/geosearch)，但将结果存储在目标键中。此命令替换现已弃用的 GEORADIUS 和 [`GEORADIUSBYMEMBER。`](https://redis.io/commands/georadiusbymember)默认情况下，它将结果及其地理空间信息存储在排序集中。`destination`使用该选项时，该命令将项目存储在一个排序集中，其中填充了它们与圆或框中心的距离，作为浮点数，采用为该形状指定的相同单位。`STOREDIST` |

用法：地图信息，计算距离

### (基数统计)HyperLogLog

> HyperLogLog  是用来做基数统计的算法，HyperLogLog  的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定且是很小的。
>
> 在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基 数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。
>
> 但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [PFADD key element element ...](https://www.runoob.com/redis/hyperloglog-pfadd.html) 添加指定元素到 HyperLogLog 中。 |
| 2    | [PFCOUNT key key ...](https://www.runoob.com/redis/hyperloglog-pfcount.html) 返回给定 HyperLogLog 的基数估算值。 |
| 3    | [PFMERGE destkey sourcekey sourcekey ...](https://www.runoob.com/redis/hyperloglog-pfmerge.html) 将多个 HyperLogLog 合并为一个 HyperLogLog |

用法：浏览数量统计

### (位图)BitMap

> 由0和1状态表现的二进制位的bit数组

![image-20240115115734225](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240115115734225.png)

| 序号 | 命令及描述 |
| ---- | ---------- |
| 1    | [BITCOUNT](https://redis.io/commands/bitcount/):与 GETRANGE 命令一样，start 和 end 可以包含负值 从字符串末尾开始索引字节的顺序，其中 -1 是最后一个 字节，-2 是倒数第二个，依此类推。  |
| 2    |  [BITFIELD](https://redis.io/commands/bitfield/):该命令将 Redis 字符串视为位数组，并且能够寻址具有不同位宽和任意非（必要）对齐偏移量的特定整数字段。实际上，使用此命令，您可以设置一个有符号的 5 位整数，例如，在位偏移量 1234 处设置一个特定值，从偏移量 4567 中检索一个 31 位无符号整数。同样，该命令处理指定整数的递增和递减，提供用户可以配置的有保证且明确指定的上溢和下溢行为。          |
| 3    |   [BITFIELD_RO ](https://redis.io/commands/bitfield_ro/):BITFIELD 命令的只读变体。 它类似于原始的 BITFIELD，但只接受子命令，并且可以安全地在只读副本中使用。GET         |
| 4    | [BITOP ](https://redis.io/commands/bitop/):该命令支持四种按位运算：AND、OR、XOR 和 NOT，并返回一个新的值           |
| 5    | [BITPOS](https://redis.io/commands/bitpos/)：返回位置，将字符串视为从左到 右，其中第一个字节的最高有效位位于位置 0，第二个字节位于位置 0 Byte 的最高有效位位于位置 8，依此类推。           |
| 6    |   [GETBIT ](https://redis.io/commands/getbit/):返回存储在键处的字符串值中偏移处的位值。         |
| 7    |   [SETBIT ](https://redis.io/commands/setbit/):根据值设置或清除位，该值可以是 0 或 1.        |

用法：签到统计

### (位域)BitField

> 通过 Bitfield 命令可以一次性操作多个比特位域(指的是连续的多个比特位)，它会执行一系列操作并返回一个响应数组，这个数组中的元素对应参数列表中的相应操作的执行结果。
>
> 说白了就是通过bitfield命令我们可以一次性对多个比特位域进行操作。

不想用啊！谁会没事直接操作 bit ，上连接！

[Redis BitField 官网命令集合](https://redis.io/commands/?group=bitfield)

### (流)Stream

> Redis Stream 是 Redis 5.0 版本新增加的数据结构。
>
> Redis Stream 主要用于消息队列（MQ，Message Queue），Redis 本身是有一个 Redis 发布订阅 (pub/sub) 来实现消息队列的功能，但它有个缺点就是消息无法持久化，如果出现网络断开、Redis 宕机等，消息就会被丢弃。
>
> 简单来说发布订阅 (pub/sub) 可以分发消息，但无法记录历史消息。
>
> 而 Redis Stream 提供了消息的持久化和主备复制功能，可以让任何客户端访问任何时刻的数据，并且能记住每一个客户端的访问位置，还能保证消息不丢失

Stream 主要是用作消息队列的功能，但是专业的事情还是有专业的工具来使用，所以这里就不作过多的介绍了！
[Redis Stream 官网命令集合](https://redis.io/commands/?group=stream)

## Redis 持久化

![image-20240123172651670](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123172651670.png)

**所有配置的持久化都会在redis重启的时候自动进行数据恢复**

### RDB

> RDB（Redis 数据库）：RDB 持久性以指定的时间间隔执行数据集的时间点快照。
>
> 在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的Snapshot内存快照，它恢复时再将硬盘,快照文件直接读回到内存里
>
> Redis的数据都在内存中，保存备份时它执行的是`全量快照`也就是说，把内存中的所有数据都记录到磁盘中，一锅端

**配置**

打开`save ""  关闭 `RDB` 持久化
打开` save 3600 1 300 100 60 10000` 开启 `RDB` 持久化

> 意思是3600s 内有1次修改便会出发自动持久化 或 300s 内有100次修改便会出发自动持久化 或 60s 内有10000次修改便会出发自动持久化
>
> 上面开就没事情可以了当然还可以配置存放的路劲等参数

![image-20240123173120642](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123173120642.png)

**其他配置**

```bash
save 3600 1 300 100 60 5 #配置自动触发时间
stop-writes-on-bgsave-error yes #这可以确保在持久化过程中发生问题时不会继续写入可能导致数据不一致的新数据。
rdbcompression yes # 启用RDB文件的压缩。这意味着在持久化时，Redis将尝试使用压缩算法来减小RDB文件的大小。
rdbchecksum yes # 在RDB文件中包含校验和，以确保文件的完整性。这可以在加载RDB文件时检测到文件是否损坏或被篡改。
dbfilename dump_6379.rdb # 配置rdb文件名
rdb-del-sync-files yes  # 当Redis启动时，如果存在旧的RDB和AOF文件，会在加载新的RDB或AOF文件之前删除这些旧文件。这可以防止旧数据的累积。
dir /data # 配置rdb文件的存放路径 这里使用绝对路径
```

![image-20240123194212519](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123194212519.png)

**手动触发 rdb 存储**

![image-20240123194623094](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123194623094.png)

手动触发有两个命令`save` 和 `bgsave`  

`save:` 会**占用主线程**进行数据备份，此时无法使用 `redis` 很少使用

`bgsave:`会新开一个线程进行数据备份，备份数据过大时`redis` 性能会略有下降

### AOF

```bash
appendonly yes # 启用AOF（Append-Only File）持久化。
appendfilename "appendonly.aof" # 配置AOF文件的名称。
appenddirname "appendonlydir" # 配置AOF文件的存放路径。
appendfsync everysec # 每秒同步一次AOF文件，确保数据持久化。
no-appendfsync-on-rewrite no # 在AOF重写时，不禁止进行文件同步。
auto-aof-rewrite-percentage 100 # 当AOF文件大小超过上一次重写后的大小的百分比时，自动触发AOF文件重写。
auto-aof-rewrite-min-size 64mb # 触发AOF文件重写所需的最小AOF文件大小。
aof-load-truncated yes # 允许加载被截断的AOF文件。
aof-use-rdb-preamble yes # 在AOF持久化时允许RDB文件的格式进行持久化。
aof-timestamp-enabled no # 禁用AOF文件的时间戳。
```

> 在 redis 7 之前是没有 `appenddirname` 属性配置的 这是新加的属性
>
> redis 6的时候 `AOF` 的 的持久化文件是和 `RDB` 文件一个目录下面的 
>
> 现在更新之后会在 `RDB` 配置的`${dir}` 路径下创建一个 `${appenddirname}` 文件中

![image-20240123200055614](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123200055614.png)

上图是持久化的文件 当文件过大的时候还会进行压缩处理 ,看到上图持久化内容的文件是不是有很多想法   0φ(*￣0￣)

### AOF+RDB 双管齐下

> 应该注意到了吧 **aof-use-rdb-preamble yes** 没错开启他就好了 两个持久化就可以一起使用了

为什么要一起使用？

答：RDB 是周期性备份，所以会丢失周期出发前的数据，所以需要 AOF，这个时候就会产生新的疑问为啥不直接使用 AOF ，那是因为 AOF 备份的文件过大，且恢复是的速度很慢。

两个都开回复的时候使用哪一个备份文件呢？

答：![image-20240123201326147](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240123201326147.png)

## Redis 事务

**事务介绍**

> 在Redis中，事务是一组**命令的集合**，它们被作为一个单独的操作单元来执行。事务可以确保这组命令要么**全部执行成功**，**要么全部失败**，从而**保持数据的一致性**。就是这样喵~ 哈哈哈
>
> `redis` 事务由三部：1. 开启事务 2. 添加指令 3. 执行事务	

---
**常用指令**

| 序号 | 命令及描述                                                   |
| :--- | :----------------------------------------------------------- |
| 1    | [DISCARD](https://www.runoob.com/redis/transactions-discard.html) 取消事务，放弃执行事务块内的所有命令。 |
| 2    | [EXEC](https://www.runoob.com/redis/transactions-exec.html) 执行所有事务块内的命令。 |
| 3    | [MULTI](https://www.runoob.com/redis/transactions-multi.html) 标记一个事务块的开始。 |
| 4    | [UNWATCH](https://www.runoob.com/redis/transactions-unwatch.html) 取消 WATCH 命令对所有 key 的监视。 |
| 5    | [WATCH key [key ...\]](https://www.runoob.com/redis/transactions-watch.html) 监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。 |

**案例**

> 下图提供了两个案例 
>
> 左侧将操作指令添加到队列中的时候没有检查到语法错误，在执行的时候只会对无法执行的命令弹出错误，其他正常执行
>
> 右侧的是在添加指令的时候有检测出语法问题所以整个事务中的所有命令都回滚

![image-20240124092758492](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124092758492.png)



**wathc**

> redis 提供的一个乐观锁 
>
> watch 的监控只针对事务在事务外是不会被出发的（上面的案例）
>
> 当执行事物的时候如果监控的数据被修改则会回滚（下面的案例）
>
> 一旦执行了exec之前加的监控锁都会被取消掉了，当客户端连接丢失的时候(比如退出链接)，所有东西都会被取消监视

![image-20240124095734879](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124095734879.png)

---

## Redis 管道

> Redis是一种基于客户端-服务端模型以及请求/响应协议的TCP服务。一个请求会遵循以下步骤：
>
> 1 客户端向服务端发送命令分四步(发送命令→命令排队→命令执行→返回结果)，并监听Socket返回，通常以阻塞模式等待服务端响应。
>
> 2 服务端处理命令，并将结果返回给客户端。
>
> **上述两步称为：Round Trip Time(简称RTT,数据包往返于两端的时间)，问题笔记最下方**

![image-20240124100646810](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124100646810.png)

---

**管道简介**

> Redis管道是一种机制，允许客户端在一次通信中发送多个命令，而不需要等待每个命令的响应。通过使用管道，可以显著提高在一定情况下的性能和效率。

**作用**

> **场景：**
>
> 如果同时需要执行大量的命令，那么就要等待上一条命令应答后再执行，这中间不仅仅多了RTT（Round Time Trip），而且还频繁调用系统IO，发送网络请求，同时需要redis调用多次read()和write()系统方法，系统方法会将数据从用户态转移到内核态，这样就会对进程上下文有比较大的影响了，性能不太好，o(╥﹏╥)o
>
> **解决：**
>
> 使用管道**一次性发送多条命令**给服务端，服务端依次处理完完毕后，通过**一条响应一次性将结果返回**，通过**减少客户端与redis的通信次数**来实现降低往返延时时间。pipeline实现的原理是队列，先进先出特性就保证数据的顺序性。

![image-20240124101014856](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124101014856.png)

**使用**

1. 使用linux命令`（printf "";）`编写一个redis 语句 ，使用`redis-cli --pipe`执行

```bash
 (printf "auth yourPassword\r\n set k1 PING1\r\nset k2  PING2\r\nget k1\r\n get k2\r\n";) | redis-cli --pipe
```

![image-20240124104731807](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124104731807.png)

2. 当然也可以创建一个文件将命令写进去执行reids命令

![image-20240124105108103](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124105108103.png)

**管道 VS 事务**

1. **事务具有原子性，管道不具有原子性**
2. 管道一次性将**多条命令发送**到服务器，事务是**一条一条的发**，事务只有在接收到exec命令后才会执行，管道不会
3. 执行**事务时会阻塞**其他命令的执行，而执行管道中的命令时不会

**注意**

1. pipeline缓冲的指令只是会依次执行，不保证原子性，如果执行中指令发生异常，将会继续执行后续的指令
2. 使用pipeline组装的命令个数不能太多，不然数据量过大客户端阻塞的时间可能过久，同时服务端此时也被迫回复一个队列答复，占用很多内存

## Redis 复制（Redis 主从/Redis 读写分离）

[Redis 复制 官网](https://redis.io/docs/management/replication/)

**可以干什么**

1. 读写分离
2. 容灾恢复
3. 数据备份
4. 水平扩容支撑高并发

**如何使用**

1. 配从（库）不配主（库）
2. master如果配置了requirepassa参数，需要密码登陆那么slave就要配置masterauth来设置校验密码，
   否则的话masters会拒绝slave的访问请求
3. 常用命令

```bash
info replication #可以查看复制节点的主从关系和配置信息
replicaof #主库IP主库端口
slaveof #主库IP主库端口
slaveof no one #从库脱离主库
```

---

### slaveof命令连接

> redis 重启主从关系将失效
>
> **注意：** 主库可读写 从库可读不可写

**配置修改**

> 配置和之前主机的一样 记得修改 端口号（按照自己的改）
> **masterauth** 连接主 redis 的 ${requirepass} 密码 从库一定要配置主库不用做任何改变

```bash
################################## MODULES #####################################
#bind 127.0.0.1 -::1
protected-mode no
port 6379

################################# GENERAL #####################################

daemonize no

################################ SNAPSHOTTING  ################################
 save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump_6379.rdb
rdb-del-sync-files yes
dir /data

################################# REPLICATION #################################
requirepass 1111222

appendonly yes
appendfilename "appendonly.aof"
appenddirname "appendonlydir_6379"
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes
aof-use-rdb-preamble yes

masterauth 1111222 # 连接主 redis 的 ${requirepass} 密码 从库一定要配置
```

**连接操作配置**

![image-20240124170332030](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124170332030.png)

![image-20240124173931839](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240124173931839.png)

### replicaof配置连接

> redis 重启主从关系将保持

配置和[slaveof命令连接](#slaveof命令连接)的配置一样只需要加上一下配置直接启动（重启）就可以实现**replicaof配置连接**

```bash
replicaof 127.0.0.1 6379 #配置master redis 的ip 和 port
```

**注意：**

1. master 下线 slaver 不会自动尚未
2. slaver 会自动同步 master 的数据
3. **replicaof配置连接** master 永远时是 master ，重启会自动上位

![image-20240125103311536](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125103311536.png)

### 主从复制流程

1. slave启动成功连接到master后会发送一个sync命令，slave首次全新连接master,一次完全同步（全量复制)将被自动执行，slave自身原有数据会被master数据覆盖清除
2. master节点收到sync命令后会开始在后台保存快照(即RDB持久化，主从复制时会触发RDB)，同时收集所有接收到的用于修改数据集命令缓存起来，master节点执行RDB持久化完后，master将rdb快照文件和所有缓存的命令发送到所有slave,以完成一次完全同步，而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中，从而完成复制初始化
3. 心跳持续，保持通信 ,默认配置心跳周期10s  `repl-ping-replica-period 10`
4. master 继续将新的所有收集到的修改命令自动依次传给 slaver
5. master会检查backlog里面的偏移量（offset），master和slave都会保存一个复制的offset还有一个masterId，offset是保存在backlog中的。Master只会把已经复制的offset后面的数据复制给Slave，类似断点续传

> 缺点：
>
> 1. 复制延迟，信号衰减。当进行多个从机进行复制时会产生信号衰减
> 2. slaver 不会自动上位 ，master 宕机服务会受到影响

## Redis 哨兵

[Redis Sentinel 的高可用性](https://redis.io/docs/management/sentinel/)

**是啥？可以干啥？**

> 介绍：
>
> 哨兵（sentinel）吹哨人巡查监控后台master主机是否故障，如果故障了根据**投票数**将某一个从库转换为新主库，继续对外服务。俗称，无人值守运维
>
> 作用：
>
> 1. 主从监控：监控主从redis库运行是否正常
> 2. 消息通知：哨兵可以将故障转移的结果发送给客户端
> 3. 故障转移：如果Master异常，则会进行主从切换，将其中一个Slave作为新Master
> 4. 配置中心：客户端通过连接哨兵来获得当前Redis服务的主节点地址

****

**使用**

官网建议使用至少三台`sentinel`在正式环境在三台机器上保证容错，联系架构模式如下：



![image-20240125153935176](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125153935176.png)

1.  添加配置`sentinel.conf`
   以下时`sentinel` 监控一台 `redis` 当然也可以监控多台,配置两组就可以，注意master的名字
   您只需要指定要监视的主站，给每个单独的主站 master（可以有任意数量的副本）不同的名称，会自动发现的slaver。

   **注意：**

   1. 需要给 conf 文件权限请执行`chmod -R 0777 /home/redis`，
   2. 三台 redis 都要配置 `masterauth`,且三台的 redis 密码要一样，不然切换主从的时候连接不上

[redis Git官网配置](https://github.com/redis/redis)

```bash
port 26379
daemonize no
pidfile /var/run/redis-sentinel.pid
logfile ""
dir /tmp
sentinel monitor mymaster 127.0.0.1 6379 2 # sentinel monitor <master-name> <ip> <redis-port> <quorum> 设置监控 ip和端口号的redis 名字设置为myredis   当投2个Sentinel认为主服务器失效时认为该redis宕机
sentinel auth-pass mymaster 123456 # sentinel auth-pass <master-name> <password> 配置 master 密码
sentinel down-after-milliseconds mymaster 30000 # 在30,000毫秒（30秒）之后，Sentinel将主服务器标记为失效。
acllog-max-len 128
sentinel parallel-syncs mymaster 1 # Sentinel一次只同步一个从服务器。
sentinel failover-timeout mymaster 180000 # 在180,000毫秒（180秒）之后，如果主服务器没有恢复，Sentinel将启动故障切换。
sentinel deny-scripts-reconfig yes
SENTINEL resolve-hostnames no
SENTINEL announce-hostnames no
```

2. 启动哨兵

```bash
redis-server /path/to/sentinel.conf --sentinel
# OR (官网给两种启动方法)
redis-sentinel /path/to/sentinel.conf
```

3. docker 安装

```bash
docker run --restart=always \
-p 26379:26379 \
--name sentinel_26379 \
-v /home/redis/sentinel_26379.conf:/etc/redis/sentinel_26379.conf \
-v /home/redis/data:/data \
-d redis:7.0.12 redis-sentinel /etc/redis/sentinel_26379.conf
```

![image-20240125143937823](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125143937823.png)

**提示：** 如果不是在docker 上配置 Redis 使用 Sentinel 进行自动管理时 sentinel 会自动修改`redis.conf` 配置文件 即便重启也会按照 新的 master，但是使用 `docker`容器时不会修改配置文件。 

---

### 工作流程

1. SDown 主观下线：当一台 Sentinel 在和 master 进行心跳检测时发现无法再规定时间内得到相应，则判断该 master 为主观下线。
2. ODown 客观下线：主观下线之后会进行和其他 sentinel 进行协商讨论 ，如果大于等于 quorum 则断定为客观下线。
3. 选举兵王：通过 `Raft`算法选举出兵王代表，进行选举master操作。
4. 选举Master：
   1. redis.conf文件中，优先级slave-priority或者replica-priority最高的从节点(数字越小优先级越高 )
   2. 偏移 setoff 高的有限 setoff 类似是与master的同步成都成正比，
   3. 如果以上都一样则采用RUN ID 的ASSCII 码进行比较，小的优先。  

**使用建议**

1. 哨兵节点的数量应为多个，哨兵本身应该集群，保证高可用
2. 哨兵节点的数量应该是奇数
3. 各个哨兵节点的配置应一致
4. 如果哨兵节点部署在Docker等容器里面，尤其要注意端口的正确映射
5. 哨兵集群+主从复制，并不能保证数据零丢失

## Redis 集群

[Redis 集群](https://redis.io/docs/management/scaling/)

**简介**由于数据量过大，单个Master复制集难以承担，因此需要对多个复制集进行集群，形成水平扩展每个复制集只负责存储整个数据集的一部分，这就是Redis的集群，其作用是提供在多个Redis节点间共享数据的程序集。
![image-20240125162209545](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125162209545.png)

**效果：**Redis集群是一个提供在多个Redis节点间共享数据的程序集，Redis集群可以支持多个Master

![image-20240125162606082](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125162606082.png)

**作用**

1. Redis?集群支特多个Master,每个Masterl又可以挂载多个Slave
2. 由于Cluster自带Sentinel的故障转移机制，内置了高可用的支持，无需再去使用哨兵功能
3. 客户端与Rdis的节点连接，不再需要连接集群中所有的节点，只需要任意连接集群中的一个可用节点即可
4. 槽位so负责分配到各个物理服务节点，由对应的集群来负责维护节点、插槽和数据之间的关系

### 概念

#### Redis 处理方案

**hash 槽位 slot**

Redis集群投有使用一致性hash而是引入了哈希槽的概念
Redis集群有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个槽.集群的每个节点负责部分hash槽，
举个例子，比如当前集群有3个节点那么：

![image-20240125164427866](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125164427866.png)



**分片**

| 问                    | 答                                                           |
| :-------------------- | :----------------------------------------------------------- |
| 分片是什么            | 使用Redis集群时我们会将存储的数据分散到多台redis机器上，这称为分片。简言之，集群中的每个Redis实例都被认为是整个数据的一个分片。 |
| 如何找到给定key的分片 | 为了找到给定key的分片，我们对key进行CRC16(key)算法处理并通过对总分片数量取模。然后，使用确定性哈希函数，这意味着给定的key将多次始终映射到同一个分片，我们可以推断将来读取特定key的位置。 |
| 为什么要这样做        | 这种结构很容易添加或者删除节点.比如如果我想新添加个节点D,我需要从节点A,B,C中得部分槽到D上.如果我想移除节点A,需要将A中的槽移到B和C节点上，然后将没有任何槽的A节点从集群中移除即可.由于从一个节点将哈希槽移动到另一个节点并不会停止服务，所以无论添加删除或者改变某个节点的哈希槽的数量都不会造成集群不可用的状态 |

#### 其他处理方案

1. Hash 取余

   ![image-20240125165416284](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125165416284.png)

   

   >  更多操作2亿条记录就是2亿个k,v，我们单机不行必须要分布式多机，假设有3台机器构成一个集群，用户每次读写操作都是根据公式：hash(key) % N个机器台数，计算出哈希值，用来决定数据映射到哪一个节点上。
   >
   > 
   >
   > **优点：** 简单粗暴，直接有效，只需要预估好数据规划好节点，例如3台、8台、10台，就能保证一段时间的数据支撑。使用Hash算法让固定的一部分请求落到同一台服务器上，这样每台服务器固定处理一部分请求（并维护这些请求的信息），起到负载均衡+分而治之的作用。
   >
   > 
   >
   > **缺点：**  当 Redis 服务器扩容或是缩容的时候需要改边 计算公式  hash(key) % ？ 。这个时候及可能无法与之前存储的数据所计算出来的位置保持一致。这都好说保证不改变数量就可以，但是如果 机器宕机了呢 对应位置的数据将无法进行存取。

2. 一致性HASH

   (1 构建hash环

   > 一致性哈希环
   >
   >   一致性哈希算法必然有个hash函数并按照算法产生hash值，这个算法的所有可能哈希值会构成一个全量集，这个集合可以成为一个hash空间[0,2^32-1]，这个是一个线性空间，但是在算法中，我们通过适当的逻辑控制将它首尾相连(0 = 2^32),这样让它逻辑上形成了一个环形空间。
   >
   >   它也是按照使用取模的方法，前面笔记介绍的节点取模法是对节点（服务器）的数量进行取模。而一致性Hash算法是对2^32取模，简单来说，一致性Hash算法将整个哈希值空间组织成一个虚拟的圆环，如假设某哈希函数H的值空间为0-2^32-1（即哈希值是一个32位无符号整形），整个哈希环如下图：整个空间按顺时针方向组织，圆环的正上方的点代表0，0点右侧的第一个点代表1，以此类推，2、3、4、……直到2^32-1，也就是说0点左侧的第一个点代表2^32-1， 0和2^32-1在零点中方向重合，我们把这个由2^32个点组成的圆环称为Hash环。

   ![image-20240125170510918](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125170510918.png)

   

   (2 节点映射

   >   将集群中各个IP节点映射到环上的某一个位置。
   >
   >   将各个服务器使用Hash进行一个哈希，具体可以选择服务器的IP或主机名作为关键字进行哈希，这样每台机器就能确定其在哈希环上的位置。假如4个节点NodeA、B、C、D，经过IP地址的哈希函数计算(hash(ip))，使用IP地址哈希后在环空间的位置如下： 

   ![image-20240125170605537](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125170605537.png)

   （3 落建服务器

   > 当我们需要存储一个kv键值对时，首先计算key的hash值，hash(key)，将这个key使用相同的函数Hash计算出哈希值并确定此数据在环上的位置，**从此位置沿环顺时针“行走”**，第一台遇到的服务器就是其应该定位到的服务器，并将该键值对存储在该节点上。
   >
   > 如我们有Object A、Object B、Object C、Object D四个数据对象，经过哈希计算后，在环空间上的位置如下：根据一致性Hash算法，数据A会被定为到Node A上，B被定为到Node B上，C被定为到Node C上，D被定为到Node D上。

   ![image-20240125170810702](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125170810702.png)

   (4 优缺点

   > **优点  1健壮性**
   >
   > 假设Node C宕机，可以看到此时对象A、B、D不会受到影响。一般的，在一致性Hash算法中，如果一台服务器不可用，则受影响的数据仅仅是此服务器到其环空间中前一台服务器（即沿着逆时针方向行走遇到的第一台服务器）之间数据，其它不会受到影响。简单说，就是C挂了，受到影响的只是B、C之间的数据且这些数据会转移到D进行存储。

   ![image-20240125170943911](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125170943911.png)

   > **优点 2易扩展**
   >
   > 数据量增加了，需要增加一台节点NodeX，X的位置在A和B之间，那收到影响的也就是A到X之间的数据，重新把A到X的数据录入到X上即可，不会导致hash取余全部数据重新洗牌。

   ![image-20240125171132223](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125171132223.png)

   > **缺点：数据倾斜**
   >
   > 一致性Hash算法在服务**节点太少时**，容易因为节点分布不均匀而造成**数据倾斜**（被缓存的对象大部分集中缓存在某一台服务器上）问题，
   >
   > 例如系统中只有两台服务器：

   ![image-20240125171405375](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125171405375.png)

3. Hash槽

> Redis 集群中内置了 16384 个哈希槽，redis 会根据节点数量大致均等的将哈希槽映射到不同的节点。当需要在 Redis 集群中放置一个 key-value时，redis先对key使用crc16算法算出一个结果然后用结果对16384求余数[ CRC16(key) % 16384]，这样每个 key 都会对应一个编号在 0-16383 之间的哈希槽，也就是映射到某个节点上。如下代码，key之A 、B在Node2， key之C落在Node3上
>
> 在 Redis 增加删除的时候只需要对 Redis 集群中的部分槽位进行重新分配到其他的 Redis机器上面即可，宕机了使用该机器的 slaver。

![image-20240125171447593](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240125171447593.png)

**为什么只有16384个槽位?**

> (1)如果槽位为65536，发送心跳信息的消息头达8k，发送的心跳包过于庞大。
>
> 在消息头中最占空间的是myslots[CLUSTER_SLOTS/8]。 当槽位为65536时，这块的大小是: 65536÷8÷1024=8kb 
>
> 在消息头中最占空间的是myslots[CLUSTER_SLOTS/8]。 当槽位为16384时，这块的大小是: 16384÷8÷1024=2kb 
>
> 因为每秒钟，redis节点需要发送一定数量的ping消息作为心跳包，如果槽位为65536，这个ping消息的消息头太大了，浪费带宽。
>
> 
>
> (2)redis的集群主节点数量基本不可能超过1000个。
>
> 集群节点越多，心跳包的消息体内携带的数据越多。如果节点过1000个，也会导致网络拥堵。因此redis作者不建议redis cluster节点数量超过1000个。 那么，对于节点数在1000以内的redis cluster集群，16384个槽位够用了。没有必要拓展到65536个。
>
> 
>
> (3)槽位越小，节点少的情况下，压缩比高，容易传输
>
> Redis主节点的配置信息中它所负责的哈希槽是通过一张bitmap的形式来保存的，在传输过程中会对bitmap进行压缩，但是如果bitmap的填充率slots / N很高的话(N表示节点数)，bitmap的压缩率就很低。 如果节点数很少，而哈希槽数量很多的话，bitmap的压缩率就很低。 



**注意：要使 Docker 与 Redis 集群兼容，您需要使用 Docker 的*主机组网模式*。**

### 搭建集群

> 练习一下docker compose

1. 编写 docker-compose.yml	

   ```yml
   version: '3.1'
   services:
     # redis6379配置
     redis6379:
       image: redis:7.0.12
       container_name: redis_6379
        ports:
         - 6379:6379 # rediis端口
         - 16379:16379 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6379.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
      # redis6380配置
     redis6380:
       image: redis:7.0.12
       container_name: redis_6380
        ports:
         - 6380:6380 # rediis端口
         - 16380:16380 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6380.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
     # redis6381配置
     redis6381:
       image: redis:7.0.12
       container_name: redis_6381
        ports:
         - 6381:6381 # rediis端口
         - 16381:16381 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6381.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
      # redis6382配置
     redis6382:
       image: redis:7.0.12
       container_name: redis_6382
        ports:
         - 6382:6382 # rediis端口
         - 16382:16382 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6382.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
         # redis6383配置
     redis6383:
       image: redis:7.0.12
       container_name: redis_6383
        ports:
         - 6383:6383 # rediis端口
         - 16383:16383 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6383.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
      # redis6384配置
     redis6384:
       image: redis:7.0.12
       container_name: redis_6384
        ports:
         - 6384:6384 # rediis端口
         - 16384:16384 # 集群节点通讯接口在对外设置的端口基础上+10000
       restart: always
       network_mode: "host"
       # 配置和数据的数据卷映射
       volumes:
         - /home/redis/cluster_6384.conf:/etc/redis/cluster.conf
         - /home/redis/data:/data
       command: ["redis-server", "/etc/redis/cluster.conf"]
   ```

2. 编写cluster_${port}.conf 配置文件

   > 每个配置文件都一样的，记得**修改端口和日志持久化等文件名**
   >
   > **容器数据卷的映射文件夹记得创建！！！！！！！！！！** 我踩坑了，不去创建的话，容器有很大概率启动不起来

   ```bash
   ################################## NETWORK #####################################
   protected-mode no
   #要改
   port 6379   
   tcp-backlog 511
   timeout 0
   tcp-keepalive 300
   ################################# GENERAL #####################################
   daemonize no
   #要改
   pidfile /data/cluster/pid/cluter_6379.pid 
   loglevel notice
   #要改
   logfile "/data/cluster/log/cluster_6379.log" 
   databases 16
   always-show-logo no
   set-proc-title yes
   proc-title-template "{title} {listen-addr} {server-mode}"
   ################################ SNAPSHOTTING  ################################
   save 3600 1 300 100 60 5
   stop-writes-on-bgsave-error yes
   rdbcompression yes
   rdbchecksum yes
   dbfilename dump_6379.rdb
   rdb-del-sync-files yes
   dir /data 
   
   ################################# REPLICATION #################################
   #要改与${requirepass}一样
   masterauth 111111 
   replica-serve-stale-data yes
   replica-read-only yes
   repl-diskless-sync yes
   repl-diskless-sync-delay 5
   repl-diskless-sync-max-replicas 0
   repl-diskless-load disabled
   repl-disable-tcp-nodelay no
   replica-priority 100
   ################################## SECURITY ###################################
   acllog-max-len 128
   requirepass 111111
   ############################# LAZY FREEING ####################################
   lazyfree-lazy-eviction no
   lazyfree-lazy-expire no
   lazyfree-lazy-server-del no
   replica-lazy-flush no
   lazyfree-lazy-user-del no
   lazyfree-lazy-user-flush no
   ############################ KERNEL OOM CONTROL ##############################
   oom-score-adj no
   oom-score-adj-values 0 200 800
   #################### KERNEL transparent hugepage CONTROL ######################
   disable-thp yes
   ############################## APPEND ONLY MODE ###############################
   appendonly yes
   #看心情改
   appendfilename "appendonly_cluster_6379.aof"  
    #看心情改
   appenddirname "cluster_appendonlydir"   
   appendfsync everysec
   no-appendfsync-on-rewrite no
   auto-aof-rewrite-percentage 100
   auto-aof-rewrite-min-size 64mb
   aof-load-truncated yes
   aof-use-rdb-preamble yes
   aof-timestamp-enabled no
   ################################ REDIS CLUSTER  ###############################
   cluster-enabled yes
   #看心情改
   cluster-config-file nodes-6379.conf 
   ################################## SLOW LOG ###################################
   slowlog-log-slower-than 10000
   slowlog-max-len 128
   ################################ LATENCY MONITOR ##############################
   latency-monitor-threshold 0
   ############################# EVENT NOTIFICATION ##############################
   notify-keyspace-events ""
   ############################### ADVANCED CONFIG ###############################
   hash-max-listpack-entries 512
   hash-max-listpack-value 64
   list-max-listpack-size -2
   list-compress-depth 0
   set-max-intset-entries 512
   zset-max-listpack-entries 128
   zset-max-listpack-value 64
   hll-sparse-max-bytes 3000
   stream-node-max-bytes 4096
   stream-node-max-entries 100
   activerehashing yes
   client-output-buffer-limit normal 0 0 0
   client-output-buffer-limit replica 256mb 64mb 60
   client-output-buffer-limit pubsub 32mb 8mb 60
   hz 10
   dynamic-hz yes
   aof-rewrite-incremental-fsync yes
   rdb-save-incremental-fsync yes
   jemalloc-bg-thread yes
   ```

   启动成功！

   ![image-20240126140120580](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126140120580.png)

3. 配置集群

   > 随便连接上一个 redis 机器  执行以下命令
   > --cluster-replicas 1 意思是每台机器有一台 slaver
   >
   > **改用自己的ip 和 端口 还有密码**

   ```
   redis-cli -a 111111 --cluster create --cluster-replicas 1 43.138.25.182:6379 43.138.25.182:6380 43.138.25.182:6381 43.138.25.182:6382 43.138.25.182:6383 43.138.25.182:6384
   ```

   ![image-20240126143341284](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126143341284.png)

   验证！

   > 使用正常连接方法连接集群时 部分 key 无法添加 因为分槽的原因 这个 key 可能不是当前 redis 机器可以存储的 使用集群方式连接则可以解决，下面测试中设置  k1 时提示槽位时 12706 需要移动到6381的机器上执行
   >
   > `redis-cli -a <连接密码> -p <redis机器端口> -c  `后面的`-c`表示集群连接

   ![image-20240126144006794](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126144006794.png) 

#### **Stream On Error: Connection is closed  踩坑！**

> 我在使用 Another Redis Desktop Manager客户端 连接redis集群报错 Stream On Error: Connection is closed

**处理：修改node.conf**

> 在node.conf中自动生成的 ip 是内网 ip 改成外网的就可以了

![image-20240126150116864](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126150116864.png)

### Redis 集群扩缩容

**reids 集群常用命令**

1. `info replication`查看主从信息
2. `cluster info` 查看集群状态配置信息
3. `cluster nodes` 查看集群节点信息

#### 缩容

1. 移除 slaver 节点机器

   ```bash
   # 命令：redis-cli -a 密码 --cluster del-node ip:从机端口 从机6384节点ID
    redis-cli -a 111111 --cluster del-node 43.138.25.182:6384 7cefe2b448b9876d7c095568a86cbbef5aaa155d
   ```

   ![image-20240126151859036](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126151859036.png)

2. 清空并分配 slot 数据槽

   ```bash
   redis-cli -a 111111 --cluster reshard 43.138.25.182:6381
   ```

   <img src="https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126162221293.png" width="" height="">

   > 第一次输入的是移动的数量，第二次输入接受的id，第三次输入输出源（也就是清空的redis 的 id ，回车在输入done）

3. 移除master

   ```bash
   # 命令：redis-cli -a 密码 --cluster del-node ip:从机端口 从机6381节点ID
   redis-cli -a 111111 --cluster del-node 43.138.25.182:6381 0e1a6339e2dc1f9ee2e47d2a46eb8a99b22b955d
   ```

   > 删除之后查看节点发现之前的6381 的槽位全在 6379上面

   ![image-20240126153637681](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126153637681.png)

#### 扩容

1. 添加新的master

   ```bash
    #将新增的6387作为master节点加入原有集群 6387 就是将要作为master新增节点 6381 就是原来集群节点里面的领路人，相当于6387拜拜6381的码头从而找到组织加入集群
   #redis-cli -a 密码 --cluster add-node 自己实际IP地址:6387 自己实际IP地址:6381
   
   redis-cli -a 111111 --cluster add-node 43.138.25.182:6381 43.138.25.182:6379
   ```

   ![image-20240126160735535](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126160735535.png)

2. 分配slot给新的master(和上面一样的)

   ```bash
   redis-cli -a 111111 --cluster reshard 43.138.25.182:6381
   ```

   <img src="https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126162221293.png" width="" height="">

3. 分配新的slaver给新的master

   ```bash
   #命令：redis-cli -a 密码 --cluster add-node ip:新slave端口 ip:新master端口 --cluster-slave --cluster-master-id 新主机节点ID
   redis-cli -a 111111 --cluster  add-node 43.138.25.182:6384 43.138.25.182:6381 --cluster-slave --cluster-master-id 0e1a6339e2dc1f9ee2e47d2a46eb8a99b22b955d
   ```

   ![image-20240126163132167](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240126163132167.png)

   分配完成！6379对应6382 ，6381对应6384 ，6382对应6383

## SpringBoot 集成 Redis

**添加依赖**

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <junit.version>4.12</junit.version>
    <log4j.version>1.2.17</log4j.version>
    <lombok.version>1.16.18</lombok.version>
    <nacos.context>2.1.0-RC</nacos.context>
    <!--        <swagger.ui>2.9.2</swagger.ui>-->
    <swagger.ui>3.0.0</swagger.ui>
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
        <version>1.18.26</version>
    </dependency>
</dependencies>
```

**写配置**

```yml
server:
  port: 1346
spring:
  swagger2:
    enabled: true
  application:
    name: redis_learn
  redis:
#    单机连接
#    host:  127.0.0.1
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

添加redis配置

> 设置value的序列化方式json，使用`GenericJackson2JsonRedisSerializer`替换默认序列化

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

>  如果不设置会导致序列化的时候在程序调用时没有异常 但是在使用redis客户端时产生乱码

![image-20240213104135170](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240213104135170.png)

**编写测试controller**

```java
--------------------------------------service--------------------------------------
@Service
@Slf4j
public class OrderService
{
    public static final String ORDER_KEY = "order:";

    @Resource
    private RedisTemplate redisTemplate;

    public String addOrder()
    {
        int keyId = ThreadLocalRandom.current().nextInt(1000)+1;
        String orderNo = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(ORDER_KEY+keyId,"京东订单"+ orderNo);
        log.info("=====>编号"+keyId+"的订单流水生成:{}",orderNo);
        return ORDER_KEY + keyId;
    }

    public String getOrderById(Integer id)
    {
        return (String)redisTemplate.opsForValue().get(ORDER_KEY + id);
    }
}
--------------------------------------controller--------------------------------------
@RestController
@Slf4j
@ApiModel(value = "订单接口",description = "订单接口")
public class OrderController
{
    @Resource
    private OrderService orderService;

    @ApiOperation("新增订单")
    @GetMapping(value = "/order/add")
    public String addOrder()
    {
      return  orderService.addOrder();
    }


    @ApiOperation("按orderId查订单信息")
    @GetMapping(value = "/order/{id}")
    public String findUserById(@PathVariable Integer id)
    {
        return orderService.getOrderById(id);
    }
}
```

swagger 测试

![image-20240213104554654](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240213104554654.png)
