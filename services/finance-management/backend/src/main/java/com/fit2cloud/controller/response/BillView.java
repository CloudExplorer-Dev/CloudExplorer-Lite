package com.fit2cloud.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "账单分组", description = "账单分组")
    private List<DefaultKeyValue<String, String>> billGroupDetails;

    @Schema(title = "值", description = "值")
    private Double value;
}
