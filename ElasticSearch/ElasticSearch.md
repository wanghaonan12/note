# ElasticSearch笔记

## 开篇引导

[Elasticsearch](https://blog.csdn.net/u011863024/article/details/115721328)

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

## ElasticSearch入门

### 1. elasticSearch安装

#### docker安装部署

> 一直在尝试添加数据卷,但是每次只要添加就会报错,这里就为了能够继续下去没有添加,会在后面补充

##### 普通安装

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

#####  添加容器数据卷

```bash
 docker run -d \
     -p 9201:9200 \
     -p 9301:9300 \
     --name es02 \
     --network es_network \
     -v /home/es/plugins:/usr/share/elasticsearch/plugins \
     -v /home/es/data:/usr/share/elasticsearch/data \
     -v /home/es/log:/usr/share/elasticsearch/logs \
     -v /home/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
     -e "discovery.type=single-node" \
     -e ES_JAVA_OPTS="-Xms100m -Xmx200m" \
     -e TAKE_FILE_OWNERSHIP=true \
 elasticsearch:7.17.5
```

![image-20230921102536190](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921102536190.png)

**验证**

![image-20230921102612716](https://raw.githubusercontent.com/wanghaonan12/picgo/main/img/image-20230921102612716.png)

##### 添加分词器

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

#### docker集群部署

[docker-compose部署](https://blog.csdn.net/iampatrick_star/article/details/127263346)

### 2. RestFul风格

RESTful风格是一种设计和构建网络应用程序的软件架构风格。它基于Representational State Transfer（资源表现层状态转化）的概念，通过使用HTTP协议中的几个关键操作（GET、POST、PUT、DELETE等）来对资源进行管理和操作。
在RESTful风格中，资源被视为应用程序中的一部分，每个资源都可以通过唯一的URL进行访问。资源的状态通过HTTP动词和数据表达，并使用HTTP状态码进行响应。
以下是RESTful风格的一些关键特点：

1. 资源定位：每个资源都有一个唯一的URL作为其标识。通过URL可以访问和操纵该资源。
2. 无状态性：服务器不保存客户端的状态信息，每个请求都是独立的。客户端的每个请求都必须包含足够的信息以使服务器能够理解。
3. 统一接口：RESTful风格使用统一的接口来操纵资源。这包括使用HTTP动词来表示操作（GET用于获取资源，POST用于创建资源，PUT用于更新资源，DELETE用于删除资源）和使用HTTP状态码来表示操作的结果。
4. 可缓存性：RESTful架构使用HTTP协议的缓存机制，提高性能和可伸缩性。
5. 完全分布式：RESTful架构支持分布式计算环境，因为资源可以在不同的系统之间传递和访问。
   总之，RESTful风格是一种基于HTTP协议的简洁、可扩展和可维护的软件架构风格，适用于构建各种类型的网络应用程序。它提供了一种简单的方式来设计和实现可靠的和可伸缩的分布式系统。

### 3. 倒排索引

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

## 4. 使用http进行增删改查操作

### 1. 创建索引

> PUT请求是幂等的，即对同一个文档多次执行相同的PUT请求会产生相同的结果。这意味着多次执行PUT请求不会导致文档的重复或多次更新。而POST请求不是幂等的，多次执行相同的POST请求会创建多个相同的文档。

```
PUT http://{{ServiceIP}}/index_name
```

![image-20230920220835051](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920220835051.png)

### 2. 检索创建的索引

```
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

```
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

```
DELETE http://{{ServiceIP}}/index_name
```

![image-20230920221707813](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920221707813.png)

### 5. 文档创建

#### 添加随机id文档

```
POST http://{{ServiceIP}}/index_name/_doc
```

> 在下面的id中可以看出是随机id

![image-20230920222940800](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920222940800.png)

#### 指定id添加文档

```
POST http://{{ServiceIP}}/index_name/_doc/id
```

> 在下下面就能看出是指定好的id数据,方便用于管理

![image-20230920224134825](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920224134825.png)

### 6. 文档查询

#### match检索

> `match`会将你搜索的`query`进行分词的查询方法和`mysql`中的模糊查询差不多

```
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

```
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

```
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

```
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

```
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

```
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

#### 使用id进行检索

> 这个方法很少用太笨了

```
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

```
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

```
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

### 7. 文档修改

### 8. 分词器

```
POST http://{{ServiceIP}}/_analyze
```

> 这里需要指定一个分词器，ES默认的分词器是standard，不过只支持英文分词，如果你用它来对中文进行分词的话会直接按字拆分，有一些中文分词器可以下载使用，像ik或者jieba之类的，这里便不去介绍如何安装了，感兴趣的可以查阅相关文章。

![image-20230920225056686](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20230920225056686.png)

ElasticSearch

ElasticSearch

ElasticSearch