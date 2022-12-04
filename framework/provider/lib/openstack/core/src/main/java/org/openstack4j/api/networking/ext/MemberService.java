package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.Member;
import org.openstack4j.model.network.ext.MemberUpdate;

import java.util.List;
import java.util.Map;

/**
 * Networking (Neutron) Lbaas member Extension API
 *
 * @author liujunpeng
 */
public interface MemberService extends RestService {
    /**
     * List all members  that the current tenant has access to
     *
     * @return list of all Member
     */
    List<? extends Member> list();

    /**
     * Returns list of member filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends Member> list(Map<String, String> filteringParams);


    /**
     * Get the specified member by ID
     *
     * @param memberId the member identifier
     * @return the member or null if not found
     */
    Member get(String memberId);

    /**
     * Delete the specified member by ID
     *
     * @param memberId the member identifier
     * @return the action response
     */
    ActionResponse delete(String memberId);

    /**
     * Create a member
     *
     * @param member Member
     * @return Member
     */
    Member create(Member member);

    /**
     * Update a member
     *
     * @param memberId the member identifier
     * @param member   MemberUpdate
     * @return Member
     */
    Member update(String memberId, MemberUpdate member);
}
