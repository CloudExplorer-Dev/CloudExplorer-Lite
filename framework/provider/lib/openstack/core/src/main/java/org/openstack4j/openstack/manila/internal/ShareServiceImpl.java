package org.openstack4j.openstack.manila.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.manila.*;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Extension;
import org.openstack4j.model.manila.*;
import org.openstack4j.openstack.common.ExtensionValue;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.manila.domain.ManilaAvailabilityZone;
import org.openstack4j.openstack.manila.domain.ManilaLimits;
import org.openstack4j.openstack.manila.domain.ManilaService;
import org.openstack4j.openstack.manila.domain.ManilaShare;
import org.openstack4j.openstack.manila.domain.actions.ServiceAction;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Shared File Systems API (Manila)
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ShareServiceImpl extends BaseShareServices implements ShareService {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Extension> listExtensions() {
        return get(ExtensionValue.Extensions.class, uri("/extensions")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Limits limits() {
        return get(ManilaLimits.class, uri("/limits")).execute();
    }

    /**
     * {@inheritDoc}
     */
    public SharesService shares() {
        return Apis.get(SharesService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecurityServiceService securityServices() {
        return Apis.get(SecurityServiceService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareSnapshotService shareSnapshots() {
        return Apis.get(ShareSnapshotService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareNetworkService shareNetworks() {
        return Apis.get(ShareNetworkService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareServerService shareServers() {
        return Apis.get(ShareServerService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareInstanceService shareInstances() {
        return Apis.get(ShareInstanceService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareTypeService shareTypes() {
        return Apis.get(ShareTypeService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchedulerStatsService schedulerStats() {
        return Apis.get(SchedulerStatsService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Service> services() {
        return get(ManilaService.Services.class, uri("/os-services")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManilaService.ServiceStatus enableService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);

        return put(ManilaService.ServiceStatus.class, uri("/os-services/enable"))
                .entity(ServiceAction.enable(binary, host))
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ManilaService.ServiceStatus disableService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);

        return put(ManilaService.ServiceStatus.class, uri("/os-services/disable"))
                .entity(ServiceAction.disable(binary, host))
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends AvailabilityZone> availabilityZones() {
        return get(ManilaAvailabilityZone.AvailabilityZones.class, uri("/os-availability-zone")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Share manageShare(ShareManage shareManage) {
        checkNotNull(shareManage);

        return post(ManilaShare.class, uri("/os-share-manage"))
                .entity(shareManage)
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse unmanageShare(String shareId) {
        checkNotNull(shareId);

        return ToActionResponseFunction.INSTANCE.apply(
                post(Void.class, uri("/os-share-unmanage/%s/unmanage", shareId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaSetService quotaSets() {
        return Apis.get(QuotaSetService.class);
    }
}
