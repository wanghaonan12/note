> 本片文章前端部署思路是打包构建，转移到部署服务器

# docker 安装 jekens

```bash
docker run \
--env JAVA_OPTS="-server -Xms1024m -Xmx2048m -XX:PermSize=512m -XX:MaxPermSize=512m" \
--name docker-jenkins \
--privileged=true \
--restart=on-failure \
-itd \
-p 8080:8080 \
-p 50000:50000 \
-e JENKINS_OPTS='--prefix=/jenkins' \
-e TZ='Asia/Shanghai' \
-e JENKINS_ARGS='--prefix=/jenkins' \
-v /home/pde/jenkins/jenkins_home:/var/jenkins_home \
-v /etc/localtime:/etc/localtime \
jenkins/jenkins:lts-jdk11
```

# 一、node环境

## 安装node插件

> jenkins->系统管理->插件管理->available Plugins 中检索`nodejs`勾选并安装
>
> 我的已经安装过了所以检索不出来，第二张是在网上找了一张图

![image-20240621140936369](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240621140936369.png)

![image-20240621141320082](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240621141320082.png)

## 配置node参数

![image-20240621150740397](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240621150740397.png)

# 二、创建工程

>  创建自由风格的项目

![image-20240624140415892](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624140415892.png)

## 前期配置

> 前期配置是设置的一些环境变量在运行服务器脚本的时候用到的，方便修改管理和使用，下面的是配置好构建参数后运行时的样子
>
> **$ServiceIp**:  转移的服务器Ip
>
> **$ServiceName**: 转移的服务器地名字
>
> **$RemovePath**: 转移的一个临时文件夹，当然也可以直接转移`scp`
>
> **$TargetPath**: 转移至服务的某个地址
>
> **$DemoPath**:  在 `jenkins` 就行代码拉取后的代码位置
>
> ![image-20240624141208836](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141208836.png)

**单选框配置**

![image-20240624141331950](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141331950.png)

**字符串配置**

![image-20240624141356579](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141356579.png)

![image-20240624141420979](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141420979.png)

![image-20240624141437052](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141437052.png)

![image-20240624141445663](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141445663.png)

## 源码管理配置

![image-20240624141522457](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141522457.png)

## 构建环境

![image-20240624141541298](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624141541298.png)

## 构建步骤

### 前端依赖配置

>  由于node_models 每次下载都需要大量时间所以而且有一些是无法下载的，所以将提前准备好的依赖文件复制前端 `node_models`中
>
> 调用**`cp_node_module.sh`** 脚本将前端的依赖转移到 `jenkins` 持久化文件的工作空间下这个项目对应前端项目的文件下

![image-20240624142006228](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624142006228.png)

### npm参数展示及配置

> 展示并配置 node 的配置

![image-20240624142106246](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624142106246.png)

```sh
#!/bin/bash
node -v
npm -v
npm config set registry http://registry.npmmirror.com/
vue -V
```

### 服务打包

> 执行脚本，这里执行的环境是 Jenkins 配置的环境，就是我们选择的构建环境下执行的脚本
>
> 这里拉下来了两个服务，所以在脚本中会 cd 进去每个服务进行打包构建`$WORKSPACE`是`jenkins`的环境变量
> 如果有后期添加的依赖可以在这里进行 install 也可以更新依赖文件夹

![image-20240624142418735](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624142418735.png)

```sh
echo '构建服务'
cd $WORKSPACE/sy-management-system
export NODE_OPTIONS=--openssl-legacy-provider
npm install
npm install --save css-unicode-loader pinyin-match
npm run build:prod
```

### 前端包转移至指定服务器

> 这里是我们指定的服务器所执行的脚本。需要创建指定服务器的凭据
>
> 在这一部是将所打包好的文件转移到指定服务器！

![image-20240624153442284](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240624153442284.png)

