package com.fit2cloud.controller.request;

import com.fit2cloud.provider.ICloudProvider;
import io.reactivex.rxjava3.functions.BiFunction;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

/**
 * Author: LiuDi
 * Date: 2022/10/26 10:11 AM
 */
@Data
@Builder
public class ExecProviderMethodRequest {
    /**
     * 云平台
     */
    String platform;
    /**
     * 执行方法
     */
    BiFunction<ICloudProvider, String, Object> execMethod;
    /**
     * 方法参数
     */
    HashMap<String, Object> methodParams;
}
