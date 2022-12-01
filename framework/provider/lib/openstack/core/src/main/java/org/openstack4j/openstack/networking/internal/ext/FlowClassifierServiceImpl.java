package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.FlowClassifierService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.FlowClassifier;
import org.openstack4j.openstack.networking.domain.ext.NeutronFlowClassifier;
import org.openstack4j.openstack.networking.domain.ext.NeutronFlowClassifier.FlowClassifiers;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@inheritDoc}
 */
public class FlowClassifierServiceImpl extends BaseNetworkingServices implements FlowClassifierService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FlowClassifier> list() {
        return get(FlowClassifiers.class, uri("/sfc/flow_classifiers")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowClassifier create(FlowClassifier flowClassifier) {
        checkNotNull(flowClassifier);
        return post(NeutronFlowClassifier.class, uri("/sfc/flow_classifiers")).entity(flowClassifier).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String flowClassifierId) {
        checkNotNull(flowClassifierId);
        return deleteWithResponse(uri("/sfc/flow_classifiers/%s", flowClassifierId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowClassifier get(String flowClassifierId) {
        checkNotNull(flowClassifierId);
        return get(NeutronFlowClassifier.class, uri("/sfc/flow_classifiers/%s", flowClassifierId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowClassifier update(String flowClassifierId, FlowClassifier flowClassifier) {
        checkNotNull(flowClassifierId);
        return put(NeutronFlowClassifier.class, uri("/sfc/flow_classifiers/%s", flowClassifierId)).entity(flowClassifier).execute();
    }

}
