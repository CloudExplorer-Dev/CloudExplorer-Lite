package com.fit2cloud.controller.request.optimize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jianneng
 * @date 2023/1/16 11:20
 **/
@Data
public class OptimizeBaseRequest {

    /**
     * 公共
     */
    @Schema(title = "云账号ID")
    private List<String> accountIds;

    @Schema(title = "云主机名称")
    private String instanceName;

    /**
     * 目前支持4中优化建议
     * 建议降配
     * 建议升配
     * 建议变更付费方式
     * 建议回收资源
     */
    @Schema(title = "优化建议")
    @NotNull(message = "优化建议策略code不能为空")
    private String optimizeSuggestCode;
    /**
     * 其他后端参数
     */
    private List<String> instanceIds;

    private List<String> instanceUuids;

    /**
     * 组织或者工作空间 ID 集合
     */
    private List<String> sourceIds;

    /**
     * 优化建议策略内容
     */
    private String strategyContent;

}
