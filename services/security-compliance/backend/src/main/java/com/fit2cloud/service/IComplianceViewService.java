package com.fit2cloud.service;

import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;
import com.fit2cloud.controller.response.view.ComplianceViewRuleCountResponse;
import com.fit2cloud.dao.constants.RiskLevel;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface IComplianceViewService {


    /**
     * 资源分组聚合数据
     *
     * @param request 请求对象
     * @return 资源分组数据
     */
    List<ComplianceViewGroupResponse> resourceGroup(ComplianceGroupRequest request);

    /**
     * 获取资源同居数据
     *
     * @param request 请求对象
     * @return 资源统计数据
     */
    ComplianceViewCountResponse resourceCount(ComplianceCountRequest request);


    /**
     * 获取规则聚合数据
     *
     * @param request 请求过滤对象
     * @return 规则聚合数据
     */
    Map<RiskLevel, ComplianceViewRuleCountResponse> ruleCount(ComplianceCountRequest request);
}
