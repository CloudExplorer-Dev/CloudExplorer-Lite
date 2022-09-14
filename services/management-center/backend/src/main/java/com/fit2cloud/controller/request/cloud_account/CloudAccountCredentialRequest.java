package com.fit2cloud.controller.request.cloud_account;

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
import java.util.Arrays;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  9:25 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class CloudAccountCredentialRequest {
    @ApiModelProperty(value = "凭证信息", notes = "凭证信息")
    @JsonDeserialize(using = CloudAccountCredentialRequest.CredentialDeserializer.class)
    @NotNull(message = "云账号凭证信息不能为空")
    private Credential credential;

    @ApiModelProperty(value = "云平台", notes = "云平台")
    @NotNull(message = "云平台不能为空")
    private String platform;

    public static class CredentialDeserializer extends JsonDeserializer<Credential> {
        @Override
        public Credential deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            CloudAccountCredentialRequest cloudAccountRequest = (CloudAccountCredentialRequest) ctxt.getParser().getParsingContext().getParent().getCurrentValue();
            if (!Arrays.stream(PlatformConstants.values()).anyMatch(platform -> platform.name().equals(cloudAccountRequest.getPlatform()))) {
                throw new Fit2cloudException(1, "不支持的云平台");
            }
            PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccountRequest.getPlatform());
            return p.getCodec().readValue(p, platformConstants.getCredentialClass());
        }
    }
}
