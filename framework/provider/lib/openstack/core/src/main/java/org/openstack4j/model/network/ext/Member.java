package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.MemberBuilder;

/**
 * A member of a Lbaas pool
 *
 * @author liujunpeng
 */
public interface Member extends ModelEntity, Buildable<MemberBuilder> {

    /**
     * @return status The status of the member. Indicates whether the member is
     * operational.
     */
    public String getStatus();

    /**
     * @return address the IP address of a member
     */
    public String getAddress();

    /**
     * @return The administrative state of the member, which is up (true) or
     * down (false).
     */
    public boolean isAdminStateUp();

    /**
     * @return the id of a tenant. Owner of the member.
     */
    public String getTenantId();

    /**
     * @return the member identifier
     */
    public String getId();

    /**
     * @return The port on which the application is hosted.such as 80
     */
    public Integer getProtocolPort();

    /**
     * @return Weight of member.1~256
     */
    public Integer getWeight();

    /**
     * @return The Loadbalance pool identifier
     */
    public String getPoolId();

}
