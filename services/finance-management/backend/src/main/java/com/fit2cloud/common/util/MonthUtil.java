package com.fit2cloud.common.util;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/29  11:58}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class MonthUtil {
    /**
     * 如果还未到上个月的出账日,还需要同步上个月数据,反之则同步当前月
     *
     * @param billingDay 出账日
     * @return 需要同步的月份
     */
    public static List<String> getMonths(Integer billingDay) {
        Calendar instance = Calendar.getInstance();
        if (instance.get(Calendar.DAY_OF_MONTH) < billingDay) {
            return getHistoryMonth(2);
        }
        return getHistoryMonth(1);
    }

    /**
     * 获取历史月份
     *
     * @param month   当前月份
     * @param history 获取多少个月
     * @return 月份数组
     */
    public static List<String> getHistoryMonth(String month, Integer history) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            simpleDateFormat.parse(month);
        } catch (ParseException e) {
            throw new Fit2cloudException(ErrorCodeConstants.BILL_VIEW_DATE_FORMAT.getCode(), ErrorCodeConstants.BILL_VIEW_DATE_FORMAT.getMessage());
        }
        Calendar instance = Calendar.getInstance();
        String[] split = month.split("-");
        return IntStream.range(0, history).boxed().map(i -> {
            instance.set(Calendar.DAY_OF_MONTH, 1);
            instance.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
            instance.set(Calendar.YEAR, Integer.parseInt(split[0]));
            instance.add(Calendar.MONTH, (i * -1));
            return String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1);
        }).toList();

    }

    /**
     * 获取历史月份
     *
     * @param history 获取历史多少个月
     * @return 月份数组
     */
    public static List<String> getHistoryMonth(Integer history) {
        Calendar instance = Calendar.getInstance();
        String currentMonth = String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1);
        return getHistoryMonth(currentMonth, history);
    }

    /**
     * 获取制定月份的所有日
     *
     * @param month 指定月份
     * @return [yyyy-mm-dd]
     */
    public static List<String> getMonthDays(String month) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(CommonUtil.getUTCTime(month, "yyyy-MM"));
        return IntStream.range(1, StringUtils.equals(getCurrentMonth(), month) ? instance.get(Calendar.DAY_OF_MONTH) + 1 : instance.getActualMaximum(Calendar.DAY_OF_MONTH) + 1)
                .boxed().map(day -> String.format("%04d-%02d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1, day))
                .toList();
    }

    /**
     * 获取当前day
     *
     * @return yyyy-MM-dd
     */
    public static String getCurrentDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前月份
     *
     * @return 当前月份 format yyyy-mm
     */
    public static String getCurrentMonth() {
        Calendar instance = Calendar.getInstance();
        return String.format("%04d-%02d", instance.get(Calendar.YEAR), instance.get(Calendar.MONTH) + 1);
    }
}
