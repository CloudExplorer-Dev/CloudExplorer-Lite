package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudDatastore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jianneng
 * @date 2022/12/11 18:55
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisDatastoreDTO extends VmCloudDatastore {

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "工作空间名称")
    private String workspaceName;

    @Schema(title = "云账号名称")
    private String accountName;

    @Schema(title = "企业项目")
    private String cloudProjectName;

    @Schema(title = "云平台")
    private String platform;

    @Schema(title = "已分配")
    private String allocated;

    @Schema(title = "使用率")
    private String useRate;

    @Schema(title = "剩余率")
    private String freeRate;
}
