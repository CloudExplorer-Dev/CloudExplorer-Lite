package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.Role;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IRoleService extends IService<Role> {

    /**获取用户具有的角色
     * @param param
     * @return
     */
    List<Role> getRolesByResourceIds(Map<String, Object> param);

    List<Role> roles();
}
