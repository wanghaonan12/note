

## Docker 镜像操作

镜像拉取咱就不多说了哈,上面的基础使用中的,镜像**搜索**,**拉取**啥的都有.咱就直接从镜像的制作开始.

### 镜像制作commit

> 介绍一下为什么要制作镜像,首先比如基础镜像 Linux 在拉取下来之后他只有基础功能,vim等操作都不可以.这个时候我们就要自己去安装需要的插件,但是当换台机器的时候我们还要去重复操作,这样就不是很灵活,也失去 Docker 的最大作用和意义.
> 在我们配置完需要的操作之后我们就可以将那个容器制作成镜像,导出放到其他地方使用.这样就实现了,不搬家,直接建造一个小区的效果.把之前配置的环境等都复制过去.

**语法**

注意:镜像名称中不可以有大写字符,在我下面的操作中就展示了是用大写字符后,Docker 会给出提示必须时小写字符.

```bash 
docker commit -m="描述" -a="作者信息" [containerId] ImageName:tag
```

![image-20230911142629117](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911142629117.png)

### 镜像导出

**语法**

```bash
docker save -o 路径及名称 [imagesId]
```

案例中是将镜像id为[bf492359edee]的存储到home路径下名称为 **custom_mysql:1.0.0.tar**

![image-20230911145828944](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911145828944.png)

### 镜像导入

**语法**

```bash
docker load -i 镜像地址
```

![image-20230911151202201](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911151202201.png)

在上途中共有四步操作:

1. 从本地导入镜像:导入之前准备好的镜像包
2. 查看导入镜像:导入之后我们会发现导入的镜像是一个虚悬镜像,我们要修改他的标签
3. 镜像标签修改
4. 查看镜像标签

#### 修改标签

```bash
docker tag 镜像id 设置的镜像名:镜像标签
```

![image-20230916173445420](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916173445420.png)

### 上传到阿里云

这个就算了吧,和 GitHub 差不多,主要是大陆的比较慢,主要是我懒得去注册 DockerHub.

本章结束!!!!

### 上传到本地仓库

1. 安装运行registry

   ```bash
    docker run -d -p 5000:5000 -v /home/registry:/var/lib/registry --privileged=true --name registry registry
   ```

![image-20230911161526794](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911161526794.png)

**--privileged=true**在 docker run 命令中表示给容器提供所有Linux内核权限。

具体来说:

- 普通容器运行时,它不具备访问宿主机的设备和资源的能力,这保证了容器的安全隔离性。
- 但是有些容器需要访问宿主机的设备,如访问GPU、磁盘等,这时需要使用--privileged=true。
- 当给容器添加**--privileged=true**参数后,它将拥有所有的Linux内核能力,可以访问宿主机的任何设备和资源。
- 这意味着这个容器具有“超级用户”的权限,它可以执行任何命令,包括格式化磁盘等危险操作。

所以使用**--privileged**需要谨慎,主要应用于:

- 容器需要访问宿主机的GPU/磁盘等设备
- 容器需要管理宿主机的网络(如Docker in Docker)
- 容器需要测试需要root权限的应用

一般不建议给普通应用容器添加**--privileged**,因为它破坏了容器的安全隔离设计。只在必要时使用,同时要加强对容器的监控和访问控制。

总之**--privileged=true**表示给容器提供所有Linux内核级别的访问权限,这将严重影响容器的安全性,需要谨慎使用。
**其他参数就不介绍了啊**

2. 查看是否连接正常

   ```八十九\
   自己的ip:5000/v2/_catalog
   ```

   ![image-20230911162319384](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911162319384.png)	
   
   可以看到上面是空的没有任何镜像数据!!

3. 修改配置文件支持http协议

> docker 私服库默认不支持http请求,我们需要修改配置取消限制
> 修改文件etc/docker/daemon.json
> 之前在配置镜像加速的时候创建的文件

修改前

```json
{
    "registry-mirrors": [
        "https://824wohwf.mirror.aliyuncs.com"
    ]
}
```

修改后

> 将我们的地址添加进去

```json
{
    "registry-mirrors": [
        "https://824wohwf.mirror.aliyuncs.com"
    ],
    "insecure-registries": [
        "宿主机id:5000"
    ]
}
```

![image-20230916183202599](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916183202599.png)

如果改了之后不生效重启动一下!!

4. 修改镜像命名规范

   > 按照规范命名仓库地址/镜像名:镜像标签

   ```bash
   docker tag upload_image:1.0.0  宿主机ID:5000/upload_image:1.0.0
   ```

   将`upload_image:1.0.0 `改为`宿主机ID:5000/upload_image:1.0.0`

   ![image-20230917224635030](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917224635030.png)

   

5. 推送代码

```bash
docker push 宿主机ID:5000/upload_image:1.0.0
```

docker push 镜像名称:镜像id

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917224635030.png)

完成之后就好了,检验一下

![image-20230916185500775](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916185500775.png)

![image-20230917224813396](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230917224813396.png)

1. 移除本地镜像
2. 查看是否删除
3. 拉取远程仓库的镜像
4. 检查是否拉取下来

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
