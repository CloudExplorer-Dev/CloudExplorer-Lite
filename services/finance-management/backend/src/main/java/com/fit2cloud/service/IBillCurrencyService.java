package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.response.CurrencyResponse;
import com.fit2cloud.dao.entity.BillCurrency;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBillCurrencyService extends IService<BillCurrency> {
    List<CurrencyResponse> listCurrency();
}
