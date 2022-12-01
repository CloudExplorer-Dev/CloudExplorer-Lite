package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.MemberUpdate;

/**
 * A Builder to update a  member
 *
 * @author liujunpeng
 */
public interface MemberUpdateBuilder extends Builder<MemberUpdateBuilder, MemberUpdate> {

    /**
     * @param adminStateUp The administrative state of the member, which is up (true) or
     *                     down (false).
     * @return MemberUpdateBuilder
     */
    public MemberUpdateBuilder adminStateUp(boolean adminStateUp);

    /**
     * @param weight Weight of member.from 1 to 256
     * @return MemberUpdateBuilder
     */
    public MemberUpdateBuilder weight(Integer weight);

    /**
     * @param poolId the lb pool identifier
     * @return MemberUpdateBuilder
     */
    public MemberUpdateBuilder poolId(String poolId);
}
