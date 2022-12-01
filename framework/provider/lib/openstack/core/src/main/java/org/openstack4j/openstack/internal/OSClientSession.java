package org.openstack4j.openstack.internal;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.openstack4j.api.Apis;
import org.openstack4j.api.EndpointTokenProvider;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.artifact.ArtifactService;
import org.openstack4j.api.barbican.BarbicanService;
import org.openstack4j.api.client.CloudProvider;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.dns.v2.DNSService;
import org.openstack4j.api.gbp.GbpService;
import org.openstack4j.api.heat.HeatService;
import org.openstack4j.api.identity.EndpointURLResolver;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.api.magnum.MagnumService;
import org.openstack4j.api.manila.ShareService;
import org.openstack4j.api.murano.v1.AppCatalogService;
import org.openstack4j.api.networking.NetworkingService;
import org.openstack4j.api.networking.ext.ServiceFunctionChainService;
import org.openstack4j.api.octavia.OctaviaService;
import org.openstack4j.api.placement.PlacementService;
import org.openstack4j.api.sahara.SaharaService;
import org.openstack4j.api.senlin.SenlinService;
import org.openstack4j.api.storage.BlockStorageService;
import org.openstack4j.api.storage.ObjectStorageService;
import org.openstack4j.api.tacker.TackerService;
import org.openstack4j.api.telemetry.TelemetryAodhService;
import org.openstack4j.api.telemetry.TelemetryService;
import org.openstack4j.api.trove.TroveService;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.api.workflow.WorkflowService;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.identity.AuthVersion;
import org.openstack4j.model.identity.URLResolverParams;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.identity.internal.DefaultEndpointURLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

/**
 * A client which has been identified. Any calls spawned from this session will
 * automatically utilize the original authentication that was successfully
 * validated and authorized
 *
 * @author Jeremy Unruh
 */
