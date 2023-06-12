package com.fit2cloud.controller.request.disk;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/27 14:45
 **/
@Data
@Accessors(chain = true)
public class PageVmCloudDiskRequest extends PageRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = -7824240660208325381L;

    @Schema(title = "名称")
    private String diskName;

    @Schema(title = "ID")
    private String id;

    @Schema(title = "ID列表")
    private List<String> ids;

    @Schema(title = "组织ID")
    private String organizationId;
    @Schema(title = "组织IDs")
    private List<String> organizationIds;
    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;
    @Schema(title = "组织名称")
    private String organizationName;
    @Schema(title = "工作空间ID")
    private String workspaceId;
    @Schema(title = "工作空间IDs")
    private List<String> workspaceIds;
    @Schema(title = "工作空间名称")
    private String workspaceName;
    @Schema(title = "云账号名称")
    private String accountName;
    @Schema(title = "所属云主机")
    private String vmInstanceName;
    @Schema(title = "是否随实例删除")
    private List<String> deleteWithInstance;
    @Schema(title = "磁盘类型")
    private List<String> diskType;
    @Schema(title = "磁盘属性")
    private List<Boolean> bootable;
    @Schema(title = "磁盘状态")
    private String status;
    @Schema(title = "云账号IDs")
    private List<String> accountIds;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "到期时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> expireTime;
    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
