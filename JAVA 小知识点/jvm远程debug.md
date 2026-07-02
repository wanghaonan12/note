# JVM 远程 DEBUG

## 1. 设置jvm运行参数

就是喜欢看图片,这是在制作 java 服务镜像时的启动参数!!!!!!!

![image-20241012172650756](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20241012172650756.png)

> 在 `JVM`运行时加上一下参数

```shell
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Xms6g -Xmx6g -jar /app.jar
```

- `-agentlib`: 这个参数用于加载本地的代理库（native agent library），以便对JVM进行监控和调试。
- `jdwp`: 这是Java Debug Wire Protocol的缩写，它是一个用于调试Java代码的协议。
- `transport=dt_socket`: 指定传输方式为套接字（Socket）传输。
- `server=y`: 表示启动的JVM将作为调试服务器运行。
- `suspend=n`: 指定当调试器连接时，JVM不会挂起执行（即应用程序会继续运行，而不是等待调试器连接）。
- `address=5005`: 指定调试服务器监听的端口号，这里是5005。

## 2. idea 配置

> 在 Run/Debug Configurations面板，点击左上角“+”号，然后选择“Remote JVM Debug”

![image-20241012170649260](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20241012170649260.png)

![image-20241012172607772](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20241012172607772.png)

## 3. 使用

使用就很简单,先启动服务,看到`jvm`动了,就可以点击这个新建启动项的`Debug` 按钮了 ,然后想在哪里打断点,就在哪里打断点.

**注意:** 

1. 远程代码与本地代码不一样,断点不生效
2. 远程的服务会停止,等待断点释放