package com.fit2cloud.vm.entity.result;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CheckCreateServerResult {
    private boolean pass;
    private String errorInfo;

    public static CheckCreateServerResult success() {
        return new CheckCreateServerResult().setPass(true);
    }

    public static CheckCreateServerResult fail(String error) {
        return new CheckCreateServerResult().setPass(false).setErrorInfo(error);
    }
}
