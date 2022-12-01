package org.openstack4j.model.identity.v3;

import com.google.common.collect.SortedSetMultimap;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.AuthStore;
import org.openstack4j.model.identity.AuthVersion;

import java.util.Date;
import java.util.List;

/**
 * V3 token model
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#tokens-v3">API reference</a>
 */
public interface Token extends ModelEntity {

    /**
     * @return the id of the token
     */
    String getId();

    /**
     * sets the id of the token from response header value
     *
     * @param id the token id
     */
    void setId(String id);

    /**
     * @return the catalog of the token
     */
    List<? extends Service> getCatalog();

    /**
     * @return the timestamp when the token expires
     */
    Date getExpires();

    /**
     * @return the timestamp when the token was issued
     */
    Date getIssuedAt();

    /**
     * @return the project of the token
     */
    Project getProject();

    /**
     * @return the domain of the token
     */
    Domain getDomain();

    /**
     * @return the User of the token
     */
    User getUser();

    /**
     * @return the authentication store
     */
    AuthStore getCredentials();

    /**
     * @return the endpoint
     */
    String getEndpoint();

    /**
     * @return the list of roles
     */
    List<? extends Role> getRoles();

    /**
     * @return the list of audit identifiers
     */
    List<String> getAuditIds();

    /**
     * @return the methods of the token
     */
    List<String> getMethods();

    /**
     * @return the authentication version
     */
    AuthVersion getVersion();

    /**
     * @return the internal UUID used for cache lookups
     */
    String getCacheIdentifier();

    /**
     * A Lazy loading Aggregated Service Catalog Mapping.  The key is a stripped version service type or name with a collection
     * of Services sorted by version
     *
     * @return sorted aggregate service catalog
     */
    SortedSetMultimap<String, Service> getAggregatedCatalog();

}
