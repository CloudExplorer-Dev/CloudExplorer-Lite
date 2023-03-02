package com.fit2cloud.common.log.constants;

/**
 * 资源类型
 * @author jianneng
 * @date 2022/9/23 15:13
 **/
public enum ResourceTypeEnum {
    SYSTEM("SYSTEM","系统"),

    LOG("LOG","日志"),
    ORGANIZATION("ORGANIZATION","组织"),
    WORKSPACE("WORKSPACE","工作空间"),
    USER("USER","用户"),
    ROLE("ROLE","角色"),
    CLOUD_ACCOUNT("CLOUD_ACCOUNT","云账号"),
    CLOUD_SERVER("CLOUD_SERVER","云主机"),
    CLOUD_DISK("CLOUD_DISK","磁盘"),
    DISK("DISK","磁盘"),
    RECYCLE("RECYCLE","回收站"),
    BILL_RULE("BILL_RULE","账单规则"),
    DIMENSION("DIMENSION","分账设置"),

    COMPLIANCE_RULE("COMPLIANCE_RULE","合规规则"),
    COMPLIANCE_RULE_GROUP("COMPLIANCE_RULE_GROUP","合规规则组"),
    COMPLIANCE_SCAN("COMPLIANCE_SCAN","扫描检查"),
    EMPTY("EMPTY","无"),
    ;

    private String code;
    private String name;

    ResourceTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (ResourceTypeEnum typeEnum : values()) {
            if(typeEnum.getCode().equals(code)){
                return typeEnum.getName();
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static ResourceTypeEnum codeOf(String code){
        for (ResourceTypeEnum typeEnum : values()) {
            if(typeEnum.getCode().equals(code)){
                return typeEnum;
            }
        }
        return null;
    }
}
