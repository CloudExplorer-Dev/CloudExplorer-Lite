package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.gbp.Direction;
import org.openstack4j.model.gbp.PolicyClassifierUpdate;
import org.openstack4j.model.gbp.Protocol;
import org.openstack4j.model.gbp.builder.PolicyClassifierUpdateBuilder;

/**
 * Model implementation for Policy Classifier Update
 *
 * @author vinod borole
 */
@JsonRootName("policy_classifier")
public class GbpPolicyClassifierUpdate implements PolicyClassifierUpdate {

    private static final long serialVersionUID = 1L;
    private String description;
    private Direction direction;
    @JsonProperty("port_range")
    private String portRange;
    private Protocol protocol;
    private Boolean shared;
    private String name;

    public static PolicyClassifierUpdateBuilder builder() {
        return new PolicyClassifierUpdateConcreteBuilder();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String getPortRange() {
        return portRange;
    }

    public void setPortRange(String portRange) {
        this.portRange = portRange;
    }

    @Override
    public boolean isShared() {
        return this.shared == null ? false : shared;
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    @Override
    public PolicyClassifierUpdateBuilder toBuilder() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class PolicyClassifierUpdateConcreteBuilder implements PolicyClassifierUpdateBuilder {
        private GbpPolicyClassifierUpdate policyClassfierUpdate;

        public PolicyClassifierUpdateConcreteBuilder(GbpPolicyClassifierUpdate gbpPolicyClassifierUpdate) {
            this.policyClassfierUpdate = gbpPolicyClassifierUpdate;
        }

        public PolicyClassifierUpdateConcreteBuilder() {
            this(new GbpPolicyClassifierUpdate());
        }

        @Override
        public PolicyClassifierUpdate build() {
            return policyClassfierUpdate;
        }

        @Override
        public PolicyClassifierUpdateBuilder from(PolicyClassifierUpdate in) {
            this.policyClassfierUpdate = (GbpPolicyClassifierUpdate) in;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder name(String name) {
            this.policyClassfierUpdate.name = name;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder description(String description) {
            this.policyClassfierUpdate.description = description;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder shared(boolean shared) {
            this.policyClassfierUpdate.shared = shared;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder portRangeMin(int min) {
            String range = "";
            if (this.policyClassfierUpdate.portRange != null && !this.policyClassfierUpdate.portRange.isEmpty())
                range = min + ":" + this.policyClassfierUpdate.portRange;
            else
                range = "" + min;

            this.policyClassfierUpdate.portRange = range;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder portRangeMax(int max) {
            String range = "";
            if (this.policyClassfierUpdate.portRange != null && !this.policyClassfierUpdate.portRange.isEmpty())
                range = this.policyClassfierUpdate.portRange + ":" + max;
            else
                range = "" + max;

            this.policyClassfierUpdate.portRange = range;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder direction(Direction direction) {
            this.policyClassfierUpdate.direction = direction;
            return this;
        }

        @Override
        public PolicyClassifierUpdateBuilder protocol(Protocol protocol) {
            this.policyClassfierUpdate.protocol = protocol;
            return this;
        }


    }

}
