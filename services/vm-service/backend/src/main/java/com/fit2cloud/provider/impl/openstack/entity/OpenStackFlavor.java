package com.fit2cloud.provider.impl.openstack.entity;

import lombok.Data;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.openstack.common.GenericLink;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class OpenStackFlavor implements Serializable {
    @Serial
    private static final long serialVersionUID = 5258326540646643511L;

    private String instanceTypeDesc;


    private String id;
    private String name;
    private Integer ram;
    private Integer vcpus;
    private Integer disk;
    private int ephemeral;
    private int swap;
    private float rxtxFactor = 1.0f;
    private Boolean disabled;
    private Integer rxtxQuota;
    private Integer rxtxCap;
    private Boolean isPublic;
    private List<GenericLink> links;

    public static OpenStackFlavor copy(Flavor flavor) {
        OpenStackFlavor openStackFlavor = new OpenStackFlavor();
        BeanUtils.copyProperties(flavor, openStackFlavor);

        openStackFlavor.setInstanceTypeDesc(flavor.getVcpus() + "vCPU " + flavor.getRam() / 1024 + "GB " + (flavor.getDisk() == 0 ? "" : (" " + flavor.getDisk() + "GB")));

        return openStackFlavor;
    }

}
