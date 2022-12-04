package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.SecurityServiceCreateBuilder;

/**
 * Object used to create new security services.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface SecurityServiceCreate extends ModelEntity, Buildable<SecurityServiceCreateBuilder> {
    /**
     * @return the security service type
     */
    SecurityService.Type getType();

    /**
     * @return the security service name
     */
    String getName();

    /**
     * @return the security service description
     */
    String getDescription();

    /**
     * @return the DNS IP address that is used inside the tenant network
     */
    String getDnsIp();

    /**
     * @return the security service user or group name that is used by the tenant
     */
    String getUser();

    /**
     * @return the user password
     */
    String getPassword();

    /**
     * @return the security service domain
     */
    String getDomain();

    /**
     * @return the security service host name or IP address
     */
    String getServer();
}
