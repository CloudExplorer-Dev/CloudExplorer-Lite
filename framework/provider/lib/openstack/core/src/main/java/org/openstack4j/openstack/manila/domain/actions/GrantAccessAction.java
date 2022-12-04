package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.Access;

/**
 * Grant access to a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("os-allow_access")
public class GrantAccessAction implements ShareAction {
    private static final long serialVersionUID = 1L;

    @JsonProperty("access_level")
    private Access.Level accessLevel;
    @JsonProperty("access_type")
    private Access.Type accessType;
    @JsonProperty("access_to")
    private String accessTo;

    GrantAccessAction(Access.Level accessLevel, Access.Type accessType, String accessTo) {
        this.accessLevel = accessLevel;
        this.accessType = accessType;
        this.accessTo = accessTo;
    }

    public Access.Level getAccessLevel() {
        return accessLevel;
    }

    public Access.Type getAccessType() {
        return accessType;
    }

    public String getAccessTo() {
        return accessTo;
    }
}
