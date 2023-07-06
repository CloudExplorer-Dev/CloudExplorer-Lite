package com.fit2cloud.common.form.vo;

import com.fit2cloud.common.form.constants.InputType;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    private boolean leftLabel;

    private boolean hideLabel;
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

    private List<String> relationShowValues;

    private List<String> relationTrigger;

    /**
     * 执行类
     */
    private String clazz;
    /**
     * 执行函数
     */
    private String method;
    /**
     * 插件id
     */
    private String pluginId;
    /**
     * 执行模块
     */
    private String execModule;

    /**
     * 是否在Spring容器里
     */
    private boolean serviceMethod;

    /**
     * 分组
     */
    private Integer group;

    /**
     * 步骤
     */
    private Integer step;

    /**
     * 排序
     */
    private Integer index;

    private Integer confirmGroup;

    private boolean confirmSpecial;

    private Integer confirmItemSpan;

    private String confirmPosition;

    private Integer footerLocation;

    private String regexp;

    private String regexpDescription;

    private String extraInfo;
    private String hint;
    /**
     * 配置信息
     */
    private Map<String, Object> propsInfo;

    /**
     * 正则集合
     */
    private List<Object> regexList;

    /**
     * 加密，密码框之类的
     */
    private boolean encrypted;
    /**
     * 值
     */
    private String valueField;
    /**
     * label 提示
     */
    private String textField;

    private boolean formatTextField;

}
