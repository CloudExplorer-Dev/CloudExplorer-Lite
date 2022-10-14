package com.fit2cloud.common.provider.exception;

import com.aliyun.tea.TeaException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  9:48 PM
 * @Version 1.0
 * @注释: 重试异常
 */
public class ReTryException extends RuntimeException {
    private String message;
    private Integer code;

    public ReTryException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    /**
     * 如果请求异常是 服务器超时 或者请求被流控 则重试
     *
     * @param e 异常信息
     */
    public static void throwReTry(Exception e) {
        //	Ecs	Throttling	Request was denied due to request throttling, please try again after 5 minutes.	您当前的请求被流控，请5分钟后重试。
        //	Ecs	RequestTimeout	The request encounters an upstream server timeout.	上游服务器超时，请求被拒绝。
        if (e instanceof TeaException teaException) {
            if (teaException.getCode().equals("Throttling") || teaException.getCode().equals("RequestTimeout")) {
                throw new ReTryException(1000, "重试");
            }
        }
    }

    public static void throwHuaweiReTry(Exception e) {
        // 弹性云服务器	Common.0024	exceeds flow over limit	请求流控	请求并发过高，请稍后重试。
        if (e instanceof ServiceResponseException serviceResponseException) {
            if (serviceResponseException.getErrorCode().equals("Common.0024")) {
                throw new ReTryException(1000, "重试");
            }
        }
    }
}
