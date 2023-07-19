package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.constants.Language;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/7  10:25}
 * {@code @Version 1.0}
 * {@code @注释: }
 */

public class ProxmoxBaseRequest {
    /**
     * 认证对象
     */
    private String credential;

    /**
     * 区域id
     */
    private String regionId;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * 语言
     */
    private Language language;

    /**
     * 如果云管参数区域是region 那么就重写set函数去赋值
     *
     * @param region 区域
     */
    public void setRegion(String region) {
        this.regionId = region;
    }


}
