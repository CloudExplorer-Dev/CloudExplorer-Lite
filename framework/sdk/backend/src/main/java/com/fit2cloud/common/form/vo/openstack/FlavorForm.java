package com.fit2cloud.common.form.vo.openstack;

import com.fit2cloud.common.form.vo.Form;
import lombok.Data;

@Data
public class FlavorForm extends Form {
    /**
     * 值
     */
    private String valueField;
    /**
     * label 提示
     */
    private String textField;

    private String baseTextField;

    private boolean formatTextField;
    /**
     * 获取options数据
     */
    private String method;

}
