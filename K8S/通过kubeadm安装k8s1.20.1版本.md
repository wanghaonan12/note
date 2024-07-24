> 请安装步骤进行，教程中`k8s`等组件的版本不要轻易改动，可能会导致某一个步骤报错

安装步骤

1. `Docker` 安装
2. 环境准备
3. `K8S` `Master` 初始化
4. 安装网络插件 `Calico`
5. 加入`Node`节点
6. 验证状态
7. 安装 `Dashboard`

| 主机Ip       | hostname   |
| ------------ | ---------- |
| 10.20.40.105 | k8s-master |
| 10.20.20.135 | k8s-node1  |

## `Docker` 安装

> 如果是一个全新的环境直接安装就行

**1. 卸载原有`Docker`**

```sh
yum remove docker*
yum remove -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin
```

**2.`Docker 19.0.15` 安装**

```sh
# 1. 安装 yum 工具  
yum install -y yum-utils

# 2. 配置 Docker 的 yum 地址
yum-config-manager \
--add-repo \
http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 3. 安装 Docker 19.03.15
yum install -y docker-ce-19.03.15 docker-ce-cli-19.03.15 containerd.io-1.4.6

# 4. 启动&开机启动docker
systemctl enable docker --now

# 5. docker 镜像加速器配置 exec-opts 一定要有，其他参数视情况而定
mkdir -p /etc/docker
tee /etc/docker/daemon.json <<-'EOF'
{
    "data-root":"/home/pde/docker",
     "insecure-registries": ["registry.access.redhat.com","quay.io","harbor.rdc.pde:180"],
     "registry-mirrors": ["https://q2gr04ke.mirror.aliyuncs.com",
         "https://do.nark.eu.org",
          "https://dc.j8.work",
          "https://docker.m.daocloud.io",
          "https://dockerproxy.com",
          "https://docker.mirrors.ustc.edu.cn",
          "https://docker.nju.edu.cn"],
     "bip": "172.7.22.1/24",
     "exec-opts": ["native.cgroupdriver=systemd"],
     "log-driver": "json-file",
     "log-opts": {
       "max-size": "100m",
       "max-file": "1"
     },
     "live-restore": true
}
EOF
systemctl daemon-reload
systemctl restart docker
```

## 环境准备

> 所有机器执行以下操作  每个机器使用内网ip互通
>
> 关闭防火墙可以避免端口阻塞问题，确保 Kubernetes 各组件之间的通信畅通。
>
> 关闭 SELinux 可以避免潜在的兼容性问题，简化配置过程。
>
> 关闭 Swap 是确保 kubelet 正常运行的必要步骤。Swap 是指交换分区或交换文件
>
> 确保桥接的 IPv4 流量传递到 iptables 链，避免网络问题。
>
> 时间同步确保所有节点的系统时间一致，避免因时间不同步导致的问题。

```sh
# 1. 关闭防火墙并设置开机不启动
systemctl stop firewalld
systemctl disable firewalld

# 2、关闭 selinux 将 SELinux 设置为 permissive 模式（相当于将其禁用）
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# 3、关闭swap 重启生效
# swap分区指的是虚拟内存分区，它的作用是物理内存使用完，之后将磁盘空间虚拟成内存来使用，启用swap设备会对系统的性能产生非常负面的影响，因此kubernetes要求每个节点都要禁用swap设备，但是如果因为某些原因确实不能关闭swap分区，就需要在集群安装过程中通过明确的参数进行配置说明
swapoff -a  
sed -ri 's/.*swap.*/#&/' /etc/fstab
systemctl reboot
#查看下swap交换区是否都为0，如果都为0则swap关闭成功
free -m  

# 4、给三台机器分别设置主机名 第一台：k8s-master 第二台：k8s-node1
hostnamectl set-hostname <hostname>

# 5、添加hosts，执行如下命令，ip需要修改成你自己机器的ip
cat >> /etc/hosts << EOF
10.20.40.105 k8s-master
10.20.20.135 k8s-node1
EOF

# 6、允许 iptables 检查桥接流量
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system  

# 7、设置时间同步
# CentOS7 执行
yum install ntpdate -y
ntpdate time.windows.com
# CentOS8系统中执行，原有的时间同步服务 ntp/ntpdate服务已经无法使用,chrony替代ntp，首先安装chrony
yum install -y chrony
# 修改时间同步服务器
vim /etc/chrony.conf 
# 注释第三行原有的时间服务器地址，添加阿里云的时间服务器地址 阿里云提供了7个NTP时间服务器也就是Internet时间同步服务器地址
server ntp1.aliyun.com
systemctl restart chronyd.service
systemctl enable chronyd.service

# 8、配置k8s的yum源地址
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
   http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# 9、如果之前安装过k8s，先卸载旧版本
yum remove -y kubelet kubeadm kubectl

# 10、安装 kubelet，kubeadm，kubectl指定版本，我们使用kubeadm方式安装k8s集群
yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9

# 11、开机启动kubelet
systemctl enable --now kubelet

# 12 拉取需要的 镜像
mkdir /home/k8s/
cat > /home/k8s/pullImages.sh << EOF
#!/bin/bash
images=(
kube-apiserver:v1.20.9
kube-proxy:v1.20.9
kube-controller-manager:v1.20.9
kube-scheduler:v1.20.9
coredns:1.7.0
etcd:3.4.13-0
pause:3.2
)
for imageName in ${images[@]} ; do
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/$imageName
done
EOF
chmod +x ./pullImages.sh && ./pullImages.sh
```

