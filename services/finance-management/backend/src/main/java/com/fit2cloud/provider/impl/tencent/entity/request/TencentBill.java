package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  6:06 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class TencentBill  implements Bill {

    @Form(inputType = InputType.Radio, label = "", execModule = "finance-management", defaultValue = "api", textField = "key", valueField = "value", method = "getSyncModes", clazz = TencentCloudProvider.class, attrs = "{\"style\":\"width:100%\"}")
    private String syncMode;

    @Form(inputType = InputType.SingleSelect, label = "区域", execModule = "finance-management", relationShows = {"syncMode"}, relationShowValues = "bucket", textField = "name", valueField = "regionId", method = "getRegions", relationTrigger = "syncMode", clazz = TencentCloudProvider.class)
    private String regionId;

    @Form(inputType = InputType.SingleSelect, label = "桶", execModule = "finance-management", relationShows = "syncMode", relationShowValues = "bucket", textField = "name", valueField = "name", relationTrigger = "regionId", method = "getBuckets", clazz = TencentCloudProvider.class)
    private String bucketId;

    @Override
    public void verification() {
        if (StringUtils.isEmpty(syncMode)) {
            throw new Fit2cloudException(1001, "useBucket为必填参数");
        }
        if (StringUtils.equals(syncMode, "bucket")) {
            if (StringUtils.isEmpty(regionId) || StringUtils.isEmpty(bucketId)) {
                throw new Fit2cloudException(1002, "获取数据使用桶后, 区域id和桶id为必填参数");
            }
        }
    }

    @Override
    public Map<String, Object> getDefaultParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("syncMode", "api");
        return params;
    }
}