```sh
cp -r $DemoPath/archives-management-system/ams $RemovePath/ams
rm -rf $DemoPath/archives-management-system/ams
rm -rf $DemoPath/archives-management-system/node_modules
cp -r $DemoPath/sy-management-system/sys $RemovePath/sys
rm -rf $DemoPath/sy-management-system/sys
rm -rf $DemoPath/sy-management-system/node_modules
sh /home/pde/jenkins/move_package.sh << EOF
$ServiceIp
$ServiceName
$RemovePath
$TargetPath
EOF
```

# 脚本文件

**`cp_node_module.sh`**

> 仅限参考每个项目的结构不同所以也会有所不同

```sh
#!/bin/bash
# 配置构建信息
echo '请输入 node_modules 位置:'
read nodeModulePath

echo '请输入前端代码位置:'
read demoPath

# 检查输入的路径是否为空
if [ -z "$nodeModulePath" ] || [ -z "$demoPath" ]; then
  echo "错误: node_modules 位置或前端代码位置不能为空！"
  exit 1
fi

# 拼接参数
amsPath="$nodeModulePath/ams/*"
sysPath="$nodeModulePath/sys/*"
pdesAms="$demoPath/archives-management-system/node_modules/"
pdesSys="$demoPath/sy-management-system/node_modules/"

# 打印输入信息
echo "前端代码位置: $demoPath"
echo "node_modules 位置: $nodeModulePath"
echo "ams node_modules 复制路径: $pdesAms"
echo "sys node_modules 复制路径: $pdesSys"

# 检查 node_modules 子路径是否存在
if [ ! -d "$nodeModulePath/ams" ] || [ ! -d "$nodeModulePath/sys" ]; then
  echo "错误: $nodeModulePath 下的 ams 或 sys 目录不存在！"
  exit 1
fi

# 删除旧的 node_modules 目录
rm -rf "$pdesAms" "$pdesSys"
mkdir -p "$pdesAms" "$pdesSys"

# 检查 mkdir 是否成功
if [ $? -ne 0 ]; then
  echo "错误: 创建目标目录失败！"
  exit 1
fi

# 复制文件
cp -r $amsPath "$pdesAms"
if [ $? -ne 0 ]; then
  echo "错误: 复制 $amsPath 到 $pdesAms 失败！"
  exit 1
fi

cp -r $sysPath "$pdesSys"
if [ $? -ne 0 ]; then
  echo "错误: 复制 $sysPath 到 $pdesSys 失败！"
  exit 1
fi

# 授权
chmod 777 -R "$pdesAms"
if [ $? -ne 0 ]; then
  echo "错误: 授权 $pdesAms 失败！"
  exit 1
fi

chmod 777 -R "$pdesSys"
if [ $? -ne 0 ]; then
  echo "错误: 授权 $pdesSys 失败！"
  exit 1
fi

echo "操作完成，文件已成功复制并授权。"
```

**`move_package.sh`**

> 需要添加copy目标服务器的ssh认证
>
> > 生产环境慎重考虑！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
>
> ```sh
> #生成空的密钥
> ssh-keygen -t rsa -b 2048 -f /root/.ssh/id_rsa -N ""
> 
> #将密钥添加到指定的服务器
> # ssh-copy-id serviceName@serviceIp
> ssh-copy-id root@127.0.0.1
> ```
>
> 

```sh
#!/bin/bash
# 从命令行获取参数
read -p "请输入服务器地址: " SERVER_ADDRESS
read -p "请输入用户名: " USERNAME
read -p "请输入要传输的文件路径: " FILE_TO_COPY
read -p "请输入目标路径: " TARGET_PATH

# 判断目标文件是否为空
if [ $(stat --format=%s $FILE_TO_COPY) -eq 4096 ]; then
  echo "Directory is empty"
  exit 1
else
  echo "Directory is not empty"
fi

# 执行 scp 传输
echo "正在将文件复制到服务器：$SERVER_ADDRESS"
scp -r "$FILE_TO_COPY"/* "$USERNAME@$SERVER_ADDRESS:$TARGET_PATH"

if [ $? -eq 0 ]; then
  echo "文件成功复制到 $SERVER_ADDRESS"
else
  echo "文件复制到 $SERVER_ADDRESS 失败"
fi
rm -rf $FILE_TO_COPY/*
echo "文件传输操作完成。"
```

