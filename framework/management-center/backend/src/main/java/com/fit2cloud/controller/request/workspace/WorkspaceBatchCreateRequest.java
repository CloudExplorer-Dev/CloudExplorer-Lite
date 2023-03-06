package com.fit2cloud.controller.request.workspace;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/6 10:51
 **/
@Data
public class WorkspaceBatchCreateRequest {
    @ApiModelProperty("所属组织id")
    private String organizationId;

    @Size(min = 1,message = "{i18n.workspace.is.not.empty}")
    @ApiModelProperty("工作空间数据")
    private List<WorkspaceBatchCreateRequest.WorkspaceDetails> workspaceDetails;
    @Data
    public static class WorkspaceDetails{
        @ApiModelProperty(value = "工作空间id",notes = "工作空间id")
        private String id;
        @ApiModelProperty(value = "工作空间名称",notes = "工作空间名称")
        private String name;
        @ApiModelProperty("所属组织id")
        private String organizationId;
        @ApiModelProperty(value = "工作空间描述",notes = "工作空间描述")
        private String description;
    }

}
