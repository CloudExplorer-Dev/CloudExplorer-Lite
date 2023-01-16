package com.fit2cloud.service;

import java.util.List;

/**
 * @author : LiuDi
 * @date : 2023/1/13 13:45
 */
public interface IPermissionService {

    /**
     * 获取当前登录角色具有权限查看的组织或者工作空间 ID 集合
     *
     * @return
     */
     List<String> getSourceIds();
}
