package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.Protocol;
import org.openstack4j.model.network.ext.SessionPersistence;
import org.openstack4j.model.network.ext.Vip;

/**
 * A builder to create a vip
 *
 * @author liujunpeng
 */
public interface VipBuilder extends Builder<VipBuilder, Vip> {

    /**
     * @param tenantId Owner of the VIP. Only an admin user can specify a tenant ID
     *                 other than its own.
     * @return VipBuilder
     */
    VipBuilder tenantId(String tenantId);

    /**
     * Optional
     *
     * @param name Human-readable name for the VIP. Does not have to be unique.
     * @return VipBuilder
     */
    VipBuilder name(String name);

    /**
     * Optional
     *
     * @param description Human-readable description for the VIP.
     * @return VipBuilder
     */
    VipBuilder description(String description);

    /**
     * Optional
     *
     * @param subnetId The ID of the subnet on which to allocate the VIP address.
     * @return VipBuilder
     */
    VipBuilder subnetId(String subnetId);

    /**
     * Optional
     *
     * @param address The IP address of the VIP.
     * @return VipBuilder
     */
    VipBuilder address(String address);

    /**
     * @param protocol The protocol of the VIP address. A valid value is TCP, HTTP,
     *                 or HTTPS.
     * @return VipBuilder
     */
    VipBuilder protocol(Protocol protocol);

    /**
     * @param protocolPort The port on which to listen to client traffic that is
     *                     associated with the VIP address. A valid value is from 1 to
     *                     65535.
     * @return VipBuilder
     */
    VipBuilder protocolPort(Integer protocolPort);

    /**
     * Optional
     *
     * @param poolId The pool identifier
     * @return VipBuilder
     */
    VipBuilder poolId(String poolId);

    /**
     * Optional
     *
     * @param sessionPersistence . Session persistence parameters for the VIP. Omit the
     *                           session_persistence parameter to prevent session persistence.
     *                           When no session persistence is used, the session_persistence
     *                           parameter does not appear in the API response. To clear
     *                           session persistence for the VIP, set the session_persistence
     *                           parameter to null in a VIP update request.
     * @return VipBuilder
     */
    VipBuilder sessionPersistence(SessionPersistence sessionPersistence);

    /**
     * Optional
     *
     * @param connectionLimit The maximum number of connections allowed for the VIP. Value
     *                        is -1 if the limit is not set.
     * @return VipBuilder
     */
    VipBuilder connectionLimit(Integer connectionLimit);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN).
     * @return VipBuilder
     */
    VipBuilder adminStateUp(boolean adminStateUp);
}
