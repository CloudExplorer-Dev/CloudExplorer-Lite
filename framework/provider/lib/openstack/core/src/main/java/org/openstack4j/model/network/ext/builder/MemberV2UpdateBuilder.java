package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.network.ext.MemberV2Update;

/**
 * A builder to update an lbaas v2 member
 *
 * @author emjburns
 */
public interface MemberV2UpdateBuilder extends Buildable.Builder<MemberV2UpdateBuilder, MemberV2Update> {

    /**
     * @param adminStateUp The administrative state of the member, which is up (true) or
     *                     down (false).
     * @return MemberV2UpdateBuilder
     */
    public MemberV2UpdateBuilder adminStateUp(boolean adminStateUp);

    /**
     * @param weight Weight of member.from 1 to 256
     * @return MemberUpdateBuilder
     */
    public MemberV2UpdateBuilder weight(Integer weight);
}
