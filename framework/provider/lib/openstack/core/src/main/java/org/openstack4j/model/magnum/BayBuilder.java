package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface BayBuilder extends Builder<BayBuilder, Bay> {
    /**
     * @see Bay#getStatus
     */
    BayBuilder status(String status);

    /**
     * @see Bay#getUuid
     */
    BayBuilder uuid(String uuid);

    /**
     * @see Bay#getLinks
     */
    BayBuilder links(List<GenericLink> links);

    /**
     * @see Bay#getStackId
     */
    BayBuilder stackId(String stackId);

    /**
     * @see Bay#getCreatedAt
     */
    BayBuilder createdAt(String createdAt);

    /**
     * @see Bay#getApiAddress
     */
    BayBuilder apiAddress(String apiAddress);

    /**
     * @see Bay#getDiscoveryUrl
     */
    BayBuilder discoveryUrl(String discoveryUrl);

    /**
     * @see Bay#getUpdatedAt
     */
    BayBuilder updatedAt(String updatedAt);

    /**
     * @see Bay#getMasterCount
     */
    BayBuilder masterCount(Integer masterCount);

    /**
     * @see Bay#getCoeVersion
     */
    BayBuilder coeVersion(String coeVersion);

    /**
     * @see Bay#getBaymodelId
     */
    BayBuilder baymodelId(String baymodelId);

    /**
     * @see Bay#getMasterAddresses
     */
    BayBuilder masterAddresses(List<String> masterAddresses);

    /**
     * @see Bay#getNodeCount
     */
    BayBuilder nodeCount(Integer nodeCount);

    /**
     * @see Bay#getNodeAddresses
     */
    BayBuilder nodeAddresses(List<String> nodeAddresses);

    /**
     * @see Bay#getStatusReason
     */
    BayBuilder statusReason(String statusReason);

    /**
     * @see Bay#getBayCreateTimeout
     */
    BayBuilder bayCreateTimeout(String bayCreateTimeout);

    /**
     * @see Bay#getName
     */
    BayBuilder name(String name);

}
