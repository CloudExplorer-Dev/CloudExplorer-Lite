package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface ClusterBuilder extends Builder<ClusterBuilder, Cluster> {
    /**
     * @see Cluster#getStatus
     */
    ClusterBuilder status(String status);

    /**
     * @see Cluster#getClusterTemplateId
     */
    ClusterBuilder clusterTemplateId(String clusterTemplateId);

    /**
     * @see Cluster#getUuid
     */
    ClusterBuilder uuid(String uuid);

    /**
     * @see Cluster#getLinks
     */
    ClusterBuilder links(List<GenericLink> links);

    /**
     * @see Cluster#getStackId
     */
    ClusterBuilder stackId(String stackId);

    /**
     * @see Cluster#getMasterCount
     */
    ClusterBuilder masterCount(Integer masterCount);

    /**
     * @see Cluster#getCreateTimeout
     */
    ClusterBuilder createTimeout(Integer createTimeout);

    /**
     * @see Cluster#getNodeCount
     */
    ClusterBuilder nodeCount(Integer nodeCount);

    /**
     * @see Cluster#getDiscoveryUrl
     */
    ClusterBuilder discoveryUrl(String discoveryUrl);

    /**
     * @see Cluster#getKeypair
     */
    ClusterBuilder keypair(String keypair);

    /**
     * @see Cluster#getName
     */
    ClusterBuilder name(String name);

}
