package com.fit2cloud.provider.impl.proxmox.request;

import com.fit2cloud.provider.client.PrivateLocalCloudClient;
import com.fit2cloud.provider.impl.proxmox.credential.ProxmoxBillCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/8  17:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class SyncBillRequest extends PrivateLocalCloudClient.BillRequest {

    /**
     * 认证对象
     */
    private ProxmoxBillCredential credential;

}
