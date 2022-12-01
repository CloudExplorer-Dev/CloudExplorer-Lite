package org.openstack4j.model.identity.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v2.builder.RoleBuilder;

/**
 * A permission scheme a user is assigned when performing specific operations.  A role includes a set of rights and previleges. Any user who is assigned
 * the role inherits these traits.
 *
 * @author Jeremy Unruh
 */
public interface Role extends ModelEntity, Buildable<RoleBuilder> {

    /**
     * @return the id of the role
     */
    String getId();

    /**
     * @return the name of the role
     */
    String getName();

    /**
     * @return the service id of the role or null, if not present
     */
    String getServiceId();

    /**
     * @return the tenant id of the role or null, if not present
     */
    String getTenantId();

    /**
     * @return the description of the role
     */
    String getDescription();

    /**
     * @return true if the role is enabled
     */
    boolean isEnabled();

}
