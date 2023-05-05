package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.dto.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<User> listUser(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    List<RoleInfo> roleInfo(Map<String, Object> param);

    Long countActiveUsers(@Param("roleId") String roleId);
}
