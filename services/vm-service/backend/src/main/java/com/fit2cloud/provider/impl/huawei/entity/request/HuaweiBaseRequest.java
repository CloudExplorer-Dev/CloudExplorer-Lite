package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.common.constants.Language;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/26 09:59
 **/
@Data
public class HuaweiBaseRequest {

    private String credential;

    private String regionId;

    private Language language;
}
