package com.fit2cloud.response.cloud_account;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: LiuDi
 * Date: 2022/9/29 1:43 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCountResponse {
    @ApiModelProperty(value = "资源图标")
    private String icon;
    @ApiModelProperty(value = "资源显示名称")
    private String name;
    @ApiModelProperty(value = "资源计数")
    private long count;
}
