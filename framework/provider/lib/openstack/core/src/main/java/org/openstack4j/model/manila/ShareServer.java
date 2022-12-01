package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * A share server is created by multi-tenant back-end drivers where shares are hosted.
 * For example, with the <code>generic</code> driver, shares are hosted on Compute VMs.
 * With the <code>cluster_mode</code> driver from NetApp, shares are hosted on virtual storage servers,
 * also known as Vservers or SVMs.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareServer extends ModelEntity {
    /**
     * @return the share server ID
     */
    String getId();

    /**
     * @return the project ID
     */
    String getProjectId();

    /**
     * @return the share server status
     */
    Status getStatus();

    /**
     * @return the back-end details for a server
     */
    Map<String, String> getBackendDetails();

    /**
     * @return the UUID of a share network that is associated with the share server
     */
    String getShareNetworkId();

    /**
     * @return the name of a share network that is associated with the share server
     */
    String getShareNetworkName();

    /**
     * @return the share server host name or IP address
     */
    String getHost();

    /**
     * @return the date and time stamp when the share server was created
     */
    String getCreatedAt();

    /**
     * @return the date and time stamp when the share server was updated
     */
    String getUpdatedAt();

    enum Status {
        ACTIVE, ERROR, DELETING, CREATING;

        @JsonCreator
        public static Status value(String v) {
            return valueOf(v.toUpperCase());
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
