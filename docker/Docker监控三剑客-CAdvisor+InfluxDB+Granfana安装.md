## Docker容器监控-CAdvisor+InfluxDB+Granfana

​	Docker容器监控（Container Monitoring）是一种追踪、测量和分析Docker容器运行时性能和行为的方法。在Docker容器监控中，通常使用一组工具来收集和可视化容器的指标数据，以便及时发现问题、优化性能和规划资源。

1. **CAdvisor（容器监控工具）**

​	CAdvisor是由Google开发的开源容器监控工具，它能够自动收集Docker容器的性能指标数据，如CPU使用率、内存使用量、网络流量等，并将这些数据提供给用户。

2. **InfluxDB（时序数据库）**

​	InfluxDB是一个开源的时序数据库，特别适用于存储时间序列数据。在Docker容器监控中，InfluxDB用于存储CAdvisor收集到的容器指标数据。

3. **Grafana（可视化工具）**

​	Grafana是一个开源的数据可视化和监控平台，它可以连接各种数据源（包括InfluxDB），并将数据以图表、仪表盘的形式展示出来。在Docker容器监控中，Grafana用于可视化InfluxDB中的容器指标数据，提供直观的监控界面。

​	使用docker-compose安装

```bash
version: '3.1' # Docker Compose文件的版本

volumes:
  # 定义数据卷
  grafana_data: {} # Grafana的数据卷

services:
  # 定义服务列表
  influxdb:
    # InfluxDB服务
    image: tutum/influxdb:0.9 # 使用InfluxDB 0.9镜像
    restart: always # 容器退出时总是重启
    environment:
      # 环境变量配置
      - PRE_CREATE_DB=cadvisor # 预先创建cadvisor数据库
    ports:
      # 端口映射
      - "8083:8083" # InfluxDB管理界面端口
      - "8086:8086" # InfluxDB API端口
    volumes:
      # 挂载数据卷
      - ./data/influxdb:/data # 将本地目录映射到InfluxDB容器的/data目录下

  cadvisor:
    # Cadvisor服务
    image: google/cadvisor # 使用Google Cadvisor镜像
    links:
      # 链接到InfluxDB服务
      - influxdb:influxsrv # 将influxdb服务命名为influxsrv并链接到cadvisor容器
    command: -storage_driver=influxdb -storage_driver_db=cadvisor -storage_driver_host=influxsrv:8086 # 启动参数，使用InfluxDB作为存储驱动
    restart: always # 容器退出时总是重启
    ports:
      # 端口映射
      - "8085:8080" # Cadvisor Web界面端口
    volumes:
      # 挂载文件和目录
      - /:/rootfs:ro # 挂载根目录为只读
      - /var/run:/var/run:rw # 挂载/var/run目录为读写
      - /sys:/sys:ro # 挂载/sys目录为只读
      - /var/lib/docker/:/var/lib/docker:ro # 挂载/var/lib/docker目录为只读

  grafana:
    # Grafana服务
    user: "104" # 指定用户ID
    image: grafana/grafana # 使用Grafana镜像
    restart: always # 容器退出时总是重启
    links:
      # 链接到InfluxDB服务
      - influxdb:influxsrv # 将influxdb服务命名为influxsrv并链接到grafana容器
    ports:
      # 端口映射
      - "3000:3000" # Grafana Web界面端口
    volumes:
      # 挂载Grafana数据卷
      - grafana_data:/var/lib/grafana # 将grafana_data数据卷映射到/var/lib/grafana目录下
    environment:
      # 环境变量配置
      - HTTP_USER=admin # Grafana登录用户名
      - HTTP_PASS=admin # Grafana登录密码
      - INFLUXDB_HOST=influxsrv # InfluxDB主机名
      - INFLUXDB_PORT=8086 # InfluxDB端口
      - INFLUXDB_NAME=cadvisor # InfluxDB数据库名称
      - INFLUXDB_USER=root # InfluxDB用户名
      - INFLUXDB_PASS=123456 # InfluxDB密码
```

1. 创建docker-compose.yml文件执行`docker-compose up -d`
2. 检查启动状况(账号密码在配置中有写都是`admin`)

![image-20231031135113969](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031135113969.png)

3. 配置`Grafana`数据源

![image-20231031135507957](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031135507957.png)

4. 连接数据库

![image-20231031140157067](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031140157067.png)

5. 创建面板

> 跟着点

![image-20231031141501974](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141501974.png)

> 选则数据源

![image-20231031141529100](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141529100.png)

> 配置查询条件`Time series`那个下拉框可以选则不同的图标样式

![image-20231031141921078](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20231031141921078.png)

[学习视频笔记地址](blob:https://github.com/7a30ee28-c241-44d3-a53b-15d96e106143)
