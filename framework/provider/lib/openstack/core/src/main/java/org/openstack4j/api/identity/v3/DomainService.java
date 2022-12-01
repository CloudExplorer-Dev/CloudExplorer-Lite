package org.openstack4j.api.identity.v3;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Domain;

import java.util.List;

/**
 * Identity V3 Domain Service
 */
public interface DomainService extends RestService {

    /**
     * Creates a new domain
     *
     * @param domain the Domain to create
     * @return the new domain
     */
    Domain create(Domain domain);

    /**
     * Creates a new domain
     *
     * @param name        the name of the new domain
     * @param description the description of the new domain
     * @param enabled     the enabled status of the new domain
     * @return the new domain
     */
    Domain create(String name, String description, boolean enabled);

    /**
     * Updates an existing domain
     *
     * @param domain the domain set to update
     * @return the updated domain
     */
    Domain update(Domain domain);

    /**
     * Get detailed information on a domain by id
     *
     * @param domainId the domain identifier
     * @return the domain
     */
    Domain get(String domainId);

    /**
     * Get detailed information on a domain by name
     *
     * @param domainName the domain name
     * @return the domain
     */
    List<? extends Domain> getByName(String domainName);

    /**
     * Deletes a domain by id
     *
     * @param domainId the domain id
     * @return the ActionResponse
     */
    ActionResponse delete(String domainId);

    /**
     * lists all domains the current token has access to
     *
     * @return list of domains
     */
    List<? extends Domain> list();

}
