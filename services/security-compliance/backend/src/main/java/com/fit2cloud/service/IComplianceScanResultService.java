package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanRuleGroupResultResponse;
import com.fit2cloud.dao.entity.ComplianceScanResult;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 */
public interface IComplianceScanResultService extends IService<ComplianceScanResult> {

    /**
     * 查询所有
     *
     * @param request 请求对象
     * @return 合规数据
     */
    List<ComplianceScanResultResponse> list(ComplianceScanRequest request);

    /**
     * 分页查询
     *
     * @param currentPage 当前页
     * @param limit       每页显示多少条
     * @param request     请求对象
     * @return 分页数据
     */
    IPage<ComplianceScanResultResponse> page(Integer currentPage, Integer limit, ComplianceScanRequest request);

    /**
     * 获取规则组扫描结果
     *
     * @param complianceRuleGroupId 合规规则id
     * @return 规则组扫描id
     */
    ComplianceScanRuleGroupResultResponse getComplianceRuleGroupByGroupId(String complianceRuleGroupId);

    /**
     * 插入或者更新结果数据
     *
     * @param list 数据
     */
    void saveOrUpdate(List<ComplianceScanResult> list);

}
