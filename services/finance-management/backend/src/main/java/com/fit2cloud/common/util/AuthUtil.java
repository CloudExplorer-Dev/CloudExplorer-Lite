package com.fit2cloud.common.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/7  17:34}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AuthUtil {
    /**
     * 获取当前角色
     *
     * @return 角色
     */
    public static String currentRule() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
        return userDto.getCurrentRole().name();
    }

    /**
     * 当前用户所属id
     *
     * @return 组织/工作空间id
     */
    public static String currentSource() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
        return userDto.getCurrentSource();
    }

    /**
     * 获取当前用户角色的查询Query
     *
     * @return 获取当前用户角色查询Query
     */
    public static Query getAuthQuery(Function<String, Integer> getOrgLevel) {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDto userDto = (UserDto) context.getAuthentication().getPrincipal();
        return getOrgAuthQuery(userDto.getCurrentRole(), userDto.getCurrentSource(), getOrgLevel);
    }

    /**
     * 获取当前用户角色的查询Query
     *
     * @param currentRule   当前角色
     * @param currentSource 当前资源数据 组织id/工作空间id
     * @param getOrgLevel   获取组织级别函数
     * @return 获取当前用户角色查询Query
     */
    public static Query getOrgAuthQuery(RoleConstants.ROLE currentRule, String currentSource, Function<String, Integer> getOrgLevel) {
        if (StringUtils.isEmpty(currentSource)) {
            return null;
        }
        if (currentRule.equals(RoleConstants.ROLE.ADMIN)) {
            // 如果是管理员则查询全部
            return null;
        }
        if (currentRule.equals(RoleConstants.ROLE.ORGADMIN)) {
            Integer orgLevel = getOrgLevel.apply(currentSource);
            return new Query.Builder().term(new TermQuery.Builder().field("orgTree." + orgLevel + "级组织").value(currentSource).build()).build();
        }
        if (currentRule.equals(RoleConstants.ROLE.USER)) {
            return new Query.Builder().term(new TermQuery.Builder().field("workspaceId").value(currentSource).build()).build();
        }
        return null;
    }
}
