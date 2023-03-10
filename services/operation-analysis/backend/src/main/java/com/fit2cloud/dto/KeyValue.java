package com.fit2cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jianneng
 * @date 2022/12/18 16:10
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue {
    private String name;
    private Long value;
}
