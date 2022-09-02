package com.fit2cloud.dto;


import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.common.constants.RoleConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
public class UserDto extends User {

    @Serial
    private static final long serialVersionUID = 990142634584088427L;

    private RoleConstants.ROLE currentRole = RoleConstants.ROLE.ANONYMOUS; //不是给前端用的

    private Map<RoleConstants.ROLE, List<UserRoleDto>> roleMap;

    private String currentSource;

    @ApiModelProperty("角色列表")
    private List<Role> roles = new ArrayList<>();

}

