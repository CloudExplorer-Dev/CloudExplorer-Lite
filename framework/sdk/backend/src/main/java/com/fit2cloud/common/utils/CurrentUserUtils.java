package com.fit2cloud.common.utils;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Author: LiuDi
 * Date: 2022/9/5 3:37 PM
 */
public class CurrentUserUtils {

    public static UserDto getUser() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 当前用户所在工作空间
     * @return
     */
    public static String getWorkspaceId() {
        return getUser().getCurrentSource();
    }

    /**
     * 当前用户所在组织
     * @return
     */
    public static String getOrganizationId() {
        return getUser().getCurrentSource();
    }

    /**
     * 当前用户是否为系统管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        try {
            return RoleConstants.ROLE.ADMIN.name().equals(CurrentUserUtils.getUser().getCurrentRole().name());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 当前用户是否为组织管理员
     *
     * @return
     */
    public static boolean isOrgAdmin() {
        try {
            return RoleConstants.ROLE.ORGADMIN.name().equals(CurrentUserUtils.getUser().getCurrentRole().name());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 当前用户是否为工作空间用户
     *
     * @return
     */
    public static boolean isUser() {
        try {
            return RoleConstants.ROLE.USER.name().equals(CurrentUserUtils.getUser().getCurrentRole().name());
        } catch (Exception e) {
            return false;
        }
    }

}
