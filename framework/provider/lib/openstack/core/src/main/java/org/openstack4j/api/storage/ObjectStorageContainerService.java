package org.openstack4j.api.storage;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.object.SwiftContainer;
import org.openstack4j.model.storage.object.options.ContainerListOptions;
import org.openstack4j.model.storage.object.options.CreateUpdateContainerOptions;

import java.util.List;
import java.util.Map;

/**
 * Provides access to the OpenStack Object Storage (Swift) Container API features.
 *
 * @author Jeremy Unruh
 */
public interface ObjectStorageContainerService extends RestService {

    /**
     * Listing of all containers associated with the Account (max result size is 10,000);
     *
     * @return List of containers ordered by name
     */
    List<? extends SwiftContainer> list();

    /**
     * Listing of all containers with the associated options used in the query
     *
     * @param options the query based options
     * @return List of containers ordered by name
     */
    List<? extends SwiftContainer> list(ContainerListOptions options);

    /**
     * Creates a new container with the specified {@code name}
     *
     * @param name the name of the new container
     * @return the action response
     */
    ActionResponse create(String name);

    /**
     * Creates a pseudo path aka directory for the specified {@code containerName}
     *
     * @param containerName the name of the container
     * @param path          the path to create
     * @return the ETAG checksum of the new path
     */
    String createPath(String containerName, String path);

    /**
     * Creates a new container with the specified {@code name} with options
     *
     * @param name    the name of the new container
     * @param options the options to use when creating a container
     * @return the action response
     */
    ActionResponse create(String name, CreateUpdateContainerOptions options);

    /**
     * Updates a container with the specified {@code name} with options
     *
     * @param name    the name of the container to update
     * @param options the options to use when updating a container
     * @param the     action response
     */
    ActionResponse update(String name, CreateUpdateContainerOptions options);

    /**
     * Deletes an empty container with the specified {@code name}
     *
     * @param name the name of the container to delete
     * @return the action response (true if successfully deleted)
     */
    ActionResponse delete(String name);

    /**
     * Gets the Metadata for a given container
     *
     * @param name the container name
     * @return Map of key to value metadata
     */
    Map<String, String> getMetadata(String name);

    /**
     * Creates or Updates metadata items for the specified container {@code name}
     *
     * @param name     the name of the container
     * @param metadata the metadata to create/update for this container
     * @return true if the create/update was successful, false if not
     */
    boolean updateMetadata(String name, Map<String, String> metadata);

    /**
     * Deletes metadata items from the specified container {@code name}
     *
     * @param name     the name of the container
     * @param metadata the metadata items to remove from the container's metadata
     * @return true if the removal of items was successful, false if not
     */
    boolean deleteMetadata(String name, Map<String, String> metadata);

}
