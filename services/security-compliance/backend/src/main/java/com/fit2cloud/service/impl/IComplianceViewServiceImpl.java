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
import com.fit2cloud.controller.response.view.ComplianceViewRuleCountResponse;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.entity.*;
import com.fit2cloud.dao.mapper.ComplianceScanResultMapper;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceRuleService;
import com.fit2cloud.service.IComplianceViewService;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<ComplianceViewGroupResponse> resourceGroup(ComplianceGroupRequest request) {
        QueryWrapper<ComplianceScanResult> wrapper = getWrapper(request.getCloudAccountId());
        List<ComplianceGroup> groups = complianceScanResultMapper.group(request.getGroupType().name(), wrapper);
        List<DefaultKeyValue<String, String>> keyValueByGroupTypes = getKeyValueByGroupType(request.getGroupType());
        return groups.stream().map(group -> toComplianceViewGroupResponse(group, request.getGroupType(), (key) -> keyValueByGroupTypes.stream().filter(keyValueByGroupType -> keyValueByGroupType.getKey().equals(key)).findFirst().map(DefaultKeyValue::getValue).orElse(key))).toList();
    }

    @Override
    public ComplianceViewCountResponse resourceCount(ComplianceCountRequest request) {
        ComplianceCount complianceCount = complianceScanResultMapper.count(getWrapper(request.getCloudAccountId()));
        ComplianceViewCountResponse complianceViewCountResponse = new ComplianceViewCountResponse();
        complianceViewCountResponse.setNotComplianceCount(complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setComplianceCount(complianceCount.getComplianceCount());
        complianceViewCountResponse.setTotal(complianceCount.getComplianceCount() + complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setNotCompliancePercentage((double) complianceCount.getNotComplianceCount() / complianceViewCountResponse.getTotal());
        return toComplianceViewCountResponse(complianceCount);
    }

    @Override
    public Map<RiskLevel, ComplianceViewRuleCountResponse> ruleCount(ComplianceCountRequest request) {
        QueryWrapper<ComplianceScanResult> eq = new QueryWrapper<ComplianceScanResult>()
                .eq(StringUtils.isNotEmpty(request.getCloudAccountId()), "compliance_scan_result.cloud_account_id", request.getCloudAccountId());
        List<ComplianceRuleCount> complianceRuleCounts = complianceScanResultMapper.ruleCount(eq);
        return complianceRuleCounts.stream().collect(Collectors.groupingBy(ComplianceRuleCount::getRiskLevel, Collectors.mapping(c -> {
                    ComplianceViewRuleCountResponse complianceViewRuleCountResponse = new ComplianceViewRuleCountResponse();
                    BeanUtils.copyProperties(c, complianceViewRuleCountResponse);
                    return complianceViewRuleCountResponse;
                }, Collectors.reducing((pre, next) -> {
                    if (Objects.nonNull(pre.getRiskLevel())) {
                        pre.setComplianceCount(pre.getComplianceCount() + next.getComplianceCount());
                        pre.setNotComplianceCount(pre.getNotComplianceCount() + next.getNotComplianceCount());
                        pre.setTotal(pre.getTotal() + next.getTotal());
                    }
                    return pre;
                }))))
                .entrySet()
                .stream()
                .map(entry -> new DefaultKeyValue<>(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toMap(DefaultKeyValue::getKey, DefaultKeyValue::getValue));

    }


    private ComplianceViewCountResponse toComplianceViewCountResponse(ComplianceCount complianceCount) {
        ComplianceViewCountResponse complianceViewCountResponse = new ComplianceViewCountResponse();
        complianceViewCountResponse.setNotComplianceCount(complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setComplianceCount(complianceCount.getComplianceCount());
        complianceViewCountResponse.setTotal(complianceCount.getComplianceCount() + complianceCount.getNotComplianceCount());
        complianceViewCountResponse.setNotCompliancePercentage((double) complianceCount.getNotComplianceCount() / complianceViewCountResponse.getTotal());
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


    /**
     * 转换聚合对象
     *
     * @param group        分组对象
     * @param groupType    分组类型
     * @param getGroupName 分组名称
     * @return
     */
    private ComplianceViewGroupResponse toComplianceViewGroupResponse(ComplianceGroup group, GroupTypeConstants groupType, Function<String, String> getGroupName) {
        ComplianceViewGroupResponse complianceViewGroupResponse = new ComplianceViewGroupResponse();
        complianceViewGroupResponse.setComplianceCount(group.getComplianceCount());
        complianceViewGroupResponse.setNotComplianceCount(group.getNotComplianceCount());
        complianceViewGroupResponse.setTotal(group.getComplianceCount() + group.getNotComplianceCount());
        complianceViewGroupResponse.setGroupType(groupType);
        complianceViewGroupResponse.setGroupName(getGroupName.apply(group.getKey()));
        complianceViewGroupResponse.setGroupValue(group.getKey());
        return complianceViewGroupResponse;
    }

    /**
     * 获取查询对象
     *
     * @param cloudAccountId 云账号id
     * @return 查询对象
     */
    public QueryWrapper<ComplianceScanResult> getWrapper(String cloudAccountId) {
        return new QueryWrapper<ComplianceScanResult>().eq(StringUtils.isNotEmpty(cloudAccountId), ColumnNameUtil.getColumnName(ComplianceScanResourceResult::getCloudAccountId, true), cloudAccountId);
    }
}
