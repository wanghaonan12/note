package org.whn.service.Impl;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: WangHn
 * @Date: 2024/4/7 15:42
 * @Description: 测试实现类
 */
@Service
public class TestServiceImpl {
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public String testSearch() throws IOException {
        SearchResponse search = restHighLevelClient.search(new SearchRequest("user"),RequestOptions.DEFAULT);
        //System.out.println(search);
        System.out.println(search.getAggregations());
        System.out.println(search.getClusters());
        search.getHits().forEach(hit->{
            System.out.println(hit.getSourceAsMap());
            //System.out.println(hit);
        });
        System.out.println(search.getProfileResults());
        System.out.println(search.getTook());
        System.out.println(search.getSuggest());
        return "testSearch";
    }
}
