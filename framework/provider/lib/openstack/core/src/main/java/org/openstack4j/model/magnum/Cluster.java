package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface Cluster extends ModelEntity, Buildable<ClusterBuilder> {
    /**
     * Gets status
     *
     * @return status
     */
    String getStatus();

    /**
     * Gets clusterTemplateId
     *
     * @return clusterTemplateId
     */
    String getClusterTemplateId();

    /**
     * Gets uuid
     *
     * @return uuid
     */
    String getUuid();

    /**
     * Gets links
     *
     * @return links
     */
    List<GenericLink> getLinks();

    /**
     * Gets stackId
     *
     * @return stackId
     */
    String getStackId();

    /**
     * Gets masterCount
     *
     * @return masterCount
     */
    Integer getMasterCount();

    /**
     * Gets createTimeout
     *
     * @return createTimeout
     */
    Integer getCreateTimeout();

    /**
     * Gets nodeCount
     *
     * @return nodeCount
     */
    Integer getNodeCount();

    /**
     * Gets discoveryUrl
     *
     * @return discoveryUrl
     */
    String getDiscoveryUrl();

    /**
     * Gets keypair
     *
     * @return keypair
     */
    String getKeypair();

    /**
     * Gets name
     *
     * @return name
     */
    String getName();

}
