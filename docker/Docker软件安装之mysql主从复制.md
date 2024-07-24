## Docker复杂安装

### mysql主从复制

1. 启动容器挂载数据卷

```bash
docker run -dp 3306:3306 \
  -v /home/master/data:/var/lib/mysql \
  -v /home/master/config:/etc/mysql \
  -v /home/master/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=pde#123456789 \
  --name mysql_master mysql:5.7.36
```

![image-20230917220548350](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917220548350.png)

2. 配置cnf文件

在mysql的配置文件的映射文件夹下添加my.cnf配置文件内容如下:

```bash
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=101 
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能
log-bin=mall-mysql-bin  
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062
```

![image-20230917221059922](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917221059922.png)

3. 重启

```bash
docker restart mysql_master
```

![image-20230917221111346](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917221111346.png)

4. 进入容器查看是否可以使用数据库,并创建同步用户

```bash
# 进入容器mysql_master
docker exec -it mysql_master bash

# 登陆mysql
mysql -uroot -p

# 创建用户slave
CREATE USER 'slave'@'%' IDENTIFIED BY '123456';

# 户赋权
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
```

![image-20230917221412728](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917221412728.png)

5. 新建mysql从服务器

```bash
docker run -dp 3308:3306 \
  -v /home/slave/data:/var/lib/mysql \
  -v /home/slave/config:/etc/mysql \
  -v /home/slave/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --name mysql_slave mysql:5.7.36
```

![image-20230917221858695](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917221858695.png)

6. 配置从cnf文件

```bash
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=102
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能，以备Slave作为其它数据库实例的Master时使用
log-bin=mall-mysql-slave1-bin  
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062  
## relay_log配置中继日志
relay_log=mall-mysql-relay-bin  
## log_slave_updates表示slave将复制事件写进自己的二进制日志
log_slave_updates=1  
## slave设置为只读（具有super权限的用户除外）
read_only=1
sql_mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```

![image-20230917222031520](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917222031520.png)

7. 重启丛mysql

```bash
docker restart mysql_slave
```

![image-20230917222126743](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917222126743.png)

8. 前往主mysql查看主从同步状态

```mysql
show master status;
```

![image-20230917222337281](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917222337281.png)

9. 在从数据库配置主从复制

```mysql
change master to master_host='宿主机ip', master_user='slave', master_password='123456', master_port=3307, master_log_file='mall-mysql-bin.000001', master_log_pos=617, master_connect_retry=30;  
```

`master_host`：主数据库的IP地址；

`master_port`：主数据库的运行端口；

`master_user`：在主数据库创建的用于同步数据的用户账号；

`master_password`：在主数据库创建的用于同步数据的用户密码；

`master_log_file`：指定从数据库要复制数据的日志文件，通过查看主数据的状态，获取File参数；

`master_log_pos`：指定从数据库从哪个位置开始复制数据，通过查看主数据的状态，获取Position参数；

`master_connect_retry`：连接失败重试的时间间隔，单位为秒。 

![image-20230917222855930](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917222855930.png)

10. 从数据库查看并设置主从同步状态

- 查看

```mysql
show slave status \G;
```

`\G`:以键值对的形式展示

![image-20230917223007055](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917223007055.png)

- 设置启用

```mysql
start slave;
```

![image-20230917223309470](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917223309470.png)

11. 测试状况

![image-20230917224037535](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917224037535.png)

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
