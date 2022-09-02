package com.fit2cloud.dto.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.CeBaseObject;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuPermission extends CeBaseObject {

    @Serial
    private static final long serialVersionUID = 3352396364776276743L;

    @Getter
    private RoleConstants.ROLE role;

    @Getter
    private List<Permission> permissions;

    public MenuPermission module(String module) {
        setModule(module);
        for (Permission permission : this.permissions) {
            permission.setModule(module);
        }
        return this;
    }

    private MenuPermission() {
    }

    public static class Builder {
        private final MenuPermission menuPermission;

        public Builder() {
            this.menuPermission = new MenuPermission();
        }

        public Builder module(String module) {
            this.menuPermission.setModule(module);
            for (Permission permission : this.menuPermission.permissions) {
                permission.setModule(module);
            }
            return this;
        }

        public Builder role(RoleConstants.ROLE role) {
            this.menuPermission.role = role;
            return this;
        }

        public Builder permission(String group, String operate) {
            if (this.menuPermission.permissions == null) {
                this.menuPermission.permissions = new ArrayList<>();
            }
            this.menuPermission.permissions.add(new Permission(group, operate, this.menuPermission.getModule()));
            return this;
        }

        public MenuPermission build() {
            return menuPermission;
        }
    }

    private static class Permission extends CeBaseObject {

        @Serial
        private static final long serialVersionUID = -1362887965408297503L;
        @Getter
        private String group;
        @Getter
        private String operate;


        public String getSimpleId() {
            return "[" + getModule() + "]" + group + ":" + operate;
        }

        public Permission(String group, String operate, String module) {
            this.group = group;
            this.operate = operate;
            setModule(module);
        }
    }
}
