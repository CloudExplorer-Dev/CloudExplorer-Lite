package com.fit2cloud.controller.response.compliance_scan;

import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/7  14:32}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class SupportPlatformResourceResponse {
    /**
     * 云平台
     */
    private String platform;
    /**
     * 当前云账号对应的资源类型
     */
    private List<DefaultKeyValue<String, String>> resourceTypes;
}
