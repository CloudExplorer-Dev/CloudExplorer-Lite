package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.Cluster;
import org.openstack4j.model.sahara.NodeGroup;
import org.openstack4j.model.sahara.ServiceConfig;

/**
 * Builder interface used for {@link Cluster} object.
 * For mapping from object builder to JSON request
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface ClusterBuilder extends Builder<ClusterBuilder, Cluster> {

    //
    // Define setter apis for user to create a new cluster
    //

    /**
     * See {@link Cluster#getName()}
     *
     * @param name the name of the cluster
     * @return ClusterBuilder
     */
    ClusterBuilder name(String name);

    /**
     * See {@link Cluster#getHadoopVersion()}
     *
     * @param hadoopVersion the version of hadoop
     * @return ClusterBuilder
     */
    ClusterBuilder hadoopVersion(String hadoopVersion);

    /**
     * See {@link Cluster#getPluginName()}
     *
     * @param pluginName the name of the sahara plugin
     * @return ClusterBuilder
     */
    ClusterBuilder pluginName(String pluginName);

    /**
     * See {@link Cluster#getClusterTemplateId()}
     *
     * @param clusterTemplateId the id of cluster template
     * @return ClusterBuilder
     */
    ClusterBuilder template(String clusterTemplateId);

    /**
     * See {@link Cluster#getDefaultImageId()}
     *
     * @param imageId the id of image
     * @return ClusterBuilder
     */
    ClusterBuilder image(String imageId);

    /**
     * See {@link Cluster#getKeypairName()}
     *
     * @param keypairName the name of key pair
     * @return ClusterBuilder
     */
    ClusterBuilder keypairName(String keypairName);

    /**
     * See {@link Cluster#getManagementNetworkId()}
     *
     * @param networkId the id of management network
     * @return ClusterBuilder
     */
    ClusterBuilder managementNetworkId(String networkId);

    /**
     * Add a cluster config
     *
     * @param name   the service name
     * @param config the config
     * @return this builder
     */
    ClusterBuilder addServiceConfig(String name, ServiceConfig config);

    /**
     * Add a node group
     *
     * @param nodeGroup the node group
     * @return this builder
     */
    ClusterBuilder addNodeGroup(NodeGroup nodeGroup);

}
