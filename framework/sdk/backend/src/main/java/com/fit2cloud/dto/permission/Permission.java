package com.fit2cloud.dto.permission;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.constants.RoleConstants;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Permission extends PermissionBase {

    @Serial
    private static final long serialVersionUID = 4530372509768915230L;
    @Getter
    private String groupId;
    @Getter
    private String require;
    @Getter
    private String operate;
    @Getter
    private String name;
    @Getter
    private Set<RoleConstants.ROLE> roles;

    public String getId() {
        return "[" + getModule() + "]" + groupId + ":" + (require == null ? "" : (require + "+")) + operate;
    }

    public String getSimpleId() {
        return "[" + getModule() + "]" + groupId + ":" + operate;
    }

    private Permission() {
    }

    public Permission module(String module) {
        setModule(module);
        return this;
    }

    public static class Builder {

        private final Permission permission;

        public Builder() {
            this.permission = new Permission();
        }

        public Builder group(PermissionGroup group) {
            this.permission.groupId = group.getId();
            this.permission.setModule(group.getModule());
            return this;
        }

        public Builder require(@NotNull String require) {
            this.permission.require = require;
            return this;
        }

        public Builder operate(@NotNull String operate) {
            this.permission.operate = operate;
            return this;
        }

        public Builder name(@NotNull String name) {
            this.permission.name = name;
            return this;
        }

        public Builder role(@NotNull RoleConstants.ROLE... roles) {
            if (this.permission.roles == null) {
                this.permission.roles = new HashSet<>();
            }
            this.permission.roles.addAll(Arrays.asList(roles));
            return this;
        }

        public Permission build() {
            return permission;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (!StringUtils.equals(getModule(), that.getModule())) return false;
        if (!StringUtils.equals(groupId, that.groupId)) return false;
        //if (!StringUtils.equals(require, that.require)) return false;
        if (!StringUtils.equals(operate, that.operate)) return false;

        //if (!StringUtils.equals(name, that.name)) return false; //不判断name
        //return Objects.equals(roles, that.roles); //不判断roles，这里是为了判断有没有添加重复权限

        return true;
    }

    @Override
    public int hashCode() {
        int result = getModule() != null ? getModule().hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        //result = 31 * result + (require != null ? require.hashCode() : 0);
        result = 31 * result + (operate != null ? operate.hashCode() : 0);
        //result = 31 * result + (name != null ? name.hashCode() : 0); //不判断name
        //result = 31 * result + (roles != null ? roles.hashCode() : 0); //不判断roles，这里是为了判断有没有添加重复权限
        return result;
    }
}
