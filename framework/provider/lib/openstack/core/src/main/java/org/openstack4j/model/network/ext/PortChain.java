package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.network.ext.builder.PortChainBuilder;

import java.util.List;
import java.util.Map;

/**
 * A Port Chain Entity.
 *
 * @author Dmitry Gerenrot.
 */
public interface PortChain extends Resource, Buildable<PortChainBuilder> {

    /**
     * @return description : Human readable description for the port chain
     */
    String getDescription();

    /**
     * @return chainId : an id on openstack.
     * It is not equal to {@link id}.
     */
    String getChainId();

    /**
     * @return flow classifiers : list of ids
     */
    List<String> getFlowClassifiers();

    /**
     * @return port pair groups : list of ids
     */
    List<String> getPortPairGroups();

    /**
     * @return Chain Parameters
     */
    Map<String, String> getChainParameters();
}
