package com.fit2cloud.controller.request.user;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.List;

@Data
public class PageUserRequest extends PageRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = -5792220537769141979L;

    @Schema(title = "用户ID，模糊匹配")
    private String username;

    @Schema(title = "姓名，模糊匹配")
    private String name;

    @Schema(title = "邮箱，模糊匹配")
    private String email;

    @Schema(title = "角色ID，精确匹配")
    private String roleId;

    @Schema(title = "角色名称，模糊匹配")
    private String roleName;

    @Schema(title = "工作空间ID，精确匹配")
    private String workspaceId;

    @Schema(title = "组织ID，精确匹配")
    private String organizationId;

    @Schema(title = "父角色类型，精确匹配 ADMIN | ORGADMIN | USER")
    private RoleConstants.ROLE parentRole;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "编辑时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
