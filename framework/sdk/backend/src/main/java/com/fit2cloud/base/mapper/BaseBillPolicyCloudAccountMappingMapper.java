package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
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
public interface BaseBillPolicyCloudAccountMappingMapper extends BaseMapper<BillPolicyCloudAccountMapping> {
    /**
     * 查询云账号的最后关联记录
     *
     * @param wrapper 查询条件
     * @return 云账号关联
     */
    List<BillPolicyCloudAccountMapping> listLast(@Param(Constants.WRAPPER) Wrapper<BillPolicyCloudAccountMapping> wrapper);
}
