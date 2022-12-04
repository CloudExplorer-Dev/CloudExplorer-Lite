package org.openstack4j.model.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The IPv6 address modes specifies mechanisms for assigning IP addresses
 *
 * @author Taemin
 */
public enum Ipv6AddressMode {
    SLAAC("slaac"),
    DHCPV6_STATEFUL("dhcpv6-stateful"),
    DHCPV6_STATELESS("dhcpv6-stateless"),
    NULL("null");

    private final String ipv6AddressMode;

    private Ipv6AddressMode(String ipv6AddressMode) {
        this.ipv6AddressMode = ipv6AddressMode;
    }

    @JsonCreator
    public static Ipv6AddressMode forValue(String value) {
        if (value != null) {
            for (Ipv6AddressMode s : Ipv6AddressMode.values()) {
                if (s.ipv6AddressMode.equalsIgnoreCase(value))
                    return s;
            }
        }
        return null;
    }

    @JsonValue
    public String getIpv6AddressMode() {
        return ipv6AddressMode;
    }
}
