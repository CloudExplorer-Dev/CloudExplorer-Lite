package org.openstack4j.openstack.client;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.client.CloudProvider;
import org.openstack4j.api.client.IOSClientBuilder;
import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.api.types.Facing;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.openstack.common.Auth;
import org.openstack4j.openstack.identity.v2.domain.Credentials;
import org.openstack4j.openstack.identity.v2.domain.RaxApiKeyCredentials;
import org.openstack4j.openstack.identity.v2.domain.TokenAuth;
import org.openstack4j.openstack.identity.v3.domain.KeystoneAuth;
import org.openstack4j.openstack.identity.v3.domain.KeystoneAuth.AuthScope;
import org.openstack4j.openstack.internal.OSAuthenticator;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Builder definitions for creating a Client
 *
 * @author Jeremy Unruh
 */
public abstract class OSClientBuilder<R, T extends IOSClientBuilder<R, T>> implements IOSClientBuilder<R, T> {

    Config config;
    String endpoint;
    String user;
    String password;
    Facing perspective;
    CloudProvider provider = CloudProvider.UNKNOWN;

    @SuppressWarnings("unchecked")
    @Override
    public T withConfig(Config config) {
        this.config = config;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T provider(CloudProvider provider) {
        this.provider = provider;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T credentials(String user, String password) {
        this.user = user;
        this.password = password;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T endpoint(String endpoint) {
        this.endpoint = endpoint;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T perspective(Facing perspective) {
        this.perspective = perspective;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T useNonStrictSSLClient(boolean useNonStrictSSL) {
        if (config == null)
            config = Config.newConfig().withSSLVerificationDisabled();
        return (T) this;
    }

    public static class ClientV2 extends OSClientBuilder<OSClientV2, IOSClientBuilder.V2> implements IOSClientBuilder.V2 {

        String tenantName;
        String tenantId;
        String tokenId;
        boolean raxApiKey;

        @Override
        public ClientV2 tenantName(String tenantName) {
            this.tenantName = tenantName;
            return this;
        }

        @Override
        public ClientV2 tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        @Override
        public ClientV2 raxApiKey(boolean raxApiKey) {
            this.raxApiKey = raxApiKey;
            return this;
        }

        @Override
        public OSClientV2 authenticate() throws AuthenticationException {
            if (tokenId != null) {
                checkArgument(tenantName != null || tenantId != null,
                        "TenantId or TenantName is required when using Token Auth");
                return (OSClientV2) OSAuthenticator.invoke(new TokenAuth(tokenId, tenantName, tenantId), endpoint, perspective,
                        config, provider);
            }

            if (raxApiKey) {
                return (OSClientV2) OSAuthenticator.invoke(new RaxApiKeyCredentials(user, password), endpoint, perspective, config, provider);
            }

            return (OSClientV2) OSAuthenticator.invoke(new Credentials(user, password, tenantName, tenantId), endpoint, perspective, config, provider);
        }

        @Override
        public ClientV2 token(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

    }

    public static class ClientV3 extends OSClientBuilder<OSClientV3, IOSClientBuilder.V3> implements IOSClientBuilder.V3 {

        Identifier domain;
        AuthScope scope;
        String tokenId;

        @Override
        public ClientV3 domainName(String domainName) {
            this.domain = Identifier.byName(domainName);
            return this;
        }

        @Override
        public ClientV3 domainId(String domainId) {
            this.domain = Identifier.byId(domainId);
            return this;
        }

        @Override
        public ClientV3 credentials(String user, String password, Identifier domain) {
            this.user = user;
            this.password = password;
            this.domain = domain;
            return this;
        }

        @Override
        public ClientV3 token(String tokenId) {
            this.tokenId = tokenId;
            return this;
        }

        @Override
        public OSClientV3 authenticate() throws AuthenticationException {
            // token based authentication
            if (tokenId != null && tokenId.length() > 0)
                if (scope != null) {
                    return (OSClientV3) OSAuthenticator.invoke(new KeystoneAuth(tokenId, scope), endpoint, perspective, config, provider);
                } else {
                    return (OSClientV3) OSAuthenticator.invoke(new KeystoneAuth(tokenId), endpoint, perspective, config, provider);
                }
            // credential based authentication
            if (user != null && user.length() > 0)
                return (OSClientV3) OSAuthenticator.invoke(new KeystoneAuth(user, password, domain, scope), endpoint, perspective, config, provider);
            // Use tokenless auth finally
            return (OSClientV3) OSAuthenticator.invoke(new KeystoneAuth(scope, Auth.Type.TOKENLESS), endpoint, perspective, config, provider);
        }

        @Override
        public ClientV3 scopeToProject(Identifier project, Identifier domain) {
            this.scope = AuthScope.project(project, domain);
            return this;
        }

        @Override
        public ClientV3 scopeToProject(Identifier project) {
            this.scope = AuthScope.project(project);
            return this;
        }

        @Override
        public ClientV3 scopeToDomain(Identifier domain) {
            this.scope = AuthScope.domain(domain);
            return this;
        }

    }
}
