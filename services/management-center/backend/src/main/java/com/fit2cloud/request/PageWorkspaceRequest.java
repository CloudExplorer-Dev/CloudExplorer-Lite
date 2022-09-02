package com.fit2cloud.request;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/8/30 16:24
 **/
@Data
public class PageWorkspaceRequest extends PageRequest {

    @ApiModelProperty("工作空间名称")
    private String name;

    @ApiModelProperty("组织")
    private String organizationId;

    @Size(min = 2,max = 2,message = "创建时间只能是开始和结束")
    @ApiModelProperty(value="创建时间",example ="createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2,max = 2,message = "创建时间只能是开始和结束")
    @ApiModelProperty(value = "修改时间",example ="updateTime[]=2121&updateTime[]=21212" )
    private List<Long> updateTime;

    @ApiModelProperty(value = "排序",example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
