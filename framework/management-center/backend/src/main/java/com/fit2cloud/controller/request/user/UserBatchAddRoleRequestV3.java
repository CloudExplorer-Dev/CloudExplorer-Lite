package com.fit2cloud.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class UserBatchAddRoleRequestV3 {

    @ApiModelProperty(value = "组织/工作空间ID")
    private String sourceId;

    @ApiModelProperty(value = "组织/工作空间类型", notes = "ORGANIZATION | WORKSPACE , 其他默认为没有类型")
    private String type;

    @NotEmpty(message = "关联关系不能为空")
    @ApiModelProperty(value = "用户与角色ID关联列表")
    private List<UserBatchAddRoleObjectV3> userRoleMappings;

}
