package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.ShareCreateBuilder;

import java.util.Map;

/**
 * Object used to create new shares.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareCreate extends ModelEntity, Buildable<ShareCreateBuilder> {
    /**
     * @return The Shared File Systems protocol
     */
    Share.Protocol getShareProto();

    /**
     * @return The share size, in GBs
     */
    Integer getSize();

    /**
     * @return The share name
     */
    String getName();

    /**
     * @return The share description
     */
    String getDescription();

    /**
     * @return The share name
     */
    String getDisplayName();

    /**
     * @return The share description.
     */
    String getDisplayDescription();

    /**
     * @return The UUID of the share type
     */
    String getShareType();

    /**
     * @return The volume type
     */
    String getVolumeType();

    /**
     * @return The UUID of the snapshot from which to create the share
     */
    String getSnapshotId();

    /**
     * @return The level of visibility for the share
     */
    Boolean isPublic();

    /**
     * @return One or more metadata key and value pairs as a dictionary of strings
     */
    Map<String, String> getMetadata();

    /**
     * @return The UUID of the share network
     */
    String getShareNetworkId();

    /**
     * @return The UUID of the consistency group where the share was created
     */
    String getConsistencyGroupId();

    /**
     * @return The availability zone
     */
    String getAvailabilityZone();
}
