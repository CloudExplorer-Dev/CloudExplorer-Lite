package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

/**
 * @author jianneng
 * @date 2023/3/9 15:07
 **/
@Data
public class VsphereDatastoreRequest extends VsphereVmBaseRequest {

    /**
     * 宿主机ID
     */
    private String datastoreId;

    /**
     * 集群名称
     */
    private String clusterName;

}
