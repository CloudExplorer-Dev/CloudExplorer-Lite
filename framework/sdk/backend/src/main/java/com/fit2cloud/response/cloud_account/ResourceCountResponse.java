package com.fit2cloud.response.cloud_account;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "资源图标")
    private String icon;

    @Schema(title = "资源显示名称")
    private String name;

    @Schema(title = "资源计数")
    private long count;

    @Schema(title = "单位")
    private String unit;
}
