package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.constants.GroupTypeConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;
import com.fit2cloud.dao.entity.ComplianceCount;
import com.fit2cloud.dao.entity.ComplianceGroup;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.dao.mapper.ComplianceScanResultMapper;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceScanResultService;
import com.fit2cloud.service.IComplianceViewService;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public class IComplianceViewServiceImpl implements IComplianceViewService {
    @Resource
    private ComplianceScanResultMapper complianceScanResultMapper;
    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;
    @Resource
    private IComplianceRuleService complianceRuleService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;


    @Override
    public List<ComplianceViewGroupResponse> group(ComplianceGroupRequest request) {
        QueryWrapper<ComplianceScanResult> wrapper = getWrapper(request.getCloudAccountId());
        List<ComplianceGroup> groups = complianceScanResultMapper.group(request.getGroupType().name(), wrapper);
        List<DefaultKeyValue<String, String>> keyValueByGroupTypes = getKeyValueByGroupType(request.getGroupType());
        return groups.stream()
                .map(group ->
                        toComplianceViewGroupResponse(group, request.getGroupType(),
                                (key) -> keyValueByGroupTypes.stream().filter(
                                                keyValueByGroupType -> keyValueByGroupType.getKey().equals(key)).
                                        findFirst().map(DefaultKeyValue::getValue).orElse(key))).toList();
    }

    @Override
    public ComplianceViewCountResponse getComplianceViewCountResponse(ComplianceCountRequest request) {
        ComplianceCount complianceCount = complianceScanResultMapper.count(getWrapper(request.getCloudAccountId()));
        ComplianceViewCountResponse complianceViewCountResponse = new ComplianceViewCountResponse();
        complianceViewCountResponse.setNotComplianceCount(complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setComplianceCount(complianceCount.getComplianceCount());
        complianceViewCountResponse.setTotal(complianceCount.getComplianceCount() + complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setNotCompliancePercentage((double) complianceCount.getComplianceCount() / complianceViewCountResponse.getTotal());
        return complianceViewCountResponse;
    }

    /**
     * 根据资源标识 获取资源名称
     *
     * @param groupType 分组类型
     * @return key 资源标识 value 资源名称
     */
    private List<DefaultKeyValue<String, String>> getKeyValueByGroupType(GroupTypeConstants groupType) {
        if (groupType.equals(GroupTypeConstants.RESOURCE_TYPE)) {
            return Arrays.stream(ResourceTypeConstants.values()).map(type -> new DefaultKeyValue<>(type.name(), type.getMessage())).toList();
        }
        if (groupType.equals(GroupTypeConstants.RULE_GROUP)) {
            return complianceRuleGroupService.list().stream().map(complianceRuleGroup -> new DefaultKeyValue<>(complianceRuleGroup.getId(), complianceRuleGroup.getName())).toList();
        }
        if (groupType.equals(GroupTypeConstants.RULE)) {
            return complianceRuleService.list().stream().map(complianceRule -> new DefaultKeyValue<>(complianceRule.getId(), complianceRule.getName())).toList();
        }
        if (groupType.equals(GroupTypeConstants.CLOUD_ACCOUNT)) {
            return cloudAccountService.list().stream().map(cloudAccount -> new DefaultKeyValue<>(cloudAccount.getId(), cloudAccount.getName())).toList();
        }
        return List.of();
    }

    private ComplianceViewGroupResponse toComplianceViewGroupResponse(ComplianceGroup group, GroupTypeConstants groupType, Function<String, String> getGroupName) {
        ComplianceViewGroupResponse complianceViewGroupResponse = new ComplianceViewGroupResponse();
        complianceViewGroupResponse.setComplianceCount(group.getComplianceCount());
        complianceViewGroupResponse.setNotComplianceCount(group.getNotComplianceCount());
        complianceViewGroupResponse.setTotal(group.getComplianceCount() + group.getNotComplianceCount());
        complianceViewGroupResponse.setGroupType(groupType);
        complianceViewGroupResponse.setGroupName(getGroupName.apply(group.getKey()));
        return complianceViewGroupResponse;
    }

    public QueryWrapper<ComplianceScanResult> getWrapper(String cloudAccountId) {
        return new QueryWrapper<ComplianceScanResult>()
                .eq(StringUtils.isNotEmpty(cloudAccountId), ColumnNameUtil.getColumnName(ComplianceScanResult::getCloudAccountId, true), cloudAccountId);
    }
}
