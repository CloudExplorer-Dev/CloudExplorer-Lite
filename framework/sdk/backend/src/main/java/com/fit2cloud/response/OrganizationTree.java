package com.fit2cloud.response;

import com.fit2cloud.base.entity.Organization;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  10:20 AM
 * @Version 1.0
 * @注释:
 */
@Data
@ToString
public class OrganizationTree extends Organization {
    /**
     * 子组织
     */
    @ApiModelProperty(value = "子组织",notes = "子组织")
    private List<OrganizationTree> children;
}
