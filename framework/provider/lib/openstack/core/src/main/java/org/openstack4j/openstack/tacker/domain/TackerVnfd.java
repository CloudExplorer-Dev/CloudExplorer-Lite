package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.tacker.Vnfd;
import org.openstack4j.model.tacker.builder.VnfdBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
@JsonRootName("vnfd")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TackerVnfd implements Vnfd {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String description;

    @JsonProperty("mgmt_driver")
    private String managementDriver;

    @JsonProperty("infra_driver")
    private String infrastructureDriver;

    private VnfdAttributes attributes;

    @JsonProperty("service_types")
    private List<VnfdServiceTypes> serviceTypes;

    /**
     * @return VnfdBuilder
     */
    public static VnfdBuilder builder() {
        return new VnfdConcreteBuilder();
    }

    /**
     * Wrap this TackerVnfd to a builder
     *
     * @return VnfdBuilder
     */
    @Override
    public VnfdBuilder toBuilder() {
        return new VnfdConcreteBuilder(this);
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the managementDriver
     */
    public String getManagementDriver() {
        return managementDriver;
    }

    /**
     * @return the infrastructureDriver
     */
    public String getInfrastructureDriver() {
        return infrastructureDriver;
    }

    /**
     * @return the attributes
     */
    public VnfdAttributes getAttributes() {
        return attributes;
    }

    /**
     * @return the serviceTypes
     */
    public List<VnfdServiceTypes> getServiceTypes() {
        return serviceTypes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name)
                .add("tenantId", tenantId).add("description", description)
                .add("serviceTypes", serviceTypes).add("attributes", attributes)
                .add("managementDriver", managementDriver).add("infrastructureDriver", infrastructureDriver)
                .toString();
    }

    public static class TackerVnfds extends ListResult<TackerVnfd> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("vnfds")
        List<TackerVnfd> vnfds;

        @Override
        public List<TackerVnfd> value() {
            return vnfds;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("vnfds", vnfds).toString();
        }
    }

    public static class VnfdConcreteBuilder implements VnfdBuilder {

        TackerVnfd vnfd;

        public VnfdConcreteBuilder() {
            this(new TackerVnfd());
        }

        public VnfdConcreteBuilder(TackerVnfd f) {
            this.vnfd = f;
        }

        @Override
        public Vnfd build() {
            return vnfd;
        }

        @Override
        public VnfdBuilder from(Vnfd in) {
            this.vnfd = (TackerVnfd) in;
            return this;
        }

        @Override
        public VnfdBuilder tenantId(String tenantId) {
            vnfd.tenantId = tenantId;
            return this;
        }

        @Override
        public VnfdBuilder name(String name) {
            vnfd.name = name;
            return this;
        }

        @Override
        public VnfdBuilder description(String description) {
            vnfd.description = description;
            return this;
        }

        @Override
        public VnfdBuilder managementDriver(String managementDriver) {
            vnfd.managementDriver = managementDriver;
            return this;
        }

        @Override
        public VnfdBuilder infrastructureDriver(String infrastructureDriver) {
            vnfd.infrastructureDriver = infrastructureDriver;
            return this;
        }

        @Override
        public VnfdBuilder attributes(VnfdAttributes attributes) {
            vnfd.attributes = attributes;
            return this;
        }

        @Override
        public VnfdBuilder serviceTypes(List<VnfdServiceTypes> serviceTypes) {
            vnfd.serviceTypes = serviceTypes;
            return this;
        }

    }

}
