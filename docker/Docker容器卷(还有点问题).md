## 容器卷

### 什么是容器数据卷?

容器数据卷（Container Data Volumes）是一种用于在容器化环境中持久性存储和共享数据的机制。容器数据卷允许容器之间或容器与宿主机之间共享和存储数据，而不受容器的生命周期限制。这些数据卷通常用于在容器之间传递配置文件、日志、数据库文件、应用程序代码等。

以下是容器数据卷的一些关键特点和用途：

1. **持久性存储**：容器数据卷在容器的生命周期内是持久性的，即使容器停止或被删除，数据仍然保留在数据卷中。

2. **共享数据**：多个容器可以共享同一个数据卷，这使得容器之间可以轻松共享数据，例如共享配置信息或共享一个共享的数据库。

3. **数据隔离**：容器数据卷可以用于将容器的数据与宿主机分开，提供了额外的隔离性，保护数据免受容器本身的影响。

4. **挂载点**：容器可以将数据卷挂载到其文件系统中的指定位置，使容器内的应用程序能够访问数据卷中的数据。

5. **备份和恢复**：容器数据卷可以用于进行数据备份，以便在需要时恢复数据。

6. **容器迁移**：容器数据卷使容器在不同主机上迁移变得更加容易，因为数据可以在容器之间共享而不必复制。

**总结:简单来说就是将容器运行时的数据实时映射到宿主机的指定位置,当容器迁移或是删除的时候能够保证数据在宿主记得备份.**

### 作用

将运用与运行的环境打包镜像，run后形成容器实例运行 ，但是我们对数据的要求希望是持久化的 Docker容器产生的数据，如果不备份，那么当容器实例删除后，容器内的数据自然也就没有了。为了能保存数据在docker中我们使用卷。 

**特点：**

1. 数据卷可在容器之间共享或重用数据
2. 卷中的更改可以直接实时生效
3. 数据卷中的更改不会包含在镜像的更新中
4. 数据卷的生命周期一直持续到没有容器使用它为止 

### 使用

> 1 docker修改，主机同步获得 2 主机修改，docker同步获得3 docker容器stop，主机修改，docker容器重启看数据是否同步。

#### **指令公式**

```bash
 docker run -it --privileged=true -v /宿主机绝对路径目录:/容器内目录 镜像名
```

**补充**

> 修改读写规则
>
> 默认读写规则是读写:rw
>
> 容器数据卷的读写规则通常指的是**容器内部**对数据卷的读写权限，而不是宿主机的读写规则。这些规则决定了容器内的应用程序是否能够读取和写入容器数据卷中的内容。

```bash
 docker run -it --privileged=true -v /宿主机绝对路径目录:/容器内目录:ro 镜像名
```

**--privileged=true**

 Docker挂载主机目录访问如果出现cannot open directory .: Permission denied解决办法：在挂载目录后多加一个--privileged=true参数即可 如果是CentOS7安全模块会比之前系统版本加强，不安全的会先禁止，所以目录挂载的情况被默认为不安全的行为，在SELinux里面挂载目录被禁止掉了额，如果要开启，我们一般使用--privileged=true命令，扩大容器的权限解决挂载目录没有权限的问题，也即使用该参数，container内的root拥有真正的root权限，否则，container内的root只是外部的一个普通用户权限。

**示例**

```bash
docker run -dp 3307:3306 \
  -v /home/pde/data:/var/lib/mysql \
  -v /home/pde/config:/etc/mysql \
  -v /home/pde/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --name mysql2 mysql:5.7.36
```

创建成功!

![image-20230917171325387](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917171325387.png)

#### 查看挂载状态

指令公式

```json
 docker inspect 容器ID  
```

![image-20230917173053587](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917173053587.png)

#### 卷的继承和共享

挂在到卷到容器二(效果和java的继承差不多)

```bash
docker run -it  --privileged=true --volumes-from 父类容器ID
```

1. 我们在之前的数据库添加上数据

![image-20230917224911767](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917224911767.png)

2. 创建新的数据库继承他的数据卷

```bash
docker run -dp 3307:3306 \
  --volumes-from 70d867f7695d \
  -e MYSQL_ROOT_PASSWORD=123456 \
  --name mysql2 mysql:5.7.36
```

![image-20230917173913959](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917173913959.png)

3. 连接上新的数据库查看我们的数据是否存在

**容器启动失败!!!,暂时搁浅**

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
