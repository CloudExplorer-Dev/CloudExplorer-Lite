package com.fit2cloud.controller.request;

import io.reactivex.rxjava3.functions.BiFunction;
import lombok.Builder;
import lombok.Data;
import com.fit2cloud.vm.ICloudProvider;

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
    /**
     * 返回结果是否需要转换成数据库实体
     */
    Boolean resultNeedTransfer;
}
