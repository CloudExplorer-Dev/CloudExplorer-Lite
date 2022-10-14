package com.fit2cloud.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.huaweicloud.sdk.bss.v2.BssClient;
import com.huaweicloud.sdk.bss.v2.region.BssRegion;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:26 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiBillCredential extends HuaweiCredential implements Credential {

    public BssClient getBssClient() {
        ICredential auth = new GlobalCredentials().withAk(getAk()).withSk(getSk());
        return BssClient.newBuilder().withCredential(auth).withRegion(BssRegion.valueOf("cn-north-1")).build();
    }
}
