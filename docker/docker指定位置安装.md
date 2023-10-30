# docker指定位置安装

## docker卸载

```shell
# 停止docker
systemctl stop docker
# 查看yum安装的docker文件包
yum list installed |grep docker
# 把匹配到的包执行 yum remove 删除
yum remove containerd.io.x86_64
# 查看docker相关的rmp源文件
rpm -qa |grep docker
# 删除所有安装的docker文件包
yum -y remove docker版本文件包
# 查看docker是否写在完成
docker version
```

## 安装

[(29条消息) 安装docker并指定安装目录_docker安装目录_wangyue23com的博客-CSDN博客](https://blog.csdn.net/wangyue23com/article/details/121332433)

```shell
#安装yum工具
yum install yum-utils -y
#配置yum源
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
#安装docker
yum install -y docker-ce-19.03.9 docker-ce-cli-19.03.9 containerd.io
#加载镜像加速站点：
systemctl daemon-reload
#启动docker并且设置开机启动
systemctl enable docker && systemctl start docker
```

### 安装报错



![image-20230614153916542](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230614153916542.png)

## 修改docker安装位置

```shell
# 停止docker
systemctl stop docker.socket
systemctl stop docker

# 移动docker所有文件到/home/docker
mv /var/lib/docker /home/docker

# 建立软连接
ln -s /home/docker /var/lib/docker

# 重启docker
systemctl restart docker

#进入目录
cd /var/lib/docker

#查看当前目录大小，0kb
du -sh
```

