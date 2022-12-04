package org.openstack4j.api.compute;

import org.openstack4j.api.compute.ext.*;
import org.openstack4j.common.RestService;
import org.openstack4j.model.common.Extension;

import java.util.List;

/**
 * Compute (Nova) Operations API
 *
 * @author Jeremy Unruh
 */
public interface ComputeService extends RestService {

    /**
     * Server Actions Service API
     *
     * @return server actions service
     */
    ServerActionsService events();

    /**
     * Flavor Service API
     *
     * @return the flavor service
     */
    FlavorService flavors();

    /**
     * Image Service API
     *
     * @return the image service
     * @see org.openstack4j.api.image.v2.ImageService
     * @deprecated These APIs are proxy calls to the Image service. Nova has deprecated all the proxy APIs and users should use the native APIs instead. All the Image services proxy APIs except image metadata APIs will fail with a 404 starting from microversion 2.36. The image metadata APIs will fail with a 404 starting from microversion 2.39.
     */
    @Deprecated
    ComputeImageService images();

    /**
     * Hypervisor Service Extension API
     *
     * @return the hypervisor service
     */
    HypervisorService hypervisors();

    /**
     * ZoneService Extension API
     *
     * @return the zones service
     */
    ZoneService zones();

    /**
     * Server Service API
     *
     * @return the server service
     */
    ServerService servers();

    /**
     * Quota-Set Service API
     *
     * @return the quota set service
     */
    QuotaSetService quotaSets();

    /**
     * Compute Os-Host API
     *
     * @return the compute os-host service
     * @see ServerService
     * @deprecated The os-hosts API is deprecated as of the 2.43 microversion. Requests made with microversion >= 2.43 will result in a 404 error. To list and show host details, use the Hypervisors (os-hypervisors) API. To enable or disable a service, use the Compute services (os-services) API. There is no replacement for the shutdown, startup, reboot, or maintenance_mode actions as those are system-level operations which should be outside of the control of the compute service.
     */
    @Deprecated
    HostService host();

    /**
     * Floating IP Service API
     *
     * @return the floating-ip service
     * @see org.openstack4j.api.networking.NetFloatingIPService
     * @deprecated This API is a proxy call to the Network service. Nova has deprecated all the proxy APIs and users should use the native APIs instead. This API will fail with a 404 starting from microversion 2.36.
     */
    @Deprecated
    ComputeFloatingIPService floatingIps();

    /**
     * Security Groups Extension API
     *
     * @return the security groups service
     * @see org.openstack4j.api.networking.SecurityGroupService
     * @deprecated These APIs are proxy calls to the Network service. Nova has deprecated all the proxy APIs and users should use the native APIs instead. These will fail with a 404 starting from microversion 2.36
     */
    @Deprecated
    ComputeSecurityGroupService securityGroups();

    /**
     * Keypair Management Service
     *
     * @return the keypair service
     */
    KeypairService keypairs();

    /**
     * Administrators only - provides in-progress migrations for a region or cell
     *
     * @return the migration service
     */
    MigrationService migrations();

    /**
     * @return a list of Extensions that have been added against the Compute service
     * @deprecated https://specs.openstack.org/openstack/nova-specs/specs/newton/implemented/api-no-more-extensions.html
     */
    @Deprecated
    List<? extends Extension> listExtensions();

    /**
     * ServerGroup Management Service
     *
     * @return ServerGroup service
     */
    ServerGroupService serverGroups();

    /**
     * Service that manages the extension 'os-floating-ip-dns'
     *
     * @return the floating IP DNS Service
     * @deprecated Since these APIs are only implemented for nova-network, they are deprecated. These will fail with a 404 starting from microversion 2.36. They were removed in the 18.0.0 Rocky release.
     */
    @Deprecated
    FloatingIPDNSService floatingIPDNS();

    /**
     * Host Aggregates Management Service
     */
    HostAggregateService hostAggregates();

    /**
     * Server Tag Management Service
     *
     * @return ServerTag Service
     */
    ServerTagService serverTags();

    /**
     * Compute services service
     *
     * @return ServicesService
     */
    ServicesService services();
}
