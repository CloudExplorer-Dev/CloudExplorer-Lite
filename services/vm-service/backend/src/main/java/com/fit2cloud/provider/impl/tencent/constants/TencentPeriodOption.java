package com.fit2cloud.provider.impl.tencent.constants;

/**
 * @author : LiuDi
 * @date : 2022/12/1 11:32
 */
public enum TencentPeriodOption {

    one_month(1, "1个月"),
    two_month(2, "2个月"),
    three_month(3, "3个月"),
    four_month(4, "4个月"),
    five_month(5, "5个月"),
    six_month(6, "半年"),
    seven_month(7, "7个月"),
    eight_month(8, "8个月"),
    nine_month(9, "9个月"),
    ten_month(10, "10个月"),
    eleven_month(11, "11个月"),

    one_year(12, "1年"),
    two_year(24, "2年"),
    three_year(36, "3年"),
    four_year(48, "4年"),
    five_year(60, "5年");

    private int period;
    private String periodDisplayName;

    public int getPeriod() {
        return period;
    }

    public String getPeriodDisplayName() {
        return periodDisplayName;
    }

    TencentPeriodOption(int period, String periodDisplayName) {
        this.period = period;
        this.periodDisplayName = periodDisplayName;
    }
}
