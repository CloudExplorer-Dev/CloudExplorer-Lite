package org.openstack4j.openstack.placement.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.placement.ext.ResourceProvider;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * The resource provider instance
 *
 * @author Jyothi Saroja
 */
public class ExtResourceProvider implements ResourceProvider {

    private static final long serialVersionUID = 1L;

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return uuid;
    }

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
    public String toString() {
        return MoreObjects.toStringHelper(ResourceProvider.class).add("id", uuid).add("name", name).toString();
    }

    public static class ResourceProviders extends ListResult<ExtResourceProvider> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("resource_providers")
        List<ExtResourceProvider> resourceProviders;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<ExtResourceProvider> value() {
            return resourceProviders;
        }
    }
}
