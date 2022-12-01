package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.octavia.MemberV2;

public interface MemberV2Builder extends Buildable.Builder<MemberV2Builder, MemberV2> {
    /**
     * @param projectId the ID of the project/tenant that owns the resource
     * @return MemberV2Builder
     */
    MemberV2Builder projectId(String projectId);

    /**
     * @param address The IP address of the member.
     * @return MemberV2Builder
     */
    MemberV2Builder address(String address);

    /**
     * @param protocolPort The port on which the application is hosted. A valid value
     *                     is from 1 to 65535
     * @return MemberV2Builder
     */
    MemberV2Builder protocolPort(Integer protocolPort);

    /**
     * @param subnetId The subnet in which to access the member
     * @return MemberV2Builder
     */
    MemberV2Builder subnetId(String subnetId);

    /**
     * Optional
     *
     * @param weight Weight of member.from 1 to 256
     *               Default 1
     * @return MemberV2Builder
     */
    MemberV2Builder weight(Integer weight);


    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the member, which is up (true) or
     *                     down (false). Default true.
     * @return MemberV2Builder
     */
    MemberV2Builder adminStateUp(boolean adminStateUp);
}
