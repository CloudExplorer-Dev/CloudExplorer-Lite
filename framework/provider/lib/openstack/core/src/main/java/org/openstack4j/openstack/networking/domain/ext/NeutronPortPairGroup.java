package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.PortPairGroup;
import org.openstack4j.model.network.ext.builder.PortPairGroupBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Gerenrot
 */
@JsonRootName("port_pair_group")
public class NeutronPortPairGroup implements PortPairGroup {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty
    private String description;

    @JsonProperty("port_pairs")
    private List<String> portPairs;

    @JsonProperty("port_pair_group_parameters")
    private Map<String, Object> portPairGroupParameters;

    public static PortPairGroupBuilder builder() {
        return new PortPairGroupConcreteBuilder();
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

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getPortPairs() {
        return portPairs;
    }

    public void setPortPairs(List<String> portPairs) {
        this.portPairs = portPairs;
    }

    @Override
    public Map<String, Object> getPortPairGroupParameters() {
        return portPairGroupParameters;
    }

    public void setPortPairGroupParameters(Map<String, Object> portPairGroupParameters) {
        this.portPairGroupParameters = portPairGroupParameters;
    }

    @Override
    public PortPairGroupBuilder toBuilder() {
        return new PortPairGroupConcreteBuilder(this);
    }

    public static class PortPairGroups extends ListResult<NeutronPortPairGroup> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("port_pair_groups")
        private List<NeutronPortPairGroup> portPairGroups;

        public List<NeutronPortPairGroup> value() {
            return portPairGroups;
        }
    }

    public static class PortPairGroupConcreteBuilder implements PortPairGroupBuilder {

        private NeutronPortPairGroup neutronPortPairGroup;

        public PortPairGroupConcreteBuilder() {
            this.neutronPortPairGroup = new NeutronPortPairGroup();
        }

        public PortPairGroupConcreteBuilder(NeutronPortPairGroup neutronPortPairGroup) {
            this.neutronPortPairGroup = neutronPortPairGroup;
        }

        @Override
        public PortPairGroup build() {
            return neutronPortPairGroup;
        }

        @Override
        public PortPairGroupBuilder from(PortPairGroup in) {
            this.neutronPortPairGroup = (NeutronPortPairGroup) in;
            return this;
        }

        @Override
        public PortPairGroupBuilder id(String id) {
            this.neutronPortPairGroup.id = id;
            return this;
        }

        @Override
        public PortPairGroupBuilder name(String name) {
            this.neutronPortPairGroup.name = name;
            return this;
        }

        @Override
        public PortPairGroupBuilder projectId(String projectId) {
            this.neutronPortPairGroup.projectId = projectId;
            return this;
        }

        @Override
        public PortPairGroupBuilder description(String description) {
            this.neutronPortPairGroup.description = description;
            return this;
        }

        @Override
        public PortPairGroupBuilder portPairs(List<String> portPairs) {
            this.neutronPortPairGroup.portPairs = portPairs;
            return this;
        }

        @Override
        public PortPairGroupBuilder portPairGroupParameters(Map<String, Object> portPairGroupParameters) {
            this.neutronPortPairGroup.portPairGroupParameters = portPairGroupParameters;
            return this;
        }
    }
}
