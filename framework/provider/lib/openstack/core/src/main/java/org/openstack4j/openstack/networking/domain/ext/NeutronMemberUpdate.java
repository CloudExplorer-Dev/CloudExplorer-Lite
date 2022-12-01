package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.MemberUpdate;
import org.openstack4j.model.network.ext.builder.MemberUpdateBuilder;

/**
 * A updated member of a Lbaas pool
 *
 * @author liujunpeng
 */
@JsonRootName("member")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronMemberUpdate implements MemberUpdate {

    private static final long serialVersionUID = 1L;
    /**
     * 1~100
     */
    private Integer weight;
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;
    @JsonProperty("pool_id")
    private String poolId;

    public static MemberUpdateBuilder builder() {
        return new MemberUpdateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("adminStateUp", adminStateUp)
                .add("weight", weight)
                .add("poolId", poolId)
                .toString();
    }

    /**
     * Wraps this MemberUpdate into a Builder
     *
     * @return the network builder
     */
    public MemberUpdateBuilder toBuilder() {
        return new MemberUpdateConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPoolId() {

        return poolId;
    }

    /**
     * Member create builder
     *
     * @author liujunpeng
     */
    public static class MemberUpdateConcreteBuilder implements MemberUpdateBuilder {

        private NeutronMemberUpdate m;

        public MemberUpdateConcreteBuilder() {
            this(new NeutronMemberUpdate());
        }

        public MemberUpdateConcreteBuilder(NeutronMemberUpdate m) {
            this.m = m;
        }

        @Override
        public MemberUpdateBuilder from(MemberUpdate in) {
            m = (NeutronMemberUpdate) in;
            return this;
        }

        @Override
        public MemberUpdate build() {
            return m;
        }

        @Override
        public MemberUpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public MemberUpdateBuilder weight(Integer weight) {
            m.weight = weight;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberUpdateBuilder poolId(String poolId) {
            m.poolId = poolId;
            return this;
        }
    }

}
