## 常用软件安装

### tomcat

1. 检索tomact

```bash
docker search tomact
```

![image-20230917211255458](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917211255458.png)

2. 运行

   > 这里使用免修改版本

```bash
docker run -d -p 8080:8080 --name mytomcat8 billygoo/tomcat8-jdk8
```

![image-20230917212327180](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917212327180.png)

3. 查看结果

![image-20230917212458906](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917212458906.png)

![image-20230917225016578](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917225016578.png)

### mysql

> 之前在容器数据卷里面有使用mysql[指令公式](#指令公式)主从复制在[mysql主从复制](#mysql主从复制)



### redis

> redis 集群使用 docker-compose 安装配置

1. 在`/home/docker_compose`添加`docker-compose.yml`文件

```yml
version: '3'
services:
  redis_node_00: 
    image: redis:6.0.8
    container_name: redis_node_00 # 容器名
    command: # 容器内部执行的命令
      [
        "--cluster-enabled","yes", "--appendonly","yes", "--port","6380","--daemonize","NO"
      ]
    volumes: # 容器卷
      - /home/redis_node_00/data:/data
    ports:
      - 6380:6380 # rediis端口
      - 16380:16380 # 集群节点通讯接口在对外设置的端口基础上+10000
    networks:
      - redis_network # 容器网络
  redis_node_01:
    image: redis:6.0.8
    container_name: redis_node_01
    command:
      [
        "--cluster-enabled","yes","--appendonly","yes","--port","6381","--daemonize","NO"
      ]
    volumes:
      - /home/redis_node_01/data:/data
    ports:
      - 6381:6381
      - 16381:16381
    networks:
      - redis_network
  redis_node_02:
    image: redis:6.0.8
    container_name: redis_node_02
    command:
      [
        "--cluster-enabled","yes","--appendonly","yes","--port","6382","--daemonize","NO"
      ]
    volumes:
      - /home/redis_node_02/data:/data
    ports:
      - 6382:6382
      - 16382:16382
    networks:
      - redis_network
  redis_node_03:
    image: redis:6.0.8
    container_name: redis_node_03
    command:
      [
        "--cluster-enabled","yes","--appendonly","yes","--port","6383","--daemonize","NO"
      ]
    volumes:
      - /home/redis_node_03/data:/data
    ports:
      - 6383:6383
      - 16383:16383
    networks:
      - redis_network
  redis_node_04:
    image: redis:6.0.8
    container_name: redis_node_04
    command:
      [
        "--cluster-enabled","yes","--appendonly","yes","--port","6384","--daemonize","NO"
      ]
    volumes:
      - /home/redis_node_04/data:/data
    ports:
      - 6384:6384
      - 16384:16384
    networks:
      - redis_network
  redis_node_05:
    image: redis:6.0.8
    container_name: redis_node_05
    command:
      [
        "--cluster-enabled","yes","--appendonly","yes","--port","6385","--daemonize","NO"
      ]
    volumes:
      - /home/redis_node_05/data:/data
    ports:
      - 6385:6385
      - 16385:16385
    networks:
      - redis_network
networks:
  redis_network:
```

2. 运行

```bash
docker-compose up -d
```

![image-20231030115555728](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231030115555728.png)

3. 进去容器设置集群

```bash
docker exec -it redis_node_00 /bin/bash
redis-cli --cluster create ip:6380 ip:6381 ip:6382 ip:6383 ip:6384 ip:6385 --cluster-replicas 1
```

![image-20231030132328434](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231030132328434.png)

4. 检查集群状态

```bash
redis-cli -c -h ip  -p 6380
```

![image-20231030132247198](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231030132247198.png)

5. 进行set测试

```bash
 redis-cli -c -h ip  -p 6380 # 集群中任意一个redis的端口都可以
```

![image-20231030132624114](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231030132624114.png)

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
