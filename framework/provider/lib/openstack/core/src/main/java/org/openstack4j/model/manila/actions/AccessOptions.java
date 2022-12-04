package org.openstack4j.model.manila.actions;

import org.openstack4j.model.manila.Access;

/**
 * Options for granting access to a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class AccessOptions {
    private Access.Level accessLevel;
    private Access.Type accessType;
    private String accessTo;

    public AccessOptions(Access.Level accessLevel, Access.Type accessType, String accessTo) {
        this.accessLevel = accessLevel;
        this.accessType = accessType;
        this.accessTo = accessTo;
    }

    public static AccessOptions create(Access.Level accessLevel, Access.Type accessType, String accessTo) {
        return new AccessOptions(accessLevel, accessType, accessTo);
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
