# 基础镜像使用java
FROM java:8
# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为zzyy_docker.jar
ADD springboot.jar springboot.jar
# 运行jar包
RUN bash -c 'touch /springboot.jar'
ENTRYPOINT ["java","-jar","/springboot.jar"]
#暴露8888端口作为微服务
EXPOSE 8888  