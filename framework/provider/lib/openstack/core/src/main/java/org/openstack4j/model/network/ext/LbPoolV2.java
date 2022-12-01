package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.LbPoolV2Builder;
import org.openstack4j.openstack.networking.domain.ext.ListItem;

import java.util.List;

/**
 * A v2 loadbanlance pool
 *
 * @author emjburns
 */
public interface LbPoolV2 extends ModelEntity, Buildable<LbPoolV2Builder> {
    /**
     * @return id. The unique ID for the pool.
     */
    String getId();

    /**
     * @return tenantId.
     * Owner of the pool. Only an administrative user can specify a tenant ID other than its own.
     */
    String getTenantId();

    /**
     * @return Pool name. Does not have to be unique.
     */
    String getName();

    /**
     * @return Description for the pool.
     */
    String getDescription();

    /**
     * @return The protocol of the pool, which is TCP, HTTP, or HTTPS.
     */
    Protocol getProtocol();

    /**
     * @return The load-balancer algorithm, which is round-robin, least-connections, and so on. This value, which must be supported, is dependent on the load-balancer provider. Round-robin must be supported.
     */
    LbMethod getLbMethod();

    /**
     * @return SessionPersistence
     */
    SessionPersistence getSessionPersistence();

    /**
     * @return The administrative state of the lb pool, which is up (true) or
     * down (false).
     */
    boolean isAdminStateUp();

    /**
     * Listeners associated with the pool
     *
     * @return listeners associated with the pool
     */
    List<ListItem> getListeners();

    /**
     * @return The ID of the load balancer under which this pool will be created.
     */
    String getLoadBalancerId();

    /**
     * @return List of members that belong to the pool.
     */
    List<ListItem> getMembers();

    /**
     * @return Health monitor associated with the pool.
     */
    String getHealthMonitorId();
}
