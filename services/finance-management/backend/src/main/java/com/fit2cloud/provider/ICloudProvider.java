package com.fit2cloud.provider;


import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.Bill;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:34 AM
 * @Version 1.0
 * @注释:
 */
public interface ICloudProvider extends IBaseCloudProvider {

    /**
     * 获取参数对象
     *
     * @return 参数对象
     */
    Class<? extends Bill> getParamsClass();

    List<CloudBill> syncBill(String request);

    /**
     * 获取桶中文件的所有月份
     *
     * @param request 请求对象
     * @return 桶中所有月份文件
     */
    List<String> listBucketFileMonth(String request);

    /**
     * 根据供应商获取对应云平台处理器
     *
     * @param platform 供应商
     * @return 处理器
     */
    static Class<? extends ICloudProvider> of(String platform) {
        return Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(platform)).findFirst().orElseThrow(() -> new RuntimeException("不支持的云平台")).getCloudProvider();
    }

    /**
     * 当前云平台是否支持
     *
     * @param platform 云平台
     * @return 是否支持
     */
    static boolean support(String platform) {
        return Arrays.stream(ProviderConstants.values()).anyMatch(providerConstants -> providerConstants.name().equals(platform));

    }
}
