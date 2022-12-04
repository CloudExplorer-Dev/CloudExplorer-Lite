package org.openstack4j.api.octavia;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.HealthMonitorV2;
import org.openstack4j.model.octavia.HealthMonitorV2Update;

import java.util.List;
import java.util.Map;

/**
 * Octavia V2 healthmonitor Extention API
 *
 * @author wei
 */
public interface HealthMonitorV2Service extends RestService {
    /**
     * List all healthMonitor  that the current tenant has access to
     *
     * @return list of all healthMonitorV2
     */
    List<? extends HealthMonitorV2> list();

    /**
     * Returns list of healthMonitorV2 filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends HealthMonitorV2> list(Map<String, String> filteringParams);

    /**
     * Get the specified healthMonitorV2 by ID
     *
     * @param healthMonitorId the healthMonitorV2 identifier
     * @return the healthMonitorV2 or null if not found
     */
    HealthMonitorV2 get(String healthMonitorId);

    /**
     * Delete the specified healthMonitor by ID
     *
     * @param healthMonitorId the healthMonitorV2 identifier
     * @return the action response
     */
    ActionResponse delete(String healthMonitorId);

    /**
     * Create a healthMonitorV2
     *
     * @return HealthMonitorV2
     */
    HealthMonitorV2 create(HealthMonitorV2 healthMonitor);

    /**
     * Update a healthMonitorV2
     *
     * @param healthMonitorId the healthMonitorV2 identifier
     * @param healthMonitor   HealthMonitorV2Update
     * @return HealthMonitorV2
     */
    HealthMonitorV2 update(String healthMonitorId, HealthMonitorV2Update healthMonitor);
}
