package com.fit2cloud.provider.impl.vsphere.entity.credential;

import com.fit2cloud.common.constants.Language;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.fit2cloud.provider.impl.vsphere.util.VsphereVmClient;

import java.util.Optional;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  10:31}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class VsphereComplianceCredential extends VsphereCredential {
    /**
     * 获取vc客户端
     *
     * @param regionId 区域id
     * @param lang     语言
     * @return vc客户端
     */
    public VsphereVmClient getVsphereVmClient(String regionId, Language lang) {
        return new VsphereVmClient(this, regionId, Optional.ofNullable(lang).orElse(Language.zh_CN));
    }

    /**
     * 获取vc客户端
     *
     * @param regionId 区域id
     * @return vc客户端
     */
    public VsphereVmClient getVsphereVmClient(String regionId) {
        return getVsphereVmClient(regionId, Language.zh_CN);
    }
}
