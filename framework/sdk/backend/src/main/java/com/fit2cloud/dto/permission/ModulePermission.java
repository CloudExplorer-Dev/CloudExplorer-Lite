package com.fit2cloud.dto.permission;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModulePermission extends PermissionBase {

    @Getter
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
            return modulePermission;
        }

    }


}
