package com.fit2cloud.common.scheduler.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/20  2:15 PM}
 * {@code @Version 1.0}
 * {@code @注释:
 * }
 */
public class CronUtils {
    /**
     * 表示匹配该域的任意值。假如在Minutes域使用*, 即表示每分钟都会触发事件。
     */
    private static final String all = "*";
    /**
     * 不指定,周每日,和月每日上,不能都用通配符不然表达式不正确
     */
    private static final String not = "?";
    /**
     * 表示列出枚举值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
     */
    private static final String in = ",";
    /**
     * 表示范围 例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次
     */
    private static final String region = "-";
    /**
     * 表示起始时间开始触发，然后每隔固定时间触发一次。例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次
     */
    private static final String regionInterval = "/";
    /**
     * 定时任务表达式
     * 秒 分 时 日每月 月 日每周 年
     */
    private static final String cronFormat = "%s %s %s %s %s %s %s";

    /**
     * 生成每天指定小时执行的表达式
     *
     * @param hours 指定小时
     * @return 表达式
     */
    public static String createHourOfDay(Integer[] hours) {
        return String.format(cronFormat, "0", "0", getInCron(hours), "*", "*", "?", "*");
    }

    /**
     * 拼接枚举字符串
     *
     * @param inParams 枚举参数
     * @return 拼接后字符串
     */
    private static String getInCron(Integer[] inParams) {
        if (inParams == null) {
            return "*";
        }
        return Arrays.stream(inParams).map(String::valueOf).collect(Collectors.joining(in));
    }

    /**
     * 拼接区域字符串
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 拼接完成的字符串
     */
    private static String getRegion(Integer start, Integer end) {
        return start + region + end;
    }

    /**
     * 拼接起始时间触发字符串
     *
     * @param start    开始时间
     * @param interval 间隔
     * @return 拼接完成的字符串
     */
    private static String getRegionInterval(Integer start, Integer interval) {
        return start + regionInterval + interval;
    }


}
