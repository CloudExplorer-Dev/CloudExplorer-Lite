package com.fit2cloud.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.OrganizationConstants;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.constants.WorkspaceConstants;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.security.CeGrantedAuthority;
import com.fit2cloud.security.CeUsernamePasswordAuthenticationToken;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.TokenPoolService;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class JwtTokenAuthFilter extends BasicAuthenticationFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final BasePermissionService basePermissionService;

    private final IBaseUserRoleService userRoleService;
    private final TokenPoolService tokenPoolService;

    public final static String CE_SOURCE_TOKEN = "CE-SOURCE";

    public JwtTokenAuthFilter(AuthenticationManager authenticationManager, BasePermissionService basePermissionService, IBaseUserRoleService userRoleService, TokenPoolService tokenPoolService) {
        super(authenticationManager);
        this.basePermissionService = basePermissionService;
        this.userRoleService = userRoleService;
        this.tokenPoolService = tokenPoolService;
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
        boolean tokenIdValid = false;
        if (StringUtils.isNotBlank(token)) {
            try {
                KeyValue<String, UserDto> keyValue = JwtTokenUtils.parseJwtToken(token);
                userDtoFromToken = keyValue.getValue();
                if (userDtoFromToken != null) {
                    tokenIdValid = tokenPoolService.JWTValid(userDtoFromToken.getId(), keyValue.getKey());
                }
            } catch (Exception ignore) {
            }
        }
        if (userDtoFromToken != null && tokenIdValid) {
            //获取角色
            userDtoFromToken.setCurrentRole(role);

            //为了防止用户编辑后与token中角色不同，从redis读取授权的角色
            userDtoFromToken.setRoleMap(userRoleService.getCachedUserRoleMap(userDtoFromToken.getId()));

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

            Set<String> rolesForSearchAuthority = basePermissionService.rolesForSearchAuthority(userDtoFromToken.getRoleMap(), userDtoFromToken.getCurrentRole(), source);

            List<CeGrantedAuthority> authority = basePermissionService.readPermissionFromRedis(rolesForSearchAuthority);

            //将认证信息存到上下文
            CeUsernamePasswordAuthenticationToken authenticationToken = new CeUsernamePasswordAuthenticationToken(ServerInfo.module, userDtoFromToken, token, authority);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            SecurityContextHolder.clearContext();
            //throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            if (request.getServletPath().startsWith("/api/") || request.getServletPath().equals("/api")) {
                try {
                    writeErrorMessage(request, response, HttpStatus.UNAUTHORIZED, null);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        chain.doFilter(request, response);

    }


    private void writeErrorMessage(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.getServletPath());
        errorAttributes.put("status", status.value());
        errorAttributes.put("error", status.getReasonPhrase());
        errorAttributes.put("message", message);

        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, errorAttributes);
    }

}
