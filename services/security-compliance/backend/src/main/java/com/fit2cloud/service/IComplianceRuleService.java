package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.rule.ComplianceRuleRequest;
import com.fit2cloud.controller.request.rule.PageComplianceRuleRequest;
import com.fit2cloud.controller.response.rule.ComplianceRuleResponse;
import com.fit2cloud.controller.response.rule.ComplianceRuleSearchFieldResponse;
import com.fit2cloud.dao.entity.ComplianceRule;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IComplianceRuleService extends IService<ComplianceRule> {
    /**
     * 根据云平台和实例类型获取可过滤字段
     *
     * @param platform     云平台
     * @param resourceType 资源类型
     * @return 当前云平台下的资源类型的可过滤字段
     */
    List<ComplianceRuleSearchFieldResponse> listInstanceSearchField(String platform, String resourceType);

    /**
     * 插入合规规则
     *
     * @param complianceRuleRequest 合规规则
     * @return 合规规则
     */
    ComplianceRuleResponse save(ComplianceRuleRequest complianceRuleRequest);

    /**
     * 分页查询账单规则
     *
     * @param currentPage 当前页
     * @param limit       每页显示多少条
     * @param request     请求过滤对象
     * @return 合规规则
     */
    IPage<ComplianceRuleResponse> page(Integer currentPage, Integer limit, PageComplianceRuleRequest request);

    /**
     * 获取资源类型
     *
     * @return 支持的资源类型
     */
    List<DefaultKeyValue<String, String>> listResourceType();

    /**
     * 修改合规规则
     *
     * @param complianceRuleRequest 合规规则请求对象
     * @return 合规规则响应对象
     */
    ComplianceRuleResponse update(ComplianceRuleRequest complianceRuleRequest);
}
