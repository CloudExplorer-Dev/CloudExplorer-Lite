package com.fit2cloud.provider.impl.aliyun.entity.response;

import com.aliyun.sdk.service.oss20190517.models.Bucket;
import com.aliyun.sdk.service.oss20190517.models.GetBucketAclResponseBody;
import com.aliyun.sdk.service.oss20190517.models.GetBucketEncryptionResponseBody;
import com.aliyun.sdk.service.oss20190517.models.GetBucketRefererResponseBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:17}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class BucketInstanceResponse {

    private String creationDate;
    private String extranetEndpoint;
    private String intranetEndpoint;
    private String location;
    private String name;
    private String region;
    private String storageClass;
    /**
     * acl相关信息
     */
    private GetBucketAclResponseBody acl;

    /**
     * 防盗链
     */
    private GetBucketRefererResponseBody referer;

    /**
     * 加密信息
     */
    private GetBucketEncryptionResponseBody encryption;

    public BucketInstanceResponse(Bucket bucket) {
        BeanUtils.copyProperties(bucket, this);
    }
}
