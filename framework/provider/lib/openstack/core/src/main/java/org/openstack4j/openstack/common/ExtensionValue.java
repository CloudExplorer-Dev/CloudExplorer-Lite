package org.openstack4j.openstack.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.common.Extension;
import org.openstack4j.model.common.Link;

import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Represents an Extension which adds additional functionality to the OpenStack API
 *
 * @author Jeremy Unruh
 * @deprecated https://specs.openstack.org/openstack/nova-specs/specs/newton/implemented/api-no-more-extensions.html
 */
@Deprecated
public class ExtensionValue implements Extension {

    private static final long serialVersionUID = 1L;
    String name;
    URI namespace;
    String alias;
    Date updated;
    String description;
    List<GenericLink> links;

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public URI getNamespace() {
        return namespace;
    }

    /**
     * {@inheritDoc}
     */
    public String getAlias() {
        return alias;
    }

    /**
     * {@inheritDoc}
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    public Date getUpdated() {
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public List<? extends Link> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Extension.class).omitNullValues()
                .add("name", name)
                .add("namespace", namespace)
                .add("description", description)
                .add("alias", alias)
                .add("updated", updated)
                .add("links", links)
                .addValue("\n")
                .toString();
    }

    @JsonRootName("extensions")
    public static class ExtensionList extends ListResult<ExtensionValue> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("values")
        private List<ExtensionValue> list;

        public List<ExtensionValue> value() {
            return list;
        }
    }

    public static class Extensions extends ListResult<ExtensionValue> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("extensions")
        private List<ExtensionValue> list;

        public List<ExtensionValue> value() {
            return list;
        }
    }
}
