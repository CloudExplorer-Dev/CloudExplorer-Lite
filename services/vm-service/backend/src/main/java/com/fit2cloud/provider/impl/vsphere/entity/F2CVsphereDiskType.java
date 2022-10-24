package com.fit2cloud.provider.impl.vsphere.entity;

/**
 * Author: LiuDi
 * Date: 2022/9/23 2:30 PM
 */
public enum F2CVsphereDiskType {
    /**
     * 与源格式相同（用于基于镜像创建云主机时使用）
     */
    DEFAULT,
    /**
     * 精简置备
     */
    THIN,
    /**
     * 后置备（快速）置零
     */
    THICK_EAGER_ZEROED,
    /**
     * 后置备延迟置零
     */
    THICK_LAZY_ZEROED,
    /**
     * 稀疏型(申请时不可设置)
     */
    SPARSE,
    /**
     * 未知（申请时不可设置）
     */
    NA
}
