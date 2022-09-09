package com.fit2cloud.request.role;

import com.fit2cloud.common.constants.RoleConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@Data
public class RoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8484185034593083260L;

    @ApiModelProperty("角色ID")
    private String id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色分类")
    private List<RoleConstants.Type> type;

    @ApiModelProperty("继承角色")
    private List<RoleConstants.ROLE> parentRoleId;

    @ApiModelProperty("根据该字段查询该角色及下一级角色，如传ORGAMIN则返回ORGAMIN及USER")
    private RoleConstants.ROLE baseRole;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "修改时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;


}
