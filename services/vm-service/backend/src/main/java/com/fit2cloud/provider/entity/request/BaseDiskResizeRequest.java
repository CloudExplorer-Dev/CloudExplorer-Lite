package com.fit2cloud.provider.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/15 9:24 PM
 */
@Data
public class BaseDiskResizeRequest extends BaseDiskRequest{
    /**
     * 新磁盘大小
     */
    private long newDiskSize;

}
