package com.fit2cloud.common.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author:张少虎
 * @Date: 2022/10/8  4:49 PM
 * @Version 1.0
 * @注释:
 */
public enum JobTypeConstants {
    /**
     * 云账户同步任务
     */
    CLOUD_ACCOUNT_SYNC_JOB(101),
    /**
     * 云账号账单同步任务
     */
    CLOUD_ACCOUNT_SYNC_BILL_JOB(102),

    /**
     * 安全合规扫描任务
     */
    SECURITY_COMPLIANCE_CLOUD_ACCOUNT_SYNC_JOB(103),

    /**
     * 云账号监控数据同步类型
     */
    CLOUD_ACCOUNT_SYNC_METRIC_MONITOR(104),
    /**
     * 云主机操作任务
     */
    CLOUD_SERVER_OPERATE_JOB(200),
    /**
     * 云主机创建
     */
    CLOUD_SERVER_CREATE_JOB(201),
    /**
     * 云主机删除
     */
    CLOUD_SERVER_DELETE_JOB(202),
    /**
     * 云主机开机
     */
    CLOUD_SERVER_START_JOB(203),
    /**
     * 云主机关机
     */
    CLOUD_SERVER_STOP_JOB(204),
    /**
     * 云主机重启
     */
    CLOUD_SERVER_RESTART_JOB(205),
    /**
     * 云主机配置变更任务
     */
    CLOUD_SERVER_CONFIG_CHANGE_JOB(206),
    /**
     * 云主机回收
     */
    CLOUD_SERVER_RECYCLE_JOB(207),
    /**
     * 磁盘操作任务
     */
    CLOUD_DISK_OPERATE_JOB(300),
    /**
     * 新增磁盘
     */
    CLOUD_DISK_CREATE_JOB(301),
    /**
     * 删除磁盘
     */
    CLOUD_DISK_DELETE_JOB(302),
    /**
     * 挂载磁盘
     */
    CLOUD_DISK_ATTACH_JOB(303),
    /**
     * 卸载磁盘
     */
    CLOUD_DISK_DETACH_JOB(304),
    /**
     * 扩容磁盘
     */
    CLOUD_DISK_ENLARGE_JOB(305),
    /**
     * 续期
     */
    CLOUD_RENEW_INSTANCE_JOB(306);

    @EnumValue
    private final int code;

    JobTypeConstants(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
