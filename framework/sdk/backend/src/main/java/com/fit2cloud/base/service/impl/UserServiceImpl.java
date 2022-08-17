package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.mapper.UserMapper;
import com.fit2cloud.base.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
