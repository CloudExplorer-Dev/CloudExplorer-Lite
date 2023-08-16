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
    RENEW_INSTANCE("RENEW_INSTANCE", "续费"),
    //    RECYCLE_DISK("RECYCLE_DISK", "磁盘放入回收站"),
//    BATCH_ATTACH_DISK("BATCH_ATTACH_DISK", "批量挂载磁盘"),
//    BATCH_DETACH_DISK("BATCH_DETACH_DISK", "批量卸载磁盘"),
//    BATCH_DELETE_DISK("BATCH_DELETE_DISK", "批量删除磁盘"),
//    BATCH_RECYCLE_DISK("BATCH_RECYCLE_DISK", "磁盘批量盘放入回收站"),
    SYNC("SYNC", "同步"),
    CHECK("CHECK", "校验"),
    SCAN("SCAN", "扫描"),
    RECOVER("RECOVER", "恢复"),
    BATCH_RECOVER("BATCH_RECOVER", "批量恢复"),
    RECYCLE("RECYCLE", "回收"),
    ATTACH("ATTACH", "挂载"),
    DETACH("DETACH", "卸载"),
    //    ADD_IN("ADD_IN", "放入"),
    ENLARGE("ENLARGE", "扩容"),
    BATCH_RECYCLE("BATCH_RECYCLE", "批量回收"),
    BATCH_ATTACH("BATCH_ATTACH", "批量挂载"),
    BATCH_DETACH("BATCH_DETACH", "批量卸载"),
    //    BATCH_ADD_IN("BATCH_ADD_IN", "批量放入"),
    BATCH_ENLARGE("BATCH_ENLARGE", "批量扩容"),
    AUTHORISATION("AUTHORISATION", "授权"),
    BATCH_AUTHORISATION("BATCH_AUTHORISATION", "批量授权"),
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
