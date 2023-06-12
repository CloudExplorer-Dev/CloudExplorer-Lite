package com.fit2cloud.autoconfigure;

import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.security.MD5PasswordEncoder;
import com.fit2cloud.security.UserAuthDetailsService;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.TokenPoolService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

import static com.fit2cloud.security.SecurityDSL.securityDSL;

@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private BasePermissionService basePermissionService;
    @Resource
    private IBaseUserRoleService userRoleService;
    @Resource
    private TokenPoolService tokenPoolService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 设置上下文存储为InheritableThreadLocal 这样子线程也可以拿到上下文数据
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        http
                //禁用csrf
                .csrf(AbstractHttpConfigurer::disable)
                //禁用session
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                //指定需要认证的路径
                                .requestMatchers("/api/**").authenticated()
                                //剩下的全部免认证
                                .anyRequest().permitAll()
                )
                //关闭form登录
                .formLogin(AbstractHttpConfigurer::disable)
                //关闭basic认证
                .httpBasic(AbstractHttpConfigurer::disable)
                //logout url
                .logout(logout -> logout.logoutUrl("/logout"));

        http.apply(securityDSL(basePermissionService, userRoleService, tokenPoolService));

        return http.build();
    }

    @Bean
    public MD5PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public UserAuthDetailsService userAuthDetailsService() {
        return new UserAuthDetailsService();
    }

    /*@Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CeMethodSecurityExpressionHandler();
    }*/

}
