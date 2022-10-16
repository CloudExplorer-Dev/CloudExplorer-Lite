package com.fit2cloud.provider.impl.huawei.entity.request;

import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.common.platform.bill.impl.HuaweiBill;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiBillCredential;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:22 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class SyncBillRequest {
    /**
     * 认证数据
     */
    private HuaweiBillCredential credential;
    /**
     * 账单信息
     */
    private HuaweiBill bill;
}