# `K8S` `Master` 初始化

> 接下来的操作在 `Master` 节点的机器上执行

**不要无脑执行，注意`apiserver-advertise-address`和`control-plane-endpoint`，其他的参数无所谓可以抄作业**

```sh
kubeadm init \
--apiserver-advertise-address=10.20.40.105 \
--control-plane-endpoint=k8s-master \
--image-repository registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images \
--kubernetes-version v1.20.9 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=10.244.0.0/16

# 安装出问题可以查看 kubectl 日志
journalctl -xefu kubelet  
```

#### 解释：

- `kubeadm init`：初始化 `Kubernetes` 主节点。
- `--apiserver-advertise-address=10.20.40.105`：`API` 服务器通告的 `IP` 地址，确保集群中的其他节点可以访问该` API` 服务器。
- `--image-repository registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images`：使用阿里云镜像仓库，加快镜像下载速度。
- `--kubernetes-version v1.12.9`：指定使用 `Kubernetes v1.12.9`版本。
- `--service-cidr=10.96.0.0/12`：指定服务的` IP `地址范围，避免与其他 `IP` 地址冲突。
- `--pod-network-cidr=10.244.0.0/16`：指定 `Pod` 的 `IP` 地址范围，确保网络插件能够正确配置和使用这些 `IP` 地址。

>  运行成功会有一下提示！！这里很重要记得保存下来

```sh
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of control-plane nodes by copying certificate authorities
and service account keys on each node and then running the following as root:

  kubeadm join k8s-master:6443 --token 48dddr.jlg0g2z4txxepu8e \
    --discovery-token-ca-cert-hash sha256:d0888fa2da9bbec40a714a2ef0511fca9e10e5f83c1a1ba19dc5c76df4f92f2c \
    --control-plane 

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join k8s-master:6443 --token 48dddr.jlg0g2z4txxepu8e \
    --discovery-token-ca-cert-hash sha256:d0888fa2da9bbec40a714a2ef0511fca9e10e5f83c1a1ba19dc5c76df4f92f2c 
```

> 根据提示执行命令

```sh
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

export KUBECONFIG=/etc/kubernetes/admin.conf
```

## 安装网络插件 `Calico`

```sh
curl https://docs.projectcalico.org/archive/v3.20/manifests/calico.yaml -O /home/k8s/
kubectl apply -f /home/k8s/calico.yaml
```

## 加入`Node`节点

> 还记得在`Master` 节点初始化后的提示内容吗，第一个 `kubeadm join`是高可用中多个`master`搭建`master`集群用的，第二个是加入的`Node`机器

**直接在Node机器执行**

```sh
kubeadm join k8s-master:6443 --token 48dddr.jlg0g2z4txxepu8e \
    --discovery-token-ca-cert-hash sha256:d0888fa2da9bbec40a714a2ef0511fca9e10e5f83c1a1ba19dc5c76df4f92f2c 
```

> 如果你很不听劝没有去记录初始化信息，当然我也不会去相信你把整数的`Hash`值记住了,hahahahha,下面是补救的办法,重新生成 token 

```sh
kubeadm token create --print-join-command
```

## 验证状态

> 在每个节点上都进行验证,获取节点信息

```sh
kubectl get nodes
```

![image-20240712114145852](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712114145852.png)

## 安装 `Dashboard`

