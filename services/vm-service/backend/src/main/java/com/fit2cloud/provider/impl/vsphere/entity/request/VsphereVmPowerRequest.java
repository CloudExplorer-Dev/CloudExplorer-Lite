package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/8 15:22
 **/
@Data
public class VsphereVmPowerRequest extends VsphereVmBaseRequest{

    private String uuid;
}
