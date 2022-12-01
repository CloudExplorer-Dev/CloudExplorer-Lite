package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.network.ext.HealthMonitorType;
import org.openstack4j.model.network.ext.HealthMonitorV2;

public interface HealthMonitorV2Builder extends Buildable.Builder<HealthMonitorV2Builder, HealthMonitorV2> {
    /**
     * @param tenantId Owner of the VIP. Only an administrative user can specify a
     *                 tenant ID other than its own.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder tenantId(String tenantId);

    /**
     * @param type The type of probe, which is TCP, HTTP, or HTTPS, that is
     *             sent by the health monitor to verify the member state.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder type(HealthMonitorType type);

    /**
     * @param delay The time, in seconds, between sending probes to members.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder delay(Integer delay);

    /**
     * @param timeout Time in seconds to timeout each probe.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder timeout(Integer timeout);

    /**
     * @param maxRetries Maximum consecutive health probe tries.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder maxRetries(Integer maxRetries);

    /**
     * @param poolId Pool that this health monitor is assigned to
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder poolId(String poolId);

    /**
     * Optional
     *
     * @param httpMethod GET/PUT/POST
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder httpMethod(String httpMethod);

    /**
     * Optional
     *
     * @param urlPath Path portion of URI that will be probed if type is HTTP(S).
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder urlPath(String urlPath);

    /**
     * Optional
     *
     * @param expectedCodes Expected HTTP codes for a passing HTTP(S) monitor.
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder expectedCodes(String expectedCodes);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN). Default is true
     * @return HealthMonitorV2Builder
     */
    HealthMonitorV2Builder adminStateUp(boolean adminStateUp);
}
