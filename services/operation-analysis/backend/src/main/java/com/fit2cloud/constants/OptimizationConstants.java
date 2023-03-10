package com.fit2cloud.constants;

/**
 * @author jianneng
 * @date 2023/1/29 09:30
 **/
public enum OptimizationConstants {
    DERATING("derating","降配"),
    UPGRADE("upgrade","升配"),
    PAYMENT("payment","变更付费方式"),
    RECOVERY("recovery","回收"),
    ;

    private final String code;

    private final String name;
    OptimizationConstants(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode(){
        return code;
    }

    public static OptimizationConstants getByCode(String code) {
        for (OptimizationConstants statusConstants : OptimizationConstants.values()) {
            if (statusConstants.getCode().equals(code)) {
                return statusConstants;
            }
        }
        return null;
    }
}
