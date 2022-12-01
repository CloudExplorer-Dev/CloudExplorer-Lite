package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.PortPair;

import java.util.Map;

/**
 * A builder to create a port pair
 *
 * @author Dmitry Gerenrot
 */
public interface PortPairBuilder extends Builder<PortPairBuilder, PortPair> {

    /**
     * @param id : Port Pair identifer
     * @return PortPairBuilder
     */
    PortPairBuilder id(String id);

    /**
     * @param name : Human readable name for the port pair
     * @return PortPairBuilder
     */
    PortPairBuilder name(String name);

    /**
     * @param tenantId : Tenant (project) identifier
     * @return PortPairBuilder
     */
    PortPairBuilder projectId(String projectId);

    /**
     * @param description : Human readable description
     * @return PortPairBuilder
     */
    PortPairBuilder description(String description);

    /**
     * @param egressId : id for the egress port
     * @return PortPairBuilder
     */
    PortPairBuilder egressId(String egressId);

    /**
     * @param ingressId : id for the ingress port
     * @return PortPairBuilder
     */
    PortPairBuilder ingressId(String ingressId);

    /**
     * @param serviceFunctionParameters : Map of service function parameters
     * @return PortPairBuilder
     */
    PortPairBuilder serviceFunctionParameters(Map<String, String> serviceFunctionParameters);
}
