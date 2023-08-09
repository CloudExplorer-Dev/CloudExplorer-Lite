package com.fit2cloud.controller.response;

import com.fit2cloud.dao.entity.BillCurrency;
import com.fit2cloud.provider.constants.CurrencyParent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/8  16:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

@Setter
public class CurrencyResponse implements CurrencyParent {

    @Schema(description = "币种")
    private String code;


    @Schema(description = "提示")
    private String message;


    @Schema(description = "符号")
    private String symbol;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "汇率")
    private Double exchangeRate;

    public CurrencyResponse(BillCurrency currency) {
        BeanUtils.copyProperties(currency, this);
    }

    public CurrencyResponse(CurrencyParent currencyParent) {
        this.code = currencyParent.code();
        this.unit = currencyParent.unit();
        this.symbol = currencyParent.symbol();
        this.exchangeRate = currencyParent.getExchangeRate().doubleValue();
        this.message = currencyParent.getMessage();
    }

    @Override
    public String symbol() {
        return symbol;
    }

    @Override
    public String unit() {
        return unit;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public BigDecimal getExchangeRate() {
        return BigDecimal.valueOf(exchangeRate);
    }

    public String getCode() {
        return code;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUnit() {
        return unit;
    }
}
