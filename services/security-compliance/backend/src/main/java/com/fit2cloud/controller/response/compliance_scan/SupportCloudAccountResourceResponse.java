package com.fit2cloud.controller.response.compliance_scan;

import com.fit2cloud.base.entity.CloudAccount;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/30  14:39}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class SupportCloudAccountResourceResponse {
    /**
     * 云账号
     */
    private CloudAccount cloudAccount;
    /**
     * 当前云账号对应的资源类型
     */
    private List<DefaultKeyValue<String, String>> resourceTypes;
}
