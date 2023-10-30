# docker

## 手动安装docker

### 1. docker卸载

```shell
sudo yum remove docker containerd runc docker-engine docker.io docker-client docker-common docker-latest docker-latest-logrotate docker-logrotate
```

- sudo:

  sudo(全称为 Super User do)是linux系统管理指令，是允许系统管理员让普通用户执行一些或者全部的root命令的一个工具，如halt，reboot，su等等。这样不仅减少了root用户的登录 和管理时间，同样也提高了安全性。sudo不是对shell的一个代替，它是面向每个命令的。

- yum:

  Yum（全称为 Yellow dog Updater, Modified）是一个在Fedora和RedHat以及CentOS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，无须繁琐地一次次下载、安装。[yum命令介绍](https://blog.51cto.com/u_21817/6062143)

  好的，下面是更新后的 `yum` 包管理器的功能表格，包括命令语法和案例：

  | 功能                | 描述                                                       | 命令语法                                            | 案例                                          |
  | ------------------- | ---------------------------------------------------------- | --------------------------------------------------- | --------------------------------------------- |
  | 安装软件包          | 安装新的软件包和应用程序。                                 | `sudo yum install package_name`                     | `sudo yum install nginx`                      |
  | 更新软件包          | 将已安装的软件包升级到最新版本。                           | `sudo yum update package_name`                      | `sudo yum update firefox`                     |
  | 删除软件包          | 从系统中删除不需要的软件包。                               | `sudo yum remove package_name`                      | `sudo yum remove libreoffice`                 |
  | 查询软件包          | 查找软件包是否可用于安装以及包含的文件和信息。             | `yum search package_name`                           | `yum search python`                           |
  | 列出软件包          | 列出已安装或可用于安装的软件包。                           | `yum list [installed | available]`                  | `yum list installed`                          |
  | 清理软件包缓存      | 删除 `yum` 下载的软件包缓存，释放磁盘空间。                | `sudo yum clean packages`                           | `sudo yum clean packages`                     |
  | 解决依赖性          | 自动处理软件包之间的依赖关系，确保安装和更新的成功。       | `sudo yum install package_name`                     | `sudo yum install nodejs`                     |
  | 禁用/启用软件仓库   | 管理软件仓库的启用和禁用状态。                             | `sudo yum-config-manager --disable repository_name` | `sudo yum-config-manager --disable epel`      |
  | 显示仓库信息        | 显示软件仓库的配置和详细信息。                             | `yum repolist all`                                  | `yum repolist all`                            |
  | 下载软件包          | 下载软件包和其依赖项，但不进行安装。                       | `yumdownloader package_name`                        | `yumdownloader wget`                          |
  | 清理旧内核          | 删除系统上旧的、不再需要的内核版本。                       | `sudo package-cleanup --oldkernels`                 | `sudo package-cleanup --oldkernels`           |
  | 安装调试信息        | 安装软件包的调试信息，方便在调试时使用。                   | `sudo debuginfo-install package_name`               | `sudo debuginfo-install httpd`                |
  | 构建软件包依赖项    | 安装构建软件包时所需的编译依赖项。                         | `sudo yum-builddep package_name`                    | `sudo yum-builddep gcc`                       |
  | 源代码包管理        | 安装、构建和管理软件的源代码包。                           | `sudo yum install package_name --source`            | `sudo yum install zlib --source`              |
  | 导出/导入软件包列表 | 将已安装的软件包列表导出到文件，或从文件中导入软件包列表。 | `yum list installed > installed_packages.txt`       | `yum list installed > installed_packages.txt` |
  | 显示软件包历史      | 显示软件包的安装、更新和删除历史记录。                     | `yum history list`                                  | `yum history list`                            |

  请注意，上述示例中的命令语法和案例假设您已经安装了 `yum` 包管理器，并且在使用这些命令时可能需要管理员权限（使用 `sudo`）。另外，有些命令的参数可能因 `yum` 版本的不同而略有不同，建议在使用这些命令之前查阅相关文档或使用 `man` 命令来获取更详细的信息和用法说明。

- docker containerd runc docker-engine docker.io:

  Docker 的旧版本被称为 docker，docker.io 或 docker-engine

  containerd 是一个容器运行时组件

  runC 是一套符合OCI标准的容器引擎

- docker-client docker-common docker-latest docker-latest-logrotate docker-logrotate

  docker 相关参数

### 2. 安装依赖包

```shell
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

![image-20230727115938369](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727115938369.png)

- `yum-utils` 是一组实用工具的软件包，用于增强和扩展 `yum` 包管理器的功能

  | 功能                     | 描述                             | 命令示例                                           |
  | ------------------------ | -------------------------------- | -------------------------------------------------- |
  | package-cleanup          | 清理系统上不需要的软件包         | `sudo package-cleanup --oldkernels`                |
  | debuginfo-install        | 安装软件包的调试信息             | `sudo debuginfo-install packagename`               |
  | repoquery                | 查询软件包信息                   | `repoquery --info packagename`                     |
  | yum-builddep             | 安装构建软件包所需的编译依赖项   | `sudo yum-builddep packagename`                    |
  | yum-config-manager       | 管理软件仓库                     | `sudo yum-config-manager --enable repository_name` |
  | repo-rss                 | 显示软件仓库的订阅源             | `repo-rss list`                                    |
  | package-catalogs         | 查找并显示文件的来源             | `package-catalogs /path/to/some/file`              |
  | yumdownloader            | 下载软件包及其依赖项             | `yumdownloader packagename`                        |
  | verifytree               | 验证软件包依赖关系树的完整性     | `sudo verifytree`                                  |
  | yum-groups-manager       | 管理软件包组                     | `sudo yum-groups-manager`                          |
  | repo-graph               | 生成软件仓库中软件包的依赖关系图 | `repo-graph`                                       |
  | needs-restarting         | 检查系统中需要重新启动的进程     | `needs-restarting -r`                              |
  | tsflags                  | 软件包事务标记的管理工具         | `tsflags`                                          |
  | yum-complete-transaction | 完成之前未完成的 yum 事务        | `sudo yum-complete-transaction`                    |
  | yumdb                    | 查询和管理 RPM 数据库            | `yumdb info packagename`                           |

- `device-mapper-persistent-data` 是一个 Linux 软件包，它本身是用于支持 `device-mapper` 子系统的持久化数据存储，主要用于逻辑卷管理 (Logical Volume Management, LVM) 的配置恢复。当在系统上使用 LVM 来管理逻辑卷时，`device-mapper-persistent-data` 会自动记录逻辑卷和卷组的配置信息。在系统重启后，`device-mapper-persistent-data` 将读取持久化的配置信息，并帮助恢复之前的逻辑卷和卷组配置，以确保逻辑卷能够正确挂载并保持数据的完整性。

- LVM2（Logical Volume Manager 2）是 Linux 系统中的逻辑卷管理器，它提供了对底层硬盘分区或磁盘设备的抽象，使用户能够以更高级别的逻辑方式来管理存储空间。

  | 功能                          | 描述                                               | 命令示例                                                     |
  | ----------------------------- | -------------------------------------------------- | ------------------------------------------------------------ |
  | 创建物理卷（Physical Volume） | 初始化物理硬盘或分区，并将其纳入 LVM 管理。        | `sudo pvcreate /dev/sdX`                                     |
  | 创建卷组（Volume Group）      | 将一个或多个物理卷合并为一个逻辑池，供逻辑卷使用。 | `sudo vgcreate vg_name /dev/sdX`                             |
  | 创建逻辑卷（Logical Volume）  | 在卷组上创建一个虚拟的逻辑卷，用于存储数据。       | `sudo lvcreate -L sizeG -n lv_name vg_name`                  |
  | 格式化逻辑卷                  | 在逻辑卷上创建文件系统，使其能够被挂载和使用。     | `sudo mkfs.ext4 /dev/vg_name/lv_name`                        |
  | 扩展逻辑卷                    | 增加逻辑卷的大小，以扩展存储空间。                 | `sudo lvextend -L +10G /dev/vg_name/lv_name`                 |
  | 缩减逻辑卷                    | 减小逻辑卷的大小，以释放空间或创建新的逻辑卷。     | `sudo lvreduce -L -5G /dev/vg_name/lv_name`                  |
  | 移动逻辑卷                    | 在卷组内迁移逻辑卷的位置，而无需复制数据。         | `sudo pvmove /dev/sdX /dev/sdY`                              |
  | 创建逻辑卷快照                | 创建逻辑卷的快照，以便备份、测试或恢复数据。       | `sudo lvcreate -L sizeG -n lv_snapshot -s /dev/vg_name/lv_name` |
  | 移除逻辑卷快照                | 删除不再需要的逻辑卷快照。                         | `sudo lvremove /dev/vg_name/lv_snapshot`                     |
  | 创建逻辑卷镜像                | 在不同的物理卷上创建逻辑卷的镜像，以提供数据冗余。 | `sudo lvcreate --type mirror -m 1 -L sizeG -n lv_mirror vg_name` |
  | 移除逻辑卷镜像                | 删除不再需要的逻辑卷镜像。                         | `sudo lvconvert --merge /dev/vg_name/lv_mirror`              |
  | 在线调整卷组                  | 增加或减小卷组的大小，以容纳更多逻辑卷或释放空间。 | `sudo vgextend vg_name /dev/sdX`                             |
  | 在线调整物理卷大小            | 增加或减小物理卷的大小，以扩展存储或释放空间。     | `sudo pvresize /dev/sdX`                                     |
  | 显示 LVM 信息                 | 显示当前 LVM 状态和配置信息。                      | `sudo pvs`, `sudo vgs`, `sudo lvs`                           |
  | 显示 LVM 版本信息             | 显示安装的 LVM 软件包的版本。                      | `sudo lvm version`                                           |


### 3. 配置镜像源

**清华大学源**

```bash
sudo yum-config-manager --add-repo https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/centos/docker-ce.repo
```

![image-20230727115859962](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727115859962.png)

1. `--add-repo`: 这是一个 `yum-config-manager` 的选项，用于添加新的软件仓库配置。

### 4. 安装docker

```bash
sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

![image-20230727123745134](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727123745134.png)

1. **Docker CE (Community Edition)**：Docker CE 是 Docker 社区版本，是一种开源的容器化平台，用于轻松创建、部署和管理容器化应用程序。Docker CE 允许开发者将应用程序和其依赖项打包到容器中，并在不同的环境中运行，从而实现更高效、更一致的应用程序交付。
2. **Docker CE CLI (Command Line Interface)**：Docker CE CLI 是 Docker 的命令行工具，用于与 Docker 容器和镜像进行交互。通过 Docker CE CLI，用户可以使用简单的命令来管理容器、镜像、卷和网络等资源。           
3. **containerd.io**：containerd.io 是一个开源的容器运行时工具，它是 Docker 引擎的核心组件之一。containerd.io 负责管理容器的生命周期，包括创建、运行、停止和删除容器。它提供了底层容器运行时接口，是 Docker 容器的基础。
4. **Docker Compose Plugin**：Docker Compose Plugin 是 Docker Compose 的插件，用于扩展 Docker Compose 的功能。Docker Compose 是一个工具，允许用户使用简单的 YAML 文件来定义和管理多个容器的应用程序堆栈。Docker Compose Plugin 可以提供额外的功能或整合其他工具，以满足特定场景或需求。

这些组件一起构成了 Docker 生态系统中的重要部分。Docker CE 提供了完整的容器化平台，Docker CE CLI 提供了简单易用的命令行接口，containerd.io 是 Docker 引擎的核心组件，负责管理容器的运行时，而 Docker Compose Plugin 则是对 Docker Compose 的扩展和增强。通过这些组件的协作，开发者可以更轻松地构建、部署和管理容器化应用程序。

## docker命令

| 命令                                   | 描述                                                     |
| -------------------------------------- | -------------------------------------------------------- |
| `docker search image_name`             | 从Docker仓库检索镜像                                     |
| `docker pull image_name`               | 从Docker仓库下载镜像                                     |
| `docker build -t image_name .`         | 根据Dockerfile在当前目录中构建一个镜像                   |
| `docker run image_name`                | 运行一个容器，基于指定的镜像                             |
| `docker ps`                            | 显示正在运行的容器                                       |
| `docker ps -a`                         | 显示所有容器，包括已停止的容器                           |
| `docker stop container_id`             | 停止运行中的容器                                         |
| `docker start container_id`            | 启动一个已经创建的容器                                   |
| `docker restart container_id`          | 重启一个容器                                             |
| `docker rm container_id`               | 删除一个已经停止的容器                                   |
| `docker images`                        | 显示本地所有的镜像                                       |
| `docker rmi image_id`                  | 删除一个本地镜像                                         |
| `docker exec -it container_id command` | 在运行中的容器内执行命令                                 |
| `docker logs container_id`             | 查看容器的日志                                           |
| `docker network ls`                    | 列出所有网络                                             |
| `docker volume ls`                     | 列出所有数据卷                                           |
| `docker-compose up`                    | 使用docker-compose根据docker-compose.yml文件启动服务     |
| `docker-compose down`                  | 停止并移除docker-compose.yml文件中定义的服务             |
| `docker stats`                         | 实时显示容器的资源使用情况                               |
| `docker system prune`                  | 删除所有未使用的资源（无用的容器、镜像、网络、数据卷等） |

请注意，这只是一些常用的Docker命令示例，Docker提供了更多功能丰富的命令和选项。您可以使用`docker --help`命令来查看完整的Docker命令列表及其说明。

## docker run 命令选项

| 选项                | 解释                                                         |
| ------------------- | ------------------------------------------------------------ |
| `-d`                | 在后台（detached）模式下运行容器。容器将在后台默默地运行，不会占用当前终端的控制台。 |
| `-p host:container` | 将主机的端口映射到容器的端口。使用此选项可以在主机上访问容器内运行的服务。`host`:主机端口，`container`: 容器端口 |
| `-e`                | 设置环境变量。您可以通过该选项将环境变量传递给运行的容器，以便配置应用程序或服务的行为。 |
| `-m`                | 设置容器的内存限制。使用此选项可以限制容器可以使用的内存量。 |
| `--name`            | 为容器指定一个名称。通过指定名称，可以在其他Docker命令中使用容器名称来管理容器。 |
| `--restart`         | 设置容器的重启策略。可以指定为`no`（默认，不重启）、`on-failure`（在非零退出码时重启）或`always`（总是重启）。 |
| `-v host:container` | 将主机目录或数据卷挂载到容器内的目录。通过这种方式可以持久化保存容器内的数据。 |
| `--network`         | 设置容器所使用的网络类型。可以指定为`bridge`（默认，使用Docker的桥接网络）或其他自定义网络。 |
| `--link`            | 连接到另一个容器。使用此选项可以让一个容器连接到另一个容器，并在两个容器之间建立网络连接。 |
| `--env-file`        | 从文件中读取环境变量。使用此选项可以将环境变量从指定的文件中读取，并传递给容器。 |
| `--user`            | 指定容器中运行的用户名或用户ID。使用此选项可以设置容器中运行进程的用户身份。 |
| `--hostname`        | 设置容器的主机名。使用此选项可以为容器指定一个自定义的主机名。 |
| `--privileged`      | 启用特权模式。使用此选项可以让容器访问主机系统的所有设备，并绕过许多安全限制。 |
| `--cpus`            | 设置容器可以使用的CPU资源的限制。使用此选项可以限制容器可以使用的CPU核心数量。 |
| `--memory-swap`     | 设置容器内存和交换空间的限制。使用此选项可以指定容器允许使用的总内存和交换空间的最大限制。 |
| `--tmpfs`           | 在容器内挂载一个临时文件系统。使用此选项可以将一个临时文件系统挂载到容器内的目录，用于临时存储数据。 |

解释：

- `--network`选项：设置容器所使用的网络类型。可以指定为`bridge`（默认，使用Docker的桥接网络）或其他自定义网络。例如：`docker run --network=custom_network image_name` 将容器连接到名为`custom_network`的自定义网络，从而允许容器与其他连接到该网络的容器进行通信。

- `--link`选项：连接到另一个容器。使用此选项可以让一个容器连接到另一个容器，并在两个容器之间建立网络连接。例如：`docker run --link=db_container image_name` 将容器连接到名为`db_container`的另一个容器，允许容器访问该容器中运行的服务。

- `--env-file`选项：从文件中读取环境变量。使用此选项可以将环境变量从指定的文件中读取，并传递给容器。例如：`docker run --env-file=my_env_file image_name` 从名为`my_env_file`的文件中读取环境变量，并将其传递给容器。

- `--user`选项：指定容器中运行的用户名或用户ID。使用此选项可以设置容器中运行进程的用户身份。例如：`docker run --user=my_user image_name` 将容器中的进程以`my_user`用户身份运行。

- `--hostname`选项：设置容器的主机名。使用此选项可以为容器指定一个自定义的主机名。例如：`docker run --hostname=my_container image_name` 将容器的主机名设置为`my_container`。

- `--privileged`选项：启用特权模式。使用此选项可以让容器访问主机系统的所有设备，并绕过许多安全限制。例如：`docker run --privileged image_name` 启用特权模式运行容器。

- `--cpus`选项：设置容器可以使用的CPU资源的限制。使用此选项可以限制容器可以使用的CPU核心数量。例如：`docker run --cpus=2 image_name` 将容器限制为最多使用2个CPU核心。

- `--memory-swap`选项：设置容器内存和交换空间的限制。使用此选项可以指定容器允许使用的总内存和交换空间的最大限制。例如：`docker run --memory=512

## docker镜像制作

1. 上传构建docker镜像代码

   git clone https://github.com/docker/getting-started.git

   ![image-20230727153817248](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727153817248.png)

2. 编写dockerfile

   在/home/getting-started/app文件下创建dockerfile文件**（没有后缀）**

   ![image-20230727153934998](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727153934998.png)

   ![image-20230727153923703](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727153923703.png)

3. 构建docker镜像

   ```bash
   docker build -t getting .
   ```

   其中，`-t`用于为镜像指定一个名称（getting），`.`是Dockerfile所在的路径。

   ![image-20230727155059296](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727155059296.png)

   制作完成，查看镜像

   ```shell
    docker images
   ```

   ![image-20230727155149598](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727155149598.png)

   运行镜像**(记得打开3000端口)**

   ```shell
    docker run -d -p 3000:3000 getting
   ```

   ![image-20230727155300112](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727155300112.png)

   - `docker run`: 这是用于运行Docker容器的命令。
   - `-d`: 这是一个选项，表示在后台（detached）模式下运行容器。即容器将在后台运行，并且控制台不会被容器占用。
   - `-p 3000:3000`: 这是另一个选项，用于指定端口映射。格式为`主机端口:容器端口`。在这里，它将主机的端口3000映射到容器的端口3000。这样，当在主机上访问端口3000时，请求将转发到容器内的端口3000上。
   - `getting`: 这是指定要运行的Docker镜像的名称。在这里，假设存在名为`getting`的Docker镜像。

   最后便可以访问ip:3000地址看见画面

   ![image-20230727155457032](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727155457032.png)

4. 运行docker容器

## dockerfile



## docker可视化——portainer

检索docker 仓库

```sh
docker search portainer
```

![image-20230727162340875](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727162340875.png)

拉取镜像

```shell
docker pull portainer/portainer
```

查看镜像

```shell
docker images
```

![image-20230727162526374](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727162526374.png)

运行镜像

```sh
docker run -d -p 8100:8000 -p 9100:9000 --name=portainer --restart=always -v /var/run/docker.sock:/var/run/docker.sock -v portainer_data:/data  portainer/portainer
```

![image-20230727162426867](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727162426867.png)

- `docker run`: 这是用于运行Docker容器的命令。
- `-d`: 这是一个选项，表示在后台（detached）模式下运行容器。容器将在后台运行，并且控制台不会被容器占用。
- `-p 8100:8000`: 这是一个选项，用于指定端口映射。格式为`主机端口:容器端口`。在这里，它将主机的端口8100映射到容器的端口8000。这样，当在主机上访问端口8100时，请求将转发到容器内的端口8000上。
- `-p 9100:9000`: 同上，这将主机的端口9100映射到容器的端口9000。
- `--name=portainer`: 这是为容器指定一个名称。在这里，容器的名称被设置为"portainer"。
- `--restart=always`: 这是容器的重启策略。`always`表示无论出现什么情况，Docker守护进程都会自动重启该容器。
- `-v /var/run/docker.sock:/var/run/docker.sock`: 这是一个数据卷的绑定挂载。它将主机上的`/var/run/docker.sock`套接字文件映射到容器内的`/var/run/docker.sock`文件，这样容器可以与主机上的Docker守护进程进行通信，实现对Docker环境的管理。
- `-v portainer_data:/data`: 这是另一个数据卷的绑定挂载。它将名为`portainer_data`的Docker卷映射到容器内的`/data`目录，用于持久化保存Portainer容器的数据。
- `portainer/portainer`: 这是要运行的Docker镜像的名称。在这里，它是从Docker Hub上的`portainer`仓库获取的`portainer`镜像。

访问网址ip:9100创建账号就拥有一个可视化的docker界面了

![image-20230727162656814](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230727162656814.png)

[三种docker可视化工具](https://blog.csdn.net/zfw_666666/article/details/126538026)

## 学习网址

阶段一：入门

Docker官方文档：Docker官方文档提供了全面的Docker指南和教程，适合初学者。

网址：https://docs.docker.com/
Docker官方教程：Docker官方网站上提供了一些交互式的教程，可以帮助您快速入门。

网址：https://www.docker.com/tryit/
Docker Mastery系列课程（Udemy）：这是一套非常受欢迎的视频课程，由Docker专家Bret Fisher讲授。

网址：https://www.udemy.com/docker-mastery/
Docker入门教程（YouTube）：许多YouTubers分享了Docker入门教程，例如FreeCodeCamp、Traversy Media等。

网址：https://www.youtube.com/
阶段二：深入学习

Docker网络教程（Docker官方文档）：Docker官方文档提供了有关Docker网络概念和配置的详细指南。

网址：https://docs.docker.com/network/
Docker数据管理指南（Docker官方文档）：这个指南介绍了Docker中的数据卷和数据管理技术。

网址：https://docs.docker.com/storage/
Docker Compose文档（Docker官方文档）：Docker Compose的官方文档提供了使用Compose进行容器编排的详细信息。

网址：https://docs.docker.com/compose/
Docker安全文档（Docker官方文档）：Docker官方文档中有关Docker安全性和最佳实践的指南。

网址：https://docs.docker.com/engine/security/
阶段三：应用实践

Docker示例应用（GitHub）：在GitHub上，您可以找到许多Docker示例应用程序和项目，可以供您学习和实践。

网址：https://github.com/
使用Docker进行CI/CD（Docker官方文档）：Docker官方文档中提供了关于使用Docker进行持续集成和部署的指南和最佳实践。

网址：https://docs.docker.com/samples/
Docker监控指南（Docker官方文档）：Docker官方文档提供了有关如何监控Docker容器和主机的详细说明。

网址：https://docs.docker.com/config/containers/logging/

