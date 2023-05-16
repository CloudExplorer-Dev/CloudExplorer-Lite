package com.fit2cloud.provider.impl.tencent.entity.response;

import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.BucketRefererConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Setter
@Getter
@NoArgsConstructor
public class BucketInstanceResponse extends Bucket {
    /**
     * 防盗链
     */
    private BucketRefererConfiguration referer;

    /**
     * 公共权限信息
     */
    private Map<String, Object> access;

    /**
     * 加密信息
     */
    private BucketEncryptionResponse.ServerSideEncryptionConfiguration encryption;

    public BucketInstanceResponse(Bucket bucket) {
        BeanUtils.copyProperties(bucket, this);
    }
}
