package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPoolV2;
import org.openstack4j.model.network.ext.Protocol;
import org.openstack4j.model.network.ext.SessionPersistence;

/**
 * A Builder to create a lbpoolV2
 *
 * @author emjburns
 */
public interface LbPoolV2Builder extends Buildable.Builder<LbPoolV2Builder, LbPoolV2> {
    /**
     * @param tenantId Owner of the pool. Only an administrative user can specify a
     *                 tenant ID other than its own.
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder tenantId(String tenantId);

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
     * The ID of the load balancer under which this pool will be created.
     * Each load balancer can have zero or more pools associated with it. These pools can be used for L7policies.
     * Either listener_id or loadbalancer_id must be specified.
     *
     * @return LbPoolV2Builder
     */
    LbPoolV2Builder loadBalancerId(String loadBalancerId);
}
