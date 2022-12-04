package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.PortChain;

import java.util.List;
import java.util.Map;

/**
 * A builder to create a port chain
 *
 * @author Dmitry Gerenrot
 */
public interface PortChainBuilder extends Builder<PortChainBuilder, PortChain> {

    /**
     * @param id : Port Chain identifer
     * @return PortChainBuilder
     */
    PortChainBuilder id(String id);

    /**
     * @param name : Human readable name for the port chain
     * @return PortChainBuilder
     */
    PortChainBuilder name(String name);

    /**
     * @return description : Human readable description for the port chain
     */
    PortChainBuilder description(String description);

    /**
     * @param projectId : project identifer
     * @return PortChainBuilder
     */
    PortChainBuilder projectId(String projectId);

    /**
     * @param chainId : identifier on openstack. It is not equal to {@link id}.
     * @return PortChainBuilder
     */
    PortChainBuilder chainId(String chainId);

    /**
     * @param flowClassifiers : list of ids
     * @return PortChainBuilder
     */
    PortChainBuilder flowClassifiers(List<String> flowClassifiers);

    /**
     * @param portPairGroups : list of ids
     * @return PortChainBuilder
     */
    PortChainBuilder portPairGroups(List<String> portPairGroups);

    /**
     * @param chainParameters : Map of chain parameters
     * @return PortChainBuilder
     */
    PortChainBuilder chainParameters(Map<String, String> chainParameters);
}
