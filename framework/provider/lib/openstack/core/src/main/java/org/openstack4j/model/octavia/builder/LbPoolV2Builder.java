package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.octavia.LbMethod;
import org.openstack4j.model.octavia.LbPoolV2;
import org.openstack4j.model.octavia.Protocol;
import org.openstack4j.model.octavia.SessionPersistence;

/**
 * A Builder to create a lbpoolV2
 *
 * @author wei
 */
public interface LbPoolV2Builder extends Buildable.Builder<LbPoolV2Builder, LbPoolV2> {
    /**
     * @param projectId the ID of the project/tenant that owns the resource
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder projectId(String projectId);

    /**
     * Optional
     *
     * @param name Pool name. Does not have to be unique.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder name(String name);

    /**
     * Optional
     *
     * @param description Description for the pool.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder description(String description);

    /**
     * @param protocol The protocol of the VIP address. A valid value is TCP, HTTP,
     *                 or HTTPS.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder protocol(Protocol protocol);

    /**
     * @param lbMethod The load-balancer algorithm, which is round-robin,
     *                 least-connections, and so on. This value, which must be
     *                 supported, is dependent on the load-balancer provider. Round
     *                 robin must be supported.
     *                 Must be one of ROUND_ROBIN, LEAST_CONNECTIONS, or SOURCE_IP.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder lbMethod(LbMethod lbMethod);

    /**
     * Optional
     *
     * @param sessionPersistence Default value empty dictionary
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder sessionPersistence(SessionPersistence sessionPersistence);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the lb pool, which is up (true) or
     *                     down (false). Default value true.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder adminStateUp(boolean adminStateUp);

    /**
     * The listener in which this pool will become the default pool.
     * There can only be on default pool for a listener.
     *
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder listenerId(String listenerId);

    /**
     * The load balancer this pool will be provisioned on. A tenant can only create pool on
     * load balancers authorized by policy (e.g. her own load balancers).
     *
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder loadbalancerId(String loadbalancerId);
}
