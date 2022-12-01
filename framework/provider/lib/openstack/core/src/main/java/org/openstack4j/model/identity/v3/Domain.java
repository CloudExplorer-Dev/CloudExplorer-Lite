package org.openstack4j.model.identity.v3;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v3.builder.DomainBuilder;

import java.util.Map;

/**
 * Domain model.
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#domains-v3">API reference</a>
 */
public interface Domain extends ModelEntity, Buildable<DomainBuilder> {

    /**
     * Globally unique domain identifier across all domains.
     *
     * @return the Id of the domain
     */
    String getId();

    /**
     * @return the Description of the domain
     */
    String getDescription();

    /**
     * @return the Name of the domain
     */
    String getName();

    /**
     * Domain options.
     */
    Map<String, String> getOptions();

    /**
     * @return the Links of the domain
     */
    Map<String, String> getLinks();

    /**
     * @return if domain is enabled
     */
    boolean isEnabled();

}
