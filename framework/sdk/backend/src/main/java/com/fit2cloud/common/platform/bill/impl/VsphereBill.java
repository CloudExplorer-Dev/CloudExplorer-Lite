package com.fit2cloud.common.platform.bill.impl;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.provider.impl.vsphere.VsphereBaseCloudProvider;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/9  16:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class VsphereBill implements Bill {
    @Form(inputType = InputType.Radio, defaultValue = "api", textField = "key", valueField = "value", method = "getSyncModes", clazz = VsphereBaseCloudProvider.class, attrs = "{\"style\":\"width:100%\"}")
    private String syncMode;

    @Override
    public void verification() {

    }

    @Override
    public Map<String, Object> getDefaultParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("syncMode", "api");
        return params;
    }
}
