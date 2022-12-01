package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.network.ext.PortForwarding;
import org.openstack4j.model.network.ext.builder.PortForwardingBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Objects;

/**
 * A floating IP port forwarding.
 *
 * @author zjluo
 */
@JsonRootName("port_forwarding")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FloatingIPPortForwarding implements PortForwarding {
    private static final long serialVersionUID = 1L;

    /**
     * The id of floating IP port forwarding.
     */
    private String id;

    /**
     * The IP protocol used in the floating IP port forwarding.
     */
    private String protocol;

    /**
     * The fixed IPv4 address of the Neutron port associated to the floating IP port forwarding.
     */
    @JsonProperty("internal_ip_address")
    private String internalIpAddress;

    /**
     * The TCP/UDP/other protocol port number of the Neutron port fixed IP address associated to the floating ip port forwarding.
     */
    @JsonProperty("internal_port")
    private int internalPort;

    /**
     * The ID of the Neutron port associated to the floating IP port forwarding.
     */
    @JsonProperty("internal_port_id")
    private String internalPortId;

    /**
     * The TCP/UDP/other protocol port number of the port forwarding’s floating IP address.
     */
    @JsonProperty("external_port")
    private int externalPort;

    /**
     * A text describing the rule, which helps users to manage/find easily theirs rules.
     */
    @JsonProperty("description")
    private String description;

    public static PortForwardingBuilder builder() {
        return new PortForwardingConcreteBuilder();
    }

    @Override
    public PortForwardingBuilder toBuilder() {
        return new PortForwardingConcreteBuilder(this);
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
    public String getProtocol() {
        return protocol;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInternalIpAddress() {
        return internalIpAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInternalPort() {
        return internalPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInternalPortId() {
        return internalPortId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExternalPort() {
        return externalPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "NeutronPortForwarding{" +
                "id='" + id + '\'' +
                ", protocol='" + protocol + '\'' +
                ", internalIpAddress='" + internalIpAddress + '\'' +
                ", internalPort=" + internalPort +
                ", internalPortId='" + internalPortId + '\'' +
                ", externalPort=" + externalPort +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FloatingIPPortForwarding that = (FloatingIPPortForwarding) o;
        return internalPort == that.internalPort &&
                externalPort == that.externalPort &&
                Objects.equals(id, that.id) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(internalIpAddress, that.internalIpAddress) &&
                Objects.equals(internalPortId, that.internalPortId) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, protocol, internalIpAddress, internalPort, internalPortId, externalPort, description);
    }

    public static class PortForwardings extends ListResult<FloatingIPPortForwarding> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("port_forwardings")
        private List<FloatingIPPortForwarding> portForwardings;

        @Override
        protected List<FloatingIPPortForwarding> value() {
            return portForwardings;
        }
    }

    public static class PortForwardingConcreteBuilder implements PortForwardingBuilder {
        FloatingIPPortForwarding f;

        public PortForwardingConcreteBuilder() {
            this(new FloatingIPPortForwarding());
        }

        public PortForwardingConcreteBuilder(FloatingIPPortForwarding f) {
            this.f = f;
        }

        @Override
        public FloatingIPPortForwarding build() {
            return f;
        }

        @Override
        public PortForwardingConcreteBuilder from(PortForwarding in) {
            this.f = (FloatingIPPortForwarding) in;
            return this;
        }

        public PortForwardingConcreteBuilder id(String id) {
            f.id = id;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder description(String description) {
            f.description = description;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder protocol(String protocol) {
            f.protocol = protocol;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder internalIpAddress(String internalIpAddress) {
            f.internalIpAddress = internalIpAddress;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder internalPort(int internalPort) {
            f.internalPort = internalPort;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder internalPortId(String internalPortId) {
            f.internalPortId = internalPortId;
            return this;
        }

        @Override
        public PortForwardingConcreteBuilder externalPort(int externalPort) {
            f.externalPort = externalPort;
            return this;
        }
    }
}
