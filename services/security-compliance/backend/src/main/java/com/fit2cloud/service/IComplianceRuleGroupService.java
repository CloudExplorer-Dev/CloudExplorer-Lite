package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.rule_group.ComplianceRuleGroupRequest;
import com.fit2cloud.controller.request.rule_group.PageComplianceRuleGroupRequest;
import com.fit2cloud.controller.response.rule_group.ComplianceRuleGroupResponse;
import com.fit2cloud.dao.entity.ComplianceRuleGroup;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IComplianceRuleGroupService extends IService<ComplianceRuleGroup> {
    /**
     * 分页查询规则组
     *
     * @param currentPage 当前页
     * @param limit       每页展示多少条
     * @param request     过滤参数
     * @return 合规规则组
     */
    Page<ComplianceRuleGroupResponse> page(Integer currentPage, Integer limit, PageComplianceRuleGroupRequest request);

    /**
     * 插入规则组
     *
     * @param request 合规规则组
     * @return 合规规则组
     */
    ComplianceRuleGroupResponse save(ComplianceRuleGroupRequest request);

    /**
     * 删除规则组根据规则组id
     *
     * @param complianceRuleGroupId 规则组id
     * @return 是否删除
     */
    Boolean deleteById(String complianceRuleGroupId);

    /**
     * 修改规则组
     *
     * @param request 规则组对象
     * @return 合规规则组
     */
    ComplianceRuleGroupResponse update(ComplianceRuleGroupRequest request);
}
