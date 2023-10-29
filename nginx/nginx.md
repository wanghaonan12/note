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

​	Nginx 动静分离简单来说就是把动态跟静态请求分开，不能理解成只是单纯的把动态页面和静态页面物理分离。严格意义上说应该是动态请求跟静态请求分开，可以理解成使用 Nginx处理静态页面，Tomcat 处理动态页面。动静分离从目前实现角度来讲大致分为两种，一种是纯粹把静态文件独立成单独的域名，放在独立的服务器上，也是目前主流推崇的方案；另外一种方法就是动态跟静态文件混合在一起发布，通过 nginx 来分开。

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

2. 编辑profile文件,在最后添加

```bash
# NGINX 环境变量
export NGINX_HOME=/usr/local/nginx
export PATH=${NGINX_HOME}/bin:$PATH:$PATH:${NGINX_HOME}/sbin
```

![image-20231024225859671](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231024225859671.png)

3. 刷新环境变量

```bash 
source /etc/profile
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

# Nginx配置反向代理

1. 安装 Tomact

```bash
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.82/bin/apache-tomcat-9.0.82.tar.gz
```

2. 解压 Tomact

```bash
tar -vxf apache-tomcat-9.0.82.tar.gz
```

3. 启动 Tomact

```bash
cd ./apache-tomcat-9.0.82/bin
./startup.sh
```

需要java环境![image-20231025215613476](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231025215613476.png)接着装

4. 安装jdk

[jdk1.8地址](https://download.oracle.com/otn/java/jdk/8u381-b09/8c876547113c4e4aab3c868e9e0ec572/jdk-8u381-linux-x64.tar.gz?AuthParam=1698244751_31ad8a01b241def4270500db5cab09b3)

5. 解压配置环境变量

```bash
tar -vxf jdk-8u381-linux-x64.tar.gz
cd /etc
```

```bash
export JAVA_HOME=/home/jdk
export PATH=$JAVA_HOME/bin:$PATH 
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
```

注意:`profile`这里的`JAVA_HOME`的地址填写自己的真实地址

![image-20231029200741576](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029200741576.png)

6. 继续启动

```bash
./startup.sh
```

![image-20231029201433048](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029201433048.png)

**tomcat安装完成!!!**

7. 配置 `nginx` 反向代理使用80的端口访问 `tomcat`

> 在 nginx 的配置文件中修改配置文件 http 的 server配置
>
> `proxy_pass http://127.0.0.1:8080;` 中的地址是需要代理的地址,这里代理的是服务器本地的 `tomcat`

```bash
location / {
            root   html;
            proxy_pass http://127.0.0.1:8080;
            index  index.html index.htm;
        }
```

![image-20231029202420671](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029202420671.png)

8. 重新加载配置验证效果

```bash
 nginx -s reload
```

![image-20231029203110396](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029203110396.png)

# Nginx 多个方向代理配置

1. 在启动一个 `tomcat`  

> 注意文件名字(操碎了心,啧啧啧!)

```bash
cp -r /home/tomcat9 /home/tomcat9.1
```

2. 修改端口配置`/home/tomcat9.1/conf/server.xml`

![image-20231029205036789](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029205036789.png)

3. 修改tomcat样式用来区分

4. 添加nginx配置

> `location` 后面配置请求代理的地址规则,支持正则表达式

![image-20231029210135785](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029210135785.png)

5. 验证

```bash
nginx -s reload
```



![image-20231029210824502](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029210824502.png)

# 配置负载均衡

1. 修改 `nginx` 配置

```xml

	upstream myserver{
		server ip:8081; #这里的地址不能使用127.0.0.1
		server ip:8080;
	}

  location / {
      root   html;
      proxy_pass http://myserver;
      index  index.html index.htm;
  }

```



![image-20231029212052158](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029212052158.png)

2. 验证

> 刷新之后就会访问另一个 `tomcat`

![image-20231029212002026](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029212002026.png)

## 负载均衡方式

1. 轮询(默认)

每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器 down 掉，能自动剔除。

```bash
	upstream myserver{
		server ip:8081; #这里的地址不能使用127.0.0.1
		server ip:8080;
	}
```



2. weight

weight 代表权重默认为 1,权重越高被分配的客户端越多

```bash
	upstream myserver{
		server ip:8081 weight=2; #这里的地址不能使用127.0.0.1
		server ip:8080 weight=1; #weight 决定被分配到的频率这里是 2:1
	}
```



3. ip_hash

每个请求按访问 ip 的 hash 结果分配，这样每个访客固定访问一个后端服务器

```bash
	upstream myserver{
		ip_hash;
		server ip:8081; #这里的地址不能使用127.0.0.1
		server ip:8080; 
	}
```



4. fair(第三方)

