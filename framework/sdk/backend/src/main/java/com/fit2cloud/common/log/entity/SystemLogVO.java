package com.fit2cloud.common.log.entity;

import lombok.Data;

/**
 * 系统日志
 * @author jianneng
 * @date 2022/9/22 13:33
 **/
@Data
public class SystemLogVO {

    /**
     * 详细信息
     */
    private String msg;

    /**
     * 行数
     */
    private String lineNumber;

    /**
     * 触发类
     */
    private String javaClass;
    /**
     * 模块
     */
    private String module;
    /**
     * 触发方法名
     */
    private String methodName;
    /**
     * pid
     */
    private String pid;
    /**
     * 等级
     */
    private String level;
    /**
     * 日志全文
     */
    private String message;
    /**
     * 日志类型
     */
    private String type;
    /**
     * 线程名
     */
    private String threadName;
    /**
     * 日志转化成功multiline
     */
    private String tags;
    /**
     * 文件目录
     */
    private String path;
    /**
     * 创建时间
     */
    private String createTime;
}
