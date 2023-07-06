package com.fit2cloud.vm;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;

public interface ICreateServerRequest {

    default FormObject toForm() {
        return FormUtil.toForm(this.getClass());
    }

    int getCount();

    int getIndex();

    void setCredential(String credential);

    void setIndex(int count);

    void setId(String id);

    String getId();
}
