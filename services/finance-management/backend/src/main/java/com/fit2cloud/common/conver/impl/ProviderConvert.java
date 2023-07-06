package com.fit2cloud.common.conver.impl;

import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.provider.IBaseCloudProvider;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  2:02 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ProviderConvert implements Convert {
    @Override
    public String conver(String o) {
        return PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, o).getCloudAccountMeta().message;
    }
}
