package com.fit2cloud.controller.request;

import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: LiuDi
 * Date: 2022/10/26 10:14 AM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceState<T, V> {
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

    /**
     * 更新资源方法
     */
    Consumer<T> updateResourceMethod;

    /**
     * 删除资源方法
     */
    Consumer<T> deleteResourceMethod;

    /**
     * 保存资源方法
     */
    BiConsumer<T, V> saveResourceMethod;

    /**
     * 更新资源方法
     */
    BiConsumer<T,V> updateResourceMethodNeedTransfer;

}
