[arthas神器--watch查看方法入参、出参、类成员变量_arthas 查看入参-](https://blog.csdn.net/qq_38586496/article/details/110181963)

[spring boot 集成arthas实现线上远程监控调优 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/610030983)

https://arthas.aliyun.com/



[引入Arthas Spring Boot Starter工程后本地启动多个应用报错_arthas failed to bind telnet or http port! telnet -CSDN博客](https://blog.csdn.net/whereabouts_/article/details/128122709?spm=1001.2101.3001.6650.5&utm_medium=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-5-128122709-blog-126762866.235^v43^pc_blog_bottom_relevance_base9&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-5-128122709-blog-126762866.235^v43^pc_blog_bottom_relevance_base9)

官方docker 使用

```bash
 docker run --name math-game -it hengyunabc/arthas:latest /bin/sh -c "java -jar /opt/arthas/math-game.jar"
```





```bash
docker run -dp 6060:8080 -p 7777:7777 -p 8563:8563 -p 3658:3658 --name arthas-tunnel-server pde/arthas-tunnel-server.jar:10.1.0
```

```
ognl '@com.kerwin.arthas.demo.OgnlDemo02@ognlDemo02.getS1()' 
```

```yml
arthas:
  agent-id: ${AGENT_ID:ro}
  username: pde
  password: pde88888Pde
  tunnel-server: ${TUNNEL_SERVER:ws://10.20.40.104:7777/ws}
  #  处理 arthas 组件使用时一台机器启动多个服务 8563，3658 端口占用问题
  telnetPort: -1
  httpPort: -1
```



# arthas 使用案例

进入界面 [Arthas web](http://10.20.40.104:6060/)，登录报错的服务

![image-20240428154124027](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428154124027.png)

## 排错案例

接口`/pdes-ams-ro/accession/batch/archiveSettings/getList`实现类中添加了报错代码

![image-20240428153635735](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428153635735.png)

1. trace定位报错接口

   > 使用 swagger 或是在系统上点击报错接口

   ```bash
   trace com.pde.pdes.ro.controller.RoArchiveSettingsController *
   ```

   - `com.pde.pdes.ro.controller.RoArchiveSettingsController`: 类路径
   - `*`:匹配的方法名

   ![image-20240428154706720](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428154706720.png)

   查看控制台发现`com.pde.pdes.ro.controller.RoArchiveSettingsController` 类中的` getList()`  方法出的问题，而最终导致的是`com.pde.pdes.ro.service.RoArchiveSettingsService` 实现类中的 `getList()`方法`ArrayIndexOutOfBoundsException`下标越界异常

2. watch查看传参

   ```bash
   watch class-pattern method-pattern express [opition]
   -- 请求样例
   watch com.whn.controller.* * {params,returnObj} -x 2
   
   watch com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl getList  {params, returnObj}-x 2
   ```

   |            参数名称 | 参数说明                                                     |
   | ------------------: | :----------------------------------------------------------- |
   |     *class-pattern* | 类名表达式匹配                                               |
   |    *method-pattern* | 函数名表达式匹配                                             |
   |           *express* | 观察表达式，默认值：`{params, target, returnObj}`（）        |
   | *condition-express* | 条件表达式                                                   |
   |                 [b] | 在**函数调用之前**观察                                       |
   |                 [e] | 在**函数异常之后**观察                                       |
   |                 [s] | 在**函数返回之后**观察                                       |
   |                 [f] | 在**函数结束之后**(正常返回和异常返回)观察                   |
   |                 [E] | 开启正则表达式匹配，默认为通配符匹配                         |
   |                [x:] | 指定输出结果的属性遍历深度，默认为 1，最大值是 4（属性的展示深度，比如一个班级对象里面有一个学生列表，当x为1时，只展示学生列表，不会展示学生的属性，当x大于等于2时才会展示） |
   |         `[m <arg>]` | 指定 Class 最大匹配数量，默认值为 50。长格式为`[maxMatch <arg>]`。 |

   ![](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428163730099.png)

3. jad反编译`com.pde.pdes.ro.service.RoArchiveSettingsService` 类的 `getList` 方法

   ```bash
   jad --source-only com.pde.pdes.ro.service.RoArchiveSettingsService *
   ```

   - `--source-only`:使用类路径查找
   - `com.pde.pdes.ro.service.RoArchiveSettingsService`: 类路径
   - `*`:  方法名

   ![image-20240428161058979](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428161058979.png)

   发现是接口接着使用 `trace`查看调用路径

   ```bash
   trace com.pde.pdes.ro.service.RoArchiveSettingsService *
   ```

   ![image-20240428161239855](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428161239855.png)

   找到实现类是` com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl`继续反编译查看源码

   ```bash
   jad --source-only com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl getList
   ```

   ![image-20240428161752688](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428161752688.png)

## 不重启服务热更新代码测试

1. 导出源码

   ```bash
   jad --source-only com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl > D:/tmp/RoArchiveSettingsServiceImpl.java
   ```

   ![image-20240428164249502](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428164249502.png)

2. class文件替换

   - 查看类加载器

     ```bash
     sc -d com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl | grep classLoader
     ```

     ![image-20240428164639108](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428164639108.png)

   - 加载新修改的java文件

     ```bash
     mc -c 18b4aac2 D:/tmp/RoArchiveSettingsServiceImpl.java -d D:/tmp/test
     ```

     > 生成class文件出错，我们可以在idea中编译好class文件进行热加载

     ![image-20240428170329365](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428170329365.png)

   - 加载新的class文件

     ```bash
     retransform  D:/tmp/RoArchiveSettingsServiceImpl.class 
     ```

     ![image-20240428170538122](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428170538122.png)

   - 检查新的编译后的代码

     ```bash
     jad --source-only com.pde.pdes.ro.service.impl.RoArchiveSettingsServiceImpl getList
     ```

     ![image-20240428170924121](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428170924121.png)

   - 接口测试

     > 此时已经热加载完成，项目并没有重启并修改完成

     ![image-20240428170956187](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240428170956187.png)
