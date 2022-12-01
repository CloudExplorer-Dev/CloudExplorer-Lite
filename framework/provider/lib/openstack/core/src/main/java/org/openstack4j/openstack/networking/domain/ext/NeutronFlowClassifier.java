package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.Ethertype;
import org.openstack4j.model.network.ext.FlowClassifier;
import org.openstack4j.model.network.ext.builder.FlowClassifierBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Gerenrot
 */
@JsonRootName("flow_classifier")
public class NeutronFlowClassifier implements FlowClassifier {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty
    private String description;

    @JsonProperty
    private String protocol;

    @JsonProperty("source_port_range_min")
    private Integer sourcePortRangeMin;

    @JsonProperty("source_port_range_max")
    private Integer sourcePortRangeMax;

    @JsonProperty("destination_port_range_min")
    private Integer destinationPortRangeMin;

    @JsonProperty("destination_port_range_max")
    private Integer destinationPortRangeMax;

    @JsonProperty("source_ip_prefix")
    private String sourceIpPrefix;

    @JsonProperty("destination_ip_prefix")
    private String destinationIpPrefix;

    @JsonProperty("logical_source_port")
    private String logicalSourcePort;

    @JsonProperty("logical_destination_port")
    private String logicalDestinationPort;

    @JsonProperty("l7_parameters")
    private Map<String, String> l7Parameters;

    private Ethertype ethertype;

    public static FlowClassifierBuilder builder() {
        return new FlowClassifierConcreteBuilder();
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
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Integer getSourcePortRangeMin() {
        return sourcePortRangeMin;
    }

    @Override
    public Integer getSourcePortRangeMax() {
        return sourcePortRangeMax;
    }

    @Override
    public Integer getDestinationPortRangeMin() {
        return destinationPortRangeMin;
    }

    @Override
    public Integer getDestinationPortRangeMax() {
        return destinationPortRangeMax;
    }

    @Override
    public String getSourceIpPrefix() {
        return sourceIpPrefix;
    }

    @Override
    public String getDestinationIpPrefix() {
        return destinationIpPrefix;
    }

    @Override
    public String getLogicalSourcePort() {
        return logicalSourcePort;
    }

    @Override
    public String getLogicalDestinationPort() {
        return logicalDestinationPort;
    }

    @Override
    public Map<String, String> getL7Parameters() {
        return l7Parameters;
    }

    @Override
    public Ethertype getEthertype() {
        return ethertype;
    }

    public FlowClassifierBuilder toBuilder() {
        return new FlowClassifierConcreteBuilder(this);
    }

    public static class FlowClassifiers extends ListResult<NeutronFlowClassifier> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("flow_classifiers")
        private List<NeutronFlowClassifier> flowClassifiers;

        public List<NeutronFlowClassifier> value() {
            return flowClassifiers;
        }
    }

    public static class FlowClassifierConcreteBuilder implements FlowClassifierBuilder {

        private NeutronFlowClassifier neutronFlowClassifier;

        public FlowClassifierConcreteBuilder() {
            this.neutronFlowClassifier = new NeutronFlowClassifier();
        }

        public FlowClassifierConcreteBuilder(NeutronFlowClassifier m) {
            this.neutronFlowClassifier = m;
        }

        public FlowClassifierBuilder id(String id) {
            neutronFlowClassifier.id = id;
            return this;
        }

        @Override
        public FlowClassifierBuilder name(String name) {
            neutronFlowClassifier.name = name;
            return this;
        }

        @Override
        public FlowClassifierBuilder projectId(String projectId) {
            neutronFlowClassifier.projectId = projectId;
            return this;
        }

        @Override
        public FlowClassifierBuilder description(String description) {
            neutronFlowClassifier.description = description;
            return this;
        }

        @Override
        public FlowClassifierBuilder protocol(String protocol) {
            neutronFlowClassifier.protocol = protocol;
            return this;
        }

        @Override
        public FlowClassifierBuilder sourcePortRangeMin(Integer sourcePortRangeMin) {
            neutronFlowClassifier.sourcePortRangeMin = sourcePortRangeMin;
            return this;
        }

        @Override
        public FlowClassifierBuilder sourcePortRangeMax(Integer sourcePortRangeMax) {
            neutronFlowClassifier.sourcePortRangeMax = sourcePortRangeMax;
            return this;
        }


        @Override
        public FlowClassifierBuilder destinationPortRangeMin(Integer destinationPortRangeMin) {
            neutronFlowClassifier.destinationPortRangeMin = destinationPortRangeMin;
            return this;
        }

        @Override
        public FlowClassifierBuilder destinationPortRangeMax(Integer destinationPortRangeMax) {
            neutronFlowClassifier.destinationPortRangeMax = destinationPortRangeMax;
            return this;
        }

        @Override
        public FlowClassifierBuilder sourceIpPrefix(String sourceIpPrefix) {
            neutronFlowClassifier.sourceIpPrefix = sourceIpPrefix;
            return this;
        }

        @Override
        public FlowClassifierBuilder destinationIpPrefix(String destinationIpPrefix) {
            neutronFlowClassifier.destinationIpPrefix = destinationIpPrefix;
            return this;
        }

        @Override
        public FlowClassifierBuilder logicalSourcePort(String logicalSourcePort) {
            neutronFlowClassifier.logicalSourcePort = logicalSourcePort;
            return this;
        }

        @Override
        public FlowClassifierBuilder logicalDestinationPort(String logicalDestinationPort) {
            neutronFlowClassifier.logicalDestinationPort = logicalDestinationPort;
            return this;
        }

        @Override
        public FlowClassifierBuilder l7Parameters(Map<String, String> l7Parameters) {
            neutronFlowClassifier.l7Parameters = l7Parameters;
            return this;
        }

        @Override
        public FlowClassifierBuilder ethertype(Ethertype ethertype) {
            neutronFlowClassifier.ethertype = ethertype;
            return this;
        }

        @Override
        public FlowClassifier build() {
            return neutronFlowClassifier;
        }

        @Override
        public FlowClassifierBuilder from(FlowClassifier in) {
            neutronFlowClassifier = (NeutronFlowClassifier) in;
            return this;
        }
    }
}
