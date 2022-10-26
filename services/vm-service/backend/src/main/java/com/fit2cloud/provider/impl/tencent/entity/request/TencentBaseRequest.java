package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.constants.Language;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/26 11:52
 **/
@Data
public class TencentBaseRequest {

    private String credential;

    private String regionId;

    private Language language;
}
