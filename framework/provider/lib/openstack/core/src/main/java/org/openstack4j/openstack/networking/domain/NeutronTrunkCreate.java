package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.Trunk;

/**
 * Model for trunk create call
 *
 * @author Kashyap Jha
 */
@JsonRootName("trunk")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronTrunkCreate implements ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;
    @JsonProperty("name")
    private String name;
    @JsonProperty("port_id")
    private String parentPortId;

    public static NeutronTrunkCreate fromTrunk(Trunk trunk) {

        NeutronTrunkCreate toCreate = new NeutronTrunkCreate();
        toCreate.name = trunk.getName();
        toCreate.adminStateUp = trunk.isAdminStateUp();
        toCreate.parentPortId = trunk.getParentPort();
        return toCreate;
    }

}
