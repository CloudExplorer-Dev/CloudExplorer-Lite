package com.fit2cloud.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @Author:张少虎
 * @Date: 2022/9/28  9:00 AM
 * @Version 1.0
 * @注释:
 */
@Document(indexName = "ce-file-system-logs")
@Data
public class SystemLog {

    @Id
    private String id;
    /**
     * 详细信息
     */
    @Field(type = FieldType.Auto)
    private String msg;

    /**
     * 行数
     */
    @Field(type = FieldType.Auto)
    private String lineNumber;

    /**
     * 触发类
     */
    @Field(type = FieldType.Auto)
    private String javaClass;
    /**
     * 模块
     */
    @Field(type = FieldType.Auto)
    private String module;
    /**
     * 触发方法名
     */
    @Field(type = FieldType.Auto)
    private String methodName;
    /**
     * pid
     */
    @Field(type = FieldType.Auto)
    private String pid;
    /**
     * 等级
     */
    @Field(type = FieldType.Auto)
    private String level;
    /**
     * 日志全文
     */
    @Field(type = FieldType.Auto)
    private String message;
    /**
     * 日志类型
     */
    @Field(type = FieldType.Auto)
    private String type;
    /**
     * 线程名
     */
    @Field(type = FieldType.Auto)
    private String threadName;
    /**
     * 日志转化成功multiline
     */
    @Field(type = FieldType.Auto)
    private String tags;
    /**
     * 文件目录
     */
    @Field(type = FieldType.Auto)
    private String path;
    /**
     * 创建时间
     */
    @Field(type = FieldType.Auto)
    private String createTime;
}
