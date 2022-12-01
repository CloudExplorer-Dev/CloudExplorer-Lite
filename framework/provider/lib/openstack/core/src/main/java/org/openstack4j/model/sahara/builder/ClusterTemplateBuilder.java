package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.ClusterTemplate;
import org.openstack4j.model.sahara.NodeGroup;
import org.openstack4j.model.sahara.ServiceConfig;

/**
 * Builder interface used for {@link ClusterTemplate} object.
 *
 * @author Ekasit Kijsipongse
 */
public interface ClusterTemplateBuilder extends Builder<ClusterTemplateBuilder, ClusterTemplate> {

    //
    // Define setter apis for user to create a new cluster template
    //

    /**
     * See {@link ClusterTemplate#getPluginName()}
     *
     * @param pluginName the name of the sahara plugin
     * @return ClusterTemplateBuilder
     */
    ClusterTemplateBuilder pluginName(String pluginName);

    /**
     * See {@link ClusterTemplate#getHadoopVersion()}
     *
     * @param hadoopVersion the version of hadoop
     * @return ClusterTemplateBuilder
     */
    ClusterTemplateBuilder hadoopVersion(String hadoopVersion);

    /**
     * Add a node group
     *
     * @param nodeGroup the node group
     * @return this builder
     */
    ClusterTemplateBuilder addNodeGroup(NodeGroup nodeGroup);

    /**
     * See {@link ClusterTemplate#getName()}
     *
     * @param name the name of the cluster template
     * @return ClusterTemplateBuilder
     */
    ClusterTemplateBuilder name(String name);

    /**
     * See {@link ClusterTemplate#getDescription()}
     *
     * @param description the description of cluster template
     * @return ClusterTemplateBuilder
     */
    ClusterTemplateBuilder description(String description);


    /**
     * See {@link ClusterTemplate#getNeutronManagementNetworkId()}
     *
     * @param networkId the id of management network
     * @return ClusterTemplateBuilder
     */
    ClusterTemplateBuilder managementNetworkId(String networkId);

    /**
     * Add a cluster config
     *
     * @param name   the service name
     * @param config the config
     * @return this builder
     */
    ClusterTemplateBuilder addServiceConfig(String name, ServiceConfig config);

}
