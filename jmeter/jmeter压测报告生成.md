

# jmeter 安装

[JMeter - 官网下载地址](https://jmeter.apache.org/download_jmeter.cgi)

**注意：需要java8+的环境**

解压后运行`.bat`文件就可以

![image-20240423165647123](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423165647123.png)

# jmeter 中文设置

打开后设置中文：

Opitions->choose Language ->chinese 就行

![image-20240423165811727](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423165811727.png)

# jmeter 页面

1. 新建线程

   ![image-20240423170041200](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170041200.png)

2. 配置线程参数

   ![image-20240423170112006](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170112006.png)

3. 新建http请求

   ![image-20240423170150860](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170150860.png)

4. 配置http请求

   这里就根据实际状况配置一下啊就行了。在高级里面可以设置响应超时等参数。

   配置请求地址，协议，端口，uri，请求类型。

   ![image-20240423170313189](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170313189.png)

   ![image-20240423170426461](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170426461.png)

5. 添加组件

   在这里配置请求头，cookie等参数。

   ![image-20240423170602939](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170602939.png)

   在框框里操作啊，从粘贴板添加最方便。教程太详细了，cookie也是一样的。

   ![image-20240423170735023](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423170735023.png)

6. 监控组件

   在**请求组件**里面和**线程**里面都可以添加：

   请求组件：针对当前请求的参数进行处理统计

   线程组件：针对当前线程的请求进行统计（可以进行对比）

   ![image-20240423171147204](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423171147204.png)

   **请求组件**：针对当前请求的参数进行处理统计

   **线程组件**：针对当前线程的请求进行统计（可以与多个请求进行对比）

   ![image-20240423172141593](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172141593.png)

# 插件管理器安装

插件文档：https://jmeter-plugins.org/wiki/Start/
插件管理器安装：https://jmeter-plugins.org/install/Install/

1. 下载后解压并放到`jmeter`目录下的`/lib/ext`文件夹下面

![image-20240423172344723](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172344723.png)

![image-20240423172507513](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172507513.png)

2. 重启`jmeter`在Opition中多了一个`plugin manager`

   ![image-20240423172622272](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172622272.png)

   ![image-20240423172738652](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172738652.png)

# jmeter插件安装

标准插件下载地址：https://jmeter-plugins.org/downloads/old/

官方组件和之前的`plugin manager`的安装一样下载->解压->放在`jmeter目录下的`lib/ext/目录下便可

![image-20240423172800860](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172800860.png)

重启便能看到安装进去的组件了

![image-20240423172949456](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423172949456.png)

# 生成压测报告

在`jmeter`文件`bin`目录下执行或者将这个目录放在环境变量中

```bash
jmeter -n -t D:\DeskTop\clutter\jmeter脚本\105环境P10利用性能.jmx -l D:\tmp\105result.jtl -e -o D:\tmp\ResultReport
```

> 环境要求
> 1：jmeter3.0版本之后开始支持动态生成测试报表
>
> 2：jdk版本1.8以上
>
> 3：需要jmx脚本文件
>
> 基本操作
>
> 1：在你的脚本文件路径下，执行cmd命令：jmeter -n -t test.jmx -l result.jtl -e -o /tmp/ResultReport
>
> 参数说明:
> ● -n: 非GUI模式执行JMeter
> ● -t: 执行测试文件所在的位置（新建的项目保存文件）
> ● -l: 指定生成测试结果的保存文件，jtl文件格式
> ● -e: 测试结束后，生成测试报告
> ● -o: 指定测试报告的存放位置
>
> 注意：结尾的 ResultReport 是自己手动创建的报告文件夹。每次启动命令之前，文件夹内容必须和 jtl 文件一起清空

![image-20240423173741501](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423173741501.png)

在`-l`设置的位置会有一个`.jtl`文件

在`-o`设置的位置会有一堆文件`.html`文件就是生成的报告

![image-20240423173813618](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423173813618.png)

打开`.html`

![image-20240423173909368](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423173909368.png)

> 压测文件模板

 [jmeter_p10_ar_压测.jmx](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/document/jmeter_p10_ar_%E5%8E%8B%E6%B5%8B.jmx) 

# 修改报表生成粒度

在刚生成报告的文档里面他的时间颗粒度为`1min`只有一个小点，时间节点太长了，无法直观看到数据,我这里却是`100ms`

![image-20240423174014810](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423174014810.png)

在`jmeter`目录的`/bin`下面的`reportgenerator.properties`中修改参数就好了。

```properties
# jmeter.reportgenerator.overall_granularity=6000
jmeter.reportgenerator.overall_granularity=100
```

![image-20240423174348903](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240423174348903.png)