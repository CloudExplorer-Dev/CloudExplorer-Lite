package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.service.BillViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.intellij.lang.annotations.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/25  5:50 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Api("账单总览相关接口")
@RequestMapping("/api/bill_view")
public class BillViewController {
    @Resource
    private BillViewService billViewService;

    @GetMapping("/expenses/{type}/{value}")
    @ApiOperation(value = "获取账单花费,可以按月,按年", notes = "获取账单花费")
    public ResultHolder<BigDecimal> getBillExpenses(@Pattern("MONTH|YEAR") @ApiParam("类型,支持MONTH,YEAR") @PathVariable("type") String type,
                                                    @ApiParam("如果类型是MONTH yyyy-mm格式,YEAR yyyy") @PathVariable("value") String value,
                                                    BillExpensesRequest billExpensesRequest) {
        return ResultHolder.success(billViewService.getBillExpenses(type, value, billExpensesRequest));
    }

    @GetMapping("/history_trend/{type}/{history_num}")
    @ApiOperation(value = "获取账单趋势", notes = "获取账单趋势")
    public ResultHolder<List<Trend>> historyTrend(@PathVariable("type") @Pattern("MONTH|YEAR") String type,
                                                  @PathVariable("history_num") Integer historyNum,
                                                  HistoryTrendRequest historyTrendRequest) {
        List<Trend> trends = billViewService.getTrend(type, historyNum, historyTrendRequest);
        return ResultHolder.success(trends);
    }

    @GetMapping("/{ruleId}/{month}")
    public ResultHolder<Map<String, List<BillView>>> billViewByRule(@ApiParam("账单规则id") @PathVariable("ruleId") String ruleId,
                                                                    @ApiParam("月份") @PathVariable("month") String month) {
        return ResultHolder.success(billViewService.billViewByRuleId(ruleId, month));
    }


}
