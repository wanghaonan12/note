# ES 开启安全认证

> 本次使用 `docker `  配置 `es:7.17.5` ,默认 `es` 已经安装

## 设置密码

### 1.在es配置文件中修改配置

```yml
cluster.name: "pdes-es"
network.host: 0.0.0.0
# 开启下面两个配置安装认证和ssl安全配置
xpack.security.enabled: true
xpack.security.transport.ssl.enabled: truex
```

### 2.重启`es`容器

```sh
docekr restart [containerId]
```

### 3.进入`docker`前往`es`的`bin`目录设置密码

```sh
docker exec -it [containerId] bash
cd ./bin
elasticsearch-setup-passwords interactive
```

![image-20240709094552456](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240709094552456.png)

![image-20240709094653896](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240709094653896.png)

> 依次设置用户：`elastic`、`apm_system`、`kibana_system`、`logstash_system`、`beats_system`、`remote_monitoring_user`共6个用户
>
> 再次访问的时候就需使用密码登录了，用户名为上面的用户名，密码当然是自己设置的了。



![image-20240709095152679](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240709095152679.png)

## 修改密码

>  同样需要在`bin`目录下执行 ，这里面修改用户`elastic`的密码为`123456`

```sh
curl -H "Content-Type:application/json" -XPOST -u elastic 'http://127.0.0.1:9200/_xpack/security/user/elastic/_password' -d '{ "password" : "123456" }'
```

![image-20240709100037526](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240709100037526.png)

接下来就可以使用新设置的密码登陆了！！！！！！！！！！！

# 服务配置

> `nacos`服务配置中修改 `elasticsearch-dev.yml`

```yml
es-config:
  address: 10.20.40.104:7004
  enable: true
  password: pde88888 #es 密码
  userName: pde88888 #es 账号
  connection-timeout: 3s
  read-timeout: 10s
  enableOpenSearch: false
  connectionRequestTimeout: 20000
  maxConnectNum: 1000
  maxConnectPerRoute: 1000
  trustStorePath: "/usr/local/sourcecode/java-opensearch23-client/src/main/resources/wjunshenStore"
  trustStorePwd: "wjunshen"
```

> Log stash  连接账号密码文件默认位置`/home/pde/logstash/config`

```json
input {
  tcp {
    host => "0.0.0.0"
    port => 5044
    mode => "server"
    codec => json_lines
  }
}
output {
  elasticsearch {
    hosts => ["http://10.20.40.104:7004"]
    index => "pdes-syslog-%{+YYYY.MM}"
    action => "index"
    codec  => "json"
    user => "pde88888"
    password => "pde88888"
  }
}
```

