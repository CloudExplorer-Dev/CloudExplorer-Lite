package com.fit2cloud.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;

/**
 * 参考 SimpleGrantedAuthority
 */
public class CeGrantedAuthority implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final String role;

    @Getter
    private final String currentModulePermission;


    public CeGrantedAuthority(String role, String currentModulePermission) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
        this.currentModulePermission = currentModulePermission;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CeGrantedAuthority) {
            return this.role.equals(((CeGrantedAuthority) obj).role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }


}
