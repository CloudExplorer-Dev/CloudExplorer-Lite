package com.fit2cloud.provider.impl.huawei.entity.response;

import com.huaweicloud.sdk.cbr.v1.model.Policy;
import com.huaweicloud.sdk.cbr.v1.model.Vault;
import com.huaweicloud.sdk.evs.v2.model.VolumeDetail;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  11:27}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class DiskInstanceResponse extends VolumeDetail {
    /**
     * 存储库
     */
    private Vault vault;
    /**
     * 存储策略
     */
    private Policy policy;
}
