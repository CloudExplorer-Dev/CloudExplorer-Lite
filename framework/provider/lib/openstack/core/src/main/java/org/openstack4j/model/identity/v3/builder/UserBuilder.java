package org.openstack4j.model.identity.v3.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;

import java.util.Map;

/**
 * A Builder which creates a identity v3 project
 */
public interface UserBuilder extends Builder<UserBuilder, User> {

    /**
     * @see User#getId()
     */
    UserBuilder id(String id);

    /**
     * @see User#getName()
     */
    UserBuilder name(String name);

    /**
     * @see User#getDefaultProjectId()
     */
    UserBuilder defaultProjectId(String defaultProjectId);

    /**
     * @see User#getDescription()
     */
    UserBuilder description(String description);

    /**
     * @see User#getDomainId()
     */
    UserBuilder domainId(String domainId);

    /**
     * Accepts an existing domain and uses its id
     *
     * @see User#getDomain()
     */
    UserBuilder domain(Domain domain);

    /**
     * @see User#getEmail()
     */
    UserBuilder email(String email);

    /**
     * @see User#getLinks()
     */
    UserBuilder links(Map<String, String> links);

    /**
     * @see User#getPassword()
     */
    UserBuilder password(String password);

    /**
     * @see User#isEnabled()
     */
    UserBuilder enabled(boolean enabled);

}
