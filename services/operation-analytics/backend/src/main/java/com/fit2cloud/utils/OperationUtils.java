package com.fit2cloud.utils;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jianneng
 * @date 2022/12/21 16:33
 **/
public class OperationUtils {

    /**
     * 时间差值计算
     * @param start
     * @param end
     * @return
     */
    public static DateHistogramInterval getIntervalUnit(long start, long end) {
        // 计算时间差值，转为小时(/1000 / 60 / 60)
        long hours = (end - start) / 3600000;
        if (hours <= 168) {
            // 小于一周
            return DateHistogramInterval.HOUR;
        }
        // 小于3个月
        if (hours <= 93 * 24) {
            return DateHistogramInterval.DAY;
        }
        // 小于6个月
        if (hours <= 186 * 24) {
            return DateHistogramInterval.MONTH;
        }
        // 小于24月
        if (hours <= 24 * 30 * 24) {
            return DateHistogramInterval.MONTH;
        } else {
            return DateHistogramInterval.YEAR;
        }
    }

    /**
     * 时间间隔单位转化
     * @param time
     * @param intervalUnit
     * @return
     */
    public static String getTimeFormat(String time, DateHistogramInterval intervalUnit){

        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter formatter = null;

        if (DateHistogramInterval.HOUR.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        } else if(DateHistogramInterval.DAY.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if(DateHistogramInterval.MONTH.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else if(DateHistogramInterval.YEAR.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy");
        }
        return formatter.format(dateTime);
    }

    public static CalendarInterval getCalendarIntervalUnit(long start, long end) {
        // 计算时间差值，转为小时(/1000 / 60 / 60)
        long hours = (end - start) / 3600000;
        if (hours <= 168) {
            // 小于一周
            return CalendarInterval.Hour;
        }
        // 小于3个月
        if (hours <= 93 * 24) {
            return CalendarInterval.Day;
        }
        // 小于6个月
        if (hours <= 186 * 24) {
            return CalendarInterval.Month;
        }
        // 小于24月
        if (hours <= 24 * 30 * 24) {
            return CalendarInterval.Month;
        } else {
            return CalendarInterval.Year;
        }
    }

    public static String getTimeFormat(String time, CalendarInterval intervalUnit){

        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter formatter = null;

        if (CalendarInterval.Hour.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        } else if(CalendarInterval.Day.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if(CalendarInterval.Month.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else if(CalendarInterval.Year.equals(intervalUnit)){
            formatter = DateTimeFormatter.ofPattern("yyyy");
        }
        return formatter.format(dateTime);
    }

}
