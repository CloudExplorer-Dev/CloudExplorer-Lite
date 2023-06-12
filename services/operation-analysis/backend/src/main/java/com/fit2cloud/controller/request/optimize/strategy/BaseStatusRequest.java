package com.fit2cloud.controller.request.optimize.strategy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jianneng
 * @date 2023/4/7 13:59
 **/
@Getter
@Setter
public class BaseStatusRequest {

    @Schema(title = "持续开机")
    private boolean continuedRunning;

    @Schema(title = "持续天数")
    private Long continuedDays;
}
