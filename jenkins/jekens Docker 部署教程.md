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

