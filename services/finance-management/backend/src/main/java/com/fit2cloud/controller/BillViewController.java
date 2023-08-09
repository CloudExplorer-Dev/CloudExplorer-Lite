package com.fit2cloud.controller;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.constants.CalendarConstants;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.BillExpensesRequest;
import com.fit2cloud.controller.request.BillViewRequest;
import com.fit2cloud.controller.request.CurrencyRequest;
import com.fit2cloud.controller.request.HistoryTrendRequest;
import com.fit2cloud.controller.response.BillView;
import com.fit2cloud.controller.response.CurrencyResponse;
import com.fit2cloud.controller.response.ExpensesResponse;
import com.fit2cloud.controller.response.Trend;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import com.fit2cloud.provider.constants.CostFieldConstants;
import com.fit2cloud.service.BillViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/25  5:50 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@Tag(name = "账单总览相关接口")
@RequestMapping("/api/bill_view")
@Validated
public class BillViewController {
    @Resource
    private BillViewService billViewService;

    @GetMapping("/expenses/{type}/{value}")
    @Operation(summary = "获取账单花费,可以按月,按年", description = "获取账单花费")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<ExpensesResponse> getBillExpenses(@Pattern(regexp = "MONTH|YEAR", message = "类型,支持MONTH,YEAR") @Parameter(description = "类型,支持MONTH,YEAR") @PathVariable("type") String type,
                                                          @Parameter(description = "如果类型是MONTH yyyy-mm格式,YEAR yyyy") @PathVariable("value") String value,
                                                          @Validated BillExpensesRequest billExpensesRequest) {
        return ResultHolder.success(billViewService.getBillExpenses(CalendarConstants.valueOf(type), value, billExpensesRequest));
    }

    @GetMapping("/history_trend/{type}/{history_num}")
    @Operation(summary = "获取账单趋势", description = "获取账单趋势")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<List<Trend>> historyTrend(@PathVariable("type") @Pattern(regexp = "MONTH|YEAR", message = "类型,支持MONTH,YEAR") String type,
                                                  @PathVariable("history_num") Integer historyNum,
                                                  @Validated HistoryTrendRequest historyTrendRequest) {
        List<Trend> trends = billViewService.getTrend(CalendarConstants.valueOf(type), historyNum, historyTrendRequest);
        return ResultHolder.success(trends);
    }

    @GetMapping("/{ruleId}/{month}")
    @Operation(summary = "根据账单规则聚合账单", description = "根据账单规则聚合账单")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<Map<String, List<BillView>>> billViewByRule(@NotNull(message = "账单规则id不能为空") @CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, field = "id", message = "账单规则id不存在", exist = false) @Parameter(description = "账单规则id") @PathVariable("ruleId") String ruleId,
                                                                    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "月份格式必须为yyyy-mm") @Parameter(description = "月份") @PathVariable("month") String month,
                                                                    @Validated BillViewRequest request) {
        return ResultHolder.success(billViewService.billViewByRuleId(ruleId, month, request));
    }

    @GetMapping("/cloud_account/current_month")
    @Operation(summary = "获取当月云账号聚合账单", description = "获取当月云账号聚合账单")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<Map<String, List<BillView>>> currentMonthBillViewByCloudAccount() {
        return ResultHolder.success(billViewService.currentMonthBillViewByCloudAccount());
    }

    @GetMapping("/list_cost")
    @Operation(summary = "获取成本类型列表", description = "获取成本类型列表")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<List<DefaultKeyValue<String, String>>> listCost() {
        return ResultHolder.success(Arrays.stream(CostFieldConstants.values())
                .map(costFieldConstants -> new DefaultKeyValue<>(costFieldConstants.getMessage(), costFieldConstants.name()))
                .toList());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取币种列表", description = "获取币种列表")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<List<CurrencyResponse>> listCurrency() {
        return ResultHolder.success(billViewService.listCurrency());
    }

    @PutMapping("/currency")
    @Operation(summary = "修改币种汇率", description = "修改币种汇率")
    @PreAuthorize("@cepc.hasAnyCePermission('BILL_ViEW:READ')")
    public ResultHolder<Boolean> batchUpdateCurrency(@RequestBody ArrayList<CurrencyRequest> list) {
        Boolean result = billViewService.batchUpdateCurrency(list);
        return ResultHolder.success(result);
    }
}
