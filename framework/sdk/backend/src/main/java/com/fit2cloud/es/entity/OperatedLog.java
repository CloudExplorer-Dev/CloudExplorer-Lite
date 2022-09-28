package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author:张少虎
 * @Date: 2022/9/28  8:58 AM
 * @Version 1.0
 * @注释:
 */
@Document(indexName = "ce-file-api-logs")
@Data
public class OperatedLog {
    @Id
    private String id;
    /**
     * 请求ID
     */
    @Field(type = FieldType.Auto)
    private String requestId;

    /**
     * 模块
     */
    @Field(type = FieldType.Auto)
    private String module;

    /**
     * 操作
     */
    @Field(type = FieldType.Auto)
    private String operated;

    /**
     * 操作名称
     */
    @Field(type = FieldType.Auto)
    private String operatedName;

    /**
     * 资源ID
     */
    @Field(type = FieldType.Auto)
    private String resourceId;

    /**
     * 资源类型
     */
    @Field(type = FieldType.Auto)
    private String resourceType;

    /**
     * 关联资源ID
     */
    @Field(type = FieldType.Auto)
    private String joinResourceId;

    /**
     * 操作人
     */
    @Field(type = FieldType.Auto)
    private String user;

    /**
     * 操作人ID
     */
    @Field(type = FieldType.Auto)
    private String userId;

    /**
     * 请求地址
     */
    @Field(type = FieldType.Auto)
    private String url;

    /**
     * 操作内容
     */
    @Field(type = FieldType.Auto)
    private String content;

    /**
     * 请求时间
     */
    @Field(type = FieldType.Auto)
    private Long requestTime;

    /**
     * 请求方式
     */
    @Field(type = FieldType.Auto)
    private String method;

    /**
     * 请求参数
     */
    @Field(type = FieldType.Auto)
    private String params;

    /**
     * 请求状态：1 成功 0 失败
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 状态码
     */
    @Field(type = FieldType.Integer)
    private Integer code;

    /**
     * 来源IP
     */
    @Field(type = FieldType.Auto)
    private String sourceIp;

    /**
     * 耗时
     */
    @Field(type = FieldType.Long)
    private Long time;

    /**
     * 返回内容
     */
    @Field(type = FieldType.Auto)
    private String response;

    @Field(type = FieldType.Auto)
    private String date;

    @Field(type = FieldType.Auto)
    private String msg;
    @Field(type = FieldType.Auto)
    private String level;
    @Field(type = FieldType.Auto)
    private String type;
    @Field(type = FieldType.Auto)
    private String path;
    @Field(type = FieldType.Auto)
    private String host;
}
