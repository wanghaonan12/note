# 负载均衡配置时会修改请求的`IP`为`nginx upstream`服务名称

## 现象：

![image-20240109132919886](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240109132919886.png)

## 原因：

 当客户端发起原始请求时，它会包含一个`Host`头部，其中包含目标主机的域名。当代理服务器将请求转发到后端服务器时，它可能需要修改这个头部，以确保后端服务器能够正确识别请求的目标主机。否则，后端服务器可能会返回错误的重定向地址。

### 处理方法：

添加配置

```bash
proxy_redirect off;
proxy_set_header Host $http_host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
```

```bash
   location / {
        proxy_redirect off;
		proxy_set_header Host $http_host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_pass http://myserver;
        }
```

这些配置行通常用于配置一个反向代理服务器，解释一下每一行的含义：

1. `proxy_redirect off;`: 这一行配置禁用代理服务器对后端服务器响应中的重定向 URL 进行自动修改。通常情况下，如果后端服务器返回一个重定向响应，代理服务器会尝试修改其中的URL，以便正确地返回给客户端。这里设置为`off`表示禁用这种自动重定向。
2. `proxy_set_header Host $http_host;`: 这一行用于设置代理请求的HTTP头部。它将客户端发送的原始`Host`头部的值传递给后端服务器。这是为了确保后端服务器能够正确识别客户端请求的目标主机。
3. `proxy_set_header X-Real-IP $remote_addr;`: 这一行设置了一个名为`X-Real-IP`的HTTP头部，其中包含了客户端的真实IP地址。这对于记录客户端的真实IP地址很有用，因为在代理服务器后面，`$remote_addr`可能代表了代理服务器的IP地址而不是客户端的IP地址。
4. `proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;`: 这一行设置了一个名为`X-Forwarded-For`的HTTP头部，其中包含了客户端的原始IP地址。它使用了`$proxy_add_x_forwarded_for`变量，该变量包含了之前通过代理传递的`X-Forwarded-For`头部的值，并将客户端的IP地址添加到其末尾。这有助于追踪请求经过的代理链。

这些配置行可以确保代理服务器正确地传递客户端请求到后端服务器，并在需要时记录客户端的真实IP地址和代理链信息。这些配置通常在Nginx等反向代理服务器的配置文件中使用。