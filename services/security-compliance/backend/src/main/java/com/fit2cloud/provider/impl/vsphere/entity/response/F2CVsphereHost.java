package com.fit2cloud.provider.impl.vsphere.entity.response;

import lombok.Data;

@Data
public class F2CVsphereHost {
    private String hostMorVal;
    private String hostName;
    private String clusterName;
    private String dataCenterName;
}
