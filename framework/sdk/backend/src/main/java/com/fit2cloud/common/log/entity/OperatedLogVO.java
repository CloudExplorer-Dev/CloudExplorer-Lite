package com.fit2cloud.common.log.entity;

import com.fit2cloud.common.log.annotation.OperatedLogFieldConver;
import com.fit2cloud.common.log.conver.impl.CloudResourceConvert;
import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志
 *
 * @author jianneng
 * @date 2022/9/15 13:43
 **/
@Getter
@Setter
public class OperatedLogVO {


    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 模块
     */
    private String module;

    /**
     * 操作
     */
    private String operated;

    /**
     * 操作名称
     */
    private String operatedName;

    /**
     * 资源ID+@+资源类型
     * id+"@"+type
     */
    @OperatedLogFieldConver(conver = CloudResourceConvert.class)
    private String resourceId;

    private String resourceName;

    /**
     * 资源类型
     */
    private String resourceType;

    @OperatedLogFieldConver(conver = CloudResourceConvert.class)
    private String joinResourceId;

    /**
     * 关联资源ID
     */
    private String joinResourceName;

    /**
     * 操作人
     */
    private String user;

    /**
     * 操作人ID
     */
    private String userId;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 请求时间
     */
    private Long requestTime;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求状态：1 成功 0 失败
     */
    private Integer status;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 来源IP
     */
    private String sourceIp;

    /**
     * 耗时
     */
    private Long time;

    /**
     * 返回内容
     */
    private String response;


    private String date;

    private String msg;

    private String level;

    private String type;

    private String path;

    private String host;
}
