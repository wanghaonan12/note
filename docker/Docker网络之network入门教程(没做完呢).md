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

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
