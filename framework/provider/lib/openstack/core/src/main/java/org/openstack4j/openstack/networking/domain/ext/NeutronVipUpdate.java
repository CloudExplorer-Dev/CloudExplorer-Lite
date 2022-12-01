package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.SessionPersistence;
import org.openstack4j.model.network.ext.VipUpdate;
import org.openstack4j.model.network.ext.builder.VipUpdateBuilder;


/**
 * Neutron Vip for update
 *
 * @author liujunpeng
 */
@JsonRootName("vip")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronVipUpdate implements VipUpdate {


    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    @JsonProperty("pool_id")
    private String poolId;

    /**
     * Session persistence parameters for the VIP. Omit the session_persistence
     * parameter to prevent session persistence. When no session persistence is
     * used, the session_persistence parameter does not appear in the API
     * response. To clear session persistence for the VIP, set the
     * session_persistence parameter to null in a VIP update request.
     */
    @JsonProperty("session_persistence")
    private NeutronSessionPersistence sessionPersistence;

    /**
     * The maximum number of connections allowed for the VIP. Default is -1, meaning no limit.
     */
    @JsonProperty("connection_limit")
    private Integer connectionLimit;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp;

    public static VipUpdateBuilder builder() {
        return new VipUpdateContreteBuilder();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VipUpdateBuilder toBuilder() {

        return new VipUpdateContreteBuilder();
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
    public Integer getConnectionLimit() {
        return connectionLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPoolId() {
        return poolId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    public static class VipUpdateContreteBuilder implements VipUpdateBuilder {

        private NeutronVipUpdate m;

        public VipUpdateContreteBuilder() {
            this(new NeutronVipUpdate());
        }

        public VipUpdateContreteBuilder(NeutronVipUpdate m) {
            this.m = m;
        }

        @Override
        public VipUpdate build() {
            return m;
        }

        @Override
        public VipUpdateBuilder from(VipUpdate in) {
            m = (NeutronVipUpdate) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder poolId(String poolId) {
            m.poolId = poolId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder sessionPersistence(
                SessionPersistence sessionPersistence) {
            m.sessionPersistence = (NeutronSessionPersistence) sessionPersistence;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder connectionLimit(Integer connectionLimit) {
            m.connectionLimit = connectionLimit;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public VipUpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }
    }
}
