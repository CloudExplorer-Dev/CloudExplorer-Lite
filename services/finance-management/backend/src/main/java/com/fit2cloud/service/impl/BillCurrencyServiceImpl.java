package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.controller.response.CurrencyResponse;
import com.fit2cloud.dao.entity.BillCurrency;
import com.fit2cloud.dao.mapper.BillCurrencyMapper;
import com.fit2cloud.provider.constants.CurrencyConstants;
import com.fit2cloud.service.IBillCurrencyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BillCurrencyServiceImpl extends ServiceImpl<BillCurrencyMapper, BillCurrency> implements IBillCurrencyService {

    @Override
    public List<CurrencyResponse> listCurrency() {
        List<BillCurrency> diskCurrencyList = list();
        return Arrays.stream(CurrencyConstants.values())
                .map(currencyConstants -> diskCurrencyList.stream()
                        .filter(diskCurrency -> StringUtils.equals(diskCurrency.getCode(), currencyConstants.code()))
                        .findFirst()
                        .map(CurrencyResponse::new)
                        .orElse(new CurrencyResponse(currencyConstants)))
                .toList();
    }
}
