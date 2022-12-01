package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.LbPoolV2UpdateBuilder;

/**
 * An entity for updating a lbaas v2 lb pool
 *
 * @author emjburns
 */
public interface LbPoolV2Update extends ModelEntity, Buildable<LbPoolV2UpdateBuilder> {

    /**
     * @return Pool name. Does not have to be unique.
     */
    public String getName();

    /**
     * @return Description for the pool.
     */
    public String getDescription();

    /**
     * @return The administrative state of the lb pool, which is up (true) or
     * down (false).
     */
    public boolean isAdminStateUp();

    /**
     * @return The load-balancer algorithm, which is round-robin, least-connections, and so on. This value, which must be supported, is dependent on the load-balancer provider. Round-robin must be supported.
     */
    public LbMethod getLbMethod();

    /**
     * Optional
     *
     * @see Vip#getSessionPersistence()
     */
    public SessionPersistence getSessionPersistence();

}
