package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

import java.util.List;


@Data
public class VsphereNetworkRequest extends VsphereVmBaseRequest {

    private String cluster;
    private List<String> hosts;

    private String location;
}
