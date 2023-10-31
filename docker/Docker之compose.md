

## Docker-compose

​	Docker Compose是一个工具，可以使用一个单独的yaml文件来定义和运行多个Docker容器。它可以轻松地定义和运行复杂的应用程序，而无需手动创建和管理每个容器。使用Docker Compose，可以定义容器之间的依赖关系、网络设置、数据卷挂载等。它还可以在单个命令中启动、停止、重启和删除整个应用程序。Docker Compose是一个非常有用的工具，特别适用于开发人员、测试人员和部署人员。

​	Docker 建议我们每一个容器中只运行一个服务,因为 Docker 容器本身占用资源极少,所以最好是将每个服务单独的分割开来但是这样我们又面临了一个问题？ 如果我需要同时部署好多个服务,难道要每个服务单独写 Dockerfile 然后在构建镜像,构建容器,这样累都累死了,所以 Docker 官方给我们提供了 docker-compose 多服务部署的工具 例如要实现一个 Web 微服务项目，除了 Web 服务容器本身，往往还需要再加上后端的数据库mysql服务容器，redis服务器，注册中心eureka，甚至还包括负载均衡容器等等。 Compose 允许用户通过一个单独的`docker-compose.yml`模板文件（YAML 格式）来定义一组相关联的应用容器为一个项目（project）。 可以很容易地用一个配置文件定义一个多容器的应用，然后使用一条指令安装这个应用的所有依赖，完成构建。Docker-Compose 解决了容器与容器之间如何管理编排的问题。

### 准备

1. 安装

> 安装docker-compose 如果有问题 删除 之前下载的`docker-compose.yml`文件,
>
> 到git上下载放到`/usr/local/bin/docker-compose`下面,演示中使用的是 `v2.18.1`版本
>
> [Releases · docker-compose git地址](https://github.com/docker/compose/releases)
>
> [官网安装教程](https://docs.docker.com/compose/install/linux/#install-the-plugin-manually)

```bash
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

2. 验证

```bash
docker-compose version
```

![image-20231030133850447](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231030133850447.png)

3. 想要卸载的话把`/usr/local/bin/docker-compose`那个文件删掉就可以了

### 使用

>  这就是基础的使用,如果需要更复杂的,看一下[官网教程文档](https://docs.docker.com/compose/compose-file/compose-file-v3/)

1. 随便找一个文件夹创建一个`docker-compose.yml`文件

   ```yml
   version: "3"  # Docker Compose文件的版本
   
   services:  # 定义服务列表
     redis:  # 第一个服务名为redis
       image: redis:6.0.8  # 使用Redis 6.0.8镜像
       ports:
         - "6379:6379"  # 将主机的6379端口映射到容器的6379端口
       volumes:
         - /home/redis/redis.conf:/etc/redis/redis.conf  # 挂载Redis配置文件
         - /home/redis/data:/data  # 挂载Redis数据目录
       networks:
         - atguigu_net  # 使用名为atguigu_net的自定义网络
       command: redis-server /etc/redis/redis.conf  # 启动Redis时使用指定配置文件
   
     mysql:  # 第二个服务名为mysql
       image: mysql:5.7.36  # 使用MySQL 5.7.36镜像
       environment:
         MYSQL_ROOT_PASSWORD: '123456'  # 设置MySQL的root用户密码
       ports:
         - "3306:3306"  # 将主机的3306端口映射到容器的3306端口
       volumes:
         - /home/master/data:/var/lib/mysql  # 挂载MySQL数据目录
         - /home/master/config:/etc/mysql  # 挂载MySQL配置目录
         - /home/master/log:/var/log/mysql  # 挂载MySQL日志目录
       networks:
         - atguigu_net  # 使用名为atguigu_net的自定义网络
       command: --default-authentication-plugin=mysql_native_password # 解决外部无法访问的认证插件问题
   
   networks:
     atguigu_net:  # 定义名为atguigu_net的自定义网络
   
   ```

2. 运行文档

   ```bash
   docker-compose up
   ```

   ![image-20231031102227956](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031102227956.png)

   运行完ps检查一下,完美执行成功!!

### 常用指令

当使用Docker Compose时，以下是常用指令的表格形式展示：

| **指令**                                                    | **描述**                                     |
| ----------------------------------------------------------- | -------------------------------------------- |
| `docker-compose up`                                         | 启动服务，创建并启动容器。                   |
| `docker-compose up -d`                                      | 在后台模式启动服务。                         |
| `docker-compose down`                                       | 停止并移除所有服务容器、网络、图像和存储卷。 |
| `docker-compose down -v`                                    | 同时移除存储卷。                             |
| `docker-compose build`                                      | 构建或重新构建服务。                         |
| `docker-compose build <service-name>`                       | 仅构建特定服务。                             |
| `docker-compose ps`                                         | 显示服务容器的状态。                         |
| `docker-compose logs`                                       | 查看服务的日志输出。                         |
| `docker-compose logs <service-name>`                        | 查看特定服务的日志。                         |
| `docker-compose exec <service-name> <command>`              | 在特定服务容器内执行命令。                   |
| `docker-compose stop`                                       | 停止所有服务容器，但不移除它们。             |
| `docker-compose start`                                      | 启动已经停止的服务容器。                     |
| `docker-compose restart`                                    | 重启所有或指定的服务容器。                   |
| `docker-compose restart <service-name>`                     | 重启特定服务。                               |
| `docker-compose pull`                                       | 从远程仓库拉取服务的最新镜像。               |
| `docker-compose pause`                                      | 暂停所有服务容器的执行。                     |
| `docker-compose unpause`                                    | 恢复所有服务容器的执行。                     |
| `docker-compose top`                                        | 显示所有服务容器中运行的进程。               |
| `docker-compose config`                                     | 验证并查看`docker-compose.yml`文件的配置。   |
| `docker-compose scale <service-name>=<number-of-instances>` | 缩放服务的实例数量。                         |
| `docker-compose version`                                    | 显示Docker Compose的版本信息。               |

请注意，这些指令的使用可能会依赖于您的具体应用和环境，确保在使用时参考官方文档或使用`docker-compose --help`来获取详细的命令说明和参数信息。

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
