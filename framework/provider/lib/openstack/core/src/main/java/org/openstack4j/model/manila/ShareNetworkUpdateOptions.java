package org.openstack4j.model.manila;

/**
 * Options used to update a share network.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ShareNetworkUpdateOptions {
    private String name;
    private String description;

    public static ShareNetworkUpdateOptions create() {
        return new ShareNetworkUpdateOptions();
    }

    /**
     * Update the share network name.
     *
     * @param name the share network name
     * @return ShareNetworkUpdateOptions
     */
    public ShareNetworkUpdateOptions name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Update the share network description.
     *
     * @param description the share network description
     * @return ShareNetworkUpdateOptions
     */
    public ShareNetworkUpdateOptions description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return the share network name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the share network description
     */
    public String getDescription() {
        return description;
    }
}
