package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.gbp.Direction;
import org.openstack4j.model.gbp.PolicyClassifier;
import org.openstack4j.model.gbp.Protocol;
import org.openstack4j.model.gbp.builder.PolicyClassifierBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Policy Classifier
 *
 * @author vinod borole
 */
@JsonRootName("policy_classifier")
public class GbpPolicyClassifier implements PolicyClassifier {
    private static final long serialVersionUID = 1L;
    private String name;
    @JsonProperty("tenant_id")
    private String tenantId;
    private String id;
    private String description;
    private String direction;
    @JsonProperty("port_range")
    private String portRange;
    private String protocol;
    private Boolean shared;

    public static PolicyClassifierBuilder builder() {
        return new PolicyClassifierConcreteBuilder();
    }

    @Override
    public PolicyClassifierBuilder toBuilder() {
        return new PolicyClassifierConcreteBuilder(this);
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Direction getDirection() {
        return Direction.forValue(direction);
    }

    @Override
    public String getPortRange() {
        return portRange;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.forValue(protocol);
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("name", name).add("desription", description)
                .add("tenantId", tenantId).add("portRange", portRange).add("protocol", protocol).add("shared", shared).toString();
    }

    public static class PolicyClassifiers extends ListResult<GbpPolicyClassifier> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("policy_classifiers")
        private List<GbpPolicyClassifier> policyClassfiers;

        @Override
        protected List<GbpPolicyClassifier> value() {
            return policyClassfiers;
        }
    }

    public static class PolicyClassifierConcreteBuilder implements PolicyClassifierBuilder {

        private GbpPolicyClassifier policyClassfier;

        public PolicyClassifierConcreteBuilder(GbpPolicyClassifier gbpPolicyClassifier) {
            this.policyClassfier = gbpPolicyClassifier;
        }

        public PolicyClassifierConcreteBuilder() {
            this(new GbpPolicyClassifier());
        }

        @Override
        public PolicyClassifier build() {
            return policyClassfier;
        }

        @Override
        public PolicyClassifierBuilder from(PolicyClassifier in) {
            this.policyClassfier = (GbpPolicyClassifier) in;
            return this;
        }

        @Override
        public PolicyClassifierBuilder name(String name) {
            this.policyClassfier.name = name;
            return this;
        }

        @Override
        public PolicyClassifierBuilder description(String description) {
            this.policyClassfier.description = description;
            return this;
        }

        @Override
        public PolicyClassifierBuilder portRangeMin(int min) {
            String range = "";
            if (this.policyClassfier.portRange != null && !this.policyClassfier.portRange.isEmpty())
                range = min + ":" + this.policyClassfier.portRange;
            else
                range = "" + min;

            this.policyClassfier.portRange = range;
            return this;
        }

        @Override
        public PolicyClassifierBuilder portRangeMax(int max) {
            String range = "";
            if (this.policyClassfier.portRange != null && !this.policyClassfier.portRange.isEmpty())
                range = this.policyClassfier.portRange + ":" + max;
            else
                range = "" + max;

            this.policyClassfier.portRange = range;
            return this;
        }

        @Override
        public PolicyClassifierBuilder direction(Direction direction) {
            this.policyClassfier.direction = direction.value();
            return this;
        }

        @Override
        public PolicyClassifierBuilder protocol(Protocol protocol) {
            this.policyClassfier.protocol = protocol.value();
            return this;
        }

        @Override
        public PolicyClassifierBuilder shared(boolean shared) {
            this.policyClassfier.shared = shared;
            return this;
        }

    }

}
