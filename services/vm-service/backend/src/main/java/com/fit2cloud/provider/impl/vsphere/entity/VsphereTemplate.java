package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/22 2:45 PM
 */
@Data
public class VsphereTemplate {

    private String name;
    private String description;
    private int size;
    private boolean hasVmTools;
    public static String SEPARATOR = "&FLAG_SEPARATOR&";
}
