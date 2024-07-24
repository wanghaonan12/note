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