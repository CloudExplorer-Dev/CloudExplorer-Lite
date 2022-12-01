package org.openstack4j.api.senlin;

import org.openstack4j.common.RestService;

/**
 * This interface containts all available senlinServices
 *
 * @author lion
 */
public interface SenlinService extends RestService {

    /**
     * Service implementation which provides methods for manipulation of policy
     *
     * @return PolicyService
     */
    SenlinPolicyService policy();

    /**
     * Service implementation which provides methods for manipulation of action
     *
     * @return ActionService
     */
    SenlinActionService action();

    /**
     * Service implementation which provides methods for manipulation of buildInfo
     *
     * @return BuildInfoService
     */
    SenlinBuildInfoService buildInfo();

    /**
     * Service implementation which provides methods for manipulation of cluster
     *
     * @return ClusterService
     */
    SenlinClusterService cluster();

    /**
     * Service implementation which provides methods for manipulation of clusterPolicy
     *
     * @return ClusterPolicyService
     */
    SenlinClusterPolicyService clusterPolicy();

    /**
     * Service implementation which provides methods for manipulation of event
     *
     * @return EventService
     */
    SenlinEventService event();

    /**
     * Service implementation which provides methods for manipulation of node
     *
     * @return NodeService
     */
    SenlinNodeService node();

    /**
     * Service implementation which provides methods for manipulation of profile
     *
     * @return ProfileService
     */
    SenlinProfileService profile();

    /**
     * Service implementation which provides methods for manipulation of profileType
     *
     * @return ProfileTypeService
     */
    SenlinProfileTypeService profileType();

    /**
     * Service implementation which provides methods for manipulation of policyType
     *
     * @return PolicyTypeService
     */
    SenlinPolicyTypeService policyType();

    /**
     * Service implementation which provides methods for manipulation of receiver
     *
     * @return ReceiverService
     */
    SenlinReceiverService receiver();

    /**
     * Service implementation which provides methods for manipulation of webHook
     *
     * @return WebHookService
     */
    SenlinWebHookService webHook();

    /**
     * Service implementation which provides methods for manipulation of Version
     *
     * @return Version
     */
    SenlinVersionService version();

}
