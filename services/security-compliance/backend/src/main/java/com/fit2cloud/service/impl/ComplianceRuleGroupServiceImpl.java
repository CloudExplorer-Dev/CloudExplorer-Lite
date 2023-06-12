package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.controller.request.rule_group.ComplianceRuleGroupRequest;
import com.fit2cloud.controller.request.rule_group.PageComplianceRuleGroupRequest;
import com.fit2cloud.controller.response.rule_group.ComplianceRuleGroupResponse;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.dao.mapper.ComplianceRuleGroupMapper;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.service.IComplianceRuleGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class ComplianceRuleGroupServiceImpl extends ServiceImpl<ComplianceRuleGroupMapper, ComplianceRuleGroup> implements IComplianceRuleGroupService {
    @Resource
    private ComplianceRuleMapper complianceRuleMapper;

    @Override
    public Page<ComplianceRuleGroupResponse> page(Integer currentPage, Integer limit, PageComplianceRuleGroupRequest request) {
        LambdaUpdateWrapper<ComplianceRuleGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(request.getName()), ComplianceRuleGroup::getName, request.getName());
        Page<ComplianceRuleGroup> page = this.page(Page.of(currentPage, limit), wrapper);
        Page<ComplianceRuleGroupResponse> responsePage = Page.of(currentPage, limit);
        BeanUtils.copyProperties(page, responsePage);
        responsePage.setRecords(page.getRecords().stream().map(item -> {
            ComplianceRuleGroupResponse complianceRuleGroupResponse = new ComplianceRuleGroupResponse();
            BeanUtils.copyProperties(item, complianceRuleGroupResponse);
            return complianceRuleGroupResponse;
        }).toList());
        return responsePage;
    }

    @Override
    public ComplianceRuleGroupResponse save(ComplianceRuleGroupRequest request) {
        ComplianceRuleGroup complianceRuleGroup = new ComplianceRuleGroup();
        BeanUtils.copyProperties(request, complianceRuleGroup);
        this.save(complianceRuleGroup);
        ComplianceRuleGroupResponse complianceRuleGroupResponse = new ComplianceRuleGroupResponse();
        BeanUtils.copyProperties(complianceRuleGroup, complianceRuleGroupResponse);
        return complianceRuleGroupResponse;

    }

    @Override
    public Boolean deleteById(String complianceRuleGroupId) {
        boolean exists = complianceRuleMapper.exists(new LambdaQueryWrapper<ComplianceRule>().eq(ComplianceRule::getRuleGroupId, complianceRuleGroupId));
        if (exists) {
            throw new Fit2cloudException(40001, "当前规则组正在使用中无法删除");
        }
        return removeById(complianceRuleGroupId);

    }

    @Override
    public ComplianceRuleGroupResponse update(ComplianceRuleGroupRequest request) {
        ComplianceRuleGroup complianceRuleGroup = new ComplianceRuleGroup();
        BeanUtils.copyProperties(request, complianceRuleGroup);
        this.updateById(complianceRuleGroup);
        ComplianceRuleGroupResponse complianceRuleGroupResponse = new ComplianceRuleGroupResponse();
        BeanUtils.copyProperties(getById(request.getId()), complianceRuleGroupResponse);
        return complianceRuleGroupResponse;
    }

}
