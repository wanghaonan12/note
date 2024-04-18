package org.whn.dao;

import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;
import org.whn.po.User;

import java.io.IOException;

/**
 * @Author: WangHn
 * @Date: 2024/4/11 15:58
 * @Description:
 */
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