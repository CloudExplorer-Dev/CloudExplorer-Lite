package com.fit2cloud.provider.impl.tencent.entity.response;

import com.tencentcloudapi.cam.v20190116.models.DescribeSafeAuthFlagCollResponse;
import com.tencentcloudapi.cam.v20190116.models.SubAccountInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/15  16:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@NoArgsConstructor
public class RamInstanceResponse extends SubAccountInfo {

    /**
     * 用户认证信息
     */
    private DescribeSafeAuthFlagCollResponse userAuth;

    public RamInstanceResponse(SubAccountInfo subAccountInfo) {
        BeanUtils.copyProperties(subAccountInfo, this);
    }
}
