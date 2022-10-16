package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.fit2cloud.common.constants.Language;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/14 16:20
 **/
@Data
public class AliyunBaseRequest {

    private String credential;

    private String regionId;

    private Language language;

}
