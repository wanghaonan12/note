# Docker保姆及教程

## 简介

### 为什么会出现Docker？

场景：

> 您使用的是一台笔记本电脑而且您的开发环境具有特定的配置。其他开发人员身处的环境配置也各有不同。您正在开发的应用依赖于您当前的配置且还要依赖于某些配置文件。此外，您的企业还拥有标准化的测试和生产环境，且具有自身的配置和一系列支持文件。您希望尽可能多在本地模拟这些环境而不产生重新创建服务器环境的开销。请问？您要如何确保应用能够在这些环境中运行和通过质量检测？并且在部署过程中不出现令人头疼的版本、配置问题，也无需重新编写代码和进行故障修复？
>
> 总结：就是你开发的环境和运行环境不一定相同，可能会导致各种各样的版本兼容问题。

解决：

>  答案就是使用容器。Docker之所以发展如此迅速，也是因为它对此给出了一个标准化的解决方案-----系统平滑移植，容器虚拟化技术。 环境配置相当麻烦，换一台机器，就要重来一次，费力费时。很多人想到，能不能从根本上解决问题，软件可以带环境安装？也就是说，安装的时候，把原始环境一模一样地复制过来。开发人员利用 Docker 可以消除协作编码时“在我的机器上可正常工作”的问题。 
>
> Docker的出现使得Docker得以打破过去「程序即应用」的观念。透过镜像(images)将作业系统核心除外，运作应用程式所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作。
>
> 总结：就是使用Docker将你的环境使用的配置全部打包镜像（image）带走，这样就不会出现环境迁移版本配置不一致等问题了。**就像是搬到新小区你会对周围环境不熟悉，最简单的办法就是直接在你要去的地方直接复制一个一摸一样的小区和人物等，这样你就不会感觉到陌生了。**

### 什么是Docker

解决了运行环境和配置问题的软件容器，方便做持续集成并有助于整体发布的容器虚拟化技术。

### Docker能干吗？

方便了开发/运维/测试的环境部署等工作，实现一次构建，随处运行。

### Docker官网

