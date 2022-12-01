package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.PortChain;

import java.util.List;

/**
 * Port Chain Service
 *
 * @author Dmitry Gerenrot
 */
public interface PortChainService extends RestService {

    /**
     * Lists port chains.
     *
     * @return the list of port chains
     */
    List<? extends PortChain> list();


    /**
     * Get a port chain by id.
     *
     * @return PortChain
     */
    PortChain get(String portChainId);

    /**
     * Update a port chain with the given id to match the given update object
     *
     * @return PortChain
     */
    PortChain update(String portChainId, PortChain portChain);

    /**
     * Create a port chain
     *
     * @return portChain : object actually created
     */
    PortChain create(PortChain portChain);

    /**
     * Delete a port chain
     *
     * @return the action response
     */
    ActionResponse delete(String portChainId);
}
