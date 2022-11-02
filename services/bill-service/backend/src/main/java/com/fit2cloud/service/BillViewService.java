package com.fit2cloud.service;

import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/25  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface BillViewService {

    /**
     * 获取费用信息
     *
     * @param type                类型 MONTH月份 YEAR 年
     * @param value               yyyy-MM   yyyy
     * @param billExpensesRequest 请求过滤参数
     * @return 聚合数据数据
     */
    BigDecimal getBillExpenses(String type, String value, BillExpensesRequest billExpensesRequest);

    /**
     * @param type                类型
     * @param historyNum          历史多少个
     * @param historyTrendRequest 请求过滤参数
     * @return 历史趋势
     */
    List<Trend> getTrend(String type, Integer historyNum, HistoryTrendRequest historyTrendRequest);

    /**
     * 获取账单分账信息
     *
     * @param ruleId 账单谷子饿id
     * @param month  月份
     * @return 账单xx
     */
    Map<String, List<BillView>> billViewByRuleId(String ruleId, String month);
}
