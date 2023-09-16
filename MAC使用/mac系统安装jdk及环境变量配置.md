# mac系统安装jdk及环境变量配置

1. 下载安装jdk

**[Java JDK下载地址](https://www.oracle.com/cn/java/technologies/downloads/archive/)**

> 选择java archive模块里面包含全部的java jdk 版本选择自己需要的版本进行下载，

![image-20230916133750838](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916133750838.png)

选择下载安装程序

![image-20230916134132174](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916134132174.png)

2. 安装jdk

> 现在就可以安装了，打开之后直接下一步下一步就行我用之前的jdk8演示
>
> **注意**：不需要更改任何参数直接下一步点到底

![image-20230916134357230](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916134357230.png)

3. 配置环境变量

> 接下来就是紧张刺激的配置环境变量的环节

- 使用下面的命令获取jdk安装地地址

```bash
/usr/libexec/java_home -V
```

> 接下来会显示有三个地址,我们选择中间的那一个

![image-20230916134725293](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916134725293.png)

- 配置环境变量

```shell
#到根目录
cd ~
#创建.bash_profile文件 ,如果有这个文件直接打开就好了
touch .bash_profile
#使用文本编辑器打开
open -a TextEdit.app .bash_profile

```

> 打开之后就是这样子,我这里面配置了其他的参数
>
> 配置`JAVA_HOME`和`PATH`然后`export`出去就好了
>
> 记得点击保存!!!!!!!!!!!!

![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916135643656.png)

```bash
#java
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home
PATH=$JAVA_HOME/bin:$PATH 
export JAVA_HOME
export PATH

#maven
MAVEN_HOME=/Users/richwang/Documents/tool/maven3.8.8
PATH=$MAVEN_HOME/bin:$PATH
export MAVEN_HOME
export PATH
```

- 刷新环境变量配置

```shell
#刷新环境变量
source ~/.bash_profile
#测试环境变量
java -version
```

![image-20230916140218586](/Users/richwang/Library/Application Support/typora-user-images/image-20230916140218586.png)

> maven的环境变量设置也是一样的,设置maven路径