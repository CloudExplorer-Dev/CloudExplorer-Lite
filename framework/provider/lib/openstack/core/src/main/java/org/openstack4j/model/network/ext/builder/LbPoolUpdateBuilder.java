package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPoolUpdate;

/**
 * A Builder to update a lbpool
 *
 * @author liujunpeng
 */
public interface LbPoolUpdateBuilder extends Builder<LbPoolUpdateBuilder, LbPoolUpdate> {

    /**
     * @param lbMethod The load-balancer algorithm, which is round-robin,
     *                 least-connections, and so on. This value, which must be
     *                 supported, is dependent on the load-balancer provider. Round
     *                 robin must be supported.
     * @return LbPoolUpdateBuilder
     */
    public LbPoolUpdateBuilder lbMethod(LbMethod lbMethod);

    /**
     * @param name Pool name. Does not have to be unique.
     * @return LbPoolUpdateBuilder
     */
    public LbPoolUpdateBuilder name(String name);

    /**
     * @param description Description for the pool.
     * @return LbPoolUpdateBuilder
     */
    public LbPoolUpdateBuilder description(String description);

    /**
     * @param adminStateUp The administrative state of the lb pool, which is up (true) or
     *                     down (false).
     * @return LbPoolUpdateBuilder
     */
    public LbPoolUpdateBuilder adminStateUp(boolean adminStateUp);

}
