package com.fit2cloud.provider.impl.aliyun.entity.request;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/20 12:43
 */
@Data
public class AliyunUpdateConfigRequest extends AliyunBaseRequest{
    private String zoneId;
    private String instanceUuid;
    private String currentInstanceType;
    private String newInstanceType;

}
