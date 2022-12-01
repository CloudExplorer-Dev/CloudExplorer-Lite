package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.Member;
import org.openstack4j.model.network.ext.builder.MemberBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A member of a Lbaas pool
 *
 * @author liujunpeng
 */
@JsonRootName("member")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronMember implements Member {

    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String address;
    @JsonProperty("protocol_port")
    private Integer protocolPort;
    /**
     * 1~100
     */
    private Integer weight;
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;
    private String status;
    @JsonProperty("pool_id")
    private String poolId;

    public static MemberBuilder builder() {
        return new MemberConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAddress() {
        return address;
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
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getProtocolPort() {
        return protocolPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getWeight() {
        return weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPoolId() {
        return poolId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("address", address)
                .add("adminStateUp", adminStateUp)
                .add("poolId", poolId)
                .add("protocolPort", protocolPort)
                .add("status", status)
                .add("tenantId", tenantId)
                .add("weight", weight)
                .toString();
    }

    /**
     * Wraps this Member into a Builder
     *
     * @return the network builder
     */
    public MemberBuilder toBuilder() {
        return new MemberConcreteBuilder(this);
    }

    /**
     * Lbaas members
     *
     * @author liujunpeng
     */
    public static class Members extends ListResult<NeutronMember> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("members")
        private List<NeutronMember> members;

        @Override
        public List<NeutronMember> value() {
            return members;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("members", members).toString();
        }
    }

    /**
     * Member create builder
     *
     * @author liujunpeng
     */
    public static class MemberConcreteBuilder implements MemberBuilder {

        private NeutronMember m;

        public MemberConcreteBuilder() {
            this(new NeutronMember());
        }

        public MemberConcreteBuilder(NeutronMember m) {
            this.m = m;
        }

        @Override
        public Member build() {
            return m;
        }

        @Override
        public MemberBuilder from(Member in) {
            m = (NeutronMember) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder address(String address) {
            m.address = address;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder protocolPort(Integer protocolPort) {
            m.protocolPort = protocolPort;
            return this;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder weight(Integer weight) {
            m.weight = weight;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberBuilder poolId(String lbPoolId) {
            m.poolId = lbPoolId;
            return this;
        }
    }

}
