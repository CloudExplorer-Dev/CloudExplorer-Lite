package com.fit2cloud.controller.request;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:54 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class PageOrganizationRequest extends PageRequest {

    @Serial
    private static final long serialVersionUID = 1628387637305005678L;

    @Schema(title = "组织名称")
    private String name;

    @Schema(title = "组织根ID")
    private String rootId;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @Schema(title = "编辑时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
