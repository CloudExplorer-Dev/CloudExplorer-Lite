package com.fit2cloud.controller.response.cloud_account;

import com.fit2cloud.common.form.vo.Form;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  10:53 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class PlatformResponse {
    @ApiModelProperty(value = "云平台名称label", notes = "云平台名称label")
    private String label;
    @ApiModelProperty(value = "云平台对应的code", notes = "云平台对应的code")
    private String field;
    @ApiModelProperty(value = "凭证表单", notes = "凭证表单")
    private List<? extends Form> credentialFrom;
}
