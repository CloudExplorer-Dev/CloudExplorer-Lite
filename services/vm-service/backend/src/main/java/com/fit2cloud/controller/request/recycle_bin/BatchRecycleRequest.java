package com.fit2cloud.controller.request.recycle_bin;

import com.fit2cloud.common.constants.ResourceTypeConstants;
import lombok.Data;

import java.util.List;

/**
 * @author : LiuDi
 * @date : 2023/1/12 19:49
 */
@Data
public class BatchRecycleRequest {
    /**
     * 回收站 ID 集合
     */
    private List<String> ids;
    /**
     * 资源 ID 集合
     */
    private  List<String> resourceIds;
    /**
     * 资源类型
     */
    private ResourceTypeConstants resourceType;

}
