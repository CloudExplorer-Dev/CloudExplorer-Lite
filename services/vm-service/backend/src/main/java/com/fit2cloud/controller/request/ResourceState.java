package com.fit2cloud.controller.request;

import lombok.Builder;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/26 10:14 AM
 */
@Data
@Builder
public class ResourceState<T>  {
    /**
     * 操作之前的资源实体记录
     */
    private T beforeResource;
    /**
     * 操作中的资源实体记录
     */
    private T processingResource;
    /**
     * 操作后的资源实体记录
     */
    private T afterResource;
}
