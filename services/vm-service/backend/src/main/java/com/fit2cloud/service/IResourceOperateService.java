package com.fit2cloud.service;

import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.ResourceState;

/**
 * Author: LiuDi
 * Date: 2022/10/26 11:16 AM
 */
public interface IResourceOperateService {

    /**
     * 操作资源并生 JobRecord 记录
     *
     * @param createJobRecordRequest
     * @param execProviderMethodRequest
     * @param resourceState
     * @param <T>                       云管资源实体类型
     * @param <V>                       云平台方法返回实体类型
     */
    <T, V> void operateWithJobRecord(CreateJobRecordRequest createJobRecordRequest, ExecProviderMethodRequest execProviderMethodRequest, ResourceState<T, V> resourceState);

}
