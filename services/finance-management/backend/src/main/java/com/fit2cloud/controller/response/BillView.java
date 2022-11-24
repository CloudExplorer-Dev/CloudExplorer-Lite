package com.fit2cloud.controller.response;

import com.fit2cloud.dao.jentity.Group;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/29  6:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class BillView {
    @ApiModelProperty(value = "账单分组", notes = "账单分组")
    private List<DefaultKeyValue<String, String>> billGroupDetails;

    @ApiModelProperty(value = "值", notes = "值")
    private Double value;
}
