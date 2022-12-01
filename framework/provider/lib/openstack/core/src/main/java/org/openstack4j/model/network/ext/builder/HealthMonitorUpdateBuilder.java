package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.HealthMonitorUpdate;

/**
 * A builder to update a vip
 *
 * @author liujunpeng
 */
public interface HealthMonitorUpdateBuilder extends Builder<HealthMonitorUpdateBuilder, HealthMonitorUpdate> {
    /**
     * @param dealy The time, in seconds, between sending probes to members.
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder delay(Integer delay);

    /**
     * @param urlPath Path portion of URI that will be probed if type is HTTP(S).
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder urlPath(String urlPath);

    /**
     * @param expectedCodes Expected HTTP codes for a passing HTTP(S) monitor.
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder expectedCodes(String expectedCodes);

    /**
     * @param httpMethod GET/PUT/POST
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder httpMethod(String httpMethod);

    /**
     * @param maxRetries Maximum consecutive health probe tries.
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder maxRetries(Integer maxRetries);

    /**
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN).
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder adminStateUp(boolean adminStateUp);

    /**
     * @param timeout Time in seconds to timeout each probe.
     * @return HealthMonitorUpdateBuilder
     */
    public HealthMonitorUpdateBuilder timeout(Integer timeout);

}
