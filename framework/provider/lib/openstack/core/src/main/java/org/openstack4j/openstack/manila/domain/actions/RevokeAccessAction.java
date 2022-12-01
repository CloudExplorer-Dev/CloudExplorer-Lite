package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Revokes access from a share;
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("os-deny_access")
public class RevokeAccessAction implements ShareAction {
    private static final long serialVersionUID = 1L;

    @JsonProperty("access_id")
    private String accessId;

    RevokeAccessAction(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessId() {
        return accessId;
    }
}
