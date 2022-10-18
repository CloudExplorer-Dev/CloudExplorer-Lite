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
    CLOUD_ACCOUNT_SYNC_JOB(0),
    /**
     * 云账号账单同步任务
     */
    CLOUD_ACCOUNT_SYNC_BILL_JOB(3),

    /*
     * 虚拟机操作任务
     */
    CLOUD_SERVER_OPERATE_JOB(1),

    /**
     * 虚拟机批量操作任务
     */
    CLOUD_SERVER_BATCH_OPERATE_JOB(2);

    @EnumValue
    private final int code;

    JobTypeConstants(int code) {
        this.code = code;
    }
}
