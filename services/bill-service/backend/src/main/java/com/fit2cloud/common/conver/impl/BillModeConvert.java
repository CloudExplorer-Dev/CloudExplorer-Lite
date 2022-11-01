package com.fit2cloud.common.conver.impl;

import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.provider.constants.BillModeConstants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:23 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class BillModeConvert implements Convert {
    @Override
    public String conver(String o) {
        return BillModeConstants.valueOf(o).getMessage();
    }
}
