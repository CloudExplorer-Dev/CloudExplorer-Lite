package com.fit2cloud.controller.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/12/14 17:05
 **/
@Data
@Builder
public class ResourceAllocatedInfo {

    private BigDecimal total;
    private BigDecimal allocated;
    private BigDecimal free;
    private BigDecimal allocatedRate;
}
