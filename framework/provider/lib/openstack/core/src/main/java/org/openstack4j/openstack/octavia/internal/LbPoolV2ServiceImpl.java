package org.openstack4j.openstack.octavia.internal;

import org.openstack4j.api.octavia.LbPoolV2Service;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.LbPoolV2;
import org.openstack4j.model.octavia.LbPoolV2Update;
import org.openstack4j.model.octavia.MemberV2;
import org.openstack4j.model.octavia.MemberV2Update;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.octavia.domain.OctaviaLbPoolV2;
import org.openstack4j.openstack.octavia.domain.OctaviaMemberV2;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Octavia) lbaas v2 lb pool operations
 *
 * @author wei
 */
public class LbPoolV2ServiceImpl extends BaseOctaviaServices implements LbPoolV2Service {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LbPoolV2> list() {
        return get(OctaviaLbPoolV2.LbPoolsV2.class, uri("/lbaas/pools")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LbPoolV2> list(Map<String, String> filteringParams) {
        Invocation<OctaviaLbPoolV2.LbPoolsV2> req = get(OctaviaLbPoolV2.LbPoolsV2.class, uri("/lbaas/pools"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2 get(String lbPoolId) {
        checkNotNull(lbPoolId);
        return get(OctaviaLbPoolV2.class, uri("/lbaas/pools/%s", lbPoolId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2 create(LbPoolV2 lbPool) {
        checkNotNull(lbPool);
        return post(OctaviaLbPoolV2.class, uri("/lbaas/pools")).entity(lbPool).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2 update(String lbPoolId, LbPoolV2Update lbPool) {
        checkNotNull(lbPoolId);
        checkNotNull(lbPool);
        return put(OctaviaLbPoolV2.class, uri("/lbaas/pools/%s", lbPoolId)).entity(lbPool).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String lbPoolId) {
        checkNotNull(lbPoolId);
        return ToActionResponseFunction.INSTANCE.apply(delete(void.class, uri("/lbaas/pools/%s", lbPoolId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends MemberV2> listMembers(String lbPoolId) {
        return get(OctaviaMemberV2.MembersV2.class, uri("/lbaas/pools/%s/members", lbPoolId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends MemberV2> listMembers(String lbPoolId, Map<String, String> filteringParams) {
        Invocation<OctaviaMemberV2.MembersV2> req = get(OctaviaMemberV2.MembersV2.class, uri("/lbaas/pools/%s/members", lbPoolId));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberV2 getMember(String lbPoolId, String memberId) {
        checkNotNull(lbPoolId);
        checkNotNull(memberId);
        return get(OctaviaMemberV2.class, uri("/lbaas/pools/%s/members/%s", lbPoolId, memberId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberV2 createMember(String lbPoolId, MemberV2 member) {
        checkNotNull(lbPoolId);
        checkNotNull(member);
        return post(OctaviaMemberV2.class, uri("/lbaas/pools/%s/members", lbPoolId)).entity(member).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberV2 updateMember(String lbPoolId, String memberId, MemberV2Update member) {
        checkNotNull(lbPoolId);
        checkNotNull(memberId);
        checkNotNull(member);
        return put(OctaviaMemberV2.class, uri("/lbaas/pools/%s/members/%s", lbPoolId, memberId)).entity(member).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse deleteMember(String lbPoolId, String memberId) {
        checkNotNull(lbPoolId);
        checkNotNull(memberId);
        return ToActionResponseFunction.INSTANCE.apply(delete(void.class, uri("/lbaas/pools/%s/members/%s", lbPoolId, memberId)).executeWithResponse());
    }
}
