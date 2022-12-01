package org.openstack4j.model.common;

import org.openstack4j.model.ModelEntity;

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
public interface Extension extends ModelEntity {

    /**
     * @return the name of the extension
     */
    String getName();

    /**
     * @return the namespace of this extension
     */
    URI getNamespace();

    /**
     * @return the alias which is used within RestFul and other operational calls
     */
    String getAlias();

    /**
     * @return when the extension was last updated
     */
    Date getUpdated();

    /**
     * @return the description of this extension
     */
    String getDescription();

    /**
     * @return the additional information and documentation based links for this extension
     */
    List<? extends Link> getLinks();
}
