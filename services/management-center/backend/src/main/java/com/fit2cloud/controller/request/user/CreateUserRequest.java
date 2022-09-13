package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.entity.User;
import com.fit2cloud.dto.RoleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/8/30 4:26 PM
 */
@Data
public class CreateUserRequest extends User {

    @ApiModelProperty(value = "角色信息列表", hidden = true)
    private List<RoleInfo> roleInfoList;
}
