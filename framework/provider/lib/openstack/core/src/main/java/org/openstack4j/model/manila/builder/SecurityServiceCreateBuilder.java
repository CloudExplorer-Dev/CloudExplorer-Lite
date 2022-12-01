package org.openstack4j.model.manila.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.manila.SecurityService;
import org.openstack4j.model.manila.SecurityServiceCreate;

/**
 * Builds a {@link org.openstack4j.model.manila.SecurityServiceCreate}.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface SecurityServiceCreateBuilder
        extends Buildable.Builder<SecurityServiceCreateBuilder, SecurityServiceCreate> {
    /**
     * Set the security service type
     *
     * @param type the security service type
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder type(SecurityService.Type type);

    /**
     * Set the security service name
     *
     * @param name the security service name
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder name(String name);

    /**
     * Set the security service description
     *
     * @param description the security service description
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder description(String description);

    /**
     * Set the security service DNS IP
     *
     * @param dnsIp the security service DNS IP
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder dnsIp(String dnsIp);

    /**
     * Set the security service user
     *
     * @param user the security service user
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder user(String user);

    /**
     * Set the security service password
     *
     * @param password the security service password
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder password(String password);

    /**
     * Set the security service domain
     *
     * @param domain the security service domain
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder domain(String domain);

    /**
     * Set the security service server
     *
     * @param server the security service server
     * @return SecurityServiceCreateBuilder
     */
    SecurityServiceCreateBuilder server(String server);
}
