package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.Firewall;
import org.openstack4j.model.network.ext.builder.FirewallBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A Neutron Firewall (FwaaS) : Firewall Entity.
 *
 * @author Vishvesh Deshmukh
 */
@JsonRootName("firewall")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronFirewall implements Firewall {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String description;
    private Boolean shared;
    @JsonProperty("admin_state_up")
    private Boolean adminStateUp;
    private FirewallStatus status;
    @JsonProperty("firewall_policy_id")
    private String policyId;
    @JsonProperty("router_ids")
    private List<String> routerIds;

    /**
     * @return FirewallBuilder
     */
    public static FirewallBuilder builder() {
        return new FirewallConcreteBuilder();
    }

    /**
     * Wrap this Firewall to a builder
     *
     * @return FirewallBuilder
     */
    @Override
    public FirewallBuilder toBuilder() {
        return new FirewallConcreteBuilder(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isAdminStateUp() {
        return adminStateUp != null && adminStateUp;
    }

    @Override
    public Boolean isShared() {
        return shared != null && shared;
    }

    @Override
    public FirewallStatus getStatus() {
        return status;
    }

    @Override
    public List<String> getRouterIds() {
        return routerIds;
    }

    @JsonIgnore
    @Override
    public String getPolicy() {
        return policyId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name)
                .add("status", status).add("policyId", policyId)
                .add("shared", shared).add("adminStateUp", adminStateUp)
                .add("tenantId", tenantId).add("description", description)
                .add("routerIds", routerIds)
                .toString();
    }

    /**
     * <p>The state of a Neutron (Firewall - FwaaS) entity.</p>
     *
     * <p>Indicates whether firewall resource is currently operational.</p>
     *
     * @author Vishvesh Deshmukh
     */
    public enum FirewallStatus {
        ACTIVE,
        DOWN,
        BUILD,
        ERROR,
        PENDING_CREATE,
        PENDING_UPDATE,
        PENDING_DELETE,
        UNRECOGNIZED;

        @JsonCreator
        public static FirewallStatus forValue(String value) {
            if (value != null) {
                for (FirewallStatus s : FirewallStatus.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return FirewallStatus.UNRECOGNIZED;
        }
    }

    public static class Firewalls extends ListResult<NeutronFirewall> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("firewalls")
        List<NeutronFirewall> firewalls;

        @Override
        public List<NeutronFirewall> value() {
            return firewalls;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("firewalls", firewalls).toString();
        }
    }

    public static class FirewallConcreteBuilder implements FirewallBuilder {
        NeutronFirewall f;

        public FirewallConcreteBuilder() {
            this(new NeutronFirewall());
        }

        public FirewallConcreteBuilder(NeutronFirewall f) {
            this.f = f;
        }

        @Override
        public Firewall build() {
            return f;
        }

        @Override
        public FirewallBuilder from(Firewall in) {
            this.f = (NeutronFirewall) in;
            return this;
        }

        @Override
        public FirewallBuilder tenantId(String tenantId) {
            f.tenantId = tenantId;
            return this;
        }

        @Override
        public FirewallBuilder name(String name) {
            f.name = name;
            return this;
        }

        @Override
        public FirewallBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public FirewallBuilder adminStateUp(Boolean adminStateUp) {
            f.adminStateUp = adminStateUp;
            return this;
        }

        @Override
        public FirewallBuilder shared(Boolean shared) {
            f.shared = shared;
            return this;
        }

        @Override
        public FirewallBuilder policy(String policyId) {
            f.policyId = policyId;
            return this;
        }

        @Override
        public FirewallBuilder routerIds(List<String> routerIds) {
            f.routerIds = routerIds;
            return this;
        }
    }
}
