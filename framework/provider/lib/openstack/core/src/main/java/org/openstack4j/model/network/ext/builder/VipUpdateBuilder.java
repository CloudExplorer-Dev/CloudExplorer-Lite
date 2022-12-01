package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.SessionPersistence;
import org.openstack4j.model.network.ext.VipUpdate;

/**
 * A Builder to update a vip
 *
 * @author liujunpeng
 */
public interface VipUpdateBuilder extends Builder<VipUpdateBuilder, VipUpdate> {

    /**
     * Optional
     *
     * @param name Human-readable name for the VIP. Does not have to be unique.
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder name(String name);

    /**
     * Optional
     *
     * @param description Human-readable description for the VIP.
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder description(String description);

    /**
     * Optional
     *
     * @param poolId The ID of the pool with which the VIP is associated.
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder poolId(String poolId);

    /**
     * Optional
     *
     * @param sessionPersistence Session persistence parameters for the VIP. Omit the
     *                           session_persistence parameter to prevent session persistence.
     *                           When no session persistence is used, the session_persistence
     *                           parameter does not appear in the API response. To clear
     *                           session persistence for the VIP, set the session_persistence
     *                           parameter to null in a VIP update request.
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder sessionPersistence(SessionPersistence sessionPersistence);

    /**
     * Optional
     *
     * @param connectionLimit . The maximum number of connections allowed for the VIP. Value
     *                        is -1 if the limit is not set.
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder connectionLimit(Integer connectionLimit);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN).
     * @return VipUpdateBuilder
     */
    VipUpdateBuilder adminStateUp(boolean adminStateUp);

}
