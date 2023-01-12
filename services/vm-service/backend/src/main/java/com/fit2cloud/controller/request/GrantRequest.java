package com.fit2cloud.controller.request;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/29 12:56
 */
@Data
public class GrantRequest {
    private Boolean grant;
    /**
     * 要授权的资源 ID 集合，云主机 ID 或云磁盘 ID 集合
     */
    private String[] ids;
    /**
     * 组织 ID 或工作空间 ID
     */
    private String sourceId;
}
