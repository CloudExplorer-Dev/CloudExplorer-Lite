package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.NetQuota;
import org.openstack4j.model.network.builder.NetQuotaBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Network quotas that are bound to a Tenant
 *
 * @author Jeremy Unruh
 */
@JsonRootName("quota")
public class NeutronNetQuota implements NetQuota {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private int subnet;
    @JsonProperty
    private int router;
    @JsonProperty
    private int port;
    @JsonProperty
    private int network;
    @JsonProperty("floatingip")
    private int floatingIp;
    @JsonProperty("security_group")
    private int securityGroup;
    @JsonProperty("security_group_rule")
    private int securityGroupRule;
    @JsonProperty("subnetpool")
    private int subnetpool;
    @JsonProperty("rbac_policy")
    private int rbacPolicy;

    public static NetQuotaBuilder builder() {
        return new NetQuotaConcreteBuilder();
    }

    @Override
    public NetQuotaBuilder toBuilder() {
        return new NetQuotaConcreteBuilder(this);
    }

    @Override
    public int getSubnet() {
        return subnet;
    }

    @Override
    public int getRouter() {
        return router;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getNetwork() {
        return network;
    }

    @JsonIgnore
    @Override
    public int getFloatingIP() {
        return floatingIp;
    }

    @Override
    public int getSecurityGroup() {
        return securityGroup;
    }

    @Override
    public int getSubnetpool() {
        return subnetpool;
    }

    @Override
    public int getRbacPolicy() {
        return rbacPolicy;
    }

    @Override
    public int getSecurityGroupRule() {
        return securityGroupRule;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("subnet", subnet).add("router", router).add("port", port)
                .add("network", network).add("floatingIp", floatingIp).add("subnetpool", subnetpool)
                .add("rbacPolicy", rbacPolicy).toString();
    }

    public static class NetQuotaConcreteBuilder implements NetQuotaBuilder {

        private NeutronNetQuota model;

        public NetQuotaConcreteBuilder() {
            model = new NeutronNetQuota();
        }

        public NetQuotaConcreteBuilder(NeutronNetQuota model) {
            this.model = model;
        }

        @Override
        public NetQuota build() {
            return model;
        }

        @Override
        public NetQuotaBuilder from(NetQuota in) {
            model = (NeutronNetQuota) in;
            return this;
        }

        @Override
        public NetQuotaBuilder subnet(int subnet) {
            model.subnet = subnet;
            return this;
        }

        @Override
        public NetQuotaBuilder router(int router) {
            model.router = router;
            return this;
        }

        @Override
        public NetQuotaBuilder port(int port) {
            model.port = port;
            return this;
        }

        @Override
        public NetQuotaBuilder network(int network) {
            model.network = network;
            return this;
        }

        @Override
        public NetQuotaBuilder floatingIP(int floatingIP) {
            model.floatingIp = floatingIP;
            return this;
        }

        @Override
        public NetQuotaBuilder securityGroup(int securityGroup) {
            model.securityGroup = securityGroup;
            return this;
        }

        @Override
        public NetQuotaBuilder securityGroupRule(int securityGroupRule) {
            model.securityGroupRule = securityGroupRule;
            return this;
        }

        @Override
        public NetQuotaBuilder subnetpool(int subnetpool) {
            model.subnetpool = subnetpool;
            return this;
        }

        @Override
        public NetQuotaBuilder rbacPolicy(int rbacPolicy) {
            model.rbacPolicy = rbacPolicy;
            return this;
        }
    }

    public static class NeutronNetQuotas extends ListResult<NeutronNetQuota> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("quotas")
        private List<NeutronNetQuota> quotas;

        @Override
        protected List<NeutronNetQuota> value() {
            return quotas;
        }
    }

}
