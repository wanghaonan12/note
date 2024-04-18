package org.whn.dao;

import org.apache.commons.codec.binary.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ScriptType;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.whn.po.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: WangHn
 * @Date: 2024/4/11 15:58
 * @Description:
 */
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

    /**
     * 测试批量添加文档
     */
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

    /**
     * 测试更新文档
     */
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

    /**
     * 条件更新
     */
    @Test
    void updateQueryDocument() {
        String name = "whn";
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("name", name)).build();
        UpdateQuery updateQuery = UpdateQuery.builder(searchQuery).withScript("ctx._source.name = '13546'").withScriptName("123456").withScriptType(ScriptType.STORED).build();
        ByQueryResponse byQueryResponse = elasticsearchRestTemplate.updateByQuery(updateQuery, IndexCoordinates.of(User.class.getDeclaredAnnotation(Document.class).indexName()));
        System.out.println(byQueryResponse.getTotal());
        AllSearchDocument();
    }




    /**
     * 删除全部
     */
    @Test
    void DeleteAll() {
        userDao.deleteAll();
    }

    /**
     * 根据Id删除
     */
    @Test
    void DeleteDocumentById() {
        String id = "whn12345116";
        List<String> list = Arrays.asList(id);
        userDao.deleteAllById(list);
    }

    /**
     * 条件删除
     */
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

    /**
     * 检索全部文档
     */
    @Test
    void AllSearchDocument() {
        Iterable<User> all = userDao.findAll();
        all.forEach(System.out::println);
    }

    /**
     * 简单文档检索
     */
    @Test
    void simpleSearchDocument() {
    }

    /**
     * 高级文档检索
     */
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

    /**
     * 高亮查询
     */
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