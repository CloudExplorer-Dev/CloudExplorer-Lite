package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.AttachInterfaceType;

/**
 * Adds a Router Interface based on a Port or Subnet identifier
 *
 * @author Jeremy Unruh
 */
public class AddRouterInterfaceAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("port_id")
    private String portId;

    @JsonProperty("subnet_id")
    private String subnetId;

    public static AddRouterInterfaceAction create(AttachInterfaceType type, String portOrSubnetId) {
        AddRouterInterfaceAction action = new AddRouterInterfaceAction();
        if (type == AttachInterfaceType.PORT)
            action.portId = portOrSubnetId;
        else
            action.subnetId = portOrSubnetId;
        return action;
    }

    @JsonIgnore
    public String getPortId() {
        return portId;
    }

    @JsonIgnore
    public String getSubnetId() {
        return subnetId;
    }

}
