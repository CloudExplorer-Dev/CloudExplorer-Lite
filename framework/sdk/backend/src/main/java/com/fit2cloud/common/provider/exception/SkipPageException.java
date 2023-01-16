package com.fit2cloud.common.provider.exception;

import com.aliyun.tea.TeaException;
import com.huaweicloud.sdk.core.exception.ClientRequestException;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  4:27 PM
 * @Version 1.0
 * @注释:
 */
public class SkipPageException extends RuntimeException {
    private String message;
    private Integer code;

    public SkipPageException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    /**
     * 当遇到region不可用的情况需要抛出SkipPageException的异常从而跳过同步
     *
     * @param e 异常信息
     */
    public static void throwSkip(Exception e) {
        if (e instanceof TeaException teaException) {
            if (teaException.getCode().equals("InvalidOperation.NotSupportedEndpoint") || teaException.getCode().equals("InvalidRegionId.NotFound") || teaException.getCode().equals("EntityNotExist.User.LoginProfile")) {
                throw new SkipPageException(1001, e.getMessage());
            }
        }
    }

    public static void throwHuaweiSkip(Exception e) {
        if (e instanceof IllegalArgumentException illegalArgumentException) {
            //Unexpected regionId: af-south-1
            if (illegalArgumentException.getMessage().startsWith("Unexpected regionId")) {
                throw new SkipPageException(1001, e.getMessage());
            }
        }
    }

    /**
     * 查询监控时先查询资源再查监控数据
     * 包了一层
     * 在查询资源的时候抛出了跳出异常
     * @param e
     */
    public static void throwSkipPageException(Exception e) {
        if (e instanceof SkipPageException skipPageException) {
            //Unexpected regionId: af-south-1
            if (skipPageException.message.startsWith("Unexpected regionId")) {
                throw new SkipPageException(1001, e.getMessage());
            }
        }
        if (e instanceof ClientRequestException clientRequestException) {
            if (clientRequestException.getHttpStatusCode() == 404 && clientRequestException.getErrorCode().equals("APIGW.0101")) {
                throw new SkipPageException(1001, "跳过");
            }
        }
    }
}
