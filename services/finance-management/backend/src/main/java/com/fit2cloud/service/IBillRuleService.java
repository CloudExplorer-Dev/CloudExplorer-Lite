package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.AddBillRuleRequest;
import com.fit2cloud.controller.request.BillRuleRequest;
import com.fit2cloud.controller.request.UpdateBillRuleRequest;
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

    /**
     * 新增一个账单规则
     *
     * @param request 请求对象
     * @return 添加的账单规则
     */
    BillRule add(AddBillRuleRequest request);

    /**
     * 修改账单规则
     *
     * @param request 账单规则对象
     * @return 修改后的账单规则
     */
    BillRule update(UpdateBillRuleRequest request);
}
