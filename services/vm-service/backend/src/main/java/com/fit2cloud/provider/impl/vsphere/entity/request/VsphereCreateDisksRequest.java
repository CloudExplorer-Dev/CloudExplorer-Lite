package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;
import com.fit2cloud.vm.entity.F2CDisk;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:52 PM
 */
@Data
public class VsphereCreateDisksRequest extends VsphereVmBaseRequest {
    /**
     * 磁盘信息
     */
    private List<F2CDisk> disks;

    private String instanceUuid;

}
