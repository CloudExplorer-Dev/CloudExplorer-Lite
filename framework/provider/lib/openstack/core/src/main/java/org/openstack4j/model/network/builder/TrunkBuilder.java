package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.identity.v3.Tenant;
import org.openstack4j.model.network.Trunk;
import org.openstack4j.openstack.networking.domain.NeutronTrunkSubport;

import java.util.List;

/**
 * A builder which creates a network trunk
 *
 * @author Kashyap Jha
 */
public interface TrunkBuilder extends Builder<TrunkBuilder, Trunk> {

    /**
     * Sets the name
     */
    TrunkBuilder name(String name);

    /**
     * Sets the tenant
     */
    TrunkBuilder tenant(Tenant tenant);

    /**
     * Sets the tenantId
     */
    TrunkBuilder tenantId(String tenantId);

    /**
     * Sets the parent port
     */
    TrunkBuilder parentPort(String parentPortId);

    /**
     * Sets the description
     */
    TrunkBuilder description(String description);

    /**
     * Sets the admin state
     */
    TrunkBuilder adminState(boolean adminStateUp);

    /**
     * Sets the subports
     */
    TrunkBuilder trunkSubports(List<NeutronTrunkSubport> trunkSubports);

    /**
     * Sets the trunk id
     */
    TrunkBuilder trunkId(String trunkId);
}
