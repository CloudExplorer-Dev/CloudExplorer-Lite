package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.SystemParameter;
import com.fit2cloud.base.mapper.BaseSystemParameterMapper;
import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Service
public class BaseSystemParameterServiceImpl extends ServiceImpl<BaseSystemParameterMapper, SystemParameter> implements IBaseSystemParameterService {

    public String getValue(String key) {
        LambdaQueryWrapper<SystemParameter> wrapper = new LambdaQueryWrapper<SystemParameter>();
        wrapper.eq(StringUtils.isNotBlank(key), SystemParameter::getParamKey, key);
        SystemParameter systemParameter = this.getOne(wrapper);
        if (systemParameter == null) {
            return null;
        }
        return systemParameter.getParamValue();
    }

    public void saveValue(SystemParameter systemParameter) {
        LambdaQueryWrapper<SystemParameter> wrapper = new LambdaQueryWrapper<SystemParameter>();
        wrapper.eq(StringUtils.isNotEmpty(systemParameter.getParamKey()),SystemParameter::getParamKey,systemParameter.getParamKey());
        if (!this.update(systemParameter,wrapper)) {
            this.save(systemParameter);
        }
    }
}
