package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * A Wrapper for Rest calls to set/update Meta Data
 *
 * @author Jeremy Unruh
 */
public class MetaDataWrapper implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("metadata")
    Map<String, String> metadata;

    public MetaDataWrapper() {
    }

    private MetaDataWrapper(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * Wraps the given MetaData map into the wrapper
     *
     * @param metadata the metadata
     * @return the meta data wrapper
     */
    public static MetaDataWrapper wrap(Map<String, String> metadata) {
        return new MetaDataWrapper(metadata);
    }

    /**
     * @return the meta data
     */
    public Map<String, String> getMetaData() {
        return metadata;
    }

}
