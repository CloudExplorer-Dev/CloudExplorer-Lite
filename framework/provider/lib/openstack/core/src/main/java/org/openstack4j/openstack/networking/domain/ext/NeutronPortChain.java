package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.PortChain;
import org.openstack4j.model.network.ext.builder.PortChainBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 */
@JsonRootName("port_chain")
public class NeutronPortChain implements PortChain {

    private static final long serialVersionUID = 1L;
    @JsonProperty("chain_id")
    String chainId;
    @JsonProperty("flow_classifiers")
    List<String> flowClassifiers;
    @JsonProperty("port_pair_groups")
    List<String> portPairGroups;
    @JsonProperty("chain_parameters")
    Map<String, String> chainParameters;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty
    private String description;

    public static PortChainBuilder builder() {
        return new PortChainConcreteBuilder();
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
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @Override
    public String getTenantId() {
        return projectId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.projectId = tenantId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    @Override
    public List<String> getFlowClassifiers() {
        return flowClassifiers;
    }

    public void setFlowClassifiers(List<String> flowClassifiers) {
        this.flowClassifiers = flowClassifiers;
    }

    @Override
    public List<String> getPortPairGroups() {
        return portPairGroups;
    }

    public void setPortPairGroups(List<String> portPairGroups) {
        this.portPairGroups = portPairGroups;
    }

    @Override
    public Map<String, String> getChainParameters() {
        return chainParameters;
    }

    public void setChainParameters(Map<String, String> chainParameters) {
        this.chainParameters = chainParameters;
    }

    @Override
    public PortChainBuilder toBuilder() {
        return new PortChainConcreteBuilder(this);
    }

    public static class PortChains extends ListResult<NeutronPortChain> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("port_chains")
        private List<NeutronPortChain> portChains;

        public List<NeutronPortChain> value() {
            return portChains;
        }
    }

    public static class PortChainConcreteBuilder implements PortChainBuilder {

        private NeutronPortChain neutronPortChain;

        public PortChainConcreteBuilder() {
            this.neutronPortChain = new NeutronPortChain();
        }

        public PortChainConcreteBuilder(NeutronPortChain neutronPortChain) {
            this.neutronPortChain = neutronPortChain;
        }

        public PortChainBuilder id(String id) {
            this.neutronPortChain.id = id;
            return this;
        }

        @Override
        public PortChainBuilder name(String name) {
            this.neutronPortChain.name = name;
            return this;
        }

        @Override
        public PortChainBuilder description(String description) {
            this.neutronPortChain.description = description;
            return this;
        }

        @Override
        public PortChainBuilder projectId(String projectId) {
            this.neutronPortChain.projectId = projectId;
            return this;
        }

        @Override
        public PortChainBuilder chainId(String chainId) {
            this.neutronPortChain.chainId = chainId;
            return this;
        }

        @Override
        public PortChainBuilder flowClassifiers(List<String> flowClassifiers) {
            this.neutronPortChain.flowClassifiers = flowClassifiers;
            return this;
        }

        @Override
        public PortChainBuilder portPairGroups(List<String> portPairGroups) {
            this.neutronPortChain.portPairGroups = portPairGroups;
            return this;
        }

        @Override
        public PortChainBuilder chainParameters(Map<String, String> chainParameters) {
            this.neutronPortChain.chainParameters = chainParameters;
            return this;
        }

        @Override
        public PortChain build() {
            return this.neutronPortChain;
        }

        @Override
        public PortChainBuilder from(PortChain in) {
            this.neutronPortChain = (NeutronPortChain) in;
            return this;
        }
    }
}
