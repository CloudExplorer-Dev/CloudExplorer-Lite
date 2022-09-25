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
    VIRTUAL_MACHINE("VIRTUAL_MACHINE","虚拟机"),
    CLOUD_DISK("CLOUD_DISK","云磁盘"),
    DISK("DISK","磁盘");

    private String operate;
    private String description;

    ResourceTypeEnum(String operate, String description){
        this.operate = operate;
        this.description = description;
    }

    public static String getDescriptionByOperate(String operate) {
        for (ResourceTypeEnum typeEnum : values()) {
            if(typeEnum.getOperate().equals(operate)){
                return typeEnum.getDescription();
            }
        }
        return "";
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
