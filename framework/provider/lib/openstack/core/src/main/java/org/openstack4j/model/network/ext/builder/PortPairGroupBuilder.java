package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.PortPairGroup;

import java.util.List;
import java.util.Map;

public interface PortPairGroupBuilder extends Builder<PortPairGroupBuilder, PortPairGroup> {
    /**
     * @param id : Port Pair Group identifer
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder id(String id);

    /**
     * @param name : Human readable name for the port pair group
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder name(String name);

    /**
     * @param projectId : Project (tenant) identifier
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder projectId(String projectId);

    /**
     * @param description : Human readable description for the port pair group
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder description(String description);

    /**
     * @param portPairs : port pair members in this group
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder portPairs(List<String> portPairs);

    /**
     * @param portPairGroupParameters : parameters for this group.
     *                                Possible keys: lb_fields or service_type. Check openstack documentation.
     * @return PortPairGroupBuilder
     */
    PortPairGroupBuilder portPairGroupParameters(Map<String, Object> portPairGroupParameters);

}
