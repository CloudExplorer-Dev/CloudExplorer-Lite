package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.InterfaceAttachment;
import org.openstack4j.model.compute.PortState;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonRootName("interfaceAttachment")
public class NovaInterfaceAttachment implements InterfaceAttachment {

    private static final long serialVersionUID = 1L;

    @JsonProperty("fixed_ips")
    private List<NovaFixedIp> fixedIps;
    @JsonProperty("mac_addr")
    private String macAddr;
    @JsonProperty("net_id")
    private String netId;
    @JsonProperty("port_id")
    private String portId;
    @JsonProperty("port_state")
    private PortState portState;

    public NovaInterfaceAttachment() {
    }

    public NovaInterfaceAttachment(String portId) {
        this.portId = portId;
    }

    @Override
    public List<? extends FixedIp> getFixedIps() {
        return fixedIps;
    }

    @Override
    public String getMacAddr() {
        return macAddr;
    }

    @Override
    public String getNetId() {
        return netId;
    }

    @Override
    public String getPortId() {
        return portId;
    }

    @Override
    public PortState getPortState() {
        return portState;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("mac_addr", macAddr).add("net_id", netId).add("port_id", portId)
                .add("port_state", portState).add("fixed_ips", fixedIps)
                .toString();
    }

    public static class NovaInterfaceAttachments extends ListResult<NovaInterfaceAttachment> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("interfaceAttachments")
        private List<NovaInterfaceAttachment> attachments;

        @Override
        protected List<NovaInterfaceAttachment> value() {
            return attachments;
        }

    }

    public static class NovaFixedIp implements FixedIp {

        private static final long serialVersionUID = 1L;

        @JsonProperty("ip_address")
        private String ipAddress;
        @JsonProperty("subnet_id")
        private String subnetId;


        @Override
        public String getIpAddress() {
            return ipAddress;
        }

        @Override
        public String getSubnetId() {
            return subnetId;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("ip_address", ipAddress).add("subnet_id", subnetId).toString();
        }
    }

}
