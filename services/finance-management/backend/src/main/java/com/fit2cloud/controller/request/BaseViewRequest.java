package com.fit2cloud.controller.request;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeField;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeFieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.fit2cloud.provider.constants.CostFieldConstants;
import com.fit2cloud.provider.constants.CurrencyConstants;
import com.fit2cloud.provider.constants.CurrencyParent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/7  17:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class BaseViewRequest {

    @Schema(description = "云账号id")
    protected String cloudAccountId;

    @Schema(description = "成本字段", example = "officialAmount|payableAmount|cashAmount|couponAmount")
    @Pattern(regexp = "officialAmount|payableAmount|cashAmount|couponAmount")
    protected String costField;

    @Schema(description = "展示币种", example = "USD|JPY|CNY")
    protected CurrencyConstants currency;


    /**
     * 将当前对象转换为查询对象
     *
     * @return 查询条件
     */
    public List<Query> toQuery() {
        List<Query> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.cloudAccountId)) {
            result.add(new Query.Builder()
                    .match(new MatchQuery.Builder()
                            .query(this.cloudAccountId)
                            .field("cloudAccountId").build()
                    ).build());
        }
        return result;
    }

    /**
     * 生成运行时字段
     *
     * @param currencyParents 币种类型
     * @return 运行时字段
     */
    public Map<String, RuntimeField> toRuntimeMappings(List<? extends CurrencyParent> currencyParents) {
        return toRuntimeMappings(this.costField, currencyParents, this.currency);
    }

    /**
     * 生成运行时字段
     *
     * @return 运行时字段
     */
    public Map<String, RuntimeField> toRuntimeMappings() {
        return toRuntimeMappings(null, null, null);
    }

    /**
     * 转换为运行时字段
     *
     * @param costField       账单字段
     * @param currencyParents 当前支持币种以及汇率
     * @param currency        显示币种
     * @return 运行时字段
     */
    public Map<String, RuntimeField> toRuntimeMappings(String costField,
                                                       List<? extends CurrencyParent> currencyParents,
                                                       CurrencyConstants currency) {
        String script = "  double price = doc[params.field].value;\n" +
                "        // 将价钱统一转换为人民币\n" +
                "        String currency= doc['currency'].value;\n" +
                "        // 再讲人民币统一转换为需要显示的字段\n" +
                "        String viewCurrency=params['currency'];\n" +
                "        if(price<0){\n" +
                "           price = Math.abs(price);\n" +
                "           double result= BigDecimal.valueOf(price)\n" +
                "             .divide(BigDecimal.valueOf(params[currency]),MathContext.DECIMAL32)\n" +
                "             .multiply(BigDecimal.valueOf(params[viewCurrency]))\n" +
                "             .multiply(BigDecimal.valueOf(-1)).setScale(32,RoundingMode.CEILING).doubleValue();\n" +
                "           emit(result);\n" +
                "        }else{\n" +
                "           double result= BigDecimal.valueOf(price)\n" +
                "           .divide(BigDecimal.valueOf(params[currency]),MathContext.DECIMAL32)\n" +
                "           .multiply(BigDecimal.valueOf(params[viewCurrency])).setScale(32,RoundingMode.CEILING)\n" +
                "           .doubleValue();\n" +
                "        emit(result);\n" +
                "        }";
        Map<String, JsonData> params = new HashMap<>();
        params.put("field", JsonData.of(CostFieldConstants.valueOf(StringUtils.isEmpty(costField) ? "payableAmount" : costField).getField()));
        for (CurrencyParent currencyParent : CollectionUtils.isEmpty(currencyParents) ? Arrays.stream(CurrencyConstants.values()).toList() : currencyParents) {
            params.put(currencyParent.code(), JsonData.of(currencyParent.getExchangeRate().doubleValue()));
        }
        params.put("currency", JsonData.of(Objects.isNull(currency) ? CurrencyConstants.CNY.code() : currency.code()));

        RuntimeField runtimeField = new RuntimeField
                .Builder()
                .type(RuntimeFieldType.Double)
                .script(new Script.Builder()
                        .inline(new InlineScript.Builder()
                                .source(script)
                                .params(params)
                                .build())
                        .build())
                .build();
        return Map.of("realTotalCost", runtimeField);
    }
}
