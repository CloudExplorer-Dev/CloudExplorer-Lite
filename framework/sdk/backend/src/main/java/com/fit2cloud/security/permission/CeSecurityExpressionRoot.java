package com.fit2cloud.security.permission;

import com.fit2cloud.security.CeGrantedAuthority;
import com.fit2cloud.security.CeUsernamePasswordAuthenticationToken;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CeSecurityExpressionRoot extends SecurityExpressionRoot {

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public CeSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    /**
     * 判断是否有当前模块内权限
     * // @PreAuthorize("hasCePermission('group','operate')")
     */
    public boolean hasCePermission(String permission) {
        Set<String> authorities = getCurrentModulePermissions();
        return checkPermission(permission, authorities);
    }

    /**
     * 判断是否有当前模块内权限
     * // @PreAuthorize("hasCePermission('group:operate')")
     */
    public boolean hasCePermission(String group, String operate) {
        return hasCePermission(group + ":" + operate);
    }

    /**
     * 判断是否有当前模块内权限
     * // @PreAuthorize("hasAnyCePermission('group1:operate1', 'group2:operate2')")
     */
    public boolean hasAnyCePermission(String... permissions) {

        String module = getModuleName();

        Set<String> authorities = getCurrentModulePermissions();

        for (String permission : permissions) {
            try {
                if (checkPermission(permission, authorities)) {
                    return true;
                }
            } catch (Exception ignore) {
            }
        }
        return false;
    }

    private String getModuleName() {
        CeUsernamePasswordAuthenticationToken authenticationToken = (CeUsernamePasswordAuthenticationToken) this.getAuthentication();
        return authenticationToken.getModule();
    }

    private Set<String> getCurrentModulePermissions() {
        List<CeGrantedAuthority> authorityList = (List<CeGrantedAuthority>) this.getAuthentication().getAuthorities();
        return authorityList.stream().map(CeGrantedAuthority::getCurrentModulePermission).collect(Collectors.toSet());
    }

    private boolean checkPermission(String permission, Set<String> authorities) {
        return authorities.contains("[" + getModuleName() + "]" + permission);
    }

}
