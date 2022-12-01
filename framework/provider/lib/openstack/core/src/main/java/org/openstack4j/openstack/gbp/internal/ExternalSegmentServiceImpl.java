package org.openstack4j.openstack.gbp.internal;

import org.openstack4j.api.gbp.ExternalSegmentService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.ExternalSegment;
import org.openstack4j.openstack.gbp.domain.GbpExternalSegment;
import org.openstack4j.openstack.gbp.domain.GbpExternalSegment.ExternalSegments;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * External Segment API Implementation
 *
 * @author vinod borole
 */
public class ExternalSegmentServiceImpl extends BaseNetworkingServices implements ExternalSegmentService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ExternalSegment> list() {
        return get(ExternalSegments.class, uri("/grouppolicy/external_segments")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ExternalSegment> list(Map<String, String> filteringParams) {
        Invocation<ExternalSegments> externalSegmentInvocation = buildInvocation(filteringParams);
        return externalSegmentInvocation.execute().getList();
    }

    private Invocation<ExternalSegments> buildInvocation(Map<String, String> filteringParams) {
        Invocation<ExternalSegments> externalSegmentInvocation = get(ExternalSegments.class, "/grouppolicy/external_segments");
        if (filteringParams == null) {
            return externalSegmentInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                externalSegmentInvocation = externalSegmentInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return externalSegmentInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalSegment get(String id) {
        checkNotNull(id);
        return get(GbpExternalSegment.class, uri("/grouppolicy/external_segments/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String id) {
        checkNotNull(id);
        return deleteWithResponse(uri("/grouppolicy/external_segments/%s", id)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalSegment create(ExternalSegment externalSegment) {
        return post(GbpExternalSegment.class, uri("/grouppolicy/external_segments")).entity(externalSegment).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalSegment update(String externalSegmentId, ExternalSegment externalSegment) {
        checkNotNull(externalSegmentId);
        checkNotNull(externalSegment);
        return put(GbpExternalSegment.class, uri("/grouppolicy/external_segments/%s", externalSegmentId)).entity(externalSegment).execute();
    }

}
