package com.fit2cloud.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jianneng
 * @date 2023/1/9 11:46
 **/
@Data
@NoArgsConstructor
public class BarTreeChartData {

    private String id;
    private String name;
    private Long value;
    private String pId;
    private String groupName;
    private List<BarTreeChartData> children;

}
