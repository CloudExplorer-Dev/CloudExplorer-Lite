package org.openstack4j.api.octavia;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.LbPoolV2;
import org.openstack4j.model.octavia.LbPoolV2Update;
import org.openstack4j.model.octavia.MemberV2;
import org.openstack4j.model.octavia.MemberV2Update;

import java.util.List;
import java.util.Map;

/**
 * Octavia V2 pool Extension API
 *
 * @author wei
 */
public interface LbPoolV2Service extends RestService {
    /**
     * List all lb pools that the current tenant has access to
     *
     * @return list of all lb pools
     */
    List<? extends LbPoolV2> list();

    /**
     * Returns list of lb v2 pools filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return List
     */
    List<? extends LbPoolV2> list(Map<String, String> filteringParams);

    /**
     * Get the specified lb pool by ID
     *
     * @param lbPoolId the lb v2 pool identifier
     * @return the lbPoolV2 or null if not found
     */
    LbPoolV2 get(String lbPoolId);

    /**
     * Delete the specified lb Pool by ID
     *
     * @param lbPoolId the lb pool identifier
     * @return the action response
     */
    ActionResponse delete(String lbPoolId);

    /**
     * Create a lb Pool
     *
     * @param lbPool LbPool
     * @return lbPoolV2
     */
    LbPoolV2 create(LbPoolV2 lbPool);

    /**
     * Update a lb pool
     *
     * @param lbPoolId the lb pool identifier
     * @param lbPool   LbPoolV2Update
     * @return LbPoolV2
     */
    LbPoolV2 update(String lbPoolId, LbPoolV2Update lbPool);

    /**
     * List all members  that the current tenant has access to
     *
     * @param lbPoolId the load balancer pool
     * @return list of all Member
     */
    List<? extends MemberV2> listMembers(String lbPoolId);

    /**
     * Returns list of member filtered by parameters.
     *
     * @param lbPoolId        the load balancer pool
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends MemberV2> listMembers(String lbPoolId, Map<String, String> filteringParams);


    /**
     * Get the specified member by ID
     *
     * @param lbPoolId the load balancer pool
     * @param memberId the member identifier
     * @return the member or null if not found
     */
    MemberV2 getMember(String lbPoolId, String memberId);

    /**
     * Create a member
     *
     * @param lbPoolId the load balancer pool
     * @param member   Member
     * @return Member
     */
    MemberV2 createMember(String lbPoolId, MemberV2 member);

    /**
     * Delete the specified member by ID
     *
     * @param lbPoolId the load balancer pool
     * @param memberId the member identifier
     * @return the action response
     */
    ActionResponse deleteMember(String lbPoolId, String memberId);

    /**
     * Update a member
     *
     * @param lbPoolId the load balancer pool
     * @param memberId the member identifier
     * @param member   MemberUpdate
     * @return Member
     */
    MemberV2 updateMember(String lbPoolId, String memberId, MemberV2Update member);
}
