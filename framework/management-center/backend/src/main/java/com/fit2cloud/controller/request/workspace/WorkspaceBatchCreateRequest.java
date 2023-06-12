package com.fit2cloud.controller.request.workspace;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/6 10:51
 **/
@Data
public class WorkspaceBatchCreateRequest {
    @Schema(title = "所属组织id")
    private String organizationId;

    @Size(min = 1, message = "{i18n.workspace.is.not.empty}")
    @Schema(title = "工作空间数据")
    private List<WorkspaceBatchCreateRequest.WorkspaceDetails> workspaceDetails;

    @Data
    public static class WorkspaceDetails {
        @Schema(title = "工作空间id", description = "工作空间id")
        private String id;
        @Schema(title = "工作空间名称", description = "工作空间名称")
        private String name;
        @Schema(title = "所属组织id")
        private String organizationId;
        @Schema(title = "工作空间描述", description = "工作空间描述")
        private String description;
    }

}
