package org.whn.dao;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.ScriptedMetric;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author whn
 * @time 2024/12/20
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class search {

    @Autowired
    RestHighLevelClient client;
    @Test
    public void testSearch() throws IOException {
        // 构建搜索请求
        SearchRequest searchRequest = new SearchRequest("pdes_archive_use");

        // 构建查询
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("originals"))
                .mustNot(QueryBuilders.matchQuery("originals", ""))
                .mustNot(QueryBuilders.matchQuery("originals", "null")));

        searchSourceBuilder.size(0);

        // 初始化脚本
        Script initScript = new Script("state.totalViewCount = 0L; state.totalDownCount = 0L; state.total_number_of_pages = 0L;");
        // 映射脚本
        String mapScript = "for (item in params['_source']['originals']) {" +
                "    state.totalViewCount += item['viewCount'].longValue(); " +
                "    state.totalDownCount += item['downCount'].longValue(); " +
                "    state.total_number_of_pages += (item['total_number_of_pages'] != null && item['total_number_of_pages'] != '') ? item['total_number_of_pages'].longValue() : 0;" +
                "}";

        // 合并脚本
        Script combineScript = new Script("return state;");
        // 归约脚本
        Script reduceScript = new Script("def result = [:];\n" +
                "        result.totalViewCount = 0L;\n" +
                "        result.totalDownCount = 0L;\n" +
                "        result.total_number_of_pages = 0L;\n" +
                "        for (state in states) {\n" +
                "            result.totalViewCount += state.totalViewCount;\n" +
                "            result.totalDownCount += state.totalDownCount;\n" +
                "            result.total_number_of_pages += state.total_number_of_pages;\n" +
                "        }\n" +
                "        return result;");

        searchSourceBuilder.aggregation(
                AggregationBuilders.scriptedMetric("originals_stats")
                        .initScript(initScript)
                        .mapScript(new Script(mapScript))
                        .combineScript(combineScript)
                        .reduceScript(reduceScript)
        );

        // 执行搜索请求
        searchRequest.source(searchSourceBuilder);
        client.search(searchRequest, RequestOptions.DEFAULT);

        // 执行搜索请求
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        ScriptedMetric scriptedMetric = aggregations.get("originals_stats");
        Map<String, Object> resultMap = (Map<String, Object>) scriptedMetric.aggregation();
        System.out.println(resultMap);
    }
}
