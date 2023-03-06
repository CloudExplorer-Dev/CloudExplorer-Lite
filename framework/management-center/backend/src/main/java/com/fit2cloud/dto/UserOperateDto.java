package com.fit2cloud.dto;

import com.fit2cloud.base.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserOperateDto extends User {

    @ApiModelProperty(value = "角色信息列表")
    private List<RoleInfo> roleInfoList;

}
