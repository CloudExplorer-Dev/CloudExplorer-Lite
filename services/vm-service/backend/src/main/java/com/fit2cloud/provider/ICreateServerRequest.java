package com.fit2cloud.provider;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;

public interface ICreateServerRequest {

    default FormObject toForm() {
        return FormUtil.toForm(this.getClass());
    }
}
