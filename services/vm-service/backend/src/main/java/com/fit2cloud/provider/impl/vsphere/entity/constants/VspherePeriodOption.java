package com.fit2cloud.provider.impl.vsphere.entity.constants;

/**
 * @author : LiuDi
 * @date : 2022/12/1 11:32
 */
public enum VspherePeriodOption {

    one_month("1", "1个月"),
    two_month("2", "2个月"),
    three_month("3", "3个月"),
    four_month("4", "4个月"),
    five_month("5", "5个月"),
    six_month("6", "6个月"),
    seven_month("7", "7个月"),
    eight_month("8", "8个月"),
    nine_month("9", "9个月"),

    one_year("12", "1年"),
    two_year("24", "2年"),
    three_year("36", "3年");

    private final String period;
    private final String periodDisplayName;

    public String getPeriod() {
        return period;
    }

    public String getPeriodDisplayName() {
        return periodDisplayName;
    }

    VspherePeriodOption(String period, String periodDisplayName) {
        this.period = period;
        this.periodDisplayName = periodDisplayName;
    }
}
