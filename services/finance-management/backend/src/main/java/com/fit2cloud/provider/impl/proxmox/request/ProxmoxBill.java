package com.fit2cloud.provider.impl.proxmox.request;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.proxmox.ProxmoxBillProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/19  15:13}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ProxmoxBill implements Bill {
    @Form(inputType = InputType.Radio, defaultValue = "api", execModule = "finance-management",
            textField = "key", valueField = "value", method = "getSyncModes",
            clazz = ProxmoxBillProvider.class, attrs = "{\"style\":\"width:100%\"}")
    private String syncMode;

    @Override
    public void verification() {
        if (StringUtils.isEmpty(syncMode)) {
            throw new Fit2cloudException(1001, "useBucket为必填参数");
        }
    }

    @Override
    public Map<String, Object> getDefaultParams() {
        return null;
    }
}
