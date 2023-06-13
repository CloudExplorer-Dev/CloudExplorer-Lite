package com.fit2cloud.dto.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.dto.CeBaseObject;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ModulePermission extends CeBaseObject {

    @Serial
    private static final long serialVersionUID = -2139932042065786160L;
    @Getter
    @Setter
    private List<PermissionGroup> groups;

    private ModulePermission() {
    }

    public static class Builder {

        private final ModulePermission modulePermission;

        public Builder() {
            this.modulePermission = new ModulePermission();
        }

        public Builder module(@NotNull String module) {
            this.modulePermission.setModule(module);
            if (this.modulePermission.groups != null) {
                for (PermissionGroup group : this.modulePermission.groups) {
                    group.module(module);
                }
            }

            return this;
        }

        public Builder group(@NotNull PermissionGroup.Builder... groupBuilder) {
            if (this.modulePermission.groups == null) {
                this.modulePermission.groups = new ArrayList<>();
            }
            for (PermissionGroup.Builder builder : groupBuilder) {
                builder.module(this.modulePermission.getModule());
                this.modulePermission.groups.add(builder.build());
            }
            return this;
        }

        public ModulePermission build() {
            for (PermissionGroup group : modulePermission.getGroups()) {
                Map<String, Permission> map = group.getPermissions().stream().collect(Collectors.toMap(Permission::getOperate, permission -> permission));
                //防止配置的时候有写错，要把父级权限内没有的角色，子权限里有角色的权限移除
                group.setPermissions(
                        group.getPermissions().stream().filter(permission -> {
                            if (StringUtils.isNotBlank(permission.getRequire())) {
                                if (map.get(permission.getRequire()) == null) {
                                    log.warn("permission: {} does not exist for require: {}", permission.getId(), permission.getRequire());
                                    return false; //不存在这个父级
                                } else {
                                    permission.setRoles(permission.getRoles().stream().filter(role -> map.get(permission.getRequire()).getRoles().contains(role)).collect(Collectors.toSet()));
                                    if (CollectionUtils.isEmpty(permission.getRoles())) { //去除父级不存在的角色后若没有权限，就排除
                                        log.warn("permission: {} does not exist for require: {}", permission.getId(), permission.getRequire());
                                        return false;
                                    }
                                }
                            }
                            return true;
                        }).collect(Collectors.toList())
                );
            }
            return modulePermission;
        }

    }


}
