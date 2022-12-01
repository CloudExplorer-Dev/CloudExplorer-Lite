package org.openstack4j.api.gbp;

import org.openstack4j.common.RestService;

/**
 * This interface contains all available GbpServices
 *
 * @author vinod borole
 */
public interface GbpService extends RestService {

    /**
     * Service implementation which provides methods for manipulation of external Policies
     *
     * @return ExternalPolicyService
     */
    ExternalPolicyService externalPolicy();

    /**
     * Service implementation which provides methods for manipulation of external segments
     *
     * @return ExternalSegmentService
     */
    ExternalSegmentService externalSegment();

    /**
     * Service implementation which provides methods for manipulation of groups
     *
     * @return GroupService
     */
    GroupService group();

    /**
     * Service implementation which provides methods for manipulation of l2policies
     *
     * @return L2policyService
     */
    L2policyService l2Policy();

    /**
     * Service implementation which provides methods for manipulation of l3policies
     *
     * @return L3policyService
     */
    L3policyService l3Policy();

    /**
     * Service implementation which provides methods for manipulation of nat pools
     *
     * @return NatPoolService
     */
    NatPoolService natPool();

    /**
     * Service implementation which provides methods for manipulation of network services
     *
     * @return NetworkPolicyService
     */
    NetworkPolicyService networkPolicyService();

    /**
     * Service implementation which provides methods for manipulation of policy actions
     *
     * @return PolicyActionService
     */
    PolicyActionService policyAction();

    /**
     * Service implementation which provides methods for manipulation of policy rules
     *
     * @return PolicyRuleService
     */
    PolicyRuleService policyRule();

    /**
     * Service implementation which provides methods for manipulation of policy rules Sets
     *
     * @return PolicyRuleSetService
     */
    PolicyRuleSetService policyRuleSet();

    /**
     * Service implementation which provides methods for manipulation of policy targets
     *
     * @return PolicyTargetService
     */
    PolicyTargetService policyTarget();

    /**
     * Service implementation which provides methods for manipulation of policy classifiers
     *
     * @return PolicyClassifierService
     */
    PolicyClassifierService policyClassifier();

    /**
     * Service implementation which provides methods for manipulation of service chains
     *
     * @return ServicechainService
     */
    ServicechainService servicechain();

    /**
     * Service implementation which provides methods for manipulation of service profiles
     *
     * @return ServiceProfileService
     */
    ServiceProfileService serviceProfile();
}
