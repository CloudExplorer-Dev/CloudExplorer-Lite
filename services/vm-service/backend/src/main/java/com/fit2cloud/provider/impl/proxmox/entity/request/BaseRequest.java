package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.platform.credential.impl.ProxmoxCredential;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/10  16:20}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class BaseRequest {
    /**
     * node节点
     */
    private String regionId;
    /**
     * 认证信息
     */
    private ProxmoxCredential credential;

    /**
     * 如果云管参数区域是region 那么就重写set函数去赋值
     *
     * @param region 区域
     */
    public void setRegion(String region) {
        this.regionId = region;
    }
}
