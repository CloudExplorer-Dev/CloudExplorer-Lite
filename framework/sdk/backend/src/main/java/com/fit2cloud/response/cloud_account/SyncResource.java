package com.fit2cloud.response.cloud_account;

import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/9/30  2:17 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class SyncResource {
    /**
     * 模块
     */
    private String module;
    /**
     * 定时任务名称
     */
    private String jobName;
    /**
     * 资源类型描述
     */
    private String resourceDesc;
}
