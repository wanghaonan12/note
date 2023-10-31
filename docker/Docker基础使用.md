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

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
