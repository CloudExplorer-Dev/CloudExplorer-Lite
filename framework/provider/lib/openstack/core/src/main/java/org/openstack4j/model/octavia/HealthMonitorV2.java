package org.openstack4j.model.octavia;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.builder.HealthMonitorV2Builder;
import org.openstack4j.openstack.octavia.domain.ListItem;

import java.util.List;

/**
 * A healthMonitor of a Lbaas V2 pool
 *
 * @author wei
 */
public interface HealthMonitorV2 extends ModelEntity, Buildable<HealthMonitorV2Builder> {
    /**
     * @return id the healthMonitor identifier
     */
    String getId();

    /**
     * @return the ID of the project/tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return type The type of probe sent by the load balancer to verify the
     * member state, which is TCP, HTTP, or HTTPS.
     */
    HealthMonitorType getType();

    /**
     * @return delay The time, in seconds, between sending probes to members.
     */
    Integer getDelay();

    /**
     * @return timeout The maximum number of seconds for a monitor to wait for a
     * connection to be established before it times out. This value must
     * be less than the delay value.
     */
    Integer getTimeout();

    /**
     * @return maxRetries Number of allowed connection failures before changing
     * the status of the member to INACTIVE. A valid value is from 1 to
     * 10.
     */
    Integer getMaxRetries();

    /**
     * @return maxRetriesDown Number of allowed connection failures before changing
     * the status of the member to INACTIVE. A valid value is from 1 to
     * 10.
     */
    Integer getMaxRetriesDown();

    /**
     * Optional.
     *
     * @return httpMethod The HTTP method that the monitor uses for requests.
     */
    String getHttpMethod();

    /**
     * Optional.
     *
     * @return urlPath The HTTP path of the request sent by the monitor to test
     * the health of a member. Must be a string beginning with a forward
     * slash (/).
     */
    String getUrlPath();

    /**
     * Optional.
     *
     * @return expectedCodes Expected HTTP codes for a passing HTTP(S) monitor.
     */
    String getExpectedCodes();

    /**
     * Optional.
     *
     * @return adminstateup The administrative state of the health monitor,
     * which is up (true) or down (false). Default true.
     */
    boolean isAdminStateUp();

    /**
     * The pools that this health monitor will monitor.
     *
     * @return pools
     */
    List<ListItem> getPools();

}
