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

# Nginx常用操作

## nginx设置环境变量

>  **不仅仅是`nginx	`这样配置环境变量,其他的比如`java`,`maven`都可以这样配置**

1. 进入etc文件(因为我还没有学过vim操作所以这里我就使用了ssh工具进行编辑)

```bash
cd /etc
```

2. 编辑environment文件

```bash
# NGINX 环境变量
export NGINX_HOME=/usr/local/nginx
export PATH=${NGINX_HOME}/bin:$PATH:$PATH:${NGINX_HOME}/sbin
```

![image-20231024225859671](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024225859671.png)

3. 刷新环境变量

```bash 
source /etc/environment
```

4. 验证环境变量

```bash
echo $NGINX_HOME
```

他会展示我们设置的地址![image-20231024230041539](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024230041539.png)

5. 验证生效

```bash
nginx -v
```

展示nginx版本![image-20231024230116516](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024230116516.png)大功告成

## 常用指令

> 刚刚手滑一下关掉了,还好保存了,上了一天班真累啊

1. 版本号查看

```bash
nginx -V
```

2. 启动

```bash
nginx
```

3. 停止

```bash
nginx stop
```

4. 重新加载

```bash
nginx -s reload
```

5. 帮助文档(最喜欢这个了)

```bash
nginx -h
```

![image-20231024230851673](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024230851673.png)

## nginx配置文件

**配置文件位置:**`/usr/local/nginx/conf/nginx.conf`

>  nginx由三部分组成:全局块,events块,http块

![image-20231024232003537](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024232003537.png)

### 全局块

从配置文件开始到 events 块之间的内容，主要会设置一些影响 nginx 服务器整体运行的配置指令，主要包括配置运行 Nginx 服务器的用户（组）、允许生成的 worker process 数，进程 PID 存放路径、日志存放路径和类型以及配置文件的引入等

```bash
worker_processes  1;
```

​	上面的配置这是 Nginx 服务器并发处理服务的关键配置，worker_processes 值越大，可以支持的并发处理量也越多，但是

会受到硬件、软件等设备的制约.

### events 块

events 块涉及的指令主要影响 Nginx 服务器与用户的网络连接，常用的设置包括是否开启对多 work process下的网络连接进行序列化，是否允许同时接收多个网络连接，选取哪种事件驱动模型来处理连接请求，每个 wordprocess 可以同时支持的最大连接数等。

```bash
events {
    worker_connections  1024;
}
```

上述例子就表示每个 work process 支持的最大连接数为 1024.这部分的配置对 Nginx 的性能影响较大，在实际中应该灵活配置。

### http 块

这算是 Nginx 服务器配置中最频繁的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里。

>  需要注意的是：http 块也可以包括` http 全局块`、`server 块`。

![image-20231024232937500](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024232937500.png)

1. **http 全局块**

​	http 全局块配置的指令包括文件引入、MIME-TYPE 定义、日志自定义、连接超时时间、单链接请求数上限等。

```bash
		include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;
```

1. `include mime.types;`：包含了mime.types文件中定义的MIME类型。

2. `default_type  application/octet-stream;`：设置默认的MIME类型为application/octet-stream。

3. `#log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '`：注释掉了日志格式的定义，如果需要记录日志可以取消注释。

4. `#access_log  logs/access.log  main;`：注释掉了访问日志的记录，如果需要记录可以取消注释。

5. `#sendfile on;`：开启了sendfile模块，可以将文件内容直接传输给客户端，减少网络传输时间。

6. `#tcp_nopush on;`：开启了TCP_NOPUSH选项，可以减少TCP连接的建立和关闭次数。

7. `keepalive_timeout  65;`：设置keepalive连接的超时时间为65秒。

8. `#gzip on;`：开启了gzip压缩模块，可以将响应数据进行压缩，减少网络带宽占用。



2. **http server 块**

​		这块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的，该技术的产生	是	为了节省互联网服务器硬件成本。

​		每个 http 块可以包括多个 server 块，而每个 server 块就相当于一个虚拟主机。而每个 server 块也分为全局 	server 块，以及可以同时包含多个 locaton 块。

```bash
				listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}
```

这段代码是一个nginx配置文件的片段，包含了一些常用的指令和配置项。具体解释如下：

1. `listen 80;`：监听80端口，即HTTP协议的端口。

2. `server_name localhost;`：指定服务器名，这里是本地主机。

3. `location / {...}`：定义了一个location块，匹配所有请求。

4. `root html;`：指定根目录为html。

5. `index index.html index.htm;`：当请求的文件不存在时，返回index.html或index.htm。

6. `error_page 500 502 503 504  /50x.html;`：指定错误页面，当出现500、502、503、504等错误时，返回/50x.html页面。

7. `location = /50x.html {...}`：指定错误页面的位置，即/50x.html页面。

8. `location ~ \.php$ {...}`：定义了一个location块，匹配以.php结尾的请求。

9. `fastcgi_pass 127.0.0.1:9000;`：将请求转发给FastCGI服务器，监听在127.0.0.1:9000端口。

10. `location ~ \.ht {...}`：定义了一个location块，匹配以.ht结尾的请求。

11. `deny  all;`：拒绝所有以.ht结尾的请求。

12. `#charset koi8-r;`：定义了字符集，但被注释掉了。

13. `#access_log  logs/host.access.log  main;`：定义了访问日志，但被注释掉了。

14. `server_name  somename  alias  another.alias;`：定义了多个server_name，用逗号分隔。

15. `listen 443 ssl;`：监听443端口，即HTTPS协议的端口。

16. `ssl_certificate cert.pem;`：指定SSL证书文件。

17. `ssl_certificate_key  cert.key;`：指定SSL证书密钥文件。

18. `ssl_session_cache shared:SSL:1m;`：指定SSL会话缓存。

19. `ssl_session_timeout 5m;`：指定SSL会话超时时间。

20. `ssl_ciphers  HIGH:!aNULL:!MD5;`：指定SSL密码。

21. `ssl_prefer_server_ciphers  on;`：优先使用服务器端的SSL密码。

22. `location / {...}`：指定根目录为html。

23. `index  index.html index.htm;`：当请求的文件不存在时，返回index.html或index.htm。