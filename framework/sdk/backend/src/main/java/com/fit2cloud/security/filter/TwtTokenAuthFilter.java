package com.fit2cloud.security.filter;

import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwtTokenAuthFilter extends BasicAuthenticationFilter {


    public TwtTokenAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //super.doFilterInternal(request, response, chain);
        UserDto userDtoFromToken = null;
        String token = request.getHeader(JwtTokenUtils.TOKEN_NAME);
        if (StringUtils.isNotBlank(token)) {
            try {
                userDtoFromToken = JwtTokenUtils.parseJwtToken(token);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        if (userDtoFromToken != null) {
            //todo 额外的校验？

            //todo 权限
            List<SimpleGrantedAuthority> authority = new ArrayList<>();

            //将认证信息存到上下文
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDtoFromToken.getUsername(), token, authority);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (request.getServletPath().startsWith("/api/") || request.getServletPath().equals("/api")) {
                //todo token续期？

                response.setHeader(JwtTokenUtils.TOKEN_NAME, token);
            }

        }

        chain.doFilter(request, response);

    }
}
