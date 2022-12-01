package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPool;
import org.openstack4j.model.network.ext.Protocol;

/**
 * A Builder to create a lbpool
 *
 * @author liujunpeng
 */
public interface LbPoolBuilder extends Builder<LbPoolBuilder, LbPool> {

    /**
     * @param tenantId Owner of the pool. Only an administrative user can specify a
     *                 tenant ID other than its own.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder tenantId(String tenantId);

    /**
     * @param protocol The protocol of the VIP address. A valid value is TCP, HTTP,
     *                 or HTTPS.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder protocol(Protocol protocol);

    /**
     * @param name Pool name. Does not have to be unique.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder name(String name);

    /**
     * @param description Description for the pool.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder description(String description);

    /**
     * @param subnentId The ID of the subnet on which to allocate the VIP address.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder subnetId(String subnentId);

    /**
     * @param provider Loadbalance provider which openstack supports,such as haproxy
     * @return LbPoolBuilder
     */
    public LbPoolBuilder provider(String provider);

    /**
     * @param lbMethod The load-balancer algorithm, which is round-robin,
     *                 least-connections, and so on. This value, which must be
     *                 supported, is dependent on the load-balancer provider. Round
     *                 robin must be supported.
     * @return LbPoolBuilder
     */
    public LbPoolBuilder lbMethod(LbMethod lbMethod);

    /**
     * @param adminStateUp The administrative state of the lb pool, which is up (true) or
     *                     down (false).
     * @return LbPoolBuilder
     */
    public LbPoolBuilder adminStateUp(boolean adminStateUp);
}
