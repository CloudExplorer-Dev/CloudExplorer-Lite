package org.openstack4j.openstack.identity.internal;

import com.google.common.base.Optional;
import com.google.common.collect.SortedSetMultimap;
import org.openstack4j.api.exceptions.RegionEndpointNotFoundException;
import org.openstack4j.api.identity.EndpointURLResolver;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.URLResolverParams;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v2.Endpoint;
import org.openstack4j.model.identity.v3.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves an Endpoint URL based on the Service Type and Facing perspective
 *
 * @author Jeremy Unruh
 */
public class DefaultEndpointURLResolver implements EndpointURLResolver {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultEndpointURLResolver.class);
    private static final Map<Key, String> CACHE = new ConcurrentHashMap<Key, String>();
    private static boolean LEGACY_EP_HANDLING = Boolean.getBoolean(LEGACY_EP_RESOLVING_PROP);
    private String publicHostIP;

    @Override
    public String findURLV2(URLResolverParams p) {
        if (p.type == null) {
            return p.access.getEndpoint();
        }

        Key key = Key.of(p.access.getCacheIdentifier(), p.type, p.perspective, p.region);
        String url = CACHE.get(key);

        if (url != null)
            return url;

        url = resolveV2(p);

        if (url != null) {
            return url;
        } else if (p.region != null)
            throw RegionEndpointNotFoundException.create(p.region, p.type.getServiceName());

        return p.access.getEndpoint();
    }

    @Override
    public String findURLV3(URLResolverParams p) {

        if (p.type == null) {
            return p.token.getEndpoint();
        }

        Key key = Key.of(p.token.getCacheIdentifier(), p.type, p.perspective, p.region);

        String url = CACHE.get(key);

        if (url != null)
            return url;

        url = resolveV3(p);

        if (url != null) {
            CACHE.put(key, url);
            return url;
        } else if (p.region != null)
            throw RegionEndpointNotFoundException.create(p.region, p.type.getServiceName());

        return p.token.getEndpoint();
    }

    private String resolveV2(URLResolverParams p) {
        SortedSetMultimap<String, ? extends Access.Service> catalog = p.access.getAggregatedCatalog();
        SortedSet<? extends Access.Service> services = catalog.get(p.type.getServiceName());

        if (services.isEmpty()) {
            services = catalog.get(p.type.getType());
        }

        if (!services.isEmpty()) {
            Access.Service sc = p.getV2Resolver().resolveV2(p.type, services);
            for (org.openstack4j.model.identity.v2.Endpoint ep : sc.getEndpoints()) {
                if (p.region != null && !p.region.equalsIgnoreCase(ep.getRegion()))
                    continue;

                if (sc.getServiceType() == ServiceType.NETWORK) {
                    sc.getEndpoints().get(0).toBuilder().type(sc.getServiceType().name());
                }

                if (p.perspective == null)
                    return getEndpointURL(p.access, ep);

                switch (p.perspective) {
                    case ADMIN:
                        return ep.getAdminURL().toString();
                    case INTERNAL:
                        return ep.getInternalURL().toString();
                    case PUBLIC:
                    default:
                        return ep.getPublicURL().toString();
                }
            }
        } else {
            //if no catalog returned, if is identity service, just return endpoint
            if (ServiceType.IDENTITY.equals(p.type)) {
                return p.access.getEndpoint();
            }
        }
        return null;
    }

    private String resolveV3(URLResolverParams p) {
        Token token = p.token;

        //in v3 api, if user has no default project, and token is unscoped, no catalog will be returned
        //then if service is Identity service, should directly return the endpoint back
        if (token.getCatalog() == null) {
            if (ServiceType.IDENTITY.equals(p.type)) {
                return token.getEndpoint();
            } else {
                return null;
            }
        }

        for (org.openstack4j.model.identity.v3.Service service : token.getCatalog()) {
            if (p.type == ServiceType.forName(service.getType()) || p.type == ServiceType.forName(service.getName())) {
                if (p.perspective == null) {
                    p.perspective = Facing.PUBLIC;
                }

                for (org.openstack4j.model.identity.v3.Endpoint ep : service.getEndpoints()) {

                    if (matches(ep, p))
                        return ep.getUrl().toString();
                }
            }
        }


        return null;
    }

    /**
     * Returns <code>true</code> for any endpoint that matches a given
     * {@link URLResolverParams}.
     */
    private boolean matches(org.openstack4j.model.identity.v3.Endpoint endpoint, URLResolverParams p) {
        boolean matches = endpoint.getIface() == p.perspective;
        if (Optional.fromNullable(p.region).isPresent()) {
            matches &= endpoint.getRegion().equals(p.region);
        }
        return matches;
    }

    /**
     * Gets the endpoint url.
     *
     * @param access   the current access data source
     * @param endpoint the endpoint
     * @return the endpoint url
     */
    private String getEndpointURL(Access access, Endpoint endpoint) {
        if (LEGACY_EP_HANDLING) {
            if (endpoint.getAdminURL() != null) {
                if (getPublicIP(access) != null && !getPublicIP(access).equals(endpoint.getAdminURL().getHost())) {
                    return endpoint.getAdminURL().toString().replaceAll(endpoint.getAdminURL().getHost(),
                            getPublicIP(access));
                }
                return endpoint.getAdminURL().toString();
            }
        }
        return endpoint.getPublicURL().toString();
    }

    private String getPublicIP(Access access) {
        if (publicHostIP == null) {
            try {
                publicHostIP = new URI(access.getEndpoint()).getHost();
            } catch (URISyntaxException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return publicHostIP;
    }

    private static final class Key {
        private final String uid;
        private final ServiceType type;
        private final Facing perspective;

        private Key(String uid, ServiceType type, Facing perspective) {
            this.type = type;
            this.perspective = perspective;
            this.uid = uid;
        }

        static Key of(String uid, ServiceType type, Facing perspective, String region) {
            return new Key((region == null) ? uid : uid + region, type, perspective);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((perspective == null) ? 0 : perspective.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            result = prime * result + ((uid == null) ? 0 : uid.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (perspective != other.perspective)
                return false;
            if (type != other.type)
                return false;
            if (uid == null) {
                if (other.uid != null)
                    return false;
            } else if (!uid.equals(other.uid))
                return false;
            return true;
        }
    }

}
