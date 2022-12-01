package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.network.ext.builder.PortPairGroupBuilder;

import java.util.List;
import java.util.Map;

/**
 * A Port Pair Group Entity.
 *
 * @author Dmitry Gerenrot.
 */
public interface PortPairGroup extends Resource, Buildable<PortPairGroupBuilder> {

    /**
     * @return description : Human readable description for the port pair group
     */
    String getDescription();

    /**
     * @return list of port pairs in this group
     */
    List<String> getPortPairs();

    /**
     * @return Dictionary of parameters for this group.
     * Possible keys: lb_fields or service_type. Check openstack documentation.
     */
    Map<String, Object> getPortPairGroupParameters();
}