[Docker官网](http://www.docker.com)

[Docker Hub仓库官网](https://hub.docker.com/)

## 基础使用

### 使用说明

Docker 安装前提条件目前，CentOS 仅发行版本中的内核支持 Docker。Docker 运行在CentOS 7 (64-bit)上，要求系统为64位、Linux系统内核版本为 3.8以上。

### 安装步骤

[Docker 官网安装教程](https://docs.docker.com/engine/install/centos/)笔记使用CentOS系统

1. 安装 yum-utils(配置远程仓库小工具)

```shell
yum install -y yum-utils
```

![image-20230820212319447](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820212319447.png)

2. 配置远程仓库

> 使用自己的镜像地址，使用官网镜像地址会超时

```
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```

![image-20230820212932857](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820212932857.png)

1-2对应官网教程

![image-20230820213546852](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820213546852.png)

3. 更新一下yum包索引(官网没有建议执行)

> 作用：将软件包信息提前在本地索引缓存，用来提高搜索安装软件的速度,建议执行这个命令可以提升yum安装的速度。

![image-20230820213259844](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820213259844.png)

4. 安装docker引擎

```shell
yum -y install docker-ce docker-ce-cli containerd.io
```

![image-20230820213453913](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820213453913.png)

安装最新版官网教程

![image-20230820213625358](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820213625358.png)

5. 启动docker

```shell
systemctl start docker
```

![image-20230820213946356](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820213946356.png)

6. 检查docker

```shell
docker version
```

![image-20230820214015803](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820214015803.png)

ok！安装成功啦！

7. 进入 hello word环节

> 创建hello-word容器，本地没有hello-word镜像 会自动去仓库拉取

```shell
docker run hello-world
```

![image-20230820214451230](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820214451230.png)

### docker镜像加速

1. 登陆阿里云开发者账户

[阿里云](https://www.aliyun.com/)

![image-20230916180501436](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916180501436.png)

2. 选择容器镜像服务

- 控制台->左上角的三横-> 容器镜像服务

![image-20230916181002275](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916181002275.png)

- 选择镜像工具->镜像加速器

> **注意自己的操作系统**



![image-20230916181258629](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916181258629.png)

```bash
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://824wohwf.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

- 复制代码执行就可以了

![image-20230916181533422](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916181533422.png)

![image-20230916181610087](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916181610087.png)

就配置好了!!!

### Docker卸载

刚装好就要卸载，苦瓜脸，叹气~

看官网教程

![image-20230820214658474](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820214658474.png)

1. 停止docker（官网上没写）

```shell
systemctl stop docker.socket
```

![image-20230820215002196](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820215002196.png)

2. 移除Docker

```shell
sudo yum remove docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin docker-ce-rootless-extras
```

![image-20230820215217308](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820215217308.png)

3. 移除残留

```shell
 sudo rm -rf /var/lib/docker
 sudo rm -rf /var/lib/containerd
```

![image-20230820215232163](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820215232163.png)

> 删除完毕！残留也清理了，完成！我在给下回来，继续做笔记（这怎么没有表情包啊!岂可休！）

### docker启动相关命令

> 这就不做截图了，没有意义

1. 启动docker

```shell
systemctl start docker
```

2. 停止docker

```shell
 systemctl stop docker
```

3. 重启docker

```shell
systemctl restart docker
```

4. 查看dcker状态

```shell
systemctl status docker
```

5. 开机启动docker

```shell
systemctl enable docker
```

6. 查看docker帮助文档（好东西）

```shell
docker --help
```

7. 查看docker具体命令语法（也是好东西）

```shell
docker 具体命令 --help
```

![image-20230820220055712](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230820220055712.png)

给出的提示`docker run [OPTIONS] IMAGE [COMMAND] [ARG...]`

[OPTIONS] ：可选项，就是下面给了好多的`-a`啥的在`Opitions：`分支下面，后也有作用解释，自行百度就可以了后面我们也会接触到

IMAGE ：镜像名或镜像ID

[COMMAND]：指定了在容器内执行的命令

> 当运行一个容器时,docker会执行容器内指定的ENTRYPOINT或CMD指令。如果在docker run命令中也指定了COMMAND,那么COMMAND会覆盖容器内的ENTRYPOINT或CMD指令来执行。
>
> 
>
> - 一个镜像的Dockerfile中指定了ENTRYPOINT ["top"]
> - 那么运行这个镜像时:
>   - docker run 镜像ID,top命令会执行
>   - docker run 镜像ID,ps命令,那么ps命令会覆盖top,执行ps命令
>
> 
>
> 这个会在学完dockerFile之后了解

[ARG...]：参数表示要传入容器内的命令参数。

> ARG参数会将参数传给容器内部指定的ENTRYPOINT或CMD命令。
>
> 
>
> 例如:
>
> 如果一个镜像的Dockerfile中定义了:
>
> ENTRYPOINT ["myapp", "arg1", "arg2"]
>
> 那么运行这个镜像时:
>
> - docker run 镜像名,参数1 参数2
>
> 这时参数1和参数2会作为ARG传给ENTRYPOINT指定的myapp命令,myapp命令会接收到参数1和参数2作为参数。
>
> 
>
> 这个也会在学完dockerFile之后了解

### docker常用命令

1. docker info: 用于查看Docker的系统信息，包括镜像和容器的数量。

示例：`docker info`

![image-20230826110909592](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826110909592.png)

2. docker images: 用于查看本地主机上的所有Docker镜像。

> options:说明  `-a` :显示所有镜像  `-q`:只显示镜像id
>
> 当然也可以一起使用`-aq`

示例：`docker images -aq`

![image-20230826152938192](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826152938192.png)

3. docker search: 用于在Docker Hub或私有仓库中搜索镜像。

> options：说明`--limit`这个和mysql中的limit一样 只显示n个镜像

示例：`docker search --limit 5 nginx`

![image-20230826152829048](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826152829048.png)

4. docker pull: 用于拉取Docker镜像。

示例：`docker pull nginx:latest`

![image-20230826152807987](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826152807987.png)

5. docker rm: 用于删除Docker镜像。

示例：`docker rm nginx`

6. docker rmi: 用于删除未使用的Docker镜像。

示例：`docker rmi nginx:latest`

7. docker rmi -f: 用于强制删除Docker镜像。

示例：`docker rmi -f nginx`

8. docker run: 用于在Docker容器中运行应用程序或命令。

> 在本地没有检索到镜像时会自动去docker 仓库拉取

示例：`docker run nginx`

![image-20230826153326206](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826153326206.png)

> 其中`-e` 是mysql中的内部命令，是`environment`的简写设置参数使用。
>
> `-dp`：是`-d`和`-p`的简写指的是后台守护式运行和端口映射
>
> `--name`：是定义容器名字 
>
> 最后的mysql是运行的镜像名

9. docker exec: 用于在运行的Docker容器中执行命令。

示例：`docker exec nginx ps aux`

![image-20230826154228090](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826154228090.png)

10. docker stop: 用于停止正在运行的Docker容器。

示例：`docker stop nginx`

![image-20230826155316012](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155316012.png)

11. docker start nginx ：启动容器。

示例：`docker start nginx`

![image-20230826155519175](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155519175.png)

12. docker rm: 用于删除已停止的Docker容器。

示例：`docker rm nginx`

13. docker kill: 用于强制停止正在运行的Docker容器。

示例：`docker kill nginx`

![image-20230826155533331](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155533331.png)

14. docker logs: 用于查看正在运行的Docker容器的日志。

示例：`docker logs nginx`

![image-20230826155641529](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155641529.png)

15. docker commit: 用于将正在运行的Docker容器保存为新的镜像。

示例：`docker commit nginx my-image:1.0`

16. docker ps: 用于查看正在运行的Docker容器的列表。

示例：`docker ps`

![image-20230826155831164](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155831164.png)

17. docker top: 用于查看正在运行的Docker容器的进程列表。

示例：`docker top nginx`

![image-20230826155919246](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826155919246.png)

18. docker exec -it: 用于在指定的Docker容器中运行命令。

示例：`docker exec -it nginx /bin/bash`

![image-20230826153948008](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826153948008.png)

19. docker exec -it -u <username> : 用于在指定的Docker容器中以特定用户身份运行命令。

示例：`docker exec -it -u root nginx /bin/bash`

![image-20230826160246573](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230826160246573.png)



---

> 看完上面的这些命令应该可以对 docker 进行基础使用了，在文件最后会添加所有的 docker help 的翻译。

### 知识补充

#### docker镜像是什么？

镜像是一种轻量级、可执行的独立软件包，它包含运行某个软件所需的所有内容，我们把应用程序和配置依赖打包好形成一个可交付的运行环境(包括代码、运行时需要的库、环境变量和配置文件等)，这个打包好的运行环境就是image镜像文件。 只有通过这个镜像文件才能生成Docker容器实例(类似Java中new出来一个对象)。  

#### docker虚悬镜像是什么？

仓库名、标签都是<none>的镜像，俗称虚悬镜像dangling image

![image-20230911140630628](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230911140630628.png)

#### UnionFS（联合文件系统）？

![联合文件系统](https://img-blog.csdnimg.cn/200cecd38673471f84da80e3eafbd7f8.png)

(网图，反正就是一层一层的这个样子，上层可以继承下层所用功能)

 以我们的pull为例，在下载的过程中我们可以看到docker的镜像好像是在一层一层的在下载  

UnionFS（联合文件系统）：Union文件系统（UnionFS）是一种分层、轻量级并且高性能的文件系统，它支持对文件系统的修改作为一次提交来一层层的叠加，同时可以将不同目录挂载到同一个虚拟文件系统下(unite several directories into a single virtual filesystem)。Union 文件系统是 Docker 镜像的基础。镜像可以通过分层来进行继承，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像。 特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录

 镜像分层最大的一个好处就是共享资源，方便复制迁移，就是为了复用。 比如说有多个镜像都从相同的 base 镜像构建而来，那么 Docker Host 只需在磁盘上保存一份 base 镜像；同时内存中也只需加载一份 base 镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享。 

**重点：**

Docker镜像层都是只读的，容器层是可写的
当容器启动时，一个新的可写层被加载到镜像的顶部。
这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。

![联合文件系统](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/200cecd38673471f84da80e3eafbd7f8.png)

当容器启动时，一个新的可写层被加载到镜像的顶部。这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。所有对容器的改动 - 无论添加、删除、还是修改文件都只会发生在容器层中。只有容器层是可写的，容器层下面的所有镜像层都是只读的。

#### Docker加载原理

以Docker 安装 Linux 系统为例子. Docker镜像加载原理：  docker的镜像实际上由一层一层的文件系统组成，这种层级的文件系统UnionFS。bootfs(boot file system)主要包含bootloader和kernel, bootloader主要是引导加载kernel, Linux刚启动时会加载bootfs文件系统，在Docker镜像的最底层是引导文件系统bootfs。这一层与我们典型的Linux/Unix系统是一样的，包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由bootfs转交给内核，此时系统也会卸载bootfs。 rootfs (root file system) ，在bootfs之上。包含的就是典型 Linux 系统中的 /dev, /proc, /bin, /etc 等标准目录和文件。rootfs就是各种不同的操作系统发行版，比如Ubuntu，Centos等等 。 平时我们安装进虚拟机的CentOS都是好几个G，为什么docker这里才200M？？对于一个精简的OS，rootfs可以很小，只需要包括最基本的命令、工具和程序库就可以了，因为底层直接用Host的kernel，自己只需要提供 rootfs 就行了。由此可见对于不同的linux发行版, bootfs基本是一致的, rootfs会有差别, 因此不同的发行版可以公用bootfs。

- bootloader: 引导加载程序,它是计算机系统启动的第一个程序。bootloader的主要任务是加载并启动操作系统的内核程序。它读取内核映像文件,将内核加载到内存中并转交控制权给内核,使系统能够继续启动。负责启动内核,它是系统启动的第一阶段
- kernel: 操作系统内核,它是操作系统的核心。内核负责管理和调度系统的资源,例如处理器、内存、设备等。内核还提供抽象层,管理进程和线程的调度,管理文件系统和网络连接等。内核是操作系统的核心,负责管理系统资源和进程,可以看作是系统启动的第二阶段。
- bootfs:引导文件系统,它包含在引导加载程序(bootloader)中,通常很小,只包含启动所必需的最小文件,例如内核映像等。
- rootfs:根文件系统,也称为根分区。它包含操作系统所有其他需要的文件,例如系统命令、库文件、设备节点等。
  - 位置:bootfs存储在boot分区中,rootfs存储在根分区中。
  - 作用:bootfs仅用于引导,加载内核并切换到rootfs。rootfs包含完整的操作系统文件系统结构。

就是能不要的都不要了,就要最基础的一个linux系统,你需要什么就自己装.

#### 为什么Docker镜像要分层呢?

 镜像分层最大的一个好处就是共享资源，方便复制迁移，就是为了复用。 比如说有多个镜像都从相同的 base 镜像构建而来，那么 Docker Host 只需在磁盘上保存一份 base 镜像；同时内存中也只需加载一份 base 镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享。 

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

> 1 docker修改，主机同步获得
>
>  2 主机修改，docker同步获得
>
> 3 docker容器stop，主机修改，docker容器重启看数据是否同步。

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

数据库需要保证数据的安全性所以容器卷不支持共享,这里换成`ubantu`或是`centOS`的镜像就不会出想问题,所以我门使用`tomcat`测试,哈哈哈!!!

1. 运行tomcat8

```bash
 docker run -d -p 8087:8080 -v /home/tomcatdata:/usr/local/tomcat/webapps/ROOT/index.html --name tomcat8 billygoo/tomcat8-jdk8
```

2. `/home/tomcatdata`添加文件

   ![image-20231031155759465](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031155759465.png)

3. 运行tomcat8_1

   ```bash
   docker run -d -p 8089:8080 --volumes-from tomcat8 --name tomcat8_1 billygoo/tomcat8-jdk8
   ```

4. 检查两个容器中的文件

   > 修改后两个地方同时修改

   ![image-20231031160015949](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031160015949.png)

## Docker复杂安装

### mysql主从复制

1. 启动容器挂载数据卷

```bash
docker run -dp 3307:3306 \
  -v /home/master/data:/var/lib/mysql \
  -v /home/master/config:/etc/mysql \
  -v /home/master/log:/var/log/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
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



## Dockerfile

[Dockerfile 官网文档](https://docs.docker.com/engine/reference/builder/)

> Dockerfile是用来构建Docker镜像的文本文件，是由一条条构建镜像所需的指令和参数构成的脚本。

 从应用软件的角度来看，Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段，

**Dockerfile是软件的原材料**,***Docker镜像是软件的交付品*** ,**Docker容器则可以认为是软件镜像的运行态**，也即依照镜像运行的容器实例Dockerfile面向开发，Docker镜像成为交付标准，Docker容器则涉及部署与运维，三者缺一不可，合力充当Docker体系的基石。1 Dockerfile，需要定义一个Dockerfile，Dockerfile定义了进程需要的一切东西。Dockerfile涉及的内容包括执行代码或者是文件、环境变量、依赖包、运行时环境、动态链接库、操作系统的发行版、服务进程和内核进程(当应用进程需要和系统服务和内核进程打交道，这时需要考虑如何设计namespace的权限控制)等等; 2 Docker镜像，在用Dockerfile定义一个文件之后，docker build时会产生一个Docker镜像，当运行 Docker镜像时会真正开始提供服务; 3 Docker容器，容器是直接提供服务的。  

### 基础知识

1. 每条保留字指令都必须为大写字母且后面要跟随至少一个参数(`FROM`就是保留字作用和java中的关键字一样)
2. 指令按照从上到下顺序执行
3. `#`表示注释
4. 每条指令都会创建一个新的镜像层并进行提交
5. docker从基础镜像运行一个容器(`FROM`就是基础镜像)
6. 执行一条指令并对容器进行修改(之前说的联合文件系统)
7. 执行类似docker commit的操作提交一个新的镜像
8. docker 在基于刚提交的镜像运行一个新容器
9. 执行dockerfile中的吓一跳指令直到所有指令都执行完毕

![image-20231018100037368](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231018100037368.png)

当编写Dockerfile时，常用的指令可以整理成如下的表格，包括指令、作用、示例和解释：

| **指令**  | **作用**                     | **示例**                                         | **解释**                                              |
| --------- | ---------------------------- | ------------------------------------------------ | ----------------------------------------------------- |
| `FROM`    | 指定基础镜像                 | `FROM ubuntu:latest`                             | 选择Ubuntu最新版本作为基础镜像。                      |
| `WORKDIR` | 设置工作目录                 | `WORKDIR /app`                                   | 在容器中创建/app目录，所有后续指令在此目录下执行。    |
| `COPY`    | 复制文件或目录到容器中       | `COPY . /app`                                    | 将主机上的所有文件复制到容器的/app目录下。            |
| `RUN`     | 在容器内部执行命令           | `RUN apt-get update && apt-get install -y nginx` | 在容器内部运行更新和安装Nginx的命令。                 |
| `ENV`     | 设置环境变量                 | `ENV NODE_ENV=production`                        | 设置环境变量NODE_ENV的值为production。                |
| `EXPOSE`  | 声明容器监听的网络端口       | `EXPOSE 80`                                      | 声明容器将监听80端口。                                |
| `CMD`     | 指定容器启动时默认执行的命令 | `CMD ["nginx", "-g", "daemon off;"]`             | 定义容器启动时的默认命令，运行Nginx并以前台模式启动。 |

其中`RUN`有两种语法格式分别是`shell`和`exec`

​	在编写Dockerfile时，可以根据需要选择合适的格式。如果需要使用 Shell 特性，或者有多个命令需要串联执行，可以选择 Shell 形式。如果追求性能，或者希望更清晰地了解指令的执行情况，可以选择 Exec 形式。

**Shell**

​	在 Shell 形式下，`RUN` 指令会直接在 `/bin/sh -c` 中执行。这种格式可以使用通常在 Shell 中可用的各种特性，例如管道操作、变量扩展等。在Shell形式中，多个命令可以使用 `&&` 连接，表示命令串联执行。

```bash
RUN <命令>
RUN <命令> && <命令>
```

示例

```bash
RUN apt-get update && apt-get install -y nginx
RUN echo "Hello, World!" > /app/index.html
```

**exec**

​	在 Exec 形式下，`RUN` 指令会直接在不启动 shell 的情况下执行。这种格式使用 JSON 数组表示，命令及其参数作为独立的元素。Exec 形式通常更高效，因为不启动额外的 shell 进程。

```bash
RUN ["可执行文件", "参数1", "参数2"]
```

示例

```bash
RUN ["apt-get", "update", "&&", "apt-get", "install", "-y", "nginx"]
RUN ["echo", "Hello, World!", ">", "/app/index.html"]
```

以以下Dockerfile为例：

```bash
FROM ubuntu:latest
WORKDIR /app
COPY . /app
RUN apt-get update && apt-get install -y nginx
ENV NODE_ENV=production
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

- `FROM ubuntu:latest`：选择Ubuntu最新版本作为基础镜像。
- `WORKDIR /app`：在容器中创建/app目录，所有后续指令都将在此目录下执行。
- `COPY . /app`：将主机上的所有文件复制到容器的/app目录下。
- `RUN apt-get update && apt-get install -y nginx`：在容器内部运行更新和安装Nginx的命令。
- `ENV NODE_ENV=production`：设置环境变量NODE_ENV的值为production。
- `EXPOSE 80`：声明容器将监听80端口。
- `CMD ["nginx", "-g", "daemon off;"]`：定义容器启动时的默认命令，运行Nginx并以前台模式启动。

### 构建镜像

目标:将服务jar制作成镜像

![image-20231031145949484](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031145949484.png)

1. 编写Dockerfile

> 暴露的端口为服务的端口,在制作镜像之后的宿主机映射的端口可以随意

```bash
# 基础镜像使用java
FROM openjdk:8
# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中 注意这里的jar位置是在当前dockerfile的同目录
ADD springboot.jar springboot.jar
# 运行jar包
RUN bash -c 'touch /springboot.jar'
ENTRYPOINT ["java","-jar","/springboot.jar"]
#暴露8888端口作为微服务
EXPOSE 8888  
```

2. 上传jar包

   [jar包下载](https://github.com/ab128118-3c3c-4ca5-9e7f-c510a47ac249)

   ![image-20231031151314549](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031151314549.png)

3. 制作镜像

   ```bash
   docker build -t springboot:2.4 . 
   ```

   `-t` :设置镜像名和标签Name and optionally a tag (format: "name:tag")

   `. `: 是Dockerfile的位置

   <img src="https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031151952352.png" alt="image-20231031151952352" style="zoom:50%;" />

   **查看镜像**

   ![image-20231031151332436](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031151332436.png)

4. 运行镜像

   ```bash
   docker run -d -p 8888:8888 --name spring springboot:2.4
   ```

5. 测试

![image-20231031151902217](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031151902217.png)

## Docker微服务

## Docker网络

Docker网络（Docker Network）是Docker提供的一种用于容器间通信和容器与外部网络通信的网络方案。Docker网络允许你创建、管理和连接多个Docker容器，提供了更灵活、可扩展的网络架构。

### 1. **Docker网络的作用**

- **容器间通信**：Docker网络允许不同容器之间进行网络通信，使得容器之间可以方便地交换数据和信息。
  
- **容器与外部网络通信**：Docker网络可以将容器连接到外部网络，使得容器可以与外部世界进行通信，例如访问互联网或局域网资源。

### 2. **三种Docker网络类型的区别**

Docker提供了三种基本的网络类型：

- **Bridge Network（桥接网络）**：
  - **作用**：默认的网络模式，容器通过网络桥接方式与主机相连，容器间互相隔离，但可以与主机和外部网络通信。
  - **使用场景**：适用于多个容器需要在同一个宿主机上运行，相互隔离，但需要与外部通信的情况。
  - **使用方式**：使用 `docker network create` 命令创建桥接网络，然后将容器连接到该网络。

- **Host Network（主机网络）**：
  - **作用**：容器直接使用宿主机的网络命名空间，与宿主机网络共享同一网络接口，容器间无网络隔离。
  - **使用场景**：适用于对网络性能要求较高、不需要容器间隔离的场景。
  - **使用方式**：使用 `--network host` 选项启动容器。

- **Overlay Network（覆盖网络）**：
  - **作用**：允许不同Docker守护进程间的容器进行通信，适用于跨主机的容器通信，提供多主机上的容器互联。
  - **使用场景**：适用于分布式应用，跨多个主机运行的场景。
  - **使用方式**：使用 `docker network create` 命令创建覆盖网络，然后将容器连接到该网络。

### 3. **使用Docker网络的基本操作**

- **创建网络**：使用 `docker network create` 命令创建新的Docker网络。

  ```
  docker network create mynetwork
  ```

- **查看网络**：使用 `docker network ls` 命令查看所有网络。

  ```
  docker network ls
  ```

- **将容器连接到网络**：在启动容器时使用 `--network` 选项将容器连接到指定的网络。

  ```
  docker run --network mynetwork mycontainer
  ```

- **断开容器与网络的连接**：使用 `docker network disconnect` 命令将容器与网络断开连接。

  ```
  docker network disconnect mynetwork mycontainer
  ```

以上操作示例展示了Docker网络的基本用法。根据具体场景，可以选择合适的网络类型，使得容器能够在不同的网络环境中进行通信和交互。

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



## Docker容器监控-CAdvisor+InfluxDB+Granfana

​	Docker容器监控（Container Monitoring）是一种追踪、测量和分析Docker容器运行时性能和行为的方法。在Docker容器监控中，通常使用一组工具来收集和可视化容器的指标数据，以便及时发现问题、优化性能和规划资源。

1. **CAdvisor（容器监控工具）**

​	CAdvisor是由Google开发的开源容器监控工具，它能够自动收集Docker容器的性能指标数据，如CPU使用率、内存使用量、网络流量等，并将这些数据提供给用户。

2. **InfluxDB（时序数据库）**

​	InfluxDB是一个开源的时序数据库，特别适用于存储时间序列数据。在Docker容器监控中，InfluxDB用于存储CAdvisor收集到的容器指标数据。

3. **Grafana（可视化工具）**

​	Grafana是一个开源的数据可视化和监控平台，它可以连接各种数据源（包括InfluxDB），并将数据以图表、仪表盘的形式展示出来。在Docker容器监控中，Grafana用于可视化InfluxDB中的容器指标数据，提供直观的监控界面。

​	使用docker-compose安装

```bash
version: '3.1' # Docker Compose文件的版本

volumes:
  # 定义数据卷
  grafana_data: {} # Grafana的数据卷

services:
  # 定义服务列表
  influxdb:
    # InfluxDB服务
    image: tutum/influxdb:0.9 # 使用InfluxDB 0.9镜像
    restart: always # 容器退出时总是重启
    environment:
      # 环境变量配置
      - PRE_CREATE_DB=cadvisor # 预先创建cadvisor数据库
    ports:
      # 端口映射
      - "8083:8083" # InfluxDB管理界面端口
      - "8086:8086" # InfluxDB API端口
    volumes:
      # 挂载数据卷
      - ./data/influxdb:/data # 将本地目录映射到InfluxDB容器的/data目录下

  cadvisor:
    # Cadvisor服务
    image: google/cadvisor # 使用Google Cadvisor镜像
    links:
      # 链接到InfluxDB服务
      - influxdb:influxsrv # 将influxdb服务命名为influxsrv并链接到cadvisor容器
    command: -storage_driver=influxdb -storage_driver_db=cadvisor -storage_driver_host=influxsrv:8086 # 启动参数，使用InfluxDB作为存储驱动
    restart: always # 容器退出时总是重启
    ports:
      # 端口映射
      - "8085:8080" # Cadvisor Web界面端口
    volumes:
      # 挂载文件和目录
      - /:/rootfs:ro # 挂载根目录为只读
      - /var/run:/var/run:rw # 挂载/var/run目录为读写
      - /sys:/sys:ro # 挂载/sys目录为只读
      - /var/lib/docker/:/var/lib/docker:ro # 挂载/var/lib/docker目录为只读

  grafana:
    # Grafana服务
    user: "104" # 指定用户ID
    image: grafana/grafana # 使用Grafana镜像
    restart: always # 容器退出时总是重启
    links:
      # 链接到InfluxDB服务
      - influxdb:influxsrv # 将influxdb服务命名为influxsrv并链接到grafana容器
    ports:
      # 端口映射
      - "3000:3000" # Grafana Web界面端口
    volumes:
      # 挂载Grafana数据卷
      - grafana_data:/var/lib/grafana # 将grafana_data数据卷映射到/var/lib/grafana目录下
    environment:
      # 环境变量配置
      - HTTP_USER=admin # Grafana登录用户名
      - HTTP_PASS=admin # Grafana登录密码
      - INFLUXDB_HOST=influxsrv # InfluxDB主机名
      - INFLUXDB_PORT=8086 # InfluxDB端口
      - INFLUXDB_NAME=cadvisor # InfluxDB数据库名称
      - INFLUXDB_USER=root # InfluxDB用户名
      - INFLUXDB_PASS=123456 # InfluxDB密码
```

1. 创建docker-compose.yml文件执行`docker-compose up -d`
2. 检查启动状况(账号密码在配置中有写都是`admin`)

![image-20231031135113969](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031135113969.png)

3. 配置`Grafana`数据源

![image-20231031135507957](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031135507957.png)

4. 连接数据库

![image-20231031140157067](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031140157067.png)

5. 创建面板

> 跟着点

![image-20231031141501974](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141501974.png)

> 选则数据源

![image-20231031141529100](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141529100.png)

> 配置查询条件`Time series`那个下拉框可以选则不同的图标样式

![image-20231031141921078](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141921078.png)

## Docker轻量级可视化工具Portainer

1. 安装portainer

```bash
 docker run -d -p 8000:8000 -p 9000:9000 --name portainer --restart=always -v /home/run/docker.sock:/var/run/docker.sock -v /home/data:/data portainer/portainer 
```

2. 访问地址`IP:9000`结果portainer更新了,老大的local没了,看官网吧[Initial setup - Portainer Documentation](https://docs.portainer.io/start/install-ce/server/setup)

> 登录,设置账号密码然后点点点,第四步复制到服务器执行,第五步起一个名字,然后把刚才安装的`agrent`的服务器和端口填写上去,然后点击连接

![image-20231031130355074](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031130355074.png)

![image-20231031130511507](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031130511507.png)

3. 连接完成,就是这样了

![image-20231031130531370](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031130531370.png)



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



## Docker总结



[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
