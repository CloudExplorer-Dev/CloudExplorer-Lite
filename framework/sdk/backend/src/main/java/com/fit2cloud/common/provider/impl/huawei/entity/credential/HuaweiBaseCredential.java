package com.fit2cloud.common.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.region.BssRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;

public class HuaweiBaseCredential extends HuaweiCredential implements Credential {
    /**
     * 获取认证对象
     *
     * @return 认证对象
     */
    private com.huaweicloud.sdk.core.auth.ICredential getAuth() {
        try {
            return new BasicCredentials().withAk(getAk()).withSk(getSk());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取认证对象
     *
     * @return 认证对象
     */
    private com.huaweicloud.sdk.core.auth.ICredential getGlobalAuth() {
        try {
            return new GlobalCredentials().withAk(getAk()).withSk(getSk());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取 BssClient
     * @param region
     * @return
     */
    public BssClient getBssClient(String region) {
        try {
            return BssClient.newBuilder().withCredential(getGlobalAuth()).withRegion(BssRegion.valueOf(region)).build();
        } catch (Exception e) {
            throw e;
        }
    }
}
