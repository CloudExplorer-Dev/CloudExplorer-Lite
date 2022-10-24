package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.provider.entity.F2CDisk;
import lombok.Data;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:52 PM
 */
@Data
public class VsphereCreateDiskRequest extends VsphereVmBaseRequest {
    /**
     * 磁盘信息
     */
    private List<F2CDisk> disks;

    private String instanceUuid;

}
