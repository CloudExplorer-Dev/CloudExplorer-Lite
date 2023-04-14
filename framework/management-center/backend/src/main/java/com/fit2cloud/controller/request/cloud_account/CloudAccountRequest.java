package com.fit2cloud.controller.request.cloud_account;

import com.fit2cloud.request.pub.PageOrderRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:10 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountRequest extends PageOrderRequest {
    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    private String name;

    @ApiModelProperty(value = "供应商", notes = "供应商")
    @Size(min = 1, message = "{i18n.cloud_account_platform_size}")
    private List<String> platform;

    @ApiModelProperty(value = "同步状态", notes = "同步状态")
    @Size(min = 1, message = "{i18n.cloud_account.status.size}")
    private List<String> status;

    @ApiModelProperty(value = "云账号状态", notes = "云账号状态")
    @Size(min = 1, message = "{i18n.cloud_account.state.size}")
    private List<Boolean> state;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "修改时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;
}
