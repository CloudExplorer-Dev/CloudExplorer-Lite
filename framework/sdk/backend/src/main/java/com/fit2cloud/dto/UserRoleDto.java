package com.fit2cloud.dto;


import com.fit2cloud.base.entity.Role;
import com.fit2cloud.common.constants.RoleConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Accessors(chain = true)
@Getter
@Setter
public class UserRoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -485123063773421741L;

    private RoleConstants.ROLE parentRole;

    private String source;

    private String parentSource;

    private List<Role> roles;

    public UserRoleDto sortRoles() {
        if (this.roles == null) {
            return this;
        }
        this.roles = this.roles.stream().sorted(Comparator.comparing(role -> role, (role1, role2) -> {
            RoleConstants.Type t1 = RoleConstants.Type.valueOf(role1.getType());
            RoleConstants.Type t2 = RoleConstants.Type.valueOf(role2.getType());
            if (!t1.equals(t2)) {
                return t1.ordinal() - t2.ordinal();
            }
            if (StringUtils.isNotBlank(role1.getParentRole()) && StringUtils.isNotBlank(role2.getParentRole())) {
                RoleConstants.ROLE p1 = RoleConstants.ROLE.valueOf(role1.getParentRole());
                RoleConstants.ROLE p2 = RoleConstants.ROLE.valueOf(role2.getParentRole());
                if (!p1.equals(p2)) {
                    return p1.ordinal() - p2.ordinal();
                }
            }
            return role1.getRole().compareTo(role2.getRole());
        })).collect(Collectors.toList());
        return this;
    }
}
