package com.fit2cloud.dto.optimization;

import com.fit2cloud.controller.response.OptimizationRuleFieldResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述：资源类型dto
 * @author jianneng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTypeDTO {

    private String name;
    private String value;
    private List<OptimizationRuleFieldResponse> optimizationRuleFieldList;

}
