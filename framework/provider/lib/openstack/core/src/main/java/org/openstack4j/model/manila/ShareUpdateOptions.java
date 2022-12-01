package org.openstack4j.model.manila;

/**
 * Options used to update a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ShareUpdateOptions {
    private String displayName;
    private String displayDescription;
    private Boolean isPublic;

    public static ShareUpdateOptions create() {
        return new ShareUpdateOptions();
    }

    /**
     * Update the share name.
     *
     * @param displayName the share name
     * @return ShareUpdateOptions
     */
    public ShareUpdateOptions displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Update the share description.
     *
     * @param displayDescription the share description
     * @return ShareUpdateOptions
     */
    public ShareUpdateOptions displayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
        return this;
    }

    /**
     * Update the level of visibility for the share.
     *
     * @param isPublic the level of visibility for the share
     * @return ShareUpdateOptions
     */
    public ShareUpdateOptions isPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    /**
     * @return the share name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the share description.
     */
    public String getDisplayDescription() {
        return displayDescription;
    }

    /**
     * @return the level of visibility for the share
     */
    public Boolean isPublic() {
        return isPublic;
    }
}
