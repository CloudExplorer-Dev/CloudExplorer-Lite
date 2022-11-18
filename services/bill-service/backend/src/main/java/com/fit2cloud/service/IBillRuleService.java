package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.request.BillRuleRequest;
import com.fit2cloud.dao.entity.BillRule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBillRuleService extends IService<BillRule> {
    /**
     * 分页查询账单规则
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示大小
     * @param request     账单规则请求参数
     * @return 分页数据
     */
    Page<BillRule> page(Integer currentPage, Integer pageSize, BillRuleRequest request);

}
