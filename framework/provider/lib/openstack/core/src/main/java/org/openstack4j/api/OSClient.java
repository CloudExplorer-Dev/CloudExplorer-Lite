package org.openstack4j.api;

import org.openstack4j.api.artifact.ArtifactService;
import org.openstack4j.api.barbican.BarbicanService;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.dns.v2.DNSService;
import org.openstack4j.api.exceptions.RegionEndpointNotFoundException;
import org.openstack4j.api.gbp.GbpService;
import org.openstack4j.api.heat.HeatService;
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
import org.openstack4j.api.telemetry.TelemetryService;
import org.openstack4j.api.trove.TroveService;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.api.workflow.WorkflowService;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Token;

import java.util.Map;
import java.util.Set;

/**
 * A client which has been identified. Any calls spawned from this session will
 * automatically utilize the original authentication that was successfully
 * validated and authorized
 *
 * @author Jeremy Unruh
 */
public interface OSClient<T extends OSClient<T>> {

    /**
     * Specifies the region that should be used for further invocations with
     * this client. If the region is invalid or doesn't exists execution errors
     * will occur when invoking API calls and a
     * {@link RegionEndpointNotFoundException} will be thrown
     *
     * @param region the region to use
     * @return OSClient for method chaining
     */
    T useRegion(String region);

    /**
     * Removes the current region making all calls no longer resolving to region
     * (if originally set otherwise no-op)
     *
     * @return OSClient for method chaining
     */
    T removeRegion();

    /**
     * Changes the Perspective for the current Session (Client)
     *
     * @param perspective the new perspective
     * @return OSClient for method chaining
     */
    T perspective(Facing perspective);

    /**
     * Passes the Headers for the current Session(Client)
     *
     * @param headers the headers to use for keystone tokenless
     * @return OSClient for method chaining
     */
    T headers(Map<String, String> headers);

    /**
     * Gets the supported services. A set of ServiceTypes will be returned
     * identifying the OpenStack services installed and supported
     *
     * @return the supported services
     */
    Set<ServiceType> getSupportedServices();

    /**
     * Determines if the Compute (Nova) service is supported
     *
     * @return true, if supports compute
     */
    boolean supportsCompute();

    /**
     * Determines if the Identity (Keystone) service is supported
     *
     * @return true, if supports identity
     */
    boolean supportsIdentity();

    /**
     * Determines if the Network (Neutron) service is supported
     *
     * @return true, if supports network
     */
    boolean supportsNetwork();

    /**
     * Determines if the Image (Glance) service is supported
     *
     * @return true, if supports image
     */
    boolean supportsImage();

    /**
     * Determines if the Orchestration (Heat) service is supported
     *
     * @return true if supports Heat
     */
    boolean supportsHeat();

    /**
     * Determines if the App Catalog (Murano) service is supported
     *
     * @return true if supports Murano
     */
    boolean supportsMurano();

    /**
     * Determines if the Block Storage (Cinder) service is supported
     *
     * @return true if supports Block Storage
     */
    boolean supportsBlockStorage();

    /**
     * Determines if the Object Storage (Swift) service is supported
     *
     * @return true if supports Object Storage
     */
    boolean supportsObjectStorage();

    /**
     * Determines if the Telemetry (Ceilometer) service is supported
     *
     * @return true if supports Telemetry
     */
    boolean supportsTelemetry();

    /**
     * Determines if the Shared File Systems (Manila) service is supported
     *
     * @return true if supports Shared File Systems
     */
    boolean supportsShare();

    /**
     * Gets the current endpoint of the Identity service
     *
     * @return the endpoint
     */
    String getEndpoint();

    /**
     * Returns the Compute Service API
     *
     * @return the compute service
     */
    ComputeService compute();

    /**
     * Returns the Networking Service API
     *
     * @return the networking service
     */
    NetworkingService networking();

    /**
     * Returns the Placement Service API
     *
     * @return the placement service
     */
    PlacementService placement();

    /**
     * Returns the SFC Service API
     *
     * @return the Service Function Chain Service API
     */
    ServiceFunctionChainService sfc();

    /**
     * Returns the Load Balancer Service API
     *
     * @return the Load Balancer service
     */
    OctaviaService octavia();

    /**
     * Returns the Artifact Service API
     *
     * @return the artifact service
     */
    ArtifactService artifact();

    /**
     * Returns the Tacker Service API
     *
     * @return the tacker service
     */
    TackerService tacker();

    /**
     * Returns the Block Storage Service API
     *
     * @return the block storage service
     */
    BlockStorageService blockStorage();

    /**
     * Returns the Object Storage Service API
     *
     * @return the object storage service
     */
    ObjectStorageService objectStorage();

    /**
     * Returns the Image Service API
     *
     * @return the image service
     */
    ImageService images();

    /**
     * Returns the Image V2 Service API
     *
     * @return the image v2 service
     */
    org.openstack4j.api.image.v2.ImageService imagesV2();

    /**
     * Returns the Telemetry Service API
     *
     * @return the telemetry service
     */
    TelemetryService telemetry();

    /**
     * Returns the Shared File Systems API
     *
     * @return the share service
     */
    ShareService share();

    /**
     * Returns the Heat Service API
     *
     * @return the Heat service
     */
    HeatService heat();

    /**
     * Returns the Murano Service API
     *
     * @return the Murano service
     */
    AppCatalogService murano();

    /**
     * Returns the Sahara Service API
     *
     * @return the Sahara service
     */
    SaharaService sahara();

    /**
     * Returns the Workflow Service API
     *
     * @return the Workflow service
     */
    WorkflowService workflow();

    /**
     * Returns the Magnum Service API
     *
     * @return the Magnum Service
     */
    MagnumService magnum();

    /**
     * Returns the Gbp Service API
     *
     * @return the Gbp service
     */
    GbpService gbp();

    /**
     * Returns the Senlin Service API
     *
     * @return the Senlin service
     */
    SenlinService senlin();

    /**
     * Returns the Trove Service API
     *
     * @return the Trove service
     */
    TroveService trove();

    /**
     * Returns the Barbican Service API
     *
     * @return the Barbican service
     */
    BarbicanService barbican();

    /**
     * Returns the DNS Service API
     *
     * @return the DNS service
     */
    DNSService dns();

    /**
     * OpenStack4j Client which authenticates against version V2
     */
    interface OSClientV2 extends OSClient<OSClient.OSClientV2> {

        /**
         * Returns the Identity V2 Access object assigned during authentication
         *
         * @return the Access object
         */
        Access getAccess();

        /**
         * Returns the Identity Service API V2
         *
         * @return the identity service version 2
         */
        org.openstack4j.api.identity.v2.IdentityService identity();

    }

    /**
     * OpenStack4j Client which authenticates against version V3
     */
    interface OSClientV3 extends OSClient<OSClient.OSClientV3> {


        /**
         * Gets the token that was assigned during authorization
         *
         * @return the authentication token
         */
        Token getToken();

        /**
         * Returns the Identity Service API V3
         *
         * @return the identity service version 3
         */
        org.openstack4j.api.identity.v3.IdentityService identity();

    }
}
