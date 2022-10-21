package com.fit2cloud.provider.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13 9:45 PM
 */
@Data
public class BaseDiskAttachRequest extends BaseDiskRequest {

    private String deleteWithInstance;

    private String attachMode;
}
