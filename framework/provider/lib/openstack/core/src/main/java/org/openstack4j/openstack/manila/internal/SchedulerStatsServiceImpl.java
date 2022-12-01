package org.openstack4j.openstack.manila.internal;

import org.openstack4j.api.manila.SchedulerStatsService;
import org.openstack4j.model.manila.BackendStoragePool;
import org.openstack4j.openstack.manila.domain.ManilaBackendStoragePool;

import java.util.List;

public class SchedulerStatsServiceImpl extends BaseShareServices implements SchedulerStatsService {
    @Override
    public List<? extends BackendStoragePool> pools() {
        return list(false);
    }

    @Override
    public List<? extends BackendStoragePool> poolsDetail() {
        return list(true);
    }

    private List<? extends BackendStoragePool> list(boolean detail) {
        return get(ManilaBackendStoragePool.BackendStoragePools.class, uri("/scheduler-stats/pools" + (detail ? "/detail" : "")))
                .execute()
                .getList();
    }
}
