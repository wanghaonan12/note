

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

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
