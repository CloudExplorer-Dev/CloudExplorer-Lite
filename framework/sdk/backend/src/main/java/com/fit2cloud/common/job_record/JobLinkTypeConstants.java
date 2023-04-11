package com.fit2cloud.common.job_record;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/31  15:51}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum JobLinkTypeConstants {
    /**
     * 任务开始
     */
    JOB_START(1),
    /**
     * 任务结束
     */
    JOB_END(2),
    /**
     * 任务转发
     */
    JOB_FORWARD(3),
    /**
     * 同步区域资源
     */
    CLOUD_SYNC_REGION_RESOURCE(100),

    /**
     * 同步资源
     */
    CLOUD_SYNC_RESOURCE(101),

    /**
     * 添加云资源
     */
    CLOUD_ACTION_ADD_RESOURCE(102),

    /**
     * 删除云资源
     */
    CLOUD_ACTION_DELETE_RESOURCE(103),

    /**
     * 变更云资源
     */
    CLOUD_ACTION_UPDATE_RESOURCE(104),
    /**
     * 校验云账号
     */
    VERIFICATION_CLOUD_ACCOUNT(105),

    /**
     * 插入数据
     */
    SYSTEM_SAVE_DATA(200),

    /**
     * 修改数据
     */
    SYSTEM_UPDATE_DATA(201),

    /**
     * 删除数据
     */
    SYSTEM_DELETE_DATA(202),

    /**
     * 获取数据
     */
    SYSTEM_GET_DATA(203);

    /**
     * 节点类型code
     */
    private Integer code;

    JobLinkTypeConstants(Integer code) {
        this.code = code;
    }
}
