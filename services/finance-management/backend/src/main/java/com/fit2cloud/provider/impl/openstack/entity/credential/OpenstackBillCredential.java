package com.fit2cloud.provider.impl.openstack.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.OpenStackCredential;
import com.fit2cloud.provider.client.PrivateLocalCloudClient;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/9  14:05}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OpenstackBillCredential extends OpenStackCredential implements Credential {
    /**
     * 获取客户端
     *
     * @return 客户端
     */
    public PrivateLocalCloudClient getClient() {
        return new PrivateLocalCloudClient("admin");
    }
}
