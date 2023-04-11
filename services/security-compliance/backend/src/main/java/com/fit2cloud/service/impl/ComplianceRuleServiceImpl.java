package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.page.PageImpl;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.controller.request.rule.ComplianceRuleRequest;
import com.fit2cloud.controller.request.rule.PageComplianceRuleRequest;
import com.fit2cloud.controller.response.compliance_scan.SupportPlatformResourceResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleSearchFieldResponse;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;
import com.fit2cloud.dao.entity.ComplianceScanResourceResult;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.fit2cloud.dao.mapper.ComplianceRuleMapper;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    private IComplianceRuleInsuranceStatuteMappingService complianceRuleInsuranceStatuteMappingService;
    @Resource
    private IComplianceScanResultService complianceScanResultService;
    @Resource
    private IComplianceScanResourceResultService complianceScanResourceResultService;

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
    @Transactional(rollbackFor = Exception.class)
    public ComplianceRuleResponse save(ComplianceRuleRequest complianceRuleRequest) {
        ComplianceRule complianceRule = new ComplianceRule();
        BeanUtils.copyProperties(complianceRuleRequest, complianceRule);
        complianceRule.setEnable(true);
        save(complianceRule);
        complianceRuleInsuranceStatuteMappingService.save(complianceRule.getId(), complianceRuleRequest.getInsuranceStatuteIds());
        ComplianceRuleResponse complianceRuleResponse = new ComplianceRuleResponse();
        BeanUtils.copyProperties(complianceRule, complianceRuleResponse);
        complianceScanResultService.initComplianceScanResultService(complianceRule);
        return complianceRuleResponse;
    }

    @Override
    public IPage<ComplianceRuleResponse> page(Integer currentPage, Integer limit, PageComplianceRuleRequest request) {
        Page<ComplianceRuleResponse> page = PageImpl.of(currentPage, limit, ComplianceRuleResponse.class, Objects.isNull(request.getOrder()) ? new OrderItem() : request.getOrder());
        return this.baseMapper.pageRule(page, getWrapper(request));
    }

    public Wrapper<ComplianceRule> getWrapper(PageComplianceRuleRequest request) {
        return new QueryWrapper<ComplianceRule>()
                .like(StringUtils.isNotEmpty(request.getName()), ColumnNameUtil.getColumnName(ComplianceRule::getName, "cr"), request.getName())
                .eq(StringUtils.isNotEmpty(request.getPlatform()), ColumnNameUtil.getColumnName(ComplianceRule::getPlatform, "cr"), request.getPlatform())
                .like(StringUtils.isNotEmpty(request.getDescription()), ColumnNameUtil.getColumnName(ComplianceRule::getDescription, "cr"), request.getDescription())
                .eq(StringUtils.isNotEmpty(request.getRiskLevel()), ColumnNameUtil.getColumnName(ComplianceRule::getRiskLevel, "cr"), StringUtils.isNotEmpty(request.getRiskLevel()) ? RiskLevel.valueOf(request.getRiskLevel()) : null)
                .like(StringUtils.isNotEmpty(request.getRuleGroupName()), ColumnNameUtil.getColumnName(ComplianceRuleGroup::getName, "crg"), request.getRuleGroupName())
                .eq(StringUtils.isNotEmpty(request.getResourceType()), ColumnNameUtil.getColumnName(ComplianceRule::getResourceType, "cr"), request.getResourceType())
                .eq(StringUtils.isNotEmpty(request.getRuleGroupId()), ColumnNameUtil.getColumnName(ComplianceRule::getRuleGroupId, "cr"), request.getRuleGroupId())
                .eq(Objects.nonNull(request.getEnable()), ColumnNameUtil.getColumnName(ComplianceRule::getEnable, "cr"), request.getEnable());
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
        if (CollectionUtils.isNotEmpty(complianceRuleRequest.getInsuranceStatuteIds())) {
            complianceRuleInsuranceStatuteMappingService.update(complianceRule.getId(), complianceRuleRequest.getInsuranceStatuteIds());
        }
        ComplianceRuleResponse complianceRuleResponse = new ComplianceRuleResponse();
        BeanUtils.copyProperties(complianceRule, complianceRuleResponse);
        IComplianceScanService complianceScanService = SpringUtil.getBean(IComplianceScanService.class);
        complianceScanService.scanComplianceOrSave(complianceRule);
        return complianceRuleResponse;
    }

    @Override
    public void remove(String id) {
        removeById(id);
        complianceScanResourceResultService.remove(new LambdaQueryWrapper<ComplianceScanResourceResult>()
                .eq(ComplianceScanResourceResult::getComplianceRuleId, id));
        complianceScanResultService.remove(new LambdaQueryWrapper<ComplianceScanResult>()
                .eq(ComplianceScanResult::getComplianceRuleId, id));
    }

    @Override
    public List<SupportPlatformResourceResponse> listSupportPlatformResource() {
        return Arrays.stream(ProviderConstants.values()).map(platform -> {
            List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> exec = CommonUtil.exec(ICloudProvider.of(platform.name()), ICloudProvider::getResourceSyncDimensionConstants);
            List<DefaultKeyValue<String, String>> resourceTypes = exec.stream().map(resourceTypeConstants -> new DefaultKeyValue<>(resourceTypeConstants.getKey().getMessage(), resourceTypeConstants.getKey().name())).toList();
            SupportPlatformResourceResponse supportPlatformResourceResponse = new SupportPlatformResourceResponse();
            supportPlatformResourceResponse.setPlatform(platform.name());
            supportPlatformResourceResponse.setResourceTypes(resourceTypes);
            return supportPlatformResourceResponse;
        }).toList();

    }
}
