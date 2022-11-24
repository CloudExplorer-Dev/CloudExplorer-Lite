package com.fit2cloud.provider.impl.openstack.entity;

import lombok.Data;

@Data
public class CheckStatusResult {

    private boolean success;
    private String fault;
    private Object object;

    private CheckStatusResult(boolean success, Object object, String fault) {
        this.success = success;
        this.object = object;
        this.fault = fault;
    }

    public static CheckStatusResult success() {
        return new CheckStatusResult(true, null, null);
    }

    public static CheckStatusResult success(Object object) {
        return new CheckStatusResult(true, object, null);
    }

    public static CheckStatusResult fail(String fault) {
        return fail(fault, null);
    }

    public static CheckStatusResult fail(String fault, Object object) {
        return new CheckStatusResult(false, object, fault);
    }
}
