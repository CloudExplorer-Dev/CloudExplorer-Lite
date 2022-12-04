package org.openstack4j.model.octavia;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.octavia.builder.MemberV2Builder;

/**
 * A member of a Lbaas V2 pool
 *
 * @author wei
 */
public interface MemberV2 extends ModelEntity, Buildable<MemberV2Builder> {
    /**
     * @return the member identifier
     */
    String getId();

    /**
     * @return the ID of the project/tenant that owns the resource
     */
    String getProjectId();

    /**
     * @return address the IP address of a member
     */
    String getAddress();

    /**
     * @return The port on which the application is hosted.such as 80
     */
    Integer getProtocolPort();

    /**
     * @return Weight of member.1~256
     */
    Integer getWeight();

    /**
     * @return The subnet in which to access the member
     */
    String getSubnetId();

    /**
     * @return The administrative state of the member, which is up (true) or
     * down (false).
     */
    boolean isAdminStateUp();

}
