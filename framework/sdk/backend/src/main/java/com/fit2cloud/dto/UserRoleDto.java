package com.fit2cloud.dto;


import com.fit2cloud.base.entity.Role;
import com.fit2cloud.common.constants.RoleConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
            RoleConstants.Type t1 = role1.getType();
            RoleConstants.Type t2 = role2.getType();
            if (!t1.equals(t2)) {
                return t1.ordinal() - t2.ordinal();
            }
            if (role1.getParentRoleId() != null && role2.getParentRoleId() != null) {
                RoleConstants.ROLE p1 = role1.getParentRoleId();
                RoleConstants.ROLE p2 = role2.getParentRoleId();
                if (!p1.equals(p2)) {
                    return p1.ordinal() - p2.ordinal();
                }
            }
            return role1.getId().compareTo(role2.getId());
        })).collect(Collectors.toList());
        return this;
    }
}
