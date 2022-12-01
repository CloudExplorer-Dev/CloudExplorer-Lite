package org.openstack4j.model.identity;

/**
 * An entity which holds enough information in store to re-authenticate at any given time during a session.  This is a generic mapping which provides the common
 * information needed for authentication.  Version dependent attributes can be found via the {@link #unwrap()} call returning the real typed object
 *
 * @author Jeremy Unruh
 */
public interface AuthStore {

    /**
     * @return the version of this authentication store type
     */
    AuthVersion getVersion();

    /**
     * @return the username used to authenticate
     */
    String getUsername();

    /**
     * @return the password used to authenticate
     */
    String getPassword();

    /**
     * If this is a {@link AuthVersion#V2} then this is the tenantId.  If {@link AuthVersion#V3} then this maps to the projectId
     *
     * @return the tenantId (V2) or projectId for V3
     */
    String getId();

    /**
     * If this is a {@link AuthVersion#V2} then this is the tenant name.  If {@link AuthVersion#V3} then this maps to the project name
     *
     * @return the tenant name (V2) or project name for V3
     */
    String getName();

}
