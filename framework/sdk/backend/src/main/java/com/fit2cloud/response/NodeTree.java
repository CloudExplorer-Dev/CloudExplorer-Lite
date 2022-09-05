package com.fit2cloud.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class NodeTree {

    private String pid;

    private String id;

    private String name;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private List<NodeTree> children;
}
