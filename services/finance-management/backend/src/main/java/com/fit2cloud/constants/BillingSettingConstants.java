package com.fit2cloud.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/24  10:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Component
public class BillingSettingConstants {

    public static String billingPath = "/opt/cloudexplorer/data/billing";

    @Value("${billing.path}")
    public void setBillingPath(String billingPath) {
        BillingSettingConstants.billingPath = billingPath;
    }
}
