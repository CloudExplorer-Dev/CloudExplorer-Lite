package com.fit2cloud.controller.request.workspace;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/8/30 16:24
 **/
@Data
public class PageWorkspaceRequest extends PageRequest implements PageOrderRequestInterface {

    @Schema(title = "工作空间名称")
    private String name;

    @Schema(title = "组织")
    private List<String> organizationIds;

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
