package org.openstack4j.openstack.octavia.internal;

import org.openstack4j.api.octavia.HealthMonitorV2Service;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.HealthMonitorV2;
import org.openstack4j.model.octavia.HealthMonitorV2Update;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.octavia.domain.OctaviaHealthMonitorV2;
import org.openstack4j.openstack.octavia.domain.OctaviaHealthMonitorV2.HealthMonitorsV2;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Octavia) lbaas v2 health monitor operations
 *
 * @author wei
 */
public class HealthMonitorV2ServiceImpl extends BaseOctaviaServices implements HealthMonitorV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HealthMonitorV2> list() {
        return get(HealthMonitorsV2.class, uri("/lbaas/healthmonitors")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HealthMonitorV2> list(Map<String, String> filteringParams) {
        Invocation<HealthMonitorsV2> req = get(HealthMonitorsV2.class, uri("/lbaas/healthmonitors"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2 get(String healthMonitorId) {
        checkNotNull(healthMonitorId);
        return get(OctaviaHealthMonitorV2.class, uri("/lbaas/healthmonitors/%s", healthMonitorId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String healthMonitorId) {
        checkNotNull(healthMonitorId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class, uri("/lbaas/healthmonitors/%s", healthMonitorId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2 create(HealthMonitorV2 healthMonitor) {
        checkNotNull(healthMonitor);
        return post(OctaviaHealthMonitorV2.class, uri("/lbaas/healthmonitors")).entity(healthMonitor).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2 update(String healthMonitorId,
                                  HealthMonitorV2Update healthMonitor) {
        checkNotNull(healthMonitorId);
        checkNotNull(healthMonitor);
        return put(OctaviaHealthMonitorV2.class, uri("/lbaas/healthmonitors/%s", healthMonitorId)).entity(healthMonitor).execute();
    }
}
