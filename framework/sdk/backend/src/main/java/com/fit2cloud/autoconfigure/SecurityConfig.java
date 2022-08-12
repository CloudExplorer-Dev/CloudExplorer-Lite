package com.fit2cloud.autoconfigure;

import com.fit2cloud.security.MD5PasswordEncoder;
import com.fit2cloud.security.UserAuthDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static com.fit2cloud.security.SecurityDSL.securityDSL;


@EnableWebSecurity //可以不添加，spring boot的WebSecurityEnablerConfiguration已经引入了该注解
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //禁用csrf
                .csrf().disable()
                //禁用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //指定需要认证的路径
                .antMatchers("/api/**").authenticated()
                //禁用OPTIONS方法
                //.antMatchers(HttpMethod.OPTIONS).denyAll()
                //剩下的全部免认证
                .anyRequest().permitAll()
                .and()
                //关闭form登录
                .formLogin().disable()
                //关闭basic认证
                .httpBasic().disable()
                //logout url
                .logout(logout -> logout.logoutUrl("/logout"));

        http.apply(securityDSL());

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


}
