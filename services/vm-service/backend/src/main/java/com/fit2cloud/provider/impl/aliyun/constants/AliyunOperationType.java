package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * @author : LiuDi
 * @date : 2023/1/3 10:35
 */

public enum AliyunOperationType {
    /**
     * 升级资源
     */
    Upgrade,
    /**
     * 降级资源
     */
    Downgrade,
    /**
     * 续费降配
     */
    RenewDowngrade,
    /**
     * 过期实例的续费变配
     */
    RenewModify,
}
