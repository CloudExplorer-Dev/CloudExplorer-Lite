package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.SystemParameter;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseSystemParameterService extends IService<SystemParameter> {

    public String getValue(String key);

    public void saveValue(SystemParameter systemParameter);
}