按后端服务器的响应时间来分配请求，响应时间短的优先分配。

```bash
	upstream myserver{
		server ip:8081; #这里的地址不能使用127.0.0.1
		server ip:8080;
		fair;
	}
```

# 动静分离

​	通过 location 指定不同的后缀名实现不同的请求转发。通过 expires 参数设置，可以使浏览器缓存过期时间，减少与服务器之前的请求和流量。具体 Expires 定义：是给一个资源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可，所以不会产生额外的流量。此种方法非常适合不经常变动的资源。（如果经常更新的文件，不建议使用 Expires 来缓存），我这里设置 3d，表示在这 3 天之内访问这个 URL，发送一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码 304，如果有修改，则直接从服务器重新下载，返回状态码 200。

![image-20231029214057122](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029214057122.png)

1. 准备静态资源

​	我在`/home/static`目录下放了两个文件`html`和`image`,分别放了html文件和图片的静态文件

![image-20231029215831333](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029215831333.png)

2. 配置

> 其中location后面的路径是静态文件的下一级目录位置

```bash
location /image/ {
    root   /home/static/; # 静态文件目录
    index  index.html index.htm;
    autoindex on; # 启用或禁用目录列表
}

location /html/ {
    root   /home/static/;
    index  index.html index.htm;
}
```



![image-20231029220057413](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029220057413.png)

3. 验证

```bash
nginx -s reload
```

![image-20231029220713251](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029220713251.png)

# Nginx 高可用

​	Nginx的高可用性（High Availability）指的是在系统面临故障或者部分组件失效的情况下，仍然能够保持服务的持续性和稳定性。

​	说人话就是一台服务器坏了,还有另一台作为备用,提供服务,保证服务的稳定性.

​	原理是使用 `keepalive` 作为技术支持,在多台服务器配置安装 `keepalive` ,去监测 `nginx` 的状态,如果状态正常就访问 `master(主服务)` ,如果坏了就访问 `backup (备用服务)`.不过访问的地址是 `keepalive` 提供的虚拟ip. 

![image-20231029220954356](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029220954356.png)

1. 两台服务器安装nginx(不做演示了)
2. 两台服务器安装keepalive

```bash
yum install keepalived -y
```

3. 主服务`/etc/keepalived/keepalived.conf`修改配置

> 备用服务配置一样,注意修改 `interface`网卡 ` vrrp_instance VI_1---> state/virtual_router_id` 服务器的主从配置

```bash
global_defs {
   	notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from Alexandre.Cassen@firewall.loc
   smtp_server ip地址 #自己的IP地址
   smtp_connect_timeout 30
   router_id LVS_DEVEL
}
vrrp_script chk_http_port {
	script "/usr/local/src/nginx_check.sh"  #检测脚本位置 
	interval 2 #(检测脚本执行的间隔) 
	weight 2 # 权重,脚本条件成立,权重+2
}
vrrp_instance VI_1 {
  state BACKUP # 备份服务器上将 MASTER 改为 BACKUP
  interface eth0 //网卡
  virtual_router_id 51 # 主、备机的 virtual_router_id 必须相同
  priority 100 # 主、备机取不同的优先级，主机值较大，备份机值较小
  advert_int 1
  authentication {
          auth_type PASS
          auth_pass 1111
      }
  virtual_ipaddress {
  192.168.17.50 // VRRP H 虚拟地址
  } 
}

```

注意:网卡配置根据自己的配置来

![image-20231029222421773](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029222421773.png)

`/usr/local/src/nginx_check.sh` 脚本文件

```bash
#!/bin/bash

# 检查Nginx进程数量
A=$(ps -C nginx --no-header | wc -l)

# 如果Nginx进程数量为0，表示Nginx没有在运行
if [ $A -eq 0 ]; then
    # 启动Nginx，这里需要替换为你的Nginx路径
    /usr/local/nginx/sbin/nginx
    sleep 2

    # 再次检查Nginx进程数量
    if [ $(ps -C nginx --no-header | wc -l) -eq 0 ]; then
        # 如果Nginx仍然没有启动成功，终止keepalived进程
        killall keepalived
    fi
fi

# 添加注释：
# 1. 上面的脚本首先检查Nginx进程的数量。
# 2. 如果Nginx进程数量为0，表示Nginx没有在运行，尝试重新启动Nginx。
# 3. 脚本会等待2秒，然后再次检查Nginx进程数量。
# 4. 如果Nginx仍然没有启动成功（进程数量仍为0），则终止keepalived进程。
# 5. 这个脚本通常用于维护高可用性系统，确保Nginx持续运行，如果Nginx停止运行，则尝试重新启动，并在启动失败时处理相关进程（例如keepalived）。

```

4. 启动`keepalived`访问虚拟ip测试

```bash
systemctl start keepalived
```

