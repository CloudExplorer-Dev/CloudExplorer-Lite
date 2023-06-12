package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.request.pub.PageOrderRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:10 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountRequest extends PageOrderRequest {
    @Schema(title = "云账号名称", description = "云账号名称")
    private String name;

    @Schema(title = "供应商", description = "供应商")
    @Size(min = 1, message = "{i18n.cloud_account_platform_size}")
    private List<String> platform;

    @Schema(title = "同步状态", description = "同步状态")
    @Size(min = 1, message = "{i18n.cloud_account.status.size}")
    private List<String> status;

    @Schema(title = "云账号状态", description = "云账号状态")
    @Size(min = 1, message = "{i18n.cloud_account.state.size}")
    private List<Boolean> state;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "编辑时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;
}
