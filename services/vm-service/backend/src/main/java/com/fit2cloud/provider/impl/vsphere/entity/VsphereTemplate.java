package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: LiuDi
 * Date: 2022/9/22 2:45 PM
 */
@Data
@NoArgsConstructor
public class VsphereTemplate {

    private String name;
    private String description;
    private int size;
    private boolean hasVmTools;
    public static String SEPARATOR = "&FLAG_SEPARATOR&";

    public VsphereTemplate(String name, boolean hasVmTools, int size) {
        this.name = name;
        this.description = name;
        this.hasVmTools = hasVmTools;
        if(!hasVmTools) {
            this.description += " [未安装VmTools]";
        }
        this.size = size;
    }

    public VsphereTemplate(String libraryId, String id, String name, boolean hasVmTools, int size){
        this(name,hasVmTools,size);
        this.name = libraryId + SEPARATOR + id;
        this.description = name + "[内容库]";
    }
}
