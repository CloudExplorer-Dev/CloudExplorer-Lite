package org.openstack4j.api.storage;

import org.openstack4j.common.RestService;
import org.openstack4j.model.storage.object.SwiftAccount;

import java.util.Map;

/**
 * The Object Storage Account based services
 *
 * @author Jeremy Unruh
 */
public interface ObjectStorageAccountService extends RestService {

    /**
     * Gets the {@link SwiftAccount}.
     *
     * @return The {@link SwiftAccount} object.
     */
    SwiftAccount get();

    /**
     * Creates or updates the {@link SwiftAccount} metadata.
     *
     * @param metadata the metadata to create or update.
     * @return true if the metadata was created or updated successfully,
     * false if not
     */
    boolean updateMetadata(Map<String, String> metadata);

    /**
     * Deletes the {@link SwiftAccount} metadata.
     *
     * @param metadata the metadata to delete.
     * @return true if the metadata was deleted successfully,
     * false if not
     */
    boolean deleteMetadata(Map<String, String> metadata);

    /**
     * Replaces the temporary URL key for the {@link SwiftAccount}
     *
     * @param temporaryUrlKey the temporary URL key
     * @return true if the update was successful
     */
    boolean updateTemporaryUrlKey(String temporaryUrlKey);
}
