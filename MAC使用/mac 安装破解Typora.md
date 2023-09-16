# 安装破解Typora

1. 官网下载安装Typora

[typora官网](https://typora.io/#download)

2. 修改文件

- 打开显示包内容

![image-20230916144257793](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916144257793.png)

- 进入目标文件夹

  /Contents/Resources/TypeMark/page-dist/static/js 

  > 如果找不到请直接检索js

  ![image-20230916144931199](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916144931199.png)

- 修改文件内容

![image-20230916144819607](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916144819607.png)

```bash
# 搜索
hasActivated="true"==e.hasActivated
# 替换
hasActivated="true"=="true"
```

3. 最后打开就结束了!!!

![image-20230916145019465](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230916145019465.png)