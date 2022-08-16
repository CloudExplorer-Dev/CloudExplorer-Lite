package com.fit2cloud.base.service.impl;

import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.mapper.UserMapper;
import com.fit2cloud.base.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 2022-08-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    public void test(){

    }

}
