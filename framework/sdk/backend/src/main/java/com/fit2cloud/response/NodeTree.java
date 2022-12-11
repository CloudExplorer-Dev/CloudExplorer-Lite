package com.fit2cloud.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NodeTree {
    private String id;
    private String pid;
    private String name;
    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private List<NodeTree> children;

    public NodeTree(String id, String pid, String name) {
        this.pid = pid;
        this.id = id;
        this.name = name;
    }
}
