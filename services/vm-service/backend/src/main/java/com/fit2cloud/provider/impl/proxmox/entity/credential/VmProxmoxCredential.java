package com.fit2cloud.provider.impl.proxmox.entity.credential;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.impl.ProxmoxCredential;
import com.fit2cloud.common.provider.impl.proxmox.client.PveClient;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/7  14:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class VmProxmoxCredential extends ProxmoxCredential {
    public PveClient getClient() {
        PveClient pveClient = new PveClient(this.getIp(), this.getPort());
        if (pveClient.login(this.getUserName(), this.getPassword(), this.getRealm())) {
            return pveClient;
        }
        throw new Fit2cloudException(500, "认证失败");
    }
}
