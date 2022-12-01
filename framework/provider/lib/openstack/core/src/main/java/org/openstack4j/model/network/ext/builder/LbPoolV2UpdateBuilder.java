package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPoolV2Update;
import org.openstack4j.model.network.ext.SessionPersistence;

/**
 * A builder to update an lbaas v2 pool
 *
 * @author emjburns
 */
public interface LbPoolV2UpdateBuilder extends Buildable.Builder<LbPoolV2UpdateBuilder, LbPoolV2Update> {
    /**
     * @param lbMethod The load-balancer algorithm, which is round-robin,
     *                 least-connections, and so on. This value, which must be
     *                 supported, is dependent on the load-balancer provider. Round
     *                 robin must be supported.
     * @return LbPoolV2UpdateBuilder
     */
    public LbPoolV2UpdateBuilder lbMethod(LbMethod lbMethod);

    /**
     * @param name Pool name. Does not have to be unique.
     * @return LbPoolV2UpdateBuilder
     */
    public LbPoolV2UpdateBuilder name(String name);

    /**
     * @param description Description for the pool.
     * @return LbPoolV2UpdateBuilder
     */
    public LbPoolV2UpdateBuilder description(String description);

    /**
     * @param adminStateUp The administrative state of the lb pool, which is up (true) or
     *                     down (false).
     * @return LbPoolV2UpdateBuilder
     */
    public LbPoolV2UpdateBuilder adminStateUp(boolean adminStateUp);

    /**
     * Optional
     *
     * @param sessionPersistence Session persistence parameters for the lbpool. Omit the
     *                           session_persistence parameter to prevent session persistence.
     *                           When no session persistence is used, the session_persistence
     *                           parameter does not appear in the API response. To clear
     *                           session persistence for the lbpool, set the session_persistence
     *                           parameter to null in a lbpool update request.
     * @return LbPoolV2UpdateBuilder
     */
    LbPoolV2UpdateBuilder sessionPersistence(SessionPersistence sessionPersistence);
}
