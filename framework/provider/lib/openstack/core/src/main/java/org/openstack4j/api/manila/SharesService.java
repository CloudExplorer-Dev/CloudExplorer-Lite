package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.Access;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.ShareCreate;
import org.openstack4j.model.manila.ShareUpdateOptions;
import org.openstack4j.model.manila.actions.AccessOptions;
import org.openstack4j.model.manila.builder.ShareCreateBuilder;
import org.openstack4j.openstack.common.Metadata;

import java.util.List;

/**
 * Shares Service for Manila Shared File Systems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface SharesService extends RestService {
    /**
     * Creates a share.
     *
     * @param shareCreate the share to create
     * @return the created share
     */
    Share create(ShareCreate shareCreate);

    /**
     * Lists all shares.
     *
     * @return list of all shares
     */
    List<? extends Share> list();

    /**
     * Lists all shares, with details.
     *
     * @return list of all shares with details
     */
    List<? extends Share> listDetails();

    /**
     * Shows details for a share.
     *
     * @param shareId the share ID
     * @return the share or null if not found
     */
    Share get(String shareId);

    /**
     * Updates a share.
     *
     * @param shareId            the share ID
     * @param shareUpdateOptions the options to update on the share
     * @return the updated share
     */
    Share update(String shareId, ShareUpdateOptions shareUpdateOptions);

    /**
     * Deletes a share.
     *
     * @param shareId the share ID
     * @return the action response
     */
    ActionResponse delete(String shareId);

    /**
     * Deletes a share.
     *
     * @param shareId            the share ID
     * @param consistencyGroupId the UUID of the consistency group where the share was created
     * @return the action response
     */
    ActionResponse delete(String shareId, String consistencyGroupId);

    /**
     * Shows the metadata for a share.
     *
     * @param shareId the share ID
     * @return the shares metadata
     */
    Metadata getMetadata(String shareId);

    /**
     * Updates the metadata for a share.
     *
     * @param shareId  the share ID
     * @param metadata the metadata to update
     * @return the updated metadata
     */
    Metadata updateMetadata(String shareId, Metadata metadata);

    /**
     * Sets the metadata on a share.
     *
     * @param shareId  the share ID
     * @param metadata the metadata to set
     * @return the updated metadata
     */
    Metadata setMetadata(String shareId, Metadata metadata);

    /**
     * Unsets the metadata on a share.
     *
     * @param shareId     the share ID
     * @param metadataKey the metadata key to unset
     * @return the action response
     */
    ActionResponse unsetMetadata(String shareId, String metadataKey);

    /**
     * Grants access to a share.
     *
     * @param shareId the share ID
     * @param options the options of the granted access
     * @return the granted access
     */
    Access grantAccess(String shareId, AccessOptions options);

    /**
     * Revokes access from a share.
     *
     * @param shareId  the share ID
     * @param accessId the access ID to revoke
     * @return the action response
     */
    ActionResponse revokeAccess(String shareId, String accessId);

    /**
     * List access rules for a share.
     *
     * @param shareId the share ID
     * @return a list fo all access rules for the given share
     */
    List<? extends Access> listAccess(String shareId);

    /**
     * Administrators only. Explicitly updates the state of a share.
     *
     * @param shareId the share ID
     * @param status  the status to set
     * @return the action response
     */
    ActionResponse resetState(String shareId, Share.Status status);

    /**
     * Administrators only. Force-deletes a share in any state.
     *
     * @param shareId the share ID
     * @return the action response
     */
    ActionResponse forceDelete(String shareId);

    /**
     * Increases the size of a share.
     *
     * @param shareId the share ID
     * @param newSize new size of the share, in GBs
     * @return the action response
     */
    ActionResponse extend(String shareId, int newSize);

    /**
     * Shrinks the size of a share.
     *
     * @param shareId the share ID
     * @param newSize the new size of the share, in GBs
     * @return the action response
     */
    ActionResponse shrink(String shareId, int newSize);

    /**
     * @return a builder to create a share
     */
    ShareCreateBuilder shareCreateBuilder();
}
