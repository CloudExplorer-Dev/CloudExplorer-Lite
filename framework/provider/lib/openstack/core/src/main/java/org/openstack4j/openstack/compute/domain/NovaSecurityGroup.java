package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.SecurityGroup;

@JsonRootName("security_groups")
public class NovaSecurityGroup implements SecurityGroup {

    private static final long serialVersionUID = 1L;
    private String name;

    @Override
    public String getName() {
        return name;
    }

}
