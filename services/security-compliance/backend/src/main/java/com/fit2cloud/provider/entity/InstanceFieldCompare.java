package com.fit2cloud.provider.entity;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  15:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum InstanceFieldCompare {
    /**
     * 判断字段不存在
     */
    NOT_EXIST("不存在", loadingScript("painless/not_exist")),
    /**
     * 字段存在
     */
    EXIST("存在", loadingScript("painless/exist")),
    /**
     * 字段不等于
     */
    NOT_EQ("不等于", loadingScript("painless/not_eq")),
    /**
     * 字段等于
     */
    EQ("等于", loadingScript("painless/eq")),
    /**
     * 字段不包含
     */
    NOT_CONTAIN("不包含", loadingScript("painless/not_contain")),
    /**
     * 包含
     */
    CONTAIN("包含", loadingScript("painless/contain")),
    /**
     * 包含于 用于值包含于远端字段数据 相当于反向包含
     */
    INCLUDED_IN("包含于", loadingScript("painless/included_in")),
    /**
     * 小于等于
     */
    LE("小于等于", loadingScript("painless/le")),
    /**
     * 小于
     */
    LT("小于", loadingScript("painless/lt")),
    /**
     * 大于等于
     */
    GE("大于等于", loadingScript("painless/ge")),
    /**
     * 大于
     */
    GT("大于", loadingScript("painless/gt")),
    /**
     * 平均值大于等于
     */
    AVG_GE("平均值大于等于", loadingScript("painless/avg_ge")),
    /**
     * 平均值大于
     */
    AVG_GT("平均值大于", loadingScript("painless/avg_gt")),
    /**
     * 平均值小于等于
     */
    AVG_LE("平均值小于等于", loadingScript("painless/avg_le")),
    /**
     * 平均值小于
     */
    AVG_LT("平均值小于", loadingScript("painless/avg_lt")),
    /**
     * 总和大于等于
     */
    SUM_GE("总和大于等于", loadingScript("painless/sum_ge")),
    /**
     * 总和大于
     */
    SUM_GT("总和大于", loadingScript("painless/sum_gt")),
    /**
     * 总和小于
     */
    SUM_LT("总和小于", loadingScript("painless/sum_lt")),
    /**
     * 总和小于等于
     */
    SUM_LE("总和小于等于", loadingScript("painless/sum_le")),

    /**
     * 长度大于等于
     */
    LENGTH_GE("长度大于等于", loadingScript("painless/length_ge")),
    /**
     * 长度大于
     */
    LENGTH_GT("长度大于", loadingScript("painless/length_gt")),
    /**
     * 长度小于等于
     */
    LENGTH_LE("长度小于等于", loadingScript("painless/length_le")),
    /**
     * 长度大于
     */
    LENGTH_LT("长度小于", loadingScript("painless/length_lt"));

    /**
     * 比较器提示
     */
    private final String message;
    /**
     * 比较器脚本
     */
    private final String script;

    InstanceFieldCompare(String message, String script) {
        this.message = message;
        this.script = script;
    }

    @SneakyThrows
    private static String loadingScript(String url) {
        File file = new File(InstanceFieldCompare.class.getClassLoader().getResource(url).getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        return new String(bytes, Charset.forName("utf-8"));
    }

    public String getMessage() {
        return message;
    }

    public String getScript() {
        return script;
    }

}
