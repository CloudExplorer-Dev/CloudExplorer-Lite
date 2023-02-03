package com.fit2cloud.common.scheduler.entity;

import com.fit2cloud.common.scheduler.util.CronUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/1  12:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleCron {
    /**
     * 秒
     */
    private Integer[] second = new Integer[]{};
    /**
     * 分钟
     */
    private Integer[] minute = new Integer[]{};
    /**
     * 小时
     */
    private Integer[] hours = new Integer[]{};
    /**
     * 天 基于月
     */
    private Integer[] dayOfMonth = new Integer[]{};
    /**
     * 月份
     */
    private Integer[] month = new Integer[]{};
    /**
     * 周
     */
    Integer[] dayOfWeek = new Integer[]{};
    /**
     * 年
     */
    Integer[] year = new Integer[]{};

    public SimpleCron(String cronExpression) {
        try {
            CronUtils.isValid(cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String[] split = cronExpression.split(" ");
        Integer[][] integers = IntStream.range(0, split.length).boxed()
                .map(index -> Arrays.stream(split[index].split(",")).filter(s -> !s.equals(CronUtils.all) && !s.equals(CronUtils.not))
                        .map(Integer::parseInt).toArray(Integer[]::new))
                .toArray(Integer[][]::new);
        this.second = integers[0];
        this.minute = integers[1];
        this.hours = integers[2];
        this.dayOfMonth = integers[3];
        this.month = integers[4];
        this.dayOfWeek = integers[5];
        if (integers.length == 7) {
            this.year = integers[6];
        } else {
            this.year = new Integer[]{};
        }
    }

    /**
     * 转换为cron 表达式
     *
     * @return cron 表达式
     */
    public String parse() {
        return CronUtils.create(this.second, this.minute, this.hours, this.dayOfMonth, this.month, this.dayOfWeek, this.year);
    }
}
