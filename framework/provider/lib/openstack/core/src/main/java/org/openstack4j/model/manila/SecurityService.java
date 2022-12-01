package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * A security service stores configuration information for clients for authentication and authorization (AuthN/AuthZ).
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface SecurityService extends ModelEntity {
    /**
     * @return the security service status
     */
    String getStatus();

    /**
     * @return the security service ID
     */
    String getId();

    /**
     * @return the project where the security service was created
     */
    String getProjectId();

    /**
     * @return the security service type
     */
    Type getType();

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

    /**
     * @return the date and time stamp when the security service was created
     */
    String getCreatedAt();

    /**
     * @return the date and time stamp when the security service was updated
     */
    String getUpdatedAt();

    /**
     * @return the share networks this security is added to
     */
    List<String> getShareNetworks();

    enum Type {
        LDAP, KERBEROS, ACTIVE_DIRECTORY;

        @JsonCreator
        public static Type value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
