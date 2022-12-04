package org.openstack4j.api.manila;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Extension;
import org.openstack4j.model.manila.*;
import org.openstack4j.openstack.manila.domain.ManilaService;

import java.util.List;

/**
 * Shared File Systems API (Manila)
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareService extends RestService {
    /**
     * @return a list of available Shared File Systems API extensions
     * @deprecated https://docs.openstack.org/api-ref/compute/?expanded=#extensions-extensions-deprecated
     */
    @Deprecated
    List<? extends Extension> listExtensions();

    /**
     * @return the resource limitations allowed for the tenant
     */
    Limits limits();

    /**
     * @return service which provides methods to manage shares
     */
    SharesService shares();

    /**
     * @return service which provides methods to manage security services
     */
    SecurityServiceService securityServices();

    /**
     * @return service which provides methods to manage share snapshots
     */
    ShareSnapshotService shareSnapshots();

    /**
     * @return service which provides methods to manage share networks
     */
    ShareNetworkService shareNetworks();

    /**
     * @return service which provides methods to manage share servers
     */
    ShareServerService shareServers();

    /**
     * @return service which provides methods to manage share instances
     */
    ShareInstanceService shareInstances();

    /**
     * @return service which provides methods to manage share types
     */
    ShareTypeService shareTypes();

    /**
     * @return service which provides methods to query scheduler stats
     */
    SchedulerStatsService schedulerStats();

    /**
     * Lists all services.
     *
     * @return a list of all services
     */
    List<? extends Service> services();

    /**
     * Enables a service.
     *
     * @param binary the name of the service binary that you want to enable
     * @param host   the host name of the service that you want to enable
     * @return the status of the enabled service
     */
    ManilaService.ServiceStatus enableService(String binary, String host);

    /**
     * Disables a service.
     *
     * @param binary the name of the service binary that you want to disable
     * @param host   the host name of the service that you want to disable
     * @return the status of the disabled service
     */
    ManilaService.ServiceStatus disableService(String binary, String host);

    /**
     * Lists all availability zones.
     *
     * @return a list of all availability zones
     */
    List<? extends AvailabilityZone> availabilityZones();

    /**
     * Configures Shared File Systems to manage a share.
     *
     * @param shareManage the share to manage
     * @return the managed share
     */
    Share manageShare(ShareManage shareManage);

    /**
     * Configures Shared File Systems to stop managing a share.
     *
     * @param shareId the share ID
     * @return the action response
     */
    ActionResponse unmanageShare(String shareId);

    /**
     * @return service which provides methods to manage quota sets
     */
    QuotaSetService quotaSets();
}
