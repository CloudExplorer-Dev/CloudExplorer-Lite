package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.NetworkPolicyService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.NetworkServicePolicy;
import org.openstack4j.openstack.gbp.domain.GbpNetworkServicePolicy;
import org.openstack4j.openstack.gbp.domain.GbpNetworkServicePolicy.NetworkServicePolicies;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Network service policy API Implementation
 *
 * @author sumit gandhi
 */
public class NetworkPolicyServiceImpl extends BaseNetworkingServices implements NetworkPolicyService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NetworkServicePolicy> list() {
        return get(NetworkServicePolicies.class, uri("/grouppolicy/network_service_policies")).
                execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends NetworkServicePolicy> list(Map<String, String> filteringParams) {
        Invocation<NetworkServicePolicies> natpoolInvocation = buildInvocation(filteringParams);
        return natpoolInvocation.execute().getList();
    }

    private Invocation<NetworkServicePolicies> buildInvocation(Map<String, String> filteringParams) {
        Invocation<NetworkServicePolicies> servicePoliciesInvocation = get(NetworkServicePolicies.class, "/grouppolicy/network_service_policies");
        if (filteringParams == null) {
            return servicePoliciesInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                servicePoliciesInvocation = servicePoliciesInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return servicePoliciesInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkServicePolicy get(String id) {
        checkNotNull(id);
        return get(GbpNetworkServicePolicy.class, uri("/grouppolicy/network_service_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GbpNetworkServicePolicy create(NetworkServicePolicy gbpServicePolicy) {
        return post(GbpNetworkServicePolicy.class, uri("/grouppolicy/network_service_policies")).entity(gbpServicePolicy).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/network_service_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkServicePolicy update(String gbpServicePolicyId, NetworkServicePolicy gbpServicePolicy) {
        checkNotNull(gbpServicePolicy);
        checkNotNull(gbpServicePolicyId);
        return put(GbpNetworkServicePolicy.class, uri("/grouppolicy/network_service_policies/%s", gbpServicePolicyId)).
                entity(gbpServicePolicy).execute();
    }

}