public abstract class OSClientSession<R, T extends OSClient<T>> implements EndpointTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OSClientSession.class);
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal<OSClientSession> sessions = new ThreadLocal<OSClientSession>();

    Config config;
    Facing perspective;
    String region;
    Set<ServiceType> supports;
    CloudProvider provider;
    EndpointURLResolver fallbackEndpointUrlResolver = new DefaultEndpointURLResolver();
    private Map<String, String> headers;

    @SuppressWarnings("rawtypes")
    public static OSClientSession getCurrent() {
        return sessions.get();
    }

    @SuppressWarnings("unchecked")
    @VisibleForTesting
    public R useConfig(Config config) {
        this.config = config;
        return (R) this;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T useRegion(String region) {
        this.region = region;
        return (T) this;
    }

    /**
     * {@inheritDoc}
     */
    public T removeRegion() {
        return useRegion(null);
    }

    /**
     * @return the current perspective
     */
    public Facing getPerspective() {
        return perspective;
    }

    /**
     * @return the original client configuration associated with this session
     */
    public Config getConfig() {
        return config;
    }

    /**
     * {@inheritDoc}
     */
    public ComputeService compute() {
        return Apis.getComputeServices();
    }

    /**
     * {@inheritDoc}
     */
    public NetworkingService networking() {
        return Apis.getNetworkingServices();
    }

    /**
     * {@inheritDoc}
     */
    public PlacementService placement() {
        return Apis.getPlacementServices();
    }

    /**
     * {@inheritDoc}
     */
    public ServiceFunctionChainService sfc() {
        return Apis.getSfcServices();
    }

    /**
     * {@inheritDoc}
     */
    public OctaviaService octavia() {
        return Apis.getOctaviaService();
    }

    /**
     * {@inheritDoc}
     */
    public ArtifactService artifact() {
        return Apis.getArtifactServices();
    }

    /**
     * {@inheritDoc}
     */
    public TackerService tacker() {
        return Apis.getTackerServices();
    }

    /**
     * {@inheritDoc}
     */
    public ImageService images() {
        return Apis.getImageService();
    }

    public org.openstack4j.api.image.v2.ImageService imagesV2() {
        return Apis.getImageV2Service();
    }

    /**
     * {@inheritDoc}
     */
    public BlockStorageService blockStorage() {
        return Apis.get(BlockStorageService.class);
    }

    /**
     * {@inheritDoc}
     */
    public TelemetryService telemetry() {
        return Apis.get(TelemetryService.class);
    }

    /**
     * {@inheritDoc}
     */
    public ShareService share() {
        return Apis.get(ShareService.class);
    }

    /**
     * {@inheritDoc}
     */
    public HeatService heat() {
        return Apis.getHeatServices();
    }

    /**
     * {@inheritDoc}
     */
    public AppCatalogService murano() {
        return Apis.getMuranoServices();
    }

    /**
     * {@inheritDoc}
     */
    public MagnumService magnum() {
        return Apis.getMagnumService();
    }

    /**
     * {@inheritDoc}
     */
    public SenlinService senlin() {
        return Apis.getSenlinServices();
    }

    /**
     * {@inheritDoc}
     */
    public ObjectStorageService objectStorage() {
        return Apis.get(ObjectStorageService.class);
    }

    /**
     * {@inheritDoc}
     */
    public SaharaService sahara() {
        return Apis.getSaharaServices();
    }

    /**
     * {@inheritDoc}
     */
    public WorkflowService workflow() {
        return Apis.getWorkflowServices();
    }

    /**
     * {@inheritDoc}
     */
    public BarbicanService barbican() {
        return Apis.getBarbicanServices();
    }

    /**
     * {@inheritDoc}
     */
    public DNSService dns() {
        return Apis.getDNSService();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T perspective(Facing perspective) {
        this.perspective = perspective;
        return (T) this;
    }

    public CloudProvider getProvider() {
        return (provider == null) ? CloudProvider.UNKNOWN : provider;
    }

    /**
     * {@inheritDoc}
     */
    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsCompute() {
        return getSupportedServices().contains(ServiceType.COMPUTE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsIdentity() {
        return getSupportedServices().contains(ServiceType.IDENTITY);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsNetwork() {
        return getSupportedServices().contains(ServiceType.NETWORK);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsPlacement() {
        return getSupportedServices().contains(ServiceType.PLACEMENT);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsImage() {
        return getSupportedServices().contains(ServiceType.IMAGE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsHeat() {
        return getSupportedServices().contains(ServiceType.ORCHESTRATION);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsMurano() {
        return getSupportedServices().contains(ServiceType.APP_CATALOG);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsBlockStorage() {
        return getSupportedServices().contains(ServiceType.BLOCK_STORAGE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsObjectStorage() {
        return getSupportedServices().contains(ServiceType.OBJECT_STORAGE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsTelemetry() {
        return getSupportedServices().contains(ServiceType.TELEMETRY);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsTelemetry_aodh() {
        return getSupportedServices().contains(ServiceType.TELEMETRY_AODH);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsShare() {
        return getSupportedServices().contains(ServiceType.SHARE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsTrove() {
        return getSupportedServices().contains(ServiceType.DATABASE);
    }

    /**
     * {@inheritDoc}
     */
    public boolean supportsDNS() {
        return getSupportedServices().contains(ServiceType.DNS);
    }

    public Set<ServiceType> getSupportedServices() {
        return null;
    }

    public AuthVersion getAuthVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public GbpService gbp() {
        return Apis.getGbpServices();
    }

    /**
     *
     */
    public TroveService trove() {
        return Apis.getTroveServices();
    }

    public static class OSClientSessionV2 extends OSClientSession<OSClientSessionV2, OSClientV2> implements OSClientV2 {

        Access access;

        private OSClientSessionV2(Access access, String endpoint, Facing perspective, CloudProvider provider, Config config) {
            this.access = access;
            this.config = config;
            this.perspective = perspective;
            this.provider = provider;
            sessions.set(this);
        }

        private OSClientSessionV2(Access access, OSClientSessionV2 parent, String region) {
            this.access = parent.access;
            this.perspective = parent.perspective;
            this.region = region;
        }

        public static OSClientSessionV2 createSession(Access access) {
            return new OSClientSessionV2(access, access.getEndpoint(), null, null, null);
        }

        public static OSClientSessionV2 createSession(Access access, Facing perspective, CloudProvider provider, Config config) {
            return new OSClientSessionV2(access, access.getEndpoint(), perspective, provider, config);
        }

        @Override
        public Access getAccess() {
            return access;
        }

        @Override
        public String getEndpoint() {
            return access.getEndpoint();
        }

        @Override
        public AuthVersion getAuthVersion() {
            return AuthVersion.V2;
        }

        private String addNATIfApplicable(String url) {
            if (config != null && config.isBehindNAT()) {
                try {
                    URI uri = new URI(url);
                    return url.replace(uri.getHost(), config.getEndpointNATResolution());
                } catch (URISyntaxException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            return url;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEndpoint(ServiceType service) {

            final EndpointURLResolver eUrlResolver = (config != null && config.getEndpointURLResolver() != null) ? config.getEndpointURLResolver() : fallbackEndpointUrlResolver;

            return addNATIfApplicable(eUrlResolver.findURLV2(URLResolverParams
                    .create(access, service)
                    .resolver(config != null ? config.getV2Resolver() : null)
                    .perspective(perspective)
                    .region(region)));
        }

        @Override
        public String getTokenId() {
            return access.getToken().getId();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public org.openstack4j.api.identity.v2.IdentityService identity() {
            return Apis.getIdentityV2Services();
        }

        @Override
        public Set<ServiceType> getSupportedServices() {
            if (supports == null)
                supports = Sets.immutableEnumSet(Iterables.transform(access.getServiceCatalog(),
                        new org.openstack4j.openstack.identity.v2.functions.ServiceToServiceType()));
            return supports;
        }

    }

    public static class OSClientSessionV3 extends OSClientSession<OSClientSessionV3, OSClientV3> implements OSClientV3 {

        protected String reqId;
        Token token;

        private OSClientSessionV3(Token token, String endpoint, Facing perspective, CloudProvider provider, Config config) {
            this.token = token;
            this.config = config;
            this.perspective = perspective;
            this.provider = provider;
            sessions.set(this);
        }

        private OSClientSessionV3(Token token, OSClientSessionV3 parent, String region) {
            this.token = parent.token;
            this.perspective = parent.perspective;
            this.region = region;
        }

        public static OSClientSessionV3 createSession(Token token) {
            return new OSClientSessionV3(token, token.getEndpoint(), null, null, null);
        }

        public static OSClientSessionV3 createSession(Token token, Facing perspective, CloudProvider provider, Config config) {
            return new OSClientSessionV3(token, token.getEndpoint(), perspective, provider, config);
        }

        public String getXOpenstackRequestId() {
            return reqId;
        }

        @Override
        public Token getToken() {
            return token;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEndpoint() {
            return token.getEndpoint();
        }

        @Override
        public AuthVersion getAuthVersion() {
            return AuthVersion.V3;
        }

        private String addNATIfApplicable(String url) {
            if (config != null && config.isBehindNAT()) {
                try {
                    URI uri = new URI(url);
                    return url.replace(uri.getHost(), config.getEndpointNATResolution());
                } catch (URISyntaxException e) {
                    LoggerFactory.getLogger(OSClientSessionV3.class).error(e.getMessage(), e);
                }
            }
            return url;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEndpoint(ServiceType service) {

            final EndpointURLResolver eUrlResolver = (config != null && config.getEndpointURLResolver() != null) ? config.getEndpointURLResolver() : fallbackEndpointUrlResolver;

            return addNATIfApplicable(eUrlResolver.findURLV3(URLResolverParams
                    .create(token, service)
                    .resolver(config != null ? config.getResolver() : null)
                    .perspective(perspective)
                    .region(region)));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getTokenId() {
            return token.getId();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public org.openstack4j.api.identity.v3.IdentityService identity() {
            return Apis.getIdentityV3Services();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Set<ServiceType> getSupportedServices() {
            if (supports == null)
                supports = Sets.immutableEnumSet(Iterables.transform(token.getCatalog(),
                        new org.openstack4j.openstack.identity.v3.functions.ServiceToServiceType()));
            return supports;
        }

        @Override
        public TelemetryService telemetry() {
            return Apis.get(TelemetryAodhService.class);
        }

    }


}
