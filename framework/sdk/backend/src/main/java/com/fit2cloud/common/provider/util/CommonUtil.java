package com.fit2cloud.common.provider.util;

import io.reactivex.rxjava3.functions.BiFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;

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
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
            int dstOffset = cal.get(Calendar.DST_OFFSET);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT")));
            Date date = sdf.parse(dateStr);
            return date.getTime() + (zoneOffset + dstOffset);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static LocalDateTime getLocalDateTime(String dateStr, String format) {
        return LocalDateTime.ofEpochSecond(getUTCTime(dateStr, format) / 1000, 0, ZoneOffset.ofHours(0));
    }

    /**
     * 执行函数
     * 有返回值
     *
     * @param providerClass 执行处理器
     * @param req           请求参数
     * @param exec          执行函数
     * @param <T>           执行函数返回对象
     * @return 执行函数返回对象泛型
     */
    public static <T, P> T exec(Class<? extends P> providerClass, String req, BiFunction<P, String, T> exec) {
        try {
            P iCloudProvider = providerClass.getConstructor().newInstance();
            return exec.apply(iCloudProvider, req);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 执行函数
     *
     * @param platform         供应商
     * @param getCloudProvider 获取处理器
     * @param req              请求参数
     * @param exec             执行函数
     * @param <T>              执行函数返回对象
     * @param <P>              执行器对象
     * @return 执行函数返回对象泛型
     */
    public static <T, P> T exec(String platform, Function<String, Class<? extends P>> getCloudProvider, String req, BiFunction<P, String, T> exec) {
        Class<? extends P> providerClass = getCloudProvider.apply(platform);
        return exec(providerClass, req, exec);
    }

    /**
     * 获取请求参数
     *
     * @param credential
     * @param regionId
     * @return
     */
    public static HashMap<String, Object> getParams(String credential, String regionId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("credential", credential);
        params.put("regionId", regionId);
        return params;
    }

    /**
     * 将源List按照指定元素数量拆分为多个List
     *
     * @param source       源List
     * @param splitItemNum 每个List中元素数量
     */
    public static <T> List<List<T>> averageAssign(List<T> source, int splitItemNum) {
        List<List<T>> result = new ArrayList<>();
        if (source != null && source.size() > 0 && splitItemNum > 0) {
            if (source.size() <= splitItemNum) {
                // 源List元素数量小于等于目标分组数量
                result.add(source);
            } else {
                // 计算拆分后list数量
                int splitNum = (source.size() % splitItemNum == 0) ? (source.size() / splitItemNum) : (source.size() / splitItemNum + 1);
                List<T> value = null;
                for (int i = 0; i < splitNum; i++) {
                    if (i < splitNum - 1) {
                        value = source.subList(i * splitItemNum, (i + 1) * splitItemNum);
                    } else {
                        // 最后一组
                        value = source.subList(i * splitItemNum, source.size());
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }
}
