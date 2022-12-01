package org.openstack4j.api.networking.ext;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortPairGroup;

import java.util.List;

/**
 * Port Pair Group Service
 *
 * @author Dmitry Gerenrot
 */
public interface PortPairGroupService {

    /**
     * Lists port pair groups for port chains
     *
     * @return the list of port pair groups
     */
    List<? extends PortPairGroup> list();

    /**
     * Get a port pair group by id.
     *
     * @return PortPairGroup
     */
    PortPairGroup get(String id);

    /**
     * Update a port pair group with the given id to match the given update object
     *
     * @return PortPairGroup
     */
    PortPairGroup update(String portPairGroupId, PortPairGroup portPairGroup);

    /**
     * Create a port pair group
     *
     * @return portPairGroup : object actually created
     */
    PortPairGroup create(PortPairGroup portPairGroup);

    /**
     * Delete a port pair group
     *
     * @return the action response
     */
    ActionResponse delete(String portPairGroupId);
}
