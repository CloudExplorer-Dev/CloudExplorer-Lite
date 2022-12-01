package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.ExternalPolicy;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for External Policy
 *
 * @author vinod borole
 */
@JsonRootName("external_policy")
public class GbpExternalPolicy implements ExternalPolicy {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    @JsonProperty("consumed_policy_rule_sets")
    private List<String> consumedPolicyRuleSets;
    @JsonProperty("provided_policy_rule_sets")
    private List<String> providedPolicyRuleSets;
    @JsonProperty("external_segments")
    private List<String> externalSegments;
    private Boolean shared;

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
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
    public void setName(String name) {
        this.name = name;
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
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getConsumedPolicyRuleSets() {
        return consumedPolicyRuleSets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getProvidedPolicyRuleSets() {
        return providedPolicyRuleSets;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getExternalSegments() {
        return externalSegments;
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
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("name", name).add("tenantId", tenantId).add("externalSegments", externalSegments).add("id", id).add("description", description).add("shared", shared).add("consumedPolicyRuleSets", consumedPolicyRuleSets).add("providedPolicyRuleSets", providedPolicyRuleSets).toString();
    }

    public static class ExternalPolicies extends ListResult<GbpExternalPolicy> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("external_policies")
        private List<GbpExternalPolicy> externalPolicys;

        @Override
        protected List<GbpExternalPolicy> value() {
            return externalPolicys;
        }
    }
}
