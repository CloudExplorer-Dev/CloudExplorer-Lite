package com.fit2cloud.controller.request;

import java.util.HashMap;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/14  15:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class RenewInstanceRequest extends HashMap<String, Object> {
    /**
     * 云账号
     *
     * @return 云账号id
     */
    public String getAccountId() {
        return (String) get("accountId");
    }

    /**
     * 认证
     *
     * @return 认证
     */
    public String getCredential() {
        return (String) get("credential");
    }

    public String getPlatform() {
        return (String) get("platform");
    }


    /**
     * 区域
     *
     * @return 区域
     */
    public String getRegionId() {
        return (String) get("region");
    }

    /**
     * 虚拟机主键id
     *
     * @return 虚拟机主键id
     */
    public String getUuid() {
        return (String) get("uuid");
    }

    /**
     * 云主机 ID
     */
    public String getInstanceUuid() {
        return (String) get("instanceUuid");
    }

    /**
     * 续费时间
     *
     * @return 续费时间
     */
    public String getPeriodNum() {
        return (String) get("periodNum");
    }


}
