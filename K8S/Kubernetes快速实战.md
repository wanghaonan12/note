

[TOC]

# 简介

## 1.K8S 是什么

[k8s 中文官网](https://kubernetes.io/zh-cn/docs/home/)

K8S 是Kubernetes的全称，源于希腊语，意为“舵手”或“飞行员”。Kubernetes 是用于自动部 署、扩缩和管理容器化应用程序的开源系统。 Kubernetes 源自 ， 同时凝聚了社区的最佳创意和实践。

> Docker：作为开源的应用容器引擎，可以把应用程序和其相关依赖打包生成一个 Image 镜像文 件，是一个标准的运行环境，提供可持续交付的能力； Kubernetes：作为开源的容器编排引擎，用来对容器化应用进行自动化部署、 扩缩和管理

[通过kubeadm安装k8s1.20.1](https://blog.csdn.net/weixin_52359996/article/details/140381427)

## 2.K8S 核心架构

我们已经知道了 K8S 的核心功能：自动化运维管理多个容器化程序。那么 K8S 怎么做到的呢？这里， 我们从宏观架构上来学习 K8S 的设计思想。首先看下图：

![image-20240717090449549](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/components-of-kubernetes.svg)

K8S 是属于`Master-Worker`架构，**即有 `Master` 节点负责核心的调度、管理和运维，`Worker `节点则 执行用户的程序,和`nginx` 有点类似**。但是在` K8S` 中，主节点一般被称为`Master Node `，而从节点则被称为`Worker Node` 或者 `Node`。

 **注意：**`Master Node `和 `Worker Node` 是分别安装了` K8S` 的` Master` 和 `Woker` 组件的实体服务器， 每个 `Node` 都对应了一台实体服务器（虽然 `Master Node` 可以和其中一个 `Worker Node `安装在同一台服务器，但是建议 `Master Node `单独部署），所有 `Master Node` 和 `Worker Node` 组成了 K8S 集群，同一个集群可能存在多个 `Master Node` 和 `Worker Node`。

**首先来看Master Node都有哪些组件：**

- **kube-apiserver：**`K8S `的请求入口服务。`API Server` 负责接收 `K8S` 所有请求（来自 `UI` 界面或者 `CLI` 命令行工 具），然后，`API Server `根据用户的具体请求，去通知其他组件干活。
-  **Scheduler**。`K8S` 所有 `Worker Node` 的调度器。当用户要部署服务时，`Scheduler` 会选择最合适的 `Worker Node`（服务器）来部署。
- **Controller Manager：**`K8S` 所有 `Worker Node` 的监控器。`Controller Manager` 有很多具体的 `Controller`， `Node Controller`、`Service Controller`、`Volume Controller `等。`Controller` 负责监控和调整在 `Worker Node` 上部署的服务的状态，比如用户要求 `A` 服务部署 `2` 个副本，那么当其中一个服务挂了的时候，`Controller `会马 上调整，让 `Scheduler` 再选择一个 `Worker Node` 重新部署服务。
- **etcd：**K8S 的存储服务。`etcd `存储了 `K8S `的关键配置和用户配置，`K8S `中仅 `API Server` 才具备读写权限，其 他组件必须通过 `API Server` 的接口才能读写数据。

**接着来看Worker Node的组件:**

- **Kubelet：**`Worker Node `的监视器，以及与` Master Node `的通讯器。`Kubelet` 是 `Master Node` 安插在 `Worker Node `上的**“眼线”**，它会定期向 `Master Node` 汇报自己 `Node `上运行的服务的状态，并接受来自 `Master Node` 的指示采取调整措施。负责控制所有容器的启动停止，保证节点工作正常。
- **Kube-Proxy：**`K8S` 的网络代理。`Kube-Proxy `负责 `Node` 在 `K8S` 的网络通讯、以及对外部网络流量的负载均衡。
- **Container Runtime：**`Worker Nod`e 的运行环境。即安装了容器化所需的软件环境确保容器化程序能够跑起 来，比如 `Docker Engine`运行环境。

## 核心特性

- **服务发现与负载均衡：**无需修改你的应用程序即可使用陌生的服务发现机制。 
- **存储编排：**自动挂载所选存储系统，包括本地存储。 
- **Secret和配置管理：**部署更新`Secrets`和应用程序的配置时不必重新构建容器镜像，且不必将软件堆栈配置中的秘 密信息暴露出来。 
- **批量执行：**除了服务之外，`Kubernetes`还可以管理你的批处理和`CI`工作负载，在期望时替换掉失效的容器。 
- **水平扩缩：**使用一个简单的命令、一个`UI`或基于`CPU`使用情况自动对应用程序进行扩缩。 
- **自动化上线和回滚：**`Kubernetes`会分步骤地将针对应用或其配置的更改上线，同时监视应用程序运行状况以确保 你不会同时终止所有实例。 
- **自动装箱：**根据资源需求和其他约束自动放置容器，同时避免影响可用性。 
- **自我修复：**重新启动失败的容器，在节点死亡时替换并重新调度容器，杀死不响应用户定义的健康检查的容器。

# 使用

> `k8s` 的知识点相对其他的工具知识量还是相当庞大的，在一开始的学习中由于急于求成，浪费了不少时间。本文会一点点带你了解 `k8s` 并能够进行基础的使用。
>
> 我们在对整个 k8s 操作的都是使用 kubectl 指令进行的。我们都是直接操作更高一级的`Deployment` ,让他去管理下面的组件的配合。在下面的这些组件中我们最常使用的是：`Deployment` 、` ConfigMap 和 Secret Volume`、 `Service`  、`Ingress`、`Namespace` 很少回去直接操作 `Pod`。
>
> 可以这么理解：所有的组件都在服务 `Pod` 但是，`Pod` 会被 `Deployment`管理。

**Namespace**：用于在 `Kubernetes` 集群中进行资源隔离和组织管理的逻辑分区。

**Pod**：`Kubernetes` 中最小的可部署单位，包含一个或多个容器。

**ReplicaSet**：确保指定数量的 `Pod `副本在任意时间点处于运行状态。

**探针（Probe）**：用于检测 `Pod` 内应用程序的健康状态和可用性。

**Deployment**：用于管理 `Pod` 和 `ReplicaSet` 的声明性更新。**(主要使用！！！！)**

**StatefulSet**：用于管理有状态应用的部署和扩展，确保 `Pod` 的顺序和持久性。

**Service**：为一组 `Pod` 提供稳定的网络访问和负载均衡。

**Volume**：提供 `Pod` 中容器共享的持久化存储。

**服务配置（ConfigMap 和 Secret）**：用于管理非机密和机密的配置信息。

**Ingress**：提供 `HTTP` 和 `HTTPS` 路由，将外部流量转发到集群内部的服务。

> 接下来的笔记中会使用 k8s 中资源的简称，如有疑惑可以过来查询。名字太长了，一点不想打字啊😂😂😂

| 资源名称                | 简称   |
| ----------------------- | ------ |
| Namespace               | ns     |
| Pod                     | po     |
| ReplicaSet              | rs     |
| Deployment              | deploy |
| StatefulSet             | sts    |
| DaemonSet               | ds     |
| Service                 | svc    |
| ConfigMap               | cm     |
| Secret                  | sec    |
| PersistentVolume        | pv     |
| PersistentVolumeClaim   | pvc    |
| Ingress                 | ing    |
| Job                     | job    |
| CronJob                 | cj     |
| HorizontalPodAutoscaler | hpa    |
| NetworkPolicy           | netpol |
| Role                    | role   |
| RoleBinding             | rb     |
| ClusterRole             | cr     |
| ClusterRoleBinding      | crb    |

## Kubectl

> `kubectl`是`Apiserver`的客户端工具，工作在命令行下，能够连接`apiserver`实现各种增删改查等操作 `kubectl`官方使用文档： [Kubectl 官方帮助文档](https://kubernetes.io/zh-cn/docs/reference/kubectl/)

` K8S`的各种命令帮助文档做得非常不错，在接下来的学习中我们也会经常使用`kubectl` 命令，如遇到问题可以多查`help`帮助。

```sh
kubectl --help
kubectl [operation] --help
#例如查看 get 的命令使用 kubectl get --help
```

![image-20240717094123686](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240717094123686.png)

**常用命令！！！**

```sh
# 查看资源列表
# 查看当前命名空间中的所有 Pod 列表。
kubectl get pods 
# 查看当前命名空间中的所有部署列表。
kubectl get deployments 
# 查看当前命名空间中的所有服务列表。
kubectl get services



# 查看详细信息
# 查看特定 Pod 的详细信息，包括容器状态、事件等。
kubectl describe pod <pod_name>
# 查看特定部署的详细信息，包括副本集和事件等。
kubectl describe deployment <deployment_name>



# 创建和管理资源
# 根据 YAML 文件创建资源。
kubectl create -f <yaml_file>
# 应用（创建或更新）指定的 YAML 文件中定义的资源。
kubectl apply -f <yaml_file>
# 删除指定的 Pod。
kubectl delete pod <pod_name>
# 删除指定的部署。
kubectl delete deployment <deployment_name>



# 管理命名空间
# 查看所有命名空间的列表。
kubectl get namespaces
# 创建一个新的命名空间。
kubectl create namespace <namespace_name>
# 删除指定的命名空间及其所有资源。
kubectl delete namespace <namespace_name>



# 查看日志和执行命令
# 查看指定 Pod 的日志。
kubectl logs <pod_name>
# 在指定 Pod 中执行特定的命令，例如进入 shell。
kubectl exec -it <pod_name> -- <command>


# 调试和排查问题
# 查看资源的详细描述，用于排查问题。
kubectl describe <resource_type> <resource_name>
# 查看集群中的事件，包括 Pod 创建、删除等状态变化。
kubectl get events



# 扩展和管理集群
# 扩展或缩减指定部署的副本数量。
kubectl scale deployment <deployment_name> --replicas=<num>
# 查看部署的滚动更新状态。
kubectl rollout status deployment/<deployment_name>
```

**注意!!!!!!!!!!!!!!：**在操作k8s的时候会经常使用yaml进行操作，如果没有或是不知道格式是什么样子，可以在官网进行查询或是获取已存在的对象的yaml进行修改。获取yaml方法如下：

```sh
 kubectl get po mynginx -o yaml > /home/k8s/mynginx.yaml
```

![image-20240717111952826](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240717111952826.png)

---

## Namespace

> K8s 中， 提供一种机制，将同一集群中的资源划分为相互隔离的组。同一命名空间内的资 源名称要唯一，命名空间是用来隔离资源的，不隔离网络。 Kubernetes 启动时会创建四个初始命名空间：

**default：** Kubernetes 包含这个命名空间，以便于你无需创建新的命名空间即可开始使用新集群。 

**kube-node-lease：**该命名空间包含用于与各个节点关联的 对象。 节点租约允许 kubelet 发送 ， 由此 控制面能够检测到节点故障。 

**kube-node-lease：**所有的客户端（包括未经身份验证的客户端）都可以读取该命名空间。 该命名空间主要预留为集群使 用，以便某些资源需要在整个集群中可见可读。 该命名空间的公共属性只是一种约定而非要求。 

**kube-system：**该命名空间用于 Kubernetes 系统创建的对象。

> 作用：

**资源隔离**：ns 提供了一种机制，可以在同一个集群中将不同的环境（如开发、测试和生产）隔离开来，避免相互干扰。<u>在同一个 ns 下 资源名称不允许重复。</u>

**访问控制**：通过 Role-Based Access Control (RBAC)，可以对不同的 ns 设置不同的权限，确保用户只能访问和操作他们有权限的资源。

**资源配额**：可以为不同的 ns 设置资源配额 (Resource Quota)，限制某个 ns 中的资源使用情况（如 CPU、内存、存储等），从而防止资源滥用。

**简化管理**：将相关联的资源（如 po 、svc 、cm 等）放在同一个 ns 中，方便管理和查找。

---

> **操作与使用**

- 使用命令创建&删除 ns

  ```sh
  # 创建
  kubectl create ns pde
  # 删除
  kubectl delete ns pde
  ```

- 使用`yaml`文本 创建 ns

  先创建一个 yaml 名为 pde_ns.yaml

  ```yaml
  apiVersion: v1
  kind: Namespace
  metadata:
    name: pde
  ```

  在执行

  ```sh
  # 创建
  kubectl apply -f pde_ns.yaml
  # 删除
  kubectl delete -f pde_ns.yaml
  ```

- 获取 ns

  ```sh
  kubectl get ns
  ```

  ![image-20240717104604762](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240717104604762.png)

## Pod（了解的多一点）

> Pod 是 Kubernetes 中一个非常核心的概念，它使得容器化应用能够更加灵活地管理和部署，同时提供了高度的隔离性和资源共享能力。

1. **容器集合**: Pod 可以包含一个或多个容器，这些容器通常是紧密相关的应用组件，它们共享同一份资源（如网络命名空间和存储卷），并能够通过 localhost 进行通信。
2. **共享资源**: Pod 内的容器共享一些资源，比如存储卷 (Volumes) 和网络设置。这使得它们可以共同协作完成某些任务，例如共享状态或共享数据。
3. **唯一 IP 地址**: 每个 Pod 都有一个唯一的 IP 地址，这个 IP 地址是 Pod 内所有容器共享使用的。这种设计使得 Pod 内的容器可以轻松地相互通信。
4. **资源限制**: Pod 可以设置资源的使用限制和请求，这些限制包括 CPU、内存和存储等资源，帮助 Kubernetes 调度器更好地分配和管理资源。

---

> **操作与使用**

- 使用命令创建&删除 po

  ```sh
  # 创建
  kubectl run mynginx --image=nginx:1.14.2
  # 删除
  kubectl delete mynginx
  ```

- 使用yaml操作

  创建一个`nginx_po.yaml`

  ```yaml
  apiVersion: v1
  kind: Pod # 资源类型
  metadata:
    labels:
      run: mynginx # 标签
    name: mynginx
    namespace: default # 指定的命名空间
  spec:
    containers:
    - image: nginx:1.14.2 # 镜像版本
      imagePullPolicy: IfNotPresent # 镜像拉取策略
      name: mynginx # 镜像名称
      ports:
        - containerPort: 80 # 暴露端口
  ```

  > 下面是进阶版本的，其中添加了，端口，环境变量，还有在一个pod中启动两个容器！！
  >
  > 如果需要创建多个容器，请注意端口号不要冲突！！！！！！！！！！！！

  ```yaml
  apiVersion: v1
  kind: Pod # 资源类型
  metadata:
    labels:
      run: mynginx # 标签
    name: mynginx
    namespace: default # 指定的命名空间
  spec:
    containers:
    - image: nginx:1.14.2 # 镜像版本
      imagePullPolicy: IfNotPresent # 镜像拉取策略
      name: mynginx # 镜像名称
      ports:
        - containerPort: 80
        - containerPort: 8081
      env:
        - name: PROFILE
          value: 'dev'
        - name: LB-TAG
          value: 'dev'
      volumeMounts:
      - name: workspace
        mountPath: /usr/share/nginx/html
    - image: nginx:1.14.2 # 镜像版本
      imagePullPolicy: IfNotPresent # 镜像拉取策略
      name: mynginx # 镜像名称
      ports:
        - containerPort: 8082
        - containerPort: 8083
      env:
        - name: PROFILE
          value: 'dev'
        - name: LB-TAG
          value: 'dev'
      volumeMounts:
      - name: workspace
        mountPath: /usr/share/nginx/html
    volumes:
      - name: workspace
        persistentVolumeClaim:
          claimName: workspace-pvc
  
  ```

  执行命令

  ```sh
  # 创建 
  kubectl apply -f nginx_po.yaml
  # 删除
  kubectl delete -f nginx_po.yaml
  ```

- 其他操作

  **查看对象状态**

  ```sh
  # 查看 po 状态
  kubectl get po mynginx
  ```

  ![image-20240717114323939](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240717114323939.png)

  **查看事件详情**

  ```sh
  # 查看 po 的一个事件 如果在启动失败的时候需要进来查看原因 Events:的描述
  kubectl describe po mynginx 
  ```

  ![image-20240717114202299](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240717114202299.png)

  这里的是因为我将所有的`node`节点都停掉了导致的，加入进去就好了。

## ReplicaSet （了解）

> 在 Kubernetes 中，ReplicaSet 是用来确保指定数量的 Pod 副本在运行的控制器对象。它是 Kubernetes 中的一个重要概念，用于实现高可用性和扩展性。

**Pod 副本**: ReplicaSet 确保一定数量的 Pod 副本在运行。如果某个 Pod 失败，ReplicaSet 会创建一个新的 Pod 以保持期望的状态。

**选择器（Selectors）**: ReplicaSets 使用选择器根据标签识别并管理哪些 Pods。

**期望状态（Desired State）**: 应该运行的 Pod 副本的期望数量。

**实际状态（Actual State）**: 当前实际运行的 Pod 副本数量。

---

```yaml
apiVersion: v1
kind: ReplicaSet # 资源类型，这里是 ReplicaSet
metadata:
  name: nginx 
  labels:
    app: nginx
    tier: frontend # tier 标签通常用于表示应用程序的不同层次或组件，例如前端（frontend）、后端（backend）和数据库层（database）。这有助于组织和管理复杂的应用程序架构。也可以自定义
spec:
  replicas: 3 # 期望副本数量为3
  selector:
    matchLabels:
      tier: frontend 
  template:
    metadata:
      labels:
        tier: frontend
    spec:
      containers:
      - name: nginx 
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

## 探针(很有用，但是我用不上😂)

> 探针（Probes）是 Kubernetes 用于检查 Pod 中容器健康状态的机制。Kubernetes 提供了三种类型的探针：存活探针（Liveness Probe）、就绪探针（Readiness Probe）和启动探针（Startup Probe）
>
> 探针有三种检查方式：HTTP GET 请求、TCP Socket 和执行命令



**存活探针（Liveness Probe）：**存活探针用于检测容器是否处于健康状态。如果存活探针检查失败，Kubernetes 会杀死容器，并根据其重启策略（restart policy）决定是否重新启动容器。

<font color=green>使用场景：</font>

- 检测应用程序是否进入了死锁状态，不能再进行处理。
- 保证容器在整个生命周期中都能正常运行。

<font color=red>配置</font>

这里使用 `http Get` 方式进行验证。当然也可以使用其他的方式。

```yaml
livenessProbe: # 存货探针
  httpGet: # 探针使用HTTP GET 请求检查容器的健康状态 会对 localhost:8080/healthz 地址的状态进行验证是否在200~400间
    path: /healthz
    port: 8080 
  initialDelaySeconds: 3 # 探针在容器启动后第一次检查的延迟时间
  periodSeconds: 3  # 探针检查的间隔时间
```

**就绪探针（Readiness Probe）：**就绪探针用于检测容器是否已经准备好处理请求。如果就绪探针检查失败，Kubernetes 会从服务的负载均衡器中移除该容器，直到探针成功。

<font color=green>使用场景：</font>

- 在应用启动后确保它已经准备好接收流量。
- 检查依赖服务是否已经就绪。

<font color=red>配置</font>

这里使用 `Tcp Socket` 方式进行验证。当然也可以使用其他的方式。

```yaml
readinessProbe:
  tcpSocket: # 尝试打开到容器指定端口的 TCP 连接来验证容器的健康状态
    port: 8080
  initialDelaySeconds: 3 # 探针在容器启动后第一次检查的延迟时间
  periodSeconds: 3  # 探针检查的间隔时间
```

**启动探针（Startup Probe）：**启动探针用于检测应用程序是否已经启动。它特别适用于启动时间较长的应用。如果启动探针配置了，存活探针将在启动探针成功之前被禁用。这确保了容器有充足的时间完成启动，而不会因为存活探针的失败而被杀死。

<font color=green>使用场景：</font>

- 适用于启动时间较长的应用，以防止启动过程中被存活探针误杀。

<font color=red>配置</font>

这里使用 `http Get` 方式进行验证。当然也可以使用其他的方式。

```yaml
readinessProbe:
  exec:
    command:
    - /bin/sh
    - -c
    - "curl -f http://localhost:8080/ready || exit 1"
  initialDelaySeconds: 3 # 探针在容器启动后第一次检查的延迟时间
  periodSeconds: 3  # 探针检查的间隔时间
```

---

**实战配置！！**

> 这里使用了三个探针是想说明，一个po可以存在多个探针，淡然也没必要设置那么多！！配合上面的介绍这里使用了都是 http Get 请求的模式，也是想说明**每个探针的的模式都是自由可选的！！！**

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod  # Pod 的名称
spec:
  containers:
  - name: nginx-container  # 容器的名称
    image: nginx:1.14.2  # 使用的镜像
    ports:
    - containerPort: 80  # 容器暴露的端口号
    livenessProbe:
      httpGet:
        path: /  # 存活探针的 HTTP GET 请求路径
        port: 80  # 存活探针的端口号
      initialDelaySeconds: 10  # 存活探针第一次检查的延迟时间（秒）
      periodSeconds: 5  # 存活探针的检查间隔时间（秒）
      timeoutSeconds: 3  # 存活探针的超时时间（秒）
      successThreshold: 1  # 探针成功的连续次数，1次成功即认为探针成功
      failureThreshold: 5  # 探针失败的连续次数，5次失败即认为探针失败
    readinessProbe:
      httpGet:
        path: /  # 就绪探针的 HTTP GET 请求路径
        port: 80  # 就绪探针的端口号
      initialDelaySeconds: 5  # 就绪探针第一次检查的延迟时间（秒）
      periodSeconds: 5  # 就绪探针的检查间隔时间（秒）
      timeoutSeconds: 3  # 就绪探针的超时时间（秒）
      successThreshold: 1  # 探针成功的连续次数，1次成功即认为探针成功
      failureThreshold: 5  # 探针失败的连续次数，5次失败即认为探针失败
    startupProbe:
      httpGet:
        path: /  # 启动探针的 HTTP GET 请求路径
        port: 80  # 启动探针的端口号
      initialDelaySeconds: 0  # 启动探针第一次检查的延迟时间（秒）
      periodSeconds: 5  # 启动探针的检查间隔时间（秒）
      timeoutSeconds: 3  # 启动探针的超时时间（秒）
      successThreshold: 1  # 探针成功的连续次数，1次成功即认为探针成功
      failureThreshold: 30  # 探针失败的连续次数，30次失败即认为探针失败
  restartPolicy: Always  # 重启策略，始终重启容器
  imagePullPolicy: Always  # 镜像拉取策略，始终拉取最新镜像
```



## Deployment

## StatefulSet

---


## Service
---


## Volume
---


## 服务配置
---


## Ingress

**Service&Ingress总结**

> Service 是 K8S 服务的核心，屏蔽了服务细节，统一对外暴露服务接口，真正做到了“微服务”。举 个例子，我们的一个服务 A，部署了 3 个备份，也就是 3 个 Pod；对于用户来说，只需要关注一个 Service 的入口就可以，而不需要操心究竟应该请求哪一个 Pod。优势非常明显：一方面外部用户不需 要感知因为 Pod 上服务的意外崩溃、K8S 重新拉起 Pod 而造成的 IP 变更，外部用户也不需要感知 因升级、变更服务带来的 Pod 替换而造成的 IP 变化，另一方面，Service 还可以做流量负载均衡。 但是，Service 主要负责 K8S 集群内部的网络拓扑。集群外部需要用 Ingress 。 Ingress 是整个 K8S 集群的接入层，复杂集群内外通讯。

---

## 监控

