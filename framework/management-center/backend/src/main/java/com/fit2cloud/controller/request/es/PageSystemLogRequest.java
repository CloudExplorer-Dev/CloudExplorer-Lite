package com.fit2cloud.controller.request.es;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class PageSystemLogRequest extends PageRequest implements PageOrderRequestInterface {
    /**
     * 详细信息
     */
    @Schema(title = "详细信息")
    private String message;

    /**
     * 模块
     */
    @Schema(title = "模块")
    private String module;

    /**
     * 等级
     */
    @Schema(title = "等级")
    private String level;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
