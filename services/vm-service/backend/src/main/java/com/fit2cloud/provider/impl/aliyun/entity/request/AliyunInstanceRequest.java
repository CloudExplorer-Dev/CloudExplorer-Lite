package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/14 11:25
 **/
@Data
public class AliyunInstanceRequest extends AliyunBaseRequest {

    private String uuId;

    /**
     * 强制执行
     */
    private Boolean force;

}
