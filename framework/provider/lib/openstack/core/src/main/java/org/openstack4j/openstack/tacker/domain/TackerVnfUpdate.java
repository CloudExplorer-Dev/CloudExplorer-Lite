package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.tacker.VnfUpdate;
import org.openstack4j.model.tacker.builder.VnfUpdateBuilder;

/**
 * An entity used to update Tacker Vnf.
 *
 * @author Vishvesh Deshmukh
 * @date Aug 17, 2016
 */
@JsonRootName("vnf")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TackerVnfUpdate implements VnfUpdate {

    private static final long serialVersionUID = 1L;

    private VnfUpdateAttributes attributes;

    /**
     * @return VnfUpdateBuilder
     */
    public static VnfUpdateBuilder builder() {
        return new VnfUpdateConcreteBuilder();
    }

    /**
     * Wrap this VnfUpdate to a builder
     *
     * @return VnfUpdateBuilder
     */
    @Override
    public VnfUpdateBuilder toBuilder() {
        return new VnfUpdateConcreteBuilder(this);
    }

    @Override
    public VnfUpdateAttributes getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("attributes", attributes).toString();
    }

    public static class VnfUpdateConcreteBuilder implements VnfUpdateBuilder {
        TackerVnfUpdate f;

        public VnfUpdateConcreteBuilder() {
            this(new TackerVnfUpdate());
        }

        public VnfUpdateConcreteBuilder(TackerVnfUpdate f) {
            this.f = f;
        }

        @Override
        public TackerVnfUpdate build() {
            return f;
        }

        @Override
        public VnfUpdateBuilder from(VnfUpdate in) {
            this.f = (TackerVnfUpdate) in;
            return this;
        }

        @Override
        public VnfUpdateBuilder attributes(VnfUpdateAttributes attributes) {
            f.attributes = attributes;
            return this;
        }
    }
}
