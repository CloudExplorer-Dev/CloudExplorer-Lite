package com.fit2cloud.provider.constants;

/**
 * Author: LiuDi
 * Date: 2022/9/21 6:45 PM
 */
public enum F2CInstanceStatus {
    Running,
    Stopped,
    Deleted,
    Starting,
    Stopping,
    Deleting,
    Rebooting,

    Resize,

    Unknown,

    //创建中
    Creating,

    WaitCreating,
    //创建失败
    Failed
}
