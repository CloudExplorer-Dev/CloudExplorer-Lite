package com.fit2cloud.service;

import com.fit2cloud.constants.CalendarConstants;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.ExpensesResponse;
import com.fit2cloud.controller.response.Trend;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/4/3  10:29}
 * {@code @Version 1.0}
 * {@code @注释:  用于检索数据}
 */
public interface BillSearchService {
    /**
     * 获取账单趋势
     *
     * @param calendarConstants 日期类型
     * @param historyNum        历史n
     * @return 趋势对象
     */
    List<Trend> trend(CalendarConstants calendarConstants, Integer historyNum);

    /**
     * 获取账单趋势 携带过滤条件 注意: 当前携带过滤条件函数不对数据进行缓存
     *
     * @param calendarConstants 日期类型
     * @param historyNum        历史n
     * @param request           过滤对象
     * @return 趋势对象
     */
    List<Trend> trend(CalendarConstants calendarConstants, Integer historyNum, HistoryTrendRequest request);

    /**
     * 获取账单花销 指定的月/年 指定的上月/年
     *
     * @param calendarConstants 日期类型
     * @param value             指定日期
     * @return 账单花销
     */
    ExpensesResponse expenses(CalendarConstants calendarConstants, String value);

    /**
     * 获取账单花销 指定的月/年 指定的上月/年
     *
     * @param calendarConstants 日期类型
     * @param value             指定时间
     * @param request           过滤参数
     * @return 账单花销
     */
    ExpensesResponse expenses(CalendarConstants calendarConstants, String value, BillExpensesRequest request);
}
