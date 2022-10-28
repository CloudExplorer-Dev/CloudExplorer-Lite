package com.fit2cloud.common.form.vo;

import com.fit2cloud.common.form.constants.InputType;
import lombok.Data;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  9:08 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class Form {
    /**
     * 输入类型
     */
    private InputType inputType;

    private String attrs;
    /**
     * 字段名称
     */
    private String field;
    /**
     * 提示
     */
    private String label;
    /**
     * 数据
     */
    private Object value;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 默认值
     */
    private Object defaultValue;
    private boolean defaultJsonValue;
    /**
     * 描述
     */
    private String description;
    /**
     *
     */
    private List<String> relationShows;

    private List<String> relationTrigger;

    /**
     * 执行类
     */
    private String clazz;
    /**
     * 执行函数
     */
    private String method;

    private boolean serviceMethod;

    private Integer group;

    private Integer step;

    /**
     * 排序
     */
    private Integer index;

}
