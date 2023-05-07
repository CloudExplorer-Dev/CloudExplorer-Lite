package com.fit2cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jianneng
 * @date 2023/4/4 16:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizeSuggestDTO {
    /**
     * 建议名称
     */
    private String name;
    /**
     * 建议index
     */
    private Integer index;
    /**
     * 建议标识code
     */
    private String optimizeSuggestCode;
    /**
     * 符合条件的资源数量
     */
    private Long value;
    /**
     * 资源单位
     */
    private String unit;


}
