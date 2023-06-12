package com.fit2cloud.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(title = "子节点")
    private List<NodeTree> children;

    public NodeTree(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }
}
