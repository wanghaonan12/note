# svn代码提交数据统计

> 使用svn版本控制工具进行代码量统计

1. 下载代码量统计工具

[代码量统计工具 statsvn.jar V0.7.1](https://github.com/AusHick/StatSVN/releases/tag/v0.7.1)

![image-20230615165713047](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230615165713047.png)

2. 在项目目录运行命令（有.svn文件的目）

```shell
svn log -v --xml -r {2023-06-01}:{2023-06-15} > d:\6021.log

手动注释：{统计开始时间}：{统计结束时间} > 日志导出路径（目录需要存在）

svn log -v --xml > d:\6021.log
获取全部信息
```

生成日志文件

![image-20230615170121502](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230615170121502.png)

3. 在代码量统计工具目录下执行

```shell
java -jar statsvn.jar D:\6021.log D:\DeskTop\document\demo\work\P10.0.0 -charset gbk -output-dir D:\tjCode

注释：java -jar 插件位置 导出的日志位置 源码位置（要到能看到.svn的路径） -charset gbk -output-dir 生成的文件导出路径（路径要存在）
```

4. 等待执行

![image-20230615170840991](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230615170840991.png)

5. 打开生成文件中的index.html

![image-20230615170421906](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230615170421906.png)

6. 查看信息

![image-20230615170500966](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230615170500966.png)

当然在里面还有好多其他的数据统计，代码提交量，修改量，添加量等。