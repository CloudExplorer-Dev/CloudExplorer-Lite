package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.NodeGroup;
import org.openstack4j.model.sahara.ServiceConfig;

/**
 * Builder interface used for {@link NodeGroup} object.
 *
 * @author Ekasit Kijsipongse
 */
public interface NodeGroupBuilder extends Builder<NodeGroupBuilder, NodeGroup> {

    //
    // Define setter apis for user to create a new node group
    //

    /**
     * See {@link NodeGroup#getName()}
     *
     * @param name the name of the node group
     * @return NodeGroupBuilder
     */
    NodeGroupBuilder name(String name);

    /**
     * See {@link NodeGroup#getCount()}
     *
     * @param count the number of instances in the node group
     * @return NodeGroupBuilder
     */
    NodeGroupBuilder count(int count);

    /**
     * See {@link NodeGroup#getNodeGroupTemplateId()}
     *
     * @param templateId the id of the node group template
     * @return NodeGroupBuilder
     */
    NodeGroupBuilder nodeGroupTemplateId(String templateId);

    /**
     * See {@link NodeGroup#getFloatingNetworkId()}
     *
     * @param networkId the id of floating IP Pool
     * @return NodeGroupBuilder
     */
    NodeGroupBuilder floatingIpPool(String networkId);

    /**
     * See {@link NodeGroup#getFlavorId()}
     *
     * @param flavorId the id of flavor
     * @return NodeGroupBuilder
     */
    NodeGroupBuilder flavor(String flavorId);

    /**
     * Set the security group.
     *
     * @param isAutoSecurityGroup true or false
     * @return this builder
     */
    NodeGroupBuilder setAutoSecurityGroup(boolean isAutoSecurityGroup);

    /**
     * Add the security group.
     *
     * @param id the id
     * @return this builder
     */
    NodeGroupBuilder addSecurityGroup(String id);

    /**
     * Add a node process
     *
     * @param name the name
     * @return this builder
     */
    NodeGroupBuilder addNodeProcess(String name);

    /**
     * Add a service config
     *
     * @param name   the service name
     * @param config the config
     * @return this builder
     */
    NodeGroupBuilder addServiceConfig(String name, ServiceConfig config);

}
