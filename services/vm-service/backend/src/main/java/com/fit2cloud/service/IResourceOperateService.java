package com.fit2cloud.service;

import com.fit2cloud.controller.request.CreateJobRecordRequest;
import com.fit2cloud.controller.request.ExecProviderMethodRequest;
import com.fit2cloud.controller.request.ResourceState;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * Author: LiuDi
 * Date: 2022/10/26 11:16 AM
 */
public interface IResourceOperateService {

    /**
     * 操作资源并生 JobRecord 记录
     * @param createJobRecordRequest
     * @param execProviderMethodRequest
     * @param resourceState
     * @param updateResource
     * @param <T>
     */
    <T> void operateWithJobRecord(CreateJobRecordRequest createJobRecordRequest, ExecProviderMethodRequest execProviderMethodRequest, ResourceState<T> resourceState, Consumer<T> updateResource);

}
