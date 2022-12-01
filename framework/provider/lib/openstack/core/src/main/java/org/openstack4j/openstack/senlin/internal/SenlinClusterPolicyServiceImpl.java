package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinClusterPolicyService;
import org.openstack4j.model.senlin.ClusterPolicy;
import org.openstack4j.openstack.senlin.domain.SenlinClusterPolicy;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains getters for all implementation of the available cluster services
 *
 * @author lion
 */
public class SenlinClusterPolicyServiceImpl extends BaseSenlinServices implements SenlinClusterPolicyService {

    @Override
    public List<? extends ClusterPolicy> list(String clusterID) {
        checkNotNull(clusterID);
        return get(SenlinClusterPolicy.ClusterPolicy.class, uri("/clusters/%s/policies", clusterID)).execute().getList();
    }

    @Override
    public ClusterPolicy get(String clusterID, String policyID) {
        checkNotNull(clusterID);
        checkNotNull(policyID);
        return get(SenlinClusterPolicy.class, uri("/clusters/%s/policies/%s", clusterID, policyID)).execute();
    }

}
