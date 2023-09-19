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

### elasticSearch安装

#### 本地运行

[ Elastic 官网](https://www.elastic.co/cn/downloads/enterprise-search)根据需求自己下载



#### docker安装部署

```bash
docker run -d \
  --restart=always \
  --name es \
  --network es-net \
  -p 9200:9200 \
  -p 9300:9300 \
  --privileged \
  -v /usr/local/es/log:/usr/share/elasticsearch/logs \
  -v /usr/local/es/data:/usr/share/elasticsearch/data \
  -v /usr/local/es/plugins:/usr/share/elasticsearch/plugins \
  -v /usr/local/es/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
  -e "discovery.type=single-node" \
  -e ES_JAVA_OPTS="-Xms512m -Xms512m" \
elasticsearch:last
```



#### docker集群部署

[docker-compose部署](https://blog.csdn.net/iampatrick_star/article/details/127263346)

ElasticSearch

ElasticSearch

ElasticSearch