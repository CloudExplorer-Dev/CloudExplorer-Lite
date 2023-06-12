package com.fit2cloud.dto.optimization;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.base.entity.VmCloudServer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class VmCloudServerDTO extends VmCloudServer {

    @Serial
    private static final long serialVersionUID = 2780061897725836746L;

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "工作空间名称")
    private String workspaceName;

    @Schema(title = "组织ID")
    private String organizationId;

    @Schema(title = "工作空间ID")
    private String workspaceId;

    @Schema(title = "云账号名称")
    private String accountName;

    @Schema(title = "企业项目")
    private String cloudProjectName;

    @Schema(title = "云平台")
    private String platform;

    @Schema(title = "申请用户名")
    private String applyUserName;

    @Schema(title = "删除时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

    @Schema(title = "CPU使用率")
    private MonitoringDataValueDTO cpuMonitoringValue;

    @Schema(title = "内存使用率")
    private MonitoringDataValueDTO memoryMonitoringValue;

    @Schema(title = "优化建议")
    private String content;
}
