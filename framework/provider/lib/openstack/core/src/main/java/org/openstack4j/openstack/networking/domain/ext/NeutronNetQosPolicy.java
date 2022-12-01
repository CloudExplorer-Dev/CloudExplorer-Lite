package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.NetQosPolicy;
import org.openstack4j.model.network.ext.builder.NetQosPolicyBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Network qos policy that are bound to a Tenant
 *
 * @author bboyHan
 */
@JsonRootName("policy")
public class NeutronNetQosPolicy implements NetQosPolicy {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String description;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("revision_number")
    private String revisionNumber;
    @JsonProperty
    private boolean shared;
    @JsonProperty
    private String id;
    @JsonProperty("is_default")
    private boolean isDefault;
    @JsonProperty
    private List<Map<String, String>> rules;
    private String name;
    @JsonProperty
    private List<String> tags;

    public static NetQosPolicyBuilder builder() {
        return new NetQosPolicyConcreteBuilder();
    }

    @Override
    public NetQosPolicyBuilder toBuilder() {
        return new NetQosPolicyConcreteBuilder(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public List<Map<String, String>> getRules() {
        return rules;
    }

    @Override
    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("id", id).add("description", description).add("tenantId", tenantId).add("shared", shared)
                .add("isDefault", isDefault).add("name", name).add("rules", rules)
                .add("tags", tags)
                .toString();
    }

    public static class NetQosPolicyConcreteBuilder implements NetQosPolicyBuilder {

        private NeutronNetQosPolicy model;

        public NetQosPolicyConcreteBuilder() {
            model = new NeutronNetQosPolicy();
        }

        public NetQosPolicyConcreteBuilder(NeutronNetQosPolicy model) {
            this.model = model;
        }

        @Override
        public NetQosPolicy build() {
            return model;
        }

        @Override
        public NetQosPolicyBuilder from(NetQosPolicy in) {
            model = (NeutronNetQosPolicy) in;
            return this;
        }

        @Override
        public NetQosPolicyBuilder description(String description) {
            model.description = description;
            return this;
        }

        @Override
        public NetQosPolicyBuilder tenantId(String tenantId) {
            model.tenantId = tenantId;
            return this;
        }

        @Override
        public NetQosPolicyBuilder shared(boolean shared) {
            model.shared = shared;
            return this;
        }

        @Override
        public NetQosPolicyBuilder isDefault(boolean isDefault) {
            model.isDefault = isDefault;
            return this;
        }

        @Override
        public NetQosPolicyBuilder name(String name) {
            model.name = name;
            return this;
        }

    }

    public static class NeutronNetQosPolicies extends ListResult<NeutronNetQosPolicy> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("policies")
        private List<NeutronNetQosPolicy> policies;

        @Override
        protected List<NeutronNetQosPolicy> value() {
            return policies;
        }
    }

}
