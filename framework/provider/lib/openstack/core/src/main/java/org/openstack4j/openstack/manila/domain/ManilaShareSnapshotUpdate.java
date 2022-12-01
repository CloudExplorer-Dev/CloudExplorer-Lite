package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.ShareSnapshotUpdateOptions;

/**
 * Object used to update existing share snapshots.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("snapshot")
public class ManilaShareSnapshotUpdate implements ModelEntity {
    private static final long serialVersionUID = 1L;

    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("display_description")
    private String displayDescription;

    private ManilaShareSnapshotUpdate() {
    }

    public static ManilaShareSnapshotUpdate fromOptions(ShareSnapshotUpdateOptions options) {
        ManilaShareSnapshotUpdate shareSnapshotUpdate = new ManilaShareSnapshotUpdate();
        shareSnapshotUpdate.displayName = options.getDisplayName();
        shareSnapshotUpdate.displayDescription = options.getDisplayDescription();

        return shareSnapshotUpdate;
    }
}
