package org.openstack4j.openstack.storage.object.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.api.Apis;
import org.openstack4j.api.storage.ObjectStorageContainerService;
import org.openstack4j.model.storage.object.SwiftContainer;

import java.util.Map;

/**
 * Represents an OpenStack Swift Container which holds Objects
 *
 * @author Jeremy Unruh
 */
public class SwiftContainerImpl implements SwiftContainer {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String name;

    @JsonProperty("count")
    private int objectCount;

    @JsonProperty("bytes")
    private long totalSize;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getObjectCount() {
        return objectCount;
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }

    @Override
    public Map<String, String> getMetadata() {
        return Apis.get(ObjectStorageContainerService.class).getMetadata(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("count", objectCount).add("total size", totalSize)
                .toString();
    }
}
