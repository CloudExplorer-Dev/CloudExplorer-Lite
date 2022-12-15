package com.fit2cloud.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SourceTreeObject {

    private String id;
    private String pid;
    private String name;
    private String label;
    private boolean isWorkspace;
    private boolean root;
    private List<SourceTreeObject> children;

}
