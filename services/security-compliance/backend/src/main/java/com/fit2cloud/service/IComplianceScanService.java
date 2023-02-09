package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportPlatformResourceResponse;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.entity.ComplianceScanResult;
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
     * 扫描一个规则
     *
     * @param complianceRuleId 规则id
     * @return 规则结果
     */
    List<ComplianceScanResult> scanCompliance(String complianceRuleId);

    /**
     * 扫描一个规则
     *
     * @param complianceRule 规则对象
     * @return 规则结果
     */
    List<ComplianceScanResult> scanCompliance(ComplianceRule complianceRule);

    /**
     * 扫描所有规则
     *
     * @return 扫描结果
     */
    List<ComplianceScanResult> scanCompliance();


    /**
     * 扫描一个资源类型
     *
     * @param resourceTypeConstants 资源类型
     * @return 扫描结果
     */
    List<ComplianceScanResult> scanCompliance(ResourceTypeConstants resourceTypeConstants);

    /**
     * 扫描指定资源类型和云账号的所有资源
     *
     * @param resourceTypeConstants 资源类型
     * @param cloudAccountId        云账号id
     * @return 扫描结果
     */
    List<ComplianceScanResult> scanCompliance(ResourceTypeConstants resourceTypeConstants, String cloudAccountId);


    /**
     * 扫描规则并且入库
     */
    void scanComplianceOrSave();

    /**
     * 扫描规则并且入库
     *
     * @param complianceRuleId 规则id
     */
    void scanComplianceOrSave(String complianceRuleId);

    /**
     * 扫描规则并且入库
     *
     * @param complianceRule 规则对象
     */
    void scanComplianceOrSave(ComplianceRule complianceRule);

    /**
     * 扫描规则并且入库
     *
     * @param resourceTypeConstants 资源类型
     * @param cloudAccountId        云账号id
     */
    void scanComplianceOrSave(ResourceTypeConstants resourceTypeConstants, String cloudAccountId);

    /**
     * 扫描资源并且入库
     *
     * @param resourceTypeConstants 资源类型
     */
    void scanComplianceOrSave(ResourceTypeConstants resourceTypeConstants);

    /**
     * 分页查询扫描资源
     *
     * @param currentPage               当前页
     * @param limit                     每页多少条
     * @param complianceResourceRequest 请求对象
     * @return 资源对象
     */
    Page<ComplianceResourceResponse> pageResource(String complianceRuleId, Integer currentPage, Integer limit, ComplianceResourceRequest complianceResourceRequest);


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

    /**
     * 获取支持的云平台 资源数据
     *
     * @return 云平台以及对应的资源数据
     */
    List<SupportPlatformResourceResponse> listSupportPlatformResource();

}
