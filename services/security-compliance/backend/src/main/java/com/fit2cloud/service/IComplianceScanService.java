package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanRuleGroupResponse;

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

    List<ComplianceScanResponse> scanComplianceByRuleId(String complianceRuleId);

    ComplianceScanRuleGroupResponse getRuleGroupCompliance(String complianceRuleGroupId);

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
}
