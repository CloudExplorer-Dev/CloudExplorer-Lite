package com.fit2cloud.request;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:54 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class PageOrganizationRequest extends PageRequest {
    @ApiModelProperty("组织名称")
    private String name;

    @Size(min = 2,max = 2,message = "创建时间只能是开始和结束")
    @ApiModelProperty(value="创建时间",example ="createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2,max = 2,message = "创建时间只能是开始和结束")
    @ApiModelProperty(value = "修改时间",example ="updateTime[]=2121&updateTime[]=21212" )
    private List<Long> updateTime;

    @ApiModelProperty(value = "排序",example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