![image-20231029224616020](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029224616020.png)

5. 停止主服务器刷新页面也能访问成功

![image-20231029224732729](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029224732729.png)

# Nginx原理

​	在Nginx中，Master和Worker是两个关键的概念，用于管理和处理客户端请求。这是因为Nginx采用了多进程/多线程模型，充分利用了现代多核服务器的性能。以下是关于Master和Worker的详细解释：

![image-20231029225333045](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029225333045.png)

##  **Master进程：**

Master进程是Nginx的主进程，它负责启动和管理Worker进程，以及读取和加载配置文件。当你启动Nginx时，实际上是启动了一个Master进程，它负责控制整个服务器的生命周期。Master进程主要做以下几个事情：

- **加载配置文件：** Master进程负责读取Nginx的配置文件，解析配置信息，并初始化服务器。

- **管理Worker进程：** Master进程会创建多个Worker进程，每个Worker进程负责处理客户端的请求。如果系统有多个CPU核心，通常会创建与CPU核心数量相等的Worker进程，这样可以充分利用多核服务器的性能。

- **处理信号：** Master进程会处理各种信号，比如重载配置、平滑升级、停止服务器等操作都可以通过向Master进程发送信号来实现。

##  **Worker进程：**

Worker进程是实际处理客户端请求的进程。每个Worker进程都是一个独立的进程，它们并发处理来自客户端的请求。Worker进程主要做以下几个事情：

- **处理客户端请求：** Worker进程负责处理客户端发来的HTTP请求，包括解析请求、处理请求、发送响应等操作。

- **非阻塞处理：** Nginx的Worker进程采用非阻塞I/O模型，能够高效处理大量的并发连接，而不会因为某个连接的阻塞而影响其他连接的处理。

- **缓存和日志：** Worker进程可以处理缓存和记录日志等任务，具体取决于Nginx的配置。

Master和Worker进程之间通过进程间通信（Inter-Process Communication，IPC）来协调工作。Master进程负责监听端口，接受客户端的连接请求，并将这些连接分配给各个Worker进程处理。

这种多进程/多线程模型使得Nginx非常高效，能够处理大量并发请求，同时也具有很好的稳定性和可靠性。

## **总结一下**: 

​	master  负责接收客户端请求 然后有 worker 进行抢夺执行 , 在热部署修改配置时拥有任务的 worker 会在执行完任务后进行更新配置,没有任务的 worker 会执行更行配置.如果其中一个worker坏掉了,也不影响整体的使用.

![image-20231029230421879](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029230421879.png)

​	首先，对于每个 worker 进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销， 同时在编程以及问题查找时，也会方便很多。其次，采用独立的进程，可以让互相之间不会 影响，一个进程退出后，其它进程还在工作，服务不会中断，master 进程则很快启动新的 worker 进程。当然，worker 进程的异常退出，肯定是程序有 bug 了，异常退出，会导致当 前 worker 上的所有请求失败，不过不会影响到所有请求，所以降低了风险。

​	Nginx 同 redis 类似都采用了 io 多路复用机制，每个 worker 都是一个独立的进程，但每个进 程里只有一个主线程，通过异步非阻塞的方式来处理请求， 即使是千上万个请求也不在话 下。每个 worker 的线程可以把一个 cpu 的性能发挥到极致。所以 worker 数和服务器的 cpu 数相等是最为适宜的。设少了会浪费 cpu，设多了会造成 cpu 频繁切换上下文带来的损耗。

![image-20231029230013913](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20231029230013913.png)

 **进程数worker_processes**

基本上cpu多少核绑定多少个就可以了

```bash
 worker_processes 4; #work 绑定 cpu(4 work 绑定 4cpu)。
```

**连接数 worker_connection**
 	这个值是表示每个 worker 进程所能建立连接的最大值，所以，一个 nginx 能建立的最大连接 数，应该是 worker_connections * worker_processes。当然，这里说的是最大连接数，对于 HTTP 请求本地资源来说，能够支持的最大并发数量是 worker_connections * worker_processes.

​	如果是支持 http1.1 的浏览器每次访问要占两个连接，所以普通的静态访 问最大并发数是: worker_connections * worker_processes /2.

​	而如果是 HTTP 作 为反向代 理来说，最大并发数量应该是 worker_connections * worker_processes/4。因为作为反向代理服务器，每个并发会建立与客户端的连接和与后端服 务的连接，会占用两个连接。

 [nginx课件v1.0.pdf](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/nginx%E8%AF%BE%E5%A0%82%E7%AC%94%E8%AE%B0-20231029230837558.pdf)

[nginx笔记2.pdf](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/nginx%E8%AF%BE%E5%A0%82%E7%AC%94%E8%AE%B0-20231029230836048-20231029231034637.pdf)
