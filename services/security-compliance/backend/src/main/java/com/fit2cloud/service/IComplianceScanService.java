package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanRuleGroupResponse;
import com.fit2cloud.response.JobRecordResourceResponse;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface IComplianceScanService {

    /**
     * @param request 请求对象
     * @return 合规响应对象
     */
    List<ComplianceScanResponse> list(ComplianceScanRequest request);


    /**
     * 扫描合规规则 根据合规规则id
     *
     * @param complianceRuleId 合规规则id
     * @return 合规规则扫描结果对象
     */
    ComplianceScanResponse scanComplianceByRuleId(String complianceRuleId);

    /**
     * 扫描合规规则组 根据合规规则组id
     *
     * @param complianceRuleGroupId 合规规则组id
     * @return 合规规则组扫描结果
     */
    ComplianceScanRuleGroupResponse scanComplianceRuleGroupByGroupId(String complianceRuleGroupId);

    /**
     * 分页查询合规规则扫描结果
     *
     * @param currentPage 当前页
     * @param limit       每页展示多少条
     * @param request     请求查询参数
     * @return 分页合规规则扫描结果数据
     */
    Page<ComplianceScanResponse> page(Integer currentPage, Integer limit, ComplianceScanRequest request);

    /**
     * 分页查询资源
     *
     * @param currentPage               当前页
     * @param limit                     每页多少条
     * @param complianceResourceRequest 请求对象
     * @return 资源对象
     */
    Page<ComplianceResourceResponse> pageResource(String complianceRuleId, Integer currentPage, Integer limit, ComplianceResourceRequest complianceResourceRequest);

    /**
     * 修改合规规则组缓存结果
     *
     * @param complianceRuleGroupId 合规规则组id
     * @return 合规规则组扫描结果对象
     */
    ComplianceScanRuleGroupResponse updateCacheScanComplianceRuleGroupByGroupId(String complianceRuleGroupId);

    /**
     * 根据资源类型修改缓存数据
     *
     * @param resourceTypeConstants 资源类型
     * @return 缓存数据
     */
    List<ComplianceScanResponse> updateCacheScanComplianceByInstanceType(ResourceTypeConstants resourceTypeConstants);

    /**
     * 修改扫描规则缓存结果
     *
     * @param complianceRuleId 合规规则id
     */
    List<ComplianceScanResponse> updateCacheScanComplianceByRuleId(String complianceRuleId);

    /**
     * 修改扫描规则缓存结果 粒度为云账户
     *
     * @param complianceRuleId 规则id
     * @param cloudAccountId   云账户id
     * @return 合规规则扫描结果对象
     */
    ComplianceScanResponse updateCacheScanCompliance(String complianceRuleId, String cloudAccountId);


    /**
     * 获取支持的云账号 以及云账号可同步的资源
     *
     * @return 云账号以及对应同步资源类型
     */
    List<SupportCloudAccountResourceResponse> listSupportCloudAccountResource();

    /**
     * 获取扫描任务详情
     *
     * @return 扫描任务
     */
    List<JobRecordResourceResponse> listJobRecord();
}
