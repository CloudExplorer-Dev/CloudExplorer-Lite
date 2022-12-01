package org.openstack4j.api.networking.ext;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortPair;

import java.util.List;

/**
 * Service Port Pair Service
 *
 * @author Dmitry Gerenrot
 */
public interface PortPairService {

    /**
     * Lists Port Pairs for port chains
     *
     * @return the list of Port Pairs
     */
    List<? extends PortPair> list();


    /**
     * Get a Port Pair by id.
     *
     * @return PortPair
     */
    PortPair get(String portPairId);

    /**
     * Update a Port Pair with the given id to match the given update object
     *
     * @return PortPair
     */
    PortPair update(String portPairId, PortPair portPair);

    /**
     * Create a Port Pair
     *
     * @return PortPair : object actually created
     */
    PortPair create(PortPair portPair);

    /**
     * Delete a Port Pair
     *
     * @return the action response
     */
    ActionResponse delete(String portPairId);
}
