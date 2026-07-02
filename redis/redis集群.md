# Redis 集群

![image-20240806114153580](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240806114153580.png)

## 启动Redis节点

### Redis1

>  配置文件位置 `/home/redis/cluster_6379.conf`

**配置文件**

```sh
#要改
port 6379
pidfile /data/cluster/pid/cluter_6379.pid 
#logfile "/data/cluster/log/cluster_6379.log" 
dbfilename dump_6379.rdb
appendfilename "appendonly_cluster_6379.aof"  
cluster-config-file nodes-6379.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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

**运行脚本**

```sh
# 脚本1: 端口号6379
docker run --name cluster_redis_6379 -d \
-p 6379:6379 -p 16379:16379 \
-v /home/redis/cluster_6379.conf:/etc/redis/cluster.conf \
-v /home/redis/data3679:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf
```

### Redis2

>  配置文件位置 `/home/redis/cluster_6380.conf`

**配置文件**

```sh
#要改
port 6380
pidfile /data/cluster/pid/cluter_6380.pid 
#logfile "/data/cluster/log/cluster_6380.log" 
dbfilename dump_6380.rdb
appendfilename "appendonly_cluster_6380.aof"  
cluster-config-file nodes-6380.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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



**运行脚本**

```sh
# 脚本2: 端口号6380
docker run --name cluster_redis_6380 -d \
-p 6380:6380 -p 16380:16380 \
-v /home/redis/cluster_6380.conf:/etc/redis/cluster.conf \
-v /home/redis/data3680:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf

```

### Redis3

>  配置文件位置 `/home/redis/cluster_6381.conf`

**配置文件**

```sh
#要改
port 6381
pidfile /data/cluster/pid/cluter_6381.pid 
#logfile "/data/cluster/log/cluster_6381.log" 
dbfilename dump_6381.rdb
appendfilename "appendonly_cluster_6381.aof"  
cluster-config-file nodes-6381.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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

**运行脚本**

```sh
# 脚本3: 端口号6381
docker run --name cluster_redis_6381 -d \
-p 6381:6381 -p 16381:16381 \
-v /home/redis/cluster_6381.conf:/etc/redis/cluster.conf \
-v /home/redis/data3681:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf

```

### Redis4

>  配置文件位置 `/home/redis/cluster_6382.conf`

**配置文件**

```sh
#要改
port 6382
pidfile /data/cluster/pid/cluter_6382.pid 
#logfile "/data/cluster/log/cluster_6382.log" 
dbfilename dump_6382.rdb
appendfilename "appendonly_cluster_6382.aof"  
cluster-config-file nodes-6382.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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

**运行脚本**

```sh

# 脚本4: 端口号6382
docker run --name cluster_redis_6382 -d \
-p 6382:6382 -p 16382:16382 \
-v /home/redis/cluster_6382.conf:/etc/redis/cluster.conf \
-v /home/redis/data3682:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf
```

### Redis5

>  配置文件位置 `/home/redis/cluster_6383.conf`

**配置文件**

```sh
#要改
port 6383
pidfile /data/cluster/pid/cluter_6383.pid 
#logfile "/data/cluster/log/cluster_6383.log" 
dbfilename dump_6383.rdb
appendfilename "appendonly_cluster_6383.aof"  
cluster-config-file nodes-6383.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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

**运行脚本**

```sh
# 脚本5: 端口号6383
docker run --name cluster_redis_6383 -d \
-p 6383:6383 -p 16383:16383 \
-v /home/redis/cluster_6383.conf:/etc/redis/cluster.conf \
-v /home/redis/data3683:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf
```

### Redis6

>  配置文件位置 `/home/redis/cluster_6384.conf`

**配置文件**

```sh
#要改
port 6384
pidfile /data/cluster/pid/cluter_6384.pid 
#logfile "/data/cluster/log/cluster_6384.log" 
dbfilename dump_6384.rdb
appendfilename "appendonly_cluster_6384.aof"  
cluster-config-file nodes-6384.conf 
#要改与${requirepass}一样
masterauth pde888888
requirepass pde888888


################################## NETWORK #####################################
protected-mode no

tcp-backlog 511
timeout 0
tcp-keepalive 300
################################# GENERAL #####################################
daemonize no

loglevel notice

databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
################################ SNAPSHOTTING  ################################
save 3600 1 300 100 60 5
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes

rdb-del-sync-files yes
dir /data 

################################# REPLICATION #################################

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

**运行脚本**

```sh
# 脚本6: 端口号6384
docker run --name cluster_redis_6384 -d \
-p 6384:6384 -p 16384:16384 \
-v /home/redis/cluster_6384.conf:/etc/redis/cluster.conf \
-v /home/redis/data3684:/data \
redis:7.0.12 redis-server /etc/redis/cluster.conf
```

## 构建集群环境

1. 查看运行中的包含`cluster_redis`的容器

```shell
docker ps -f status=running | grep -E 'cluster_redis*'
```

![image-20240812105321817](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240812105321817.png)

> 对比刚刚创建的容器，所有的都存在，所以容器启动没有问题，进行下一步。

2. 进入容器

```shell
docker exec -it {containerId} bash
```

![image-20240812111121731](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240812111121731.png)

> 这里进入的是最后创建好的`redis`集群容器，**进入哪一个容器都一样**

3. 配置集群

```shell
#redis-cli -a {redis 密码} --cluster create --cluster-replicas {从节点数量} [部署集群的 redis 集合]
redis-cli -a pde888888 --cluster create --cluster-replicas 1 10.20.20.135:6379 10.20.20.135:6380 10.20.20.135:6381 10.20.20.135:6382 10.20.20.135:6383 10.20.20.135:6384
```

![image-20240812111802186](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240812111802186.png)

4. 验证

```shell
#集群连接
redis-cli -a {密码} -c -p {端口}
#查看主从信息
info replication
#查看集群状态配置信息
cluster info
#查看集群节点信息
cluster nodes
```

![image-20240812112543885](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240812112543885.png)

## 服务配置

```yaml
spring:
  redis:
    password: pde888888
    cluster:
      nodes: 10.20.20.135:6379,10.20.20.135:6380,10.20.20.135:6381,10.20.20.135:6382,10.20.20.135:6383,10.20.20.135:6384
    lettuce:
      pool:
        max-active: 20    # 连接池最大连接数(使用负值表示没有限制) 默认为8
        max-wait: -1      # 连接池最大阻塞等待时间(使用负值表示没有限制) 默认为-1
        max-idle: 8       # 连接池中的最大空闲连接 默认为8
        min-idle: 0       # 连接池中的最小空闲连接 默认为 0
    timeout: 5000         # 连接超时时间（毫秒）
```

> 等待服务启动，这样即便有些 redis 宕机了也不用担心，并且 redis 服务的性能也有所提升
