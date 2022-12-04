/**
 *
 */
package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.LbMethod;
import org.openstack4j.model.network.ext.LbPool;
import org.openstack4j.model.network.ext.Protocol;
import org.openstack4j.model.network.ext.builder.LbPoolBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A pool of a load balancer
 *
 * @author liujunpeng
 */
@JsonRootName("pool")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronLbPool implements LbPool {

    private static final long serialVersionUID = 1L;


    @JsonProperty("health_monitors")
    private List<String> healthMonitors;
    private String id;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("vip_id")
    private String vipId;
    private String name;
    private String description;
    @JsonProperty("subnet_id")
    private String subnetId;

    private Protocol protocol;

    private String provider;
    @JsonProperty("lb_method")
    private LbMethod lbMethod;

    private List<String> members;
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;
    private String status;

    public static LbPoolBuilder builder() {
        return new LbPoolContreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolBuilder toBuilder() {
        return new LbPoolContreteBuilder(this);
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
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVipId() {
        return vipId;
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
    public String getDescription() {

        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubnetId() {
        return subnetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProvider() {
        return provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbMethod getLbMethod() {
        return lbMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMembers() {
        return members;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getHealthMonitors() {
        return healthMonitors;
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
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("adminStateUp", adminStateUp)
                .add("description", description)
                .add("healthMonitors", healthMonitors)
                .add("lbMethod", lbMethod)
                .add("members", members)
                .add("name", name)
                .add("protocol", protocol)
                .add("provider", provider)
                .add("status", status)
                .add("subnetId", subnetId)
                .add("tenantId", tenantId)
                .add("vipId", vipId)
                .toString();
    }

    public static class LbPoolContreteBuilder implements LbPoolBuilder {

        private NeutronLbPool m;

        public LbPoolContreteBuilder() {
            this(new NeutronLbPool());
        }

        public LbPoolContreteBuilder(NeutronLbPool m) {
            this.m = m;
        }

        @Override
        public LbPool build() {
            return m;
        }

        @Override
        public LbPoolBuilder from(LbPool in) {
            m = (NeutronLbPool) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder subnetId(String subnentId) {
            m.subnetId = subnentId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder provider(String provider) {
            m.provider = provider;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder lbMethod(LbMethod lbMethod) {
            m.lbMethod = lbMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolBuilder protocol(Protocol protocol) {
            m.protocol = protocol;
            return this;
        }

    }

    public static class LbPools extends ListResult<NeutronLbPool> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("pools")
        List<NeutronLbPool> lbPools;

        /**
         * {@inheritDoc}
         */
        @Override
        public List<NeutronLbPool> value() {
            return lbPools;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("lbPools", lbPools)
                    .toString();
        }

    }
}
