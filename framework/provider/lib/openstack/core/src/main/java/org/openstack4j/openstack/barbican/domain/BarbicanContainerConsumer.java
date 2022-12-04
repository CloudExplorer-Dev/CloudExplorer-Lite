package org.openstack4j.openstack.barbican.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.barbican.ContainerConsumer;

public class BarbicanContainerConsumer implements ContainerConsumer {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("URL")
    private String url;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("url", url)
                .toString();
    }
}
