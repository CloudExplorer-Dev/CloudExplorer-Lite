package com.fit2cloud.controller.response.cloud_account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/29 1:43 PM
 */
@Data
public class ResourceCountResponse {
    @ApiModelProperty(value = "资源图标")
    private String icon;
    @ApiModelProperty(value = "资源显示名称")
    private String name;
    @ApiModelProperty(value = "资源计数")
    private long count;

    public  ResourceCountResponse(String icon, String name, long count) {
        this.icon = icon;
        this.name = name;
        this.count = count;
    }
}
