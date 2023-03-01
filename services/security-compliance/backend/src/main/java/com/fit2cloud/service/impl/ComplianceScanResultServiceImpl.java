package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.page.PageImpl;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanRuleGroupResultResponse;
import com.fit2cloud.dao.constants.ComplianceStatus;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.dao.mapper.ComplianceScanResultMapper;
import com.fit2cloud.service.IComplianceRuleGroupService;
import com.fit2cloud.service.IComplianceScanResultService;
import jodd.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 */
@Service
public class ComplianceScanResultServiceImpl extends ServiceImpl<ComplianceScanResultMapper, ComplianceScanResult> implements IComplianceScanResultService {
    @Resource
    private IComplianceRuleGroupService complianceRuleGroupService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @Override
    public List<ComplianceScanResultResponse> list(ComplianceScanRequest request) {
        QueryWrapper<ComplianceScanResultResponse> wrapper = getQueryWrapper(request);
        return this.baseMapper.list(wrapper, request.getCloudAccountId());
    }

    @Override
    public IPage<ComplianceScanResultResponse> page(Integer currentPage, Integer limit, ComplianceScanRequest request) {
        Page<ComplianceScanResultResponse> page = PageImpl.of(currentPage, limit, List.of(ComplianceScanResult.class, ComplianceRule.class),
                Objects.nonNull(request.getOrder()) ? request.getOrder() : OrderItem.desc(ColumnNameUtil.getColumnName(ComplianceScanResult::getNotComplianceCount, true)), true);
        QueryWrapper<ComplianceScanResultResponse> wrapper = getQueryWrapper(request);
        return this.baseMapper.page(page, wrapper, request.getCloudAccountId());
    }

    @Override
    public ComplianceScanRuleGroupResultResponse getComplianceRuleGroupByGroupId(String complianceRuleGroupId) {
        List<ComplianceScanResultResponse> complianceScanResponses = this.baseMapper.list(new QueryWrapper<ComplianceScanResultResponse>()
                .eq(StringUtils.isNotEmpty(complianceRuleGroupId),
                        ColumnNameUtil.getColumnName(ComplianceRule::getRuleGroupId, true), complianceRuleGroupId)
                .eq(ColumnNameUtil.getColumnName(ComplianceRule::getEnable, true), true), null);
        return toComplianceScanRuleGroupResponse(complianceScanResponses, complianceRuleGroupService.getById(complianceRuleGroupId).getName());
    }

    @Override
    public void saveOrUpdate(List<ComplianceScanResult> complianceScanResults) {
        for (ComplianceScanResult complianceScanResult : complianceScanResults) {
            saveOrUpdate(complianceScanResult,
                    new LambdaQueryWrapper<ComplianceScanResult>()
                            .eq(ComplianceScanResult::getResourceType, complianceScanResult.getResourceType())
                            .eq(ComplianceScanResult::getComplianceRuleId, complianceScanResult.getComplianceRuleId())
                            .eq(ComplianceScanResult::getCloudAccountId, complianceScanResult.getCloudAccountId()));
        }
    }

    @Override
    public void initComplianceScanResultService(ComplianceRule complianceRule) {
        List<ComplianceScanResult> complianceScanResults = cloudAccountService.list(new LambdaQueryWrapper<CloudAccount>()
                        .eq(CloudAccount::getPlatform, complianceRule.getPlatform()))
                .stream()
                .map(cloudAccount -> new ComplianceScanResult(
                        null, complianceRule.getId(),
                        complianceRule.getResourceType(),
                        cloudAccount.getId(),
                        0, 0, 0,
                        ComplianceStatus.COMPLIANCE, null,
                        null)).toList();
        saveOrUpdate(complianceScanResults);
    }


    /**
     * @param complianceScanResponses 规则组所有的规则
     * @param complianceRuleGroupName 合规规则组名称
     * @return 合规规则组扫描结果数据
     */
    private ComplianceScanRuleGroupResultResponse toComplianceScanRuleGroupResponse(List<ComplianceScanResultResponse> complianceScanResponses, String complianceRuleGroupName) {
        Map<ComplianceStatus, Map<RiskLevel, Long>> result = complianceScanResponses
                .stream()
                .collect(Collectors.groupingBy(ComplianceScanResultResponse::getScanStatus,
                        Collectors.groupingBy(ComplianceScanResultResponse::getRiskLevel,
                                Collectors.mapping(c -> 1L,
                                        Collectors.summingLong(Long::longValue)))));

        ComplianceScanRuleGroupResultResponse complianceScanRuleGroupResponse = new ComplianceScanRuleGroupResultResponse();
        complianceScanRuleGroupResponse.setRuleGroupName(complianceRuleGroupName);
        complianceScanRuleGroupResponse.setComplianceRuleCount(result.getOrDefault(ComplianceStatus.COMPLIANCE, Map.of()).values().stream().mapToLong(Long::longValue).sum());
        complianceScanRuleGroupResponse.setNotComplianceRuleCount(result.getOrDefault(ComplianceStatus.NOT_COMPLIANCE, Map.of()).values().stream().mapToLong(Long::longValue).sum());
        complianceScanRuleGroupResponse.setHigh(result.getOrDefault(ComplianceStatus.NOT_COMPLIANCE, Map.of()).getOrDefault(RiskLevel.HIGH, 0L));
        complianceScanRuleGroupResponse.setLow(result.getOrDefault(ComplianceStatus.NOT_COMPLIANCE, Map.of()).getOrDefault(RiskLevel.LOW, 0L));
        complianceScanRuleGroupResponse.setMiddle(result.getOrDefault(ComplianceStatus.NOT_COMPLIANCE, Map.of()).getOrDefault(RiskLevel.MIDDLE, 0L));
        return complianceScanRuleGroupResponse;
    }

    /**
     * 根据请求对象获取 查询条件
     *
     * @param request 请求对象
     * @return 查询条件对象
     */
    private QueryWrapper<ComplianceScanResultResponse> getQueryWrapper(ComplianceScanRequest request) {
        QueryWrapper<ComplianceScanResultResponse> wrapper = new QueryWrapper<ComplianceScanResultResponse>()
                .like(StringUtil.isNotEmpty(request.getComplianceRuleName()), ColumnNameUtil.getColumnName(ComplianceRule::getName, true), request.getComplianceRuleName())
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ColumnNameUtil.getColumnName(ComplianceRule::getResourceType, true), request.getResourceType())
                .eq(StringUtils.isNotEmpty(request.getComplianceRuleGroupId()), ColumnNameUtil.getColumnName(ComplianceRule::getRuleGroupId, true), request.getComplianceRuleGroupId())
                .eq(ColumnNameUtil.getColumnName(ComplianceRule::getEnable, true), true)
                .eq(StringUtils.isNotEmpty(request.getCloudAccountId()), ColumnNameUtil.getColumnName(ComplianceScanResult::getCloudAccountId, true), request.getCloudAccountId())
                .eq(StringUtils.isNotEmpty(request.getPlatform()), ColumnNameUtil.getColumnName(ComplianceRule::getPlatform, true), request.getPlatform())
                .eq(Objects.nonNull(request.getScanStatus()), ColumnNameUtil.getColumnName(ComplianceScanResult::getStatus, true), Objects.nonNull(request.getScanStatus()) ? request.getScanStatus().getCode() : -1);
        return wrapper;
    }
}
