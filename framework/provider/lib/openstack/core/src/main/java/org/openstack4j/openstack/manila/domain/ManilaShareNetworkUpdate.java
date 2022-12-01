package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.ShareNetworkUpdateOptions;

/**
 * Object used to update existing share networks.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_network")
public class ManilaShareNetworkUpdate implements ModelEntity {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;

    private ManilaShareNetworkUpdate() {
    }

    /**
     * Creates a share network from options.
     * This can be used to update an existing share networks.
     *
     * @param options the share network update options
     * @return a share network with the given options
     */
    public static ManilaShareNetworkUpdate fromOptions(ShareNetworkUpdateOptions options) {
        ManilaShareNetworkUpdate shareNetworkUpdate = new ManilaShareNetworkUpdate();
        shareNetworkUpdate.name = options.getName();
        shareNetworkUpdate.description = options.getDescription();

        return shareNetworkUpdate;
    }
}
