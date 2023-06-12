package com.fit2cloud.controller.request.vm;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/27 14:45
 **/
@Data
public class PageVmCloudServerRequest extends PageRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = -6363229760062682871L;

    @Schema(title = "云主机状态", allowableValues = "Running,Stopped,Deleted")
    private String instanceStatus;

    @Schema(title = "云主机名称")
    private String instanceName;

    @Schema(title = "云账号IDs")
    private List<String> accountIds;

    @Schema(title = "云主机ID")
    private String cloudServerId;

    @Schema(title = "云账号名称")
    private String accountName;

    @Schema(title = "数据中心")
    private String region;

    @Schema(title = "集群")
    private String zone;

    @Schema(title = "公网IP")
    private String remoteIp;

    @Schema(title = "产品名称")
    private String productName;

    @Schema(title = "IP地址")
    private String ipArray;

    @Schema(title = "工作空间ID")
    private String workspaceId;

    @Schema(title = "工作空间IDs")
    private List<String> workspaceIds;

    @Schema(title = "组织ID")
    private String organizationId;

    @Schema(title = "组织IDs")
    private List<String> organizationIds;

    @Schema(title = "组织或者工作空间集合")
    private List<String> sourceIds;

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "操作系统")
    private String os;

    @Schema(title = "操作系统版本")
    private List<String> osVersion;

    @Schema(title = "云主机ID")
    private List<String> ids;

    @Schema(hidden = true)
    private String sort;

    @Schema(title = "projectId")
    private String projectId;

    @Schema(title = "操作系统信息")
    private String osInfo;

    @Schema(title = "主机名")
    private String hostname;

    @Schema(title = "申请人")
    private String applyUser;

    @Schema(title = "付费类型")
    private List<String> instanceChargeType;

    @Schema(title = "VMTools状态")
    private List<String> vmToolsStatus;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "到期时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> expireTime;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
