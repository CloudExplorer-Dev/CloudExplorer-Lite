package com.fit2cloud.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  12:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiSecurityComplianceCredential extends HuaweiBaseCredential {

    /**
     * 获取认证对象
     *
     * @return 认证对象
     */
    private ICredential getAuth() {
        try {
            return new BasicCredentials().withAk(getAk()).withSk(getSk());
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取EcsClient
     *
     * @param region 区域
     * @return EcsClient
     */
    public EcsClient getEcsClient(String region) {
        try {
            return EcsClient.newBuilder().withCredential(getAuth()).withRegion(EcsRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }
}
