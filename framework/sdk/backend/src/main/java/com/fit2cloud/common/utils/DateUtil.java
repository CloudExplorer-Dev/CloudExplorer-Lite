package com.fit2cloud.common.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * @author jianneng
 * @date 2022/10/11 16:46
 **/
public class DateUtil extends DateUtils {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 获取当前时间,精确到秒
     * @return
     */
    public static LocalDateTime getSyncTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        return LocalDateTime.parse(now.format(dateTimeFormatter), dateTimeFormatter);
    }

    /**
     * 获取过去n个小时的时间戳
     * @param n
     * @return
     */
    public static Long getBeforeHourTime(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - n);
        return calendar.getTime().getTime();
    }

}
