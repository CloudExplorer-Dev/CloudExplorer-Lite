package com.fit2cloud.common.util;

import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;

import java.util.HashMap;
import java.util.Map;

public class EsScriptUtil {
    /**
     * @param s    脚本构造器
     * @param type 类型 MONTH 或者 YEAR
     * @return 脚本对象
     */
    public static ObjectBuilder<Script> getMonthOrYearScript(Script.Builder s, String type, String value) {
        String script = type.equals("MONTH") ? "doc['billingCycle'].value.monthValue==params.month&&doc['billingCycle'].value.year==params.year" : "doc['billingCycle'].value.year==params.year";
        return s.inline(inlineScript -> inlineScript.lang("painless").source(script).params(getMonthOrYearParams(type, value)));
    }

    /**
     * 获取月份
     * @param type    类型 MONTH 或者 YEAR
     * @param value   月份或者年的值
     * @return       脚本对于参数
     */
    public static Map<String, JsonData> getMonthOrYearParams(String type, String value) {
        if (type.equals("MONTH")) {
            String[] split = value.split("-");
            return new HashMap<>() {{
                put("year", JsonData.of(Integer.parseInt(split[0])));
                put("month", JsonData.of(Integer.parseInt(split[1])));
            }};
        }
        return new HashMap<>() {{
            put("year", JsonData.of(Integer.parseInt(value)));
        }};

    }
}
