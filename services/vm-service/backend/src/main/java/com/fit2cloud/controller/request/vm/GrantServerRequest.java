package com.fit2cloud.controller.request.vm;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/29 12:56
 */
@Data
public class GrantServerRequest {
    private Boolean grant;
    private String[] cloudServerIds;
    /**
     * 组织 ID 或工作空间 ID
     */
    private String sourceId;
}
