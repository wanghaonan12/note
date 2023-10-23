# Nginx

1. ### nginx是什么

​	nginx是一种高性能的开源Web服务器和反向代理服务器，也可以作为负载均衡器、HTTP缓存、安全性保护和TCP代理。它是由Igor Sysoev开发的，最初在2004年发布。nginx的特点包括高并发处理能力、低内存占用、高稳定性、支持动态模块化扩展等。nginx广泛应用于互联网、云计算、CDN、DNS等领域，是目前最流行的Web服务器之一。

2. ### 反向代理

​	Ngx不仅可以做反向代理，实现负载均衡。还能用作正向代理来进行上网等功能。

​	在了解反向代理前我们先了解一下什么是反向代理.

​	(1) 正向代理：如果把局域网外的Internet想象成一个巨大的资源库，则局域网中的客户端要访问Internet,则需要通过代理服务器来访问，这种代理服务就称为正向代理。

​	如下图,正常情况下用户想要访问谷歌这个网址是无法访问的,在这个时候我们就可以在浏览器(客户端)上配置一个`代理服务器`,就可以通过这个`代理服务器`访问到谷歌.

![image-20231023214800241](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023214800241.png)

​	(2) 反向代理: 我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，在返回给客户端，此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器地址，隐藏了真实服务器P地址。

​	如下图,客户端不需要再去配置任何信息,直接对服务进行请求,但是请求的地址,是另一个服务器的地址,由这个暴露出来服务器在去通知我们真正的请求地址获取数据,再返回给用户,其中个中间的服务器就叫做`反向代理服务器`.

![image-20231023220340177](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023220340177.png)

3. ### 负载均衡

​	单个服务器解决不了，我们增加服务器的数量，然后将请求分发到各个服务器上，将原先请求集中到单个服务器上的况政为将请求分发到多个服务器上，将负载分发到不同的服务器，也就是我们所说的负载均衡. 

​	简单来说就是客户端同时发送多个请求像代理服务器,而代理服务器会进行区分配将这个多个请求分配给多个服务器进行处理,就是人多力量大,分给多个人.

![image-20231023221819270](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023221819270.png)

4. ### 动静分离

​	为了加快网站的解析速度，可以把动态页面和静态页面由不同的服务器来解析，加快解析速度。降低原来单个服务器的压力。

![image-20231023222559335](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023222559335.png)

# Nginx安装

[nginx官网](http://nginx.org/)

下载nginx上传到服务器中[nginx-1.12.2.tar.gz](http://nginx.org/download/nginx-1.12.2.tar.gz)

1. 安装 pcre

​		(1) 下载压缩文件 

```bash
wget http://downloads.sourceforge.net/project/pcre/pcre/8.37/pcre-8.37.tar.gz
```

​		(2) 解压

```bash
tar -xvf pcre-8.37.tar.gz
```

​		(3) 进入文件内部执行./configure 和 make && make install

```bash
cd pcre-8.37/
./configure
make && make install
```

最后效果

![image-20231023225952984](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023225952984.png)

2. 安装 openssl、zlib、gcc

```bash
yum -y install make zlib zlib-devel gcc-c++ libtool openssl openssl-devel
```

![image-20231023230058384](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023230058384.png)

3. 安装nginx

​		(1) 使用连接工具将提前下好的nginx安装包放到服务器中,也可以直接下载

```bash
wget http://nginx.org/download/nginx-1.12.2.tar.gz
```

​		(2) 和之前一样解压,进入文件执行./configure 和 make &&make install

```bash
cd nginx-1.12.2/
./configure
make && make install
```

最终效果

![image-20231023230735527](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023230735527.png)

​		(3) 进入/usr/local/nginx/sbin/nginx地址启动服务

```bash
cd /
./usr/local/nginx/sbin/nginx
```

访问自己的服务器地址检查启动状态

![image-20231023231120530](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231023231120530.png)

**注意:注意自己的防火墙是否允许80端口访问,当然也可以关闭防火墙**

防火墙关闭指令(关闭防火墙&&系统启动时不会启动防火墙)

```bash
systemctl stop firewalld && systemctl disable firewalld
```



​		(1) 查看防火墙端口

```bash
firewall-cmd --list-all
```

下图中的porst并没用允许80端口访问

![](https://img2018.cnblogs.com/blog/1455597/201910/1455597-20191029102553820-1653097356.png)

​		(2) 设置开放的端口号(现在大部分服务器都是阿里腾讯的,当然了这要到他们的地方开启防火墙)

```bash
firewall-cmd --add-service=http –permanent
firewall-cmd --add-port=80/tcp --permanent
```

