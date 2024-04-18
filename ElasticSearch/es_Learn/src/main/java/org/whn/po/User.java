package org.whn.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: WangHn
 * @Date: 2024/4/7 09:55
 * @Description: es 测试实体类
 */
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