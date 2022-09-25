package com.fit2cloud.controller.request.es;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/9/23 16:27
 **/
@Data
public class PageOperatedLogRequest extends PageRequest {

    /**
     * 类型，0登录日志、1虚拟机操作日志、2磁盘操作日志、3平台管理日志
     */
    private String type;

    /**
     * 请求ID
     */
    @ApiModelProperty("请求ID")
    private String requestId;

    /**
     * 模块
     */
    @ApiModelProperty("模块")
    private String module;

    /**
     * 操作
     */
    @ApiModelProperty("操作")
    private String operated;

    /**
     * 操作名称
     */
    @ApiModelProperty("操作名称")
    private String operatedName;

    /**
     * 资源ID
     */
    @ApiModelProperty("资源ID")
    private String resourceId;

    /**
     * 资源类型
     */
    @ApiModelProperty("资源类型")
    private String resourceType;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String user;


    /**
     * 请求状态：1 成功 0 失败
     */
    private Integer status;

    /**
     * 来源IP
     */
    private String sourceIp;

    @ApiModelProperty(value = "排序",example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
