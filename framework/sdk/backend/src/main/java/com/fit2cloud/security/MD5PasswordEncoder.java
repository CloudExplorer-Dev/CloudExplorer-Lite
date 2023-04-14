package com.fit2cloud.security;

import com.fit2cloud.common.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;


public class MD5PasswordEncoder implements PasswordEncoder {

    //进行MD5加密
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.md5(rawPassword.toString());
    }

    //进行密码比对
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.md5(rawPassword.toString()));
    }
}
