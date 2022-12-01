package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.builder.ResourceBuilder;
import org.openstack4j.model.network.TrunkSubport;
import org.openstack4j.model.network.builder.TrunkSubportBuilder;
import org.openstack4j.openstack.common.ListEntity;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Objects;

/**
 * A Subport ONLY used for adding to trunks
 *
 * @author Kashyap Jha
 */
@JsonRootName("sub_port")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronTrunkSubport implements TrunkSubport, ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("port_id")
    private String portId;
    @JsonProperty("segmentation_id")
    private int segmentationId;
    @JsonProperty("segmentation_type")
    private String segmentationType;

    public NeutronTrunkSubport() {
    }

    public NeutronTrunkSubport(String portId) {
        this.portId = portId;
    }

    public static TrunkSubportBuilder builder() {
        return new TrunkSubportConcreteBuilder();
    }

    public static NeutronTrunkSubport fromTrunkSubport(TrunkSubport trunkSubport) {
        NeutronTrunkSubport toCreate = new NeutronTrunkSubport();
        toCreate.portId = trunkSubport.getPortId();
        toCreate.segmentationId = trunkSubport.getSegmentationId();
        toCreate.segmentationType = trunkSubport.getSegmentationType();
        return toCreate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof NeutronTrunkSubport) {
            NeutronTrunkSubport that = (NeutronTrunkSubport) obj;
            if (Objects.equals(portId, that.portId) && Objects.equals(segmentationId, that.segmentationId)
                    && Objects.equals(segmentationType, that.segmentationType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Subports don't have the ID attribute. Use {@link this.getPortId} instead
     */
    @Override
    @Deprecated
    public String getId() {
        return portId;
    }

    @Override
    @Deprecated
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Subports don't have the name attribute
     */
    @Override
    @Deprecated
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void setName(String name) {
        throw new UnsupportedOperationException();

    }

    @Override
    public String getPortId() {
        return portId;
    }

    @Override
    public int getSegmentationId() {
        return segmentationId;
    }

    @Override
    public String getSegmentationType() {
        return segmentationType;
    }

    /**
     * Subports don't have the tenantId attrbute
     */
    @Override
    @Deprecated
    public String getTenantId() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void setTenantId(String tenantId) {
        throw new UnsupportedOperationException();

    }

    @Override
    public int hashCode() {
        return Objects.hash(segmentationId, portId, segmentationType);
    }

    @Override
    public TrunkSubportBuilder toBuilder() {
        return new TrunkSubportConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("segmentationId", segmentationId)
                .add("portId", portId).add("segmentationType", segmentationType).toString();

    }

    public static class NeutronTrunkSubports implements ModelEntity {

        private static final long serialVersionUID = 1L;
        @JsonProperty("sub_ports")
        private ListEntity<NeutronTrunkSubport> trunkSubports;

        public NeutronTrunkSubports() {
            trunkSubports = new ListEntity<>();
        }

        public static NeutronTrunkSubports fromTrunkSubports(List<? extends TrunkSubport> trunkSubports) {
            NeutronTrunkSubports toCreate = new NeutronTrunkSubports();
            for (TrunkSubport trunkSubport : trunkSubports) {
                toCreate.trunkSubports.add(NeutronTrunkSubport.fromTrunkSubport(trunkSubport));
            }
            return toCreate;
        }
    }

    public static class TrunkSubportConcreteBuilder extends ResourceBuilder<TrunkSubport, TrunkSubportConcreteBuilder>
            implements TrunkSubportBuilder {

        private NeutronTrunkSubport reference;

        TrunkSubportConcreteBuilder() {
            this(new NeutronTrunkSubport());
        }

        TrunkSubportConcreteBuilder(NeutronTrunkSubport trunkSubport) {
            this.reference = trunkSubport;
        }

        @Override
        public TrunkSubport build() {
            return reference;
        }

        @Override
        public TrunkSubportBuilder from(TrunkSubport in) {
            reference = (NeutronTrunkSubport) in;
            return this;
        }

        @Override
        public TrunkSubportBuilder portId(String portId) {
            reference.portId = portId;
            return this;
        }

        @Override
        protected TrunkSubport reference() {
            return reference;
        }

        @Override
        public TrunkSubportBuilder segmentationId(int segmentationId) {
            reference.segmentationId = segmentationId;
            return this;
        }

        @Override
        public TrunkSubportBuilder segmentationType(String segmentationType) {
            reference.segmentationType = segmentationType;
            return this;
        }
    }

    public static class TrunkSubports extends ListResult<NeutronTrunkSubport> {
        private static final long serialVersionUID = 1L;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonProperty("sub_ports")
        private List<NeutronTrunkSubport> trunkSubports;

        @Override
        protected List<NeutronTrunkSubport> value() {
            return trunkSubports;
        }
    }
}
