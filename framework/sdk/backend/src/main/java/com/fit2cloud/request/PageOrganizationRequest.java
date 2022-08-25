package com.fit2cloud.request;

import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

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
}
