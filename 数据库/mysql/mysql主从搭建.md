# Mysql 主从搭建

> 基于 docker 环境搭建 mysql 主从数据库
>
> 如不使用`docker`仅需修改数据库配置即可完成！！&#x20;
>
> 本教程为一主一从模式，主从端口：master：3307 slaver：3308

## master 节点

### 配置

> 创建`mysql`配置文件
>
> 文件位置在下docker run 脚本中 `-v /home/master/config:/etc/mysql` 的  `/home/master/config`位置下创建 `my.cnf`文件

```cnf
[mysqld]
#synchronous_binlog = 1  # 表示等待一个从服务器确认，开启后性能会变低
innodb_force_recovery=0
character-set-server=utf8
## 设置server_id，同一局域网中需要唯一
server_id=102
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能，以备Slave作为其它数据库实例的Master时使用
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
## relay_log配置中继日志
relay_log=mall-mysql-relay-bin  
## log_slave_updates表示slave将复制事件写进自己的二进制日志
log_slave_updates=1  
## slave设置为只读（具有super权限的用户除外）
read_only=1
## sql mode 设置 STRICT_TRANS_TABLES 事务会回滚，并且返回错误 ,ERROR_FOR_DIVISION_BY_ZERO 除以零报错,NO_ENGINE_SUBSTITUTION 使用默认的引擎来创建表 
sql_mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
## 连接数量
max_connections=1000
## 表明使用小写
lower_case_table_names=1
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```

### 运行容器

> mysql master 节点  docker 运行脚本&#x20;
>
> 这里的**数据卷位置，和数据库密码根据情况自行修改**

```sh
docker run -dp 3307:3306 \
  -v /home/master/data:/var/lib/mysql \
  -v /home/master/config:/etc/mysql/conf.d \
  -v /home/master/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --name mysql_master mysql:5.7
```

> 创建从数据库的用户，及授权

```sh
# 进入容器 mysql_master 
docker exec -it mysql_master bash

# 登陆 mysql 注意自己的密码！！！
mysql -uroot -p
```

```sql
# 创建用户slave
CREATE USER 'slave'@'%' IDENTIFIED BY '123456';

# 户赋权 所有数据库授予从服务器复制的权限 授予客户端复制的权限，这里没有限制从节点的ip，如有需要请自行修改
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
```

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/20240722154342.png)

**mysql查看主从同步状态,这个很重要保存下来！！！！！！！！！！！！！！！！！！！**

```sql
show master status;
```

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/20240722161738.png)

## salve 节点

### 配置

> 配置文件的位置和主节点查看的方式一样这里放在了`  /home/slave/config/my.cnf  ` 中

```cnf
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=103
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
## sql mode 设置 STRICT_TRANS_TABLES 事务会回滚，并且返回错误 ,ERROR_FOR_DIVISION_BY_ZERO 除以零报错,NO_ENGINE_SUBSTITUTION 使用默认的引擎来创建表 
sql_mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
## 连接数量
max_connections=1000
## 表明使用小写
lower_case_table_names=1
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8
```

### 运行容器

```sh
docker run -dp 3308:3306 \
  -v /home/slave/data:/var/lib/mysql \
  -v /home/slave/config:/etc/mysql/conf.d \
  -v /home/slave/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --name mysql_slave mysql:5.7
```

## &#x20;主从绑定

```sh
# 进入容器mysql_slave 
docker exec -it mysql_slave bash

# 登陆 mysql 注意自己的密码！！！
mysql -uroot -p
```

```sql
change master to master_host='10.20.20.135', master_user='slave', master_password='123456', master_port=3307, master_log_file='mall-mysql-bin.000001', master_log_pos=154, master_connect_retry=30;  
```

`master_host`：主数据库的IP地址；

`master_port`：主数据库的运行端口；

`master_user`：在主数据库创建的用于同步数据的用户账号；

`master_password`：在主数据库创建的用于同步数据的用户密码；

`master_log_file`：指定从数据库要复制数据的日志文件，通过查看主数据的状态，获取File参数；

`master_log_pos`：指定从数据库从哪个位置开始复制数据，通过查看主数据的状态，获取Position参数；

