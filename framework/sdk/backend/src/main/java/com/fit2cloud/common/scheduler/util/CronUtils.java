package com.fit2cloud.common.scheduler.util;

import lombok.SneakyThrows;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public static final String all = "*";
    /**
     * 不指定,周每日,和月每日上,不能都用通配符不然表达式不正确
     */
    public static final String not = "?";
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
     * 定时任务表达式 按照每周的话不用传年通配
     * 秒 分 时 日每月 月 日每周
     */
    private static final String cronDayOfWeekFormat = "%s %s %s %s %s %s";

    /**
     * 生成每天指定小时执行的表达式
     *
     * @param hours 指定小时
     * @return 表达式
     */
    public static String createHourOfDay(Integer[] hours) {
        return String.format(cronFormat, "0", "0", getInCron(hours, in), "*", "*", "?", "*");
    }

    /**
     * 创建一个定时任务表达式
     *
     * @param integers 区间
     * @param type     类型
     * @return cron表达式
     */
    public static String create(Integer[] integers, Integer type) {
        if (type.equals(Calendar.SECOND)) {
            return create(integers, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{});
        } else if (type.equals(Calendar.MINUTE)) {
            return create(new Integer[]{}, integers, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{});
        } else if (type.equals(Calendar.HOUR)) {
            return create(new Integer[]{}, new Integer[]{}, integers, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{});
        } else if (type.equals(Calendar.DAY_OF_MONTH)) {
            return create(new Integer[]{}, new Integer[]{}, new Integer[]{}, integers, new Integer[]{}, new Integer[]{}, new Integer[]{});
        } else if (type.equals(Calendar.MONTH)) {
            return create(new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, integers, new Integer[]{}, new Integer[]{});
        } else if (type.equals(Calendar.DAY_OF_WEEK)) {
            return create(new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, integers, new Integer[]{});
        } else if (type.equals(Calendar.YEAR)) {
            return create(new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{}, integers);
        }
        throw new RuntimeException("不支持的类型");
    }

    /**
     * 创建一个定时任务表达式
     * 当前参数的意思是 每年的4月3日1时20分钟22秒执行
     * create(new Integer[]{22}, new Integer[]{20}, new Integer[]{1}, new Integer[]{3}, new Integer[]{4}, new Integer[]{}, new Integer[]{});
     * 当前参数的意思是 每天1时20分22秒执行
     * create(new Integer[]{22}, new Integer[]{20}, new Integer[]{1}, new Integer[]{}, new Integer[]{}, new Integer[]{}, new Integer[]{});
     * 当前参数的意思是 每周日20分1时20分22秒执行
     * create(new Integer[]{22}, new Integer[]{20}, new Integer[]{1}, new Integer[]{}, new Integer[]{}, new Integer[]{1}, new Integer[]{});
     *
     * @param second     秒        0-59
     * @param minute     分钟      0-59
     * @param hours      小时      0-23
     * @param dayOfMonth 每天基于月 1-31 这里需要注意 如果month为2月 如果传31的话 当前任务则永远不会执行
     * @param month      月        1-12
     * @param dayOfWeek  每天基于周 1-7  注意1代码周日
     * @param year       年        指定年需要大于当前时间 不然任务不会执行
     * @return cron表达式
     */
    public static String create(Integer[] second, Integer[] minute, Integer[] hours, Integer[] dayOfMonth, Integer[] month, Integer[] dayOfWeek, Integer[] year) {
        if (dayOfMonth.length > 0 && dayOfWeek.length > 0) {
            throw new RuntimeException("定时任务每日只能基于周或者基于月,不能俩个同时满足");
        }
        List<Integer[]> seconds = List.of(second, minute, hours, dayOfMonth, month, dayOfWeek, year);
        String cronExpression = "";
        if (dayOfWeek.length > 0) {
            if (year.length > 0) {
                cronExpression = String.format(cronFormat,
                        getInCron(second, getDefaultValue(0, seconds, true)),
                        getInCron(minute, getDefaultValue(1, seconds, true)),
                        getInCron(hours, getDefaultValue(2, seconds, true)),
                        getInCron(dayOfMonth, getDefaultValue(3, seconds, true)),
                        getInCron(month, getDefaultValue(4, seconds, true)),
                        getInCron(dayOfWeek, getDefaultValue(5, seconds, true)),
                        getInCron(year, all));
            } else {
                cronExpression = String.format(cronDayOfWeekFormat,
                        getInCron(second, getDefaultValue(0, seconds, true)),
                        getInCron(minute, getDefaultValue(1, seconds, true)),
                        getInCron(hours, getDefaultValue(2, seconds, true)),
                        getInCron(dayOfMonth, getDefaultValue(3, seconds, true)),
                        getInCron(month, getDefaultValue(4, seconds, true)),
                        getInCron(dayOfWeek, getDefaultValue(5, seconds, true)));
            }
        } else {
            cronExpression = String.format(cronFormat,
                    getInCron(second, getDefaultValue(0, seconds, false)),
                    getInCron(minute, getDefaultValue(1, seconds, false)),
                    getInCron(hours, getDefaultValue(2, seconds, false)),
                    getInCron(dayOfMonth, getDefaultValue(3, seconds, false)),
                    getInCron(month, getDefaultValue(4, seconds, false)),
                    getInCron(dayOfWeek, getDefaultValue(5, seconds, false)),
                    getInCron(year, getInCron(year, all)));

        }
        try {
            isValid(cronExpression);
            return cronExpression;
        } catch (Exception e) {
            throw new RuntimeException("参数传递错误:" + e.getMessage());
        }
    }

    /**
     * 创建一个正则表达式根据时间
     *
     * @param date 时间
     * @return 对应时间执行的正则表达式
     */
    public static String create(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return create(new Integer[]{calendar.get(Calendar.SECOND)},
                new Integer[]{calendar.get(Calendar.MINUTE)},
                new Integer[]{calendar.get(Calendar.HOUR)},
                new Integer[]{calendar.get(Calendar.DAY_OF_MONTH)},
                new Integer[]{calendar.get(Calendar.MONTH) + 1},
                new Integer[]{},
                new Integer[]{calendar.get(Calendar.YEAR)});
    }

    public static void main(String[] args) {
        String cronText = create(new Integer[]{22}, new Integer[]{20}, new Integer[]{1}, new Integer[]{3}, new Integer[]{4}, new Integer[]{}, new Integer[]{});
        String nextExecDateString = getNextExecDateString(cronText);
        System.out.println(cronText + " 下一次执行时间:" + nextExecDateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);
        String cronText1 = create(calendar.getTime());
        System.out.println(cronText1 + " 下一次执行时间:" + getNextExecDateString(cronText1));
    }

    /**
     * 获取默认值
     *
     * @param index   所在索引
     * @param seconds 所有数据
     * @param ofWeek  是否是日指定周
     * @return 默认值
     */
    private static String getDefaultValue(Integer index, List<Integer[]> seconds, boolean ofWeek) {
        if (ofWeek) {
            if (index.equals(3)) {
                return not;
            }
            if (index.equals(4)) {
                return all;
            }
        } else {
            if (index.equals(5)) {
                return not;
            }
        }
        boolean anyMatch = IntStream.range(index, seconds.size() - 1).anyMatch(i -> seconds.get(i).length > 0);
        if (anyMatch) {
            return "0";
        } else {
            return all;
        }
    }

    /**
     * 校验 cron是否正确
     *
     * @param cronExpression cron表达式
     * @throws ParseException 解析异常
     */
    public static void isValid(String cronExpression) throws ParseException {
        new CronExpression(cronExpression);
    }

    /**
     * 获取下一次执行时间字符串
     *
     * @param cronExpression cron表达式
     * @return 下一次执行的时间字符串
     */
    public static String getNextExecDateString(String cronExpression) {
        Date date = getDate(cronExpression);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 获取下一次执行的时间
     *
     * @param cronExpression cron表达式
     * @return 下一次执行的时间
     */
    @SneakyThrows
    public static Date getDate(String cronExpression) {
        CronExpression cron = new CronExpression(cronExpression);
        return cron.getNextValidTimeAfter(new Date());
    }

    /**
     * 拼接枚举字符串
     *
     * @param inParams 枚举参数
     * @return 拼接后字符串
     */
    private static String getInCron(Integer[] inParams, String defaultValue) {
        if (inParams == null || inParams.length == 0) {
            return defaultValue;
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
