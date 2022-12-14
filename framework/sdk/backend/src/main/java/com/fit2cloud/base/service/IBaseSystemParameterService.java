package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.SystemParameter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IBaseSystemParameterService extends IService<SystemParameter> {

    public String getValue(String key);

    public void saveValue(SystemParameter systemParameter);
}
