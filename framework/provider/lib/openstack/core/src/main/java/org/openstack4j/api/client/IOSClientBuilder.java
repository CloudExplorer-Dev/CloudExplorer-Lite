package org.openstack4j.api.client;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.api.types.Facing;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;

/**
 * OpenStack4j Client Builder
 *
 * @author Jeremy Unruh
 */
public interface IOSClientBuilder<R, T extends IOSClientBuilder<R, T>> {

    /**
     * Associates the given configuration with this Client
     *
     * @param config OpenStack4j configuration options
     * @return self for method chaining
     */
    T withConfig(Config config);

    /**
     * The authentication credentials
     *
     * @param userId   the user id to authenticate with
     * @param password the password to authenticate with
     * @return self for method chaining
     */
    T credentials(String userId, String password);

    /**
     * The identity endpoint to connect to
     *
     * @param endpoint the endpoint URL of the identity service
     * @return self for method chaining
     */
    T endpoint(String endpoint);

    /**
     * The OpenStack cloud provider which helps determine compatibility within
     * requests
     *
     * @param provider the cloud provider
     * @return self for method chaining
     */
    T provider(CloudProvider provider);

    /**
     * Allows for a specific network perspective to be used. For example to only
     * use AdminURL Endpoints you would want to set the {@link Facing#ADMIN} as
     * a facing perspective.
     * <p>
     * NOTE: If you choose PUBLIC some features may not work that are normally
     * admin based configuration/functionality. If you normally are not using
     * these features PUBLIC works fine in most cases.
     *
     * @param perspective the network facing perspective
     * @return self for method chaining
     */
    T perspective(Facing perspective);

    /**
     * DEPRECATED: Use {@link #applyConfig(Config)} to configure SSL policies
     * <p>
     * In some private environments self signed certificates are used. If you
     * are using HTTPS and using self-signed cerificates then set this to true.
     * Otherwise the default strict hostname and properly signed validation
     * based client will be used.
     *
     * @param useNonStrictSSL true if an HTTPS self-signed environment
     * @return self for method chaining
     */
    @Deprecated
    T useNonStrictSSLClient(boolean useNonStrictSSL);

    /**
     * Attempts to connect, authenticated and obtain an authorization access
     * entity which contains a token, service catalog and endpoints from the
     * controller. As a result a client will be returned encapsulating the
     * authorized access and corresponding API access
     *
     * @return the authenticated client
     * @throws AuthenticationException if the credentials or default tenant is
     *                                 invalid
     */
    R authenticate() throws AuthenticationException;

    /**
     * OpenStack4j Client builder which authenticates against version V3
     */
    public interface V2 extends IOSClientBuilder<OSClientV2, V2> {

        /**
         * The tenant/project to authenticate as
         *
         * @param tenantName the tenant/project name
         * @return self for method chaining
         */
        V2 tenantName(String tenantName);

        /**
         * The tenant/project to authenticate as (some clouds such as HP use
         * tenantId vs tenantName)
         *
         * @param tenantId the tenant/project id
         * @return self for method chaining
         */
        V2 tenantId(String tenantId);

        /**
         * Use Rackspace API Key Authentication Admin Extension to authenticate
         *
         * @param raxApiKey true if enabled
         * @return self for method chaining
         */
        V2 raxApiKey(boolean raxApiKey);

        /**
         * Allows authentication via an existing Token versus
         * {@link #credentials(String, String)}. Note: The
         * {@link #tenantId(String)} or {@link #tenantName(String)} will
         * required when using this method for authentication.
         *
         * @param tokenId the token identifier
         * @return self for method chaining
         */
        V2 token(String tokenId);

    }

    /**
     * OpenStack4j Client builder which authenticates against version V3
     */
    public interface V3 extends IOSClientBuilder<OSClientV3, V3> {

        /**
         * The authentication credentials and default scoped domain
         *
         * @param userName the user name to authenticate with
         * @param password the password to authenticate with
         * @param domain   the domain if using "default scoped"
         * @return self for method chaining
         */
        V3 credentials(String userName, String password, Identifier domain);

        /**
         * DEPRECATED: Please use
         * {@link #credentials(String, String, Identifier)
         * <p>
         * Authenticates against the specified domain name
         *
         * @param domainName the domain name to authenticate against
         * @return self for method chaining
         */
        @Deprecated
        V3 domainName(String domainName);

        /**
         * DEPRECATED: Please use
         * {@link #credentials(String, String, Identifier)}
         * <p>
         * Authenticates against the specified domain identifier
         *
         * @param domainId the domain identifier to authenticate against
         * @return self for method chaining
         */
        @Deprecated
        V3 domainId(String domainId);

        /**
         * A token object. With token authentication, the id uniquely identifies
         * the token. This method is typically used in combination with a
         * request to change authorization scope
         *
         * @param tokenId the token identifier
         * @return self for method chaining
         */
        V3 token(String tokenId);

        /**
         * Scopes the token to a project level
         *
         * @param project the project ID or Name value
         * @param domain  the domain ID or Name value
         * @return self for method chaining
         */
        V3 scopeToProject(Identifier project, Identifier domain);

        /**
         * scopes the token to a project level
         *
         * @param project the project id
         * @return self for method chaining
         */
        V3 scopeToProject(Identifier project);

        /**
         * Scopes the token to a domain level
         *
         * @param domain the domain ID or Name value
         * @return self for method chaining
         */
        V3 scopeToDomain(Identifier domain);

    }

}
