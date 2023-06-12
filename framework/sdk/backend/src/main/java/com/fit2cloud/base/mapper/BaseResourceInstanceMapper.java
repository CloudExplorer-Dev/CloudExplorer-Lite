package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.ResourceInstance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface BaseResourceInstanceMapper extends BaseMapper<ResourceInstance> {

    List<ResourceInstance> listLastResourceInstance(@Param(Constants.WRAPPER) Wrapper<ResourceInstance> wrapper);

}
