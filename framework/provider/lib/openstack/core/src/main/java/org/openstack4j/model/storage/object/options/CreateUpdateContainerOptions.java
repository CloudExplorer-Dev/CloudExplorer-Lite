package org.openstack4j.model.storage.object.options;

import com.google.common.collect.Maps;
import org.openstack4j.openstack.storage.object.functions.MetadataToHeadersFunction;

import java.util.Map;

import static org.openstack4j.model.storage.object.SwiftHeaders.*;

/**
 * Options for creating or updating a container
 *
 * @author Jeremy Unruh
 */
public final class CreateUpdateContainerOptions {

    private Map<String, String> headers = Maps.newHashMap();

    private CreateUpdateContainerOptions() {
    }

    /**
     * Creates a new CreateUpdateContainerOptions
     *
     * @return CreateUpdateContainerOptions
     */
    public static final CreateUpdateContainerOptions create() {
        return new CreateUpdateContainerOptions();
    }

    /**
     * Adds additional headers to the create or update request
     *
     * @param headers the headers to add
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions headers(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * Sets the metadata against the container being created or updated
     *
     * @param metadata the metadata to set
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions metadata(Map<String, String> metadata) {
        this.headers.putAll(MetadataToHeadersFunction.create(CONTAINER_METADATA_PREFIX).apply(metadata));
        return this;
    }

    /**
     * Enables versioning on this container. The value is the name of another container.
     * You must UTF-8-encode and then URL-encode the name before you include it in the header.
     * To disable versioning, set the header to an empty string.
     *
     * @param containerName the container name of the other container
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions versionsLocation(String containerName) {
        add(VERSIONS_LOCATION, containerName);
        return this;
    }

    /**
     * Enables history-location versioning mode on this container. The value is the name of another container.
     * You must UTF-8-encode and then URL-encode the name before you include it in the header.
     * To disable versioning, set the header to an empty string.
     *
     * @param containerName the container name of the other container
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions historyLocation(String containerName) {
        add(HISTORY_LOCATION, containerName);
        return this;
    }

    /**
     * Sets the read ACL (if supported) to allow public access
     *
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions accessAnybodyRead() {
        add(CONTAINER_READ, CONTAINER_ACL_ANYBODY_READ);
        return this;
    }

    /**
     * Sets the read ACL (if supported)
     *
     * @param acl the read access control list
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions accessRead(String acl) {
        add(CONTAINER_READ, acl);
        return this;
    }

    /**
     * Sets the write ACL (if supported)
     *
     * @param acl the write access control list
     * @return CreateUpdateContainerOptions
     */
    public CreateUpdateContainerOptions accessWrite(String acl) {
        add(CONTAINER_WRITE, acl);
        return this;
    }

    public Map<String, String> getOptions() {
        return headers;
    }

    private void add(String key, String value) {
        this.headers.put(key, value);
    }
}
