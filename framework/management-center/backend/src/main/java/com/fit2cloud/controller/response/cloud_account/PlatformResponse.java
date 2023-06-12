package com.fit2cloud.controller.response.cloud_account;

import com.fit2cloud.common.form.vo.Form;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "云平台名称label", description = "云平台名称label")
    private String label;

    @Schema(title = "云平台对应的code", description = "云平台对应的code")
    private String field;

    @Schema(title = "是否为公有云", description = "是否为公有云")
    private boolean publicCloud;

    @Schema(title = "凭证表单", description = "凭证表单")
    private List<? extends Form> credentialForm;
}
