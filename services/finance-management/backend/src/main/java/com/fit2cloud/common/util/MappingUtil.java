package com.fit2cloud.common.util;

import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.constants.BillFieldConstants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/6  7:22 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class MappingUtil {
    public static String mapping(String field, String groupName) {
        if (BillFieldConstants.BILL_FIELD.containsKey(field)) {
            return CommonUtil.exec(BillFieldConstants.BILL_FIELD.get(field).conver(), groupName, Convert::conver);
        }
        if (field.startsWith("orgTree.")) {
            return CommonUtil.exec(BillFieldConstants.BILL_FIELD.get("orgTree").conver(), groupName, Convert::conver);
        }
        return groupName;
    }
}
