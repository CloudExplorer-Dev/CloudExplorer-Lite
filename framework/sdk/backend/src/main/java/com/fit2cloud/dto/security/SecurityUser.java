package com.fit2cloud.dto.security;

import com.fit2cloud.dto.UserDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
public class SecurityUser implements UserDetails {
    //当前登录用户
    private transient UserDto currentUserInfoDto;

    //当前权限
    private List<String> permissionValueList;

    public SecurityUser() {
    }

    public SecurityUser(UserDto userDto) {
        if (userDto != null) {
            this.currentUserInfoDto = userDto;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Collection<SimpleGrantedAuthority> authorities = permissionValueList.stream().map(SimpleGrantedAuthority::new).toList();
        return new ArrayList<>();
    }


    @Override
    public String getPassword() {
        return currentUserInfoDto.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfoDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return currentUserInfoDto.getEnabled();
    }
}
