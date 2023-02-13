package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.log.utils.IpUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.common.utils.MD5Util;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.LoginRequest;
import com.fit2cloud.request.user.EditUserRequest;
import com.fit2cloud.request.user.ResetPwdRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseUserServiceImpl extends ServiceImpl<BaseUserMapper, User> implements IBaseUserService {

    @Resource
    private IBaseUserRoleService userRoleService;

    @Override
    public String login(ServerHttpRequest request, LoginRequest loginRequest) {

        if (StringUtils.isBlank(loginRequest.getUsername())) {
            throw new RuntimeException("用户名为空");
        }

        if (StringUtils.isBlank(loginRequest.getPassword())) {
            throw new RuntimeException("密码为空");
        }

        UserDto user = getUserByIdOrEmail(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getEnabled()) {
            throw new RuntimeException("用户已被禁用");
        }

        if (!checkPassword(user, loginRequest.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        //将当前用户的授权角色更新到redis
        userRoleService.saveCachedUserRoleMap(user.getId());

        System.out.println(IpUtil.getIpAddress(request));
        user.setIp(IpUtil.getIpAddress(request));
        user.setLoginTime(LocalDateTime.now());

        return JwtTokenUtils.createJwtToken(user);
    }

    @Override
    public UserDto getUserByIdOrEmail(String username) {
        User user = getUserByUserName(username);
        if (user == null) {
            user = getUserByEmail(username);
        }

        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        //dto.setRoleMap(userRoleService.getUserRoleMap(dto.getId()));
        return dto;
    }


    public User getUserById(String userId) {
        return this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getId, userId)
        );
    }

    public User getUserByUserName(String username) {
        return this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );
    }

    public User getUserByEmail(String email) {
        return this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getEmail, email)
        );
    }

    @Override
    public boolean checkPassword(User user, String password) {
        BaseUserMapper userMapper = this.getBaseMapper();
        return userMapper.checkPassword(user.getUsername(), MD5Util.md5(password));
    }

    @Override
    public boolean updateUserBasicInfo(EditUserRequest request) {
        // 校验修改的用户邮箱是否已存在
        if (StringUtils.isNotEmpty(request.getEmail())) {
            if (this.count(new LambdaQueryWrapper<User>().ne(User::getId, request.getId()).eq(User::getEmail, request.getEmail())) > 0) {
                throw new RuntimeException("邮箱已存在");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(request,user);
        baseMapper.updateById(user);
        return true;
    }

    @Override
    public boolean resetPwd(ResetPwdRequest request) {
        // 非本地创建用户不允许修改密码
        if (!"local".equalsIgnoreCase(CurrentUserUtils.getUser().getSource())) {
            throw new RuntimeException("非云管本地创建的用户无法修改密码");
        }

        UserDto userDto = CurrentUserUtils.getUser();
        Optional.ofNullable(userDto).orElseThrow(() -> new RuntimeException("无法获取当前登录用户"));
        User user = this.getUserById(userDto.getId());
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!MD5Util.md5(request.getOldPassword()).equalsIgnoreCase(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        user.setPassword(MD5Util.md5(request.getNewPassword()));
        this.updateById(user);
        return true;
    }
}
