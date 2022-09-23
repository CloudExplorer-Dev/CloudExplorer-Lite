package com.fit2cloud.provider.util;

import com.huaweicloud.sdk.ecs.v2.model.ServerFlavor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author:张少虎
 * @Date: 2022/9/22  4:42 PM
 * @Version 1.0
 * @注释:
 */
public class CommonUtil {
    /**
     * 获取utc时间,根据时间字符串
     *
     * @param dateStr 时间字符串
     * @param format  format表达式
     * @return 时间戳
     */
    public static long getUTCTime(String dateStr, String format) {
        try {
            Calendar cal = Calendar.getInstance();
            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);
            return date.getTime() + (zoneOffset + dstOffset);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
