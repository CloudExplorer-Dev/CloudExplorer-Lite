package com.fit2cloud.common.log.constants;

/**
 * 操作类型枚举
 *
 * @author jianneng
 * @date 2022/9/15 11:52
 **/
public enum OperatedTypeEnum {

    LOGIN("LOGIN", "登录"),
    LOGOUT("LOGOUT", "登出"),
    ADD("ADD", "创建"),

    CREATE_SERVER("CREATE_SERVER", "创建云主机"),
    CHANGE_SERVER_CONFIG("CHANGE_SERVER_CONFIG", "配置变更"),
    BATCH_ADD("BATCH_ADD", "批量创建"),
    MODIFY("MODIFY", "更新"),
    DELETE("DELETE", "删除"),
    RECYCLE_SERVER("RECYCLE_SERVER", "云主机放入回收站"),
    RECOVER_SERVER("RECOVER_SERVER", "云主机从回收站恢复"),
    BATCH_DELETE("BATCH_DELETE", "批量删除"),
    SEARCH("SEARCH", "搜索"),

    POWER_ON("POWER_ON", "开机"),
    POWER_OFF("POWER_OFF", "关闭电源"),
    SHUTDOWN("SHUTDOWN", "关机"),
    HARD_SHUTDOWN("HARD_SHUTDOWN", "硬关机"),
    REBOOT("REBOOT", "重启"),
    HARD_REBOOT("HARD_REBOOT", "硬重启"),

    BATCH_OPERATE("BATCH_OPERATE", "批量操作"),

    CREATE_DISK("CREATE_DISK", "新增磁盘"),
    ENLARGE_DISK("ENLARGE_DISK", "扩容磁盘"),
    ATTACH_DISK("ATTACH_DISK", "挂载磁盘"),
    DETACH_DISK("DETACH_DISK", "卸载磁盘"),
    DELETE_DISK("DELETE_DISK", "删除磁盘"),
    RECYCLE_DISK("RECYCLE_DISK", "磁盘放入回收站"),

    BATCH_ATTACH_DISK("BATCH_ATTACH_DISK", "批量挂载磁盘"),
    BATCH_DETACH_DISK("BATCH_DETACH_DISK", "批量卸载磁盘"),
    BATCH_DELETE_DISK("BATCH_DELETE_DISK", "批量删除磁盘"),
    BATCH_RECYCLE_DISK("BATCH_RECYCLE_DISK", "磁盘批量盘放入回收站"),
    SYNC("SYNC", "同步"),
    CHECK("CHECK", "校验"),
    ;

    private String operate;
    private String description;

    OperatedTypeEnum(String operate, String description) {
        this.operate = operate;
        this.description = description;
    }

    public static String getDescriptionByOperate(String operate) {
        for (OperatedTypeEnum typeEnum : values()) {
            if (typeEnum.getOperate().equals(operate)) {
                return typeEnum.getDescription();
            }
        }
        return "";
    }

    public static OperatedTypeEnum getByDescription(String description) {
        for (OperatedTypeEnum typeEnum : values()) {
            if (typeEnum.getDescription().equals(description)) {
                return typeEnum;
            }
        }
        return null;
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
