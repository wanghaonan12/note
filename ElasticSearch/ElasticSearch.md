# ElasticSearch开篇引导

[Elasticsearch](https://blog.csdn.net/u011863024/article/details/115721328)

[Elasticsearch: 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/running-elasticsearch.html)

结构化数据(能以二维表形式展开的数据信息)

![image-20230918211747699](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230918211747699.png)

非结构化数据(视频,报表,图片...通常使用nosql数据库存储)

![image-20230918211804920](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230918211804920.png)

半结构化数据(xml,yml...)

![image-20230918212115672](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230918212115672.png)

## ElasticSearch简介

### 什么是ElasticSearch

Elasticsearch 是一个开源的分布式搜索和分析引擎，最初由 Elastic 公司开发，用于处理大规模数据的搜索、分析和可视化。它构建在 Apache Lucene 基础之上，提供了强大的全文搜索、实时数据分析和日志存储等功能。能够安全可靠地获取任何来源、任何格式的数据，然后实时地对数据进行搜索、分析和可视化。

Elaticsearch,简称为ES,ES是一个开源的高扩展的分布式全文搜索引擎，是整个ElasticStack技术栈的核心。它可以近乎实时的存储、检索数据：本身扩展性很好，可以扩展到上百台服务器，处理PB级别的数据。

The Elastic Stack, 包括 Elasticsearch、 Kibana、 Beats 和 Logstash（也称为 ELK Stack）。能够安全可靠地获取任何来源、任何格式的数据，然后实时地对数据进行搜索、分析和可视化。

### 特点和用途

1. **分布式架构**：Elasticsearch 是一个分布式系统，可以水平扩展以处理大规模数据。数据可以分散存储在多个节点上，提供了高可用性和性能。

2. **全文搜索**：Elasticsearch 提供了强大的全文搜索功能，支持各种复杂的查询，如匹配、模糊搜索、多字段搜索、范围搜索等。

3. **实时数据**：它支持实时数据索引和查询，适用于需要实时监控、日志分析、仪表盘等应用场景。

4. **多种数据类型支持**：除了文本数据，Elasticsearch 也可以存储和搜索结构化数据、地理空间数据和时间序列数据等。

5. **分析和聚合**：Elasticsearch 提供了聚合功能，可以对数据进行统计、分组、排序等操作，用于生成数据报告和可视化。

6. **开源和生态系统**：它是开源项目，拥有丰富的插件和生态系统，包括 Kibana（数据可视化工具）、Logstash（日志收集和处理工具）以及 Beats（数据采集器）等工具。

7. **RESTful API**：Elasticsearch 使用 RESTful API 进行与应用程序的交互，使其易于集成到各种编程语言和应用中。

8. **安全性**：Elasticsearch 提供了身份验证、授权、加密和审计功能，以确保数据的安全性。

9. **弹性和可扩展性**：Elasticsearch 具有自动恢复能力，可以处理节点故障，并支持动态扩展和缩小集群。

10. **多种用途**：Elasticsearch 可以用于多种用途，包括搜索引擎、日志和事件数据存储、电子商务网站搜索、应用程序性能监控等。

总之，Elasticsearch 是一个功能强大、高度可定制且可扩展的搜索和分析引擎，被广泛用于处理大规模数据、构建实时应用和生成数据洞察。它在各个行业和领域中都有广泛的应用，帮助组织更好地理解和利用其数据资源。

## ES 名词核心理念解释

### 索引(Index)

​	索引就是一个拥有几分**相似特征的文档的集合**(**类似关系数据库的表**)。比如说，你可以有一个客户数据的 索引，另一个产品目录的索引，还有一个订单数据的索引。一个索引由一个名字来标识（必须全部是小写字母），并且当我们要对这个索引中的文档进行索引、搜索、更新和删除的时候，都要使用到这个名字。在一个集群中，可以定义任意多的索引。  能搜索的数据必须索引，这样的好处是可以提高查询速度，比如：新华字典前面的目录 就是索引的意思，目录可以提高查询速度。  

 **Elasticsearch索引的精髓：一切设计都是为了提高搜索的性能。** 

### 类型(Type)

​	在一个索引中，你可以定义一种或多种类型。  一个类型是你的索引的一个逻辑上的分类/分区，其语义完全由你来定。通常，会为具 有一组共同字段的文档定义一个类型。不同的版本，类型发生了不同的变化 （在5.x之后就不再支持了，有带你像是2级分类）

| 版本 | Type                                           |
| ---- | ---------------------------------------------- |
| 5.x  | 支持多种 type                                  |
| 6.x  | 只能有一种 type                                |
| 7.x  | 默认不再支持自定义索引类型（默认类型为：_doc） |

### 文档(Document)

​	一个文档是一个可被索引的基础信息单元（**关系型数据库的条目**），也就是一条数据  比如：你可以拥有某一个客户的文档，某一个产品的一个文档，当然，也可以拥有某个 订单的一个文档。文档以 JSON（Javascript Object Notation）格式来表示，而 JSON 是一个 到处存在的互联网数据交互格式。  在一个 index/type 里面，你可以存储任意多的文档。 

### 字段(Field)

​	相当于是数据表的字段，对文档数据根据不同属性进行的分类标识。

### 映射(Mapping)

​	mapping 是处理数据的方式和规则方面做一些限制（**设置字段属性等约束**），如：某个字段的数据类型、默认值、 分析器、是否被索引等等。这些都是映射里面可以设置的，其它就是处理 ES 里面数据的一 些使用规则设置也叫做映射，按着最优规则处理数据对性能提高很大，因此才需要建立映射， 并且需要思考如何建立映射才能对性能更好。 

### 分片(Shards)

​	一个索引可以存储超出单个节点硬件限制的大量数据。比如，一个具有 10 亿文档数据 的索引占据 1TB 的磁盘空间，而任一节点都可能没有这样大的磁盘空间。或者单个节点处 理搜索请求，响应太慢。为了解决这个问题，Elasticsearch 提供了将索引划分成多份的能力， 每一份就称之为分片。当你创建一个索引的时候，你可以指定你想要的分片的数量。每个**分片本身也是一个功能完善并且独立的“索引”**，这个“索引”可以被放置到集群中的任何节点 上。  分片很重要，主要有两方面的原因：

1. 允许你水平分割 / 扩展你的内容容量。
2. 允许你在分片之上进行分布式的、并行的操作，进而提高性能/吞吐量。  

​	至于一个分片怎样分布，它的文档怎样聚合和搜索请求，是完全由 Elasticsearch 管理的， 对于作为用户的你来说，这些都是透明的，无需过分关心。  被混淆的概念是，一个 Lucene **索引**我们在 Elasticsearch 称作**分片** 。 一个  Elasticsearch **索引是分片的集合。** 当 Elasticsearch 在索引中搜索的时候， 他发送**查询到每一个属于索引的分片**(Lucene 索引)，**然后合并**每个分片的结果到一个全局的结果集。

### 副本(Replicas)

​	在一个网络 / 云的环境里，失败随时都可能发生，在某个分片/节点不知怎么的就处于 离线状态，或者由于任何原因消失了，这种情况下，有一个故障转移机制是非常有用并且是强烈推荐的。为此目的，Elasticsearch 允许你**创建分片的一份或多份拷贝，这些拷贝叫做复 制分片(副本)。**  复制分片之所以重要，有两个主要原因：

- 在分片/节点失败的情况下，提供了高可用性。因为这个原因，注意到复制分片从不与 原/主要（original/primary）分片置于同一节点上是非常重要的。 
- 扩展你的搜索量/吞吐量，因为搜索可以在所有的副本上并行运行。 

​	总之，每个索引可以被分成多个分片。一个索引也可以被复制 0 次（意思是没有复制） 或多次。一旦复制了，每个索引就有了主分片（作为复制源）和复制分片（拷贝的）之别。分片和复制的数量可以在索引创建的时候指定。在索引创建之后，**你可以在任何时候动态地改变复制的数量，但是你事后不能改变分片的数量。**默认情况下， Elasticsearch 中的每个索引被分片 1 个主分片和 1 个复制，这意味着，如果你的集群中至少 有两个节点，你的索引将会有 1 个主分片和另外 1 个复制分片（1 个完全拷贝），这样的话 每个索引总共就有 2 个分片，需要根据索引需要确定分片个数。

### 分配(Allocation)

将分片分配给某个节点的过程，包括分配主分片或者副本。如果是副本，还包含从主分 片复制数据的过程。这个过程是由 master 节点完成的。 

# ElasticSearch入门

## 1. elasticSearch安装

### docker安装部署

> 一直在尝试添加数据卷,但是每次只要添加就会报错,这里就为了能够继续下去没有添加,会在后面补充

```bash
docker run -d \
     -p 9200:9200 \
     -p 9300:9300 \
     --name es01 \
     --network es_network \
     -e "discovery.type=single-node" \
     -e ES_JAVA_OPTS="-Xms100m -Xmx200m" \
     -e TAKE_FILE_OWNERSHIP=true \
 elasticsearch:7.17.5

```

![image-20230920101706637](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230920101706637.png)

**验证**

![image-20230920102008514](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230920102008514.png)

> 添加容器数据卷

```bash
 docker run -it \
     -p 9200:9200 \
     -p 9300:9300 \
     --name es00 \
     --network es_network \
     -v /home/es00/plugins:/usr/share/elasticsearch/plugins \
     -v /home/es00/data:/usr/share/elasticsearch/data \
     -v /home/es00/log:/usr/share/elasticsearch/logs \
     -v /home/es00/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
     -e "discovery.type=single-node" \
     -e ES_JAVA_OPTS="-Xms200m -Xmx200m" \
     -e TAKE_FILE_OWNERSHIP=true \
 elasticsearch:7.17.5
 
  docker run -it \
     -p 9201:9200 \
     -p 9301:9300 \
     --name es01 \
     --network es_network \
     -v /home/es01/plugins:/usr/share/elasticsearch/plugins \
     -v /home/es01/data:/usr/share/elasticsearch/data \
     -v /home/es01/log:/usr/share/elasticsearch/logs \
     -v /home/es01/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
     -e ES_JAVA_OPTS="-Xms200m -Xmx200m" \
     -e TAKE_FILE_OWNERSHIP=true \
 elasticsearch:7.17.5
```

![image-20230921102536190](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921102536190.png)

**验证**

![image-20230921102612716](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921102612716.png)

> 添加分词器

1. 官网下载所需分词器,我这里使用ik分词器
   [ik分词下载地址](https://github.com/medcl/elasticsearch-analysis-ik/releases?page=2)

   > 注意分词器与es的版本需要保持一致,如果没有保持一致请下载相近的版本并修改版本信息如下:
   >
   > 解压ik分词器之后修改`plugin-descriptor.properties`文件中的`version`和`elasticsearch.version`与你使用的`es`版本保持一致

   ![image-20230921110323625](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921110323625.png)

2. 修改好之后找打我们创建的plugins数据卷,并创建ik文件夹将内容放进去
   ![image-20230921110656594](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921110656594.png)

3. 重启es容器

   ```bash
   docker restart es02
   ```

   ![image-20230921110734285](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921110734285.png)

4. 检验

![image-20230921111243245](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921111243245.png)

ik**分分词器与默认分词器区别**

> 1. 细粒度分词模式（ik_smart）：
> 这是默认的分词模式，它会尽可能地将句子切分为最小的词语单元。它不仅可以识别普通词汇，还可以识别一些常见的专有名词、地名、人名等。
>
> 2. 智能分词模式（ik_max_word）：
> 这种模式会在细粒度分词的基础上，对长词进行进一步的切分。它可以识别更多的词语，但也会增加一些不必要的词语。

![image-20230921111600801](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921111600801.png)

### docker集群部署

[docker-compose部署](https://blog.csdn.net/iampatrick_star/article/details/127263346)

**docker-compose.yml**

```yml
services:
  cerebro:
    image: lmenezes/cerebro:0.8.4
    container_name: cerebro
    ports:
      - "9000:9000"
    networks:
      - es_network
  kibana:
    image: kibana:7.17.5
    container_name: kibana
    volumes:
      - /home/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml
    ports:
      - 5601:5601
    networks:
      - es_network
  es00:
    image: elasticsearch:7.17.5
    container_name: es00
    restart: always
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - "ES_JAVA_OPTS=-Des.insecure.allow.root=true"
    volumes:
      - /home/es00/plugins:/usr/share/elasticsearch/plugins
      - /home/es00/data:/usr/share/elasticsearch/data
      - /home/es00/log:/usr/share/elasticsearch/logs
      - /home/es00/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
    ports:
      - 9200:9200
      - 9300:9300
    networks:
      - es_network
  es01:
    image: elasticsearch:7.17.5
    container_name: es01
    restart: always
    environment:
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - "ES_JAVA_OPTS=-Des.insecure.allow.root=true"
    volumes:
      - /home/es01/plugins:/usr/share/elasticsearch/plugins
      - /home/es01/data:/usr/share/elasticsearch/data
      - /home/es01/log:/usr/share/elasticsearch/logs
      - /home/es01/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
    ports:
      - 9201:9200
      - 9301:9300
    networks:
      - es_network

networks:
  es_network:
```

**es00/config/elasticsearch.yml**

```yml
# Elasticsearch集群名称
cluster.name: "es-cluster"

# 节点名称
node.name: es00

# 是否允许成为主节点
node.master: true

# 是否允许存储数据
node.data: true

# 索引数据存储路径
#path.data: /usr/share/elasticsearch/data

# 日志存储路径
#path.logs: /usr/share/elasticsearch/logs

# 内存锁定
#bootstrap.memory_lock: true

# 绑定的网络接口 0.0.0.0表示节点将监听所有可用的网络接口
network.host: 0.0.0.0

# HTTP通信端口
http.port: 9200

# 初始主节点列表
cluster.initial_master_nodes: ["es00"]

# 种子主机列表，用于发现其他节点,集群中的节点都在这个地址列表中
discovery.seed_hosts: ["es00","es01"]

# 启用CORS（跨域资源共享）
http.cors.enabled: true

# 允许CORS的来源
http.cors.allow-origin: "*"

# 启用Elasticsearch安全性
#xpack.security.enabled: false

# 启用传输层安全性（TLS/SSL）
#xpack.security.transport.ssl.enabled: false
```

**es01/config/elasticsearch.yml**

```yml
# Elasticsearch集群名称
cluster.name: "es-cluster"

# 节点名称
node.name: es01

# 是否允许成为主节点
node.master: true

# 是否允许存储数据
node.data: true

# 索引数据存储路径
#path.data: /usr/share/elasticsearch/data

# 日志存储路径
#path.logs: /usr/share/elasticsearch/logs

# 内存锁定
#bootstrap.memory_lock: true

# 绑定的网络接口 0.0.0.0表示节点将监听所有可用的网络接口
network.host: 0.0.0.0

# HTTP通信端口
http.port: 9200

# 初始主节点列表
cluster.initial_master_nodes: ["es00"]

# 种子主机列表，用于发现其他节点,集群中的节点都在这个地址列表中
discovery.seed_hosts: ["es00","es01"]

# 启用CORS（跨域资源共享）
http.cors.enabled: true

# 允许CORS的来源
http.cors.allow-origin: "*"

# 启用Elasticsearch安全性
#xpack.security.enabled: false

# 启用传输层安全性（TLS/SSL）
#xpack.security.transport.ssl.enabled: false
```

## 2. es可视化工具之kibana安装

```bash
 docker run \
     -dp 5601:5601 \
     --name kibana \
     --privileged=true \
     -v /home/kibana/config/kibana.yml:/usr/share/kibana/config/kibana.yml \
     kibana:7.17.5
```

```yml
server.host: "0.0.0.0"
server.shutdownTimeout: "5s"
elasticsearch.hosts: ["http://127.0.0.0:9200"]  # es地址
monitoring.ui.container.elasticsearch.enabled: true
```



![image-20230921184405693](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230921184405693.png)

![image-20230921184421661](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230921184421661.png)

## 3. es可视化工具之cerebro安装

```bash
docker run -dp 9000:9000  --network es_network --name cerebro lmenezes/cerebro:0.8.4
```

![image-20230922172328669](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230922172328669.png)

![image-20230922172422029](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230922172422029.png)

## 4. RestFul风格

RESTful风格是一种设计和构建网络应用程序的软件架构风格。它基于Representational State Transfer（资源表现层状态转化）的概念，通过使用HTTP协议中的几个关键操作（GET、POST、PUT、DELETE等）来对资源进行管理和操作。
在RESTful风格中，资源被视为应用程序中的一部分，每个资源都可以通过唯一的URL进行访问。资源的状态通过HTTP动词和数据表达，并使用HTTP状态码进行响应。
以下是RESTful风格的一些关键特点：

1. 资源定位：每个资源都有一个唯一的URL作为其标识。通过URL可以访问和操纵该资源。
2. 无状态性：服务器不保存客户端的状态信息，每个请求都是独立的。客户端的每个请求都必须包含足够的信息以使服务器能够理解。
3. 统一接口：RESTful风格使用统一的接口来操纵资源。这包括使用HTTP动词来表示操作（GET用于获取资源，POST用于创建资源，PUT用于更新资源，DELETE用于删除资源）和使用HTTP状态码来表示操作的结果。
4. 可缓存性：RESTful架构使用HTTP协议的缓存机制，提高性能和可伸缩性。
5. 完全分布式：RESTful架构支持分布式计算环境，因为资源可以在不同的系统之间传递和访问。
   总之，RESTful风格是一种基于HTTP协议的简洁、可扩展和可维护的软件架构风格，适用于构建各种类型的网络应用程序。它提供了一种简单的方式来设计和实现可靠的和可伸缩的分布式系统。

## 5. 倒排索引

> 传统的索引模式是就是
> 正排索引:正排索引是根据文档的顺序来建立索引的。
>
> 倒排索引:倒排索引是根据词汇来建立索引的。它将每个词汇作为索引的关键词，并建立一个倒排列表。

​	ES使用倒排索引来管理文档和进行全文搜索。倒排索引在ES中是以分片（Shard）的形式存储在集群中的不同节点上，并且支持水平扩展和数据分布。当文档被索引时，ES会将文档中的每个词汇提取出来，并将其添加到倒排索引中。倒排索引会记录每个词汇在哪些文档中出现以及出现的位置等相关信息。

**正排索引（传统）**

| id   | content              |
| ---- | -------------------- |
| 1001 | my name is zhang san |
| 1002 | my name is li si     |

**倒排索引**

| keyword | id         |
| ------- | ---------- |
| name    | 1001, 1002 |
| zhang   | 1001       |

# 高级使用

# ES使用操作

## 索引操作

### 1. 创建索引

> PUT请求是幂等的，即对同一个文档多次执行相同的PUT请求会产生相同的结果。这意味着多次执行PUT请求不会导致文档的重复或多次更新。而POST请求不是幂等的，多次执行相同的POST请求会创建多个相同的文档。

```json
PUT http://{{ServiceIP}}/index_name
```

![image-20230920220835051](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920220835051.png)

### 2. 检索创建的索引

```json
GET http://{{ServiceIP}}/_cat/indices?v
```

![image-20230920222035205](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920222035205.png)


|表头	|含义|
|------- | ---------- |
|health|	当前服务器健康状态： green(集群完整) yellow(单点正常、集群不完整) red(单点不正常)|
|status	|索引打开、关闭状态|
|index	|索引名|
|uuid	|索引统一编号|
|pri	|主分片数量|
|rep|	副本数量|
|docs.count|	可用文档数量|
|docs.deleted|	文档删除状态（逻辑删除）|
|store.size|	主分片和副分片整体占空间大小|
|pri.store.size|	主分片占空间大小|

### 3. 检索索引详情

```json
GET http://{{ServiceIP}}/index_name
```

![image-20230920223222083](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920223222083.png)

```json
{
    "shopping": { // 索引名称
        "aliases": {}, // 这是一个空对象，用于存储别名相关的配置信息。在此结果中，别名未定义。
        "mappings": {}, // 这是一个空对象，用于存储索引的映射信息，包括字段的数据类型、分词器等。在此结果中，映射未定义。
        "settings": { // 索引的设置信息，包含了索引级别的各种配置项
            "index": { // 索引级别的设置信息
                "routing": { // 路由策略的配置信息
                    "allocation": { //数据分片分配的配置信息
                        "include": { //包含数据分片分配相关配置的对象
                            "_tier_preference": "data_content" //控制数据分配偏好的配置项，指定了数据分配的偏好策略为"data_content"
                        }
                    }
                },
                "number_of_shards": "1", //索引的分片数，即在此结果中索引的分片数为1
                "provided_name": "shopping", // 索引的提供的名称，即在此结果中索引的名称为"shopping"
                "creation_date": "1695219617958", // 索引的创建日期，表示为一个时间戳
                "number_of_replicas": "1",  // 索引的副本数，即在此结果中索引的副本数为1
                "uuid": "ELK_h7rTT-ePH04m5OOsYg", //索引id
                "version": { // 索引的版本信息
                    "created": "7170599"  // 索引的创建版本号
                }
            }
        }
    }
}
```



### 4. 删除索引

```json
DELETE http://{{ServiceIP}}/index_name
```

![image-20230920221707813](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920221707813.png)

## 文档操作

### 1. 文档创建

> 索引已经创建好了，接下来我们来创建文档，并添加数据。这里的文档可以类比为关系型数 据库中的表数据，添加的数据格式为 JSON 格式

#### 添加随机id文档

```json
POST http://{{ServiceIP}}/index_name/_doc
```

> 在下面的id中可以看出是随机id

![image-20230920222940800](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920222940800.png)

```json
{
    "_index": "user",// 索引信息
    "_type": "_doc", // 文档类型
    "_id": "_sXs5I4BawIgNvjwO-SQ", //id
    "_version": 1, // 版本号，每一次对文档进行修改都会增加版本号
    "result": "created", //结果 这里的 created 表示创建成功
    "_shards": { //分片信息
        "total": 2, //分片总数2
        "successful": 1, //分片成功1
        "failed": 0 // 分片失败0
    },
    "_seq_no": 0, 
    "_primary_term": 1
}
```



#### 指定id添加文档

```json
POST http://{{ServiceIP}}/index_name/_doc/id
```

> 在下下面就能看出是指定好的id数据,方便用于管理

![image-20230920224134825](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920224134825.png)

#### 批量添加文档	

[批量 API |Elasticsearch 指南 [8.13\] |弹性的](https://www.elastic.co/guide/en/elasticsearch/reference/8.13/docs-bulk.html)

```bash
http://{{ServiceIP}}/_bulk
```

```json
{"index":{"_index":"books"}} // 这里指定添加文档的索引等信息
{"name":"Revelation Space","author":"Alastair Reynolds","release_date":"2000-03-15","page_count":585} // 这里时文档的json格式 内容
{"index":{"_index":"books"}}
{"name":"1984","author":"George Orwell","release_date":"1985-06-01","page_count":328}
{"index":{"_index":"books"}}
{"name":"Fahrenheit 451","author":"Ray Bradbury","release_date":"1953-10-15","page_count":227}
{"index":{"_index":"books"}}
{"name":"Brave New World","author":"Aldous Huxley","release_date":"1932-06-01","page_count":268}
{"index":{"_index":"books"}}
{"name":"The Handmaids Tale","author":"Margaret Atwood","release_date":"1985-06-01","page_count":311}
```

### 2. 文档查询

#### match检索

> `match`会将你搜索的`query`进行分词的查询方法和`mysql`中的模糊查询差不多

```json
POST http://{{ServiceIP}}/index_name/_search
{
  "query": {
    "match": {
      "field_name":"content"
    }
  }
}
```



![image-20230920225728595](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920225728595.png)

#### term 精确匹配检索

> term查询也是比较常用的一种查询方式，它和match的唯一区别就是match需要对query进行分词，而term是不会进行分词的，它会直接拿query整体和原文进行匹配。

#### match_phrase 分词精确检索

> match_phrase和match一样也是会对你的搜索query进行分词，但是，不同的是它不是匹配到某一处分词的结果就算是匹配成功了，而是需要query中所有的词都匹配到，而且相对顺序还要一致，而且默认还是连续的

```json
POST http://{{ServiceIP}}/index_name/_search
{
  "query": {
    "match_phrase": {
      "name":"search content"
    }
  }
}
```

在这里只有一条数据匹配

![image-20230920233837405](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920233837405.png)

#### multi_match多字段查询

> multi_match比是可以设置多个字段检索

```json
POST http://{{ServiceIP}}/user/_search
{
  "query": {
    "multi_match": {
      "query":"content",
      "fields":["field1","field2"]
    }
  }
}
```

下面检索的就是`name`,`tel`字段中能匹配到`wanghaonan`的数据

![image-20230920234947312](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920234947312.png)

#### terms 检索

> `terms`和`mysql`中的`in` 一样

```json
POST http://{{ServiceIP}}/index_name/_search
{
    "query": {
        "terms": {
            "field_name": [
                "content1",
                "content2"
            ]
        }
    }
}
```

![image-20230920234422076](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920234422076.png)

#### fuzzy模糊查询

> fuzzy和term一样，也不会将query进行分词，但是不同的是它在进行匹配时可以容忍你的词语拼写有错误，至于容忍度如何，是根据参数fuzziness决定的。fuzziness默认是2，也就是在默认情况下，fuzzy查询容忍你有两个字符及以下的拼写错误。即如果你要匹配的词语为test，但是你的query是text，那也可以匹配到.

```json
POST http://{{ServiceIP}}/user/_search
{
  "query": {
    "fuzzy": {
      "field_name":"content"
    }
  }
}
```



![image-20230920235723842](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920235723842.png)

#### range范围查询

> range查询时对于某一个数值字段的大小范围查询
>
> 1. gte：大于等于
> 2. gt：大于
> 3. lt：小于
> 4. lte：小于等于

```json
POST http://{{ServiceIP}}/index_name/_search
{
    "query": {
        "range": {
            "field_name": {
                "gte": 1,
                "lt": 5
            }
        }
    }
}
```

![image-20230921000200665](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230921000200665.png)

#### 高亮检索

```json
POST http://{{ServiceIP}}/{{Index_name}}/_search
{
    "query": {
        "match": {
            "query_field_name": "query content"
        }name
    },
    "_source": [
        "show_field_name"
    ],
    "highlight": {
        "fields": {
            "highlight_field_na'me": {}
        }
    }
}
```

![image-20230921155636970](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921155636970.png)

高亮字段可以是正则匹配,匹配上的字符便会加上高亮

![image-20230921161436450](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230921161436450.png)

#### 聚合查询

```json
POST http://{{ServiceIP}}/{{Index_name}}/_doc/_search
{
    "from": 0,
    "size": 0,
    "aggs": {
        "all_add": {// 聚合的名字
            "terms": {
                "field": "age"
            }
        }
    }
}
```

![image-20230921163631562](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230921163631562.png)

> 官网上还给了例子再局和内部还可以镜像其他的操作,聚合加强版操作
>
> ![image-20230921164016549](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230921164016549.png)

#### 使用id进行检索

> 这个方法很少用太笨了

```json
GET http://{{ServiceIP}}/index_name/_doc/id
```

![image-20230920230256165](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920230256165.png)

#### 分页检索

```bash
GET http://{{ServiceIP}}/index_name/_search
// 这里检索的意思是从下标0开始向后检索两个和mysql一样
{
    "query": {
        "match_all": {}
    },
    "from": 0, // 起始的下标位置 
    "size": 2 // 检索的大小
}
```

![image-20230920231017441](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920231017441.png)

#### 排序检索

```json
POST http://{{ServiceIP}}/index_name/_search
{
    "query": {
        "match_all": {}
    },
    "from":0, // 从第0个开始
    "size":3, // 显示3个
    "_source":["age"],// 只显示age字段
    "sort": { // 排序
        "field_name": { // 根据age字段排序
            "order": "asc" // 正序排序
        }
    }
}
```

![image-20230920231854081](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920231854081.png)

#### 多条件检索

> bool查询是上面查询的一个综合，它可以用多个查询去组合出一个大的查询语句

```json
GET http://{{ServiceIP}}/index_name/_search
{
    "query": {
        "bool": {
            "must": [
                {
                    "match": {
                        "name": "wanghaonan"
                    }
                },
                {
                    "match": {
                        "age": 18
                    }
                }
            ],
            "should": {
                "term":{ // 
                    "tel": "123456"
                }
            }
        }
    }
}
```

> term查询是比较常用的一种查询方式，它和match的唯一区别就是match需要对query进行分词，而term是不会进行分词的，它会直接拿query整体和原文进行匹配
>
> 
>
> 1. must：代表且的关系，也就是必须要满足该条件
> 2. should：代表或的关系，代表符合该条件就可以被查出来
> 3. must_not：代表非的关系，也就是要求不能是符合该条件的数据才能被查出来
>
> 
>
> 上面检索的意思是`name`和`age`分词检索必须匹配`wanghaonan`和`18`,或`tel`完全匹配`123456`的数据

![image-20230920233405916](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920233405916.png)

### 3. 文档修改

#### 根据id修改

> 整体替换 相同id的文档 如果有个字段为空 也会被赋值为空

```json
// POST  :http://{{ServiceIP}}/{{Index_name}}/_doc/user20230920
{
    "name":"wanghaonan  update",
    "address":"南京",
    "age":18,
    "email":"123456789@qq.com",
    "tel":"123456"
}
```

#### 修改指定字段

```json
// POST : http://{{ServiceIP}}/{{Index_name}}/_update/user20230920
{
    "doc": {
        "name": "wanghaonan  update",
        "address": "南京11"
    }
}
```

#### 使用Script 脚本批量修改

```json
// POST : http://{{ServiceIP}}/{{Index_name}}/_update_by_query
{
  "script": {
    "lang": "painless",
      // 修改文档中name属性的值为zhangsan的为 whnn
    "source": "if (ctx._source.name == 'zhangsan'){ctx._source.name='whnn'}"
  }
}
```



### 4. 分词器

```json
POST http://{{ServiceIP}}/_analyze
```

> 这里需要指定一个分词器，ES默认的分词器是standard，不过只支持英文分词，如果你用它来对中文进行分词的话会直接按字拆分，有一些中文分词器可以下载使用，像ik或者jieba之类的，这里便不去介绍如何安装了，感兴趣的可以查阅相关文章。

![image-20230920225056686](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920225056686.png)

# Spring Boot 整合 ES

## 前期准备

1. 依赖文件

```xml
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <nacos.context>2.1.0-RC</nacos.context>
        <!--         <swagger.ui>2.9.2</swagger.ui> -->
        <swagger.ui>3.0.0</swagger.ui>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.18.26</lombok.version>
        <mysql.version>5.1.47</mysql.version>
        <druid.version>1.1.16</druid.version>
        <druid.spring.boot.starter.version>1.1.10</druid.spring.boot.starter.version>
        <mapper.version>4.1.5</mapper.version>
        <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
        <mysql.connector.version>5.1.47</mysql.connector.version>
        <hutool.version>5.2.3</hutool.version>
        <mybatis.plus.boot.starter.version>3.2.0</mybatis.plus.boot.starter.version>
        <guava.version>23.0</guava.version>
        <canal.client.version>1.1.0</canal.client.version>
        <redission.version>3.19.1</redission.version>
        <elasticsearch.version>7.8.0</elasticsearch.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2-extension</artifactId>
            <version>2.0.12</version>
        </dependency>
<!--        <dependency>-->
<!--            <groupId>org.elasticsearch</groupId>-->
<!--            <artifactId>elasticsearch</artifactId>-->
<!--            <version>${elasticsearch.version}</version>-->
<!--        </dependency>-->
<!--        &lt;!&ndash; elasticsearch 的客户端 &ndash;&gt;-->
<!--        <dependency>-->
<!--            <groupId>org.elasticsearch.client</groupId>-->
<!--            <artifactId>elasticsearch-rest-high-level-client</artifactId>-->
<!--            <version>${elasticsearch.version}</version>-->
<!--        </dependency>-->
        <!--基于AMQP协议的消息中间件框架-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>

        <!-- SpringBoot通用依赖模块 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 通用基础配置 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>${junit.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <optional>true</optional>
        </dependency>

        <!-- swagger2 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>${swagger.ui}</version>
        </dependency>
        <!-- lombok   -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
        <!-- 通用基础配置junit/devtools/test/log4j/lombok/hutool -->
        <!-- hutool -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool.version}</version>
        </dependency>
        <dependency>
            <groupId>com.vaadin.external.google</groupId>
            <artifactId>android-json</artifactId>
            <version>0.0.20131108.vaadin1</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
```

2. 配置文件

```yml
server:
  port: 8080
spring:
  application:
    name: rabbit-mq-learn
  swagger2:
    enabled: true
  elasticsearch:
    rest:
      uris:  http://43.138.25.182:9200
      connection-timeout:  5s
```

3. PO

> 这里`@Document`注解设置`class`在 `ES` 中的索引和分片等信息
> ` @Field`设置字段的类型信息，索引状态等，用来创建映射使用的的信息

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "user", shards = 3, replicas = 1)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 使用@Id注解声明该字段为文档的唯一标识，使用@Field注解声明该字段为关键字字段，并指定字段不可分词
     */
    @Id
    @Field(type = FieldType.Keyword,fielddata = true)
    private String id;
    /**
     * 姓名可以进行分册查询
     */
    @Field(type = FieldType.Text)
    private String name;
    /**
     * 性别： 1，男 2，女
     */
    @Field(type = FieldType.Integer)
    private Integer age;
    /**
     * 住址可进行分词查询
     */
    @Field(type = FieldType.Text)
    private String address;
    /**
     * 电子邮箱地址不可分词
     */
    @Field(type = FieldType.Keyword)
    private String email;
    /**
     * 电话不可分词
     */
    @Field(type = FieldType.Keyword)
    private String tel;
    /**
     * 日期类型
     */
    @Field(type = FieldType.Date)
    private Date birthday;
}
```

4. DAO

> 这里我们就可以和使用`mybatis`一样去操作`es`了

```java
@Repository
public interface UserDao extends ElasticsearchRepository<User,String> {
}
```

## 索引操作

> 索引的创建，删除， 查询所有索引

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class ESIndexOpreationTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 判断是否存在索引 存在删除再次创建，不存在直接创建
     */
    @Test
    void indexOperation() {
        // 获取索引操作对象
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(User.class);
        // 判断索引是否存在
        boolean exists = indexOperations.exists();
        if (exists){
            System.out.println("exists:"+exists);
            //删除索引
            System.out.println("delete："+indexOperations.delete());
        }
        //创建索引
        System.out.println("create:"+indexOperations.create());
        Document mapping = indexOperations.createMapping();
        System.out.println(mapping);
    }

    /**
     * 获取所有 index
     * @throws IOException
     */

    @Test
    void getAllIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        // 设置匹配所有索引的模式（默认也是匹配所有，这里为了明确指出）
        request.indices("*");
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        String[] indices = getIndexResponse.getIndices();
        for (int i = 0; i < indices.length; i++) {
            System.out.println(indices[i]);
        }
    }
}
```

## 文档操作

### 文档添加

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 测试添加文档
     */
    @Test
    void addDocument() {
        String name = "whn";
        //String id = UUID.nameUUIDFromBytes(name.getBytes()).toString();
        String id = "whn123456";
        User user = User.builder()
                .id(id).address("南京鼓楼")
                .age(18).email("147258369@qq.com")
                .tel("1234567890")
                .birthday(new Date())
                .name(name).build();
        User save = userDao.save(user);
        System.out.println(save);
    }
}
```

### 批量添加文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void batchAddDocument() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String name = "whn" + i;
            String id = UUID.nameUUIDFromBytes(name.getBytes()).toString().replace("-", "");
            User user = User.builder()
                    .id(id).address("南京鼓楼")
                    .age(18).email("147258369@qq.com")
                    .tel("1234567890")
                    .birthday(new Date())
                    .name(name).build();
            list.add(user);
        }
        Iterable<User> users = userDao.saveAll(list);
        users.forEach(System.out::println);
    }
    
}
```

### 根据ID文档更新

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void updateDocument() {
        String name = "whn1";
        String id = "whn123456879";
        User user = User.builder()
                .id(id).address("徐州鼓楼")
                .age(188).email("147258369@qq.com")
                .tel("1234567890")
                .birthday(new Date())
                .name(name).build();
        User save = userDao.save(user);
        System.out.println(save);
    }
    
}
```

### 条件更新文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Test
    void updateQueryDocument() {
        String name = "whn";
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", name)).build();
        UpdateQuery updateQuery = UpdateQuery.builder(searchQuery).withScript("ctx._source.name = '13546'").withScriptName("123456").withScriptType(ScriptType.STORED).build();
        ByQueryResponse byQueryResponse = elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of(User.class.getDeclaredAnnotation(Document.class).indexName()));
        System.out.println(byQueryResponse.getTotal());
        AllSearchDocument();
    }


    
}
```

