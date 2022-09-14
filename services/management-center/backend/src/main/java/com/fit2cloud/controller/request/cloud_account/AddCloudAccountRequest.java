package com.fit2cloud.controller.request.cloud_account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  2:14 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class AddCloudAccountRequest extends CloudAccountCredentialRequest {

    @ApiModelProperty(value = "云账号名称", notes = "云账号名称")
    @NotNull(message = "云账号名称不能为null")
    private String name;


}
