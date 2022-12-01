package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.NodeGroupTemplate;
import org.openstack4j.model.sahara.ServiceConfig;

/**
 * Builder interface used for {@link NodeGroupTemplate} object.
 *
 * @author Ekasit Kijsipongse
 */
public interface NodeGroupTemplateBuilder extends Builder<NodeGroupTemplateBuilder, NodeGroupTemplate> {

    //
    // Define setter apis for user to create a new node group template
    //

    /**
     * See {@link NodeGroupTemplate#getName()}
     *
     * @param name the name of the node group template
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder name(String name);

    /**
     * See {@link NodeGroupTemplate#getDescription()}
     *
     * @param description the description of the node group template
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder description(String description);

    /**
     * See {@link NodeGroupTemplate#getHadoopVersion()}
     *
     * @param hadoopVersion the version of hadoop
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder hadoopVersion(String hadoopVersion);

    /**
     * See {@link NodeGroupTemplate#getPluginName()}
     *
     * @param pluginName the name of the sahara plugin
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder pluginName(String pluginName);

    /**
     * See {@link NodeGroupTemplate#getFloatingNetworkId()}
     *
     * @param networkId the id of floating IP Pool
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder floatingIpPool(String networkId);

    /**
     * See {@link NodeGroupTemplate#getFlavorId()}
     *
     * @param flavorId the id of flavor
     * @return NodeGroupTemplateBuilder
     */
    NodeGroupTemplateBuilder flavor(String flavorId);

    /**
     * Set the security group.
     *
     * @param isAutoSecurityGroup true or false
     * @return this builder
     */
    NodeGroupTemplateBuilder setAutoSecurityGroup(boolean isAutoSecurityGroup);

    /**
     * Add the security group.
     *
     * @param id the id
     * @return this builder
     */
    NodeGroupTemplateBuilder addSecurityGroup(String id);

    /**
     * Add a node process
     *
     * @param name the name
     * @return this builder
     */
    NodeGroupTemplateBuilder addNodeProcess(String name);

    /**
     * Add a service config
     *
     * @param name   the service name
     * @param config the config
     * @return this builder
     */
    NodeGroupTemplateBuilder addServiceConfig(String name, ServiceConfig config);

}
