package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  9:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@Document(indexName = "ce-resource-instance")
public class ResourceInstance {
    @Id
    private String id;
    /**
     * 供应商
     */
    @Field(type = FieldType.Text)
    private String platform;
    /**
     * 云账户id
     */
    @Field(type = FieldType.Keyword)
    private String cloudAccountId;
    /**
     * 实例类型
     */
    @Field(type = FieldType.Keyword)
    private String instanceType;
    /**
     * 实例对象
     */
    @Field(type = FieldType.Object)
    private Map<String, Object> instance;
}
