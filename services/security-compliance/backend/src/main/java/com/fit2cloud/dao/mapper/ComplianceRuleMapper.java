package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.dao.entity.ComplianceRuleCloudAccount;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface ComplianceRuleMapper extends BaseMapper<ComplianceRule> {

    IPage<ComplianceRuleCloudAccount> page(IPage<ComplianceRuleCloudAccount> page, @Param(Constants.WRAPPER) Wrapper<ComplianceRuleCloudAccount> wrapper);
}
