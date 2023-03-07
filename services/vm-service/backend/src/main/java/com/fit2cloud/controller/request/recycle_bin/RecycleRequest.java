package com.fit2cloud.controller.request.recycle_bin;

import com.fit2cloud.common.constants.ResourceTypeConstants;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2023/3/7 10:40
 */
@Data
public class RecycleRequest {
    /**
     * 回收站 ID
     */
    private String id;
    /**
     * 资源 ID
     */
    private String resourceId;
    /**
     * 资源类型
     */
    private ResourceTypeConstants resourceType;
}
