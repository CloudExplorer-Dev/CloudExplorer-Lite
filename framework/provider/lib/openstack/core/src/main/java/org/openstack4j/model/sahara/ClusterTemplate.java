package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.ClusterTemplateBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A Sahara Cluster Template
 *
 * @author Ekasit Kijsipongse
 */
public interface ClusterTemplate extends ModelEntity, Buildable<ClusterTemplateBuilder> {

    /**
     * @return the hadoop version
     */
    String getHadoopVersion();

    /**
     * @return the default image identifier
     */
    String getDefaultImageId();

    /**
     * @return the name of the node group template
     */
    String getName();

    /**
     * @return the updated date
     */
    Date getUpdatedAt();

    /**
     * @return the tenant ID
     */
    String getTenantId();

    /**
     * @return the plugin name
     */
    String getPluginName();

    /**
     * @return the list of anti affinity
     */
    List<String> getAntiAffinity();

    /**
     * @return the description of the node group template
     */
    String getDescription();

    /**
     * @return the identifier
     */
    String getId();

    /**
     * @return the list of node groups
     */
    List<? extends NodeGroup> getNodeGroups();

    /**
     * @return the id of neutron management network
     */
    String getManagementNetworkId();

    /**
     * @return the created date
     */
    Date getCreatedAt();

    /**
     * @return map of cluster configurations or null
     */
    Map<String, ? extends ServiceConfig> getClusterConfigs();

}
