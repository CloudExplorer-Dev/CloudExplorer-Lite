package com.fit2cloud.controller.response;

import com.fit2cloud.base.entity.CloudAccount;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/6  14:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class CloudAccountResponse extends CloudAccount {
    /**
     * 是否公有云
     */
    private boolean publicCloud;
    /**
     * 是否选中
     */
    private boolean selected;
}
