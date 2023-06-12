package com.fit2cloud.dto.charging;

import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.form.vo.Form;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/1  18:34}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class BillingFieldMetaSetting {
    /**
     * 模块名称
     */
    private String module;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 账单字段设置元数据
     */
    private Map<String, BillingFieldMeta> billingFieldMeta;
    /**
     * 表单收集
     */
    private List<? extends Form> globalConfigMetaForms;
    /**
     * 默认数据
     */
    private Map<String, Object> defaultGlobalConfigMeta;
}
