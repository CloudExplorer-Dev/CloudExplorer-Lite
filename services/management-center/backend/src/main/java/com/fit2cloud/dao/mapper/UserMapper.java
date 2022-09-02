package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<User> pageUser(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

}
