package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.base.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesByResourceIds(Map<String,Object> param);

}
