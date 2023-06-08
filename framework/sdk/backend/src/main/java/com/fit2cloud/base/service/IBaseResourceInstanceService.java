package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.ResourceInstance;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseResourceInstanceService extends IService<ResourceInstance> {
    List<ResourceInstance> listLastResourceInstance(Wrapper<ResourceInstance> wrapper);
}
