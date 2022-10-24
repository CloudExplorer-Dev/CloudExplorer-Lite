package com.fit2cloud.common.platform.bill;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.util.CommonUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  1:36 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface Bill {
    /**
     * 校验参数是否正确
     *
     * @return 参数
     */
    void verification();

    static Map<String, Object> getDefaultParams(String platform) {
        try {
            Bill bill = PlatformConstants.valueOf(platform).getBillClass().getConstructor().newInstance();
            return bill.getDefaultParams();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @return 获取默认参数
     */
    Map<String, Object> getDefaultParams();
}
