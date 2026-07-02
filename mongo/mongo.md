# MongoDB Docker 安装

```shell
docker run --name mongo -d \
-p 27017:27017 \
-v /Users/richwang/Documents/Tools/docker/volume/mongodb:/data/db \
-e MONGO_INITDB_ROOT_USERNAME=root \
-e MONGO_INITDB_ROOT_PASSWORD=123456 \
mongo
```

