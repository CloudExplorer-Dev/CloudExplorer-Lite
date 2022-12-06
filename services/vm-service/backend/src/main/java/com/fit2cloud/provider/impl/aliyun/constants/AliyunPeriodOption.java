package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * @author : LiuDi
 * @date : 2022/12/1 14:21
 */
public enum AliyunPeriodOption {

    one_week("1week", "1周"),

    one_month("1", "1个月"),
    two_month("2", "2个月"),
    three_month("3", "3个月"),
    six_month("6", "半年"),

    one_year("12", "1年"),
    two_year("24", "2年"),
    three_year("36", "3年"),
    four_year("48", "4年"),
    five_year("60", "5年");

    private String period;
    private String periodDisplayName;

    public String getPeriod() {
        return period;
    }

    public String getPeriodDisplayName() {
        return periodDisplayName;
    }

    AliyunPeriodOption(String period, String periodDisplayName) {
        this.period = period;
        this.periodDisplayName = periodDisplayName;
    }
}
