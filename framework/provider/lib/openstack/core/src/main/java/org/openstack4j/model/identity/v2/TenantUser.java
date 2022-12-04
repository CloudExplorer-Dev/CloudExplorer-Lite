package org.openstack4j.model.identity.v2;

import org.openstack4j.model.ModelEntity;

/**
 * A User from a Tenant perspective
 *
 * @author Jeremy Unruh
 */
public interface TenantUser extends ModelEntity {

    /**
     * @return the user identifier
     */
    String getId();

    /**
     * @return the name of the user
     */
    String getName();

    /**
     * @return the email address of the user
     */
    String getEmail();

    /**
     * @return true, if the user is enabled (active)
     */
    boolean isEnabled();

}