### 删除全部文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void DeleteAll() {
        userDao.deleteAll();
    }
    
}
```

### 根据id删除文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void DeleteDocumentById() {
        String id = "whn12345116";
        List<String> list = Arrays.asList(id);
        userDao.deleteAllById(list);
    }

    
}
```

### 条件删除文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void ConditionsDeleteDocument() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "whn"));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.lte(20);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder).withQuery(rangeQueryBuilder);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        ByQueryResponse delete = elasticsearchRestTemplate.delete(nativeSearchQuery, User.class);
        System.out.println(delete.getTotal());
    }

    
}
```

### 检索全部文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void AllSearchDocument() {
        Iterable<User> all = userDao.findAll();
        all.forEach(System.out::println);
    }

    
}
```

### 条件检索文档

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void advancedSearchDocument() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "whn"));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.lte(20);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder).withQuery(rangeQueryBuilder);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, User.class);
        searchHits.forEach(personSearchHit -> {
            System.out.println(personSearchHit.getContent());
        });
    }
    
}
```

### 高亮检索

```java
@SpringBootTest
@RunWith(SpringRunner.class)
class DocumentTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    void highLightSearchDocument() {
        //高亮字段设置
        HighlightBuilder.Field field = new HighlightBuilder
                //高连字段名
                .Field("name")
                //高亮标签设置
                .preTags("<span class=\"highlight\">")
                .postTags("</span>");
        // 匹配查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "whn"));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.lte(20);
        // 构建查询对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder).withQuery(rangeQueryBuilder).withHighlightFields(field);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        //检索
        SearchHits<User> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, User.class);
        List<User> userList = new ArrayList<>();
        // 遍历检索对象处理高亮内容
        searchHits.forEach(personSearchHit -> {
            User content = personSearchHit.getContent();
            // 处理高亮
            Map<String, List<String>> highlightFields = personSearchHit.getHighlightFields();
            for (Map.Entry<String, List<String>> stringHighlightFieldEntry : highlightFields.entrySet()) {
                String key = stringHighlightFieldEntry.getKey();
                if (StringUtils.equals(key, "name")) {
                    List<String> fragments = stringHighlightFieldEntry.getValue();
                    StringBuilder sb = new StringBuilder();
                    for (String fragment : fragments) {
                        sb.append(fragment);
                    }
                    content.setName(sb.toString());
                }
                if (StringUtils.equals(key, "email")) {
                    List<String> fragments = stringHighlightFieldEntry.getValue();
                    StringBuilder sb = new StringBuilder();
                    for (String fragment : fragments) {
                        sb.append(fragment);
                    }
                    content.setEmail(sb.toString());
                }
            }
            userList.add(content);
        });
        System.out.println(userList);
    }
    
}
```

## ES 使用sql进行查询

以下是一个使用 Elasticsearch SQL 查询的简单示例，假设我们有一个名为 `employee` 的索引，其中包含如下结构的数据：

```json
{
  "id": "1",
  "first_name": "John",
  "last_name": "Doe",
  "age": 30,
  "department": "Sales",
  "salary": 50000
}
```

现在，我们可以使用 SQL 查询语句来检索和分析这些数据：

```sql
-- 基本查询
SELECT first_name, last_name, age FROM employee WHERE department = 'Sales';
-- 排序查询
SELECT * FROM employee ORDER BY salary DESC LIMIT 10;
-- 聚合查询
SELECT department, AVG(salary) as avg_salary FROM employee GROUP BY department;
```

要实际执行这些查询，你可以通过 Elasticsearch 的 REST API 或者使用支持 JDBC 的工具（如 SQL 客户端、BI 工具等）连接到 Elasticsearch。以下是如何通过 REST API 执行查询的一个示例：

```bash
curl -X POST -H 'Content-Type: application/json' -d '
{
  "query": "SELECT first_name, last_name, age FROM employee WHERE department = 'Sales'"
}' 'http://localhost:9200/_sql'
```

请注意，上述示例中的 URL http://localhost:9200 应替换为你实际的 Elasticsearch 服务器地址。响应将是一个 JSON 对象，包含查询结果。
以上就是使用 Elasticsearch SQL 进行查询的简单示例。根据你的具体需求，可以编写更复杂的 SQL 查询来利用 Elasticsearch 的强大搜索和分析能力。

```java
    public static void translateConditions(String sql, RestHighLevelClient client) throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        map.put("query", sql);
        Request request = new Request("POST", "/_sql");
        Gson gson = new Gson();
        String jsonEntity = gson.toJson(map);
        LOGGER.info("jsonEntity：" + jsonEntity);
        request.setJsonEntity(gson.toJson(map));
        Response response = client.getLowLevelClient().performRequest(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("组合查询sql解析异常！");
        }
      //  ...response 响应结果处理
    }
```

## SQL 转 DSL

```java
    public static QueryBuilder translateConditions(String sql, RestHighLevelClient client) throws IOException {
        Map<String, Object> map = new HashMap<>(2);
        map.put("query", sql);
        Request request = new Request("POST", "/_sql/translate");
        Gson gson = new Gson();
        String jsonEntity = gson.toJson(map);
        LOGGER.info("jsonEntity：" + jsonEntity);
        request.setJsonEntity(gson.toJson(map));
        Response response = client.getLowLevelClient().performRequest(request);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("组合查询sql解析异常！");
        }
        JsonObject jsonObject = gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
        String queryJson = jsonObject.get("query").toString();
        LOGGER.info("translated DSL is：" + queryJson);
        return QueryBuilders.wrapperQuery(queryJson);
    }
```

