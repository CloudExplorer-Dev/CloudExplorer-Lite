package org.openstack4j.model.identity.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.User;

/**
 * A Builder which creates an Identity User
 *
 * @author Jeremy Unruh
 */
public interface UserBuilder extends Builder<UserBuilder, User> {

    /**
     * @see User#getName()
     */
    UserBuilder name(String name);

    /**
     * ID should only ever be set if the user already exists and this is used for update based actions
     *
     * @param id the user id
     * @return this for method chaining
     */
    UserBuilder id(String id);

    /**
     * Sets the initial password for the user
     *
     * @param password the password to set
     * @return this builder
     */
    UserBuilder password(String password);

    /**
     * @see User#getEmail()
     */
    UserBuilder email(String email);

    /**
     * @see User#isEnabled()
     */
    UserBuilder enabled(boolean enabled);

    /**
     * @see User#getTenantId()
     */
    UserBuilder tenantId(String tenantId);

    /**
     * Accepts an existing tenant and uses the tenant's id
     *
     * @see User#getTenantId()
     */
    UserBuilder tenant(Tenant tenant);

}
