package com.fit2cloud.controller.request.workspace;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/8/30 16:24
 **/
@Data
public class PageWorkspaceRequest extends PageRequest implements PageOrderRequestInterface {

    @ApiModelProperty("工作空间名称")
    private String name;

    @ApiModelProperty("组织")
    private List<String> organizationIds;

    @ApiModelProperty("组织名称")
    private String organizationName;

    @ApiModelProperty(value = "排序",example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
