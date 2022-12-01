package org.openstack4j.model.common;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.builder.LinkBuilder;

/**
 * Model for a generic link
 *
 * @author Jeremy Unruh
 */
public interface Link extends ModelEntity, Buildable<LinkBuilder> {

    /**
     * @return the relative URL or null
     */
    String getRel();

    /**
     * @return the href URL
     */
    String getHref();

    /**
     * @return the type of link or null
     */
    String getType();

}
