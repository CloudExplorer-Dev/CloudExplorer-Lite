package com.fit2cloud.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jianneng
 * @date 2023/4/5 09:49
 **/
@Getter
@Setter
@NoArgsConstructor
public class TreeNode {
    public String id;
    public String name;
    public int value;
    public String groupName;
    public String pid;
    public List<TreeNode> children;

    public TreeNode(String id, String name, int value, String groupName, String pid) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.groupName = groupName;
        this.pid = pid;
        this.children = new ArrayList<>();
    }
}
