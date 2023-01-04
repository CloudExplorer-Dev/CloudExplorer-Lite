package com.fit2cloud.constants;

/**
 * 磁盘状态
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public enum DiskTypeConstants {
    CLOUD_EFFICIENCY("cloud_efficiency","高效云盘"),
    CLOUD_ESSD("cloud_essd","ESSD云盘"),
    CLOUD_SSD("cloud_ssd","SSD云盘"),
    CLOUD("cloud","普通云盘"),
    GPSSD("GPSSD","通用型SSD"),
    ESSD("ESSD","极速型SSD"),
    SSD("SSD","超高IO"),
    SAS("SAS","高IO"),
    CLOUD_PREMIUM("CLOUD_PREMIUM","高性能云硬盘"),
    CLOUD_HSSD("CLOUD_HSSD","增强型SSD云硬盘"),
    CLOUD_BSSD("CLOUD_BSSD","通用型SSD云硬盘"),
    cloud_auto("cloud_auto","ESSD AutoPL云盘"),
    THIN("THIN","精简置备"),
    THICK_EAGER_ZEROED("THICK_EAGER_ZEROED","厚置备置零"),
    THICK_LAZY_ZEROED("THICK_LAZY_ZEROED","厚置备延迟置零"),
    SPARSE("SPARSE","稀疏型"),
    LVMDRIVER("lvmdriver-1","lvmdriver-1"),
    DEFAULT("__DEFAULT__","__DEFAULT__"),
    NA("NA","未知"),
    ;

    private String code;

    private String name;
    DiskTypeConstants(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode(){
        return code;
    }

    public static String getName(String code) {
        for (DiskTypeConstants statusConstants : DiskTypeConstants.values()) {
            if (statusConstants.getCode().equals(code)) {
                return statusConstants.name;
            }
        }
        return code;
    }
}
