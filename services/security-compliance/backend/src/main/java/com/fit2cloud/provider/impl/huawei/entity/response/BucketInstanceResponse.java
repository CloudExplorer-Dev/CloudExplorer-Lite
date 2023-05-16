package com.fit2cloud.provider.impl.huawei.entity.response;

import com.obs.services.model.AccessControlList;
import com.obs.services.model.BucketEncryption;
import com.obs.services.model.ObsBucket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class BucketInstanceResponse extends ObsBucket {

    /**
     * 访问控制相关信息
     */
    private AccessControlList acl;

    /**
     * 加密相关信息
     */
    private BucketEncryption encryption;

    public BucketInstanceResponse(ObsBucket bucket) {
        BeanUtils.copyProperties(bucket, this);
    }
}
