package com.fit2cloud.dto.optimization;

import com.fit2cloud.dao.entity.OptimizationStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author jianneng
 * @date 2023/5/31 12:08
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OptimizationStrategyDTO extends OptimizationStrategy {


    /**
     * 优化规则内容
     */
    private String optimizationContent;
    /**
     * 忽略资源数量
     */
    private Long ignoreNumber;

    /**
     * 忽略资源id列表
     */
    private List<String> ignoreResourceIdList;

}
