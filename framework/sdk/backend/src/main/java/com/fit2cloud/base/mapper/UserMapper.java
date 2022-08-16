package com.fit2cloud.base.mapper;

import com.fit2cloud.base.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 2022-08-16
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select if((select password from user where username = #{username}) = #{password}, 1, 0)")
    boolean checkPassword(@Param("username") String userId, @Param("password") String password);

}
