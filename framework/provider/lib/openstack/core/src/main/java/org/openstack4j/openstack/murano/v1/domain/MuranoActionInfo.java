package org.openstack4j.openstack.murano.v1.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.murano.v1.domain.ActionInfo;

/**
 * @author Nikolay Mahotkin.
 */
public class MuranoActionInfo implements ActionInfo {
    @JsonProperty
    private String name;

    @JsonProperty
    private String title;

    @JsonProperty
    private boolean enabled;

    private String id;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Sets the action ID (It is used only for deserialization).
     *
     * @param id the ID which belongs to corresponding Action
     */
    void setId(String id) {
        this.id = id;
    }
}
