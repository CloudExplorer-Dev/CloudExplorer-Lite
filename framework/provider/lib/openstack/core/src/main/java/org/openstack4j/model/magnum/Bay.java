package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface Bay extends ModelEntity, Buildable<BayBuilder> {
    /**
     * Gets status
     *
     * @return status
     */
    String getStatus();

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
     * Gets createdAt
     *
     * @return createdAt
     */
    String getCreatedAt();

    /**
     * Gets apiAddress
     *
     * @return apiAddress
     */
    String getApiAddress();

    /**
     * Gets discoveryUrl
     *
     * @return discoveryUrl
     */
    String getDiscoveryUrl();

    /**
     * Gets updatedAt
     *
     * @return updatedAt
     */
    String getUpdatedAt();

    /**
     * Gets masterCount
     *
     * @return masterCount
     */
    Integer getMasterCount();

    /**
     * Gets coeVersion
     *
     * @return coeVersion
     */
    String getCoeVersion();

    /**
     * Gets baymodelId
     *
     * @return baymodelId
     */
    String getBaymodelId();

    /**
     * Gets masterAddresses
     *
     * @return masterAddresses
     */
    List<String> getMasterAddresses();

    /**
     * Gets nodeCount
     *
     * @return nodeCount
     */
    Integer getNodeCount();

    /**
     * Gets nodeAddresses
     *
     * @return nodeAddresses
     */
    List<String> getNodeAddresses();

    /**
     * Gets statusReason
     *
     * @return statusReason
     */
    String getStatusReason();

    /**
     * Gets bayCreateTimeout
     *
     * @return bayCreateTimeout
     */
    String getBayCreateTimeout();

    /**
     * Gets name
     *
     * @return name
     */
    String getName();

}
