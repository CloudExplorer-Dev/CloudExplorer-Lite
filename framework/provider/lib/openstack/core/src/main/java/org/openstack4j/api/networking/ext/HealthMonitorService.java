package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.HealthMonitor;
import org.openstack4j.model.network.ext.HealthMonitorUpdate;

import java.util.List;
import java.util.Map;

/**
 * Networking (Neutron) Lbaas healthmonitor Extension API
 *
 * @author liujunpeng
 */
public interface HealthMonitorService extends RestService {
    /**
     * List all healthMonitor  that the current tenant has access to
     *
     * @return list of all healthMonitor
     */
    List<? extends HealthMonitor> list();

    /**
     * Returns list of healthMonitor filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends HealthMonitor> list(Map<String, String> filteringParams);


    /**
     * Get the specified healthMonitor by ID
     *
     * @param healthMonitorId the healthMonitor identifier
     * @return the healthMonitor or null if not found
     */
    HealthMonitor get(String healthMonitorId);

    /**
     * Delete the specified healthMonitor by ID
     *
     * @param healthMonitorId the healthMonitor identifier
     * @return the action response
     */
    ActionResponse delete(String healthMonitorId);

    /**
     * Create a healthMonitor
     *
     * @return HealthMonitor
     */
    HealthMonitor create(HealthMonitor healthMonitor);

    /**
     * Update a healthMonitor
     *
     * @param healthMonitorId the healthMonitor identifier
     * @param healthMonitor   HealthMonitorUpdate
     * @return HealthMonitor
     */
    HealthMonitor update(String healthMonitorId, HealthMonitorUpdate healthMonitor);
}
