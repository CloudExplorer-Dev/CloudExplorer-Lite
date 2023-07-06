package com.fit2cloud.vm.entity.request;

import com.fit2cloud.common.constants.Language;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/27 14:38
 **/
@Data
public class BaseRequest {

    private String credential;

    private String regionId;

    private Language language;
}
