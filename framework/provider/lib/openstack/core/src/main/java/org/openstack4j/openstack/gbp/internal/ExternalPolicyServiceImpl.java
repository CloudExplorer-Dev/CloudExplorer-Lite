package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.ExternalPolicyService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.ExternalPolicy;
import org.openstack4j.model.gbp.ExternalPolicyCreate;
import org.openstack4j.openstack.gbp.domain.GbpExternalPolicy;
import org.openstack4j.openstack.gbp.domain.GbpExternalPolicy.ExternalPolicies;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * External Policy API Implementation
 *
 * @author vinod borole
 */
public class ExternalPolicyServiceImpl extends BaseNetworkingServices implements ExternalPolicyService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ExternalPolicy> list() {
        return get(ExternalPolicies.class, uri("/grouppolicy/external_policies")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ExternalPolicy> list(Map<String, String> filteringParams) {
        Invocation<ExternalPolicies> externalPolicyInvocation = buildInvocation(filteringParams);
        return externalPolicyInvocation.execute().getList();
    }

    private Invocation<ExternalPolicies> buildInvocation(Map<String, String> filteringParams) {
        Invocation<ExternalPolicies> externalPolicyInvocation = get(ExternalPolicies.class, "/grouppolicy/external_policies");
        if (filteringParams == null) {
            return externalPolicyInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                externalPolicyInvocation = externalPolicyInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return externalPolicyInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalPolicy get(String id) {
        checkNotNull(id);
        return get(GbpExternalPolicy.class, uri("/grouppolicy/external_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/external_policies/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalPolicy create(ExternalPolicyCreate externalPolicy) {
        return post(GbpExternalPolicy.class, uri("/grouppolicy/external_policies")).entity(externalPolicy).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalPolicy update(String externalPolicyId, ExternalPolicyCreate externalPolicy) {
        checkNotNull(externalPolicyId);
        checkNotNull(externalPolicy);
        return put(GbpExternalPolicy.class, uri("/grouppolicy/external_policies/%s", externalPolicyId)).entity(externalPolicy).execute();
    }


}