`master_connect_retry`：连接失败重试的时间间隔，单位为秒。

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/20240722161408.png)

### &#x20;启动并查看从节点状态

```sql
start slave; 
show slave status\G;
```

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/20240722163628.png)

## 主从验证

> 向主节点插入数据，从节点也出现了同样的表

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/20240722165356.png)

# mybatisPlus 配置主从

> 确保安全可以在从节点创建一个只读权限用户

```sql
# 创建只读用户：read_only_pde  密码为：123456
CREATE USER 'read_only_pde'@'%' IDENTIFIED BY '123456';
# 设置只读权限
GRANT SELECT ON *.* TO 'read_only_pde'@'%';
# 禁用权限
REVOKE INSERT, UPDATE, DELETE ON *.* FROM 'read_only_pde'@'%';
```

> 添加的依赖

```xml
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.30</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.3</version>
        </dependency>
```

> yml 配置

```yml
spring:
  datasource:
    master:
      username: root
      password: 123456
      url: jdbc:mysql://10.20.20.135:3307/135master?useUnicode=true&characterEncoding=utf8
      driver-class-name: com.mysql.cj.jdbc.Driver
    slave:
      username: read_only_pde
      password: 123456
      url: jdbc:mysql://10.20.20.135:3308/135master?useUnicode=true&characterEncoding=utf8
      driver-class-name: com.mysql.cj.jdbc.Driver
```

database-dev.yaml

> 等待验证，此配置还没有进行项目验证

```yaml
# 此文件为关系型数据库连接配置信息
# 部署过程可能需要修改的项目如下：
# 1、 spring.datasource.dynamic.datasource.master.url：数据库连接url
# 2、 spring.datasource.dynamic.datasource.master.username: 用户名
# 3、 spring.datasource.dynamic.datasource.master.password: 口令
# 4、 spring.datasource.dynamic.datasource.master.driver-class-name: 驱动类名
spring:
  datasource:
    dynamic:
      primary: master
      datasource:
        master:
          driver-class-name: ${DATABASE_DRIVER:com.mysql.cj.jdbc.Driver}
          url: jdbc:mysql://10.20.20.135:3307/135master?characterEncoding=utf8&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull&nullCatalogMeansCurrent=true
          username: ${DATABASE_USERNAME:root}
          password: ${DATABASE_PASSWORD:123456}
		slave:
          driver-class-name: ${DATABASE_DRIVER:com.mysql.cj.jdbc.Driver}
          url: jdbc:mysql://10.20.20.135:3308/135master?characterEncoding=utf8&serverTimezone=UTC&zeroDateTimeBehavior=convertToNull&nullCatalogMeansCurrent=true
          username: ${DATABASE_USERNAME:read_only_pde}
          password: ${DATABASE_PASSWORD:123456}
    druid: #druid连接池配置
      initial-size: 10
      max-wait: 10000
      filters: stat
      stat-view-servlet:
        login-password: 888
        login-username: pde
        url-pattern: /druid/*
      filter:
        stat:
          log-slow-sql: true
          slow-sql-millis: 2000

pagehelper:
    #helperDialect: oracle
    pageSizeZero: true
    supportMethodsArguments: true
    params: count=countSql
    # 自动适配数据源的
    auto-dialect: true

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    cache-enabled: false
    call-setters-on-nulls: true #空也会返回
    jdbc-type-for-null: 'null' #null类型解析
    map-underscore-to-camel-case: true  #驼峰命名,在执行查询后，可以将数据库的NN_NN格式字段，在java结果集对象中自动转换成驼峰命名参数

  mapper-locations: classpath*:mapper/**/*Mapper.xml
  global-config:
    banner: false #不显示mybatis-plus LOGO
    db-config:
      id-type: assign_uuid                                    #全局配置，ID生成策略UUID，也可通过属性注解@TableId(type = IdType.ASSIGN_UUID)单独指定
      #field-strategy: not_null                               #字段策略 IGNORED:"忽略判断",NOT_NULL:"非 NULL 判断"),NOT_EMPTY:"非空判断"
```