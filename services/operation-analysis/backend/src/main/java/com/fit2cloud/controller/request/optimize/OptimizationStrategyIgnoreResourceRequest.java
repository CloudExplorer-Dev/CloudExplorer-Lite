package com.fit2cloud.controller.request.optimize;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 操作优化策略资源请求
 * @author jianneng
 * @date 2023/6/11 14:58
 **/
@Data
@Validated
public class OptimizationStrategyIgnoreResourceRequest {

    private String optimizationStrategyId;
    private List<String> resourceIdList;
}
