package org.openstack4j.model.manila.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.ShareCreate;

import java.util.Map;

/**
 * Builds a {@link org.openstack4j.model.manila.ShareCreate}.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareCreateBuilder extends Buildable.Builder<ShareCreateBuilder, ShareCreate> {
    /**
     * Set the Shared File Systems protocol.
     *
     * @param shareProto the Shared File Systems protocol
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder shareProto(Share.Protocol shareProto);

    /**
     * Set the share size, in GBs.
     *
     * @param size the share size
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder size(Integer size);

    /**
     * Set the share name.
     *
     * @param name the share name
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder name(String name);

    /**
     * Set the share description.
     *
     * @param description the share description
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder description(String description);

    /**
     * Set the share name.
     *
     * @param displayName the share name
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder displayName(String displayName);

    /**
     * Set the share description.
     *
     * @param displayDescription the share description
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder displayDescription(String displayDescription);

    /**
     * Set the UUID of the share type.
     *
     * @param shareType the share type
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder shareType(String shareType);

    /**
     * Set the volume type.
     *
     * @param volumeType the volume type
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder volumeType(String volumeType);

    /**
     * Set the UUID of the snapshot from which to create the share.
     *
     * @param snapshotId the snapshot ID
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder snapshotId(String snapshotId);

    /**
     * Set the level of visibility for the share.
     *
     * @param isPublic the level of visibility for the share
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder isPublic(Boolean isPublic);

    /**
     * Adds a new metadata item to the share.
     *
     * @param key   the key of the metadata item
     * @param value the value of the metadata item
     *              ShareCreateBuilder
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder addMetadataItem(String key, String value);

    /**
     * One or more metadata key and value pairs as a dictionary of strings.
     *
     * @param metadata the share metadata
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder metadata(Map<String, String> metadata);

    /**
     * Set the UUID of the share network.
     *
     * @param shareNetworkId the share network ID
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder shareNetworkId(String shareNetworkId);

    /**
     * Set the UUID of the consistency group where the share was created.
     *
     * @param consistencyGroupId the consistency group ID
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder consistencyGroupId(String consistencyGroupId);

    /**
     * Set the availability zone.
     *
     * @param availabilityZone the availability zone
     * @return ShareCreateBuilder
     */
    ShareCreateBuilder availabilityZone(String availabilityZone);
}
