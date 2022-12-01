package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.tacker.Vnf;
import org.openstack4j.model.tacker.builder.VnfBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
@JsonRootName("vnf")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TackerVnf implements Vnf {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String description;

    private VnfAttributes attributes;

    private TackerVnfStatus status;

    @JsonProperty("mgmt_url")
    private String managementUrl;

    @JsonProperty("vnfd_id")
    private String vnfdId;

    @JsonProperty("error_reason")
    private String errorReason;

    @JsonProperty("vim_id")
    private String vimId;

    @JsonProperty("instance_id")
    private String instanceId;

    @JsonProperty("placement_attr")
    private VnfPlacementAttribute placementAttribute;

    /**
     * @return VnfBuilder
     */
    public static VnfBuilder builder() {
        return new VnfConcreteBuilder();
    }

    /**
     * Wrap this TackerVnf to a builder
     *
     * @return VnfBuilder
     */
    @Override
    public VnfBuilder toBuilder() {
        return new VnfConcreteBuilder(this);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @return the status
     */
    public TackerVnfStatus getStatus() {
        return status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the attributes
     */
    public VnfAttributes getAttributes() {
        return attributes;
    }

    /**
     * @return the managementUrl
     */
    public String getManagementUrl() {
        return managementUrl;
    }

    /**
     * @return the vnfdId
     */
    public String getVnfdId() {
        return vnfdId;
    }

    /**
     * @return the errorReason
     */
    public String getErrorReason() {
        return errorReason;
    }

    /**
     * @return the vimId
     */
    public String getVimId() {
        return vimId;
    }

    /**
     * @return the instanceId
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @return the placementAttribute
     */
    public VnfPlacementAttribute getPlacementAttribute() {
        return placementAttribute;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("tenantId", tenantId)
                .add("description", description).add("attributes", attributes).add("status", status)
                .add("managementUrl", managementUrl).add("vnfdId", vnfdId).add("errorReason", errorReason)
                .add("vimId", vimId).add("instanceId", instanceId).add("placementAttribute", placementAttribute)
                .toString();
    }

    public static class TackerVnfs extends ListResult<TackerVnf> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("vnfs")
        List<TackerVnf> vnfs;

        @Override
        public List<TackerVnf> value() {
            return vnfs;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("vnfs", vnfs).toString();
        }
    }

    public static class VnfConcreteBuilder implements VnfBuilder {

        TackerVnf vnf;

        public VnfConcreteBuilder() {
            this(new TackerVnf());
        }

        public VnfConcreteBuilder(TackerVnf f) {
            this.vnf = f;
        }

        @Override
        public Vnf build() {
            return vnf;
        }

        @Override
        public VnfBuilder from(Vnf in) {
            this.vnf = (TackerVnf) in;
            return this;
        }

        @Override
        public VnfBuilder tenantId(String tenantId) {
            vnf.tenantId = tenantId;
            return this;
        }

        @Override
        public VnfBuilder name(String name) {
            vnf.name = name;
            return this;
        }

        @Override
        public VnfBuilder description(String description) {
            vnf.description = description;
            return this;
        }

        @Override
        public VnfBuilder attributes(VnfAttributes attributes) {
            vnf.attributes = attributes;
            return this;
        }

        @Override
        public VnfBuilder vnfdId(String vnfdId) {
            vnf.vnfdId = vnfdId;
            return this;
        }

        @Override
        public VnfBuilder vimId(String vimId) {
            vnf.vimId = vimId;
            return this;
        }

    }

}