>  下载 `Dashboard ` 的 `yaml`部署文件 ,并创建 deployment.
>
> 如果下载不下来，请在自己电脑上访问并复制`yml`内容，并进行手动创建

```sh
curl  https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml -o /home/k8s/
kubectl apply -f /home/k8s/recommended.yaml
```

**创建成功的样子，让你瞅瞅，万一你创建就不行呢，哈哈哈哈哈，我也是遇到了很多问题才到这一步的😭😭😭**

![image-20240712100645643](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712100645643.png)

> 虽然都没什么问题，但是在这里你还是无法使用宿主机的`Ip`进行访问，在这里需要修改一下 `Service` 的网络模式
>
> 修改 `svc` 为 `NodePort` 模式并添加 对外接口 

```sh
 kubectl edit svc kubernetes-dashboard -n kubernetes-dashboard
```

![image-20240712100734530](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712100734530.png)

> 然后就可以登陆了，但是他还需要`Token`😒😒😒😒,没想到吧，我也没想到，继续呗！！！
>
> 天哪，我太贴心了，把所有的脚本都使用命令创建好直接执行就行了，被自己感动到了😎😎😎

```sh
cat > /home/k8s/dashboard_user.yml << EOF
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
EOF
cat > /home/k8s/ClusterRoleBinding.yml << EOF
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
EOF
kubectl apply -f dashboard_user.yml
kubectl apply -f ClusterRoleBinding.yml
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
```

![image-20240712105107075](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712105107075.png)

![image-20240712105321601](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712105321601.png)

![image-20240712105333302](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240712105333302.png)

## K8S验证

> 获取节点查看状态

```sh
 kubectl get node
```

![image-20240715095611272](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240715095611272.png)

> 创建一个`nginx`的`deployment`和`svc`

```sh
# 创建 deployment
kubectl create deployment nginx --image=nginx
# 创建 svc
kubectl expose deployment nginx --port=80 --type=NodePort
# 查看对外暴露端口 
kubectl get pod,svc -owide
```

![image-20240715095939376](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240715095939376.png)

**验证成功！！**

![image-20240715100041598](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240715100041598.png)

## 安装问题及解决处理

### The connection to the server localhost:8080 was refused - did you specify the right host or port?

> node节点执行kubectl命令kubectl get nodes出现下面的错误：
>
> The connection to the server localhost:8080 was refused - did you specify the right host or port?
>
> 在node节点配置KUBECONFIG环境变量即可

```sh
echo "export KUBECONFIG=/etc/kubernetes/kubelet.conf" >> /etc/profile
source /etc/profile
```

### DNS域名解析失效

> 咱也不知道为啥这破服务器老是出问题

```sh
cat > /etc/resolv.conf << EOF
nameserver 114.114.114.114
nameserver 8.8.8.8
nameserver 8.8.4.4
EOF
```

### `kubeadm reset` 时 `must remove /etc/cni/net.d`

> 有残留文件导致的

```sh
The reset process does not clean CNI configuration. To do so, you must remove /etc/cni/net.d

The reset process does not reset or clean up iptables rules or IPVS tables.
If you wish to reset iptables, you must do so manually by using the "iptables" command.

If your cluster was setup to utilize IPVS, run ipvsadm --clear (or similar)
to reset your system's IPVS tables.
```

**处理**

```sh
# 清理ipvsadm
yum install -y ipvsadm
ipvsadm -C
iptables -F && iptables -t nat -F && iptables -t mangle -F && iptables -X
# 清除遗留文件
rm -rf /root/.kube
rm -rf /etc/cni/net.d
```

### [ERROR CRI]: container runtime is not running: output: time="2024-07-08T13:52:36+08:00" level=fatal msg="validate service connection: CRI v1 runtime API is not implemented for endpoint \"unix:///var/run/containerd/containerd.sock\": rpc error: code = Unimplemented desc = unknown service runtime.v1.RuntimeService"

[官网提示！！！](https://kubernetes.io/zh-cn/docs/setup/production-environment/container-runtimes/#containerd)

![image-20240708140418433](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240708140418433.png)

> 修改`/etc/containerd/config.toml`文件

![image-20240708140434830](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240708140434830.png)

> 重启`containerd`

```sh
systemctl restart containerd
```

### yum下载源出问题

> 将`/etc/yum.repos.d/CentOS-Base.repo `进行备份，执行下面语句，获取新的下载源

```sh
mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.bf
curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-8.repo
```