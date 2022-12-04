package org.openstack4j.model.manila;

/**
 * Options used to update a share snapshot.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ShareSnapshotUpdateOptions {
    private String displayName;
    private String displayDescription;

    public static ShareSnapshotUpdateOptions create() {
        return new ShareSnapshotUpdateOptions();
    }

    /**
     * Update the share snapshot name.
     *
     * @param displayName the share snapshot name
     * @return ShareSnapshotUpdateOptions
     */
    public ShareSnapshotUpdateOptions displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Update the share snapshot description.
     *
     * @param displayDescription the share snapshot description
     * @return ShareSnapshotUpdateOptions
     */
    public ShareSnapshotUpdateOptions displayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
        return this;
    }

    /**
     * @return the share snapshot name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return the share snapshot description.
     */
    public String getDisplayDescription() {
        return displayDescription;
    }
}
