package com.fit2cloud.vm.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.vm.constants.ExpirePolicyConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/11  11:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class RenewInstanceRequest extends BaseRequest {
    /**
     * 实例id
     */
    private String instanceUuid;

    /**
     * 是否是批量设置
     */
    private boolean batch;

    /**
     * 续费时长
     */
    @Form(inputType = InputType.SingleSelect,
            label = "续费时长",
            defaultValue = "1",
            required = false,
            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            clazz = RenewInstanceRequest.class,
            propsInfo = "{\"style\":{\"width\":\"120px\"}}"
    )
    private String periodNum;

    /**
     * 是否自动续费
     */
    @Form(inputType = InputType.SwitchBtn,
            label = "自动续费",
            defaultValue = "NO",
            textField = "key",
            valueField = "value",
            attrs = "{\"inactive-value\":\"NO\",\"active-value\":\"YES\"}",
            propsInfo = "{\"style\":{\"width\":\"40px\"}}"
    )
    private ExpirePolicyConstants expirePolicy;


    public void setRegion(String regionId) {
        super.setRegionId(regionId);
    }

    /**
     * 获取付费周期
     *
     * @param req 请求参数
     * @return 付费周期
     */
    public List<Map<String, Object>> getPeriodOption(String req) {
        List<Map<String, Object>> periodList = new ArrayList<>();
        for (PeriodOption option : PeriodOption.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("period", option.getPeriod());
            map.put("periodDisplayName", option.getPeriodDisplayName());
            periodList.add(map);
        }
        return periodList;
    }

    public enum PeriodOption {
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

        PeriodOption(String period, String periodDisplayName) {
            this.period = period;
            this.periodDisplayName = periodDisplayName;
        }
    }

}
