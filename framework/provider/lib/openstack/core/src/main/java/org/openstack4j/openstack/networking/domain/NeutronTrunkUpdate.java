package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.Trunk;

/**
 * Model for trunk update call
 *
 * @author Kashyap Jha
 */
@JsonRootName("trunk")
public class NeutronTrunkUpdate implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp;

    @JsonProperty("description")
    private String description;

    public static NeutronTrunkUpdate update(Trunk t) {
        NeutronTrunkUpdate u = new NeutronTrunkUpdate();
        u.adminStateUp = t.isAdminStateUp();
        u.description = t.getDescription();
        u.name = t.getName();
        return u;
    }

}
