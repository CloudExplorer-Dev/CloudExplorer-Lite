package com.fit2cloud.provider.impl.vsphere.entity;

import com.vmware.vim25.mo.Datastore;
import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/16 8:33 PM
 */
@Data
public class VsphereDisk {
    private String diskId;
    private String path;
    private String datastoreName;
    private Datastore datastore;
    private DiskOpsType diskOpsType;
    private long size;//GB
    private String diskType;
    private String diskMode;
}
