package com.fit2cloud.controller.request.cloud_account;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.common.validator.handler.ExistQueryWrapperValidatedHandler;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * { @Author:张少虎}
 * { @Date: 2022/9/13  5:06 PM}
 * { @Version 1.0}
 * { @注释:}
 */
@Data
@CustomQueryWrapperValidated(groups = ValidationGroup.UPDATE.class,
        handler = ExistQueryWrapperValidatedHandler.class,
        el = "#getQueryWrapper().ne(\"id\",#this.id).eq(\"name\",#this.name)",
        message = "云账号名称不能重复", exist = true,
        mapper = CloudAccountMapper.class)
@JsonDeserialize(using = UpdateCloudAccountRequest.Deserializer.class)
public class UpdateCloudAccountRequest {
    @Schema(title = "云账号id", description = "云账号id")
    @NotNull(message = "云账号id不能为空")
    @CustomValidated(groups = ValidationGroup.UPDATE.class, mapper = CloudAccountMapper.class, field = "id", handler = ExistHandler.class, message = "{i18n.cloud_account.id.is.not.existent}", exist = false)
    private String id;

    @Schema(title = "云账号名称", description = "云账号名称")
    @NotNull(message = "云账号名称不能为null")
    @CustomValidated(groups = ValidationGroup.SAVE.class, mapper = CloudAccountMapper.class, field = "name", handler = ExistHandler.class, message = "{i18n.cloud_account.name.not.repeat}", exist = true)
    private String name;

    @Schema(title = "凭证信息", description = "凭证信息")
    @NotNull(message = "{i18n.cloud_account.credential.is.not.empty}")
    private Credential credential;

    @Schema(title = "云平台", description = "云平台")
    @NotNull(message = "{i18n.cloud_account.platform,is.not.empty}")
    private String platform;

    public static class Deserializer extends JsonDeserializer<UpdateCloudAccountRequest> {
        @Override
        public UpdateCloudAccountRequest deserialize(JsonParser p, DeserializationContext c) throws IOException {
            JsonNode jsonNode = p.getCodec().readValue(p, JsonNode.class);
            if (!jsonNode.has("platform")) {
                throw new Fit2cloudException(ErrorCodeConstants.CLOUD_ACCOUNT_PLATFORM_IS_NOT_NULL.getCode(), ErrorCodeConstants.CLOUD_ACCOUNT_PLATFORM_IS_NOT_NULL.getMessage());
            }
            if (!jsonNode.has("credential")) {
                throw new Fit2cloudException(ErrorCodeConstants.CLOUD_ACCOUNT_CREDENTIAL_IS_NOT_EMPTY.getCode(), ErrorCodeConstants.CLOUD_ACCOUNT_CREDENTIAL_IS_NOT_EMPTY.getMessage());
            }
            String platform = jsonNode.get("platform").textValue();
            Optional<IBaseCloudProvider> first = PluginsContextHolder.getExtensions(IBaseCloudProvider.class).stream()
                    .filter(platformConstants -> StringUtils.equals(platform, platformConstants.getCloudAccountMeta().platform))
                    .findFirst();

            if (first.isEmpty()) {
                throw new Fit2cloudException(ErrorCodeConstants.CLOUD_ACCOUNT_NOT_SUPPORT_PLATFORM.getCode(), ErrorCodeConstants.CLOUD_ACCOUNT_NOT_SUPPORT_PLATFORM.getMessage());
            }
            UpdateCloudAccountRequest updateCloudAccountRequest = new UpdateCloudAccountRequest();
            try {
                updateCloudAccountRequest.setCredential(JsonUtil.mapper.readValue(jsonNode.get("credential").traverse(), first.get().getCloudAccountMeta().credential));
            } catch (Exception e) {
                throw new Fit2cloudException(ErrorCodeConstants.CLOUD_ACCOUNT_CREDENTIAL_FORM_ERROR.getCode(), ErrorCodeConstants.CLOUD_ACCOUNT_CREDENTIAL_FORM_ERROR.getMessage());
            }
            updateCloudAccountRequest.setId(jsonNode.has("id") ? jsonNode.get("id").asText(null) : null);
            updateCloudAccountRequest.setName(jsonNode.has("name") ? jsonNode.get("name").asText(null) : null);
            updateCloudAccountRequest.setPlatform(platform);
            return updateCloudAccountRequest;
        }
    }
}
