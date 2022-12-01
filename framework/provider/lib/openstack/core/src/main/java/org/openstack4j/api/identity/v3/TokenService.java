package org.openstack4j.api.identity.v3;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.identity.v3.Token;

import java.util.List;

/**
 * Identity V3 Token operations
 */
public interface TokenService extends RestService {

    /***
     * Validates and shows information for a token.
     *
     * @param tokenId the identifier of the token that is subject to be checked
     * @return the token if valid
     */
    Token get(String tokenId);

    /**
     * Validates a token.
     *
     * @param tokenId the identifier of the token that is subject to be checked
     * @return the ActionResponse
     */
    ActionResponse check(String tokenId);

    /**
     * Revokes a token.
     *
     * @param tokenId the identifier of the token that is going to be deleted
     * @return the ActionResponse
     */
    ActionResponse delete(String tokenId);

    /**
     * Get service catalog for specified token
     *
     * @param tokenId the identifier of the token, of which the catalog of services is requested
     * @return the service catalog for the token provided in the request
     */
    List<? extends Service> getServiceCatalog(String tokenId);

    /**
     * Get available project scopes for specified token
     *
     * @param tokenId the identifier of the token in question
     * @return list of projects that are available to be scoped to
     */
    List<? extends Project> getProjectScopes(String tokenId);

    /**
     * Get available domain scopes for specified token
     *
     * @param tokenId the identifier of the token in question
     * @return list of domains that are available to be scoped to
     */
    List<? extends Domain> getDomainScopes(String tokenId);


}
