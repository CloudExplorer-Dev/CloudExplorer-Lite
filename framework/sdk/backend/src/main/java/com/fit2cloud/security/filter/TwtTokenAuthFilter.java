package com.fit2cloud.security.filter;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.OrganizationConstants;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.constants.WorkspaceConstants;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.security.CeGrantedAuthority;
import com.fit2cloud.security.CeUsernamePasswordAuthenticationToken;
import com.fit2cloud.security.permission.PermissionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwtTokenAuthFilter extends BasicAuthenticationFilter {

    private final PermissionService permissionService;

    private final IBaseUserRoleService userRoleService;

    private final static String CE_SOURCE_TOKEN = "CE_SOURCE";

    public TwtTokenAuthFilter(AuthenticationManager authenticationManager, PermissionService permissionService, IBaseUserRoleService userRoleService) {
        super(authenticationManager);
        this.permissionService = permissionService;
        this.userRoleService = userRoleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //super.doFilterInternal(request, response, chain);
        UserDto userDtoFromToken = null;
        String token = request.getHeader(JwtTokenUtils.TOKEN_NAME);
        RoleConstants.ROLE role = RoleConstants.ROLE.ANONYMOUS;
        try {
            role = RoleConstants.ROLE.valueOf(request.getHeader(RoleConstants.ROLE_TOKEN));
        } catch (Exception ignore) {
        }
        if (StringUtils.isNotBlank(token)) {
            try {
                userDtoFromToken = JwtTokenUtils.parseJwtToken(token);
            } catch (Exception ignore) {
            }
        }
        if (userDtoFromToken != null) {
            //获取角色
            userDtoFromToken.setCurrentRole(role);

            Set<String> rolesForSearchAuthority = new HashSet<>();

            //信任jwt，直接从jwt内取授权的角色
            //List<UserRoleDto> userRoleDtos = userDtoFromToken.getRoleMap().getOrDefault(userDtoFromToken.getCurrentRole(), new ArrayList<>());

            //为了防止用户编辑后与token中角色不同，从redis读取授权的角色
            userDtoFromToken.setRoleMap(userRoleService.getCachedUserRoleMap(userDtoFromToken.getId()));
            List<UserRoleDto> userRoleDtos = userDtoFromToken.getRoleMap().getOrDefault(userDtoFromToken.getCurrentRole(), new ArrayList<>());

            String source = null;
            if (RoleConstants.ROLE.USER.equals(userDtoFromToken.getCurrentRole())) {
                source = request.getHeader(WorkspaceConstants.WORKSPACE_TOKEN);
            } else if (RoleConstants.ROLE.ORGADMIN.equals(userDtoFromToken.getCurrentRole())) {
                source = request.getHeader(OrganizationConstants.ORGANIZATION_TOKEN);
            }
            if (StringUtils.isBlank(source)) {
                source = request.getHeader(CE_SOURCE_TOKEN);
            }
            userDtoFromToken.setCurrentSource(source);

            //判断是否存在传入的role和source
            if (RoleConstants.ROLE.ADMIN.equals(userDtoFromToken.getCurrentRole())) {
                if (CollectionUtils.isNotEmpty(userRoleDtos)) {
                    rolesForSearchAuthority.addAll(userRoleDtos.get(0).getRoles().stream().map(Role::getId).toList());
                }
            } else if (source != null) {
                for (UserRoleDto userRoleDto : userRoleDtos) {
                    if (StringUtils.equals(source, userRoleDto.getSource())) {
                        if (userDtoFromToken.getCurrentRole().equals(userRoleDto.getParentRole())) {
                            rolesForSearchAuthority.addAll(userRoleDto.getRoles().stream().map(Role::getId).toList());
                        }
                        break;
                    }
                }
            }

            rolesForSearchAuthority.add(RoleConstants.ROLE.ANONYMOUS.name()); //默认有匿名用户权限

            List<CeGrantedAuthority> authority = permissionService.readPermissionFromRedis(rolesForSearchAuthority);

            //将认证信息存到上下文
            CeUsernamePasswordAuthenticationToken authenticationToken = new CeUsernamePasswordAuthenticationToken(PermissionService.module, userDtoFromToken, token, authority);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (request.getServletPath().startsWith("/api/") || request.getServletPath().equals("/api")) {
                //todo token续期？

                response.setHeader(JwtTokenUtils.TOKEN_NAME, token);
            }

        }

        chain.doFilter(request, response);

    }
}
