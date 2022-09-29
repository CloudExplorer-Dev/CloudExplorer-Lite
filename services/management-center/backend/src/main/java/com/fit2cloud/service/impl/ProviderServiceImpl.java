package com.fit2cloud.service.impl;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.ProviderConstants;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.service.IProviderService;
import io.reactivex.rxjava3.functions.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Author: LiuDi
 * Date: 2022/9/28 4:16 PM
 */
@Service
public class ProviderServiceImpl implements IProviderService {
    private static Logger logger = LoggerFactory.getLogger(ProviderServiceImpl.class);

    @Resource
    private IBaseCloudAccountService cloudAccountService;

    public F2CBalance getAccountBalance(String cloudAccountId) {
        F2CBalance balance = null;
        try {
            CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
            Class<? extends IBaseCloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
            balance = exec(cloudProvider, getParams(cloudAccount.getCredential()), IBaseCloudProvider::getAccountBalance);
        } catch (Exception e) {
            logger.error("Error:getAccountBalance!" + e.getMessage(), e);
        }
        return balance;
    }


    /**
     * 执行函数
     *
     * @param providerClass 执行处理器
     * @param req           请求参数
     * @param exec          执行函数
     * @param <T>           执行函数返回对象
     * @return 执行函数返回对象泛型
     */
    private <T> T exec(Class<? extends IBaseCloudProvider> providerClass, String req, BiFunction<IBaseCloudProvider, String, T> exec) {
        try {
            IBaseCloudProvider iCloudProvider = providerClass.getConstructor().newInstance();
            return exec.apply(iCloudProvider, req);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取函数执行参数
     *
     * @param credential 认证信息
     * @return json参数
     */
    private String getParams(String credential) {
        HashMap<String, String> params = new HashMap<>();
        params.put("credential", credential);
        return JsonUtil.toJSONString(params);
    }
}
