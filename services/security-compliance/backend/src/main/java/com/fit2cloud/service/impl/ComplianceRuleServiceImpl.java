package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.rule.ComplianceRuleRequest;
import com.fit2cloud.controller.request.rule.PageComplianceRuleRequest;
import com.fit2cloud.controller.response.rule.ComplianceRuleResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleSearchFieldResponse;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceRuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class ComplianceRuleServiceImpl extends ServiceImpl<ComplianceRuleMapper, ComplianceRule> implements IComplianceRuleService {

    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;

    @Override
    public List<ComplianceRuleSearchFieldResponse> listInstanceSearchField(String platform, String resourceType) {
        ResourceTypeConstants instanceTypeConstants = Arrays.stream(ResourceTypeConstants.values())
                .filter(instance -> instance.name().equals(resourceType))
                .findFirst()
                .orElseThrow(() -> new Fit2cloudException(4000, "不支持的资源类型"));

        return CommonUtil.exec(ICloudProvider.of(platform), instanceTypeConstants.getListInstanceSearchField()).stream().map(instanceSearchField -> {
            ComplianceRuleSearchFieldResponse searchFieldResponse = new ComplianceRuleSearchFieldResponse();
            BeanUtils.copyProperties(instanceSearchField, searchFieldResponse);
            searchFieldResponse.setCompares(instanceSearchField.getFieldType().getCompares().stream().map(c -> new DefaultKeyValue<>(c.getMessage(), c.name())).toList());
            return searchFieldResponse;
        }).toList();
    }

    @Override
    public ComplianceRuleResponse save(ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRule complianceRule = new ComplianceRule();
        BeanUtils.copyProperties(complianceRuleRequest, complianceRule);
        complianceRule.setEnable(true);
        save(complianceRule);
        ComplianceRuleResponse complianceRuleResponse = new ComplianceRuleResponse();
        BeanUtils.copyProperties(complianceRule, complianceRuleResponse);
        return complianceRuleResponse;
    }

    @Override
    public Page<ComplianceRuleResponse> page(Integer currentPage, Integer limit, PageComplianceRuleRequest request) {
        LambdaUpdateWrapper<ComplianceRule> wrapper = new LambdaUpdateWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(request.getName()), ComplianceRule::getName, request.getName());
        Page<ComplianceRule> page = this.page(Page.of(currentPage, limit), wrapper);
        Page<ComplianceRuleResponse> responsePage = Page.of(page.getCurrent(), page.getSize(), page.getTotal());
        List<ComplianceRuleGroup> complianceRuleGroups = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            complianceRuleGroups.addAll(complianceRuleGroupService.list(new LambdaQueryWrapper<ComplianceRuleGroup>().in(ComplianceRuleGroup::getId, page.getRecords().stream().map(ComplianceRule::getRuleGroupId).toList())));
        }
        responsePage.setRecords(page.getRecords().stream().map(item -> {
            ComplianceRuleResponse complianceRuleResponse = new ComplianceRuleResponse();
            BeanUtils.copyProperties(item, complianceRuleResponse);
            complianceRuleGroups.stream().filter(group -> group.getId().equals(item.getRuleGroupId())).findFirst()
                    .ifPresent(group -> complianceRuleResponse.setRuleGroupName(group.getName()));
            return complianceRuleResponse;
        }).toList());
        return responsePage;
    }

    @Override
    public List<DefaultKeyValue<String, String>> listResourceType() {
        return Arrays.stream(ResourceTypeConstants.values()).map(resourceType -> new DefaultKeyValue<>(resourceType.getMessage(), resourceType.name())).toList();
    }

    @Override
    public ComplianceRuleResponse update(ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRule complianceRule = new ComplianceRule();
        BeanUtils.copyProperties(complianceRuleRequest, complianceRule);
        this.updateById(complianceRule);
        ComplianceRuleResponse complianceRuleResponse = new ComplianceRuleResponse();
        BeanUtils.copyProperties(complianceRule, complianceRuleResponse);
        return complianceRuleResponse;
    }
}
