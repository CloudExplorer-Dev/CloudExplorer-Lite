package com.fit2cloud.controller.request.optimize.strategy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jianneng
 * @date 2023/4/7 13:59
 **/
@Getter
@Setter
public class BaseStatusRequest {
    @ApiModelProperty("持续开机")
    private boolean continuedRunning;
    @ApiModelProperty("持续天数")
    private Long continuedDays;
}
