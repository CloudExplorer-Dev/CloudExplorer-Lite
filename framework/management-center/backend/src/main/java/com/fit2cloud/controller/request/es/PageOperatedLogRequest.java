package com.fit2cloud.controller.request.es;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/9/23 16:27
 **/
@Data
public class PageOperatedLogRequest extends PageRequest implements PageOrderRequestInterface {

    /**
     * 类型，0登录日志、1云主机操作日志、2磁盘操作日志、3平台管理日志
     */
    private String type;

    /**
     * 请求ID
     */
    @Schema(title = "请求ID")
    private String requestId;

    /**
     * 模块
     */
    @Schema(title = "模块")
    private String module;

    /**
     * 操作
     */
    @Schema(title = "操作")
    private String operated;

    /**
     * 操作名称
     */
    @Schema(title = "操作名称")
    private String operatedName;

    /**
     * 资源ID
     */
    @Schema(title = "资源ID")
    private String resourceId;

    /**
     * 资源名称
     */
    @Schema(title = "资源名称")
    private String resourceName;

    /**
     * 关联资源名称
     */
    @Schema(title = "关联资源名称")
    private String joinResourceName;


    /**
     * 资源类型
     */
    @Schema(title = "资源类型")
    private String resourceType;

    /**
     * 操作人
     */
    @Schema(title = "操作人")
    private String user;


    /**
     * 请求状态：1 成功 0 失败
     */
    private Integer status;

    /**
     * 来源IP
     */
    private String sourceIp;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
