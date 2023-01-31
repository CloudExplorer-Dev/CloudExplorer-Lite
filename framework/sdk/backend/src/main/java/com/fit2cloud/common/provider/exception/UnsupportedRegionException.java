package com.fit2cloud.common.provider.exception;

import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.huaweicloud.sdk.core.exception.ClientRequestException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import jodd.util.StringUtil;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/31  9:57}
 * {@code @Version 1.0}
 * {@code @注释: 不支持的区域异常}
 */
public class UnsupportedRegionException extends RuntimeException {
    /**
     * 异常提示
     */
    private String message;
    /**
     * 异常code
     */
    private Integer code;

    public UnsupportedRegionException(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    /**
     * 区域不可用异常
     *
     * @param e 异常
     */
    public static void throwUnsupportedRegion(Exception e) {
        throwUnsupportedRegionHuawei(e);
        throwUnsupportedRegionAli(e);
        throwUnsupportedRegionTencent(e);
    }

    /**
     * 处理华为区域不可用异常
     *
     * @param e 异常
     */
    public static void throwUnsupportedRegionHuawei(Exception e) {
        if (e instanceof IllegalArgumentException illegalArgumentException) {
            //Unexpected regionId: af-south-1
            if (illegalArgumentException.getMessage().startsWith("Unexpected regionId")) {
                throw new UnsupportedRegionException(1002, e.getMessage());
            }
        }
        if (e instanceof ClientRequestException clientRequestException) {
            if (clientRequestException.getHttpStatusCode() == 404 && clientRequestException.getErrorCode().equals("APIGW.0101")) {
                throw new SkipPageException(1002, clientRequestException.getMessage());
            }
        }
        if (e instanceof ServiceResponseException serviceResponseException) {
            if (serviceResponseException.getErrorCode().equals("APIGW.0101")) {
                throw new SkipPageException(1002, serviceResponseException.getMessage());
            }
        }

    }

    /**
     * 处理腾讯区域不可用异常
     *
     * @param e 调用客户端触发的异常
     */
    public static void throwUnsupportedRegionTencent(Exception e) {
        if (e instanceof TencentCloudSDKException tencentCloudSDKException) {
            if (StringUtil.equals(tencentCloudSDKException.getErrorCode(), "UnsupportedRegion")) {
                throw new UnsupportedRegionException(1002, e.getMessage());
            }
        }
    }

    /**
     * 处理阿里云区域不可用异常
     *
     * @param e 调用客户端触发的异常
     */
    public static void throwUnsupportedRegionAli(Exception e) {
        if (e instanceof TeaException teaException) {
            if (teaException.getCode().equals("InvalidOperation.NotSupportedEndpoint") || teaException.getCode().equals("InvalidRegionId.NotFound") || teaException.getCode().equals("EntityNotExist.User.LoginProfile") || teaException.getCode().equals("OperationFailed.Endpoint")) {
                throw new UnsupportedRegionException(1002, e.getMessage());
            }
        }
        if (e instanceof TeaUnretryableException teaUnretryableException) {
            throw new UnsupportedRegionException(1002, e.getMessage());
        }
    }
}
